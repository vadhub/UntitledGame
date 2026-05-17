package src.view.background;

import src.engine.GameObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

// Башня с поддержкой деградации от урона: STONE → WOODEN → RUINED
public class Tower extends GameObject {

    // Перечисление типов башен
    public enum TowerType {
        STONE(200, 140, 95, new Color(140, 120, 100)),
        WOODEN(150, 100, 60, new Color(160, 130, 80)),
        RUINED(70, 90, 50, new Color(100, 85, 70));

        private final int maxHealth;
        private final int baseWidth;
        private final int topWidth;
        private final Color color;

        TowerType(int maxHealth, int baseWidth, int topWidth, Color color) {
            this.maxHealth = maxHealth;
            this.baseWidth = baseWidth;
            this.topWidth = topWidth;
            this.color = color;
        }

        public int getMaxHealth() { return maxHealth; }
        public int getBaseWidth() { return baseWidth; }
        public int getTopWidth() { return topWidth; }
        public Color getColor() { return color; }
    }

    // Параметры башни
    private TowerType towerType;
    private float xOffset;
    private float relativeX = 0.1f; // доля от ширины панели
    private float degradeThresholdStoneToWooden;
    private float degradeThresholdWoodenToRuined;

    // Пороги деградации (в процентах от максимального здоровья)
    private static final float DEFAULT_DEGRADE_THRESHOLD_1 = 0.5f;
    private static final float DEFAULT_DEGRADE_THRESHOLD_2 = 0.2f;

    // Константы для отрисовки
    private static final int TOWER_HEIGHT = 360;
    private static final int WINDOW_HEIGHT_OFFSET = 150;
    private static final int DOOR_WIDTH = 40;
    private static final int DOOR_HEIGHT = 60;
    private static final int FLAG_POLE_HEIGHT = 40;
    private static final int BATTLEMENT_COUNT = 6;
    private static final int BATTLEMENT_GAP = 5;

    // ===== ДОБАВЛЕНО ДЛЯ АНИМАЦИИ РАЗРУШЕНИЯ =====
    private boolean isDying = false;
    private float deathTimer = 0f;
    private final float DEATH_DURATION = 1.5f;
    private ArrayList<Debris> debrisList = new ArrayList<>();
    private Random random = new Random();
    // ============================================

    // Конструктор с типом башни
    public Tower(int id, float x, float y, int size, TowerType type, float xOffset) {
        this(id, x, y, size, type, xOffset,
                DEFAULT_DEGRADE_THRESHOLD_1, DEFAULT_DEGRADE_THRESHOLD_2);
    }

    public Tower(int id, float x, float y, int size, TowerType type, float xOffset,
                 float degradeThresholdStoneToWooden, float degradeThresholdWoodenToRuined) {
        super(id, x, y, size, 0f);
        this.towerType = type;
        this.xOffset = xOffset;
        validateThresholds(degradeThresholdStoneToWooden, degradeThresholdWoodenToRuined);
        this.degradeThresholdStoneToWooden = degradeThresholdStoneToWooden;
        this.degradeThresholdWoodenToRuined = degradeThresholdWoodenToRuined;
        this.health = type.getMaxHealth();

        System.out.println("Tower created: " + type.name() + " with HP: " + type.getMaxHealth());
    }

    // Конструктор для обратной совместимости
    public Tower(int id, float x, float y, int size) {
        this(id, x, y, size, TowerType.STONE, 0);
    }

    private void validateThresholds(float degradeThresholdStoneToWooden, float degradeThresholdWoodenToRuined) {
        if (degradeThresholdStoneToWooden < 0f || degradeThresholdStoneToWooden > 1f) {
            throw new IllegalArgumentException("degradeThresholdStoneToWooden must be between 0 and 1");
        }
        if (degradeThresholdWoodenToRuined < 0f || degradeThresholdWoodenToRuined > 1f) {
            throw new IllegalArgumentException("degradeThresholdWoodenToRuined must be between 0 and 1");
        }
        if (degradeThresholdWoodenToRuined > degradeThresholdStoneToWooden) {
            throw new IllegalArgumentException("wooden-to-ruined threshold cannot exceed stone-to-wooden threshold");
        }
    }

