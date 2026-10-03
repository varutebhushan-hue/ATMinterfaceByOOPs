import java.util.List;
import java.util.Scanner;

/** Console user interface: login + menu loop. */
public class ATM {
    private static final int MAX_ATTEMPTS = 3;
    private final Bank bank;
    private final Scanner in = new Scanner(System.in);

    public ATM(Bank bank) { this.bank = bank; }

    public void start() {
        System.out.println("=====================================");
        System.out.println("        WELCOME TO JAVA ATM");
        System.out.println("=====================================");
        Account user = login();
        if (user == null) {
            System.out.println("Too many incorrect attempts. Access denied.");
            return;
        }
        System.out.println("\nLogin successful. Hello, " + user.getHolderName() + "!");
        menuLoop(user);
    }

    private Account login() {
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            System.out.print("Enter User ID: ");
            String id = in.nextLine().trim();
            System.out.print("Enter PIN: ");
            String pin = in.nextLine().trim();
            Account acc = bank.authenticate(id, pin);
            if (acc != null) return acc;
            int left = MAX_ATTEMPTS - attempt;
            System.out.println("Invalid User ID or PIN. " + (left > 0 ? left + " attempt(s) left.\n" : ""));
        }
        return null;
    }

    private void menuLoop(Account user) {
        boolean running = true;
        while (running) {
            System.out.println("\n------------- MAIN MENU -------------");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.print("Choose an option: ");
            String choice = in.nextLine().trim();
            switch (choice) {
                case "1": showHistory(user); break;
                case "2": withdraw(user); break;
                case "3": deposit(user); break;
                case "4": transfer(user); break;
                case "5":
                    System.out.println("Thank you for using Java ATM. Goodbye!");
                    running = false;
                    break;
                default: System.out.println("Invalid option. Please enter 1-5.");
            }
        }
    }

    private void showHistory(Account user) {
        List<Transaction> list = user.getHistory();
        System.out.println("\n--------- TRANSACTION HISTORY ---------");
        if (list.isEmpty()) {
            System.out.println("No transactions in this session.");
        } else {
            for (int i = 0; i < list.size(); i++) {
                System.out.println((i + 1) + ". " + list.get(i));
            }
        }
        System.out.printf("Current balance: %.2f%n", user.getBalance());
    }

    private void withdraw(Account user) {
        double amount = readAmount("Enter amount to withdraw: ");
        if (amount <= 0) return;
        if (!user.hasSufficientFunds(amount)) {
            System.out.println("Insufficient Funds");
            return;
        }
        user.withdraw(amount);
        System.out.printf("Withdrawal successful. New balance: %.2f%n", user.getBalance());
    }

    private void deposit(Account user) {
        double amount = readAmount("Enter amount to deposit: ");
        if (amount <= 0) return;
        user.deposit(amount);
        System.out.printf("Deposit successful. New balance: %.2f%n", user.getBalance());
    }

    private void transfer(Account user) {
        System.out.print("Enter recipient account ID: ");
        String toId = in.nextLine().trim();
        double amount = readAmount("Enter amount to transfer: ");
        if (amount <= 0) return;
        switch (bank.transfer(user, toId, amount)) {
            case SUCCESS:
                System.out.printf("Transferred %.2f to %s. New balance: %.2f%n", amount, toId, user.getBalance());
                break;
            case INSUFFICIENT_FUNDS: System.out.println("Insufficient Funds"); break;
            case RECIPIENT_NOT_FOUND: System.out.println("Recipient account not found."); break;
            case SAME_ACCOUNT: System.out.println("Cannot transfer to your own account."); break;
            default: System.out.println("Invalid amount.");
        }
    }

    /** Reads a positive amount; returns -1 on invalid input. */
    private double readAmount(String prompt) {
        System.out.print(prompt);
        try {
            double v = Double.parseDouble(in.nextLine().trim());
            if (v <= 0) { System.out.println("Amount must be greater than zero."); return -1; }
            return v;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            return -1;
        }
    }
}
