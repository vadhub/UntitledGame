import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        Engine engine = Engine.getInstance();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        engine.setScreenWidth((int) screenSize.getWidth());
        engine.setScreenHeight((int) screenSize.getHeight());
        Tower tower = new Tower(1, 3350, 900, 50, 0); // Добавил
        tower.setFraction(0);
        engine.spawnObject(tower);
        GameView gameView = new GameView(engine);
        JFrame frame = new JFrame("Untitled Game");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());
        frame.add(gameView);
        frame.add(new Controls(engine), BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}