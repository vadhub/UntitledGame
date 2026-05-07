import java.awt.*;
import java.awt.geom.Path2D;
import java.util.List;

public class BaseUnit extends GameObject {

    private static final float BASE_SPEED = 20f;
    private static final float BASE_ATTACK_COOLDOWN = 1f;
    private float lastAttackTime = -5f;
    private boolean showHealthBar = true; // Флаг для отрисовки на иконках

    // КООРДИНАТЫ БАШНИ (ПОМЕНЯЙ НА СВОИ)
    private static final float TOWER_X = 500;
    private static final float TOWER_Y = 300;

    public BaseUnit() {
        this.fraction = 2;
    }

    public BaseUnit(int id, float x, float y, int size, float speed) {
        super(id, x, y, size, BASE_SPEED, Color.BLACK);
        attackCooldown = BASE_ATTACK_COOLDOWN;
        attackDamage = 50;
        health = 100;
        fraction = 2;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void update(float deltaTime) {
        if (!isAlive) return;

        // Движение ТОЛЬКО по горизонтали к башне
        if (x < TOWER_X) {
            x += BASE_SPEED * deltaTime;
        } else if (x > TOWER_X) {
            x -= BASE_SPEED * deltaTime;
        }
        // Y не меняем — идём строго прямо!

        // Проверка дистанции для атаки (только по X)
        float distX = Math.abs(x - TOWER_X);
        if (distX <= 10) {
            if (engine != null) {
                float currentTime = engine.getGameTime();
                if (currentTime - lastAttackTime >= attackCooldown) {
                    List<GameObject> objects = engine.getObjects();
                    if (objects != null) {
                        for (GameObject obj : objects) {
                            if (obj == null) continue;
                            String className = obj.getClass().getSimpleName();
                            if (className.contains("Tower") && obj.isAlive()) {
                                obj.takeDamage(attackDamage);
                                System.out.println(" BASE UNIT HITS " + className + " FOR " + attackDamage + " DAMAGE! ");
                                lastAttackTime = currentTime;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        float k = this.size / 100.0f;
        if (k <= 0) k = 1.0f;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillOval(Math.round(x - 25 * k), Math.round(y + 50 * k),
                Math.round(150 * k), Math.round(20 * k));

        g2.setColor(new Color(108, 29, 13));
        g2.fillRoundRect(Math.round(x + 15 * k), Math.round(y - 40 * k),
                Math.round(40 * k), Math.round(50 * k),
                Math.round(35 * k), Math.round(75 * k));

        g2.setColor(new Color(217, 142, 73));
        g2.fillOval(Math.round(x + 17 * k), Math.round(y - 65 * k),
                Math.round(35 * k), Math.round(35 * k));

        g2.setColor(Color.BLACK);
        g2.fillOval(Math.round(x + 42 * k), Math.round(y - 57 * k),
                Math.round(5 * k), Math.round(10 * k));
        g2.fillOval(Math.round(x + 33 * k), Math.round(y - 55 * k),
                Math.round(5 * k), Math.round(10 * k));

        Graphics2D gBat = (Graphics2D) g2.create();
        double angle = Math.toRadians(-40);
        int bx = Math.round(x + 50 * k);
        int by = Math.round(y - 20 * k);

        gBat.rotate(angle, bx, by);

        Path2D bat = new Path2D.Float();
        bat.moveTo(bx - 18 * k, by);
        bat.lineTo(bx - 12 * k, by - 5 * k);
        bat.lineTo(bx - 8 * k, by - 6 * k);
        bat.lineTo(bx, by - 5 * k);
        bat.lineTo(bx + 35 * k, by - 5 * k);
        bat.lineTo(bx + 55 * k, by - 10 * k);
        bat.lineTo(bx + 100 * k, by - 15 * k);
        bat.lineTo(bx + 115 * k, by - 10 * k);
        bat.lineTo(bx + 115 * k, by);
        bat.lineTo(bx + 115 * k, by + 10 * k);
        bat.lineTo(bx + 100 * k, by + 15 * k);
        bat.lineTo(bx + 55 * k, by + 10 * k);
        bat.lineTo(bx + 35 * k, by + 5 * k);
        bat.lineTo(bx, by + 5 * k);
        bat.lineTo(bx - 8 * k, by + 6 * k);
        bat.lineTo(bx - 12 * k, by + 5 * k);
        bat.closePath();

        GradientPaint metal = new GradientPaint(
                bx - 10 * k, by,
                new Color(200, 40, 40),
                bx + 100 * k, by,
                new Color(100, 20, 20)
        );

        gBat.setPaint(metal);
        gBat.fill(bat);
        gBat.setColor(new Color(20, 20, 20));
        gBat.fillRoundRect(bx - Math.round(5 * k), by - Math.round(6 * k),
                Math.round(40 * k), Math.round(12 * k), 8, 8);
        gBat.setColor(new Color(255, 220, 220, 120));
        gBat.setStroke(new BasicStroke(3 * k));
        gBat.drawLine(Math.round(bx + 20 * k), Math.round(by - 2 * k),
                Math.round(bx + 105 * k), Math.round(by - 8 * k));
        gBat.dispose();

        // Полоска здоровья — рисуем только если флаг включен
        if (showHealthBar) {
            drawHealthBar(g2, k);
        }
    }

    private void drawHealthBar(Graphics2D g2d, float k) {
        int barWidth = 60;
        int barHeight = 8;
        int barX = Math.round(x + 5 * k);
        int barY = Math.round(y - 10 * k);
        g2d.setColor(Color.RED);
        g2d.fillRect(barX, barY, barWidth, barHeight);
        g2d.setColor(Color.GREEN);
        int healthPercent = (int) ((float) health / 100f * barWidth);
        healthPercent = Math.max(0, Math.min(barWidth, healthPercent));
        g2d.fillRect(barX, barY, healthPercent, barHeight);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(barX, barY, barWidth, barHeight);
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        float oldX = this.x;
        float oldY = this.y;
        this.x = x;
        this.y = y + 40;
        showHealthBar = false; // Отключаем полоску для иконки
        draw(g);
        showHealthBar = true;  // Включаем обратно для игры
        this.x = oldX;
        this.y = oldY;
    }

    @Override
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            isAlive = false;
        }
    }
}
