import javax.swing.*;
import java.awt.*;

public class GameView extends JPanel {
    private Engine engine;

    public GameView(Engine engine) {
        this.engine = engine;
        new Timer(16, e -> {
            engine.update(0.016f);
            repaint();
        }).start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    public void draw(Graphics g) {
        engine.render(g);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
}