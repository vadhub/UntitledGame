package org.vlg;
import javax.swing.*;
import java.awt.*;
public class Button extends JButton {

    // Засыпкин - исправить
    public Button(String text /* Вытоптов - добавить событие */) {

        // Рябухин - добавить иконку
        setPreferredSize(new Dimension(60,60));
        super(text);
    }
}
