import java.util.Timer;
import java.util.TimerTask;

public class CurrencyManager {
    private static CurrencyManager instance;
    private int currency = 10;

    private CurrencyManager() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currency += 5;
                System.out.println(" +5 у.к. | Всего: " + currency);
            }
        }, 5000, 5000);
    }

    public static CurrencyManager getInstance() {
        if (instance == null) {
            instance = new CurrencyManager();
        }
        return instance;
    }

    public boolean spend(int amount) {
        if (currency >= amount) {
            currency -= amount;
            return true;
        }
        return false;
    }

    public int getCurrency() {
        return currency;
    }
}