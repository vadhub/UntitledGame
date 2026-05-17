package src.engine;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

/**
 * Класс GameObject описывает объект в игре.
 * У него есть позиция, размер, скорость и боевые характеристики.
 * Также реализует интерфейс Icon для отображения.
 *
 * Может использоваться как базовый класс для других объектов.
 */

public class GameObject implements Cloneable {
    protected int id;
    protected float x;
    protected float y;
    protected int size;
    protected float speed;
    protected float speedTemp;

    protected Color color;
    protected int health;
    protected int maxHealth = 100;
    protected int attackDamage;
    protected float attackRange;
    protected float attackCooldown;
    protected float lastAttackTime;
    protected int fraction;
    protected boolean isAlive = true;
    protected int direction = 1;
    protected float scale;
    protected Engine engine = Engine.getInstance();

    /**
     * Конструктор по умолчанию.
     * Создаёт объект с базовыми значениями.
     */
    public GameObject() {
        scale = this.size / 100.0f;
        if (scale <= 0) scale = 1.0f;
        id = -1;
        size = 50;
        speed = 0;
        color = Color.BLACK;
    }

    /**
     * Конструктор с основными параметрами.
     *
     * @param id идентификатор объекта
     * @param x позиция по X
     * @param y позиция по Y
     * @param size размер объекта
     * @param speed скорость движения
     * @param color цвет объекта
     */
    public GameObject(int id, float x, float y, int size, float speed, Color color) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
        this.color = color;
    }

    public GameObject(int id, float x, float y, int size, float speed) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
    }

    public GameObject(float x, float y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public GameObject(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public GameObject(int size) {
        this.size = size;
    }

    public static Builder builder() {
        return new GameObject().new Builder();
    }

    public class Builder {
        protected Builder() {}

        public Builder id(int id) {
            GameObject.this.id = id;
            return this;
        }

        public Builder x(float x) {
            GameObject.this.x = x;
            return this;
        }

        public Builder y(float y) {
            GameObject.this.y = y;
            return this;
        }

        public Builder size(int size) {
            GameObject.this.size = size;
            return this;
        }

        public Builder speed(int speed) {
            GameObject.this.speed = speed;
            return this;
        }

        public Builder color(Color color) {
            GameObject.this.color = color;
            return this;
        }

        public Builder health(int health) {
            GameObject.this.health = health;
            GameObject.this.maxHealth = Math.max(GameObject.this.maxHealth, health);
            return this;
        }

        public Builder maxHealth(int maxHealth) {
            GameObject.this.maxHealth = maxHealth;
            return this;
        }

        public Builder attackDamage(int attackDamage) {
            GameObject.this.attackDamage = attackDamage;
            return this;
        }

        public Builder attackRange(float attackRange) {
            GameObject.this.attackRange = attackRange;
            return this;
        }

        public Builder attackCooldown(float attackCooldown) {
            GameObject.this.attackCooldown = attackCooldown;
            return this;
        }

        public Builder lastAttackTime(float lastAttackTime) {
            GameObject.this.lastAttackTime = lastAttackTime;
            return this;
        }

        public Builder isAlive(boolean isAlive) {
            GameObject.this.isAlive = isAlive;
            return this;
        }

        public Builder fraction(int fraction) {
            GameObject.this.fraction = fraction;
            return this;
        }

        public Builder direction(int direction) {
            GameObject.this.direction = direction;
            return this;
        }

        public GameObject build() {
            return GameObject.this;
        }
    }

    /**
     * Обновляет позицию объекта.
     *
     * @param dt время кадра
     */
    public void update(float dt) {
        x += (int) (speed);
    }

    /**
     * Двигает объект к другой цели.
     *
     * @param target цель
     * @param dt время кадра
     */
    public void moveTowards(GameObject target, float dt) {
        float dirX = target.x - this.x;
        float dirY = target.y - this.y;
        float distance = (float) Math.sqrt(dirX * dirX + dirY * dirY);

        if (distance > 1.0f) {
            float normX = dirX / distance;
            float normY = dirY / distance;

            this.x += normX * speed;
            this.y += normY * speed;
        }
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            destroy();
        }
    }

    public void destroy() {
        isAlive = false;
    }

    public boolean canAttack(float currentTime) {
        return currentTime - lastAttackTime >= attackCooldown;
    }

    /**
     * Выполняет атаку по цели.
     *
     * @param target цель
     * @param currentTime текущее время
     */
    public void attack(GameObject target, float currentTime) {
        if (target == null || !target.isAlive()) return;

        // проверка дистанции
        if (distanceTo(target) > attackRange) return;

        // проверка кулдауна
        if (currentTime - lastAttackTime < attackCooldown) return;

        // наносим урон
        target.takeDamage(this.attackDamage);

        // обновляем время последней атаки
        lastAttackTime = currentTime;

    }

    /**
     * Считает расстояние до объекта.
     *
     * @param other другой объект
     * @return расстояние
     */
    public float distanceTo(GameObject other) {
        float dx = this.x - other.x;
        float dy = this.y - other.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Считает расстояние без корня (быстрее).
     *
     * @param other другой объект
     * @return квадрат расстояния
     */
    public float distanceSqTo(GameObject other) {
        float dx = this.x - other.x;
        float dy = this.y - other.y;
        return dx * dx + dy * dy;
    }

    /**
     * Отрисовывает объект.
     * Сделал public чтобы можно было переопределять в дочерних классах
     *
     * @param g графика
     */
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.fill(new Rectangle2D.Float(x, y, size, size)); // float coords
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    public int getSize() {
        return size;
    }

    public int getId() {
        return id;
    }

    public float getSpeed() {
        return speed;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        this.maxHealth = Math.max(this.maxHealth, health);
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public float getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(float attackRange) {
        this.attackRange = attackRange;
    }

    public float getAttackCooldown() {
        return attackCooldown;
    }

    public void setAttackCooldown(float attackCooldown) {
        this.attackCooldown = attackCooldown;
    }

    public float getLastAttackTime() {
        return lastAttackTime;
    }

    public void setLastAttackTime(float lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }

    public int getFraction() {
        return fraction;
    }

    public void setFraction(int fraction) {
        this.fraction = fraction;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Проверка на равенство объектов.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GameObject that = (GameObject) o;
        return id == that.id && Float.compare(x, that.x) == 0 && Float.compare(y, that.y) == 0 && Float.compare(speed, that.speed) == 0;
    }

    /**
     * Генерация hashCode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, x, y, speed);
    }

    /**
     * Клонирование объекта.
     */
    @Override
    public GameObject clone() {
        try {
            return (GameObject) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * Строковое представление объекта.
     */
    @Override
    public String toString() {
        return "GameObject{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", size=" + size +
                ", speed=" + speed +
                ", color=" + color +
                '}';
    }

    public void start() {
        speed = speedTemp;
        System.out.println(speed);
    }

    public void stop() {
        if (speed != 0) {
            speedTemp = speed;
        }
        System.out.println("stop " + speedTemp);
        speed = 0;
    }
}
