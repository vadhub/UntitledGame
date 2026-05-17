package src.view;

import src.engine.Button;
import src.engine.CurrencyManager;
import src.engine.Engine;
import src.engine.MyIcon;
import src.view.unit.BaseUnit;
import src.view.unit.UnitArcher;
import src.view.unit.UnitDinoRider;

import javax.swing.*;
import java.awt.*;

public class Controls extends JPanel {
    private CurrencyManager currency;

    public Controls(Engine game) {
        this.currency = CurrencyManager.getInstance();
        setLayout(new FlowLayout());
        int playerTowerX = game.getScreenWidth() / 2 - 200;
        int playerSpawnX = playerTowerX + 80;
        int fractionFriend = 2;

        // Кнопка BaseUnit - 3 у.к.
        add(new src.engine.Button("Spawn BaseUnit", new MyIcon(BaseUnit.builder().notVisibleHeathBar().build()), () -> {
            if (currency.spend(3)) {
                BaseUnit baseUnit = (BaseUnit) BaseUnit.builder()
                        .fraction(fractionFriend)
                        .speed(Engine.BASE_UNIT_SPEED)
                        .attackRange(130)
                        .x(playerSpawnX)
                        .y(game.getScreenHeight() - 150)
                        .size(90)
                        .build();
                game.spawnObject(baseUnit);
            }
        }));

        // Кнопка Archer - 5 у.к.
        add(new src.engine.Button("Spawn Archer", new MyIcon(UnitArcher.builder().notVisibleHeathBar().build()), () -> {
            if (currency.spend(5)) {
                UnitArcher unitArcher = (UnitArcher) UnitArcher.builder()
                        .fraction(fractionFriend)
                        .speed(Engine.ARCHER_SPEED)
                        .attackRange(250)
                        .x(playerSpawnX)
                        .y(game.getScreenHeight() - 250)
                        .size(50)
                        .build();

                game.spawnObject(unitArcher);
            } else {
                System.out.println(" Не хватает валюты! Нужно 5 у.к.");
            }
        }));

        // Кнопка Tank - 7 у.к.
        add(new Button("Spawn Tank", new MyIcon(UnitDinoRider.builder().notVisibleHeathBar().build()), () -> {
            if (currency.spend(7)) {
                UnitDinoRider unitDinoRider = (UnitDinoRider) UnitDinoRider.builder()
                        .fraction(fractionFriend)
                        .speed(Engine.TANK_SPEED)
                        .attackRange(130)
                        .x(playerSpawnX)
                        .y(game.getScreenHeight() - 230)
                        .size(80)
                        .build();;
                game.spawnObject(unitDinoRider);
            } else {
                System.out.println(" Не хватает валюты! Нужно 7 у.к.");
            }
        }));
    }
}
