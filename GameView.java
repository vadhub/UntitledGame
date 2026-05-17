package engine;

import view.background.Background;
import view.background.RoadPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameView extends JPanel implements MouseListener {
    private long lastFrameTime;
    private Background background = new Background();
    private RoadPanel roadPanel;
    private Engine engine;
    private CurrencyManager currency;

    public GameView(Engine engine) {
        this.engine = engine;
        this.currency = CurrencyManager.getInstance();
        this.roadPanel = new RoadPanel();
        lastFrameTime = System.currentTimeMillis();

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
    public void paint(Graphics g) {
        super.paint(g);

        // Рисуем небо и облака
        background.drawSky((Graphics2D) g, getWidth(), getHeight());
        background.drawClouds((Graphics2D) g, getWidth(), getHeight());

        // Рисуем дорогу с травой
        roadPanel.setBounds(0, 0, getWidth(), getHeight());
        roadPanel.paintComponent(g);

        // Рисуем игровые объекты
        engine.draw(g);

        // Отображение валюты
        drawCurrencyPanel(g);
    }

    private void drawCurrencyPanel(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = 15;
        int y = 15;

        // Полупрозрачный фон
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(x, y, 120, 45, 12, 12);

        // Золотая рамка
        g2d.setColor(new Color(255, 215, 0));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(x, y, 120, 45, 12, 12);

        // Иконка монеты
        g2d.setColor(new Color(255, 215, 0));
        g2d.fillOval(x + 8, y + 7, 30, 30);
        g2d.setColor(new Color(255, 180, 0));
        g2d.fillOval(x + 10, y + 9, 26, 26);

        // Символ валюты
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("$", x + 15, y + 30);

        // Количество валюты
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        String currencyText = currency.getCurrency() + " у.к.";
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