import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        // Запускаем меню
        SwingUtilities.invokeLater(() -> {
            new MainMenu();
        });
    }

    // ЭТОТ МЕТОД ЗАПУСКАЕТ ИГРУ (вызывается из меню)
    public static void startGame() {
        SwingUtilities.invokeLater(() -> {
            CurrencyManager.getInstance();

            Engine engine = Engine.getInstance();
            engine.clearObjects();

            int playerTowerX = engine.getScreenWidth() / 2 - 200;
            int enemyTowerX = engine.getScreenWidth() / 2 + 200;
            int towerY = engine.getScreenHeight() - 180;

            Tower enemyTower = new Tower(1, (float) enemyTowerX, (float) towerY, 100, 0f);
            enemyTower.setFraction(1);
            engine.spawnObject(enemyTower);

            Tower playerTower = new Tower(2, (float) playerTowerX, (float) towerY, 100, 0f);
            playerTower.setFraction(2);
            engine.spawnObject(playerTower);

            GameView gameView = new GameView(engine);
            JFrame frame = new JFrame("Tower Battle - Archer Defense");
            frame.setSize(engine.getScreenWidth(), engine.getScreenHeight());
            frame.setLayout(new BorderLayout());
            frame.add(gameView, BorderLayout.CENTER);
            frame.add(new Controls(engine), BorderLayout.SOUTH);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
