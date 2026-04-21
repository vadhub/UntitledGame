// FireTower.java
import java.awt.*;

/**
 * Огненная башня — только визуально отличается.
 * Без дополнительных механик (горение, DoT и т.д.)
 */
public class FireTower extends Tower {

    public FireTower(int id, float x, float y, int size, float heal, float xOffset) {
        super(id, x, y, size, heal, TowerType.STONE, xOffset);
        this.color = new Color(200, 80, 40);  // огненный цвет
    }

    public FireTower(int id, float x, float y, float xOffset) {
        super(id, x, y, 100, 0, TowerType.STONE, xOffset);
        this.color = new Color(200, 80, 40);
    }

    public FireTower(int id, float x, float y) {
        super(id, x, y, 100, 0, TowerType.STONE, 0);
        this.color = new Color(200, 80, 40);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = (int)(x + getXOffset());
        int baseY = (int)y;
        int towerHeight = 280;

        // Градиент от красного к оранжевому
        GradientPaint fireGradient = new GradientPaint(
                centerX - 40, baseY - towerHeight, new Color(255, 80, 40),
                centerX + 40, baseY, new Color(180, 50, 30)
        );
        g2d.setPaint(fireGradient);

        // Корпус башни
        int[] towerX = {
                centerX - 45, centerX + 45, centerX + 35, centerX + 35,
                centerX, centerX - 35, centerX - 35
        };
        int[] towerY = {
                baseY, baseY, baseY - towerHeight + 80, baseY - towerHeight + 40,
                baseY - towerHeight, baseY - towerHeight + 40, baseY - towerHeight + 80
        };

        g2d.fillPolygon(towerX, towerY, 7);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(towerX, towerY, 7);

        // Огненный шар на вершине
        g2d.setColor(new Color(255, 80, 40));
        g2d.fillOval(centerX - 20, baseY - towerHeight - 15, 40, 40);
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(centerX - 10, baseY - towerHeight - 5, 20, 20);
        g2d.setColor(Color.ORANGE);
        g2d.fillOval(centerX - 5, baseY - towerHeight, 10, 10);

        // Окно в виде пламени
        g2d.setColor(new Color(255, 100, 50));
        g2d.fillRect(centerX - 12, baseY - towerHeight + 130, 24, 30);
        g2d.setColor(Color.YELLOW);
        g2d.fillRect(centerX - 8, baseY - towerHeight + 135, 16, 20);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(centerX - 12, baseY - towerHeight + 130, 24, 30);

        // Дверь
        int doorWidth = 35;
        int doorHeight = 50;
        g2d.setColor(new Color(140, 60, 30));
        g2d.fillRect(centerX - doorWidth/2, baseY - doorHeight, doorWidth, doorHeight);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(centerX - doorWidth/2, baseY - doorHeight, doorWidth, doorHeight);

        // Раскалённая ручка
        g2d.setColor(Color.RED);
        g2d.fillOval(centerX + doorWidth/2 - 10, baseY - doorHeight/2, 6, 6);

        // Полоса здоровья
        int barWidth = 90;
        int barHeight = 10;
        int barX = centerX - barWidth/2;
        int barY = baseY - 310;

        g2d.setColor(Color.GRAY);
        g2d.fillRect(barX, barY, barWidth, barHeight);

        int healthPercent = (int)((getCurrentHealth() / (float)getMaxHealth()) * barWidth);
        g2d.setColor(new Color(255, 80, 40));
        g2d.fillRect(barX, barY, healthPercent, barHeight);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(barX, barY, barWidth, barHeight);

        // Текст здоровья
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        String healthText = getCurrentHealth() + "/" + getMaxHealth();
        FontMetrics fm = g2d.getFontMetrics();
        int textX = centerX - fm.stringWidth(healthText)/2;
        g2d.setColor(Color.WHITE);
        g2d.drawString(healthText, textX, barY + barHeight - 2);
    }
}
