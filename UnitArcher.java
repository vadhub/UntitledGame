package view.unit;

import engine.Engine;
import engine.GameObject;
import view.Arrow;

import java.awt.*;
import java.util.List;

public class UnitArcher extends GameObject {
    private GameObject currentTarget;
    private static final float ARCHER_SPEED = 20f;  // ← ИЗМЕНЕНО: как у BaseUnit
    private static final float ARCHER_ATTACK_RANGE = 300f;
    private static final float ARCHER_ATTACK_COOLDOWN = 1.5f;
    private static final float ARROW_SPEED = 600f;

    public UnitArcher() {
        this.fraction = 2;
        this.size = 50;
        this.speed = ARCHER_SPEED;
        this.attackRange = ARCHER_ATTACK_RANGE;
        this.attackCooldown = ARCHER_ATTACK_COOLDOWN;
        this.lastAttackTime = -5f;
        this.attackDamage = 25;
        this.health = 100;
    }

    public UnitArcher(float x, float y) {
        this();
        this.x = x;
        this.y = y;
    }

    public UnitArcher(int id, float x, float y, int size, float speed) {
        super(id, x, y, size, ARCHER_SPEED, new Color(70, 130, 180));
        attackRange = ARCHER_ATTACK_RANGE;
        attackCooldown = ARCHER_ATTACK_COOLDOWN;
        lastAttackTime = -5f;
        attackDamage = 25;
        health = 100;
        fraction = 2;
    }

    public static Builder builder() {
        UnitArcher instance = new UnitArcher();
        return instance.new Builder(instance);
    }

    public class Builder extends GameObject.Builder {
        private final UnitArcher instance;

        protected Builder(UnitArcher instance) {
            super();
            this.instance = instance;
        }

        @Override
        public UnitArcher build() {
            return instance;
        }
    }

    public void setEngine(Engine engine) { this.engine = engine; }

    @Override
    public void update(float deltaTime) {
        if (!isAlive) return;
        if (isBlockedByAlly()) return;

        if (currentTarget == null || !currentTarget.isAlive()) {
            currentTarget = findNearestEnemy();
        }

        if (currentTarget != null) {
            float dist = distanceTo(currentTarget);

            // ← ИЗМЕНЕНО: останавливаемся в радиусе атаки
            if (dist > attackRange) {
                // Двигаемся только вправо к цели
                this.x += ARCHER_SPEED * deltaTime;
            } else {
                // В радиусе атаки — стоим и стреляем
                if (canAttack(engine.getGameTime())) {
                    shootAt(currentTarget);
                    lastAttackTime = engine.getGameTime();
                }
            }
        } else {
            // Если нет целей — идём вправо
            this.x += ARCHER_SPEED * deltaTime;
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

    private void shootAt(GameObject target) {
        float angle = Arrow.calculateArrowAngle(x, y, target.getX(), target.getY(), ARROW_SPEED);
        Arrow arrow = new Arrow(x, y, angle, ARROW_SPEED);
        arrow.setAttackDamage(attackDamage);
        arrow.setFraction(fraction);
        engine.spawnObject(arrow);
    }

    @Override
    public void draw(Graphics g) {
        float k = this.size / 100.0f;
        if (k <= 0) k = 1.0f;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int drawX = Math.round(x);
        int drawY = Math.round(y);

        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillOval(drawX - 10, drawY + 115, 70, 10);

        g2d.setColor(new Color(255, 218, 185));
        g2d.fillOval(drawX, drawY, Math.round(70 * k), Math.round(80 * k));

        g2d.setColor(Color.BLACK);
        g2d.fillOval(Math.round(x + 25 * k), Math.round(y + 30 * k), Math.round(10 * k), Math.round(12 * k));
        g2d.fillOval(Math.round(x + 50 * k), Math.round(y + 30 * k), Math.round(10 * k), Math.round(12 * k));

        g2d.setColor(new Color(70, 130, 180));
        g2d.fillOval(Math.round(x - 10 * k), Math.round(y + 80 * k), Math.round(90 * k), Math.round(140 * k));

        g2d.setColor(new Color(101, 67, 33));
        g2d.setStroke(new BasicStroke(4.0f * k));
        g2d.drawArc(Math.round(x + 30 * k), Math.round(y + 50 * k), Math.round(100 * k), Math.round(150 * k), 270, 190);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2.0f * k));
        int bowCenterX = Math.round(x + 80 * k);
        int bowTopY = Math.round(y + 52 * k);
        int bowBottomY = Math.round(y + 200 * k);
        g2d.drawLine(bowCenterX, bowTopY, bowCenterX, bowBottomY);

        g2d.setColor(new Color(139, 69, 19));
        g2d.fillRect(Math.round(x - 30 * k), Math.round(y + 100 * k), Math.round(25 * k), Math.round(60 * k));
        g2d.fillOval(Math.round(x - 30 * k), Math.round(y + 95 * k), Math.round(25 * k), Math.round(20 * k));

        g2d.setColor(Color.DARK_GRAY);
        for (int i = 0; i < 3; i++) {
            int yOffset = 110 + i * 15;
            g2d.drawLine(Math.round(x - 25 * k), Math.round(y + yOffset * k), Math.round(x - 10 * k), Math.round(y + yOffset * k));
        }

        if (isShowHealthBar()) {
            drawHealthBar(g2d, k);
        }
    }

    private void drawHealthBar(Graphics2D g2d, float k) {
        int barWidth = 60, barHeight = 8;
        int barX = Math.round(x + 5 * k), barY = Math.round(y - 10 * k);
        g2d.setColor(Color.RED);
        g2d.fillRect(barX, barY, barWidth, barHeight);
        g2d.setColor(Color.GREEN);
        int healthPercent = (int) ((float) health / 100f * barWidth);
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

    @Override
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            isAlive = false;
        }
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