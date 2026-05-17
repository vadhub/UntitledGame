package src.engine;

import javax.swing.*;
import java.awt.*;

public class MyIcon implements Icon {
    private static final int ICON_SIZE = 104;

    private final GameObject gameObject;
    private final Rectangle modelBounds;

    public MyIcon(GameObject gameObject) {
        this.gameObject = gameObject;
        this.modelBounds = boundsFor(gameObject);
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        float previousX = gameObject.getX();
        float previousY = gameObject.getY();

        double scale = Math.min(
                (double) ICON_SIZE / modelBounds.width,
                (double) ICON_SIZE / modelBounds.height
        );
        double scaledWidth = modelBounds.width * scale;
        double scaledHeight = modelBounds.height * scale;

        g2d.translate(x, y);
        g2d.setClip(0, 0, ICON_SIZE, ICON_SIZE);
        g2d.translate(
                (ICON_SIZE - scaledWidth) / 2.0 - modelBounds.x * scale,
                (ICON_SIZE - scaledHeight) / 2.0 - modelBounds.y * scale
        );
        g2d.scale(scale, scale);

        gameObject.setX(0);
        gameObject.setY(0);
        gameObject.draw(g2d);
        gameObject.setX(previousX);
        gameObject.setY(previousY);

        g2d.dispose();
    }

    @Override
    public int getIconWidth() {
        return ICON_SIZE;
    }

    @Override
    public int getIconHeight() {
        return ICON_SIZE;
    }

    private Rectangle boundsFor(GameObject gameObject) {
        String className = gameObject.getClass().getSimpleName();

        if ("UnitArcher".equals(className)) {
            return new Rectangle(-35, -5, 180, 230);
        }

        if ("UnitDinoRider".equals(className)) {
            return new Rectangle(-35, -90, 185, 190);
        }

        return new Rectangle(-35, -105, 220, 180);
    }
}
