package view.background;

import engine.GameObject;

import java.awt.*;
import java.util.Random;

public class Grass extends GameObject {
    private static final Random rand = new Random();

    // Константы для настройки травы
    private static final int MIN_BLADES = 3;
    private static final int MAX_BLADES_EXTRA = 3; // будет 3-6 травинок
    private static final int MIN_LEN = 12;
    private static final int MAX_LEN_EXTRA = 18;
    private static final int LEAF_SIZE = 4;
    private static final int SHADOW_OFFSET_X = -2;
    private static final int SHADOW_OFFSET_Y = 5;
    private static final int SHADOW_SIZE = 6;

    public Grass(int id, float x, float y) {
        super(id, x, y, 8, 0f, new Color(60, 150, 60));
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {

        float oldX = this.x;
        float oldY = this.y;


        this.x = x;
        this.y = y;


        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(getColor());
        g2d.setStroke(new BasicStroke(1.0f));


        for (int i = 0; i < 3; i++) {
            int offsetX = i * 5;
            g2d.drawLine(x + offsetX, y + 10, x + offsetX + 2, y);
        }

        g2d.dispose();

        // Восстанавливаем координаты
        this.x = oldX;
        this.y = oldY;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(getColor());
        g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        int count = rand.nextInt(MAX_BLADES_EXTRA) + MIN_BLADES;
        int lastX = (int) getX(); // запоминаем последнюю X для тени

        for (int i = 0; i < count; i++) {
            double angle = Math.toRadians(rand.nextInt(60) - 30);
            int len = rand.nextInt(MAX_LEN_EXTRA) + MIN_LEN;
            int dx = (int) (Math.cos(angle) * 2);

            int x1 = (int) getX() + dx;
            int y1 = (int) getY();
            int x2 = x1 + (int) (Math.cos(angle) * len);
            int y2 = y1 - (int) (Math.sin(angle) * len);

            g2d.drawLine(x1, y1, x2, y2);
            lastX = x2; // сохраняем для тени

            // Листик
            double leafAngle = angle + Math.toRadians(rand.nextInt(40) - 20);
            int lx = x2 + (int) (Math.cos(leafAngle) * 3);
            int ly = y2 - (int) (Math.sin(leafAngle) * 3);
            g2d.fillOval(lx - 2, ly - 1, LEAF_SIZE, 2);
        }

        // Тень под травой
        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.fillOval(lastX + SHADOW_OFFSET_X, (int) getY() + SHADOW_OFFSET_Y, SHADOW_SIZE, 2);
    }
}