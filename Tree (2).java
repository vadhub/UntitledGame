package org.example;

import java.awt.*;
import java.util.Random;

public class Tree extends GameObject {
    private static final Random rand = new Random();
    private final boolean isPine;

    public Tree(int id, float x, float y, boolean isPine) {
        super(id, x, y, 45, 0f, Color.GREEN);
        this.isPine = isPine;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Тень
        g2d.setColor(new Color(0, 0, 0, 60));
        g2d.fillOval((int)fx - 12, (int)fy + size/2 + 8, size + 15, 8);

        if (isPine) {
            // Ствол
            g2d.setColor(new Color(100, 60, 30));
            g2d.fillRect((int)fx + size/2 - 4, (int)fy + size/2 - 35, 8, 35);

            // Крона — 3 уровня
            for (int level = 0; level < 3; level++) {
                int h = 22 - level * 6;
                int w = 42 - level * 12;
                int y = (int)fy + size/2 - h * (level + 1) - 10;
                int[] xPoints = {
                        (int)fx + size/2 - w/2,
                        (int)fx + size/2 + w/2,
                        (int)fx + size/2
                };
                int[] yPoints = {y, y, y - h};
                Color c = new Color(30 + level * 25, 100 + level * 25, 30 + level * 25);
                g2d.setColor(c);
                g2d.fillPolygon(xPoints, yPoints, 3);
                g2d.setColor(Color.DARK_GRAY);
                g2d.drawPolygon(xPoints, yPoints, 3);
            }
        } else {
            // Ствол
            g2d.setColor(new Color(100, 60, 30));
            g2d.fillRect((int)fx + size/2 - 3, (int)fy + size/2 - 30, 6, 30);

            // Крона — 2 слоя
            g2d.setColor(new Color(70, 160, 60));
            g2d.fillOval((int)fx - size/2 - 6, (int)fy - size/2 - 18, size + 12, size + 12);
            g2d.setColor(new Color(50, 130, 50));
            g2d.fillOval((int)fx - size/2, (int)fy - size/2, size, size);

            // Листики
            for (int i = 0; i < 10; i++) {
                double a = rand.nextDouble() * Math.PI * 2;
                int r = rand.nextInt(10) + 6;
                int lx = (int)(fx + Math.cos(a) * r);
                int ly = (int)(fy + Math.sin(a) * r - size/2);
                g2d.setColor(new Color(40, 120, 40));
                g2d.fillOval(lx - 2, ly - 1, 4, 2);
            }
        }
    }
}