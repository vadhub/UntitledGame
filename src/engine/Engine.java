package src.engine;

import src.view.unit.BaseUnit;
import src.view.unit.UnitArcher;
import src.view.unit.UnitDinoRider;
import src.view.unit.UnitGunner;
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
    private static final float SPAWN_X = 1600f;            // за правой границей окна
    public static final int BASE_UNIT_SPEED = 90;
    public static final int ARCHER_SPEED = 120;
    public static final int TANK_SPEED = 100;

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
            spawnEnemyByPattern(); // вызываем ПАТТЕРН вместо случайного спавна
            enemySpawnTimer = 0f;
        }
        this.deltaTime = deltaTime;
        this.gameTime += deltaTime;

        // Обновляем все объекты
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

        // Каждые 60 кадров выравниваем юниты по земле
        frameCounter++;
        if (frameCounter >= 60) {
            alignUnitsToGround();
            frameCounter = 0;
        }
    }

    /**
     * Принудительно выравнивает всех юнитов по земле
     */
    public void alignUnitsToGround() {
        synchronized (objects) {
            for (GameObject obj : objects) {
                if (obj instanceof BaseUnit) {
                    BaseUnit unit = (BaseUnit) obj;

                    if (unit instanceof UnitDinoRider) {
                        unit.setY(GROUND_Y + 170);
                    } else if (unit instanceof UnitArcher) {
                        unit.setY(GROUND_Y);
                    } else {
                        unit.setY(GROUND_Y + 200);
                    }
                }
            }
        }
    }

    /**
     * Отрисовывает все объекты игры
     * @param g графика для рисования
     */
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
     * Паттерн спавна врагов по заданию преподавателя:
     * Волна 0: 1 BaseUnit
     * Волна 1: 2 BaseUnit
     * Волна 2: 2 BaseUnit + 2 Archer
     * Волна 3: 1 TankUnit (UnitDinoRider)
     * Далее повторяем с волны 2
     */
    private void spawnEnemyByPattern() {
        float spawnX = SPAWN_X;
        float spawnY = GROUND_Y; // ВСЕ ВРАГИ НА УРОВНЕ ЗЕМЛИ

        System.out.println("===== ВОЛНА " + (waveIndex + 1) + " =====");

        switch (waveIndex) {
            case 0:
                // Волна 1: 1 BaseUnit
                spawnBaseUnit(spawnX, spawnY);
                System.out.println("Волна 1: 1 BaseUnit");
                break;

            case 1:
                // Волна 2: 2 BaseUnit
                spawnBaseUnit(spawnX, spawnY);
                spawnBaseUnit(spawnX + 60, spawnY);
                System.out.println("Волна 2: 2 BaseUnit");
                break;

            case 2:
                // Волна 3: 2 BaseUnit + 2 Archer
                spawnBaseUnit(spawnX, spawnY);
                spawnBaseUnit(spawnX + 60, spawnY);
                spawnArcher(spawnX + 120, spawnY);
                spawnArcher(spawnX + 180, spawnY);
                System.out.println("Волна 3: 2 BaseUnit + 2 Archer");
                break;

            case 3:
                // Волна 4: 1 TankUnit (UnitDinoRider)
                spawnTank(spawnX, spawnY);
                System.out.println("Волна 4: 1 UnitDinoRider (Tank)");
                waveIndex = 1; // после танка возвращаемся к волне 2
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

        enemy.setSpeed(-BASE_UNIT_SPEED);      // движение влево
        enemy.setFraction(1);      // фракция врага
        enemy.setDirection(-1);    // ВРАГИ ИДУТ ЛИЦОМ ВПЕРЁД

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

        enemy.setSpeed(-ARCHER_SPEED);
        enemy.setFraction(1);
        enemy.setDirection(-1);

        spawnObject(enemy);
    }

    private void spawnTank(float x, float y) {
        // UnitDinoRider - это и есть TankUnit
        UnitDinoRider enemy = (UnitDinoRider) UnitDinoRider.builder()
                .x(x)
                .y(y)
                .health(300)      // много здоровья (танк)
                .attackDamage(30)
                .attackRange(120f)
                .build();

        enemy.setSpeed(-TANK_SPEED);      // движение влево
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

    /**
     * Создаёт новый поток для спавна объектов по паттерну
     * @param pattern список объектов для спавна
     * @param delay задержка между спавном объектов в миллисекундах
     */
    public void spawnObjectPattern(List<GameObject> pattern, long delay) {
        // создаёт новый поток
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

    /**
     * Находит ближайшего врага для атакующего объекта
     * @param attacker объект, который ищет цель
     * @param range радиус поиска
     * @return ближайший объект другой фракции или null
     */
    public GameObject findNearestEnemy(GameObject attacker, float range) {
        GameObject nearest = null;
        float minDist = range;

        synchronized (objects) {
            for (GameObject obj : objects) {
                // Проверяем: объект живой и фракция отличается от атакующего
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

    /**
     * Проверяет столкновение стрелы с объектом (круг-круг)
     * @param arrow стрела
     * @param target цель для проверки
     * @return true если столкнулись, false если нет
     */
    public boolean collisionCircle(Arrow arrow, GameObject target) {
        if (arrow == null || target == null) return false;
        if (!target.isAlive()) return false;

        // Координаты стрелы (центр)
        float arrowX = arrow.getX();
        float arrowY = arrow.getY();
        float arrowRadius = 8f;

        // Координаты цели (центр)
        float targetX = target.getX() + target.getSize() / 2f;
        float targetY = target.getY() + target.getSize() / 2f;
        float targetRadius = target.getSize() / 2f;

        float dx = arrowX - targetX;
        float dy = arrowY - targetY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        return distance < (arrowRadius + targetRadius);
    }
}
