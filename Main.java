import javax.swing.*;
import java.awt.*;

public class Main {

    private static final String GAME_TITLE = "Tower Battle - Archer Defense";
    private static final int PLAYER_TOWER_ID = 2;
    private static final int ENEMY_TOWER_ID = 1;
    private static final int PLAYER_TOWER_OFFSET_X = -200;
    private static final int ENEMY_TOWER_OFFSET_X = 200;
    private static final int TOWER_Y_OFFSET = 180;
    private static final int TOWER_SIZE = 100;
    private static final float TOWER_HEAL = 0f;
    private static final int PLAYER_FRACTION = 2;
    private static final int ENEMY_FRACTION = 1;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenu::new);
    }

    public static void startGame() {
        Engine engine = Engine.getInstance();
        engine.clearObjects();

        int screenCenterX = engine.getScreenWidth() / 2;
        int towerY = engine.getScreenHeight() - TOWER_Y_OFFSET;

        int playerTowerX = screenCenterX + PLAYER_TOWER_OFFSET_X;
        int enemyTowerX = screenCenterX + ENEMY_TOWER_OFFSET_X;

        Tower enemyTower = new Tower(ENEMY_TOWER_ID, enemyTowerX, towerY, TOWER_SIZE, TOWER_HEAL);
        enemyTower.setFraction(ENEMY_FRACTION);
        engine.spawnObject(enemyTower);

        Tower playerTower = new Tower(PLAYER_TOWER_ID, playerTowerX, towerY, TOWER_SIZE, TOWER_HEAL);
        playerTower.setFraction(PLAYER_FRACTION);
        engine.spawnObject(playerTower);

        GameView gameView = new GameView(engine);
        JFrame frame = new JFrame(GAME_TITLE);
        frame.setSize(engine.getScreenWidth(), engine.getScreenHeight());
        frame.setLayout(new BorderLayout());
        frame.add(gameView, BorderLayout.CENTER);
        frame.add(new Controls(engine), BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
