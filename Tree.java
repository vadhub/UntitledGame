package view.background;

import engine.GameObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tree extends GameObject {
    private static final Random rand = new Random();
    private final boolean isPine;
    private static final int SIZE = 45;

    // Константы для отрисовки
    private static final int SHADOW_OFFSET_X = -12;
    private static final int SHADOW_OFFSET_Y = 8;
    private static final int SHADOW_WIDTH = 60;
    private static final int SHADOW_HEIGHT = 8;

    private static final Color TRUNK_COLOR = new Color(100, 60, 30);
    private static final Color PINE_LEVEL_1 = new Color(55, 125, 55);
    private static final Color PINE_LEVEL_2 = new Color(80, 150, 80);
    private static final Color PINE_LEVEL_3 = new Color(105, 175, 105);

    private static final Color LEAF_OUTER = new Color(70, 160, 60);
    private static final Color LEAF_INNER = new Color(50, 130, 50);

    private final List<Point> leafOffsets = new ArrayList<>();

    public Tree(int id, float x, float y, boolean isPine) {
        super(id, x, y, SIZE, 0f, Color.GREEN);
        this.isPine = isPine;

        if (!isPine) {
            for (int i = 0; i < 12; i++) {
                double angle = rand.nextDouble() * Math.PI * 2;
                int radius = rand.nextInt(18) + 10;
                int offsetX = (int)(Math.cos(angle) * radius);
                int offsetY = (int)(Math.sin(angle) * radius) - SIZE/3;
                leafOffsets.add(new Point(offsetX, offsetY));
            }
        }
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {

        float oldX = this.x;
        float oldY = this.y;

        this.x = x;
        this.y = y;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isPine) {

            g2d.setColor(TRUNK_COLOR);
            g2d.fillRect(x + SIZE/2 - 3, y + 10, 6, 20);

            g2d.setColor(PINE_LEVEL_2);
            int[] xPoints = {x, x + SIZE, x + SIZE/2};
            int[] yPoints = {y + 10, y + 10, y - 20};
            g2d.fillPolygon(xPoints, yPoints, 3);
        } else {

            g2d.setColor(TRUNK_COLOR);
            g2d.fillRect(x + SIZE/2 - 3, y + 15, 6, 15);

            g2d.setColor(LEAF_OUTER);
            g2d.fillOval(x - 10, y - 25, 30, 30);
            g2d.setColor(LEAF_INNER);
            g2d.fillOval(x - 7, y - 22, 24, 24);
        }

        g2d.dispose();

        // Восстанавливаем координаты
        this.x = oldX;
        this.y = oldY;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = (int)getX();
        int centerY = (int)getY();

        drawShadow(g2d, centerX, centerY);

        if (isPine) {
            drawPine(g2d, centerX, centerY);
        } else {
            drawDeciduous(g2d, centerX, centerY);
        }
    }

    private void drawShadow(Graphics2D g2d, int x, int y) {
        g2d.setColor(new Color(0, 0, 0, 60));
        g2d.fillOval(x + SHADOW_OFFSET_X, y + SIZE/2 + SHADOW_OFFSET_Y,
                SHADOW_WIDTH, SHADOW_HEIGHT);
    }

    private void drawPine(Graphics2D g2d, int x, int y) {
        // Ствол
        g2d.setColor(TRUNK_COLOR);
        g2d.fillRect(x + SIZE/2 - 4, y + SIZE/2 - 35, 8, 35);

        // Уровни кроны (от нижнего к верхнему)
        // Используем отдельные переменные вместо массива с Color
        int[] heights = {22, 16, 10};
        int[] widths = {42, 30, 18};
        int[] yOffsets = {-10, -32, -48};
        Color[] colors = {PINE_LEVEL_1, PINE_LEVEL_2, PINE_LEVEL_3};

        for (int i = 0; i < 3; i++) {
            int h = heights[i];
            int w = widths[i];
            int yOffset = yOffsets[i];
            g2d.setColor(colors[i]);

            int tipY = y + SIZE/2 + yOffset - h;
            int baseY = tipY + h;

            int[] xPoints = {x + SIZE/2 - w/2, x + SIZE/2 + w/2, x + SIZE/2};
            int[] yPoints = {baseY, baseY, tipY};

            g2d.fillPolygon(xPoints, yPoints, 3);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawPolygon(xPoints, yPoints, 3);
        }
    }

    private void drawDeciduous(Graphics2D g2d, int x, int y) {
        // Ствол
        g2d.setColor(TRUNK_COLOR);
        g2d.fillRect(x + SIZE/2 - 4, y + SIZE/2 - 30, 8, 30);

        // Крона
        g2d.setColor(LEAF_OUTER);
        g2d.fillOval(x - SIZE/2 - 6, y - SIZE/2 - 18, SIZE + 12, SIZE + 12);

        g2d.setColor(LEAF_INNER);
        g2d.fillOval(x - SIZE/2, y - SIZE/2, SIZE, SIZE);

        // Листики (используем предрассчитанные позиции)
        g2d.setColor(new Color(40, 120, 40));
        for (Point offset : leafOffsets) {
            g2d.fillOval(x + offset.x - 3, y + offset.y - 2, 6, 4);
        }

        // Добавляем блики для объёма
        g2d.setColor(new Color(100, 200, 80, 80));
        g2d.fillOval(x - SIZE/3, y - SIZE/2 - 10, SIZE/2, SIZE/3);
    }
}