package  org.example;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        Tower tower = new Tower(8);
        JFrame frame = new JFrame("Test");
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());
        frame.add(new View(tower)); // ← передаём tower в View
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}