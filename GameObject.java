package engine;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

public abstract class GameObject implements Cloneable {
    protected int id;
    protected float x;
    protected float y;
    protected int size;
    protected float speed;
    protected float speedTemp;

    protected Color color;
    protected int health;
    protected int attackDamage;
    protected float attackRange;
    protected float attackCooldown;
    protected float lastAttackTime;
    protected int fraction;
    protected boolean isAlive = true;
    protected int direction = 1;
    protected float scale;
    protected Engine engine = Engine.getInstance();
    protected boolean showHealthBar = true;

    public GameObject() {
        scale = this.size / 100.0f;
        if (scale <= 0) scale = 1.0f;
        id = -1;
        size = 50;
        speed = 0;
        color = Color.BLACK;
    }

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

        public Builder notVisibleHeathBar() {
            GameObject.this.showHealthBar = false;
            return this;
        }

        public GameObject build() {
            return GameObject.this;
        }
    }

    public void update(float dt) {
        x += (int) (speed);
    }

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

    public abstract void paintIcon(Component c, Graphics g, int x, int y);

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

    public void attack(GameObject target, float currentTime) {
        if (target == null || !target.isAlive()) return;
        if (distanceTo(target) > attackRange) return;
        if (currentTime - lastAttackTime < attackCooldown) return;

        target.takeDamage(this.attackDamage);
        lastAttackTime = currentTime;
    }

    public float distanceTo(GameObject other) {
        float dx = this.x - other.x;
        float dy = this.y - other.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public float distanceSqTo(GameObject other) {
        float dx = this.x - other.x;
        float dy = this.y - other.y;
        return dx * dx + dy * dy;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.fill(new Rectangle2D.Float(x, y, size, size));
    }

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
    public boolean isShowHealthBar() { return showHealthBar; }
    public void setShowHealthBar(boolean showHealthBar) { this.showHealthBar = showHealthBar; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GameObject that = (GameObject) o;
        return id == that.id && Float.compare(x, that.x) == 0 &&
                Float.compare(y, that.y) == 0 && Float.compare(speed, that.speed) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y, speed);
    }

    @Override
    public GameObject clone() {
        try {
            return (GameObject) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return "GameObject{" + "id=" + id + ", x=" + x + ", y=" + y +
                ", size=" + size + ", speed=" + speed + ", color=" + color + '}';
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