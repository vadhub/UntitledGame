package engine;

import javax.swing.*;
import java.awt.*;

public record MyIcon(GameObject gameObject) implements Icon {

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (gameObject != null) {

            int offsetY = switch (gameObject.getClass().getSimpleName()) {
                case "BaseUnit" -> 35;
                case "UnitDinoRider" -> 25;
                case "UnitArcher" -> -15;
                default -> 15;
            };

            gameObject.paintIcon(c, g, x, y + offsetY);
        }
    }

    @Override
    public int getIconWidth() {
        return 60;
    }

    @Override
    public int getIconHeight() {
        return 60;
    }
}