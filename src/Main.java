package src;

import src.view.Controls;
import src.engine.CurrencyManager;
import src.engine.Engine;
import src.engine.GameView;
import src.screen.MainMenu;
import src.view.background.IceTower;

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

            int towerX = engine.getScreenWidth() / 2 + 200;
            int towerY = engine.getScreenHeight() - 180;

            IceTower tower = new IceTower(1, (float) towerX, (float) towerY, 100);
            tower.setFraction(2);
            engine.spawnObject(tower);

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
