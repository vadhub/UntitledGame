package src.view.background;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Background {
    // Относительные позиции деревьев: {xFrac, isPine}
    // xFrac — доля от ширины экрана, yFrac — позиция вычисляется от земли
    private static final float[][] TREE_DEFS = {
            {0.18f, 0},   // лиственное
            {0.33f, 1},   // ель
            {0.50f, 0},   // лиственное центр
            {0.67f, 1},   // ель
            {0.82f, 0},   // лиственное
    };

    private List<Tree> trees;

    public Background() {
        trees = new ArrayList<>();
        // Деревья создаются один раз, позиции обновляются при отрисовке
        for (int i = 0; i < TREE_DEFS.length; i++) {
            trees.add(new Tree(i + 1, 0, 0, TREE_DEFS[i][1] == 1));
        }
    }

    public void draw(Graphics g, int panelWidth, int panelHeight) {
        Graphics2D g2d = (Graphics2D) g;

        Stroke originalStroke = g2d.getStroke();
        Color originalColor = g2d.getColor();

        drawSky(g2d, panelWidth, panelHeight);
        drawClouds(g2d, panelWidth, panelHeight);
        drawGround(g2d, panelWidth, panelHeight);
        updateAndDrawTrees(g2d, panelWidth, panelHeight);

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
        g2d.setColor(new Color(34, 139, 34)); // ForestGreen
        g2d.fillRect(0, height - groundHeight, width, groundHeight);
        drawRocks(g2d, width, height - groundHeight);
        g2d.setColor(new Color(20, 80, 20));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(0, height - groundHeight, width, height - groundHeight);
    }

    private void drawRocks(Graphics2D g2d, int width, int groundY) {
        g2d.setColor(new Color(105, 105, 105)); // DarkGray для камней
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        drawRock(g2d, width * 1/8, groundY, 30, 20);
        drawRock(g2d, width * 7/8, groundY, 40, 25);
        g2d.setColor(new Color(128, 128, 128));
        drawRock(g2d, width * 1/3, groundY, 20, 15);
        drawRock(g2d, width * 2/3, groundY, 35, 22);
    }

    private void drawRock(Graphics2D g2d, int x, int groundY, int width, int height) {
        int[] xPoints = {
                x,
                x + width/4,
                x + width/2,
                x + width * 3/4,
                x + width,
                x + width * 3/4,
                x + width/2,
                x + width/4
        };
        int[] yPoints = {
                groundY,
                groundY - height/2,
                groundY - height,
                groundY - height * 3/4,
                groundY - height/3,
                groundY - height/4,
                groundY - height/2,
                groundY - height/3
        };

        g2d.fillPolygon(xPoints, yPoints, 8);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(xPoints, yPoints, 8);
        g2d.setColor(new Color(105, 105, 105));
    }

    private void updateAndDrawTrees(Graphics2D g2d, int panelWidth, int panelHeight) {
        int groundY = panelHeight - 50;
        for (int i = 0; i < trees.size(); i++) {
            float xFrac = TREE_DEFS[i][0];
            int treeX = (int)(panelWidth * xFrac);
            int treeY = groundY - 10;
            trees.get(i).setX(treeX);
            trees.get(i).setY(treeY);
            trees.get(i).draw(g2d);
        }
    }
}