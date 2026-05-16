package src.engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameView extends JPanel implements MouseListener {
    private long lastFrameTime;
    private final Engine engine;
    private final CurrencyManager currency;

    public GameView(Engine engine) {
        this.engine = engine;
        this.currency = CurrencyManager.getInstance();
        lastFrameTime = System.currentTimeMillis();
        setOpaque(false);

        new Timer(16, e -> {
            long now = System.currentTimeMillis();
            float deltaTime = (now - lastFrameTime) / 1000.0f;
            lastFrameTime = now;
            if (deltaTime > 0.05f) deltaTime = 0.05f;
            engine.update(deltaTime);
            repaint();
        }).start();

        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        engine.draw(g);
        drawCurrencyPanel(g);
    }

    private void drawCurrencyPanel(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = 15;
        int y = 15;

        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(x, y, 120, 45, 12, 12);

        g2d.setColor(new Color(255, 215, 0));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(x, y, 120, 45, 12, 12);

        g2d.setColor(new Color(255, 215, 0));
        g2d.fillOval(x + 8, y + 7, 30, 30);
        g2d.setColor(new Color(255, 180, 0));
        g2d.fillOval(x + 10, y + 9, 26, 26);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("$", x + 15, y + 30);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        String currencyText = currency.getCurrency() + " u.e.";
        g2d.drawString(currencyText, x + 48, y + 30);

        g2d.dispose();
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

    public void draw() {
        repaint();
    }
}
