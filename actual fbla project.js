const readline = require("readline");

class FinanceManager {
    constructor() {
        this.transactions = [];
    }

    addTransaction(type, amount, category, date) {
        if (amount <= 0) {
            console.log("Amount must be positive.");
            return;
        }
        this.transactions.push({ id: Date.now(), type, amount, category, date: new Date(date) });
        console.log("Transaction added successfully.");
    }

    deleteTransaction(id) {
        const index = this.transactions.findIndex(txn => txn.id === id);
        if (index !== -1) {
            this.transactions.splice(index, 1);
            console.log("Transaction deleted.");
        } else {
            console.log("Transaction not found.");
        }
    }

    updateTransaction(id, newAmount, newCategory, newDate) {
        const txn = this.transactions.find(txn => txn.id === id);
        if (txn) {
            txn.amount = newAmount;
            txn.category = newCategory;
            txn.date = new Date(newDate);
            console.log("Transaction updated.");
        } else {
            console.log("Transaction not found.");
        }
    }

    getBalance() {
        let income = this.transactions.filter(txn => txn.type === 'income').reduce((sum, txn) => sum + txn.amount, 0);
        let expenses = this.transactions.filter(txn => txn.type === 'expense').reduce((sum, txn) => sum + txn.amount, 0);
        return income - expenses;
    }

    getSummary(startDate, endDate) {
        let filtered = this.transactions.filter(txn => txn.date >= new Date(startDate) && txn.date <= new Date(endDate));
        let income = filtered.filter(txn => txn.type === 'income').reduce((sum, txn) => sum + txn.amount, 0);
        let expenses = filtered.filter(txn => txn.type === 'expense').reduce((sum, txn) => sum + txn.amount, 0);

        return { income, expenses, balance: income - expenses };
    }

    getCategorizedExpenses() {
        let categories = {};
        this.transactions.filter(txn => txn.type === 'expense').forEach(txn => {
            if (!categories[txn.category]) {
                categories[txn.category] = 0;
            }
            categories[txn.category] += txn.amount;
        });
        return categories;
    }
}

const financeManager = new FinanceManager();
const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

function showMenu() {
    console.log("\n==== Personal Finance Manager ====");
    console.log("1. Add Transaction");
    console.log("2. View Balance");
    console.log("3. View Summary");
    console.log("4. View Categorized Expenses");
    console.log("5. Delete Transaction");
    console.log("6. Exit");
    rl.question("Choose an option: ", handleMenu);
}

function handleMenu(option) {
    switch (option) {
        case "1":
            rl.question("Enter type (income/expense): ", type => {
                rl.question("Enter amount: ", amount => {
                    rl.question("Enter category: ", category => {
                        rl.question("Enter date (YYYY-MM-DD): ", date => {
                            financeManager.addTransaction(type, parseFloat(amount), category, date);
                            showMenu();
                        });
                    });
                });
            });
            break;
        case "2":
            console.log("Current Balance: $" + financeManager.getBalance().toFixed(2));
            showMenu();
            break;
        case "3":
            rl.question("Enter start date (YYYY-MM-DD): ", startDate => {
                rl.question("Enter end date (YYYY-MM-DD): ", endDate => {
                    console.log("Summary:", financeManager.getSummary(startDate, endDate));
                    showMenu();
                });
            });
            break;
        case "4":
            console.log("Categorized Expenses:", financeManager.getCategorizedExpenses());
            showMenu();
            break;
        case "5":
            rl.question("Enter transaction ID to delete: ", id => {
                financeManager.deleteTransaction(parseInt(id));
                showMenu();
            });
            break;
        case "6":
            console.log("Exiting program...");
            rl.close();
            break;
        default:
            console.log("Invalid option. Try again.");
            showMenu();
    }
}

showMenu();
