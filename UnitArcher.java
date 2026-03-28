import javax.swing.*;
import java.awt.*;

// by Bebron28
// edit by vadhub
// edit by Deepseek

public class UnitArcher extends GameObject {

    private GameObject currentTarget;

    public UnitArcher() {
        attackCooldown = 5.0f;
        this.lastAttackTime = -5.0f;
    }

    public UnitArcher(int id, float x, float y, int size, float speed) {
        super(id, x, y, size, speed, Color.BLACK);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime); // базовое движение, если есть

        // Проверяем, жива ли текущая цель и не сменила ли фракцию
        if (currentTarget != null && (!currentTarget.isAlive() || currentTarget.getFraction() == this.fraction)) {
            currentTarget = null; // цель мертва или стала своей
        }

        // Если нет цели, ищем новую в удвоенном радиусе
        if (currentTarget == null) {
            currentTarget = engine.findNearestEnemy(this, attackRange * 2);
        }

        // Если есть цель — действуем
        if (currentTarget != null) {
            float dist = distanceTo(currentTarget);
            if (dist > attackRange) {
                // Вне радиуса атаки — двигаемся к цели
                moveTowards(currentTarget, deltaTime);
                setSpeed(2);
            } else {
                // В радиусе атаки — стреляем, если прошло достаточно времени
                if (canAttack(engine.getGameTime())) {
                    // Простой угол по направлению к цели (можно заменить на баллистику)
                    float angle = Arrow.calculateArrowAngle(x, y, currentTarget.x, currentTarget.y, 600);
                    Arrow arrow = new Arrow(x, y, angle, 600);
                    arrow.setAttackDamage(attackDamage);
                    arrow.setFraction(fraction);
                    engine.spawnObject(arrow);
                    lastAttackTime = engine.getGameTime(); // обновляем время атаки
                }
                setSpeed(0);
            }
        }
    }
    public void draw(Graphics g) {
        // базовая точка (x, y) соответствует координатам (150, 50) в исходном коде
        float k = this.size / 100.0f;
        if (k <= 0) k = 1.0f;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Голова (оригинал: 150,50, 70x80)
        g2d.setColor(new Color(255, 218, 185));
        g2d.fillOval(Math.round(x + 0 * k), Math.round(y + 0 * k),
                Math.round(70 * k), Math.round(80 * k));

        // Глаза
        g2d.setColor(Color.BLACK);
        g2d.fillOval(Math.round(x + 25 * k), Math.round(y + 30 * k),
                Math.round(10 * k), Math.round(12 * k));
        g2d.fillOval(Math.round(x + 50 * k), Math.round(y + 30 * k),
                Math.round(10 * k), Math.round(12 * k));

        // Тело (оригинал: 140,130, 90x140)
        g2d.setColor(new Color(70, 130, 180)); // стальной синий
        g2d.fillOval(Math.round(x - 10 * k), Math.round(y + 80 * k),
                Math.round(90 * k), Math.round(140 * k));

        // Лук (дуга)
        g2d.setColor(new Color(101, 67, 33)); // коричневый
        g2d.setStroke(new BasicStroke(4.0f * k));
        g2d.drawArc(Math.round(x + 30 * k), Math.round(y + 50 * k),
                Math.round(100 * k), Math.round(150 * k),
                270, 190);

        // Тетива
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2.0f * k));
        int bowCenterX = Math.round(x + 80 * k);
        int bowTopY = Math.round(y + 52 * k);
        int bowBottomY = Math.round(y + 200 * k);
        g2d.drawLine(bowCenterX, bowTopY, bowCenterX, bowBottomY);

        // Колчан
        g2d.setColor(new Color(139, 69, 19));
        g2d.setStroke(new BasicStroke(k));
        g2d.fillRect(Math.round(x - 30 * k), Math.round(y + 100 * k),
                Math.round(25 * k), Math.round(60 * k));
        g2d.fillOval(Math.round(x - 30 * k), Math.round(y + 95 * k),
                Math.round(25 * k), Math.round(20 * k));

        // Стрелы в колчане
        g2d.setColor(Color.DARK_GRAY);
        for (int i = 0; i < 3; i++) {
            int yOffset = 110 + i * 15;
            g2d.drawLine(Math.round(x - 25 * k), Math.round(y + yOffset * k),
                    Math.round(x - 10 * k), Math.round(y + yOffset * k));
        }
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        this.x = x;
        this.y = y;
        draw(g);
    }
}