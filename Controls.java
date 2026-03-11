import javax.swing.*;
import java.awt.*;

public class Controls extends JPanel {
    public Controls(Engine game) {
        setLayout(new FlowLayout());

        add(new Button("Spawn Base", new GameObject() /* under construction */, () -> {
            System.out.println("spawn Base");
            // under construction
        }));

        add(new Button("Spawn Archer", new UnitArcher() , () -> {
            // under construction
        }));

        // тип с дубинкой
        add(new Button("Spawn Хулиганчик", new Хулиган(), () -> {
            game.spawnObject(new Хулиган());
        }));

        add(new Button("Spawn Tank", new UnitDinoRider(), () -> {
            System.out.println("spawn Tank");
            // under construction
            game.spawnObject(new Arrow(10, 100, -40, 400));
        }));
    }
}