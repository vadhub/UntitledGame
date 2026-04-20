import java.awt.*;
import java.util.Arrays;

// Башня с поддержкой деградации от урона: STONE → WOODEN → RUINED
public class Tower extends GameObject {

    // Перечисление типов башен
    public enum TowerType {
        STONE(200, 120, 80, new Color(140, 120, 100), "Каменная", 3),      // MAX уровень
        WOODEN(150, 100, 60, new Color(160, 130, 80), "Деревянная", 2),    // MID уровень
        RUINED(70, 90, 50, new Color(100, 85, 70), "Разрушенная", 1);       // LOW уровень

        private final int maxHealth;
        private final int baseWidth;
        private final int topWidth;
        private final Color color;
        private final String name;
        private final int level; // 3=каменная, 2=деревянная, 1=разрушенная

        TowerType(int maxHealth, int baseWidth, int topWidth, Color color, String name, int level) {
            this.maxHealth = maxHealth;
            this.baseWidth = baseWidth;
            this.topWidth = topWidth;
            this.color = color;
            this.name = name;
            this.level = level;
        }

        public int getMaxHealth() { return maxHealth; }
        public int getBaseWidth() { return baseWidth; }
        public int getTopWidth() { return topWidth; }
        public Color getColor() { return color; }
        public String getName() { return name; }
        public int getLevel() { return level; }

        // Получить следующий уровень деградации
        public TowerType getNextDegraded() {
            switch (this) {
                case STONE: return WOODEN;
                case WOODEN: return RUINED;
                default: return RUINED;
            }
        }
    }

    // Параметры башни
    private TowerType towerType;
    private int maxHealth;
    private int currentHealth;
    private float xOffset;
    private float degradeThresholdStoneToWooden;
    private float degradeThresholdWoodenToRuined;

    // Пороги деградации (в процентах от максимального здоровья)
    private static final float DEFAULT_DEGRADE_THRESHOLD_1 = 0.5f;  // При 50% HP: STONE → WOODEN
    private static final float DEFAULT_DEGRADE_THRESHOLD_2 = 0.2f;  // При 20% HP: WOODEN → RUINED

    // Константы для отрисовки
    private static final int TOWER_HEIGHT = 300;
    private static final int WINDOW_HEIGHT_OFFSET = 150;
    private static final int DOOR_WIDTH = 40;
    private static final int DOOR_HEIGHT = 60;
    private static final int FLAG_POLE_HEIGHT = 40;
    private static final int BATTLEMENT_COUNT = 6;
    private static final int BATTLEMENT_GAP = 5;

    // Конструктор с типом башни
    public Tower(int id, float x, float y, int size, float heal, TowerType type, float xOffset) {
        this(id, x, y, size, heal, type, xOffset,
                DEFAULT_DEGRADE_THRESHOLD_1, DEFAULT_DEGRADE_THRESHOLD_2);
    }

    public Tower(int id, float x, float y, int size, float heal, TowerType type, float xOffset,
                 float degradeThresholdStoneToWooden, float degradeThresholdWoodenToRuined) {
        super(id, x, y, size, heal);
        this.towerType = type;
        this.xOffset = xOffset;
        validateThresholds(degradeThresholdStoneToWooden, degradeThresholdWoodenToRuined);
        this.degradeThresholdStoneToWooden = degradeThresholdStoneToWooden;
        this.degradeThresholdWoodenToRuined = degradeThresholdWoodenToRuined;
        this.maxHealth = type.getMaxHealth();
        this.currentHealth = maxHealth;
        this.health = maxHealth;

        System.out.println("Tower created: " + type.getName() + " (level " + type.getLevel() + ") with HP: " + maxHealth);
    }

    // Конструктор для обратной совместимости
    public Tower(int id, float x, float y, int size, float heal) {
        this(id, x, y, size, heal, TowerType.STONE, 0);
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
        if (!isAlive) return;

        int originalDamage = damage;

        // Критический урон для деревянных башен
        if (towerType == TowerType.WOODEN && damage > 30) {
            damage = (int)(damage * 1.3);
            System.out.println("🔥 Критический урон по деревянной башне!");
        }

        // Сниженный урон для каменных башен
        if (towerType == TowerType.STONE && damage < 20) {
            damage = (int)(damage * 0.7);
            System.out.println("🛡️ Каменная башня снизила урон");
        }

        // Разрушенная башня получает дополнительный урон
        if (towerType == TowerType.RUINED) {
            damage = (int)(damage * 1.2);
            System.out.println("💔 Разрушенная башня получила критический урон!");
        }

        currentHealth = Math.max(0, currentHealth - damage);
        health = currentHealth;

        System.out.printf("%s башня получила %d урона (%d → %d). Осталось HP: %d/%d (%.0f%%)%n",
                towerType.getName(), originalDamage, damage, currentHealth,
                currentHealth, maxHealth, (float)currentHealth / maxHealth * 100);

        // ПРОВЕРКА ДЕГРАДАЦИИ ПОСЛЕ ПОЛУЧЕНИЯ УРОНА
        checkAndDegrade();

        if (currentHealth <= 0) {
            destroy();
        }
    }

