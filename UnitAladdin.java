package src.view.unit;

import src.engine.GameObject;

import java.awt.*;

/**
 * Аладдин на верблюде: вооружён огромной вилкой.
 * Средний юнит по характеристикам: быстрее DinoRider, медленнее Archer.
 * Атакует вилкой с анимацией укола.
 *
 * by Belyakina Maria
 */
public class UnitAladdin extends BaseUnit {

    // Анимация атаки: укол вилкой вперёд
    private boolean isAttacking = false;
    private float attackAnimationTime = 0f;
    private final float ATTACK_ANIMATION_DURATION = 0.45f;

    // Смещение вилки вперёд при уколе
    private float forkOffset = 0f;
    private float targetForkOffset = 0f;

    // Покачивание верблюда при ходьбе
    private float walkBobTime = 0f;

    public static Builder builder() {
        return new UnitAladdin().new Builder();
    }

    public class Builder extends BaseUnit.Builder {
        private Builder() {
            super();
        }

        public UnitAladdin build() {
            return UnitAladdin.this;
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Покачивание верблюда при движении
        walkBobTime += deltaTime * 2.5f;

        if (isAttacking) {
            attackAnimationTime += deltaTime;
            float progress = attackAnimationTime / ATTACK_ANIMATION_DURATION;

            // Три фазы: замах назад → резкий укол вперёд → возврат
            if (progress <= 0.2f) {
                float t = progress / 0.2f;
                targetForkOffset = t * -15f;          // отвод вилки назад
            } else if (progress <= 0.55f) {
                float t = (progress - 0.2f) / 0.35f;
                targetForkOffset = -15f + t * 55f;   // резкий укол вперёд
            } else {
                float t = (progress - 0.55f) / 0.45f;
                targetForkOffset = 40f - t * 40f;    // плавный возврат
            }

            forkOffset = forkOffset + (targetForkOffset - forkOffset) * 0.35f;

            if (attackAnimationTime >= ATTACK_ANIMATION_DURATION) {
                isAttacking = false;
                forkOffset = 0f;
                targetForkOffset = 0f;
            }
        } else {
            forkOffset = forkOffset + (0f - forkOffset) * 0.2f;
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

        System.out.println("Aladdin атакует вилкой! Урон: " + attackDamage);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        boolean isFacingLeft = (direction == -1);

        if (isFacingLeft) {
            Graphics2D g2Mirror = (Graphics2D) g2.create();
            g2Mirror.translate(x + 55 * scale, y + 30 * scale);
            g2Mirror.scale(-1, 1);
            g2Mirror.translate(-(x + 55 * scale), -(y + 30 * scale));
            drawUnit(g2Mirror);
            g2Mirror.dispose();
        } else {
            drawUnit(g2);
        }

        drawHealthBar(g2, scale);
    }

    private void drawUnit(Graphics2D g2) {
        // Лёгкое покачивание верблюда (ноги)
        float bob = (float) Math.sin(walkBobTime) * 2f * scale;

        // ТЕНЬ
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillOval(Math.round(x - 20 * scale), Math.round(y + 52 * scale),
                Math.round(145 * scale), Math.round(18 * scale));

        // ВЕРБЛЮД

        // Задние ноги
        g2.setColor(new Color(185, 140, 70));
        g2.fillRoundRect(Math.round(x + 10 * scale), Math.round(y + 28 * scale + bob),
                Math.round(16 * scale), Math.round(32 * scale),
                Math.round(8 * scale), Math.round(8 * scale));
        g2.fillRoundRect(Math.round(x + 30 * scale), Math.round(y + 28 * scale + bob),
                Math.round(16 * scale), Math.round(32 * scale),
                Math.round(8 * scale), Math.round(8 * scale));

        // Тело верблюда
        g2.setColor(new Color(210, 170, 90));
        g2.fillRoundRect(Math.round(x - 5 * scale), Math.round(y - 5 * scale),
                Math.round(110 * scale), Math.round(50 * scale),
                Math.round(35 * scale), Math.round(50 * scale));

        // Горб верблюда (один — дромедар)
        g2.setColor(new Color(200, 160, 80));
        g2.fillOval(Math.round(x + 25 * scale), Math.round(y - 30 * scale),
                Math.round(50 * scale), Math.round(40 * scale));
        g2.setColor(new Color(210, 170, 90));
        g2.fillRect(Math.round(x + 25 * scale), Math.round(y - 10 * scale),
                Math.round(50 * scale), Math.round(15 * scale));

        // Шея верблюда
        g2.setColor(new Color(210, 170, 90));
        g2.fillRoundRect(Math.round(x + 75 * scale), Math.round(y - 45 * scale),
                Math.round(28 * scale), Math.round(60 * scale),
                Math.round(20 * scale), Math.round(35 * scale));

        // Голова верблюда
        g2.setColor(new Color(205, 165, 85));
        g2.fillRoundRect(Math.round(x + 73 * scale), Math.round(y - 72 * scale),
                Math.round(38 * scale), Math.round(35 * scale),
                Math.round(20 * scale), Math.round(25 * scale));
        // Морда (вытянутая)
        g2.setColor(new Color(195, 155, 75));
        g2.fillRoundRect(Math.round(x + 90 * scale), Math.round(y - 62 * scale),
                Math.round(30 * scale), Math.round(22 * scale),
                Math.round(12 * scale), Math.round(18 * scale));

        // Глаз верблюда
        g2.setColor(new Color(30, 20, 5));
        g2.fillOval(Math.round(x + 80 * scale), Math.round(y - 67 * scale),
                Math.round(7 * scale), Math.round(7 * scale));

        // Ноздри верблюда
        g2.setColor(new Color(160, 110, 40));
        g2.fillOval(Math.round(x + 108 * scale), Math.round(y - 54 * scale),
                Math.round(5 * scale), Math.round(4 * scale));
        g2.fillOval(Math.round(x + 114 * scale), Math.round(y - 54 * scale),
                Math.round(5 * scale), Math.round(4 * scale));

        // Улыбка верблюда
        g2.setColor(new Color(130, 80, 30));
        g2.setStroke(new BasicStroke(2f * scale));
        g2.drawArc(Math.round(x + 105 * scale), Math.round(y - 52 * scale),
                Math.round(14 * scale), Math.round(8 * scale), 200, -160);

        // Передние ноги
        g2.setColor(new Color(185, 140, 70));
        g2.fillRoundRect(Math.round(x + 60 * scale), Math.round(y + 28 * scale + bob),
                Math.round(16 * scale), Math.round(32 * scale),
                Math.round(8 * scale), Math.round(8 * scale));
        g2.fillRoundRect(Math.round(x + 78 * scale), Math.round(y + 28 * scale - bob),
                Math.round(16 * scale), Math.round(32 * scale),
                Math.round(8 * scale), Math.round(8 * scale));

        // ВСАДНИК (АЛАДДИН)

        // Тело (жилетка тёмно-бордовая)
        g2.setColor(new Color(108, 29, 13));
        g2.fillRoundRect(Math.round(x + 20 * scale), Math.round(y - 38 * scale),
                Math.round(38 * scale), Math.round(45 * scale),
                Math.round(30 * scale), Math.round(70 * scale));

        // Штаны (фиолетово-лиловые)
        g2.setColor(new Color(100, 60, 130));
        g2.fillRoundRect(Math.round(x + 22 * scale), Math.round(y - 5 * scale),
                Math.round(34 * scale), Math.round(20 * scale),
                Math.round(15 * scale), Math.round(15 * scale));

        // Лицо
        g2.setColor(new Color(217, 142, 73));
        g2.fillOval(Math.round(x + 22 * scale), Math.round(y - 63 * scale),
                Math.round(33 * scale), Math.round(33 * scale));

        // Глаза
        g2.setColor(Color.BLACK);
        g2.fillOval(Math.round(x + 44 * scale), Math.round(y - 55 * scale),
                Math.round(5 * scale), Math.round(9 * scale));
        g2.fillOval(Math.round(x + 35 * scale), Math.round(y - 54 * scale),
                Math.round(5 * scale), Math.round(9 * scale));

        // Чалма (фиолетовая)
        g2.setColor(new Color(110, 60, 180));
        g2.fillOval(Math.round(x + 18 * scale), Math.round(y - 75 * scale),
                Math.round(42 * scale), Math.round(28 * scale));
        // Навершие чалмы (повязка снизу)
        g2.fillRoundRect(Math.round(x + 18 * scale), Math.round(y - 63 * scale),
                Math.round(42 * scale), Math.round(12 * scale),
                Math.round(5 * scale), Math.round(5 * scale));
        // Самоцвет на чалме
        g2.setColor(new Color(220, 50, 80));
        g2.fillOval(Math.round(x + 34 * scale), Math.round(y - 74 * scale),
                Math.round(9 * scale), Math.round(9 * scale));
        g2.setColor(new Color(255, 140, 160));
        g2.fillOval(Math.round(x + 36 * scale), Math.round(y - 72 * scale),
                Math.round(4 * scale), Math.round(4 * scale));

        // ВИЛКА
        // Вилка держится горизонтально, острием вправо
        // При атаке — смещается вперёд (forkOffset > 0)
        Graphics2D gFork = (Graphics2D) g2.create();

        float fo = forkOffset * scale;

        int forkBaseX = Math.round(x + 50 * scale + fo);
        int forkBaseY = Math.round(y - 18 * scale);
        int forkTipX  = Math.round(x + 130 * scale + fo);

        // Ручка вилки (металлик)
        GradientPaint handleGrad = new GradientPaint(
                forkBaseX, forkBaseY - Math.round(5 * scale),
                new Color(160, 160, 170),
                forkBaseX, forkBaseY + Math.round(5 * scale),
                new Color(200, 200, 210)
        );
        gFork.setPaint(handleGrad);
        gFork.setStroke(new BasicStroke(8f * scale, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        gFork.drawLine(Math.round(x - 5 * scale + fo), forkBaseY,
                forkTipX - Math.round(15 * scale), forkBaseY);

        // Основание зубцов (перемычка)
        gFork.setStroke(new BasicStroke(6f * scale, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        gFork.setColor(new Color(185, 185, 195));
        gFork.drawLine(forkTipX - Math.round(15 * scale), forkBaseY - Math.round(7 * scale),
                forkTipX - Math.round(15 * scale), forkBaseY + Math.round(7 * scale));

        // Четыре зубца вилки
        gFork.setStroke(new BasicStroke(4f * scale, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int[] prongOffsets = {-9, -3, 3, 9};
        for (int dy : prongOffsets) {
            int py = forkBaseY + Math.round(dy * scale);
            gFork.setColor(new Color(175, 175, 185));
            gFork.drawLine(forkTipX - Math.round(15 * scale), py,
                    forkTipX, py);
            // Кончик зубца чуть светлее
            gFork.setColor(new Color(215, 215, 225));
            gFork.drawLine(forkTipX - Math.round(4 * scale), py,
                    forkTipX, py);
        }

        // Блик на ручке
        gFork.setColor(new Color(230, 230, 240, 140));
        gFork.setStroke(new BasicStroke(2f * scale));
        gFork.drawLine(Math.round(x - 5 * scale + fo), forkBaseY - Math.round(2 * scale),
                forkTipX - Math.round(20 * scale), forkBaseY - Math.round(2 * scale));

        gFork.dispose();
    }

    @Override
    protected void drawHealthBar(Graphics2D g2d, float k) {
        if (!isVisibleHealthBar) return;
        int barWidth  = Math.round(80 * k);
        int barHeight = Math.round(10 * k);
        int barX = Math.round(x + 38 * k) - barWidth / 2;
        int barY = Math.round(y - 25 * k); // над головой верблюда

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