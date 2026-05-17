package view;

import engine.Button;
import engine.CurrencyManager;
import engine.Engine;
import engine.MyIcon;
import view.unit.BaseUnit;
import view.unit.UnitArcher;
import view.unit.UnitDinoRider;

import javax.swing.*;
import java.awt.*;

public class Controls extends JPanel {
    private Engine engine;
    private CurrencyManager currency;

    public Controls(Engine game) {
        this.engine = game;
        this.currency = CurrencyManager.getInstance();
        setLayout(new FlowLayout());

        // ← ИСПРАВЛЕНО: спавн С ЛЕВОЙ СТОРОНЫ ЭКРАНА
        int spawnX = 50; // ← Левая сторона экрана (было towerX - 250)
        int spawnY = 420; // Y координата дороги
        int fractionFriend = 2;

        // Кнопка BaseUnit - 3 у.к.
        add(new Button("Spawn BaseUnit", new MyIcon(BaseUnit.builder().notVisibleHeathBar().build()), (Runnable) () -> {
            if (currency.spend(3)) {
                BaseUnit baseUnit = (BaseUnit) BaseUnit.builder()
                        .fraction(fractionFriend)
                        .speed((int) 80f)
                        .attackRange(130)
                        .x(spawnX)
                        .y(spawnY)
                        .size(90)
                        .build();
                baseUnit.setEngine(engine);
                engine.spawnObject(baseUnit);
                System.out.println("🎮 BaseUnit спавнен в X: " + spawnX + ", Y: " + spawnY);
            } else {
                System.out.println("Не хватает валюты для BaseUnit! Нужно 3 у.к.");
            }
        }));

        // Кнопка Archer - 5 у.к.
        add(new Button("Spawn Archer", new MyIcon(UnitArcher.builder().notVisibleHeathBar().build()), (Runnable) () -> {
            if (currency.spend(5)) {
                UnitArcher unitArcher = (UnitArcher) UnitArcher.builder()
                        .fraction(fractionFriend)
                        .speed((int) 80f)
                        .attackRange(250)
                        .x(spawnX)
                        .y(spawnY)
                        .size(50)
                        .build();
                unitArcher.setEngine(engine);
                unitArcher.setDirection(1);
                engine.spawnObject(unitArcher);
            } else {
                System.out.println("Не хватает валюты! Нужно 5 у.к.");
            }
        }));

        // Кнопка Tank - 7 у.к.
        add(new Button("Spawn Tank", new MyIcon(UnitDinoRider.builder().notVisibleHeathBar().build()), (Runnable) () -> {
            if (currency.spend(7)) {
                UnitDinoRider unitDinoRider = (UnitDinoRider) UnitDinoRider.builder()
                        .fraction(fractionFriend)
                        .speed((int) 60f)
                        .attackRange(130)
                        .x(spawnX)
                        .y(spawnY + 30)
                        .size(80)
                        .build();
                unitDinoRider.setEngine(engine);
                unitDinoRider.setDirection(1);
                engine.spawnObject(unitDinoRider);
            } else {
                System.out.println("Не хватает валюты! Нужно 7 у.к.");
            }
        }));
    }
}