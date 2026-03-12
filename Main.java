import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Untitled Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel, BorderLayout.CENTER);
        // JPanel controlPanel = new Control();
        // frame.add(controlPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
        // startBackgroundAnimation(gamePanel);
    }
    private static void startBackgroundAnimation(GamePanel gamePanel) {
        Timer timer = new Timer(50, e -> gamePanel.updateBackground());
        timer.start();
    }
}