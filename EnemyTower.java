import java.awt.*;

// 1) Добавить параметр смещения отностельно оси Х
// 2) Разные типы башен (возможно не только башни)
// 3) Механизм здоровья башни (200 >= MAX >= 150, 150 > MID >= 70, 70 > LOW >= 0)
public class EnemyTower {

    private int heath = 200;

    public EnemyTower(int heath) {
        this.heath = heath;
    }

    public EnemyTower() {
    }

    public void draw(Graphics g, int panelHeight, int panelWidth) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_MITER));

        // Высота башни (от основания до вершины флага)
        int towerHeight = 300;

        // Координаты основания башни (внизу экрана)
        int baseY = panelHeight - 50; // Основание с небольшим отступом от низа
        int centerX = panelWidth / 2; // Центр экрана

        // Основной корпус башни (шире внизу, уже вверху)
        int baseWidth = 120;  // Ширина основания
        int topWidth = 80;    // Ширина вершины

        int[] towerX = {
                centerX - baseWidth / 2,  // левый низ (1)
                centerX + baseWidth / 2,  // правый низ (2)
                centerX + topWidth / 2,   // правый верх (3)
                centerX + topWidth / 2,   // правый верх зубцов (4)
                centerX + topWidth / 2 - 10, // выступ справа (5)
                centerX + topWidth / 2 - 10, // правая сторона крыши (6)
                centerX,               // вершина крыши (7)
                centerX - topWidth / 2 + 10, // левая сторона крыши (8)
                centerX - topWidth / 2 + 10, // выступ слева (9)
                centerX - topWidth / 2,   // левый верх зубцов (10)
                centerX - topWidth / 2    // левый верх (11)
        };

        int[] towerY = {
                baseY,                      // низ (1)
                baseY,                      // низ (2)
                baseY - towerHeight + 100,  // начало сужения (3)
                baseY - towerHeight + 50,   // верх основной части (4)
                baseY - towerHeight + 15,   // (5)
                baseY - towerHeight + 100,   // верх зубцов (6)
                baseY - towerHeight,        // вершина (7)
                baseY - towerHeight + 100,   // (8)
                baseY - towerHeight + 15,   // (9)
                baseY - towerHeight + 50,   // (10)
                baseY - towerHeight + 100   // (11)
        };

        // Рисуем башню
        g2d.setColor(new Color(203, 10, 36)); // цвет камня
        g2d.fillPolygon(towerX, towerY, 11);

        // Обводка башни
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(towerX, towerY, 11);


        // Окно
        int windowY = baseY - towerHeight + 150;
        int[] windowX = {centerX - 15, centerX + 15, centerX + 15, centerX - 15};
        int[] windowYCoords = {windowY, windowY, windowY + 30, windowY + 30};

        g2d.setColor(new Color(216, 20, 20)); // свет окна
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
                centerX - doorWidth / 2,
                centerX + doorWidth / 2,
                centerX + doorWidth / 2,
                centerX - doorWidth / 2
        };
        int[] doorYCoords = {
                baseY - doorHeight,
                baseY - doorHeight,
                baseY,
                baseY
        };

        g2d.setColor(new Color(58, 9, 12)); // дерево
        g2d.fillPolygon(doorX, doorYCoords, 4);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(doorX, doorYCoords, 4);

        // Ручка двери
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(centerX + doorWidth / 2 - 10, baseY - doorHeight / 2, 6, 6);

        // Тип глаз по центру на вершине
        int EvilEyePoleHeight = 100;
        int EvilEyePoleWidth = 60;
        int EvilEyePoleX = centerX;
        int EvilEyePoleTopY = baseY - towerHeight - EvilEyePoleHeight/2;

        int[] EvilEyex = {
                EvilEyePoleX,                    // верхняя вершина
                EvilEyePoleX + EvilEyePoleWidth/2, // правая вершина
                EvilEyePoleX,                    // нижняя вершина
                EvilEyePoleX - EvilEyePoleWidth/2  // левая вершина
        };

        int[] EvilEyey = {
                EvilEyePoleTopY,                         // верх
                EvilEyePoleTopY + EvilEyePoleHeight/2,   // право
                EvilEyePoleTopY + EvilEyePoleHeight,     // низ
                EvilEyePoleTopY + EvilEyePoleHeight/2    // лево
        };

        g2d.setColor(Color.ORANGE);
        g2d.fillPolygon(EvilEyex, EvilEyey, 4);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(EvilEyex, EvilEyey, 4);

        // Рисуем круг в центре ромба
        int circleDiameter = Math.min(EvilEyePoleWidth, EvilEyePoleHeight) / 2; // диаметр круга
        int circleX = EvilEyePoleX - circleDiameter/2;
        int circleY = EvilEyePoleTopY + EvilEyePoleHeight/2 - circleDiameter/2;

        g2d.setColor(Color.RED);
        g2d.fillOval(circleX, circleY, circleDiameter, circleDiameter);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(circleX, circleY, circleDiameter, circleDiameter);

// ВЕРТИКАЛЬНАЯ черная линия по середине круга (не касается краев)
        int lineOffset = circleDiameter / 5; // Отступ от краев круга
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(5)); // Толстая линия
        g2d.drawLine(circleX + circleDiameter/2, circleY + lineOffset,
                circleX + circleDiameter/2, circleY + circleDiameter - lineOffset);
    }
}