    // Получение урона с деградацией
    @Override
    public void takeDamage(int damage) {
        if (!isAlive || isDying) return;

        int originalDamage = damage;
        int finalDamage = damage;

        // Критический урон для деревянных башен
        if (towerType == TowerType.WOODEN && damage > 30) {
            finalDamage = (int)(finalDamage * 1.3);
            System.out.println("🔥 Критический урон по деревянной башне!");
        }

        // Сниженный урон для каменных башен
        if (towerType == TowerType.STONE && damage < 20) {
            finalDamage = (int)(finalDamage * 0.7);
            System.out.println("🛡️ Каменная башня снизила урон");
        }

        // Разрушенная башня получает дополнительный урон
        if (towerType == TowerType.RUINED) {
            finalDamage = (int)(finalDamage * 1.2);
            System.out.println("💔 Разрушенная башня получила критический урон!");
        }

        health = Math.max(0, health - finalDamage);

        System.out.printf("%s башня получила %d урона (%d → %d). Осталось HP: %d/%d (%.0f%%)%n",
                towerType.name(), originalDamage, finalDamage, health,
                health, towerType.getMaxHealth(), (float) health / towerType.getMaxHealth() * 100);

        checkAndDegrade();

        if (health <= 0 && !isDying) {
            startDeathAnimation();
        }
    }

    // ===== ДОБАВЛЕНА АНИМАЦИЯ РАЗРУШЕНИЯ =====
    private void startDeathAnimation() {
        isDying = true;
        isAlive = true;
        deathTimer = 0f;

        int centerX = (int)(x + xOffset);
        int baseY = (int)y;
        for (int i = 0; i < 40; i++) {
            debrisList.add(new Debris(
                    centerX - towerType.getBaseWidth()/2 + random.nextInt(towerType.getBaseWidth()),
                    baseY - random.nextInt(TOWER_HEIGHT)
            ));
        }
        System.out.println("💀 Башня разрушается!");
    }
    // =======================================

    // Проверка и выполнение деградации на основе текущего здоровья
    private void checkAndDegrade() {
        boolean degraded;
        do {
            degraded = false;
            float healthPercent = getCurrentHealthPercent();

            if (towerType == TowerType.STONE && healthPercent <= degradeThresholdStoneToWooden) {
                degradeTo(TowerType.WOODEN);
                degraded = true;
            } else if (towerType == TowerType.WOODEN && healthPercent <= degradeThresholdWoodenToRuined) {
                degradeTo(TowerType.RUINED);
                degraded = true;
            }
        } while (degraded && isAlive && !isDying);
    }

    // Метод деградации башни до указанного типа
    private void degradeTo(TowerType newType) {
        if (!isAlive || isDying) return;
        if (towerType == newType) return;

        TowerType oldType = towerType;
        float healthPercent = getCurrentHealthPercent();

        towerType = newType;
        health = Math.max(1, (int)(towerType.getMaxHealth() * healthPercent));

        System.out.println("⚠️ БАШНЯ ДЕГРАДИРОВАЛА: " + oldType.name() + " → " + towerType.name());
    }

