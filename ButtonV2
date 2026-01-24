import javax.swing.*;
import java.awt.*;

public class Button extends JButton {

    public Button(String text, Icon icon, Event event) {
        Image img = ((ImageIcon) icon).getImage();
        Image scaledImg = img.getScaledInstance(100, 80, Image.SCALE_REPLICATE);
        super(text, new ImageIcon(scaledImg));
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.TOP);
        setHorizontalAlignment(SwingConstants.CENTER);
        setMargin(new Insets(5, 5, 5, 5));
        addActionListener(e -> {
            event.action();
        });
    }
}
