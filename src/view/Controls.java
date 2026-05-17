package src.view;

import src.engine.Button;
import src.engine.CurrencyManager;
import src.engine.Engine;
import src.engine.MyIcon;
import src.view.unit.BaseUnit;
import src.view.unit.UnitArcher;
import src.view.unit.UnitDinoRider;
import src.view.unit.UnitFireArcher;

import javax.swing.*;
import java.awt.*;

public class Controls extends JPanel {
    private final CurrencyManager currency;

    public Controls(Engine game) {
        this.currency = CurrencyManager.getInstance();
        setLayout(new FlowLayout());

        int playerTowerX = game.getScreenWidth() / 2 - 200;
        int playerSpawnX = playerTowerX + 80;
        int fractionFriend = 2;

        add(new Button("Spawn BaseUnit", new MyIcon(BaseUnit.builder().notVisibleHeathBar().build()), () -> {
            if (currency.spend(3)) {
                BaseUnit baseUnit = (BaseUnit) BaseUnit.builder()
                        .fraction(fractionFriend)
                        .speed(1)
                        .health(100)
                        .attackDamage(20)
                        .attackRange(130)
                        .attackCooldown(1.0f)
                        .x(playerSpawnX)
                        .y(game.getScreenHeight() - 150)
                        .size(90)
                        .build();
                game.spawnObject(baseUnit);
            }
        }));

        add(new Button("Spawn Archer", new MyIcon(UnitArcher.builder().notVisibleHeathBar().build()), () -> {
            if (currency.spend(5)) {
                UnitArcher unitArcher = (UnitArcher) UnitArcher.builder()
                        .fraction(fractionFriend)
                        .speed(1)
                        .health(100)
                        .attackDamage(15)
                        .attackRange(250)
                        .attackCooldown(2.0f)
                        .x(playerSpawnX)
                        .y(game.getScreenHeight() - 250)
                        .size(50)
                        .build();
                game.spawnObject(unitArcher);
            } else {
                System.out.println("Not enough currency. Need 5 u.c.");
            }
        }));

        add(new Button("Spawn Fire Archer", new MyIcon(UnitFireArcher.builder().notVisibleHeathBar().build()), () -> {
            if (currency.spend(6)) {
                UnitFireArcher unitFireArcher = (UnitFireArcher) UnitFireArcher.builder()
                        .fraction(fractionFriend)
                        .speed(1)
                        .health(100)
                        .attackDamage(15)
                        .attackRange(250)
                        .attackCooldown(2.0f)
                        .x(playerSpawnX)
                        .y(game.getScreenHeight() - 250)
                        .size(50)
                        .build();
                game.spawnObject(unitFireArcher);
            } else {
                System.out.println("Not enough currency. Need 6 u.c.");
            }
        }));

        add(new Button("Spawn Tank", new MyIcon(UnitDinoRider.builder().notVisibleHeathBar().build()), () -> {
            if (currency.spend(7)) {
                UnitDinoRider unitDinoRider = (UnitDinoRider) UnitDinoRider.builder()
                        .fraction(fractionFriend)
                        .speed(1)
                        .health(300)
                        .attackDamage(30)
                        .attackRange(130)
                        .attackCooldown(1.0f)
                        .x(playerSpawnX)
                        .y(game.getScreenHeight() - 230)
                        .size(80)
                        .build();
                game.spawnObject(unitDinoRider);
            } else {
                System.out.println("Not enough currency. Need 7 u.c.");
            }
        }));

    }
}
