import java.awt.*;
import java.util.List;

public class UnitDinoRider extends GameObject {
    private GameObject currentTarget;
    // настройки динозавра (сильнее лучника)
    private static final float DINO_SPEED = 20f;
    private static final float DINO_ATTACK_RANGE = 300f;
    private static final float DINO_ATTACK_COOLDOWN = 1.5f;
    private static final int DINO_DAMAGE = 40;
    private static final int DINO_HEALTH = 200;
    private static final float SPEAR_SPEED = 600f;

    // Флаг для управления отрисовкой HP на иконке
    private boolean showHealthBar = true;

    public UnitDinoRider() {
        this.fraction = 2;
        this.y = 470;
    }

    public UnitDinoRider(int id, float x, float y, int size, float speed) {
        super(id, x, y, size, DINO_SPEED, new Color(100, 180, 100));
        attackRange = DINO_ATTACK_RANGE;
        attackCooldown = DINO_ATTACK_COOLDOWN;
        lastAttackTime = -5f;
        attackDamage = DINO_DAMAGE;
        health = DINO_HEALTH;
        fraction = 2;
    }

    @Override
    public void update(float deltaTime) {
        if (!isAlive) return;

        // Проверяем, нет ли впереди союзника на той же линии
        if (isBlockedByAlly()) {
            return;
        }

        // выбор цели, если её нет или она мертва
        if (currentTarget == null || !currentTarget.isAlive()) {
            currentTarget = findTower();
        }

        if (currentTarget != null) {
            float dist = distanceTo(currentTarget);

            if (dist > attackRange) {
                // ДВИЖЕНИЕ ТОЛЬКО ПО ГОРИЗОНТАЛИ (не меняем Y)
                if (currentTarget.getX() > this.x) {
                    this.x += DINO_SPEED * deltaTime;
                } else {
                    this.x -= DINO_SPEED * deltaTime;
                }
            } else {
                // атака в радиусе поражения
                if (canAttack(engine.getGameTime())) {
                    throwSpearAt(currentTarget);
                    lastAttackTime = engine.getGameTime();
                }
            }
        }
    }

    private boolean isBlockedByAlly() {
        if (engine == null) return false;
        List<GameObject> objects = engine.getObjects();
        if (objects == null) return false;

        for (GameObject obj : objects) {
            if (obj == null || !obj.isAlive() || obj == this) continue;
            if (obj.getFraction() != this.fraction) continue;

            // Проверяем, находится ли объект прямо перед нами на той же линии (±10px по Y)
            if (Math.abs(obj.getY() - this.y) < 10 && obj.getX() > this.x && obj.getX() - this.x < 50) {
                return true;
            }
        }
        return false;
    }

    private GameObject findTower() {
        List<GameObject> objects = engine.getObjects();
        for (GameObject obj : objects) {
            if (obj == null || !obj.isAlive()) continue;
            if (obj.getFraction() == fraction) continue;
            // проверка по имени класса (Tower, Tower67 и т.д.)
            String className = obj.getClass().getSimpleName();
            if (className.contains("Tower")) {
                return obj;
            }
        }
        return null;
    }

    private void throwSpearAt(GameObject target) {
        float angle = Arrow.calculateArrowAngle(x, y, target.getX(), target.getY(), SPEAR_SPEED);
        Arrow spear = new Arrow(x, y, angle, SPEAR_SPEED);
        spear.setAttackDamage(attackDamage);
        spear.setFraction(fraction);
        engine.spawnObject(spear);
    }

