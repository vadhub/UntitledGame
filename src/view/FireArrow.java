package src.view;

import java.awt.*;

public class FireArrow extends Arrow {
    private static final int BODY_LENGTH = 34;
    private static final int FLAME_LENGTH = 18;

    public FireArrow(float startX, float startY, float angleDeg, float speed, int damage) {
        super(startX, startY, angleDeg, speed);
        this.size = 36;
        this.attackDamage = damage;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        float angle = (float) Math.atan2(vy, vx);
        g2d.translate(x, y);
        g2d.rotate(angle);

        g2d.setColor(new Color(255, 220, 150, 120));
        g2d.fillOval(-FLAME_LENGTH - 8, -6, FLAME_LENGTH, 12);

        g2d.setColor(new Color(255, 130, 30, 180));
        g2d.fillOval(-FLAME_LENGTH - 2, -4, 12, 8);

        g2d.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(new Color(140, 40, 20));
        g2d.drawLine(-BODY_LENGTH / 2, 0, BODY_LENGTH / 3, 0);

        g2d.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(new Color(255, 240, 180));
        g2d.drawLine(-BODY_LENGTH / 2 + 3, 0, BODY_LENGTH / 4, 0);

        int[] tipX = {BODY_LENGTH / 3, BODY_LENGTH / 3 + 14, BODY_LENGTH / 3 + 2};
        int[] tipY = {0, 4, -4};
        g2d.setColor(new Color(255, 90, 20));
        g2d.fillPolygon(tipX, tipY, 3);

        g2d.setColor(new Color(255, 190, 0, 140));
        g2d.fillOval(-BODY_LENGTH / 2 - 4, -9, 10, 18);

        g2d.dispose();
    }
}
