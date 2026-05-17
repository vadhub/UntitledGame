package engine;

import view.unit.BaseUnit;
import view.unit.UnitArcher;
import view.unit.UnitDinoRider;
import view.Arrow;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Engine {

    // Поля класса
    private List<GameObject> objects;
    private float enemySpawnTimer;
    private float deltaTime;
    private int screenWidth;
    private int screenHeight;
    private float gameTime;
    private int waveIndex = 0;
    private int frameCounter = 0;

    private static final float ENEMY_SPAWN_INTERVAL = 5f;
    private static final float GROUND_Y = 420f;
    private static final float SPAWN_X = 1600f;

    private static Engine instance;

    public static Engine getInstance() {
        if (instance == null) {
            instance = new Engine();
        }
        return instance;
    }

    private Engine() {
        this.objects = new ArrayList<>();
        this.enemySpawnTimer = 0f;
        this.gameTime = 0f;
        this.waveIndex = 0;
        this.frameCounter = 0;
        this.screenWidth = 1000;
        this.screenHeight = 800;
    }

    public void update(float deltaTime) {
        enemySpawnTimer += deltaTime;
        if (enemySpawnTimer >= ENEMY_SPAWN_INTERVAL) {
            spawnEnemyByPattern();
            enemySpawnTimer = 0f;
        }
        this.deltaTime = deltaTime;
        this.gameTime += deltaTime;

        synchronized (objects) {
            for (int i = 0; i < objects.size(); i++) {
                GameObject obj = objects.get(i);
                obj.update(deltaTime);
                if (!obj.isAlive()) {
                    objects.remove(i);
                    i--;
                }
            }
        }

        frameCounter++;
        if (frameCounter >= 60) {
            alignUnitsToGround();
            frameCounter = 0;
        }
    }

    public void alignUnitsToGround() {
        synchronized (objects) {
            for (GameObject obj : objects) {
                if (obj instanceof UnitDinoRider) {
                    obj.setY(GROUND_Y + 30);  // ← уменьшено для иконок
                } else if (obj instanceof UnitArcher) {
                    obj.setY(GROUND_Y);
                } else if (obj instanceof BaseUnit) {
                    obj.setY(GROUND_Y);
                }
            }
        }
    }

    public void draw(Graphics g) {
        synchronized (objects) {
            for (GameObject obj : objects) {
                if (obj.isAlive()) {
                    obj.draw(g);
                }
            }
        }
    }

    private void spawnEnemyByPattern() {
        float spawnX = SPAWN_X;
        float spawnY = GROUND_Y;

        System.out.println("===== ВОЛНА " + (waveIndex + 1) + " =====");

        switch (waveIndex) {
            case 0:
                spawnBaseUnit(spawnX, spawnY);
                System.out.println("Волна 1: 1 BaseUnit");
                break;
            case 1:
                spawnBaseUnit(spawnX, spawnY);
                spawnBaseUnit(spawnX + 60, spawnY);
                System.out.println("Волна 2: 2 BaseUnit");
                break;
            case 2:
                spawnBaseUnit(spawnX, spawnY);
                spawnBaseUnit(spawnX + 60, spawnY);
                spawnArcher(spawnX + 120, spawnY);
                spawnArcher(spawnX + 180, spawnY);
                System.out.println("Волна 3: 2 BaseUnit + 2 Archer");
                break;
            case 3:
                spawnTank(spawnX, spawnY);
                System.out.println("Волна 4: 1 UnitDinoRider (Tank)");
                waveIndex = 1;
                return;
            default:
                waveIndex = -1;
                break;
        }
        waveIndex++;
    }

    private void spawnBaseUnit(float x, float y) {
        BaseUnit enemy = new BaseUnit();
        enemy.setX(x);
        enemy.setY(y);
        enemy.setHealth(100);
        enemy.setAttackDamage(20);
        enemy.setAttackRange(150f);
        enemy.setSpeed(-80f);
        enemy.setFraction(1);
        enemy.setDirection(-1);
        enemy.setEngine(this);
        spawnObject(enemy);
    }

    private void spawnArcher(float x, float y) {
        UnitArcher enemy = new UnitArcher();
        enemy.setX(x);
        enemy.setY(y);
        enemy.setHealth(100);
        enemy.setAttackDamage(15);
        enemy.setAttackRange(200f);
        enemy.setSpeed(-80f);
        enemy.setFraction(1);
        enemy.setDirection(-1);
        enemy.setEngine(this);
        spawnObject(enemy);
    }

    private void spawnTank(float x, float y) {
        UnitDinoRider enemy = new UnitDinoRider();
        enemy.setX(x);
        enemy.setY(y);
        enemy.setHealth(300);
        enemy.setAttackDamage(30);
        enemy.setAttackRange(120f);
        enemy.setSpeed(-60f);
        enemy.setFraction(1);
        enemy.setDirection(-1);
        enemy.setEngine(this);
        spawnObject(enemy);
    }

    public void spawnObject(GameObject gameObject) {
        if (gameObject != null) {
            synchronized (objects) {
                objects.add(gameObject);
                if (gameObject instanceof BaseUnit) {
                    ((BaseUnit) gameObject).setEngine(this);
                }
                System.out.println("Объект заспавнен: " + gameObject.getClass().getSimpleName());
            }
        }
    }

    public void spawnObjectPattern(List<GameObject> pattern, long delay) {
        Thread spawnThread = new Thread(() -> {
            for (int i = 0; i < pattern.size(); i++) {
                GameObject elem = pattern.get(i);
                GameObject newObject = elem.clone();
                newObject.setId(-1);

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
    }

    public float getGameTime() {
        return gameTime;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public GameObject findNearestEnemy(GameObject attacker, float range) {
        GameObject nearest = null;
        float minDist = range;

        synchronized (objects) {
            for (GameObject obj : objects) {
                if (obj.isAlive() && obj.getFraction() != attacker.getFraction()) {
                    float dist = attacker.distanceTo(obj);
                    if (dist < minDist) {
                        minDist = dist;
                        nearest = obj;
                    }
                }
            }
        }
        return nearest;
    }

    public boolean collisionCircle(Arrow arrow, GameObject target) {
        if (arrow == null || target == null) return false;
        if (!target.isAlive()) return false;

        float arrowX = arrow.getX();
        float arrowY = arrow.getY();
        float arrowRadius = 8f;

        float targetX = target.getX() + target.getSize() / 2f;
        float targetY = target.getY() + target.getSize() / 2f;
        float targetRadius = target.getSize() / 2f;

        float dx = arrowX - targetX;
        float dy = arrowY - targetY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        return distance < (arrowRadius + targetRadius);
    }

    public void setScreenSize(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void reset() {
        synchronized (objects) {
            objects.clear();
        }
        enemySpawnTimer = 0f;
        gameTime = 0f;
        waveIndex = 0;
        frameCounter = 0;
    }
}