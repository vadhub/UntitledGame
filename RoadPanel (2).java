package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class RoadPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth(), h = getHeight();

        // Фон — зелёная земля
        g.setColor(new Color(220, 240, 220));
        g.fillRect(0, 0, w, h);

        // Дорога — извилистая
        g.setColor(new Color(160, 120, 70));
        int[] x = {0, w/5, w/3, w/2, 2*w/3, 4*w/5, w, w, 4*w/5, 2*w/3, w/2, w/3, w/5, 0};
        int[] y = {h/3, h/3+25, h/3-15, h/3+10, h/3-10, h/3+15, h/3, h*2/3, h*2/3-15, h*2/3+10, h*2/3-10, h*2/3+15, h*2/3-25, h*2/3};
        g.fillPolygon(x, y, x.length);
        g.setColor(Color.BLACK);
        g.drawPolygon(x, y, x.length);

        // Грязевые пятна
        g.setColor(new Color(100, 70, 30));
        for (int i = 0; i < 6; i++) {
            int rx = w/2 + rand.nextInt(100) - 50;
            int ry = h/2 + rand.nextInt(80) - 40;
            g.fillOval(rx, ry, 12, 12);
        }
    }

    private final java.util.Random rand = new Random();
}