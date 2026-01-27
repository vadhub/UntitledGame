import java.awt.*;

public class Barn {

    private int heath = 200;

    public Barn(int heath) {
        this.heath = heath;
    }

    public Barn() {
    }

    public void draw(Graphics g, int panelHeight, int panelWidth) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_MITER));

        // Высота сарая
        int barnHeight = 200;

        // Координаты основания сарая
        int baseY = panelHeight - 50;
        int centerX = panelWidth / 2;

        // Размеры сарая
        int barnWidth = 250;
        int roofHeight = 50;
        // РИСУЕМ ОБЛАКА (сверху)
        g2d.setColor(new Color(240, 248, 255)); // Цвет облаков
        // Облако 1 (слева)
        g2d.fillOval(50, 30, 70, 50);
        g2d.fillOval(80, 20, 60, 50);
        g2d.fillOval(100, 30, 70, 40);
        // Облако 2 (по центру)
        g2d.fillOval(centerX - 100, 50, 80, 50);
        g2d.fillOval(centerX - 70, 40, 70, 50);
        g2d.fillOval(centerX - 40, 50, 80, 45);
        // Облако 3 (справа)
        g2d.fillOval(panelWidth - 150, 40, 75, 55);
        g2d.fillOval(panelWidth - 120, 30, 65, 50);
        g2d.fillOval(panelWidth - 90, 40, 80, 45);

        // Контуры облаков
        g2d.setColor(new Color(200, 220, 240));
        g2d.setStroke(new BasicStroke(1));
        // Облако 1
        g2d.drawOval(50, 30, 70, 50);
        g2d.drawOval(80, 20, 60, 50);
        g2d.drawOval(100, 30, 70, 40);
        // Облако 2
        g2d.drawOval(centerX - 100, 50, 80, 50);
        g2d.drawOval(centerX - 70, 40, 70, 50);
        g2d.drawOval(centerX - 40, 50, 80, 45);
        // Облако 3
        g2d.drawOval(panelWidth - 150, 40, 75, 55);
        g2d.drawOval(panelWidth - 120, 30, 65, 50);
        g2d.drawOval(panelWidth - 90, 40, 80, 45);


        // БОЛЬШОЙ КАМЕНЬ справа от сарая (полигонами)
        g2d.setStroke(new BasicStroke(2));
        int stoneX = centerX + barnWidth/2 + 50; // Правее сарая
        int stoneY = baseY - 80; // На земле

        // Координаты полигона для камня (неправильная форма)
        int[] stonePolygonX = {
                stoneX + 10,   // точка 1
                stoneX + 65,   // точка 2
                stoneX + 80,   // точка 3
                stoneX + 70,   // точка 4
                stoneX + 60,   // точка 5
                stoneX + 40,   // точка 6
                stoneX + 20,   // точка 7
                stoneX          // точка 8
        };

        int[] stonePolygonY = {
                stoneY + 20,   // точка 1
                stoneY + 10,   // точка 2
                stoneY + 30,   // точка 3
                stoneY + 55,   // точка 4
                stoneY + 65,   // точка 5
                stoneY + 60,   // точка 6
                stoneY + 50,   // точка 7
                stoneY + 35    // точка 8
        };

        // Заливка камня (градиентный эффект)
        GradientPaint stoneGradient = new GradientPaint(
                stoneX, stoneY, new Color(140, 140, 140), // светлее сверху
                stoneX + 80, stoneY + 70, new Color(100, 100, 100) // темнее снизу
        );
        g2d.setPaint(stoneGradient);
        g2d.fillPolygon(stonePolygonX, stonePolygonY, 8);

        // Контур камня
        g2d.setColor(new Color(80, 80, 80));
        g2d.drawPolygon(stonePolygonX, stonePolygonY, 8);

        // Текстура камня (трещины и неровности)
        g2d.setColor(new Color(90, 90, 90));
        g2d.setStroke(new BasicStroke(1.5f));

        // Тень под камнем
        g2d.setColor(new Color(70, 70, 70, 80));
        int[] shadowX = {
                stoneX + 5,
                stoneX + 75,
                stoneX + 70,
                stoneX + 10
        };
        int[] shadowY = {
                stoneY + 70,
                stoneY + 75,
                stoneY + 85,
                stoneY + 80
        };

        // Координаты сарая
        int[] barnX = {
                centerX - barnWidth / 2,
                centerX + barnWidth / 2,
                centerX + barnWidth / 2,
                centerX + barnWidth / 2 - 30,
                centerX,
                centerX - barnWidth / 2 + 30,
                centerX - barnWidth / 2
        };

        int[] barnY = {
                baseY,
                baseY,
                baseY - barnHeight,
                baseY - barnHeight - roofHeight,
                baseY - barnHeight - roofHeight - 20,
                baseY - barnHeight - roofHeight,
                baseY - barnHeight
        };

        // Рисуем сарай
        g2d.setColor(new Color(180, 140, 100));
        g2d.fillPolygon(barnX, barnY, 7);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(barnX, barnY, 7);

        // Ворота сарая
        int gateWidth = 120;
        int gateHeight = 140;
        int[] gateX = {
                centerX - gateWidth / 2,
                centerX + gateWidth / 2,
                centerX + gateWidth / 2,
                centerX - gateWidth / 2
        };
        int[] gateY = {
                baseY - gateHeight,
                baseY - gateHeight,
                baseY,
                baseY
        };

        g2d.setColor(new Color(120, 80, 40));
        g2d.fillPolygon(gateX, gateY, 4);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(gateX, gateY, 4);

        // Поперечины на воротах
        g2d.setStroke(new BasicStroke(4));
        g2d.drawLine(centerX - gateWidth / 2 + 10, baseY - gateHeight / 3,
                centerX + gateWidth / 2 - 10, baseY - gateHeight / 3);
        g2d.drawLine(centerX - gateWidth / 2 + 10, baseY - 2 * gateHeight / 3,
                centerX + gateWidth / 2 - 10, baseY - 2 * gateHeight / 3);

        // Калитка
        int smallDoorWidth = 35;
        int smallDoorHeight = 60;
        int[] smallDoorX = {
                centerX + gateWidth / 4 - smallDoorWidth / 2,
                centerX + gateWidth / 4 + smallDoorWidth / 2,
                centerX + gateWidth / 4 + smallDoorWidth / 2,
                centerX + gateWidth / 4 - smallDoorWidth / 2
        };
        int[] smallDoorY = {
                baseY - smallDoorHeight,
                baseY - smallDoorHeight,
                baseY,
                baseY
        };

        g2d.setColor(new Color(100, 60, 30));
        g2d.fillPolygon(smallDoorX, smallDoorY, 4);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(smallDoorX, smallDoorY, 4);

        // Ручка на калитке
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(centerX + gateWidth / 4 + smallDoorWidth / 2 - 8,
                baseY - smallDoorHeight / 2, 6, 6);

        // ЕДИНСТВЕННОЕ ОКНО - ПО ЦЕНТРУ САРАЯ (ПОДНЯТО ЕЩЕ ВЫШЕ)
        int windowY = baseY - barnHeight + 10; // БЫЛО: +30, СТАЛО: +20 (еще выше на 10 пикселей)
        int windowWidth = 60;
        int windowHeight = 50;
        int windowCenterX = centerX;

        int[] windowX = {
                windowCenterX - windowWidth / 2,
                windowCenterX + windowWidth / 2,
                windowCenterX + windowWidth / 2,
                windowCenterX - windowWidth / 2
        };

        int[] windowYCoords = {
                windowY,
                windowY,
                windowY + windowHeight,
                windowY + windowHeight
        };

        g2d.setColor(new Color(200, 230, 255));
        g2d.fillPolygon(windowX, windowYCoords, 4);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(windowX, windowYCoords, 4);

        // Переплет окна
        g2d.drawLine(windowCenterX, windowY, windowCenterX, windowY + windowHeight);
        g2d.drawLine(windowCenterX - windowWidth / 2, windowY + windowHeight / 2,
                windowCenterX + windowWidth / 2, windowY + windowHeight / 2);

        // БОЛЬШОЙ ФЛАГ
        int flagPoleHeight = 40;
        int flagPoleX = centerX;
        int flagPoleTopY = baseY - barnHeight - roofHeight - 50; // БЫЛО: -20, СТАЛО: -30 (поднят выше)

        // Флагшток (толще)
        g2d.setColor(new Color(101, 67, 33));
        g2d.setStroke(new BasicStroke(3)); // БЫЛО: 2, СТАЛО: 3 (толще)
        g2d.drawLine(flagPoleX, baseY - barnHeight - roofHeight,
                flagPoleX, flagPoleTopY);

        //  Флажок
        int[] flagX = {
                flagPoleX,
                flagPoleX + 35,
                flagPoleX
        };
        int[] flagY = {
                flagPoleTopY + 8,
                flagPoleTopY - 10,
                flagPoleTopY + 25
        };

        g2d.setColor(Color.RED);
        g2d.fillPolygon(flagX, flagY, 3);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(flagX, flagY, 3);

        // Украшение на вершине флагштока (больше)
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(flagPoleX - 4, flagPoleTopY - 4, 8, 8); // БЫЛО: 6x6, СТАЛО: 8x8
    }
}

