package src;

import src.view.Controls;
import src.engine.CurrencyManager;
import src.engine.Engine;
import src.engine.GameView;
import src.screen.MainMenu;
import src.view.background.Tower;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainMenu();
        });
    }

    // Относительные X-позиции башен (доля от ширины панели)
    private static final float LEFT_TOWER_REL_X  = 0.08f;  // левая башня
    private static final float RIGHT_TOWER_REL_X = 0.92f;  // правая башня

    public static void startGame() {
        SwingUtilities.invokeLater(() -> {
            CurrencyManager.getInstance();
            Engine engine = Engine.getInstance();

            // Начальные размеры окна
            int initWidth  = engine.getScreenWidth();
            int initHeight = engine.getScreenHeight();
            int groundY    = initHeight - 50;

            // === Левая башня (игрока) ===
            Tower leftTower = new Tower(1,
                    initWidth * LEFT_TOWER_REL_X,
                    groundY,
                    100,
                    Tower.TowerType.STONE,
                    0f);
            leftTower.setRelativeX(LEFT_TOWER_REL_X);
            leftTower.setFraction(2);
            engine.spawnObject(leftTower);

            // === Правая башня (противника) ===
            Tower rightTower = new Tower(2,
                    initWidth * RIGHT_TOWER_REL_X,
                    groundY,
                    100,
                    Tower.TowerType.STONE,
                    0f);
            rightTower.setRelativeX(RIGHT_TOWER_REL_X);
            rightTower.setFraction(1);
            engine.spawnObject(rightTower);

            GameView gameView = new GameView(engine);

            JFrame frame = new JFrame("Tower Battle - Archer Defense");
            frame.setSize(initWidth, initHeight);
            frame.setLayout(new BorderLayout());
            frame.add(gameView, BorderLayout.CENTER);
            frame.add(new Controls(engine), BorderLayout.SOUTH);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setResizable(true);   // разрешаем ресайз
            frame.setVisible(true);
        });
    }
}
