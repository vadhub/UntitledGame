package src.view.unit;

import src.engine.GameObject;

import java.awt.*;

/**
 * Всадник на крокодиле: вооружён кокосовым пистолетом и бананом.
 * Спавнится, идёт к башне, останавливается и атакует.
 *
 * by Belyakina Maria
 */
public class UnitCrocoBanan extends BaseUnit {

    // Переменные для анимации атаки (отдача кокосового пистолета)
    private boolean isAttacking = false;
    private float attackAnimationTime = 0f;
    private final float ATTACK_ANIMATION_DURATION = 0.35f;

    // Угол пистолета: плавная отдача при выстреле
    private float currentGunAngle = 0f;
    private float targetGunAngle  = 0f;

    // Анимация банана (покачивание в руке)
    private float bananaWobble = 0f;

    public static Builder builder() {
        return new UnitCrocoBanan().new Builder();
    }

    public class Builder extends BaseUnit.Builder {
        private Builder() {
            super();
        }

        public UnitCrocoBanan build() {
            return UnitCrocoBanan.this;
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Покачивание банана во время ходьбы
        bananaWobble += deltaTime * 3f;

        if (isAttacking) {
            attackAnimationTime += deltaTime;
            float progress = attackAnimationTime / ATTACK_ANIMATION_DURATION;

            // Две фазы: резкая отдача вверх → плавный возврат
            if (progress <= 0.3f) {
                float t = progress / 0.3f;
                targetGunAngle = t * -30f;          // ствол задирается вверх (отдача)
            } else {
                float t = (progress - 0.3f) / 0.7f;
                targetGunAngle = -30f + t * 30f;    // возврат в исходное
            }

            currentGunAngle = currentGunAngle + (targetGunAngle - currentGunAngle) * 0.35f;

            if (attackAnimationTime >= ATTACK_ANIMATION_DURATION) {
                isAttacking    = false;
                currentGunAngle = 0f;
                targetGunAngle  = 0f;
            }
        } else {
            currentGunAngle = currentGunAngle + (0f - currentGunAngle) * 0.2f;
        }
    }

