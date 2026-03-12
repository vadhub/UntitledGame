import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel {

    private final Background background;

    public GamePanel() {
        // Инициализируем фон с цветами по умолчанию
        this.background = new Background();

        // Настраиваем панель
        this.setPreferredSize(new Dimension(400, 400));
        this.setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Отрисовываем фон, передавая размеры панели
        background.draw(g, getWidth(), getHeight());
    }

    // Метод для обновления (если нужна анимация облаков)
    public void updateBackground() {
        background.update();
        repaint();
    }
}