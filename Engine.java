import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Engine {

    private int screenWidth = 800;
    private int screenHeight = 600;
    private final List<GameObject> objects = new ArrayList<>();
    private final Random random = new Random();

    public Engine() {
        objects.add(new GameObject(0, 0, 100, 50, 2, Color.RED));
        objects.add(new GameObject(1, 0, 200, 30, 4, Color.BLUE));
        objects.add(new GameObject(2, 0, 300, 60, 1, Color.GREEN));
    }

    public void update(float deltaTime) {
        for (GameObject obj : objects) {
            obj.update();
        }

        objects.removeIf(obj -> obj.getX() + obj.getSize() < 0 || obj.getY() < -obj.getSize());
    }

    public void spawnObject() {
        float y = random.nextFloat() * (screenHeight - 60);
        Color color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());

        GameObject obj = new GameObject(
                random.nextInt(3, 100),
                screenWidth,
                y,
                40,
                2.5f,
                color
        );
        objects.add(obj);
    }

    public List<GameObject> getObjects() {
        return new ArrayList<>(objects);
    }

    public void clearObjects() {
        objects.clear();
    }


}
