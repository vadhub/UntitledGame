package src.engine;

import javax.swing.*;
import java.awt.*;

public class MyIcon implements Icon {
    private final GameObject gameObject;

    public MyIcon(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        gameObject.setX(x);
        gameObject.setY(y);
        gameObject.draw(g);
    }

    @Override
    public int getIconWidth() {
        return gameObject.getSize();
    }

    @Override
    public int getIconHeight() {
        return gameObject.getSize();
    }
}