    @Override
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            isAlive = false;
        }
    }

    @Override
    public void draw(Graphics g) {
        int x = (int) this.x;
        int y = (int) this.y;
        float k = this.size / 100.0f;
        if (k <= 0) k = 1.0f;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // тень
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillOval(Math.round(x - 25 * k), Math.round(y + 50 * k),
                Math.round(150 * k), Math.round(20 * k));

        // задние ноги динозавра
        g2.setColor(new Color(100, 160, 100));
        g2.fillRoundRect(Math.round(x + 17 * k), Math.round(y + 30 * k),
                Math.round(18 * k), Math.round(30 * k),
                Math.round(10 * k), Math.round(10 * k));
        g2.fillRoundRect(Math.round(x + 80 * k), Math.round(y + 30 * k),
                Math.round(18 * k), Math.round(30 * k),
                Math.round(10 * k), Math.round(10 * k));

        // тело динозавра
        g2.setColor(new Color(100, 180, 100));
        g2.fillRoundRect(Math.round(x - 10 * k), Math.round(y - 10 * k),
                Math.round(110 * k), Math.round(55 * k),
                Math.round(30 * k), Math.round(50 * k));

        // шея и голова динозавра
        g2.fillRoundRect(Math.round(x + 70 * k), Math.round(y - 60 * k),
                Math.round(35 * k), Math.round(75 * k),
                Math.round(25 * k), Math.round(45 * k));
        g2.setColor(new Color(100, 160, 100));
        g2.fillRoundRect(Math.round(x + 65 * k), Math.round(y - 80 * k),
                Math.round(35 * k), Math.round(45 * k),
                Math.round(25 * k), Math.round(35 * k));
        g2.fillRoundRect(Math.round(x + 85 * k), Math.round(y - 70 * k),
                Math.round(35 * k), Math.round(35 * k),
                Math.round(25 * k), Math.round(70 * k));

        // передние ноги динозавра
        g2.fillRoundRect(Math.round(x - 5 * k), Math.round(y + 30 * k),
                Math.round(18 * k), Math.round(30 * k),
                Math.round(10 * k), Math.round(10 * k));
        g2.fillRoundRect(Math.round(x + 60 * k), Math.round(y + 30 * k),
                Math.round(18 * k), Math.round(30 * k),
                Math.round(10 * k), Math.round(10 * k));

        // глаза и нос динозавра
        g2.setColor(new Color(255, 255, 255));
        g2.fillOval(Math.round(x + 77 * k), Math.round(y - 72 * k),
                Math.round(10 * k), Math.round(15 * k));
        g2.fillOval(Math.round(x + 90 * k), Math.round(y - 77 * k),
                Math.round(10 * k), Math.round(15 * k));
        g2.setColor(new Color(0, 0, 0));
        g2.fillOval(Math.round(x + 83 * k), Math.round(y - 67 * k),
                Math.round(5 * k), Math.round(7 * k));
        g2.fillOval(Math.round(x + 96 * k), Math.round(y - 72 * k),
                Math.round(5 * k), Math.round(7 * k));
        g2.setColor(new Color(40, 40, 40));
        g2.fillOval(Math.round(x + 103 * k), Math.round(y - 62 * k),
                Math.round(5 * k), Math.round(7 * k));
        g2.fillOval(Math.round(x + 110 * k), Math.round(y - 65 * k),
                Math.round(5 * k), Math.round(7 * k));

        // всадник с копьём
        g2.setColor(new Color(108, 29, 13));
        g2.fillRoundRect(Math.round(x + 15 * k), Math.round(y - 40 * k),
                Math.round(40 * k), Math.round(50 * k),
                Math.round(35 * k), Math.round(75 * k));
        g2.setColor(new Color(217, 142, 73));
        g2.fillOval(Math.round(x + 17 * k), Math.round(y - 65 * k),
                Math.round(35 * k), Math.round(35 * k));
        g2.setColor(new Color(0, 0, 0));
        g2.fillOval(Math.round(x + 42 * k), Math.round(y - 57 * k),
                Math.round(5 * k), Math.round(10 * k));
        g2.fillOval(Math.round(x + 33 * k), Math.round(y - 55 * k),
                Math.round(5 * k), Math.round(10 * k));

        // копьё
        g2.setStroke(new BasicStroke(7.0f * k));
        g2.setColor(new Color(121, 67, 25));
        g2.drawLine(Math.round(x - 20 * k), Math.round(y - 15 * k),
                Math.round(x + 100 * k), Math.round(y - 15 * k));
        g2.setStroke(new BasicStroke(3.0f * k));
        g2.setColor(new Color(128, 121, 115));
        int[] xPoints = {
                Math.round(x + 100 * k),
                Math.round(x + 120 * k),
                Math.round(x + 100 * k)
        };
        int[] yPoints = {
                Math.round(y - 25 * k),
                Math.round(y - 15 * k),
                Math.round(y - 5 * k)
        };
        g2.fillPolygon(xPoints, yPoints, 3);

        // Полоска здоровья - рисуем только если флаг включен
        if (showHealthBar) {
            drawHealthBar(g2, x, y, k);
        }
    }

    private void drawHealthBar(Graphics2D g2d, int x, int y, float k) {
        int barWidth = 80;
        int barHeight = 8;
        int barX = Math.round(x + 10 * k);
        int barY = Math.round(y - 15 * k);

        g2d.setColor(Color.RED);
        g2d.fillRect(barX, barY, barWidth, barHeight);

        g2d.setColor(Color.GREEN);
        int healthPercent = (int) ((float) health / DINO_HEALTH * barWidth);
        healthPercent = Math.max(0, Math.min(barWidth, healthPercent));
        g2d.fillRect(barX, barY, healthPercent, barHeight);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(barX, barY, barWidth, barHeight);
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        float oldX = this.x;
        float oldY = this.y;
        this.x = x;
        this.y = y + 40;
        showHealthBar = false;
        draw(g);
        showHealthBar = true;
        this.x = oldX;
        this.y = oldY;
    }
}