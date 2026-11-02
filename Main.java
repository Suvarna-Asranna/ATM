import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

class Main {
    static double balance = 5000;
    static double startingBalance = 5000;
    static double withdrawalTotal = 0;
    static double transferTotal = 0;
    static double transferFeesTotal = 0;
    static double depositTotal = 0;
    static int transactionCount = 0;
    static int savedTransactionCount = 0;
    static double dailyDepositLimit = 50000;
    static LocalDate depositLimitDate = LocalDate.now();
    static double dailyDepositTotal = 0;

    static ArrayList<String> transactions = new ArrayList<>();
    
    // Check Balance
    static void checkBalance() {
        System.out.println("Your balance is: ₹" + balance);
    }
    
    // Deposit Money
static void depositMoney(Scanner input) {

    resetDailyDepositLimit();

    System.out.print("Enter deposit amount: ");
    double amount = input.nextDouble();

    if (amount <= 0) {

        System.out.println("Invalid deposit amount.");

    } else {

        System.out.println("\n===== DEPOSIT DETAILS =====");
        System.out.println("Deposit Amount: ₹" + amount);

        System.out.print("Confirm deposit? (1 = Yes, 2 = No): ");
        int confirmation = input.nextInt();

        if (confirmation == 1) {

            balance = balance + amount;
            depositTotal = depositTotal + amount;
            transactionCount++;
            transactions.add(getCurrentDateTime() + " | Deposit: ₹" + amount);
            System.out.println("Deposit successful!");
            System.out.println("Current balance: ₹" + balance);

        } else if (confirmation == 2) {

            System.out.println("Deposit cancelled.");

        } else {

            System.out.println("Invalid confirmation.");
        }
    }

    if (amount <= 0) {
    System.out.println("Please enter a valid deposit amount.");
    return;
}

    if (depositTotal + amount > dailyDepositLimit) {
    System.out.println("Daily deposit limit exceeded.");
    System.out.println("Remaining deposit limit: ₹"+ (dailyDepositLimit - depositTotal));
    return;
}
}
    // Withdraw Money
    static void withdrawMoney(Scanner input) {

    System.out.print("Enter withdrawal amount: ");
    double amount = input.nextDouble();

    double dailyLimit = 10000;

    if (amount <= 0) {

        System.out.println("Invalid amount.");

    } else if (withdrawalTotal + amount > dailyLimit) {

        System.out.println("Withdrawal limit exceeded.");

        System.out.println(
            "You can withdraw only ₹" +
            (dailyLimit - withdrawalTotal) + " more."
        );

    } else if (amount <= balance) {

        balance = balance - amount;
        withdrawalTotal = withdrawalTotal + amount;
        transactionCount++;
        transactions.add(getCurrentDateTime() + " | Withdrawal: ₹" + amount);
        System.out.println("Please collect your money.");
        System.out.println("Current balance: ₹" + balance);

        // Check for low balance after withdrawal
        checkLowBalance();

    } else {

        System.out.println("Insufficient balance.");
    }
}
    // Transaction History
    static void showHistory() {

        System.out.println("\n===== Transaction History =====");

        if (transactions.size() == 0) {

            System.out.println("No transactions yet.");

        } else {

            for (String transaction : transactions) {
                System.out.println(transaction);
            }
        }
    }

    static void miniStatement() {

    System.out.println("\n===== MINI STATEMENT =====");

    if (transactions.size() == 0) {

        System.out.println("No transactions available.");

    } else {

        int start = Math.max(0, transactions.size() - 3);

        for (int i = start; i < transactions.size(); i++) {
            System.out.println(transactions.get(i));
        }

        System.out.println("--------------------------");
        System.out.println("Current Balance: ₹" + balance);
    }
}

