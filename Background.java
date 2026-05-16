import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Background {
    private List<Tree> trees;
    private Random rand;

    public Background() {
        trees = new ArrayList<>();
        rand = new Random();
        generateTrees();
    }

    private void generateTrees() {
        // Деревья удалены из фона
    }

    public void draw(Graphics g, int panelWidth, int panelHeight) {
        Graphics2D g2d = (Graphics2D) g;

        Stroke originalStroke = g2d.getStroke();
        Color originalColor = g2d.getColor();

        drawSky(g2d, panelWidth, panelHeight);
        drawClouds(g2d, panelWidth, panelHeight);
        drawGround(g2d, panelWidth, panelHeight);

        g2d.setStroke(originalStroke);
        g2d.setColor(originalColor);
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
        drawCloud(g2d, width / 5, height / 6, 40);
        drawCloud(g2d, width * 4 / 5, height / 4, 50);
        drawCloud(g2d, width / 2, height / 8, 35);
        g2d.setColor(new Color(255, 255, 255, 150));
        drawCloud(g2d, width * 3 / 4, height / 10, 25);
    }

    private void drawCloud(Graphics2D g2d, int centerX, int centerY, int radius) {
        g2d.fillOval(centerX - radius, centerY - radius / 2, radius * 2, radius);
        g2d.fillOval(centerX - radius - radius / 2, centerY - radius / 4, radius, radius * 3 / 4);
        g2d.fillOval(centerX + radius / 2, centerY - radius / 3, radius, radius * 3 / 4);
        g2d.fillOval(centerX - radius / 3, centerY - radius, radius * 4 / 3, radius * 2 / 3);
    }

    private void drawGround(Graphics2D g2d, int width, int height) {
        int groundHeight = 200;
        g2d.setColor(new Color(34, 139, 34));
        g2d.fillRect(0, height - groundHeight, width, groundHeight);
        drawRocks(g2d, width, height - groundHeight);
        g2d.setColor(new Color(20, 80, 20));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(0, height - groundHeight, width, height - groundHeight);
    }

    private void drawRocks(Graphics2D g2d, int width, int groundY) {
        g2d.setColor(new Color(105, 105, 105));
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        drawRock(g2d, width / 8, groundY, 30, 20);
        drawRock(g2d, width * 7 / 8, groundY, 40, 25);
        g2d.setColor(new Color(128, 128, 128));
        drawRock(g2d, width / 3, groundY, 20, 15);
        drawRock(g2d, width * 2 / 3, groundY, 35, 22);
    }

    private void drawRock(Graphics2D g2d, int x, int groundY, int width, int height) {
        int[] xPoints = {
                x,
                x + width / 4,
                x + width / 2,
                x + width * 3 / 4,
                x + width,
                x + width * 3 / 4,
                x + width / 2,
                x + width / 4
        };
        int[] yPoints = {
                groundY,
                groundY - height / 2,
                groundY - height,
                groundY - height * 3 / 4,
                groundY - height / 3,
                groundY - height / 4,
                groundY - height / 2,
                groundY - height / 3
        };

        g2d.fillPolygon(xPoints, yPoints, 8);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(xPoints, yPoints, 8);
        g2d.setColor(new Color(105, 105, 105));
    }
}