    @Override
    public void attack(GameObject target, float currentTime) {
        if (target == null || !target.isAlive()) return;
        if (distanceTo(target) > attackRange) return;
        if (currentTime - lastAttackTime < attackCooldown) return;

        if (!isAttacking) {
            isAttacking        = true;
            attackAnimationTime = 0f;
        }

        target.takeDamage(attackDamage);
        lastAttackTime = currentTime;

        System.out.println("CrocoBanan стреляет кокосом! Урон: " + attackDamage);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

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

        // Тень
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillOval(Math.round(x - 20 * scale), Math.round(y + 48 * scale),
                Math.round(160 * scale), Math.round(18 * scale));

        // КРОКОДИЛ

        // хвост
        g2.setColor(new Color(100, 130, 60));
        int[] tailX = {
                Math.round(x - 35 * scale),
                Math.round(x + 5  * scale),
                Math.round(x + 5  * scale)
        };
        int[] tailY = {
                Math.round(y + 25 * scale),
                Math.round(y + 10 * scale),
                Math.round(y + 40 * scale)
        };
        g2.fillPolygon(tailX, tailY, 3);

        // задние ноги
        g2.setColor(new Color(90, 120, 55));
        g2.fillRoundRect(Math.round(x + 20 * scale), Math.round(y + 28 * scale),
                Math.round(16 * scale), Math.round(25 * scale),
                Math.round(8 * scale), Math.round(8 * scale));
        g2.fillRoundRect(Math.round(x + 75 * scale), Math.round(y + 28 * scale),
                Math.round(16 * scale), Math.round(25 * scale),
                Math.round(8 * scale), Math.round(8 * scale));
        // лапы задних ног
        g2.setColor(new Color(80, 110, 50));
        g2.fillRoundRect(Math.round(x + 16 * scale), Math.round(y + 48 * scale),
                Math.round(24 * scale), Math.round(8 * scale),
                Math.round(6 * scale), Math.round(6 * scale));
        g2.fillRoundRect(Math.round(x + 71 * scale), Math.round(y + 48 * scale),
                Math.round(24 * scale), Math.round(8 * scale),
                Math.round(6 * scale), Math.round(6 * scale));

        // тело крокодила — основное
        g2.setColor(new Color(110, 145, 65));
        g2.fillRoundRect(Math.round(x + 0  * scale), Math.round(y + 5  * scale),
                Math.round(115 * scale), Math.round(45 * scale),
                Math.round(20 * scale), Math.round(40 * scale));

        // бронированные пластины на спине
        g2.setColor(new Color(85, 115, 50));
        for (int i = 0; i < 5; i++) {
            g2.fillRoundRect(
                    Math.round((x + 20 + i * 18) * scale), Math.round(y + 2  * scale),
                    Math.round(12 * scale),              Math.round(10 * scale),
                    Math.round(4  * scale),              Math.round(4  * scale));
        }

        // передние ноги
        g2.setColor(new Color(90, 120, 55));
        g2.fillRoundRect(Math.round(x + 10 * scale), Math.round(y + 28 * scale),
                Math.round(16 * scale), Math.round(22 * scale),
                Math.round(8 * scale), Math.round(8 * scale));
        g2.fillRoundRect(Math.round(x + 88 * scale), Math.round(y + 28 * scale),
                Math.round(16 * scale), Math.round(22 * scale),
                Math.round(8 * scale), Math.round(8 * scale));
        // лапы передних ног
        g2.setColor(new Color(80, 110, 50));
        g2.fillRoundRect(Math.round(x + 6  * scale), Math.round(y + 46 * scale),
                Math.round(24 * scale), Math.round(8 * scale),
                Math.round(6 * scale), Math.round(6 * scale));
        g2.fillRoundRect(Math.round(x + 84 * scale), Math.round(y + 46 * scale),
                Math.round(24 * scale), Math.round(8 * scale),
                Math.round(6 * scale), Math.round(6 * scale));

        // шея + голова крокодила
        g2.setColor(new Color(110, 145, 65));
        g2.fillRoundRect(Math.round(x + 95 * scale), Math.round(y + 5  * scale),
                Math.round(30 * scale), Math.round(30 * scale),
                Math.round(15 * scale), Math.round(15 * scale));

        // пасть (нижняя челюсть)
        g2.setColor(new Color(95, 130, 58));
        g2.fillRoundRect(Math.round(x + 100 * scale), Math.round(y + 20 * scale),
                Math.round(45 * scale), Math.round(18 * scale),
                Math.round(12 * scale), Math.round(18 * scale));
        // пасть (верхняя челюсть)
        g2.setColor(new Color(110, 145, 65));
        g2.fillRoundRect(Math.round(x + 100 * scale), Math.round(y + 10 * scale),
                Math.round(50 * scale), Math.round(18 * scale),
                Math.round(12 * scale), Math.round(18 * scale));

        // зубы
        g2.setColor(Color.WHITE);
        for (int i = 0; i < 4; i++) {
            g2.fillPolygon(
                    new int[]{
                            Math.round((x + 107 + i * 9) * scale),
                            Math.round((x + 111 + i * 9) * scale),
                            Math.round((x + 109 + i * 9) * scale)
                    },
                    new int[]{
                            Math.round(y + 26 * scale),
                            Math.round(y + 26 * scale),
                            Math.round(y + 32 * scale)
                    }, 3);
        }

        // глаз крокодила
        g2.setColor(new Color(255, 220, 0));
        g2.fillOval(Math.round(x + 105 * scale), Math.round(y + 8  * scale),
                Math.round(10 * scale), Math.round(10 * scale));
        g2.setColor(Color.BLACK);
        g2.fillOval(Math.round(x + 108 * scale), Math.round(y + 11 * scale),
                Math.round(5  * scale), Math.round(5  * scale));

        // ВСАДНИК

        // юбка
        g2.setColor(new Color(180, 100, 20));
        g2.fillRoundRect(Math.round(x + 28 * scale), Math.round(y - 12 * scale),
                Math.round(30 * scale), Math.round(18 * scale),
                Math.round(8 * scale), Math.round(8 * scale));

        // тело всадника
        g2.setColor(new Color(60, 35, 20));
        g2.fillRoundRect(Math.round(x + 25 * scale), Math.round(y - 38 * scale),
                Math.round(36 * scale), Math.round(35 * scale),
                Math.round(18 * scale), Math.round(28 * scale));

        // ожерелье
        g2.setColor(new Color(220, 60, 60));
        g2.drawArc(Math.round(x + 27 * scale), Math.round(y - 32 * scale),
                Math.round(32 * scale), Math.round(16 * scale),
                200, 140);
        g2.setColor(new Color(60, 180, 60));
        g2.drawArc(Math.round(x + 30 * scale), Math.round(y - 29 * scale),
                Math.round(26 * scale), Math.round(13 * scale),
                200, 140);

        // голова
        g2.setColor(new Color(60, 35, 20));
        g2.fillOval(Math.round(x + 22 * scale), Math.round(y - 70 * scale),
                Math.round(42 * scale), Math.round(40 * scale));

        // глаза (пустые, как у DinoRider)
        g2.setColor(Color.BLACK);
        g2.fillOval(Math.round(x + 33 * scale), Math.round(y - 60 * scale),
                Math.round(6 * scale), Math.round(9 * scale));
        g2.fillOval(Math.round(x + 46 * scale), Math.round(y - 60 * scale),
                Math.round(6 * scale), Math.round(9 * scale));

        //  БАНАН (левая рука, покачивается)
        Graphics2D gBanana = (Graphics2D) g2.create();
        int bPivotX = Math.round(x + 22 * scale);
        int bPivotY = Math.round(y - 20 * scale);
        float bWobbleAngle = (float) Math.sin(bananaWobble) * 8f;
        gBanana.rotate(Math.toRadians(bWobbleAngle), bPivotX, bPivotY);

        // тело банана
        gBanana.setColor(new Color(255, 220, 0));
        gBanana.fillArc(Math.round(x - 10 * scale), Math.round(y - 30 * scale),
                Math.round(36 * scale), Math.round(22 * scale),
                10, 160);
        // кончики
        gBanana.setColor(new Color(160, 120, 0));
        gBanana.fillOval(Math.round(x - 11 * scale), Math.round(y - 20 * scale),
                Math.round(6 * scale), Math.round(6 * scale));
        gBanana.fillOval(Math.round(x + 22 * scale), Math.round(y - 30 * scale),
                Math.round(6 * scale), Math.round(6 * scale));

        gBanana.dispose();

        // КОКОСОВЫЙ ПИСТОЛЕТ
        Graphics2D gGun = (Graphics2D) g2.create();
        int gPivotX = Math.round(x + 60 * scale);
        int gPivotY = Math.round(y - 25 * scale);
        gGun.rotate(Math.toRadians(currentGunAngle), gPivotX, gPivotY);

        // рукоять пистолета
        gGun.setColor(new Color(80, 50, 20));
        gGun.fillRoundRect(Math.round(x + 55 * scale), Math.round(y - 15 * scale),
                Math.round(10 * scale), Math.round(18 * scale),
                Math.round(5 * scale), Math.round(5 * scale));
        // ствол
        gGun.setStroke(new BasicStroke(5f * scale));
        gGun.setColor(new Color(130, 130, 130));
        gGun.drawLine(Math.round(x + 60 * scale), Math.round(y - 28 * scale),
                Math.round(x + 90 * scale), Math.round(y - 28 * scale));

        // кокос на стволе (глушитель-кокос)
        gGun.setColor(new Color(140, 100, 50));
        gGun.fillOval(Math.round(x + 78 * scale), Math.round(y - 37 * scale),
                Math.round(22 * scale), Math.round(22 * scale));
        // волокна кокоса
        gGun.setColor(new Color(110, 75, 30));
        gGun.setStroke(new BasicStroke(1.5f * scale));
        for (int i = 0; i < 5; i++) {
            gGun.drawLine(
                    Math.round((x + 80 + i * 3) * scale), Math.round(y - 36 * scale),
                    Math.round((x + 79 + i * 3) * scale), Math.round(y - 16 * scale));
        }
        // отверстие ствола
        gGun.setColor(Color.BLACK);
        gGun.fillOval(Math.round(x + 85 * scale), Math.round(y - 32 * scale),
                Math.round(8 * scale), Math.round(8 * scale));

        gGun.dispose();
    }

    @Override
    protected void drawHealthBar(Graphics2D g2d, float k) {
        if (!isVisibleHealthBar) return;
        int barWidth  = Math.round(80 * k);
        int barHeight = Math.round(10 * k);
        int barX      = Math.round(x + 43 * k) - barWidth / 2;
        int barY      = Math.round(y - 92 * k); // над афро
        g2d.setColor(Color.RED);
        g2d.fillRect(barX, barY, barWidth, barHeight);
        g2d.setColor(Color.GREEN);
        int healthWidth = (int) ((float) health / 100f * barWidth);
        healthWidth = Math.max(0, Math.min(barWidth, healthWidth));
        g2d.fillRect(barX, barY, healthWidth, barHeight);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(barX, barY, barWidth, barHeight);
    }
}