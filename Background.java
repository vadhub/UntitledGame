// Background.java (обновлённая версия)
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Background {
    private List<Tree> trees;
    private List<Grass> grasses;  // ← список травы
    private Random rand;

    private static final int GRASS_COUNT = 80;  // 80 кустиков травы на фоне

    public Background() {
        trees = new ArrayList<>();
        grasses = new ArrayList<>();
        rand = new Random();
        generateTrees();
        generateGrasses();  // трава генерируется здесь, один раз
    }

    private void generateGrasses() {
        for (int i = 0; i < GRASS_COUNT; i++) {
            float x = rand.nextFloat() * 1200;  // ширина игрового поля
            float y = 0;  // временно, Y будет установлен при отрисовке
            grasses.add(new Grass(x, y));
        }
    }

    private void generateTrees() {
        trees.add(new Tree(1, 100, 450, false));
        trees.add(new Tree(2, 300, 420, true));
        trees.add(new Tree(3, 500, 460, false));
        trees.add(new Tree(4, 700, 430, true));
        trees.add(new Tree(5, 900, 440, false));
        trees.add(new Tree(6, 1100, 450, true));
    }

    public void draw(Graphics g, int panelWidth, int panelHeight) {
        Graphics2D g2d = (Graphics2D) g;

        Stroke originalStroke = g2d.getStroke();
        Color originalColor = g2d.getColor();

        drawSky(g2d, panelWidth, panelHeight);
        drawClouds(g2d, panelWidth, panelHeight);
        drawGround(g2d, panelWidth, panelHeight);

        // ★ ТРАВА рисуется на земле ★
        drawGrasses(g2d, panelWidth, panelHeight);

        drawRocks(g2d, panelWidth, panelHeight);
        drawTrees(g2d, panelHeight);
    }


    private void drawSky(Graphics2D g2d, int width, int height) {
        GradientPaint skyGradient = new GradientPaint(
                0, 0, new Color(135, 206, 235),
                0, height, new Color(70, 130, 180)
        );
        g2d.setPaint(skyGradient);
        g2d.fillRect(0, 0, width, height);
    }

    private void drawClouds(Graphics2D g2d, int width, int height) {
        g2d.setColor(new Color(255, 255, 255, 200));
        drawCloud(g2d, width * 1/5, height * 1/6, 40);
        drawCloud(g2d, width * 4/5, height * 1/4, 50);
        drawCloud(g2d, width * 1/2, height * 1/8, 35);
        g2d.setColor(new Color(255, 255, 255, 150));
        drawCloud(g2d, width * 3/4, height * 1/10, 25);
    }

    private void drawCloud(Graphics2D g2d, int centerX, int centerY, int radius) {
        g2d.fillOval(centerX - radius, centerY - radius/2, radius * 2, radius);
        g2d.fillOval(centerX - radius - radius/2, centerY - radius/4, radius, radius * 3/4);
        g2d.fillOval(centerX + radius/2, centerY - radius/3, radius, radius * 3/4);
        g2d.fillOval(centerX - radius/3, centerY - radius, radius * 4/3, radius * 2/3);
    }

    private void drawGround(Graphics2D g2d, int width, int height) {
        int groundHeight = 50;
        g2d.setColor(new Color(34, 139, 34));
        g2d.fillRect(0, height - groundHeight, width, groundHeight);
        g2d.setColor(new Color(20, 80, 20));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(0, height - groundHeight, width, height - groundHeight);
    }

    // ★ НОВЫЙ МЕТОД — рисуем траву на земле ★
    private void drawGrasses(Graphics2D g2d, int panelWidth, int panelHeight) {
        int groundY = panelHeight - 50;

        for (Grass grass : grasses) {
            // X циклически по ширине экрана
            float x = grass.getX() % panelWidth;
            // Y фиксированная — на земле
            float y = groundY - 1;

            grass.setX(x);
            grass.setY(y);
            grass.draw(g2d);
        }
    }

    private void drawRocks(Graphics2D g2d, int width, int height) {
        int groundY = height - 50;
        g2d.setColor(new Color(105, 105, 105));
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        drawRock(g2d, width * 1/8, groundY, 30, 20);
        drawRock(g2d, width * 7/8, groundY, 40, 25);
        g2d.setColor(new Color(128, 128, 128));
        drawRock(g2d, width * 1/3, groundY, 20, 15);
        drawRock(g2d, width * 2/3, groundY, 35, 22);
    }

    private void drawRock(Graphics2D g2d, int x, int groundY, int width, int height) {
        int[] xPoints = {
                x, x + width/4, x + width/2, x + width * 3/4, x + width,
                x + width * 3/4, x + width/2, x + width/4
        };
        int[] yPoints = {
                groundY, groundY - height/2, groundY - height, groundY - height * 3/4,
                groundY - height/3, groundY - height/4, groundY - height/2, groundY - height/3
        };
        g2d.fillPolygon(xPoints, yPoints, 8);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(xPoints, yPoints, 8);
        g2d.setColor(new Color(105, 105, 105));
    }

    private void drawTrees(Graphics2D g2d, int panelHeight) {
        for (Tree tree : trees) {
            tree.draw(g2d);
        }
    }
}
