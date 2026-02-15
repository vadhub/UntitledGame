package org.example;

import javax.swing.*;
import java.awt.*;

public class View extends JPanel {
    public View() {
        setPreferredSize(new Dimension(800, 600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // 1. Фон — светло-зелёный луг
        g2d.setColor(new Color(190, 230, 190));
        g2d.fillRect(0, 0, w, h);

        // 2. Дорога — широкая, плавная, как в игре
        g2d.setColor(new Color(130, 90, 50));
        int[] roadX = {
                0, w * 1/6, w * 2/6, w * 3/6, w * 4/6, w * 5/6, w,
                w, w * 5/6, w * 4/6, w * 3/6, w * 2/6, w * 1/6, 0
        };
        int[] roadY = {
                h * 1/3, h * 1/3 + 25, h * 1/3 - 20, h * 1/3 + 15, h * 1/3 - 15, h * 1/3 + 20, h * 1/3,
                h * 2/3, h * 2/3 - 20, h * 2/3 + 15, h * 2/3 - 15, h * 2/3 + 20, h * 2/3 - 25, h * 2/3
        };
        g2d.fillPolygon(roadX, roadY, roadX.length);
        g2d.setColor(new Color(80, 60, 40));
        g2d.drawPolygon(roadX, roadY, roadX.length);

        // 3. Трава — пучки по краям дороги (видны!)
        g2d.setColor(new Color(50, 140, 50));
        for (int i = 0; i < 30; i++) {
            float x = rand(w * 0.9f) + w * 0.05f;
            float y = rand(h * 0.4f) + h * 0.3f;
            // Только если рядом с дорогой
            if (Math.abs(y - h/3) < 50 || Math.abs(y - 2*h/3) < 50) {
                drawGrass(g2d, x, y);
            }
        }

        // 4. Деревья — 6 штук, КРУПНЫЕ, как в игре
        drawTree(g2d, w * 0.18f, h * 0.45f, true, 40);   // хвойное, слева
        drawTree(g2d, w * 0.32f, h * 0.52f, false, 45);  // лиственное
        drawTree(g2d, w * 0.48f, h * 0.48f, true, 42);
        drawTree(g2d, w * 0.65f, h * 0.55f, false, 48);
        drawTree(g2d, w * 0.78f, h * 0.47f, true, 44);
        drawTree(g2d, w * 0.92f, h * 0.53f, false, 46);

        // 5. Камни — 5 штук, с тенью и градиентом
        drawRock(g2d, w * 0.12f, h * 0.72f, 32);
        drawRock(g2d, w * 0.28f, h * 0.75f, 28);
        drawRock(g2d, w * 0.5f, h * 0.68f, 36);
        drawRock(g2d, w * 0.72f, h * 0.73f, 30);
        drawRock(g2d, w * 0.88f, h * 0.66f, 34);

        // 6. Сарай — большой, внизу по центру
        int barnX = w / 2 - 120;
        int barnY = h - 180;
        drawBarn(g2d, barnX, barnY);

        // 7. Облака — мягкие
        drawCloud(g2d, 100, 70);
        drawCloud(g2d, 400, 60);
        drawCloud(g2d, 700, 80);
    }

    private static java.util.Random r = new java.util.Random();

    private int rand(float max) {
        return r.nextInt((int) max);
    }

    // --- Вспомогательные методы ---
    private void drawGrass(Graphics2D g, float x, float y) {
        g.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(new Color(60, 150, 60));
        for (int i = 0; i < 3; i++) {
            double a = Math.toRadians(r.nextInt(60) - 30);
            int len = r.nextInt(18) + 12;
            int x1 = (int)x;
            int y1 = (int)y;
            int x2 = x1 + (int)(Math.cos(a) * len);
            int y2 = y1 - (int)(Math.sin(a) * len);
            g.drawLine(x1, y1, x2, y2);
            g.fillOval(x2 - 2, y2 - 1, 4, 2);
        }
        g.setColor(new Color(0, 0, 0, 50));
        g.fillOval((int)x - 2, (int)y + 6, 6, 2);
    }

    private void drawTree(Graphics2D g, float x, float y, boolean isPine, int size) {
        // Тень
        g.setColor(new Color(0, 0, 0, 70));
        g.fillOval((int)x - 12, (int)y + size + 8, size + 20, 8);

        if (isPine) {
            // Ствол
            g.setColor(new Color(100, 60, 30));
            g.fillRect((int)x + size/2 - 5, (int)y + size - 30, 10, 30);

            // Крона — 2 уровня
            g.setColor(new Color(30, 100, 30));
            int[] tx1 = { (int)x, (int)x + size, (int)x + size/2 };
            int[] ty1 = { (int)y + size - 20, (int)y + size - 20, (int)y + size - 50 };
            g.fillPolygon(tx1, ty1, 3);
            g.setColor(new Color(20, 80, 20));
            int[] tx2 = { (int)x + 5, (int)x + size - 5, (int)x + size/2 };
            int[] ty2 = { (int)y + size - 15, (int)y + size - 15, (int)y + size - 40 };
            g.fillPolygon(tx2, ty2, 3);
        } else {
            // Ствол
            g.setColor(new Color(100, 60, 30));
            g.fillRect((int)x + size/2 - 3, (int)y + size - 30, 6, 30);

            // Крона
            g.setColor(new Color(70, 160, 60));
            g.fillOval((int)x, (int)y, size, size);
            g.setColor(new Color(50, 130, 50));
            g.fillOval((int)x + 5, (int)y + 5, size - 10, size - 10);
        }
    }

    private void drawRock(Graphics2D g, float x, float y, int size) {
        // Градиент
        GradientPaint grad = new GradientPaint(
                (int)x, (int)y, new Color(140, 140, 140),
                (int)x + size, (int)y + size, new Color(90, 90, 90)
        );
        g.setPaint(grad);
        g.fillOval((int)x, (int)y, size, size);
        g.setColor(Color.DARK_GRAY);
        g.drawOval((int)x, (int)y, size, size);

        // Тень
        g.setColor(new Color(0, 0, 0, 80));
        g.fillOval((int)x - 4, (int)y + size + 4, size + 8, 8);
    }

    private void drawBarn(Graphics2D g, int x, int y) {
        // Основание
        g.setColor(new Color(180, 140, 100));
        g.fillRoundRect(x, y, 240, 160, 20, 20);
        g.setColor(Color.BLACK);
        g.drawRoundRect(x, y, 240, 160, 20, 20);

        // Дверь
        g.setColor(new Color(100, 60, 30));
        g.fillRoundRect(x + 140, y + 80, 40, 60, 10, 10);
        g.setColor(Color.BLACK);
        g.drawRoundRect(x + 140, y + 80, 40, 60, 10, 10);

        // Окно
        g.setColor(Color.CYAN);
        g.fillRect(x + 80, y + 40, 40, 30);
        g.setColor(Color.BLACK);
        g.drawRect(x + 80, y + 40, 40, 30);
        g.drawLine(x + 80, y + 55, x + 120, y + 55);
        g.drawLine(x + 100, y+ 40, x + 100, y + 70);

        // Флаг
        g.setColor(Color.YELLOW);
        g.fillOval(x + 120, y + 10, 8, 8);
        g.setColor(Color.RED);
        g.fillPolygon(new int[]{x + 120, x + 130, x + 125}, new int[]{y + 10, y + 10, y - 15}, 3);
        g.setColor(Color.BLACK);
        g.drawLine(x + 124, y + 10, x + 124, y - 15);
    }

    private void drawCloud(Graphics2D g, int x, int y) {
        g.setColor(new Color(230, 240, 250, 240));
        g.fillOval(x, y, 50, 35);
        g.fillOval(x + 35, y - 10, 50, 35);
        g.fillOval(x + 70, y, 50, 35);
        g.setColor(new Color(200, 210, 220, 200));
        g.drawOval(x, y, 50, 35);
        g.drawOval(x + 35, y - 10, 50, 35);
        g.drawOval(x + 70, y, 50, 35);
    }
}