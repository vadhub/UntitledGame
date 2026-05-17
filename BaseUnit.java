package src.view.unit;

import src.engine.GameObject;
import src.engine.Engine;

import java.awt.*;
import java.awt.geom.Path2D;

public class BaseUnit extends GameObject {

    protected boolean isVisibleHealthBar = true;

    public static Builder builder() {
        return new BaseUnit().new Builder();
    }

    public class Builder extends GameObject.Builder {
        Builder() {
            super();
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

        // Шкала здоровья рисуется ДО трансформации — всегда над юнитом
        drawHealthBar(g2, scale);

        // Изолированный контекст для тела юнита с отражением
        Graphics2D g2body = (Graphics2D) g.create();
        g2body.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        boolean isFacingLeft = (direction == -1);
        if (isFacingLeft) {
            g2body.translate(x + 35 * scale, y + 30 * scale);
            g2body.scale(-1, 1);
            g2body.translate(-(x + 35 * scale), -(y + 30 * scale));
        }

        // тень
        g2body.setColor(new Color(0, 0, 0, 40));
        g2body.fillOval(Math.round(x - 25 * scale), Math.round(y + 50 * scale),
                Math.round(150 * scale), Math.round(20 * scale));

        g2body.setColor(new Color(108, 29, 13));
        g2body.fillRoundRect(Math.round(x + 15 * scale), Math.round(y - 40 * scale),
                Math.round(40 * scale), Math.round(50 * scale),
                Math.round(35 * scale), Math.round(75 * scale));

        g2body.setColor(new Color(217, 142, 73));
        g2body.fillOval(Math.round(x + 17 * scale), Math.round(y - 65 * scale),
                Math.round(35 * scale), Math.round(35 * scale));

        g2body.setColor(Color.BLACK);
        g2body.fillOval(Math.round(x + 42 * scale), Math.round(y - 57 * scale),
                Math.round(5 * scale), Math.round(10 * scale));
        g2body.fillOval(Math.round(x + 33 * scale), Math.round(y - 55 * scale),
                Math.round(5 * scale), Math.round(10 * scale));

        Graphics2D gBat = (Graphics2D) g2body.create();
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

        g2body.dispose(); // трансформ изолирован, не утекает
    }

    protected void drawHealthBar(Graphics2D g2d, float k) {
        if (!isVisibleHealthBar) return;
        int barWidth = Math.round(60 * k);
        int barHeight = Math.round(8 * k);
        // Центрируем шкалу над головой юнита (голова ~35px шириной * scale, -80px над y)
        int barX = Math.round(x + 35 * k) - barWidth / 2;
        int barY = Math.round(y - 85 * k);

        // Фон (красный)
        g2d.setColor(Color.RED);
        g2d.fillRect(barX, barY, barWidth, barHeight);

        // Здоровье (зелёный)
        g2d.setColor(Color.GREEN);
        int healthPercent = (int) ((float) health / 100f * barWidth);
        healthPercent = Math.max(0, Math.min(barWidth, healthPercent));
        g2d.fillRect(barX, barY, healthPercent, barHeight);

        // Рамка
        g2d.setColor(Color.BLACK);
        g2d.drawRect(barX, barY, barWidth, barHeight);
    }
}