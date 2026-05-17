package view.unit;

import engine.Engine;
import engine.GameObject;
import view.Arrow;

import java.awt.*;
import java.util.List;

public class UnitDinoRider extends GameObject {
    private GameObject currentTarget;
    private static final float DINO_SPEED = 20f;
    private static final float DINO_ATTACK_RANGE = 300f;
    private static final float DINO_ATTACK_COOLDOWN = 1.5f;
    private static final int DINO_DAMAGE = 40;
    private static final int DINO_HEALTH = 200;
    private static final float SPEAR_SPEED = 600f;
    private static final float STOP_DISTANCE_FROM_TOWER = 120f; // ← Увеличил для надёжности

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

    public static Builder builder() {
        UnitDinoRider instance = new UnitDinoRider();
        return instance.new Builder(instance);
    }

    public class Builder extends GameObject.Builder {
        private final UnitDinoRider instance;

        protected Builder(UnitDinoRider instance) {
            super();
            this.instance = instance;
        }

        @Override
        public UnitDinoRider build() {
            return instance;
        }
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void update(float deltaTime) {
        if (!isAlive) return;
        if (isBlockedByAlly()) return;

        if (currentTarget == null || !currentTarget.isAlive()) {
            currentTarget = findNearestEnemy();
        }

        if (currentTarget != null) {
            String targetClass = currentTarget.getClass().getSimpleName();
            boolean isTower = targetClass.contains("Tower");

            float stopDistance = isTower ? STOP_DISTANCE_FROM_TOWER : attackRange;

            float distToCenter = Math.abs(this.x - (currentTarget.getX() + currentTarget.getSize() / 2f));

            if (isTower && distToCenter <= STOP_DISTANCE_FROM_TOWER) {

                if (canAttack(engine.getGameTime())) {
                    throwSpearAt(currentTarget);
                    lastAttackTime = engine.getGameTime();
                }
            } else if (distToCenter > stopDistance) {
                // Двигаемся только вправо к цели
                this.x += DINO_SPEED * deltaTime;
            } else {
                // В радиусе атаки — стоим и атакуем
                if (canAttack(engine.getGameTime())) {
                    throwSpearAt(currentTarget);
                    lastAttackTime = engine.getGameTime();
                }
            }
        } else {
            // Если нет целей — идём вправо
            this.x += DINO_SPEED * deltaTime;
        }
    }

    private boolean isBlockedByAlly() {
        if (engine == null) return false;
        List<GameObject> objects = engine.getObjects();
        if (objects == null) return false;

        for (GameObject obj : objects) {
            if (obj == null || !obj.isAlive() || obj == this) continue;
            if (obj.getFraction() != this.fraction) continue;

            if (Math.abs(obj.getY() - this.y) < 10 && obj.getX() > this.x && obj.getX() - this.x < 50) {
                return true;
            }
        }
        return false;
    }

    private GameObject findNearestEnemy() {
        List<GameObject> objects = engine.getObjects();
        if (objects == null) return null;

        GameObject nearest = null;
        float minDist = Float.MAX_VALUE;

        for (GameObject obj : objects) {
            if (obj == null || !obj.isAlive() || obj == this) continue;
            if (obj.getFraction() == this.fraction) continue;

            float dist = distanceTo(obj);
            if (dist < minDist) {
                minDist = dist;
                nearest = obj;
            }
        }
        return nearest;
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

        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillOval(Math.round(x - 25 * k), Math.round(y + 50 * k),
                Math.round(150 * k), Math.round(20 * k));

        g2.setColor(new Color(100, 160, 100));
        g2.fillRoundRect(Math.round(x + 17 * k), Math.round(y + 30 * k),
                Math.round(18 * k), Math.round(30 * k),
                Math.round(10 * k), Math.round(10 * k));
        g2.fillRoundRect(Math.round(x + 80 * k), Math.round(y + 30 * k),
                Math.round(18 * k), Math.round(30 * k),
                Math.round(10 * k), Math.round(10 * k));

        g2.setColor(new Color(100, 180, 100));
        g2.fillRoundRect(Math.round(x - 10 * k), Math.round(y - 10 * k),
                Math.round(110 * k), Math.round(55 * k),
                Math.round(30 * k), Math.round(50 * k));

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

        g2.fillRoundRect(Math.round(x - 5 * k), Math.round(y + 30 * k),
                Math.round(18 * k), Math.round(30 * k),
                Math.round(10 * k), Math.round(10 * k));
        g2.fillRoundRect(Math.round(x + 60 * k), Math.round(y + 30 * k),
                Math.round(18 * k), Math.round(30 * k),
                Math.round(10 * k), Math.round(10 * k));

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

        if (isShowHealthBar()) {
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
        int oldSize = this.size;

        this.x = x;
        this.y = y + 10;
        this.size = 50;

        setShowHealthBar(false);
        draw(g);
        setShowHealthBar(true);

        this.x = oldX;
        this.y = oldY;
        this.size = oldSize;
    }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setSize(int size) { this.size = size; }
    public void setSpeed(float speed) { this.speed = speed; }
    public void setFraction(int fraction) { this.fraction = fraction; }
    public void setAttackRange(float range) { this.attackRange = range; }
    public void setHealth(int health) { this.health = health; }
    public void setAttackDamage(int damage) { this.attackDamage = damage; }
    public void setDirection(int direction) { this.direction = direction; }
}