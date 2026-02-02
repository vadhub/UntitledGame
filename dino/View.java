import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import javax.swing.JPanel;

//КУ  И 2
//КА  С 3
//ЕВ  П 1
public class View extends JPanel {
    public View() {
    }
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.drawTipNaDinozavre(g2d, 100, 250);
    }

    void drawTipNaDinozavre(Graphics2D g2, int x, int y) {
        // тень под ногами
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillOval(x - 25, y + 50, 150, 20);
        //ноги задние
        g2.setColor(new Color(100, 160, 100));
        g2.fillRoundRect(x + 17, y + 30, 18, 30, 10, 10);
        g2.fillRoundRect(x + 80, y + 30, 18, 30, 10, 10);
        // тело светло-салатового-зелено-темного динозавра
        g2.setColor(new Color(100, 180, 100));
        g2.fillRoundRect(x - 10, y - 10, 110, 55, 30, 50);
        // шея и голова динозавра
        g2.fillRoundRect(x + 70, y - 60, 35, 75, 25, 45);
        g2.setColor(new Color(100, 160, 100));
        g2.fillRoundRect(x + 65, y - 80, 35, 45, 25, 35);
        g2.fillRoundRect(x + 85, y - 70, 35, 35, 25, 70);
        //ноги динозавра передние
        g2.fillRoundRect(x - 5, y + 30, 18, 30, 10, 10);
        g2.fillRoundRect(x + 60, y + 30, 18, 30, 10, 10);
        // глаза и ноздри
        g2.setColor(new Color(255, 255, 255));
        g2.fillOval(x + 77, y - 72, 10, 15);
        g2.fillOval(x + 90, y - 77, 10, 15);
        g2.setColor(new Color(0, 0, 0));
        g2.fillOval(x + 83, y - 67, 5, 7);
        g2.fillOval(x +96, y - 72, 5, 7);
        g2.setColor(new Color(40, 40, 40));
        g2.fillOval(x + 103, y - 62, 5, 7);
        g2.fillOval(x + 110, y - 65, 5, 7);
        // перс на динозавре
        g2.setColor(new Color(108, 29, 13));
        g2.fillRoundRect(x + 15, y - 40, 40, 50, 35, 75);
        g2.setColor(new Color(217, 142, 73));
        g2.fillOval(x + 17, y - 65, 35, 35);
        g2.setColor(new Color(0, 0, 0));
        g2.fillOval(x + 42, y - 57, 5, 10);
        g2.fillOval(x + 33, y - 55, 5, 10);
        //копье - хотел сделать под углом, но треугольник не получилось поверунть
        g2.setStroke(new BasicStroke(7.0F));
        g2.setColor(new Color(121, 67, 25));
        g2.drawLine(x - 20, y - 15, x + 100, y - 15);
        g2.setStroke(new BasicStroke(3.0F));
        g2.setColor(new Color(128, 121, 115));
        int[] xPoints = {x + 100, x + 120, x + 100};
        int[] yPoints = {y - 25, y - 15, y - 5};
        g2.fillPolygon(xPoints, yPoints, 3);


    }
}
