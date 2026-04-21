import javax.swing.*;
import java.awt.*;

public class Controls extends JPanel {

    private static final int BASE_UNIT_COST = 3;
    private static final int ARCHER_COST = 5;
    private static final int TANK_COST = 7;

    private static final int PLAYER_TOWER_OFFSET_X = -200;
    private static final int PLAYER_SPAWN_OFFSET_X = 80;

    private static final int BASE_UNIT_SPAWN_Y_OFFSET = 150;
    private static final int RANGED_UNIT_SPAWN_Y_OFFSET = 250;

    private static final int BASE_UNIT_SIZE = 90;
    private static final int ARCHER_SIZE = 50;
    private static final int TANK_SIZE = 100;

    private static final int PLAYER_FRACTION = 2;
    private static final int PLAYER_DIRECTION = 1;
    private static final int ARCHER_ATTACK_RANGE = 250;

    private final CurrencyManager currency;

    public Controls(Engine game) {
        this.currency = CurrencyManager.getInstance();

        setLayout(new FlowLayout());

        int playerTowerX = game.getScreenWidth() / 2 + PLAYER_TOWER_OFFSET_X;
        int playerSpawnX = playerTowerX + PLAYER_SPAWN_OFFSET_X;

        add(new Button("Spawn BaseUnit", new BaseUnit(), () -> {
            if (currency.spend(BASE_UNIT_COST)) {
                BaseUnit baseUnit = new BaseUnit();
                baseUnit.setFraction(PLAYER_FRACTION);
                baseUnit.setX(playerSpawnX);
                baseUnit.setY(game.getScreenHeight() - BASE_UNIT_SPAWN_Y_OFFSET);
                baseUnit.setSize(BASE_UNIT_SIZE);
                baseUnit.setSpeed(BaseUnit.DEFAULT_SPEED);
                baseUnit.setAttackRange(BaseUnit.DEFAULT_ATTACK_RANGE);
                baseUnit.setDirection(PLAYER_DIRECTION);
                baseUnit.setEngine(game);
                game.spawnObject(baseUnit);
            } else {
                printNotEnoughCurrency(BASE_UNIT_COST);
            }
        }));

        add(new Button("Spawn Archer", new UnitArcher(), () -> {
            if (currency.spend(ARCHER_COST)) {
                UnitArcher unitArcher = new UnitArcher();
                unitArcher.setFraction(PLAYER_FRACTION);
                unitArcher.setX(playerSpawnX);
                unitArcher.setAttackRange(ARCHER_ATTACK_RANGE);
                unitArcher.setSize(ARCHER_SIZE);
                unitArcher.setY(game.getScreenHeight() - RANGED_UNIT_SPAWN_Y_OFFSET);
                unitArcher.setDirection(PLAYER_DIRECTION);
                game.spawnObject(unitArcher);
            } else {
                printNotEnoughCurrency(ARCHER_COST);
            }
        }));

        add(new Button("Spawn Tank", new UnitDinoRider(), () -> {
            if (currency.spend(TANK_COST)) {
                UnitDinoRider unitDinoRider = new UnitDinoRider();
                unitDinoRider.setFraction(PLAYER_FRACTION);
                unitDinoRider.setX(playerSpawnX);
                unitDinoRider.setY(game.getScreenHeight() - RANGED_UNIT_SPAWN_Y_OFFSET);
                unitDinoRider.setSize(TANK_SIZE);
                unitDinoRider.setDirection(PLAYER_DIRECTION);
                game.spawnObject(unitDinoRider);
            } else {
                printNotEnoughCurrency(TANK_COST);
            }
        }));
    }

    private void printNotEnoughCurrency(int requiredAmount) {
        System.out.println("Недостаточно валюты! Нужно " + requiredAmount + " у.е.");
    }
}