    // Проверка и выполнение деградации на основе текущего здоровья
    private void checkAndDegrade() {
        float healthPercent = (float) currentHealth / maxHealth;

        // Деградация с камня на дерево
        if (towerType == TowerType.STONE && healthPercent <= degradeThresholdStoneToWooden) {
            degradeTo(TowerType.WOODEN);
        }
        // Деградация с дерева на руины
        else if (towerType == TowerType.WOODEN && healthPercent <= degradeThresholdWoodenToRuined) {
            degradeTo(TowerType.RUINED);
        }
    }

    // Метод деградации башни до указанного типа
    private void degradeTo(TowerType newType) {
        if (!isAlive) return;
        if (towerType == newType) return;

        TowerType oldType = towerType;

        // Сохраняем процент здоровья перед деградацией
        float healthPercent = (float) currentHealth / maxHealth;

        // Меняем тип башни
        towerType = newType;
        maxHealth = towerType.getMaxHealth();

        // Новое здоровье пропорционально проценту от нового макс. здоровья
        currentHealth = Math.max(1, (int)(maxHealth * healthPercent));
        health = currentHealth;

        System.out.println("⚠️ БАШНЯ ДЕГРАДИРОВАЛА: " + oldType.getName() + " → " + towerType.getName());
        System.out.println("   HP изменилось: " + currentHealth + "/" + maxHealth + " (было " +
                (int)(healthPercent * 100) + "% от старого)");

        // Если после деградации здоровье упало до 0, башня разрушается
        if (currentHealth <= 0) {
            destroy();
        }
    }

    // Принудительная деградация (например, от сильного удара)
    public void forceDegrade() {
        if (!isAlive) return;

        System.out.println("💥 Принудительная деградация башни!");

        if (towerType == TowerType.STONE) {
            degradeTo(TowerType.WOODEN);
        } else if (towerType == TowerType.WOODEN) {
            degradeTo(TowerType.RUINED);
        } else {
            System.out.println("Башня уже разрушена, дальнейшая деградация невозможна");
        }
    }

    // Восстановление здоровья (может обратить деградацию?)
    public void heal(int amount) {
        if (!isAlive) return;

        currentHealth = Math.min(maxHealth, currentHealth + amount);
        health = currentHealth;
        System.out.printf("%s башня восстановила %d HP. Текущее HP: %d/%d (%.0f%%)%n",
                towerType.getName(), amount, currentHealth, maxHealth,
                (float)currentHealth / maxHealth * 100);

        // Опционально: можно добавить восстановление типа башни при полном излечении
        // checkAndUpgradeFromHeal();
    }

    // Опционально: восстановление типа башни при излечении
    private void checkAndUpgradeFromHeal() {
        float healthPercent = (float) currentHealth / maxHealth;

        if (towerType == TowerType.RUINED && healthPercent > degradeThresholdWoodenToRuined) {
            upgradeTo(TowerType.WOODEN);
        } else if (towerType == TowerType.WOODEN && healthPercent > degradeThresholdStoneToWooden) {
            upgradeTo(TowerType.STONE);
        }
    }

    // Улучшение башни (обратный процесс деградации)
    public boolean upgradeTo(TowerType newType) {
        if (towerType.getLevel() >= newType.getLevel()) {
            System.out.println("Нельзя улучшить до равного или более низкого уровня!");
            return false;
        }

        float healthPercent = (float) currentHealth / maxHealth;
        TowerType oldType = towerType;
        towerType = newType;
        maxHealth = towerType.getMaxHealth();
        currentHealth = Math.min(maxHealth, (int)(maxHealth * healthPercent));
        health = currentHealth;

        System.out.println("✨ БАШНЯ УЛУЧШЕНА: " + oldType.getName() + " → " + towerType.getName());
        return true;
    }

    public void destroy() {
        isAlive = false;
        System.out.println("💀 " + towerType.getName() + " башня разрушена!");
    }

    // Геттеры
    public int getCurrentHealth() { return currentHealth; }
    public int getMaxHealth() { return maxHealth; }
    public TowerType getTowerType() { return towerType; }
    public float getXOffset() { return xOffset; }
    public void setXOffset(float xOffset) { this.xOffset = xOffset; }

    // Получение порогов деградации
    public float getDegradeThreshold1() { return degradeThresholdStoneToWooden; }
    public float getDegradeThreshold2() { return degradeThresholdWoodenToRuined; }
    public float getCurrentHealthPercent() { return (float) currentHealth / maxHealth; }

    // Получение цвета здоровья для отображения
    public Color getHealthColor() {
        float percent = (float) currentHealth / maxHealth;
        if (percent >= 0.75f) return Color.GREEN;
        if (percent >= 0.5f) return Color.YELLOW;
        if (percent >= 0.25f) return Color.ORANGE;
        return Color.RED;
    }

