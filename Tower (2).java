import java.awt.*;

public class Tower {

    private int heath = 200;

    public Tower(int heath) {
        this.heath = heath;
    }

    public Tower() {
    }

    public void draw(Graphics g, int panelHeight, int panelWidth) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_MITER));

        g2d.setColor(new Color(135, 206, 250)); // светло-голубой
        g2d.fillRect(0, 0, panelWidth, panelHeight);

        drawClouds(g2d, panelWidth, panelHeight);
        drawRocks(g2d, panelWidth, panelHeight);


        // Высота башни (от основания до вершины флага)
        int towerHeight = 300;

        // Координаты основания башни (внизу экрана)
        int baseY = panelHeight - 50; // Основание с небольшим отступом от низа
        int centerX = panelWidth / 2; // Центр экрана

        // Основной корпус башни (шире внизу, уже вверху)
        int baseWidth = 120;  // Ширина основания
        int topWidth = 80;    // Ширина вершины

        int[] towerX = {
                centerX - baseWidth/2,  // левый низ (1)
                centerX + baseWidth/2,  // правый низ (2)
                centerX + topWidth/2,   // правый верх (3)
                centerX + topWidth/2,   // правый верх зубцов (4)
                centerX + topWidth/2 - 10, // выступ справа (5)
                centerX + topWidth/2 - 10, // правая сторона крыши (6)
                centerX,               // вершина крыши (7)
                centerX - topWidth/2 + 10, // левая сторона крыши (8)
                centerX - topWidth/2 + 10, // выступ слева (9)
                centerX - topWidth/2,   // левый верх зубцов (10)
                centerX - topWidth/2    // левый верх (11)
        };

        int[] towerY = {
                baseY,                      // низ (1)
                baseY,                      // низ (2)
                baseY - towerHeight + 100,  // начало сужения (3)
                baseY - towerHeight + 50,   // верх основной части (4)
                baseY - towerHeight + 50,   // (5)
                baseY - towerHeight + 40,   // верх зубцов (6)
                baseY - towerHeight,        // вершина (7)
                baseY - towerHeight + 40,   // (8)
                baseY - towerHeight + 50,   // (9)
                baseY - towerHeight + 50,   // (10)
                baseY - towerHeight + 100   // (11)
        };

        // Рисуем башню
        g2d.setColor(new Color(140, 120, 100)); // цвет камня
        g2d.fillPolygon(towerX, towerY, 11);

        // Обводка башни
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(towerX, towerY, 11);

        // Рисуем зубцы (мерлоны)
        drawBattlements(g2d, centerX, baseY - towerHeight + 50, topWidth, 20, 6);

        // Окно
        int windowY = baseY - towerHeight + 150;
        int[] windowX = {centerX - 15, centerX + 15, centerX + 15, centerX - 15};
        int[] windowYCoords = {windowY, windowY, windowY + 30, windowY + 30};

        g2d.setColor(new Color(200, 230, 255)); // свет окна
        g2d.fillPolygon(windowX, windowYCoords, 4);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(windowX, windowYCoords, 4);

        // Переплет окна
        g2d.drawLine(centerX, windowY, centerX, windowY + 30);
        g2d.drawLine(centerX - 15, windowY + 15, centerX + 15, windowY + 15);

        // Дверь (в основании башни)
        int doorWidth = 40;
        int doorHeight = 60;
        int[] doorX = {
                centerX - doorWidth/2,
                centerX + doorWidth/2,
                centerX + doorWidth/2,
                centerX - doorWidth/2
        };
        int[] doorYCoords = {
                baseY - doorHeight,
                baseY - doorHeight,
                baseY,
                baseY
        };

        g2d.setColor(new Color(101, 67, 33)); // дерево
        g2d.fillPolygon(doorX, doorYCoords, 4);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(doorX, doorYCoords, 4);

        // Ручка двери
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(centerX + doorWidth/2 - 10, baseY - doorHeight/2, 6, 6);

        // Флаг по центру на вершине
        int flagPoleHeight = 40;
        int flagPoleX = centerX;
        int flagPoleTopY = baseY - towerHeight - flagPoleHeight;

        // Флагшток
        g2d.setColor(new Color(101, 67, 33)); // дерево
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(flagPoleX, baseY - towerHeight, flagPoleX, flagPoleTopY);

        // Флаг (треугольный)
        int[] flagX = {
                flagPoleX,
                flagPoleX + 25,
                flagPoleX
        };
        int[] flagY = {
                flagPoleTopY + 10,
                flagPoleTopY + 5,
                flagPoleTopY + 20
        };

        g2d.setColor(Color.RED);
        g2d.fillPolygon(flagX, flagY, 3);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(flagX, flagY, 3);

        // Украшение на вершине флагштока
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(flagPoleX - 4, flagPoleTopY - 4, 8, 8);
    }

    // облака
    private void drawClouds(Graphics2D g2d, int width, int height) {
        g2d.setColor(Color.WHITE);
        // Облако 1
        g2d.fillOval(width / 6, 60, 50, 30);
        g2d.fillOval(width / 6 + 20, 50, 50, 35);
        g2d.fillOval(width / 6 + 40, 60, 45, 30);
        // Облако 2
        g2d.fillOval(width / 2 - 30, 80, 50, 30);
        g2d.fillOval(width / 2 - 10, 70, 55, 35);
        g2d.fillOval(width / 2 + 15, 80, 50, 30);
        // Облако 3
        g2d.fillOval(4 * width / 5, 50, 50, 30);
        g2d.fillOval(4 * width / 5 + 20, 40, 55, 35);
        g2d.fillOval(4 * width / 5 + 40, 50, 45, 30);
    }

    //  камни
    private void drawRocks(Graphics2D g2d, int width, int height) {
        g2d.setColor(new Color(100, 90, 80));
        // Левый камень
        g2d.fillOval(width / 4 - 10, height - 60, 45, 37);
        // Правый камень
        g2d.fillOval(3 * width / 4 - 25, height - 55, 55, 45);
        // Мелкие камни
        g2d.fillOval(width / 3, height - 40, 30, 25);
        g2d.fillOval(2 * width / 3 - 30, height - 45, 35, 30);
    }

    private void drawBattlements(Graphics2D g2d, int centerX, int topY, int width, int height, int count) {
        int startX = centerX - width/2;
        int battlementWidth = width / count;
        int gapWidth = 5;

        g2d.setColor(new Color(100, 80, 60));

        for (int i = 0; i < count; i++) {
            int x = startX + i * battlementWidth;
            // Зубец (прямоугольник с вырезом)
            g2d.fillRect(x + gapWidth/2, topY - height,
                    battlementWidth - gapWidth, height);
        }

        // Обводка зубцов
        g2d.setColor(Color.BLACK);
        for (int i = 0; i < count; i++) {
            int x = startX + i * battlementWidth;
            g2d.drawRect(x + gapWidth/2, topY - height,
                    battlementWidth - gapWidth, height);
        }
    }
}