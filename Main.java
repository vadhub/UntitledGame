import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainMenu();
        });
    }

    public static void startGame() {
        SwingUtilities.invokeLater(() -> {
            CurrencyManager.getInstance();
            Engine engine = Engine.getInstance();

            int towerX = engine.getScreenWidth() / 2 + 200;
            int towerY = 530;

            Tower tower = new Tower(1, (float) towerX, (float) towerY, 100, 0f);
            tower.setFraction(2);
            engine.spawnObject(tower);

            GameView gameView = new GameView(engine);
            JFrame frame = new JFrame("Tower Battle - Archer Defense");

            frame.setSize(800, 900);

            frame.setLayout(new BorderLayout());
            frame.add(gameView, BorderLayout.CENTER);
            frame.add(new Controls(engine), BorderLayout.SOUTH);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}