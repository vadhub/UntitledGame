import java.awt.*;

public class Tower {

  
    // Конструктор по умолчанию
    public Tower() {
    }
    // Основной метод рисования башни
    public void draw(Graphics g, int panelHeight, int panelWidth) {
        // Преобразуем Graphics в Graphics2D для расширенных возможностей
        Graphics2D g2d = (Graphics2D) g;

        // Настраиваем толщину линии для всех элементов башни
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_MITER));

        // ========== ОСНОВНЫЕ ПАРАМЕТРЫ БАШНИ ==========
        int towerHeight = 400;                    // Общая высота башни
        int baseY = panelHeight - 40;             // Y-координата основания
        int centerX = panelWidth / 2;             // X-координата центра башни

        // Ширина башни на разных уровнях (сужается кверху)
        int baseWidth = 140;  // Ширина основания
        int midWidth = 100;   // Ширина в середине
        int topWidth = 60;    // Ширина наверху

        // ========== СОЗДАНИЕ ФОРМЫ БАШНИ ==========
        // Массив X-координат для 8 точек башни
        int[] towerX = {
                centerX - baseWidth/2,      // 1: Левый нижний угол
                centerX + baseWidth/2,      // 2: Правый нижний угол
                centerX + midWidth/2,       // 3: Правая середина
                centerX + topWidth/2,       // 4: Правый верх
                centerX,                    // 5: Самая верхняя точка
                centerX - topWidth/2,       // 6: Левый верх
                centerX - midWidth/2,       // 7: Левая середина
                centerX - baseWidth/2       // 8: Левый нижний (замыкаем)
        };

        // Массив Y-координат для 8 точек башни
        int[] towerY = {
                baseY,                      // 1: Низ башни
                baseY,                      // 2: Низ башни
                baseY - towerHeight/2,      // 3: Середина высоты
                baseY - towerHeight + 80,   // 4: 80 пикселей от вершины
                baseY - towerHeight - 20,   // 5: Самая высокая точка (-20 для острого верха)
                baseY - towerHeight + 80,   // 6: Симметрично точке 4
                baseY - towerHeight/2,      // 7: Симметрично точке 3
                baseY                       // 8: Низ башни (замыкаем)
        };

        // ========== ЗАЛИВКА БАШНИ ГРАДИЕНТОМ ==========
        // Создаем вертикальный градиент от темно-голубого к светло-голубому
        GradientPaint iceGradient = new GradientPaint(
                centerX - baseWidth/2, baseY,    // Начальная точка (низ слева)
                new Color(180, 220, 255),        // Начальный цвет (светло-голубой)
                centerX, baseY - towerHeight,    // Конечная точка (центр верха)
                new Color(220, 240, 255)         // Конечный цвет (бело-голубой)
        );
        g2d.setPaint(iceGradient);          // Устанавливаем градиент как кисть
        g2d.fillPolygon(towerX, towerY, 8); // Заливаем башню

        // ========== РИСУЕМ ЛЕДЯНЫЕ БЛИКИ ==========
        g2d.setColor(new Color(200, 230, 255, 150)); // Полупрозрачный голубой
        g2d.setStroke(new BasicStroke(2));           // Более тонкая линия для бликов

        // Координаты для линий бликов
        int[] highlightX = {
                centerX - baseWidth/4,           // Левая точка блика внизу
                centerX + baseWidth/4,           // Правая точка блика внизу
                centerX + topWidth/4,            // Правая точка блика сверху
                centerX,                         // Центральная верхняя точка
                centerX - topWidth/4,            // Левая точка блика сверху
                centerX - baseWidth/4            // Левая точка блика внизу (замыкаем)
        };

        int[] highlightY = {
                baseY - 20,                      // Низ бликов (немного выше основания)
                baseY - 20,                      // Низ бликов
                baseY - towerHeight + 100,       // Верх правого блика
                baseY - towerHeight + 60,        // Самая высокая точка бликов
                baseY - towerHeight + 100,       // Верх левого блика
                baseY - 20                       // Низ бликов (замыкаем)
        };

        // Рисуем полилинию бликов (без заливки)
        g2d.drawPolyline(highlightX, highlightY, 6);

        // ========== ОБВОДКА БАШНИ ==========
        g2d.setColor(new Color(100, 150, 200)); // Темно-голубой для контура
        g2d.drawPolygon(towerX, towerY, 8);     // Обводим башню

        // ========== ВЫЗОВ МЕТОДОВ ДЛЯ ДОПОЛНИТЕЛЬНЫХ ЭЛЕМЕНТОВ ==========

        // 1. Ледяные шипы по бокам башни
        drawIceSpikes(g2d, centerX, baseY, towerHeight, baseWidth);

        // 2. Главный кристалл на вершине
        int crystalTopY = baseY - towerHeight - 30; // Y выше вершины башни
        drawCrystal(g2d, centerX, crystalTopY);

        // 3. Ледяное окно в середине башни
        int windowY = baseY - towerHeight/2;
        drawIceWindow(g2d, centerX, windowY);

        // 4. Ледяная дверь у основания
        drawIceDoor(g2d, centerX, baseY - 100);

        // 5. Аура холода вокруг башни
        drawColdAura(g2d, centerX, windowY, 120);

        // 6. Название башни под основанием
        drawTowerName(g2d, centerX, baseY + 20);
    }

    private void drawTowerName(Graphics2D g2d, int centerX, int i) {
    }

    // ========== МЕТОД РИСОВАНИЯ ЛЕДЯНЫХ ШИПОВ ==========
    private void drawIceSpikes(Graphics2D g2d, int centerX, int baseY, int towerHeight, int baseWidth) {
        g2d.setColor(new Color(180, 220, 255)); // Светло-голубой цвет шипов

        // Левые шипы (5 штук)
        for (int i = 0; i < 5; i++) {
            int spikeY = baseY - (i * 60); // Каждый следующий шип выше на 60px

            // Проверяем, чтобы шипы не вышли за пределы башни
            if (spikeY > baseY - towerHeight + 100) {
                // Координаты для одного шипа (треугольная форма)
                int[] spikeX = {
                        centerX - baseWidth/2 - 10,   // База шипа (ближе к башне)
                        centerX - baseWidth/2 - 30,   // Острие шипа (дальше от башне)
                        centerX - baseWidth/2 - 15,   // Вершина треугольника
                        centerX - baseWidth/2         // Возврат к базе
                };
                int[] spikeYCoords = {spikeY, spikeY - 10, spikeY - 20, spikeY - 10};

                // Рисуем и заливаем шип
                g2d.fillPolygon(spikeX, spikeYCoords, 4);

                // Обводим шип темным контуром
                g2d.setColor(new Color(100, 150, 200));
                g2d.drawPolygon(spikeX, spikeYCoords, 4);

                // Возвращаем цвет для следующего шипа
                g2d.setColor(new Color(180, 220, 255));
            }
        }

        // Правые шипы (симметрично левым)
        for (int i = 0; i < 5; i++) {
            int spikeY = baseY - (i * 60);
            if (spikeY > baseY - towerHeight + 100) {
                int[] spikeX = {
                        centerX + baseWidth/2 + 10,
                        centerX + baseWidth/2 + 30,
                        centerX + baseWidth/2 + 15,
                        centerX + baseWidth/2
                };
                int[] spikeYCoords = {spikeY, spikeY - 10, spikeY - 20, spikeY - 10};
                g2d.fillPolygon(spikeX, spikeYCoords, 4);
                g2d.setColor(new Color(100, 150, 200));
                g2d.drawPolygon(spikeX, spikeYCoords, 4);
                g2d.setColor(new Color(180, 220, 255));
            }
        }
    }

    // ========== МЕТОД РИСОВАНИЯ ГЛАВНОГО КРИСТАЛЛА ==========
    private void drawCrystal(Graphics2D g2d, int centerX, int crystalY) {
        // Форма кристалла (ромб с заостренным низом)
        int[] crystalX = {
                centerX - 15,   // Левая точка
                centerX,        // Верхняя точка
                centerX + 15,   // Правая точка
                centerX,        // Нижняя точка
                centerX - 15    // Левая точка (замыкаем)
        };

        int[] crystalYCoords = {
                crystalY + 40,  // Левая точка (низ)
                crystalY + 20,  // Верхняя точка
                crystalY + 40,  // Правая точка (низ)
                crystalY + 60,  // Самая нижняя точка
                crystalY + 40   // Левая точка (замыкаем)
        };

        // Градиент для кристалла (сверху прозрачнее)
        GradientPaint crystalGradient = new GradientPaint(
                centerX, crystalY + 20,               // Верхняя точка
                new Color(150, 200, 255, 200),        // Более насыщенный цвет
                centerX, crystalY + 60,               // Нижняя точка
                new Color(100, 150, 255, 100)         // Более прозрачный цвет
        );

        g2d.setPaint(crystalGradient);
        g2d.fillPolygon(crystalX, crystalYCoords, 5);

        // ========== БЛИКИ НА КРИСТАЛЛЕ ==========
        g2d.setColor(new Color(255, 255, 255, 150)); // Белый полупрозрачный
        // Диагональная линия блика
        g2d.drawLine(centerX - 8, crystalY + 35, centerX + 8, crystalY + 45);
        // Вторая линия блика (параллельная первой)
        g2d.drawLine(centerX - 5, crystalY + 40, centerX + 5, crystalY + 50);

        // ========== ОБВОДКА КРИСТАЛЛА ==========
        g2d.setColor(new Color(80, 120, 200)); // Темно-синий контур
        g2d.drawPolygon(crystalX, crystalYCoords, 5);
    }

    // ========== МЕТОД РИСОВАНИЯ ЛЕДЯНОГО ОКНА ==========
    private void drawIceWindow(Graphics2D g2d, int centerX, int windowY) {
        int windowSize = 40; // Радиус окна

        // Создаем шестиугольное окно
        int[] windowX = new int[6];
        int[] windowYCoords = new int[6];

        // Вычисляем координаты вершин шестиугольника
        for (int i = 0; i < 6; i++) {
            double angle = 2 * Math.PI / 6 * i; // Угол для каждой вершины
            windowX[i] = centerX + (int)(windowSize * Math.cos(angle));
            windowYCoords[i] = windowY + (int)(windowSize * Math.sin(angle));
        }

        // Заливаем окно полупрозрачным голубым
        g2d.setColor(new Color(200, 230, 255, 180));
        g2d.fillPolygon(windowX, windowYCoords, 6);

        // Обводим окно
        g2d.setColor(new Color(100, 150, 220));
        g2d.drawPolygon(windowX, windowYCoords, 6);

        // ========== ЛЕДЯНЫЕ УЗОРЫ ВНУТРИ ОКНА ==========
        g2d.setStroke(new BasicStroke(1)); // Тонкие линии
        g2d.setColor(new Color(180, 210, 250)); // Светло-голубой

        // Рисуем линии от центра к вершинам (как лучи)
        for (int i = 0; i < 6; i++) {
            g2d.drawLine(centerX, windowY, windowX[i], windowYCoords[i]);
        }

        // Центральный круг (имитация отражения)
        g2d.setColor(new Color(220, 240, 255));
        g2d.fillOval(centerX - 5, windowY - 5, 10, 10);
    }

    // ========== МЕТОД РИСОВАНИЯ ЛЕДЯНОЙ ДВЕРИ ==========
    private void drawIceDoor(Graphics2D g2d, int centerX, int doorTopY) {
        int doorWidth = 50;
        int doorHeight = 80;

        // ========== ОСНОВА ДВЕРИ ==========
        g2d.setColor(new Color(170, 200, 230)); // Светло-голубой
        // Прямоугольная часть двери
        g2d.fillRect(centerX - doorWidth/2, doorTopY, doorWidth, doorHeight - 20);

        // Арочная верхняя часть двери
        g2d.fillArc(centerX - doorWidth/2, doorTopY - 20, doorWidth, 40, 0, 180);

        // ========== ОБВОДКА ДВЕРИ ==========
        g2d.setColor(new Color(130, 170, 210)); // Темнее для контура
        g2d.drawRect(centerX - doorWidth/2, doorTopY, doorWidth, doorHeight - 20);
        g2d.drawArc(centerX - doorWidth/2, doorTopY - 20, doorWidth, 40, 0, 180);

        // ========== ВЕРТИКАЛЬНЫЕ ДЕЛЕНИЯ ДВЕРИ ==========
        for (int i = 1; i < 3; i++) {
            int x = centerX - doorWidth/2 + i * doorWidth/3;
            g2d.drawLine(x, doorTopY, x, doorTopY + doorHeight - 20);
        }

        // ========== РУЧКА ДВЕРИ В ВИДЕ КРИСТАЛЛА ==========
        int[] handleX = {
                centerX + doorWidth/3,        // Правая точка
                centerX + doorWidth/3 + 5,    // Нижняя правая
                centerX + doorWidth/3,        // Нижняя точка
                centerX + doorWidth/3 - 5     // Нижняя левая
        };
        int[] handleY = {
                doorTopY + doorHeight/2,      // Центр по высоте
                doorTopY + doorHeight/2 + 5,  // Немного ниже
                doorTopY + doorHeight/2 + 10, // Самая нижняя
                doorTopY + doorHeight/2 + 5   // Симметрично
        };
        g2d.setColor(new Color(200, 230, 255)); // Светлый для ручки
        g2d.fillPolygon(handleX, handleY, 4);
    }

    // ========== МЕТОД РИСОВАНИЯ АУРЫ ХОЛОДА ==========
    private void drawColdAura(Graphics2D g2d, int centerX, int centerY, int radius) {
        // Основная аура (полупрозрачный голубой круг)
        g2d.setColor(new Color(100, 180, 255, 30)); // Очень прозрачный
        g2d.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        // Волны холода (концентрические круги)
        g2d.setColor(new Color(150, 200, 255, 50)); // Чуть менее прозрачный
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));

        // Рисуем 3 волны с увеличивающимся радиусом
        for (int i = 1; i <= 3; i++) {
            int waveRadius = radius + i * 10; // Каждая следующая волна больше
            g2d.drawOval(centerX - waveRadius, centerY - waveRadius,
                    waveRadius * 2, waveRadius * 2);
        }
    }


    // ========== МЕТОД РИСОВАНИЯ МАЛЕНЬКОЙ СНЕЖИНКИ ==========
    private void drawSmallSnowflake(Graphics2D g2d, int x, int y) {
        int size = 8;

        // Центральный круг снежинки
        g2d.fillOval(x - size/2, y - size/2, size, size);

        // Лучи снежинки
        g2d.setStroke(new BasicStroke(1));
        for (int i = 0; i < 6; i++) {
            double angle = Math.PI / 3 * i;
            int endX = (int)(x + size * Math.cos(angle));
            int endY = (int)(y + size * Math.sin(angle));
            g2d.drawLine(x, y, endX, endY);
        }
    }

