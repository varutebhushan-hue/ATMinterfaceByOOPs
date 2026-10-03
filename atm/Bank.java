import java.util.HashMap;
import java.util.Map;

/** Holds all accounts, authenticates users and executes transfers. */
public class Bank {
    public enum TransferResult { SUCCESS, INSUFFICIENT_FUNDS, RECIPIENT_NOT_FOUND, SAME_ACCOUNT, INVALID_AMOUNT }

    private final Map<String, Account> accounts = new HashMap<>();

    public void addAccount(Account account) { accounts.put(account.getAccountId(), account); }

    public Account findAccount(String id) { return accounts.get(id); }

    /** Returns the Account if ID and PIN match, otherwise null. */
    public Account authenticate(String id, String pin) {
        Account acc = accounts.get(id);
        return (acc != null && acc.checkPin(pin)) ? acc : null;
    }

    public TransferResult transfer(Account from, String toId, double amount) {
        if (amount <= 0) return TransferResult.INVALID_AMOUNT;
        Account to = accounts.get(toId);
        if (to == null) return TransferResult.RECIPIENT_NOT_FOUND;
        if (to == from) return TransferResult.SAME_ACCOUNT;
        if (!from.hasSufficientFunds(amount)) return TransferResult.INSUFFICIENT_FUNDS;
        from.debitForTransfer(amount, toId);
        to.creditFromTransfer(amount, from.getAccountId());
        return TransferResult.SUCCESS;
    }
}
