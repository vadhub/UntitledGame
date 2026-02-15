package org.example;

import java.awt.*;
import java.util.Random;

public class Grass extends GameObject {
    private static final Random rand = new Random();

    public Grass(int id, float x, float y) {
        super(id, x, y, 8, 0f, new Color(60, 150, 60));
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        int count = rand.nextInt(3) + 3;
        for (int i = 0; i < count; i++) {
            double angle = Math.toRadians(rand.nextInt(60) - 30);
            int len = rand.nextInt(18) + 12;
            int dx = (int) (Math.cos(angle) * 2);
            int dy = (int) (Math.sin(angle) * 2);

            int x1 = (int) fx + dx;
            int y1 = (int) fy;
            int x2 = x1 + (int) (Math.cos(angle) * len);
            int y2 = y1 - (int) (Math.sin(angle) * len);

            g2d.drawLine(x1, y1, x2, y2);

            // Листик
            double leafAngle = angle + Math.toRadians(rand.nextInt(40) - 20);
            int lx = x2 + (int) (Math.cos(leafAngle) * 3);
            int ly = y2 - (int) (Math.sin(leafAngle) * 3);
            g2d.fillOval(lx - 2, ly - 1, 4, 2);
        }

        // Тень под травой
        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.fillOval((int)fx - 2, (int)fy + 5, 6, 2);
    }
}