    // Receipt
    static void printReceipt(String name, String accountNumber) {

        System.out.println("\n===== ATM RECEIPT =====");

        System.out.println("Account Holder: " + name);
        System.out.println("Account Number: " +maskAccountNumber(accountNumber));

        System.out.println("Starting Balance: ₹" + startingBalance);
        System.out.println("Current Balance: ₹" + balance);
        System.out.println("Total Transactions: " + transactionCount);        System.out.println("Transfer Fees Paid: ₹" + transferFeesTotal);

        System.out.println("Thank you for using the ATM!");
    }

    // Change PIN
    
static int changePin(Scanner input, int currentPin) {

    System.out.print("Enter current PIN: ");
    int oldPin = input.nextInt();
    input.nextLine();

    if (oldPin != currentPin) {
        System.out.println("Incorrect current PIN.");
        return currentPin;
    }

    System.out.print("Enter new PIN: ");
    int newPin = input.nextInt();
    input.nextLine();

    if (newPin < 1000 || newPin > 9999) {
        System.out.println("PIN must contain exactly 4 digits.");
        return currentPin;
    }

    System.out.print("Confirm new PIN: ");
    int confirmPin = input.nextInt();
    input.nextLine();

    if (newPin != confirmPin) {
        System.out.println("PINs do not match. PIN was not changed.");
        return currentPin;
    }

    System.out.println("PIN changed successfully.");
    return newPin;
}


    // Mask Account Number
    static String maskAccountNumber(String accountNumber) {

        if (accountNumber.length() <= 4) {
            return accountNumber;
        }

        String lastFour = accountNumber.substring(accountNumber.length() - 4);

        return "****" + lastFour;
    }

