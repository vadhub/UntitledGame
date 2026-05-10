package src.engine;

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
    private float enemySpawnTimer = 0f;
    private final float ENEMY_SPAWN_INTERVAL = 5f;

    private Engine() {
    }

    public static Engine getInstance() {
        if (engine == null) {
            engine = new Engine();
        }
        return engine;
    }

    public void update(float deltaTime) {
        enemySpawnTimer += deltaTime;
        System.out.println(enemySpawnTimer + " " + ENEMY_SPAWN_INTERVAL);
        if (enemySpawnTimer >= ENEMY_SPAWN_INTERVAL) {
            spawnEnemyMob();
            System.out.println("enemy!");
            enemySpawnTimer = 0f;
        }
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
        // Случайные координаты
        float x = random.nextInt(screenWidth);
        float y = random.nextInt(screenHeight);

        // Случайный цвет
        Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));

        // Создаём объект с заданными параметрами
        GameObject newObject = new GameObject(
                -1, x, y, 30, 100, color
        );

        spawnObject(newObject); // добавляем в список
    }

    public void spawnObject(GameObject gameObject) {
        if (gameObject != null) {
            synchronized (objects) {
                objects.add(gameObject);
            }
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
        // создаёт новый поток
        Thread spawnThread = new Thread(() -> {
            for (int i = 0; i < pattern.size(); i++) {
                GameObject elem = pattern.get(i);

                // копия объекта создаётся
                GameObject newObject = new GameObject(
                        -1,
                        elem.getX(),
                        elem.getY(),
                        100,
                        elem.getSpeed(),
                        elem.getColor()
                );
                newObject.setFraction(elem.getFraction());

                // она добавляется в список
                synchronized (objects) {
                    objects.add(newObject);
                    System.out.println("Объект " + newObject.getId() + " заспавнен");
                }

                // задержка (очередь)
                if (i < pattern.size() - 1) {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        System.out.println("Спавн прерван!");
                        break;
                    }
                }
            }
            System.out.println("Общий спавн завершен");
        });

        spawnThread.start(); // запуск потока
    }

    // supplier of Pythagoras - движение к цели
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

    public List<GameObject> getEnemiesFor(int faction) {
        List<GameObject> enemies = new ArrayList<>();
        synchronized (objects) {
            for (GameObject obj : objects) {
                if (obj.isAlive() && obj.getFraction() != faction) {
                    enemies.add(obj);
                }
            }
        }
        return enemies;
    }

    public GameObject findNearestEnemy(GameObject self, float range) {
        if (self == null) return null;

        GameObject nearest = null;
        float minDistanceSq = range * range;

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

    public List<GameObject> getObjects() {
        synchronized (objects) {
            return new ArrayList<>(objects);
        }
    }

    public void clearObjects() {
        synchronized (objects) {
            objects.clear();
        }
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }


    public float getGameTime() { return gameTime; }

    private void spawnEnemyMob() {
        GameObject enemy = new GameObject(-1, 1600,300, 50, -10f);
        enemy.setFraction(1);
        spawnObject(enemy);

    }
}