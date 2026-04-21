import java.awt.*;
import java.util.List;

public class UnitGunner extends GameObject {

    // Константы для логики
    private static final int ATTACK_RANGE = 200;
    private static final int DAMAGE = 15;
    private static final int ATTACK_COOLDOWN_MS = 1000;
    private static final float MOVE_SPEED = 80f;
    private static final int MAX_HEALTH = 50;

    private GameObject target;
    private long lastAttackTime;
    private transient Engine engine;
    private int currentHealth;

    public UnitGunner() {
        super(0, 0, 0, 50, 0, null);
        this.id = -1;
        this.currentHealth = MAX_HEALTH;
        this.health = MAX_HEALTH;
        this.isAlive = true;
        this.fraction = 1;
        this.lastAttackTime = 0;
        this.target = null;
        // Спавн у правого края (x = 750, ширина окна 800)
        this.x = 750;
        this.y = 300;
    }

    public UnitGunner(int id, float x, float y, int size, float speed) {
        super(id, x, y, size, speed, null);
        this.currentHealth = MAX_HEALTH;
        this.health = MAX_HEALTH;
        this.isAlive = true;
        this.fraction = 1;
        this.lastAttackTime = 0;
        this.target = null;
        // Если x и y не заданы или равны 0 - спавним у правого края
        if (x <= 0) {
            this.x = 750;
        }
        if (y <= 0) {
            this.y = 300;
        }
    }

    @Override
    protected void update(float dt) {
        if (!isAlive) return;

        if (engine == null) {
            engine = Engine.getInstance();
            if (engine == null) return;
        }

        findTarget();

        if (target != null && target.isAlive()) {
            float distanceToTarget = Math.abs(this.x - target.getX());
            float yDifference = Math.abs(this.y - target.getY());

            if (distanceToTarget <= ATTACK_RANGE && yDifference <= 50) {
                attack();
            } else {
                // Движение к цели
                if (target.getX() > this.x) {
                    this.x += MOVE_SPEED * dt;
                } else {
                    this.x -= MOVE_SPEED * dt;
                }

                if (target.getY() > this.y) {
                    this.y += 50 * dt;
                } else if (target.getY() < this.y) {
                    this.y -= 50 * dt;
                }
            }
        } else {
            // Если нет цели - просто идем ВЛЕВО (к правому краю экрана)
            this.x -= MOVE_SPEED * dt;
        }

        if (currentHealth <= 0 || this.x > 850 || this.x < -100) {
            isAlive = false;
        }
    }

    /**
     * Поиск цели - атакуем ВСЕХ, КРОМЕ БАШНИ И СЕБЯ
     */
    private void findTarget() {
        if (engine == null) return;

        List<GameObject> objects = engine.getObjects();
        if (objects == null) return;

        GameObject nearest = null;
        float minDistance = Float.MAX_VALUE;

        for (GameObject obj : objects) {
            if (obj == null) continue;
            if (!obj.isAlive()) continue;
            if (obj == this) continue;  // пропускаем себя

            // пропускаем башню
            if (obj instanceof Tower) continue;

            // пропускаем других ганнеров (чтобы не дрались между собой)
            if (obj.getClass().getSimpleName().equals("UnitGunner")) continue;

            // Атакуем всё остальное (лучников, всадников и т.д.)
            float distance = Math.abs(obj.getX() - this.x);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = obj;
            }
        }

