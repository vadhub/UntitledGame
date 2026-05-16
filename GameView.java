import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameView extends JPanel implements MouseListener {
    private long lastFrameTime;
    private Background background = new Background();
    private RoadPanel roadPanel = new RoadPanel();
    private Engine engine;
    private CurrencyManager currency;

    public GameView(Engine engine) {
        this.engine = engine;
        this.currency = CurrencyManager.getInstance();
        lastFrameTime = System.currentTimeMillis();

        new Timer(16, e -> {
            long now = System.currentTimeMillis();
            float deltaTime = (now - lastFrameTime) / 1000f;
            lastFrameTime = now;
            if (deltaTime > 0.05f) deltaTime = 0.05f;
            engine.update(deltaTime);
            repaint();
        }).start();

        addMouseListener(this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        background.draw(g2d, getWidth(), getHeight());
        roadPanel.setBounds(0, 0, getWidth(), getHeight());
        roadPanel.paintComponent(g2d);
        engine.draw(g2d);
        drawCurrencyPanel(g2d);

        g2d.dispose();
    }

    private void drawCurrencyPanel(Graphics2D g) {
        int x = 15;
        int y = 15;

        g.setColor(new Color(0, 0, 0, 180));
        g.fillRoundRect(x, y, 120, 45, 12, 12);

        g.setColor(new Color(255, 215, 0));
        g.setStroke(new BasicStroke(1.5f));
        g.drawRoundRect(x, y, 120, 45, 12, 12);

        g.setColor(new Color(255, 215, 0));
        g.fillOval(x + 8, y + 7, 30, 30);
        g.setColor(new Color(255, 180, 0));
        g.fillOval(x + 10, y + 9, 26, 26);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("$", x + 15, y + 30);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(currency.getCurrency() + " у.к.", x + 48, y + 30);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    public void draw() { repaint(); }
}