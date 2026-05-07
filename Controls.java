import javax.swing.*;
import java.awt.*;

public class Controls extends JPanel {
    private Engine engine;
    private CurrencyManager currency;

    public Controls(Engine game) {
        this.engine = game;
        this.currency = CurrencyManager.getInstance();
        setLayout(new FlowLayout());

        // Кнопка BaseUnit - 3 у.к.
        add(new Button("Spawn BaseUnit", new BaseUnit(), () -> {
            if (currency.spend(3)) {
                BaseUnit baseUnit = new BaseUnit();
                baseUnit.setFraction(0);
                baseUnit.setX(100);
                baseUnit.setY(game.getScreenHeight() - 150);
                baseUnit.setSize(90);
                baseUnit.setEngine(game);
                game.spawnObject(baseUnit);
            } else {
                System.out.println("Не хватает валюты для BaseUnit! Нужно 3 у.к.");
            }
        }));

        // Кнопка Archer - 5 у.к.
        add(new Button("Spawn Archer", new UnitArcher(), () -> {
            if (currency.spend(5)) {
                UnitArcher unitArcher = new UnitArcher();
                unitArcher.setFraction(0);
                unitArcher.setX(0);
                unitArcher.setAttackRange(250);
                unitArcher.setSize(50);
                unitArcher.setY(game.getScreenHeight() - 250);
                unitArcher.setSpeed(1);
                unitArcher.setDirection(1);
                game.spawnObject(unitArcher);
            } else {
                System.out.println("Не хватает валюты! Нужно 5 у.к.");
            }
        }));

        // Кнопка Tank - 7 у.к.
        add(new Button("Spawn Tank", new UnitDinoRider(), () -> {
            if (currency.spend(7)) {
                UnitDinoRider unitDinoRider = new UnitDinoRider();
                unitDinoRider.setFraction(0);
                unitDinoRider.setX(0);
                unitDinoRider.setY(470);
                unitDinoRider.setSize(100);
                unitDinoRider.setSpeed(1);
                unitDinoRider.setDirection(1);
                game.spawnObject(unitDinoRider);
            } else {
                System.out.println("Не хватает валюты! Нужно 7 у.к.");
            }
        }));
    }
}