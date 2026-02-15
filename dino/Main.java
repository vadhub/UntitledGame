
import javax.swing.JFrame;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(800, 400);
        frame.setLocationRelativeTo((Component)null);
        frame.add(new View());
        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);
    }
}