        if (nearest != null) {
            target = nearest;
            System.out.println("🎯 Ганнер нашёл цель: " + target.getClass().getSimpleName());
        } else {
            target = null;
        }
    }

    private void attack() {
        if (target == null || !target.isAlive()) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttackTime >= ATTACK_COOLDOWN_MS) {
            target.takeDamage(DAMAGE);
            lastAttackTime = currentTime;
            System.out.println("💥 Ганнер АТАКУЕТ " + target.getClass().getSimpleName() +
                    "! Урон: " + DAMAGE);

            if (!target.isAlive()) {
                System.out.println("💀 " + target.getClass().getSimpleName() + " убит!");
                target = null;
            }
        }
    }

    @Override
    public void takeDamage(int damage) {
        this.currentHealth -= damage;
        this.health = this.currentHealth;
        System.out.println("💔 Ганнер получил урон: " + damage + ", HP: " + currentHealth);

        if (this.currentHealth <= 0) {
            this.isAlive = false;
            System.out.println("☠️ Ганнер уничтожен!");
        }
    }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setEngine(Engine engine) { this.engine = engine; }
    public int getCurrentHealth() { return currentHealth; }

    @Override
    public float getX() { return x; }
    @Override
    public float getY() { return y; }

    // Метод для спавна у правого края
    public void spawnAtRightEdge(float yPosition) {
        this.x = 750;  // Отступ от правого края
        this.y = yPosition;
        this.isAlive = true;
        this.currentHealth = MAX_HEALTH;
        this.health = MAX_HEALTH;
    }

    // ========== ВАШ ДИЗАЙН (НЕ МЕНЯЛ) ==========
    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int offsetX = (int) x;
        int offsetY = (int) y;

        g2.setColor(new Color(55, 120, 14));
        g2.fillOval(offsetX + 45, offsetY + 10, 30, 30);
        g2.setColor(Color.BLACK);
        g2.drawOval(offsetX + 45, offsetY + 10, 30, 30);

        g2.setColor(new Color(255, 233, 214));
        g2.fillRoundRect(offsetX + 50, offsetY + 20, 16, 14, 4, 4);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(offsetX + 50, offsetY + 20, 16, 14, 4, 4);

        g2.fillOval(offsetX + 53, offsetY + 25, 2, 2);
        g2.fillOval(offsetX + 60, offsetY + 25, 2, 2);

        g2.setColor(new Color(55, 120, 14));
        g2.fillRoundRect(offsetX + 51, offsetY + 36, 22, 18, 5, 5);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(offsetX + 51, offsetY + 36, 22, 18, 5, 5);

        g2.setColor(new Color(60, 60, 70));
        g2.fillRoundRect(offsetX + 64, offsetY + 37, 16, 11, 2, 2);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(offsetX + 64, offsetY + 37, 16, 11, 2, 2);

        g2.setColor(new Color(85, 90, 100));
        g2.fillRoundRect(offsetX + 25, offsetY + 39, 40, 7, 2, 2);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(offsetX + 25, offsetY + 39, 40, 7, 2, 2);

        g2.setColor(new Color(70, 70, 80));
        g2.fillRect(offsetX + 14, offsetY + 41, 11, 3);
        g2.setColor(Color.BLACK);
        g2.drawRect(offsetX + 14, offsetY + 41, 11, 3);

        g2.setColor(new Color(75, 75, 85));
        g2.fillRect(offsetX + 56, offsetY + 46, 5, 8);
        g2.setColor(Color.BLACK);
        g2.drawRect(offsetX + 56, offsetY + 46, 5, 8);

        g2.setColor(new Color(60, 60, 70));
        g2.fillRoundRect(offsetX + 41, offsetY + 46, 5, 7, 2, 2);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(offsetX + 41, offsetY + 46, 5, 7, 2, 2);

        g2.setColor(Color.RED);
        g2.fillRect(offsetX + 40, offsetY - 5, 50, 5);
        g2.setColor(Color.GREEN);
        int healthPercent = (int)((float)currentHealth / MAX_HEALTH * 50);
        g2.fillRect(offsetX + 40, offsetY - 5, healthPercent, 5);
        g2.setColor(Color.BLACK);
        g2.drawRect(offsetX + 40, offsetY - 5, 50, 5);

        if (target != null && target.isAlive()) {
            g2.setColor(Color.RED);
            g2.drawLine(offsetX + 60, offsetY + 25, (int)target.getX(), (int)target.getY());
        }
    }
}