    static void transferMoney(Scanner input) {

    System.out.print("Enter receiver account number: ");
    String receiverAccount = input.next();

    System.out.print("Enter transfer amount: ");
    double amount = input.nextDouble();

    double transferLimit = 25000;
    double transferFee = 10;

    if (amount <= 0) {

        System.out.println("Invalid transfer amount.");

    } else if (transferTotal + amount > transferLimit) {

        System.out.println("Daily transfer limit exceeded.");

        System.out.println("You can transfer only ₹" +(transferLimit - transferTotal) +" more today.");

    } else if (amount + transferFee > balance) {

        System.out.println("Insufficient balance.");

    } else {

        String maskedAccount ="****" +receiverAccount.substring(Math.max(0, receiverAccount.length() - 4));

        System.out.println("\n===== TRANSFER DETAILS =====");
        System.out.println("Receiver Account: " + maskedAccount);
        System.out.println("Transfer Amount: ₹" + amount);
        System.out.println("Transfer Fee: ₹" + transferFee);
        System.out.println("Total Deduction: ₹" + (amount + transferFee));

        System.out.print( "Confirm transfer? (1 = Yes, 2 = No): ");

        int confirmation = input.nextInt();

        if (confirmation == 1) {

            balance = balance - (amount+transferFee);

            transferTotal = transferTotal + amount;
            transferFeesTotal = transferFeesTotal + transferFee;
            transactionCount++;
            transactions.add( getCurrentDateTime() + " | Transfer: ₹" + amount);            System.out.println("Transfer successful.");
            System.out.println("Current balance: ₹" + balance);

            checkLowBalance();


        } else if (confirmation == 2) {

            System.out.println("Transfer cancelled.");

        } else {

            System.out.println("Invalid confirmation.");
        }
    }
}
static void dailySummary() {

    System.out.println("\n===== DAILY SUMMARY =====");

    System.out.println("Starting Balance: ₹" + startingBalance);
    System.out.println("Total Deposited: ₹" + depositTotal);
    System.out.println("Total Withdrawn: ₹" + withdrawalTotal);
    System.out.println("Total Transferred: ₹" + transferTotal);
    System.out.println("Transfer Fees: ₹" + transferFeesTotal);
    System.out.println("Current Balance: ₹" + balance);
    System.out.println("Total Transactions: " + transactions.size());

    System.out.println("==========================");
}

static void checkLowBalance() {

    if (balance < 1000) {
        System.out.println("Low Balance Warning!");
        System.out.println("Your balance is below ₹1000.");
    }
}

static void welcomeScreen() {

    System.out.println("\n================================");
    System.out.println("      BANK ATM");
    System.out.println("================================");
    System.out.println("   Welcome to our ATM service!");
    System.out.println("   Safe Banking, Anytime");
    System.out.println("================================\n");
}

static void saveBalance() {

    try {
        FileWriter writer = new FileWriter("atm-data.txt");

        writer.write(String.valueOf(balance));

        writer.close();

        System.out.println("Balance saved successfully.");

    } catch (IOException e) {
        System.out.println("Error saving balance.");
    }
}

static void loadBalance() {

    try {
        BufferedReader reader =
            new BufferedReader(new FileReader("atm-data.txt"));

        String savedBalance = reader.readLine();

        if (savedBalance != null) {
            balance = Double.parseDouble(savedBalance);
        }

        reader.close();

        System.out.println("Saved balance loaded successfully.");

    } catch (IOException e) {
        System.out.println("No saved balance found. Starting with ₹5000.");
    } catch (NumberFormatException e) {
        System.out.println("Saved balance is invalid. Starting with ₹5000.");
        balance = 5000;
    }
}

static void saveTransactionHistory() {
    try {
        FileWriter writer =new FileWriter("atm-transactions.txt", true);

        for (int i = savedTransactionCount;i < transactions.size(); i++) {

            writer.write(transactions.get(i)+ System.lineSeparator());
        }

        savedTransactionCount = transactions.size();

        writer.close();

        System.out.println("New transactions saved successfully.");

    } catch (IOException e) {
        System.out.println("Error saving transaction history.");
    }
}

static void loadTransactionHistory() {
    try {
        BufferedReader reader =new BufferedReader(new FileReader("atm-transactions.txt") );

        String transaction;

        while ((transaction = reader.readLine()) != null) {
            transactions.add(transaction);
        }

        reader.close();
        savedTransactionCount = transactions.size();
        System.out.println("Transaction history loaded.");
        
    } catch (IOException e) {
        System.out.println("No previous transaction history found.");
    }
}

static void searchTransactions(Scanner input) {
    System.out.print("Enter keyword to search: ");
    String keyword = input.nextLine().trim();

    boolean found = false;

    System.out.println("\n--- Search Results ---");

    for (String transaction : transactions) {
        if (transaction.toLowerCase()
                .contains(keyword.toLowerCase())) {

            System.out.println(transaction);
            found = true;
        }
    }

    if (!found) {
        System.out.println("No matching transactions found.");
    }
}

static void filterTransactions(Scanner input) {
    System.out.println("\n--- Filter Transactions ---");
    System.out.println("1. Deposits");
    System.out.println("2. Withdrawals");
    System.out.println("3. Transfers");
    System.out.print("Choose filter: ");

    int filterChoice;

    try {
        filterChoice = Integer.parseInt(input.nextLine());
    } catch (NumberFormatException e) {
        System.out.println("Please enter a valid number.");
        return;
    }

    String keyword;

    switch (filterChoice) {
        case 1:
            keyword = "deposit";
            break;
        case 2:
            keyword = "withdraw";
            break;
        case 3:
            keyword = "transfer";
            break;
        default:
            System.out.println("Invalid filter choice.");
            return;
    }

    boolean found = false;

    System.out.println("\n--- Filter Results ---");

    for (String transaction : transactions) {
        if (transaction.toLowerCase().contains(keyword)) {
            System.out.println(transaction);
            found = true;
        }
    }

    if (!found) {
        System.out.println("No matching transactions found.");
    }
}

static void exportMiniStatement() {
    try {
        FileWriter writer =
            new FileWriter("mini-statement.txt");

        writer.write("====== ATM MINI STATEMENT ======\n");
        writer.write("Current Balance: ₹" + balance + "\n");
        writer.write("--------------------------------\n");

        int start = Math.max(0, transactions.size() - 5);

        for (int i = start; i < transactions.size(); i++) {
            writer.write(transactions.get(i)
                + System.lineSeparator());
        }

        writer.write("--------------------------------\n");
        writer.write("Thank you for using our ATM!\n");

        writer.close();

        System.out.println(
            "Mini statement exported to mini-statement.txt"
        );

    } catch (IOException e) {
        System.out.println("Error exporting mini statement.");
    }
}

static void generateReceipt(String name, String accountNumber) {
    String fileName = "atm-receipt.txt";

    try {
        FileWriter writer = new FileWriter(fileName);

        writer.write("========= ATM RECEIPT =========\n");
        writer.write("Account Holder: " + name + "\n");
        writer.write("Account Number: "+ maskAccountNumber(accountNumber) + "\n");
        writer.write("Date and Time: " + getCurrentDateTime() + "\n");
        writer.write("--------------------------------\n");
        writer.write("Current Balance: ₹" + balance + "\n");

        if (!transactions.isEmpty()) {
            writer.write("Latest Transaction:\n");
            writer.write(transactions.get(transactions.size() - 1)
                    + "\n");
        } else {
            writer.write("No transactions available.\n");
        }

        writer.write("--------------------------------\n");
        writer.write("Thank you for using our ATM!\n");

        writer.close();

        System.out.println("Receipt saved to " + fileName);

    } catch (IOException e) {
        System.out.println("Error generating receipt.");
    }
}

static String getCurrentDateTime() {
    LocalDateTime now = LocalDateTime.now();

    DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    return now.format(formatter);
}

static void resetDailyDepositLimit() {
    LocalDate today = LocalDate.now();

    if (!today.equals(depositLimitDate)) {
        depositLimitDate = today;
        dailyDepositTotal = 0;

        System.out.println("New day detected.");
        System.out.println("Daily deposit limit has been reset.");
    }
}

static void saveDailyDepositData() {
    try {
        FileWriter writer =
            new FileWriter("daily-deposit.txt");

        writer.write(depositLimitDate + "\n");
        writer.write(String.valueOf(dailyDepositTotal));

        writer.close();

    } catch (IOException e) {
        System.out.println("Error saving daily deposit data.");
    }
}

static void loadDailyDepositData() {
    try {
        BufferedReader reader =
            new BufferedReader(
                new FileReader("daily-deposit.txt")
            );

        String savedDate = reader.readLine();
        String savedTotal = reader.readLine();

        reader.close();

        if (savedDate != null && savedTotal != null) {
            depositLimitDate = LocalDate.parse(savedDate);
            dailyDepositTotal = Double.parseDouble(savedTotal);
        }

        resetDailyDepositLimit();

    } catch (IOException e) {
        depositLimitDate = LocalDate.now();
        dailyDepositTotal = 0;
    } catch (RuntimeException e) {
        System.out.println("Could not read saved deposit data.");
        depositLimitDate = LocalDate.now();
        dailyDepositTotal = 0;
    }
}

static void showAccountProfile(String name, String accountNumber) {
    System.out.println("\n======= ACCOUNT PROFILE =======");
    System.out.println("Account Holder: " + name);
    System.out.println("Account Number: "
            + maskAccountNumber(accountNumber));
    System.out.printf("Current Balance: ₹%.2f%n", balance);
    System.out.println("Account Status: Active");
    System.out.println("===============================");
}


static void accountActivityReport() {
    int deposits = 0;
    int withdrawals = 0;
    int transfers = 0;

    for (String transaction : transactions) {
        String record = transaction.toLowerCase();

        if (record.contains("deposit")) {
            deposits++;
        } else if (record.contains("withdraw")) {
            withdrawals++;
        } else if (record.contains("transfer")) {
            transfers++;
        }
    }

    System.out.println("\n===== ACCOUNT ACTIVITY REPORT =====");
    System.out.println("Total Deposit Transactions: " + deposits);
    System.out.println("Total Withdrawal Transactions: " + withdrawals);
    System.out.println("Total Transfer Transactions: " + transfers);
    System.out.println("Total Transactions: "+ (deposits + withdrawals + transfers));
    System.out.println("===================================");
}

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        loadBalance();
        loadTransactionHistory();

