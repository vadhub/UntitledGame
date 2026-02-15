package org.example;
import java.awt.*;

public class GameObject {
    // Базовые поля (для обратной совместимости)
    protected int x, y;           // целочисленная позиция
    protected int width, height;  // размер

    // Расширенные поля для наследников (Grass, Tree)
    protected int id;
    protected float fx, fy;       // дробная позиция (для плавного движения)
    protected int size;
    protected float speed;
    protected Color color;

    // ===== Конструкторы =====

    // Старый конструктор (для обратной совместимости)
    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.fx = x;
        this.fy = y;
        this.size = Math.max(width, height);
        this.color = Color.WHITE;
        this.speed = 0f;
        this.id = -1;
    }

    // Новый конструктор для наследников
    public GameObject(int id, float x, float y, int size, float speed, Color color) {
        this.id = id;
        this.fx = x;
        this.fy = y;
        this.x = (int) x;
        this.y = (int) y;
        this.size = size;
        this.width = size;
        this.height = size;
        this.speed = speed;
        this.color = color;
    }

    // ===== Геттеры =====
    public int getX() { return x; }
    public int getY() { return y; }
    public float getFx() { return fx; }
    public float getFy() { return fy; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getId() { return id; }
    public int getSize() { return size; }
    public float getSpeed() { return speed; }
    public Color getColor() { return color; }

    // ===== Сеттеры =====
    public void setX(int x) { this.x = x; this.fx = x; }
    public void setY(int y) { this.y = y; this.fy = y; }
    public void setFx(float fx) { this.fx = fx; this.x = (int) fx; }
    public void setFy(float fy) { this.fy = fy; this.y = (int) fy; }
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public void setSize(int size) { this.size = size; this.width = size; this.height = size; }
    public void setSpeed(float speed) { this.speed = speed; }
    public void setColor(Color color) { this.color = color; }

    // ===== Вспомогательные методы =====
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Обновление позиции с учётом скорости
    public void update() {
        fx += speed;
        x = (int) fx;
    }

    // Основной метод отрисовки (совместимость с обоими подходами)
    public void render(Graphics g) {
        draw(g); // делегируем в draw() для совместимости
    }

    // Альтернативное имя метода (для наследников, которые используют draw)
    public void draw(Graphics g) {
        // базовая отрисовка — ничего не рисуем, переопределяется в подклассах
    }
}