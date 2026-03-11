package org.example;

import javax.swing.*;
import java.awt.*;

public class View extends JPanel {
    private Tower tower;

    public View(Tower tower) {
        this.tower = tower;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Рисуем башню
        tower.draw(g, getHeight(), getWidth());


        // Рисуем полоску хп над башней//////////////////////////////////////////////////////
        int barWidth = 120;
        int barHeight = 15;
        int barX = getWidth() / 6 - barWidth / 2;
        int barY = 110;

        g.setColor(Color.GRAY);
        g.fillRect(barX, barY, barWidth, barHeight);

        int healthWidth = tower.getHealth() * barWidth / 200;
        g.setColor(tower.getHealthColor());
        g.fillRect(barX, barY, healthWidth, barHeight);

        g.setColor(Color.BLACK);
        g.drawRect(barX, barY, barWidth, barHeight);

        g.setFont(new Font("Arial", Font.BOLD, 12));
        String hpText = "HP: " + tower.getHealth() + "/200";
        g.drawString(hpText, barX, barY - 5);
    }
}