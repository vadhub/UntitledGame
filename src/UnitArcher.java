import javax.swing.*;
import java.awt.*;

public class UnitArcher extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawArcher(g);
    }

    private void drawArcher(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //Голова
        g2d.setColor(new Color(255, 218, 185));
        g2d.fillOval(150, 50, 70, 80);

        //Глаза
        g2d.setColor(Color.BLACK);
        g2d.fillOval(175, 80, 10, 12);
        g2d.fillOval(200, 80, 10, 12);

        //Тело
        g2d.setColor(new Color(70, 130, 180)); // стальной синий
        g2d.fillOval(140, 130, 90, 140); // туловище

        //Лук
        g2d.setColor(new Color(101, 67, 33)); // коричневый
        g2d.setStroke(new BasicStroke(4));

        g2d.drawArc(180, 100, 100, 150, 270, 190);

        // Тетива
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));

        int bowCenterX = 230;
        int bowTopY = 102;
        int bowBottomY = 250;

        g2d.drawLine(bowCenterX, bowTopY, bowCenterX, bowBottomY);

        // Колчан
        g2d.setColor(new Color(139, 69, 19));
        g2d.setStroke(new BasicStroke(1));

        g2d.fillRect(120, 150, 25, 60);
        g2d.fillOval(120, 145, 25, 20);

        // Стрелы в колчане
        g2d.setColor(Color.DARK_GRAY);
        for (int i = 0; i < 3; i++) {
            int y = 160 + i * 15;
            g2d.drawLine(125, y, 140, y);
        }
    }
}
