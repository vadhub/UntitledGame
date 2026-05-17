package src.view;

import src.engine.Engine;
import src.engine.GameObject;

import java.awt.*;
import java.util.List;

/**
 * Стрела с баллистикой и коллизией.
 * Оптимизирован: убраны лишние операции, добавлены проверки.
 * By vadhub
 * edit by DeepSeek
 * edit by AmericanCoolBoyUSA777
 */
public class Arrow extends GameObject {
    protected float vx, vy;
    protected static final float GRAVITY = 800f;

    public Arrow(float startX, float startY, float angleDeg, float speed) {
        super(-2, startX, startY, 30, speed);
        float angleRad = (float) Math.toRadians(angleDeg);
        this.vx = speed * (float) Math.cos(angleRad);
        this.vy = speed * (float) Math.sin(angleRad);
        this.attackDamage = 20;
    }

    @Override
    public void update(float dt) {
        // перемещение с учётом гравитации
        x += vx * dt;
        y += vy * dt;
        vy += GRAVITY * dt;

        // удаление стрелы при вылете за границы экрана
        if (x < -100 || x > engine.getScreenWidth() + 100 ||
                y < -100 || y > engine.getScreenHeight() + 100) {
            isAlive = false;
            return;
        }

        // проверка столкновений с другими объектами
        List<GameObject> objects = engine.getObjects();
        for (GameObject obj : objects) {
            if (obj == this || !obj.isAlive()) continue;
            if (obj.getFraction() == fraction) continue;

            if (Engine.getInstance().collisionCircle(this, obj)) {
                obj.takeDamage(attackDamage);
                isAlive = false;
                break;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));

        // определение направления полёта
        float angle = (float) Math.atan2(vy, vx);
        int length = 35;

        int startX = (int) x;
        int startY = (int) y;
        int endX = (int) (x + length * Math.cos(angle));
        int endY = (int) (y + length * Math.sin(angle));

        g2d.drawLine(startX, startY, endX, endY);

        // отрисовка наконечника
        int tipSize = 8;
        int backX = (int) (endX - tipSize * 0.5 * Math.cos(angle));
        int backY = (int) (endY - tipSize * 0.5 * Math.sin(angle));

        int[] xPoints = {
                endX,
                (int) (backX - tipSize * 0.3 * Math.sin(angle)),
                (int) (backX + tipSize * 0.3 * Math.sin(angle))
        };
        int[] yPoints = {
                endY,
                (int) (backY + tipSize * 0.3 * Math.cos(angle)),
                (int) (backY - tipSize * 0.3 * Math.cos(angle))
        };
        g2d.fillPolygon(xPoints, yPoints, 3);

        // отрисовка оперения
        float perpX = (float) Math.sin(angle);
        float perpY = (float) -Math.cos(angle);
        g2d.drawLine(startX, startY,
                (int) (startX - 8 * perpX),
                (int) (startY - 8 * perpY));
        g2d.drawLine(startX, startY,
                (int) (startX + 8 * perpX),
                (int) (startY + 8 * perpY));
    }

    /**
     * Расчёт угла для баллистической траектории.
     * @return угол в градусах
     */
    public static float calculateArrowAngle(float startX, float startY,
                                            float targetX, float targetY,
                                            float speed) {
        float dx = targetX - startX;
        float dy = targetY - startY;

        if (dx == 0) {
            if (dy == 0) return 0;
            return dy > 0 ? -90f : 90f;
        }

        float angleRad = getAngleRad(speed, dx, dy);
        return (float) Math.toDegrees(angleRad);
    }

    private static float getAngleRad(float speed, float dx, float dy) {
        float A = (GRAVITY * dx * dx) / (2 * speed * speed);
        float discriminant = dx * dx - 4 * A * (A - dy);

        if (discriminant < 0) {
            // стрельба по прямой, если баллистика не достигает цели
            return (float) Math.atan2(dy, dx);
        }

        float sqrtD = (float) Math.sqrt(discriminant);
        float u1 = (-dx + sqrtD) / (2 * A);
        float u2 = (-dx - sqrtD) / (2 * A);
        float u = Math.abs(u1) < Math.abs(u2) ? u1 : u2;

        float cosTheta = Math.signum(dx) / (float) Math.sqrt(1 + u * u);
        float sinTheta = u * cosTheta;
        return (float) Math.atan2(sinTheta, cosTheta);
    }
}