    @Override
    public void destroy() {
        if (isDying) return;
        startDeathAnimation();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isDying) {
            deathTimer += deltaTime;
            for (Debris d : debrisList) {
                d.update(deltaTime);
            }
            if (deathTimer >= DEATH_DURATION) {
                isAlive = false;
                super.destroy();
            }
        }
    }

    // Геттеры
    public int getCurrentHealth() { return health; }
    public int getMaxHealth() { return towerType.getMaxHealth(); }
    public TowerType getTowerType() { return towerType; }
    public float getXOffset() { return xOffset; }
    public void setXOffset(float xOffset) { this.xOffset = xOffset; }
    public float getRelativeX() { return relativeX; }
    public void setRelativeX(float relX) { this.relativeX = relX; }

    public float getDegradeThreshold1() { return degradeThresholdStoneToWooden; }
    public float getDegradeThreshold2() { return degradeThresholdWoodenToRuined; }
    public float getCurrentHealthPercent() { return (float) health / towerType.getMaxHealth(); }

    public Color getHealthColor() {
        float percent = getCurrentHealthPercent();
        if (percent >= 0.75f) return Color.GREEN;
        if (percent >= 0.5f) return Color.YELLOW;
        if (percent >= 0.25f) return Color.ORANGE;
        return Color.RED;
    }

    // Отрисовка башни
    @Override
    public void draw(Graphics g) {
        if (!isAlive && !isDying) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));

        if (isDying) {
            for (Debris d : debrisList) {
                d.draw(g2d);
            }
            float alpha = 1f - (deathTimer / DEATH_DURATION);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            drawFullTower(g2d);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        } else {
            drawFullTower(g2d);
        }
    }

    private void drawFullTower(Graphics2D g2d) {
        int centerX = (int)(x + xOffset);
        int baseY = (int)(y);

        drawTowerBody(g2d, centerX, baseY);
        drawBattlements(g2d, centerX, baseY - TOWER_HEIGHT + 50, towerType.getTopWidth());
        drawWindow(g2d, centerX, baseY - TOWER_HEIGHT + WINDOW_HEIGHT_OFFSET);
        drawDoor(g2d, centerX, baseY);
        drawFlag(g2d, centerX, baseY - TOWER_HEIGHT);
        drawHealthBar(g2d, centerX, baseY);
    }

    private void drawTowerBody(Graphics2D g2d, int centerX, int baseY) {
        int baseWidth = towerType.getBaseWidth();
        int topWidth = towerType.getTopWidth();

        int[] towerX = {
                centerX - baseWidth/2,
                centerX + baseWidth/2,
                centerX + topWidth/2,
                centerX + topWidth/2,
                centerX + topWidth/2 - 10,
                centerX + topWidth/2 - 10,
                centerX,
                centerX - topWidth/2 + 10,
                centerX - topWidth/2 + 10,
                centerX - topWidth/2,
                centerX - topWidth/2
        };

        int[] towerY = {
                baseY,
                baseY,
                baseY - TOWER_HEIGHT + 100,
                baseY - TOWER_HEIGHT + 50,
                baseY - TOWER_HEIGHT + 50,
                baseY - TOWER_HEIGHT + 40,
                baseY - TOWER_HEIGHT,
                baseY - TOWER_HEIGHT + 40,
                baseY - TOWER_HEIGHT + 50,
                baseY - TOWER_HEIGHT + 50,
                baseY - TOWER_HEIGHT + 100
        };

        g2d.setColor(towerType.getColor());
        g2d.fillPolygon(towerX, towerY, 11);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(towerX, towerY, 11);
        addTexture(g2d, towerType, centerX, baseY);
    }

    private void addTexture(Graphics2D g2d, TowerType type, int centerX, int baseY) {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));

        switch (type) {
            case STONE:
                for (int i = 0; i < 5; i++) {
                    int y = baseY - 50 - i * 40;
                    g2d.drawLine(centerX - 50, y, centerX + 50, y);
                }
                break;
            case WOODEN:
                for (int i = 0; i < 8; i++) {
                    int y = baseY - 30 - i * 30;
                    g2d.drawLine(centerX - 45, y, centerX + 45, y);
                }
                break;
            case RUINED:
                g2d.setColor(Color.DARK_GRAY);
                g2d.drawLine(centerX - 30, baseY - 200, centerX, baseY - 150);
                g2d.drawLine(centerX, baseY - 150, centerX + 20, baseY - 100);
                break;
        }
    }

    private void drawBattlements(Graphics2D g2d, int centerX, int topY, int width) {
        int startX = centerX - width/2;
        int battlementWidth = width / BATTLEMENT_COUNT;

        g2d.setColor(new Color(100, 80, 60));

        for (int i = 0; i < BATTLEMENT_COUNT; i++) {
            int x = startX + i * battlementWidth;
            int battlementHeight = (towerType == TowerType.RUINED) ? 15 : 20;
            g2d.fillRect(x + BATTLEMENT_GAP/2, topY - battlementHeight,
                    battlementWidth - BATTLEMENT_GAP, battlementHeight);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x + BATTLEMENT_GAP/2, topY - battlementHeight,
                    battlementWidth - BATTLEMENT_GAP, battlementHeight);
            g2d.setColor(new Color(100, 80, 60));
        }
    }

    private void drawWindow(Graphics2D g2d, int centerX, int windowY) {
        Color windowColor = (towerType == TowerType.RUINED) ?
                new Color(50, 50, 50) : new Color(200, 230, 255);

        int[] windowX = {centerX - 15, centerX + 15, centerX + 15, centerX - 15};
        int[] windowYCoords = {windowY, windowY, windowY + 30, windowY + 30};

        g2d.setColor(windowColor);
        g2d.fillPolygon(windowX, windowYCoords, 4);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(windowX, windowYCoords, 4);
        g2d.drawLine(centerX, windowY, centerX, windowY + 30);
        g2d.drawLine(centerX - 15, windowY + 15, centerX + 15, windowY + 15);

        if (towerType == TowerType.RUINED) {
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawLine(centerX - 15, windowY, centerX + 15, windowY + 30);
        }
    }

    private void drawDoor(Graphics2D g2d, int centerX, int baseY) {
        int[] doorX = {centerX - DOOR_WIDTH/2, centerX + DOOR_WIDTH/2,
                centerX + DOOR_WIDTH/2, centerX - DOOR_WIDTH/2};
        int[] doorYCoords = {baseY - DOOR_HEIGHT, baseY - DOOR_HEIGHT, baseY, baseY};

        Color doorColor = (towerType == TowerType.WOODEN) ?
                new Color(120, 80, 40) : new Color(101, 67, 33);

        if (towerType == TowerType.RUINED) {
            doorColor = new Color(60, 40, 20);
        }

        g2d.setColor(doorColor);
        g2d.fillPolygon(doorX, doorYCoords, 4);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(doorX, doorYCoords, 4);

        g2d.setColor(Color.YELLOW);
        g2d.fillOval(centerX + DOOR_WIDTH/2 - 10, baseY - DOOR_HEIGHT/2, 6, 6);
    }

    private void drawFlag(Graphics2D g2d, int centerX, int flagBaseY) {
        int flagPoleTopY = flagBaseY - FLAG_POLE_HEIGHT;

        g2d.setColor(new Color(101, 67, 33));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(centerX, flagBaseY, centerX, flagPoleTopY);

        Color flagColor;
        if (!isAlive) {
            flagColor = Color.GRAY;
        } else if (health <= towerType.getMaxHealth() * 0.25f) {
            flagColor = Color.RED;
        } else if (health <= towerType.getMaxHealth() * 0.5f) {
            flagColor = Color.ORANGE;
        } else {
            flagColor = towerType == TowerType.STONE ? Color.BLUE :
                    (towerType == TowerType.WOODEN ? Color.GREEN : Color.DARK_GRAY);
        }

        int[] flagX = {centerX, centerX + 25, centerX};
        int[] flagY = {flagPoleTopY + 10, flagPoleTopY + 5, flagPoleTopY + 20};

        g2d.setColor(flagColor);
        g2d.fillPolygon(flagX, flagY, 3);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(flagX, flagY, 3);

        g2d.setColor(Color.YELLOW);
        g2d.fillOval(centerX - 4, flagPoleTopY - 4, 8, 8);
    }

    private void drawHealthBar(Graphics2D g2d, int centerX, int baseY) {
        int barWidth = 100;
        int barHeight = 12;
        int barX = centerX - barWidth/2;
        int barY = baseY - TOWER_HEIGHT - 20;

        g2d.setColor(Color.GRAY);
        g2d.fillRect(barX, barY, barWidth, barHeight);

        int healthWidth = (int)(getCurrentHealthPercent() * barWidth);
        g2d.setColor(getHealthColor());
        g2d.fillRect(barX, barY, healthWidth, barHeight);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(barX, barY, barWidth, barHeight);

        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        String healthText = health + "/" + towerType.getMaxHealth();
        FontMetrics fm = g2d.getFontMetrics();
        int textX = centerX - fm.stringWidth(healthText)/2;
        g2d.setColor(Color.WHITE);
        g2d.drawString(healthText, textX, barY + barHeight - 2);
    }

    // ===== ВНУТРЕННИЙ КЛАСС ДЛЯ ЧАСТИЦ =====
    private class Debris {
        float x, y;
        float vx, vy;
        float rotation, rotationSpeed;
        Color color;

        Debris(float x, float y) {
            this.x = x;
            this.y = y;
            this.vx = (random.nextFloat() - 0.5f) * 200f;
            this.vy = (random.nextFloat() - 0.5f) * 200f - 100f;
            this.rotation = random.nextFloat() * 360f;
            this.rotationSpeed = (random.nextFloat() - 0.5f) * 400f;
            this.color = new Color(100 + random.nextInt(60), 70 + random.nextInt(50), 40 + random.nextInt(40));
        }

        void update(float dt) {
            x += vx * dt;
            y += vy * dt;
            vy += 300f * dt;
            rotation += rotationSpeed * dt;
        }

        void draw(Graphics2D g) {
            int w = 4 + random.nextInt(8);
            int h = 4 + random.nextInt(8);
            g.rotate(Math.toRadians(rotation), x + w/2, y + h/2);
            g.setColor(color);
            g.fillRect((int)x, (int)y, w, h);
            g.setColor(Color.BLACK);
            g.drawRect((int)x, (int)y, w, h);
            g.rotate(-Math.toRadians(rotation), x + w/2, y + h/2);
        }
    }
}