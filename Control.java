package org.vlg;

import javax.swing.*;
import java.awt.*;

public class Control extends JPanel {
    public Control() {
        setLayout(new FlowLayout());
        add(new Button("ok"));
        add(new Button("add"));

    }

}
