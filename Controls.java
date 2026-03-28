import javax.swing.*;
import java.awt.*;

public class Controls extends JPanel {
    private GameObject towerTarget;
    public Controls(Engine engine) {
        setLayout(new FlowLayout());
        for (GameObject obj : engine.getObjects()) {
            if (obj.getClass().getSimpleName().equals("Tower")) {
                towerTarget = obj;
                break;
            }
        }

        add(new Button("Spawn Base", new GameObject() /* under construction */, () -> {
            System.out.println("spawn Base");
            // under construction
        }));

        add(new Button("Spawn Archer", new UnitArcher(), () -> { // Добавил
            float stopX = 1400; // Добавил
            float stopY = 800; // Добавил
            UnitArcher archer = new UnitArcher(1, 50, 800, 50, 5);
            archer.setFraction(1);
            archer.setAttackRange(100);
            archer.setAttackDamage(0);
            archer.setAttackCooldown(9999.0f);
            GameObject stopTarget = new GameObject(2, stopX, stopY, 10, 0); //Добавил
            stopTarget.setFraction(0); // Добавил
            stopTarget.setAlive(true); // Добавил
            engine.spawnObject(archer);
            engine.spawnObject(stopTarget); // Добавил
            System.out.println("Archer spawned - will stop and stand");
        }));

        add(new Button("Spawn Tank ", new UnitDinoRider(), () -> {
            UnitDinoRider dino = new UnitDinoRider(1, 50, 850, 50, 5);
            dino.setFraction(1);
            dino.setAttackRange(300);
            dino.setAttackDamage(10);
            dino.setAttackCooldown(1000.0f);
            if (towerTarget != null) {
                dino.setTarget(towerTarget);
            }
            engine.spawnObject(dino);
            System.out.println("dino spawned - will attack tower ");
        }));
    }
}