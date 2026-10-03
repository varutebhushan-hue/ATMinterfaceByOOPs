public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        // Demo accounts: ID / PIN / name / opening balance
        bank.addAccount(new Account("user1", "1234", "Alice", 5000.00));
        bank.addAccount(new Account("user2", "4321", "Bob", 2500.00));
        bank.addAccount(new Account("user3", "1111", "Charlie", 100.00));

        new ATM(bank).start();
    }
}
