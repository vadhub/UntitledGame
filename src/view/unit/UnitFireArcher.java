package src.view.unit;

import src.engine.GameObject;
import src.view.Arrow;
import src.view.FireArrow;

import java.awt.*;

public class UnitFireArcher extends BaseUnit {
    private static final float ARROW_SPEED = 600f;
    private static final float ATTACK_COOLDOWN = 2.0f;
    private static final int DEFAULT_HEALTH = 100;
    private static final int DEFAULT_ATTACK_DAMAGE = 15;
    private static final float DEFAULT_ATTACK_RANGE = 250f;

    private float attackTimer = 0f;

    public static Builder builder() {
        return new UnitFireArcher().new Builder();
    }

    public class Builder extends BaseUnit.Builder {
        private Builder() {
            super();
            health(DEFAULT_HEALTH);
            attackDamage(DEFAULT_ATTACK_DAMAGE);
            attackRange(DEFAULT_ATTACK_RANGE);
            attackCooldown(ATTACK_COOLDOWN);
        }

        public UnitFireArcher build() {
            return UnitFireArcher.this;
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (attackTimer > 0f) {
            attackTimer -= deltaTime;
        }
    }

    @Override
    public void attack(GameObject target, float currentTime) {
        if (target == null || !target.isAlive()) return;
        if (distanceTo(target) > attackRange) return;
        if (currentTime - lastAttackTime < attackCooldown) return;
        if (attackTimer > 0f) return;

        attackTimer = ATTACK_COOLDOWN;

        float angle = Arrow.calculateArrowAngle(x, y, target.getX(), target.getY(), ARROW_SPEED);
        FireArrow arrow = new FireArrow(x, y, angle, ARROW_SPEED, attackDamage);
        arrow.setFraction(fraction);
        engine.spawnObject(arrow);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        boolean isFacingLeft = (direction == -1);

        if (isFacingLeft) {
            Graphics2D g2dMirror = (Graphics2D) g2d.create();
            g2dMirror.translate(x + 35 * scale, y + 100 * scale);
            g2dMirror.scale(-1, 1);
            g2dMirror.translate(-(x + 35 * scale), -(y + 100 * scale));
            drawUnit(g2dMirror);
            g2dMirror.dispose();
        } else {
            drawUnit(g2d);
        }

        drawHealthBar(g2d, scale);
    }

    private void drawUnit(Graphics2D g2d) {
        g2d.setColor(new Color(255, 218, 185));
        g2d.fillOval(Math.round(x), Math.round(y),
                Math.round(70 * scale), Math.round(80 * scale));

        g2d.setColor(Color.BLACK);
        g2d.fillOval(Math.round(x + 25 * scale), Math.round(y + 30 * scale),
                Math.round(10 * scale), Math.round(12 * scale));
        g2d.fillOval(Math.round(x + 50 * scale), Math.round(y + 30 * scale),
                Math.round(10 * scale), Math.round(12 * scale));

        g2d.setColor(new Color(190, 35, 35));
        g2d.fillOval(Math.round(x - 10 * scale), Math.round(y + 80 * scale),
                Math.round(90 * scale), Math.round(140 * scale));

        g2d.setColor(new Color(101, 67, 33));
        g2d.setStroke(new BasicStroke(4.0f * scale));
        g2d.drawArc(Math.round(x + 30 * scale), Math.round(y + 50 * scale),
                Math.round(100 * scale), Math.round(150 * scale),
                270, 190);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2.0f * scale));
        int bowCenterX = Math.round(x + 80 * scale);
        int bowTopY = Math.round(y + 52 * scale);
        int bowBottomY = Math.round(y + 200 * scale);
        g2d.drawLine(bowCenterX, bowTopY, bowCenterX, bowBottomY);

        g2d.setColor(new Color(125, 45, 25));
        g2d.fillRect(Math.round(x - 30 * scale), Math.round(y + 100 * scale),
                Math.round(25 * scale), Math.round(60 * scale));
        g2d.fillOval(Math.round(x - 30 * scale), Math.round(y + 95 * scale),
                Math.round(25 * scale), Math.round(20 * scale));

        g2d.setColor(Color.DARK_GRAY);
        for (int i = 0; i < 3; i++) {
            int yOffset = 110 + i * 15;
            g2d.drawLine(Math.round(x - 25 * scale), Math.round(y + yOffset * scale),
                    Math.round(x - 10 * scale), Math.round(y + yOffset * scale));
            int flameX = Math.round(x - 12 * scale);
            int flameY = Math.round(y + (yOffset - 4) * scale);
            g2d.setColor(new Color(255, 95, 20));
            g2d.fillOval(flameX, flameY, Math.max(4, Math.round(8 * scale)), Math.max(4, Math.round(8 * scale)));
            g2d.setColor(Color.DARK_GRAY);
        }
    }
}
