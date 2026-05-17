package src.view.unit;

import src.engine.GameObject;

import java.awt.*;

/**
 * Всадник на динозавре: спавнится, идёт к башне,
 * останавливается и атакует, пока башня не разрушится.
 *
 * by Bebron28 & AmericanCoolBoyUSA777
 */
public class UnitDinoRider extends BaseUnit {

    // Переменные для анимации атаки
    private boolean isAttacking = false;
    private float attackAnimationTime = 0f;
    private final float ATTACK_ANIMATION_DURATION = 0.4f;
    private float currentAngle = -20f;
    private float targetAngle = -20f;

    public static Builder builder() {
        return new UnitDinoRider().new Builder();
    }

    public class Builder extends BaseUnit.Builder {
        private Builder() {
            super();
        }

        public UnitDinoRider build() {
            return UnitDinoRider.this;
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isAttacking) {
            attackAnimationTime += deltaTime;
            float progress = attackAnimationTime / ATTACK_ANIMATION_DURATION;

            // Три фазы анимации
            if (progress <= 0.33f) {
                float t = progress / 0.33f;
                targetAngle = -20f + t * 80f;  // подъём
            } else if (progress <= 0.66f) {
                float t = (progress - 0.33f) / 0.33f;
                targetAngle = 60f - t * 120f;  // удар
            } else {
                float t = (progress - 0.66f) / 0.34f;
                targetAngle = -60f + t * 40f;  // возврат
            }

            currentAngle = currentAngle + (targetAngle - currentAngle) * 0.3f;

            if (attackAnimationTime >= ATTACK_ANIMATION_DURATION) {
                isAttacking = false;
                currentAngle = -20f;
                targetAngle = -20f;
            }
        } else {
            currentAngle = currentAngle + (-20f - currentAngle) * 0.2f;
        }
    }

    @Override
    public void attack(GameObject target, float currentTime) {
        if (target == null || !target.isAlive()) return;
        if (distanceTo(target) > attackRange) return;
        if (currentTime - lastAttackTime < attackCooldown) return;

        if (!isAttacking) {
            isAttacking = true;
            attackAnimationTime = 0f;
        }


        target.takeDamage(attackDamage);
        lastAttackTime = currentTime;

        System.out.println("DinoRider атакует копьём! Урон: " + attackDamage);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //  ОТРАЖЕНИЕ если direction == -1 (идёт влево)

        boolean isFacingLeft = (direction == -1);

        if (isFacingLeft) {
            Graphics2D g2Mirror = (Graphics2D) g2.create();
            g2Mirror.translate(x + 60 * scale, y + 30 * scale);
            g2Mirror.scale(-1, 1);
            g2Mirror.translate(-(x + 60 * scale), -(y + 30 * scale));
            drawUnit(g2Mirror);
            g2Mirror.dispose();
        } else {
            drawUnit(g2);
        }

        drawHealthBar(g2, scale);
    }

