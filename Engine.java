package src.engine;

import src.view.unit.BaseUnit;
import src.view.unit.UnitArcher;
import src.view.unit.UnitDinoRider;
import src.view.unit.UnitCrocoBanan;
import src.view.unit.UnitGunner;
import src.view.unit.UnitAladdin;
import src.view.Arrow;

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
    private int waveIndex = 0; // счётчик волн для паттерна
    private int frameCounter = 0;

    private static final float ENEMY_SPAWN_INTERVAL = 5f;
    private static final float GROUND_Y = 420f;            // УРОВЕНЬ ЗЕМЛИ
    private static final float SPAWN_X_FRAC = 0.95f;      // доля от ширины экрана (около правой башни)

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
        alignUnitsToGround(GROUND_Y);
    }

    public void alignUnitsToGround(float groundY) {
        synchronized (objects) {
            for (GameObject obj : objects) {
                if (obj instanceof BaseUnit) {
                    BaseUnit unit = (BaseUnit) obj;

                    if (unit instanceof UnitDinoRider) {
                        unit.setY(groundY - 30);
                    } else if (unit instanceof UnitAladdin) {
                        // Верблюд чуть ниже динозавра — туловище приземистее
                        unit.setY(groundY - 25);
                    } else if (unit instanceof UnitCrocoBanan) {
                        unit.setY(groundY - 20);
                    } else if (unit instanceof UnitArcher) {
                        unit.setY(groundY);
                    } else {
                        unit.setY(groundY);
                    }
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

    /**
     * Паттерн спавна врагов:
     * Волна 0: 1 BaseUnit
     * Волна 1: 2 BaseUnit
     * Волна 2: 2 BaseUnit + 2 Archer
     * Волна 3: 1 TankUnit (UnitDinoRider)
     * Волна 4: 1 UnitCrocoBanan
     * Волна 5: 1 UnitAladdin - новая волна
     * Далее повторяем с волны 2
     */
    private void spawnEnemyByPattern() {
        float spawnX = screenWidth * SPAWN_X_FRAC;
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
                break;

            //by Belyakina Maria
            case 4:
                spawnCrocoBanan(spawnX, spawnY);
                System.out.println("Волна 5: 1 UnitCrocoBanan");
                break;

            case 5:
                spawnAladdin(spawnX, spawnY);
                System.out.println("Волна 6: 1 UnitAladdin (Camel Rider)");
                waveIndex = 1; // после Аладдина возвращаемся к волне 2
                return;

            default:
                waveIndex = -1;
                break;
        }
        waveIndex++;
    }

    //  МЕТОДЫ СПАВНА ЮНИТОВ

    private void spawnBaseUnit(float x, float y) {
        BaseUnit enemy = (BaseUnit) BaseUnit.builder()
                .x(x)
                .y(y)
                .health(100)
                .attackDamage(20)
                .attackRange(150f)
                .build();

        enemy.setSpeed(-80f);
        enemy.setFraction(1);
        enemy.setDirection(-1);

        spawnObject(enemy);
    }

    private void spawnArcher(float x, float y) {
        UnitArcher enemy = (UnitArcher) UnitArcher.builder()
                .x(x)
                .y(y)
                .health(100)
                .attackDamage(15)
                .attackRange(200f)
                .build();

        enemy.setSpeed(-80f);
        enemy.setFraction(1);
        enemy.setDirection(-1);

        spawnObject(enemy);
    }

    private void spawnTank(float x, float y) {
        UnitDinoRider enemy = (UnitDinoRider) UnitDinoRider.builder()
                .x(x)
                .y(y)
                .health(300)
                .attackDamage(30)
                .attackRange(120f)
                .build();

        enemy.setSpeed(-60f);
        enemy.setFraction(1);
        enemy.setDirection(-1);

        spawnObject(enemy);
    }

    private void spawnCrocoBanan(float x, float y) {
        UnitCrocoBanan enemy = (UnitCrocoBanan) UnitCrocoBanan.builder()
                .x(x)
                .y(y)
                .health(250)
                .attackDamage(25)
                .attackRange(180f)
                .build();

        enemy.setSpeed(-65f);
        enemy.setFraction(1);
        enemy.setDirection(-1);

        spawnObject(enemy);
    }

    private void spawnAladdin(float x, float y) {
        UnitAladdin enemy = (UnitAladdin) UnitAladdin.builder()
                .x(x)
                .y(y)
                .health(200)       // средняя прочность — между Archer и DinoRider
                .attackDamage(20)  // урон вилкой
                .attackRange(140f) // ближний бой + чуть дальше лапы
                .build();

        enemy.setSpeed(-70f);      // подвижнее танка, но медленнее лучника
        enemy.setFraction(1);
        enemy.setDirection(-1);

        spawnObject(enemy);
    }

    //  ОСНОВНЫЕ МЕТОДЫ

    public void spawnObject(GameObject gameObject) {
        if (gameObject != null) {
            synchronized (objects) {
                objects.add(gameObject);
                System.out.println("Объект заспавнен: " + gameObject.getClass().getSimpleName());
            }
        }
    }

    public void spawnObjectPattern(List<GameObject> pattern, long delay) {
        Thread spawnThread = new Thread(() -> {
            for (int i = 0; i < pattern.size(); i++) {
                GameObject elem = pattern.get(i);

                GameObject newObject = new GameObject(
                        -1,
                        elem.getX(),
                        elem.getY(),
                        elem.getSize(),
                        elem.getSpeed()
                );
                newObject.setFraction(elem.getFraction());
                newObject.setDirection(elem.getDirection());

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

    //  ГЕТТЕРЫ

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
}