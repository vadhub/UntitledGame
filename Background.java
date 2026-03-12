import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Background {

    private final List<Cloud> clouds = new ArrayList<>();
    private final List<Stone> stones = new ArrayList<>();
    private final Color skyColor;
    private final Color groundColor;

    private static final int CLOUD_COUNT = 5;
    private static final int STONE_COUNT = 8;
    private static final int RANDOM_SEED = 42; // ✅ Фиксированный seed

    public Background() {
        this(new Color(135, 206, 235), new Color(70, 130, 50));
    }

    public Background(Color skyColor, Color groundColor) {
        this.skyColor = skyColor;
        this.groundColor = groundColor;
        initElements();
    }

    private void initElements() {
        Random rand = new Random(RANDOM_SEED); // ✅ Фиксированный seed вместо new Random()

        for (int i = 0; i < CLOUD_COUNT; i++) {
            clouds.add(new Cloud(
                    rand.nextInt(800),
                    rand.nextInt(200) + 20,
                    rand.nextInt(30) + 20,
                    0.3 + rand.nextDouble() * 0.5
            ));
        }

        for (int i = 0; i < STONE_COUNT; i++) {
            stones.add(new Stone(
                    rand.nextInt(1000),
                    rand.nextInt(20) + 10,
                    rand.nextInt(15) + 5
            ));
        }
    }

    public void draw(Graphics g, int panelWidth, int panelHeight) {
        Graphics2D g2d = (Graphics2D) g;
        drawSky(g2d, panelWidth, panelHeight);
        drawGround(g2d, panelWidth, panelHeight);

        for (Cloud cloud : clouds) {
            cloud.draw(g2d);
        }

        int groundLevel = panelHeight - 50;
        for (Stone stone : stones) {
            stone.draw(g2d, groundLevel);
        }
    }

    private void drawSky(Graphics2D g2d, int width, int height) {
        GradientPaint gradient = new GradientPaint(
                0, 0, skyColor.brighter(),
                0, height * 0.7f, skyColor.darker()
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);
    }

    private void drawGround(Graphics2D g2d, int width, int height) {
        int groundHeight = 50;
        g2d.setColor(groundColor);
        g2d.fillRect(0, height - groundHeight, width, groundHeight);

        g2d.setColor(groundColor.darker());
        g2d.setStroke(new BasicStroke(1));
        for (int i = 0; i < width; i += 15) {
            g2d.drawLine(i, height - groundHeight + 5, i + 10, height - groundHeight + 8);
        }
    }

    public void update() {
        for (Cloud cloud : clouds) {
            cloud.update();
        }
    }

    private static class Cloud {
        int x, y, size;
        double speed;

        Cloud(int x, int y, int size, double speed) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.speed = speed;
        }

        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(255, 255, 255, 200));
            g2d.fillOval(x, y, size, size / 2);
            g2d.fillOval(x + size / 3, y - size / 4, size, size / 2);
            g2d.fillOval(x + size * 2 / 3, y, size, size / 2);
        }

        void update() {
            x += speed;
            if (x > 1000) x = -size;
        }
    }

    private static class Stone {
        int x, width, height;

        Stone(int x, int width, int height) {
            this.x = x;
            this.width = width;
            this.height = height;
        }

        void draw(Graphics2D g2d, int groundLevel) {
            g2d.setColor(new Color(105, 105, 105));
            g2d.fillOval(x, groundLevel - height, width, height);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(x, groundLevel - height, width, height);
        }
    }
}