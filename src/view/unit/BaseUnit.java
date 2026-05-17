package src.view.unit;

import src.engine.GameObject;
import src.engine.Engine;

import java.awt.*;
import java.awt.geom.Path2D;

public class BaseUnit extends GameObject {
    private static final int DEFAULT_HEALTH = 100;
    private static final int DEFAULT_ATTACK_DAMAGE = 20;
    private static final float DEFAULT_ATTACK_RANGE = 150f;
    private static final float DEFAULT_ATTACK_COOLDOWN = 1.0f;

    protected boolean isVisibleHealthBar = true;

    public static Builder builder() {
        return new BaseUnit().new Builder();
    }

    public class Builder extends GameObject.Builder {
        Builder() {
            super();
            health(DEFAULT_HEALTH);
            attackDamage(DEFAULT_ATTACK_DAMAGE);
            attackRange(DEFAULT_ATTACK_RANGE);
            attackCooldown(DEFAULT_ATTACK_COOLDOWN);
        }

        public Builder notVisibleHeathBar() {
            BaseUnit.this.isVisibleHealthBar = false;
            return this;
        }

        public BaseUnit build() {
            return BaseUnit.this;
        }
    }

    /**
     * Устанавливает Y-координату юнита (для выравнивания по земле)
     * @param groundY координата земли
     */
    public void setGroundY(float groundY) {
        this.y = groundY;
    }

    @Override
    public void update(float deltaTime) {
        // ВСЕГДА двигаемся (скорость отрицательная = влево)
        x += speed * deltaTime;

        if (!isAlive) return;

        Engine engine = Engine.getInstance();
        GameObject target = engine.findNearestEnemy(this, attackRange);

        // Если есть цель и она в радиусе атаки
        if (target != null && distanceTo(target) <= attackRange) {
            if (canAttack(engine.getGameTime())) {
                attack(target, engine.getGameTime());
                // stop() - УДАЛЁН! Юнит не останавливается после атаки
                lastAttackTime = engine.getGameTime();
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //  ОТРАЖЕНИЕ для направления влево (direction == -1)
        boolean isFacingLeft = (direction == -1);

        if (isFacingLeft) {
            g2.translate(x + 35 * scale, y + 30 * scale);
            g2.scale(-1, 1);
            g2.translate(-(x + 35 * scale), -(y + 30 * scale));
        }

        // тень
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillOval(Math.round(x - 25 * scale), Math.round(y + 50 * scale),
                Math.round(150 * scale), Math.round(20 * scale));

        g2.setColor(new Color(108, 29, 13));
        g2.fillRoundRect(Math.round(x + 15 * scale), Math.round(y - 40 * scale),
                Math.round(40 * scale), Math.round(50 * scale),
                Math.round(35 * scale), Math.round(75 * scale));

        g2.setColor(new Color(217, 142, 73));
        g2.fillOval(Math.round(x + 17 * scale), Math.round(y - 65 * scale),
                Math.round(35 * scale), Math.round(35 * scale));

        g2.setColor(Color.BLACK);
        g2.fillOval(Math.round(x + 42 * scale), Math.round(y - 57 * scale),
                Math.round(5 * scale), Math.round(10 * scale));
        g2.fillOval(Math.round(x + 33 * scale), Math.round(y - 55 * scale),
                Math.round(5 * scale), Math.round(10 * scale));

        Graphics2D gBat = (Graphics2D) g2.create();
        double angle = Math.toRadians(-40);
        int bx = Math.round(x + 50 * scale);
        int by = Math.round(y - 20 * scale);

        gBat.rotate(angle, bx, by);

        Path2D bat = new Path2D.Float();
        bat.moveTo(bx - 18 * scale, by);
        bat.lineTo(bx - 12 * scale, by - 5 * scale);
        bat.lineTo(bx - 8 * scale, by - 6 * scale);
        bat.lineTo(bx, by - 5 * scale);
        bat.lineTo(bx + 35 * scale, by - 5 * scale);
        bat.lineTo(bx + 55 * scale, by - 10 * scale);
        bat.lineTo(bx + 100 * scale, by - 15 * scale);
        bat.lineTo(bx + 115 * scale, by - 10 * scale);
        bat.lineTo(bx + 115 * scale, by);
        bat.lineTo(bx + 115 * scale, by + 10 * scale);
        bat.lineTo(bx + 100 * scale, by + 15 * scale);
        bat.lineTo(bx + 55 * scale, by + 10 * scale);
        bat.lineTo(bx + 35 * scale, by + 5 * scale);
        bat.lineTo(bx, by + 5 * scale);
        bat.lineTo(bx - 8 * scale, by + 6 * scale);
        bat.lineTo(bx - 12 * scale, by + 5 * scale);
        bat.closePath();

        GradientPaint metal = new GradientPaint(
                bx - 10 * scale, by,
                new Color(200, 40, 40),
                bx + 100 * scale, by,
                new Color(100, 20, 20)
        );

        gBat.setPaint(metal);
        gBat.fill(bat);
        gBat.setColor(new Color(20, 20, 20));
        gBat.fillRoundRect(bx - Math.round(5 * scale), by - Math.round(6 * scale),
                Math.round(40 * scale), Math.round(12 * scale), 8, 8);
        gBat.setColor(new Color(255, 220, 220, 120));
        gBat.setStroke(new BasicStroke(3 * scale));
        gBat.drawLine(Math.round(bx + 20 * scale), Math.round(by - 2 * scale),
                Math.round(bx + 105 * scale), Math.round(by - 8 * scale));
        gBat.dispose();

        // Отмена трансформации
        if (isFacingLeft) {
            g2.translate(x + 35 * scale, y + 30 * scale);
            g2.scale(-1, 1);
            g2.translate(-(x + 35 * scale), -(y + 30 * scale));
        }

        drawHealthBar(g2, scale);
    }

    protected void drawHealthBar(Graphics2D g2d, float k) {
        if (!isVisibleHealthBar) return;
        int barWidth = 60;
        int barHeight = 8;
        int barX = Math.round(x + 5 * k);
        int barY = Math.round(y - 10 * k);
        g2d.setColor(Color.RED);
        g2d.fillRect(barX, barY, barWidth, barHeight);
        g2d.setColor(Color.GREEN);
        int healthPercent = (int) ((float) health / Math.max(1, getMaxHealth()) * barWidth);
        healthPercent = Math.max(0, Math.min(barWidth, healthPercent));
        g2d.fillRect(barX, barY, healthPercent, barHeight);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(barX, barY, barWidth, barHeight);
    }
}
