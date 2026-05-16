import javax.swing.*;
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
public class GameObject implements Cloneable, Icon {
    protected int id;
    protected float x;
    protected float y;
    protected int size;
    protected float speed;
    protected Color color;
    protected int health = 100;
    protected int attackDamage = 10;
    protected float attackRange = 50;
    protected float attackCooldown = 1f;
    protected float lastAttackTime = -5f;
    protected int fraction = 0;
    protected boolean isAlive = true;
    protected int direction = 1;
    protected Engine engine = Engine.getInstance();

    /**
     * Конструктор по умолчанию.
     * Создаёт объект с базовыми значениями.
     */
    public GameObject() {
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

    public GameObject(int size) {
        this.size = size;
    }

    /**
     * Обновляет позицию объекта.
     *
     * @param dt время кадра
     */
    protected void update(float dt) {
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

    public void takeDamage(int damage) {}
    public void destroy() {}

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

        if (distanceTo(target) > attackRange) return;

        if (currentTime - lastAttackTime < attackCooldown) return;

        target.takeDamage(this.attackDamage);

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
        int drawX = (int)(x - size / 2f);
        int drawY = (int)(y - size / 2f);
        g2d.fill(new Rectangle2D.Float(drawX, drawY, size, size));
    }

    // Геттеры и сеттеры
    public float getX() { return x; }
    public float getY() { return y; }
    public Color getColor() { return color; }
    public int getSize() { return size; }
    public int getId() { return id; }
    public float getSpeed() { return speed; }

    public void setId(int id) { this.id = id; }
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setSize(int size) { this.size = size; }
    public void setSpeed(float speed) { this.speed = speed; }
    public void setColor(Color color) { this.color = color; }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public int getAttackDamage() { return attackDamage; }
    public void setAttackDamage(int attackDamage) { this.attackDamage = attackDamage; }
    public float getAttackRange() { return attackRange; }
    public void setAttackRange(float attackRange) { this.attackRange = attackRange; }
    public float getAttackCooldown() { return attackCooldown; }
    public void setAttackCooldown(float attackCooldown) { this.attackCooldown = attackCooldown; }
    public float getLastAttackTime() { return lastAttackTime; }
    public void setLastAttackTime(float lastAttackTime) { this.lastAttackTime = lastAttackTime; }
    public int getFraction() { return fraction; }
    public void setFraction(int fraction) { this.fraction = fraction; }
    public boolean isAlive() { return isAlive; }
    public void setAlive(boolean alive) { isAlive = alive; }
    public int getDirection() { return direction; }
    public void setDirection(int direction) { this.direction = direction; }

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
            return new GameObject(this.id, this.x, this.y, this.size, this.speed, this.color);
        }
    }

    /**
     * Отрисовка как иконки.
     */
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        this.x = x;
        this.y = y;
        draw(g);
    }

    /**
     * Ширина иконки.
     */
    @Override
    public int getIconWidth() { return size; }

    /**
     * Высота иконки.
     */
    @Override
    public int getIconHeight() { return size; }

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
                '}';
    }

    public void start(float moveSpeed) { this.speed = moveSpeed; }
    public void stop() { this.speed = 0f; }
}