import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Engine {
    private float spawnTimer = 0f;

    private int screenWidth ;
    private int screenHeight ;
    private final List<GameObject> objects = new ArrayList<>();
    private final Random random = new Random();

    public Engine() {
        objects.add(new GameObject(0, 0, 100, 50, 2, Color.RED));
        objects.add(new GameObject(1, 0, 200, 30, 4, Color.BLUE));
        objects.add(new GameObject(2, 0, 300, 60, 1, Color.GREEN));
    }
    public void setScreenSize(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
    }

    public void update(float deltaTime) {
        }


    public void draw(Graphics g) {

    }

    public void spawnObject() {
        int type = random.nextInt(3);

        int x;
        int y;

        GameObject newObject;

        if (type == 0) {
            x = random.nextInt(screenWidth - 50);
            y = random.nextInt(screenHeight - 50);
            newObject = new GameObject(0, x, y, 50, 2, Color.RED);

        } else if (type == 1) {
            x = random.nextInt(screenWidth - 30);
            y = random.nextInt(screenHeight - 30);
            newObject = new GameObject(1, x, y, 30, 4, Color.BLUE);

        } else {
            x = random.nextInt(screenWidth - 60);
            y = random.nextInt(screenHeight - 60);
            newObject = new GameObject(2, x, y, 60, 1, Color.GREEN);
        }

        synchronized (objects) {
            objects.add(newObject);
        }

        System.out.println("Объект типа " + newObject.getId() + " заспавнен");
    }

    public void spawnObject(GameObject gameObject) {
        
    }

    public boolean collisionAABB(GameObject a, GameObject b) {
        return false;
    }

    public void spawnObjectPattern(List<GameObject> pattern, long delay) {
        Thread spawnThread = new Thread(new Runnable() { // создаёт новый поток
            @Override
            public void run() {
                for (int i = 0; i < pattern.size(); i++) {
                    GameObject elem = pattern.get(i);

                    // копия объекта создаётся
                    GameObject newObject = new GameObject(
                            elem.getId(),
                            elem.getX(),
                            elem.getY(),
                            elem.getSize(),
                            elem.getSpeed(),
                            elem.getColor()
                    );

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
            }
        });

        spawnThread.start(); // запуск потока
    }

    // supplier of Pythagoras
    public void moveTowards(GameObject attaker, GameObject target) {
    }

    public List<GameObject> getObjects() {
        return new ArrayList<>(objects);
    }

    public void clearObjects() {
        objects.clear();
    }


}