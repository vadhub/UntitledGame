package src.view;

import src.engine.GameObject;
import src.view.background.Tower;

import java.awt.*;

public class HealthBar {
    private static final int BAR_WIDTH = 60;
    private static final int BAR_HEIGHT = 8;
    private static final int OFFSET_Y = -20;

    public static void draw(Graphics g, GameObject obj) {
        if (!obj.isAlive()) return;

        int currentHealth = obj.getHealth();
        int maxHealth = getMaxHealth(obj);

        if (maxHealth <= 0) return;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int barX = (int) obj.getX() - BAR_WIDTH / 2;
        int barY = (int) obj.getY() - obj.getSize() + OFFSET_Y;

        // Фон
        g2d.setColor(new Color(60, 60, 60, 200));
        g2d.fillRect(barX, barY, BAR_WIDTH, BAR_HEIGHT);

        // Цвет полоски
        float healthPercent = (float) currentHealth / maxHealth;
        Color healthColor;

        if (healthPercent > 0.6f) {
            healthColor = new Color(76, 175, 80);
        } else if (healthPercent > 0.3f) {
            healthColor = new Color(255, 193, 7);
        } else {
            healthColor = new Color(244, 67, 54);
        }

        g2d.setColor(healthColor);
        int filledWidth = (int) (BAR_WIDTH * Math.max(0, Math.min(1, healthPercent)));
        g2d.fillRect(barX, barY, filledWidth, BAR_HEIGHT);

        // Обводка
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(barX, barY, BAR_WIDTH, BAR_HEIGHT);

        // Текст HP
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        String hpText = currentHealth + "/" + maxHealth;
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(hpText);
        g2d.drawString(hpText, barX + BAR_WIDTH / 2 - textWidth / 2, barY - 2);

        g2d.dispose();
    }

    private static int getMaxHealth(GameObject obj) {
        if (obj instanceof Tower) {
            return ((Tower) obj).getMaxHealth();
        }
        return obj.getMaxHealth();
    }
}
