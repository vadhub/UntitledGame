import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

public class GameObject implements Cloneable {
    private final int id;
    private float x, y;
    private final int size;
    private final float speed;
    private final Color color;

    public GameObject(int id, float x, float y, int size, float speed, Color color) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
        this.color = color;
    }

    public void update() {
        x += (int) (speed);
    }

    public void moveTowards(GameObject target) {
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

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.fill(new Rectangle2D.Float(x, y, size, size)); // float coords
//        g.fillRect((int) x, (int) y, size, size); // int coords
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public int getSize() { return size; }
    public int getId() { return id; }
    public float getSpeed() { return speed; }
    public Color getColor() { return color; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GameObject that = (GameObject) o;
        return id == that.id && Float.compare(x, that.x) == 0 && Float.compare(y, that.y) == 0 && size == that.size && Float.compare(speed, that.speed) == 0 && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y, size, speed, color);
    }

    @Override
    public GameObject clone() {
        try {
            return (GameObject) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}