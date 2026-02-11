import java.awt.*;

public class Arrow extends GameObject {
    private float vx, vy;
    private static final float GRAVITY = 800f;
    
    public Arrow(float startX, float startY, float angleDeg, float speed) {
        super(-1, startX, startY, 30, 5, Color.BLACK);
        float angleRad = (float) Math.toRadians(angleDeg);
        this.vx = speed * (float) Math.cos(angleRad);
        this.vy = speed * (float) Math.sin(angleRad);
    }

    @Override
    public void update() {
        x += (float) (vx * 0.016);
        y += (float) (vy * 0.016);
        vy += (float) (GRAVITY * 0.016);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));

        float angle = (float) Math.atan2(vy, vx);
        int length = 35;

        int startX = (int) x;
        int startY = (int) y;
        int endX = (int) (x + length * Math.cos(angle));
        int endY = (int) (y + length * Math.sin(angle));
        
        g2d.drawLine(startX, startY, endX, endY);
        
        int tipSize = 8;
        int backX = (int) (endX - tipSize * 0.5 * Math.cos(angle));
        int backY = (int) (endY - tipSize * 0.5 * Math.sin(angle));

        int[] xPoints = {
                endX,
                (int) (backX - tipSize * 0.3 * Math.sin(angle)),
                (int) (backX + tipSize * 0.3 * Math.sin(angle))
        };
        int[] yPoints = {
                endY,
                (int) (backY + tipSize * 0.3 * Math.cos(angle)),
                (int) (backY - tipSize * 0.3 * Math.cos(angle))
        };
        g2d.fillPolygon(xPoints, yPoints, 3);
        
        int featherLength = 8;
        float perpX = (float) Math.sin(angle);
        float perpY = (float) -Math.cos(angle);

        g2d.drawLine(startX, startY,
                (int) (startX - featherLength * perpX),
                (int) (startY - featherLength * perpY));
        g2d.drawLine(startX, startY,
                (int) (startX + featherLength * perpX),
                (int) (startY + featherLength * perpY));
    }
}
