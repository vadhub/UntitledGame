import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Engine {

    private float gameTime = 0;
    private float deltaTime = 0;
    private int screenWidth = 800;
    private int screenHeight = 600;
    private final List<GameObject> objects = new ArrayList<>();
    private final Random random = new Random();
    private static Engine engine = null;

    private Engine() {
    }

    public static Engine getInstance() {
        if (engine == null) {
            engine = new Engine();
        }
        return engine;
    }

    public void update(float deltaTime) {
        this.deltaTime = deltaTime;
        gameTime += deltaTime;
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).update(deltaTime);
        }
        objects.removeIf(obj -> !obj.isAlive());
    }

    public void draw(Graphics g) {
        synchronized (objects) {
            for (GameObject object : objects) {
                if (object.isAlive()) {
                    object.draw(g);
                }
            }
        }
    }

    public void spawnObject() {
        float x = random.nextInt(screenWidth);
        float y = random.nextInt(screenHeight);
        Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        GameObject newObject = new GameObject(-1, x, y, 30, 100, color);
        spawnObject(newObject);
    }

    public void spawnObject(GameObject gameObject) {
        synchronized (objects) {
            objects.add(gameObject);
        }
    }

    public boolean collisionAABB(GameObject a, GameObject b) {
        if (a == null || b == null) return false;
        float halfA = a.getSize() / 2f;
        float halfB = b.getSize() / 2f;
        float leftA = a.getX() - halfA;
        float rightA = a.getX() + halfA;
        float topA = a.getY() - halfA;
        float bottomA = a.getY() + halfA;
        float leftB = b.getX() - halfB;
        float rightB = b.getX() + halfB;
        float topB = b.getY() - halfB;
        float bottomB = b.getY() + halfB;
        return rightA > leftB && leftA < rightB && bottomA > topB && topA < bottomB;
    }

    public boolean collisionCircle(GameObject a, GameObject b) {
        if (a == null || b == null) return false;
        float radiusA = a.getSize() / 2f;
        float radiusB = b.getSize() / 2f;
        float deltax = a.getX() - b.getX();
        float deltay = a.getY() - b.getY();
        float distance = (float) Math.sqrt(deltax * deltax + deltay * deltay);
        return distance < radiusA + radiusB;
    }

    public void spawnObjectPattern(List<GameObject> pattern, long delay) {
        Thread spawnThread = new Thread(() -> {
            for (int i = 0; i < pattern.size(); i++) {
                GameObject elem = pattern.get(i);
                GameObject newObject = new GameObject(-1, elem.getX(), elem.getY(), 100, elem.getSpeed(), elem.getColor());
                newObject.setFraction(elem.getFraction());
                synchronized (objects) {
                    objects.add(newObject);
                    System.out.println("Объект " + newObject.getId() + " заспавнен");
                }
                if (i < pattern.size() - 1) {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("Общий спавн завершен");
        });
        spawnThread.start();
    }

    public void moveTowards(GameObject attacker, GameObject target) {
        if (attacker == null || target == null) return;
        if (!attacker.isAlive() || !target.isAlive()) return;
        float dx = target.getX() - attacker.getX();
        float dy = target.getY() - attacker.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        if (distance > 0.01f) {
            float speed = attacker.getSpeed();
            float moveX = (dx / distance) * speed * deltaTime;
            float moveY = (dy / distance) * speed * deltaTime;
            attacker.setX(attacker.getX() + moveX);
            attacker.setY(attacker.getY() + moveY);
        }
    }

    public GameObject findNearestEnemy(GameObject self, float range) {
        if (self == null) return null;
        GameObject nearest = null;
        float rangeSq = range * range;
        float minDistanceSq = rangeSq;
        synchronized (objects) {
            for (GameObject obj : objects) {
                if (obj != self && obj.isAlive() && obj.getFraction() != self.getFraction()) {
                    float distSq = self.distanceSqTo(obj);
                    if (distSq < minDistanceSq) {
                        minDistanceSq = distSq;
                        nearest = obj;
                    }
                }
            }
        }
        return nearest;
    }

    public void setScreenSize(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public float getGameTime() {
        return gameTime;
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
}