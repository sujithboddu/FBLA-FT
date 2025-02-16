import java.text.SimpleDateFormat;
import java.util.*;

class Transaction {
    long id;
    String type;
    double amount;
    String category;
    Date date;

    public Transaction(long id, String type, double amount, String category, Date date) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }
}

public class FinanceManager {
    private List<Transaction> transactions;
    private Scanner scanner;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public FinanceManager() {
        this.transactions = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    // Add a new transaction
    public void addTransaction(String type) {
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        if (amount <= 0) {
            System.out.println("Amount must be positive.");
            return;
        }

        System.out.print("Enter category: ");
        String category = scanner.nextLine();

        System.out.print("Enter date (YYYY-MM-DD): ");
        String dateString = scanner.nextLine();

        try {
            Date date = dateFormat.parse(dateString);
            transactions.add(new Transaction(System.currentTimeMillis(), type, amount, category, date));
            System.out.println("Transaction added successfully.\n");
        } catch (Exception e) {
            System.out.println("Invalid date format. Use YYYY-MM-DD.");
        }
    }

    // Delete a transaction by ID
    public void deleteTransaction() {
        System.out.print("Enter transaction ID to delete: ");
        long id = scanner.nextLong();
        scanner.nextLine(); // Consume newline

        boolean removed = transactions.removeIf(txn -> txn.id == id);
        if (removed) {
            System.out.println("Transaction deleted successfully.\n");
        } else {
            System.out.println("Transaction not found.\n");
        }
    }

    // Get current balance
    public double getBalance() {
        double income = transactions.stream().filter(txn -> txn.type.equals("income")).mapToDouble(txn -> txn.amount).sum();
        double expenses = transactions.stream().filter(txn -> txn.type.equals("expense")).mapToDouble(txn -> txn.amount).sum();
        return income - expenses;
    }

    // Display financial summary
    public void getSummary() {
        double income = transactions.stream().filter(txn -> txn.type.equals("income")).mapToDouble(txn -> txn.amount).sum();
        double expenses = transactions.stream().filter(txn -> txn.type.equals("expense")).mapToDouble(txn -> txn.amount).sum();

        System.out.println("\n--- Financial Summary ---");
        System.out.println("Total Income: $" + income);
        System.out.println("Total Expenses: $" + expenses);
        System.out.println("Current Balance: $" + (income - expenses) + "\n");
    }

    // Categorized expenses
    public void getCategorizedExpenses() {
        Map<String, Double> categoryMap = new HashMap<>();
        for (Transaction txn : transactions) {
            if (txn.type.equals("expense")) {
                categoryMap.put(txn.category, categoryMap.getOrDefault(txn.category, 0.0) + txn.amount);
            }
        }

        System.out.println("\n--- Categorized Expenses ---");
        for (Map.Entry<String, Double> entry : categoryMap.entrySet()) {
            System.out.println(entry.getKey() + ": $" + entry.getValue());
        }
        System.out.println("");
    }

    // Main menu
    public void showMenu() {
        while (true) {
            System.out.println("\n--- Personal Finance Manager ---");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Balance");
            System.out.println("4. View Summary");
            System.out.println("5. View Categorized Expenses");
            System.out.println("6. Delete Transaction");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addTransaction("income");
                    break;
                case 2:
                    addTransaction("expense");
                    break;
                case 3:
                    System.out.println("\nCurrent Balance: $" + getBalance() + "\n");
                    break;
                case 4:
                    getSummary();
                    break;
                case 5:
                    getCategorizedExpenses();
                    break;
                case 6:
                    deleteTransaction();
                    break;
                case 7:
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

    public static void main(String[] args) {
        FinanceManager fm = new FinanceManager();
        fm.showMenu();
    }
}
