package src.view.unit;

import src.engine.GameObject;
import src.view.Arrow;

import java.awt.*;

/**
 * Лучник: спавнится, идёт к башне (посередине карты),
 * останавливается и атакует, пока башня не разрушится.
 *
 * by Bebron28 & AmericanCoolBoyUSA777
 */
public class UnitArcher extends BaseUnit {

    private static final float ARROW_SPEED = 600f;
    private static final float ATTACK_COOLDOWN = 2.0f; // 2 секунды между выстрелами
    private float attackTimer = 0f; // Таймер до следующей атаки

    public static Builder builder() {
        return new UnitArcher().new Builder();
    }

    public class Builder extends BaseUnit.Builder {
        private Builder() {
            super();
        }

        public UnitArcher build() {
            return UnitArcher.this;
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Обновляем таймер атаки
        if (attackTimer > 0f) {
            attackTimer -= deltaTime;
        }
    }

    @Override
    public void attack(GameObject target, float currentTime) {
        // Проверяем, можно ли атаковать
        if (attackTimer > 0f) {
            return; // Ещё не прошло 2 секунды, не атакуем
        }

        // Сбрасываем таймер
        attackTimer = ATTACK_COOLDOWN;

        float angle = Arrow.calculateArrowAngle(x, y, target.getX(), target.getY(), ARROW_SPEED);
        Arrow arrow = new Arrow(x, y, angle, ARROW_SPEED);
        arrow.setAttackDamage(attackDamage);
        arrow.setFraction(fraction);
        engine.spawnObject(arrow);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //  ОТРАЖЕНИЕ если direction == -1 (идёт влево)
        boolean isFacingLeft = (direction == -1);

        if (isFacingLeft) {
            // Сохраняем текущую трансформацию
            Graphics2D g2dMirror = (Graphics2D) g2d.create();
            // Переносим центр, отражаем, возвращаем
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
        // голова
        g2d.setColor(new Color(255, 218, 185));
        g2d.fillOval(Math.round(x), Math.round(y),
                Math.round(70 * scale), Math.round(80 * scale));

        // глаза
        g2d.setColor(Color.BLACK);
        g2d.fillOval(Math.round(x + 25 * scale), Math.round(y + 30 * scale),
                Math.round(10 * scale), Math.round(12 * scale));
        g2d.fillOval(Math.round(x + 50 * scale), Math.round(y + 30 * scale),
                Math.round(10 * scale), Math.round(12 * scale));

        // тело
        g2d.setColor(new Color(70, 130, 180));
        g2d.fillOval(Math.round(x - 10 * scale), Math.round(y + 80 * scale),
                Math.round(90 * scale), Math.round(140 * scale));

        // лук (дуга)
        g2d.setColor(new Color(101, 67, 33));
        g2d.setStroke(new BasicStroke(4.0f * scale));
        g2d.drawArc(Math.round(x + 30 * scale), Math.round(y + 50 * scale),
                Math.round(100 * scale), Math.round(150 * scale),
                270, 190);

        // тетива
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2.0f * scale));
        int bowCenterX = Math.round(x + 80 * scale);
        int bowTopY = Math.round(y + 52 * scale);
        int bowBottomY = Math.round(y + 200 * scale);
        g2d.drawLine(bowCenterX, bowTopY, bowCenterX, bowBottomY);

        // колчан
        g2d.setColor(new Color(139, 69, 19));
        g2d.fillRect(Math.round(x - 30 * scale), Math.round(y + 100 * scale),
                Math.round(25 * scale), Math.round(60 * scale));
        g2d.fillOval(Math.round(x - 30 * scale), Math.round(y + 95 * scale),
                Math.round(25 * scale), Math.round(20 * scale));

        // стрелы в колчане
        g2d.setColor(Color.DARK_GRAY);
        for (int i = 0; i < 3; i++) {
            int yOffset = 110 + i * 15;
            g2d.drawLine(Math.round(x - 25 * scale), Math.round(y + yOffset * scale),
                    Math.round(x - 10 * scale), Math.round(y + yOffset * scale));
        }
    }

    @Override
    protected void drawHealthBar(Graphics2D g2d, float k) {
        if (!isVisibleHealthBar) return;
        int barWidth = Math.round(60 * k);
        int barHeight = Math.round(8 * k);
        int barX = Math.round(x + 35 * k) - barWidth / 2;
        int barY = Math.round(y - 15 * k); // голова начинается с y, шкала чуть выше
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