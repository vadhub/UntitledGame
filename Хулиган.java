import java.awt.*;

public class Хулиган extends GameObject {

    public Хулиган() {
        super(-1, 150, 150, 30, 1);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        int x = (int) this.x;
        int y = (int) this.y;
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawOval(x, y, 30, 30); // голова
        g2.drawLine(x+15, y+30, x+15, y+80); //   тело
        g2.drawLine(x+15, y+50, x-25, y+70); // левая рука
        g2.drawLine(x+15, y+50, x+50, y+70); // правая рука (держит дубинку)
        g2.drawLine(x+15, y+80, x-10, y+120); // левая нога
        g2.drawLine(x+15, y+80, x+40, y+120); // правая нога

        g2.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(x+50, y+70, x+80, y+20); // дубинка
    }
}