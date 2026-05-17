package src.engine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public record MyIcon(GameObject gameObject) implements Icon {

    private static final int ICON_SIZE = 80; // размер иконки в пикселях

    @Override
    public void paintIcon(Component c, Graphics g, int iconX, int iconY) {
        // Рисуем юнита в кнопку (чтобы весь поместился)
        int bufSize = 400;
        BufferedImage buf = new BufferedImage(bufSize, bufSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bg = buf.createGraphics();
        bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Ставим юнита в центр кнопки
        gameObject.setX(bufSize / 2 - 40);
        gameObject.setY(bufSize / 2 + 20);
        gameObject.draw(bg);
        bg.dispose();

        // Нахождение кнопки нарисованного юнита
        int minX = bufSize, minY = bufSize, maxX = 0, maxY = 0;
        for (int px = 0; px < bufSize; px++) {
            for (int py = 0; py < bufSize; py++) {
                if ((buf.getRGB(px, py) >>> 24) > 10) { // непрозрачный пиксель
                    if (px < minX) minX = px;
                    if (py < minY) minY = py;
                    if (px > maxX) maxX = px;
                    if (py > maxY) maxY = py;
                }
            }
        }

        if (maxX <= minX || maxY <= minY) return; // ничего не нарисовано

        int contentW = maxX - minX + 1;
        int contentH = maxY - minY + 1;

        // Вписываем с отступом 4px
        int padding = 4;
        int drawSize = ICON_SIZE - padding * 2;
        float scaleF = Math.min((float) drawSize / contentW, (float) drawSize / contentH);

        int destW = Math.round(contentW * scaleF);
        int destH = Math.round(contentH * scaleF);
        int destX = iconX + padding + (drawSize - destW) / 2;
        int destY = iconY + padding + (drawSize - destH) / 2;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(buf,
                destX, destY, destX + destW, destY + destH,
                minX, minY, maxX + 1, maxY + 1,
                null);
    }

    @Override
    public int getIconWidth() {
        return ICON_SIZE;
    }

    @Override
    public int getIconHeight() {
        return ICON_SIZE;
    }
}