        welcomeScreen();

        loadDailyDepositData();

        System.out.print("Enter your name: ");
        String name = input.nextLine();

        System.out.print("Enter your account number: ");
        String accountNumber = input.nextLine();

        System.out.println("Welcome, " + name + "!");

        int correctPin = 1234;
        int attempts = 0;
        boolean pinCorrect = false;

        // PIN Login
        while (attempts < 3) {

            System.out.print("Enter your PIN: ");
            int pin = input.nextInt();

            if (pin == correctPin) {

                System.out.println("PIN accepted!");

                pinCorrect = true;

                System.out.println("\n========================");
                System.out.println("       ATM LOGIN");
                System.out.println("========================");
                System.out.println("Welcome, " + name);
                System.out.println(
                    "Account: " +
                    maskAccountNumber(accountNumber)
                );
                System.out.println("Login successful!");
                System.out.println("========================");

                break;

            } else {

                attempts++;

                System.out.println("Incorrect PIN!");

                if (attempts < 3) {

                    System.out.println(
                        "Attempts remaining: " +
                        (3 - attempts)
                    );
                }
            }
        }

        // PIN failed
        if (!pinCorrect) {

            System.out.println(
                "ATM blocked. Too many incorrect attempts."
            );

        } else {

            // ATM Menu
            int choice = 0;

            while (choice != 9) {

                System.out.println("\n===== ATM =====");
                System.out.println("Welcome, " + name + "!");
                System.out.println("1. Check Balance");
                System.out.println("2. Deposit Money");
                System.out.println("3. Withdraw Money");
                System.out.println("4. Transaction History");
                System.out.println("6. Transfer Money");
                System.out.println("7. Daily Summary");
                System.out.println("8. Filter Transactions");
                System.out.println("9. Search Transactions");
                System.out.println("10. Generate Receipt");
                System.out.println("11. Export Mini Statement");
                System.out.println("12. Change PIN");
                System.out.println("13. View Account Profile");
                System.out.println("14. Account Activity Report");
                System.out.println("15. Exit");

                System.out.print("Enter your choice: ");
                choice = input.nextInt();

                switch (choice) {

                    case 1:
                        checkBalance();
                        break;

                    case 2:
                        depositMoney(input);
                        break;

                    case 3:
                        withdrawMoney(input);
                        break;

                    case 4:
                        showHistory();
                        break;

                    case 5:
                        miniStatement();
                        break;

                    case 6:
                        transferMoney(input);
                        break;
                    
                    case 7:
                        searchTransactions(input);
                        break;

                    case 8:
                        filterTransactions(input);
                        break;

                    case 9:
                        exportMiniStatement();
                        break;
                    
                    case 10:
                        generateReceipt(name, accountNumber);
                        break;

                    case 11:
                        dailySummary();
                        break;

                    case 12:
                        correctPin = changePin(input, correctPin);
                        break;
                    case 13:
                        showAccountProfile(name, accountNumber);
                        break;

                    case 14:
                        accountActivityReport();
                        break;

                    case 15:
                        saveTransactionHistory();
                        saveBalance();
                        printReceipt(name, accountNumber);
                        System.out.println("\nLogging out...");
                        System.out.println("Thank you for using ATM!");
                        System.out.println("Have a nice day!");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                    }
                }
            }

        // Close Scanner only once, at the very end
        input.close();
    }
}