    private void drawUnit(Graphics2D g2) {
        // тень
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillOval(Math.round(x - 25 * scale), Math.round(y + 50 * scale),
                Math.round(150 * scale), Math.round(20 * scale));

        // задние ноги динозавра
        g2.setColor(new Color(100, 160, 100));
        g2.fillRoundRect(Math.round(x + 17 * scale), Math.round(y + 30 * scale),
                Math.round(18 * scale), Math.round(30 * scale),
                Math.round(10 * scale), Math.round(10 * scale));
        g2.fillRoundRect(Math.round(x + 80 * scale), Math.round(y + 30 * scale),
                Math.round(18 * scale), Math.round(30 * scale),
                Math.round(10 * scale), Math.round(10 * scale));

        // тело динозавра
        g2.setColor(new Color(100, 180, 100));
        g2.fillRoundRect(Math.round(x - 10 * scale), Math.round(y - 10 * scale),
                Math.round(110 * scale), Math.round(55 * scale),
                Math.round(30 * scale), Math.round(50 * scale));

        // шея и голова динозавра
        g2.fillRoundRect(Math.round(x + 70 * scale), Math.round(y - 60 * scale),
                Math.round(35 * scale), Math.round(75 * scale),
                Math.round(25 * scale), Math.round(45 * scale));
        g2.setColor(new Color(100, 160, 100));
        g2.fillRoundRect(Math.round(x + 65 * scale), Math.round(y - 80 * scale),
                Math.round(35 * scale), Math.round(45 * scale),
                Math.round(25 * scale), Math.round(35 * scale));
        g2.fillRoundRect(Math.round(x + 85 * scale), Math.round(y - 70 * scale),
                Math.round(35 * scale), Math.round(35 * scale),
                Math.round(25 * scale), Math.round(70 * scale));

        // передние ноги динозавра
        g2.fillRoundRect(Math.round(x - 5 * scale), Math.round(y + 30 * scale),
                Math.round(18 * scale), Math.round(30 * scale),
                Math.round(10 * scale), Math.round(10 * scale));
        g2.fillRoundRect(Math.round(x + 60 * scale), Math.round(y + 30 * scale),
                Math.round(18 * scale), Math.round(30 * scale),
                Math.round(10 * scale), Math.round(10 * scale));

        // глаза и нос динозавра
        g2.setColor(new Color(255, 255, 255));
        g2.fillOval(Math.round(x + 77 * scale), Math.round(y - 72 * scale),
                Math.round(10 * scale), Math.round(15 * scale));
        g2.fillOval(Math.round(x + 90 * scale), Math.round(y - 77 * scale),
                Math.round(10 * scale), Math.round(15 * scale));
        g2.setColor(new Color(0, 0, 0));
        g2.fillOval(Math.round(x + 83 * scale), Math.round(y - 67 * scale),
                Math.round(5 * scale), Math.round(7 * scale));
        g2.fillOval(Math.round(x + 96 * scale), Math.round(y - 72 * scale),
                Math.round(5 * scale), Math.round(7 * scale));
        g2.setColor(new Color(40, 40, 40));
        g2.fillOval(Math.round(x + 103 * scale), Math.round(y - 62 * scale),
                Math.round(5 * scale), Math.round(7 * scale));
        g2.fillOval(Math.round(x + 110 * scale), Math.round(y - 65 * scale),
                Math.round(5 * scale), Math.round(7 * scale));

        // всадник
        g2.setColor(new Color(108, 29, 13));
        g2.fillRoundRect(Math.round(x + 15 * scale), Math.round(y - 40 * scale),
                Math.round(40 * scale), Math.round(50 * scale),
                Math.round(35 * scale), Math.round(75 * scale));
        g2.setColor(new Color(217, 142, 73));
        g2.fillOval(Math.round(x + 17 * scale), Math.round(y - 65 * scale),
                Math.round(35 * scale), Math.round(35 * scale));
        g2.setColor(new Color(0, 0, 0));
        g2.fillOval(Math.round(x + 42 * scale), Math.round(y - 57 * scale),
                Math.round(5 * scale), Math.round(10 * scale));
        g2.fillOval(Math.round(x + 33 * scale), Math.round(y - 55 * scale),
                Math.round(5 * scale), Math.round(10 * scale));

        // КОПЬЁ

        Graphics2D gSpear = (Graphics2D) g2.create();

        int pivotX = Math.round(x + 50 * scale);
        int pivotY = Math.round(y - 20 * scale);

        gSpear.rotate(Math.toRadians(-20), pivotX, pivotY);


        gSpear.setStroke(new BasicStroke(7.0f * scale));
        gSpear.setColor(new Color(121, 67, 25));
        gSpear.drawLine(Math.round(x - 20 * scale), Math.round(y - 15 * scale),
                Math.round(x + 100 * scale), Math.round(y - 15 * scale));

        gSpear.setStroke(new BasicStroke(3.0f * scale));
        gSpear.setColor(new Color(128, 121, 115));
        int[] xPoints = {
                Math.round(x + 100 * scale),
                Math.round(x + 120 * scale),
                Math.round(x + 100 * scale)
        };
        int[] yPoints = {
                Math.round(y - 25 * scale),
                Math.round(y - 15 * scale),
                Math.round(y - 5 * scale)
        };
        gSpear.fillPolygon(xPoints, yPoints, 3);

        gSpear.dispose();
    }

    @Override
    protected void drawHealthBar(Graphics2D g2d, float k) {
        if (!isVisibleHealthBar) return;
        int barWidth = Math.round(80 * k);
        int barHeight = Math.round(10 * k);
        int barX = Math.round(x + 40 * k) - barWidth / 2;
        int barY = Math.round(y - 20 * k); // над головой динозавра
        g2d.setColor(Color.RED);
        g2d.fillRect(barX, barY, barWidth, barHeight);
        g2d.setColor(Color.GREEN);
        int healthPercent = (int) ((float) health / 100f * barWidth);
        healthPercent = Math.max(0, Math.min(barWidth, healthPercent));
        g2d.fillRect(barX, barY, healthPercent, barHeight);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(barX, barY, barWidth, barHeight);
    }
}