// Grass.java
import java.awt.*;
import java.util.Random;

public class Grass {
    private float x;
    private float y;
    private int variant;
    private int bladesCount;
    private int[] bladeHeights;
    private int[] bladeOffsets;
    private static final Random rand = new Random();

    public Grass(float x, float y) {
        this.x = x;
        this.y = y;
        this.variant = rand.nextInt(3);

        // Генерируем параметры травинок один раз (статично)
        this.bladesCount = rand.nextInt(3) + 3;  // 3-5 травинок
        this.bladeHeights = new int[bladesCount];
        this.bladeOffsets = new int[bladesCount];

        for (int i = 0; i < bladesCount; i++) {
            bladeHeights[i] = rand.nextInt(15) + 10;  // высота 10-25
            bladeOffsets[i] = rand.nextInt(8) - 4;    // смещение -4..3
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // Разные оттенки зелёного
        Color grassColor;
        switch(variant) {
            case 0: grassColor = new Color(50, 140, 50); break;
            case 1: grassColor = new Color(60, 150, 60); break;
            default: grassColor = new Color(40, 130, 40); break;
        }
        g2d.setColor(grassColor);

        // Рисуем травинки по заранее сгенерированным параметрам
        for (int i = 0; i < bladesCount; i++) {
            int x1 = (int)x + bladeOffsets[i];
            int y1 = (int)y;
            int x2 = x1 + (bladeOffsets[i] / 2);
            int y2 = y1 - bladeHeights[i];

            g2d.drawLine(x1, y1, x2, y2);
        }
    }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public float getX() { return x; }
    public float getY() { return y; }
}