    // Отрисовка башни
    @Override
    public void draw(Graphics g) {
        if (!isAlive) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));

        // Применяем смещение по оси X
        int centerX = (int)(x + xOffset);
        int baseY = (int)(y);

        // Рисуем башню
        drawTowerBody(g2d, centerX, baseY);
        drawBattlements(g2d, centerX, baseY - TOWER_HEIGHT + 50, towerType.getTopWidth());
        drawWindow(g2d, centerX, baseY - TOWER_HEIGHT + WINDOW_HEIGHT_OFFSET);
        drawDoor(g2d, centerX, baseY);
        drawFlag(g2d, centerX, baseY - TOWER_HEIGHT);
        drawHealthBar(g2d, centerX, baseY);
        drawDegradeIndicators(g2d, centerX, baseY);
    }

    private void drawTowerBody(Graphics2D g2d, int centerX, int baseY) {
        int baseWidth = towerType.getBaseWidth();
        int topWidth = towerType.getTopWidth();

        int[] towerX = {
                centerX - baseWidth/2,      // левый низ
                centerX + baseWidth/2,      // правый низ
                centerX + topWidth/2,       // правый верх
                centerX + topWidth/2,       // правый верх зубцов
                centerX + topWidth/2 - 10,  // выступ справа
                centerX + topWidth/2 - 10,  // правая сторона крыши
                centerX,                    // вершина крыши
                centerX - topWidth/2 + 10,  // левая сторона крыши
                centerX - topWidth/2 + 10,  // выступ слева
                centerX - topWidth/2,       // левый верх зубцов
                centerX - topWidth/2        // левый верх
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

        // Рисуем башню с учетом типа
        g2d.setColor(towerType.getColor());
        g2d.fillPolygon(towerX, towerY, 11);

        // Обводка
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(towerX, towerY, 11);

        // Добавляем текстуру для разных типов
        addTexture(g2d, towerType, centerX, baseY);
    }

    private void addTexture(Graphics2D g2d, TowerType type, int centerX, int baseY) {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));

        switch (type) {
            case STONE:
                // Имитация каменной кладки
                for (int i = 0; i < 5; i++) {
                    int y = baseY - 50 - i * 40;
                    g2d.drawLine(centerX - 50, y, centerX + 50, y);
                }
                break;
            case WOODEN:
                // Имитация деревянных досок
                for (int i = 0; i < 8; i++) {
                    int y = baseY - 30 - i * 30;
                    g2d.drawLine(centerX - 45, y, centerX + 45, y);
                }
                break;
            case RUINED:
                // Трещины на разрушенной башне
                g2d.setColor(Color.DARK_GRAY);
                g2d.drawLine(centerX - 30, baseY - 200, centerX, baseY - 150);
                g2d.drawLine(centerX, baseY - 150, centerX + 20, baseY - 100);
                g2d.drawLine(centerX - 20, baseY - 100, centerX + 10, baseY - 80);
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

        // Разбитое окно для разрушенной башни
        if (towerType == TowerType.RUINED) {
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawLine(centerX - 15, windowY, centerX + 15, windowY + 30);
            g2d.drawLine(centerX + 15, windowY, centerX - 15, windowY + 30);
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
        } else if (currentHealth <= maxHealth * 0.25f) {
            flagColor = Color.RED; // Красный флаг при критическом здоровье
        } else if (currentHealth <= maxHealth * 0.5f) {
            flagColor = Color.ORANGE; // Оранжевый при плохом здоровье
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

        // Фон
        g2d.setColor(Color.GRAY);
        g2d.fillRect(barX, barY, barWidth, barHeight);

        // Заполнение
        int healthWidth = (int)((currentHealth / (float)maxHealth) * barWidth);
        g2d.setColor(getHealthColor());
        g2d.fillRect(barX, barY, healthWidth, barHeight);

        // Обводка
        g2d.setColor(Color.BLACK);
        g2d.drawRect(barX, barY, barWidth, barHeight);

        // Текст здоровья
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        String healthText = currentHealth + "/" + maxHealth;
        FontMetrics fm = g2d.getFontMetrics();
        int textX = centerX - fm.stringWidth(healthText)/2;
        g2d.setColor(Color.WHITE);
        g2d.drawString(healthText, textX, barY + barHeight - 2);
    }

    private void drawDegradeIndicators(Graphics2D g2d, int centerX, int baseY) {
        int indicatorY = baseY - TOWER_HEIGHT - 35;

        if (towerType == TowerType.STONE) {
            int thresholdPercent = Math.round(degradeThresholdStoneToWooden * 100);
            int thresholdX = centerX - 50 + thresholdPercent;
            g2d.setColor(new Color(255, 100, 0, 100));
            g2d.fillRect(centerX - 50, indicatorY - 10, thresholdPercent, 5);
            g2d.setColor(Color.ORANGE);
            g2d.drawString("Degrade at " + thresholdPercent + "%", thresholdX - 40, indicatorY - 5);
        } else if (towerType == TowerType.WOODEN) {
            int thresholdPercent = Math.round(degradeThresholdWoodenToRuined * 100);
            g2d.setColor(new Color(255, 0, 0, 100));
            g2d.fillRect(centerX - 50, indicatorY - 10, thresholdPercent, 5);
            g2d.setColor(Color.RED);
            g2d.drawString("Degrade at " + thresholdPercent + "%", centerX - 45, indicatorY - 5);
        }
    }
}