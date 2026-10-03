import java.util.ArrayList;
import java.util.List;

/** Bank account with encapsulated state and its own transaction log. */
public class Account {
    private final String accountId;
    private String pin;
    private final String holderName;
    private double balance;
    private final ArrayList<Transaction> history = new ArrayList<>();

    public Account(String accountId, String pin, String holderName, double openingBalance) {
        this.accountId = accountId;
        this.pin = pin;
        this.holderName = holderName;
        this.balance = openingBalance;
    }

    public String getAccountId() { return accountId; }
    public String getHolderName() { return holderName; }
    public double getBalance() { return balance; }
    public List<Transaction> getHistory() { return new ArrayList<>(history); }

    public boolean checkPin(String input) { return pin.equals(input); }
    public void setPin(String newPin) { this.pin = newPin; }

    public boolean hasSufficientFunds(double amount) { return balance >= amount; }

    /** Returns false (and changes nothing) if funds are insufficient or amount invalid. */
    public boolean withdraw(double amount) {
        if (amount <= 0 || !hasSufficientFunds(amount)) return false;
        balance -= amount;
        history.add(new Transaction(Transaction.Type.WITHDRAW, amount, balance, "Cash withdrawal"));
        return true;
    }

    public boolean deposit(double amount) {
        if (amount <= 0) return false;
        balance += amount;
        history.add(new Transaction(Transaction.Type.DEPOSIT, amount, balance, "Cash deposit"));
        return true;
    }

    /** Package-private helpers used by Bank.transfer so both sides are logged. */
    void debitForTransfer(double amount, String toId) {
        balance -= amount;
        history.add(new Transaction(Transaction.Type.TRANSFER_OUT, amount, balance, "Transfer to " + toId));
    }

    void creditFromTransfer(double amount, String fromId) {
        balance += amount;
        history.add(new Transaction(Transaction.Type.TRANSFER_IN, amount, balance, "Transfer from " + fromId));
    }
}
