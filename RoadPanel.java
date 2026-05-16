import javax.swing.*;
import java.awt.*;

public class RoadPanel extends JPanel {

    public RoadPanel() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        int groundY = 400;
        int grassTopH = 30;
        int roadH = 140;
        int grassBottomH = h - groundY - grassTopH - roadH;

        // 1. Верхняя трава
        g2d.setColor(new Color(76, 175, 80));
        g2d.fillRect(0, groundY, w, grassTopH);

        g2d.setColor(new Color(56, 142, 60));
        for (int i = 0; i < w; i += 15) {
            int height = 8 + (i % 3) * 4;
            int[] x = {i, i + 7, i + 15};
            int[] y = {groundY + grassTopH, groundY + grassTopH - height, groundY + grassTopH};
            g2d.fillPolygon(x, y, 3);
        }

        // 2. Дорога
        g2d.setColor(new Color(188, 143, 94));
        g2d.fillRect(0, groundY + grassTopH, w, roadH);

        g2d.setColor(new Color(160, 110, 70));
        g2d.fillOval(150, groundY + grassTopH + 40, 30, 12);
        g2d.fillOval(450, groundY + grassTopH + 90, 45, 15);
        g2d.fillOval(700, groundY + grassTopH + 50, 25, 10);
        g2d.fillOval(300, groundY + grassTopH + 70, 20, 8);

        // 3. Нижняя трава
        g2d.setColor(new Color(67, 160, 71));
        g2d.fillRect(0, groundY + grassTopH + roadH, w, grassBottomH);

        g2d.setColor(new Color(46, 125, 50));
        g2d.fillRect(0, groundY + grassTopH + roadH, w, 12);

        g2d.setColor(new Color(50, 140, 50));
        for (int i = 0; i < w; i += 12) {
            int height = 6 + (i % 2) * 4;
            int[] x = {i, i + 5, i + 10};
            int[] y = {groundY + grassTopH + roadH + 12,
                    groundY + grassTopH + roadH + 12 - height,
                    groundY + grassTopH + roadH + 12};
            g2d.fillPolygon(x, y, 3);
        }

        // 4. ДЕРЕВЬЯ НА НИЖНЕЙ ТРАВЕ
        drawTree(g2d, 60, 570, false);
        drawTree(g2d, 200, 580, true);
        drawTree(g2d, 350, 560, false);
        drawTree(g2d, 500, 575, true);
        drawTree(g2d, 650, 585, false);
        drawTree(g2d, 780, 565, true);

        g2d.dispose();
    }

    private void drawTree(Graphics2D g, int x, int y, boolean isPine) {
        g.setColor(new Color(0, 0, 0, 60));
        g.fillOval(x - 15, y + 35, 50, 12);

        if (isPine) {
            g.setColor(new Color(101, 67, 33));
            g.fillRect(x + 15, y + 20, 12, 25);
            g.setColor(new Color(34, 139, 34));
            g.fillPolygon(new int[]{x - 5, x + 45, x + 20}, new int[]{y + 30, y + 30, y}, 3);
            g.setColor(new Color(50, 179, 60));
            g.fillPolygon(new int[]{x, x + 40, x + 20}, new int[]{y + 15, y + 15, y - 15}, 3);
            g.setColor(new Color(80, 200, 80));
            g.fillPolygon(new int[]{x + 5, x + 35, x + 20}, new int[]{y, y, y - 30}, 3);
        } else {
            g.setColor(new Color(101, 67, 33));
            g.fillRect(x + 15, y + 20, 12, 25);
            g.setColor(new Color(34, 139, 34));
            g.fillOval(x, y, 45, 40);
            g.setColor(new Color(50, 179, 60));
            g.fillOval(x + 5, y - 10, 35, 35);
            g.setColor(new Color(80, 200, 80));
            g.fillOval(x + 10, y - 20, 25, 25);
        }
    }
}