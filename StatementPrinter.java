package gic;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;



public class StatementPrinter {
    private Bank bank;
    private InterestCalculator interestCalculator;
    private Scanner scanner;

    public StatementPrinter(Bank bank, InterestCalculator interestCalculator, Scanner scanner) {
        this.bank = bank;
        this.interestCalculator = interestCalculator;
        this.scanner = scanner;
    }

    public void handle() {
        while (true) {
            System.out.println("Please enter account and month to generate the statement <Account> <Year><Month>");
            System.out.println("(or enter blank to go back to main menu):");
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) break;

            String[] parts = input.split("\\s+");
            if (parts.length != 2 || parts[1].length() != 6) {
                System.out.println("Invalid input format.");
                continue;
            }

            String accountId = parts[0];
            String yearMonthStr = parts[1];
            try {
                YearMonth yearMonth = YearMonth.parse(yearMonthStr, DateTimeFormatter.ofPattern("yyyyMM"));
                Account account = bank.getAccount(accountId);

                if (account == null) {
                    System.out.println("Account not found.");
                    continue;
                }

               
                interestCalculator.applyInterestForMonth(account, yearMonth);

                printStatement(account, yearMonth);
            } catch (Exception e) {
                System.out.println("Invalid date format or other error: " + e.getMessage());
            }
        }
    }

    private void printStatement(Account account, YearMonth month) {
        System.out.println("Account: " + account.getAccountId());
        System.out.println("| Date     | Txn Id      | Type | Amount | Balance |");

        double runningBalance = 0.0;
        List<Transaction> transactions = account.getTransactions();

        for (Transaction txn : transactions) {
            LocalDate date = txn.getDate();
            if (YearMonth.from(date).equals(month)) {
                double amount = txn.getAmount();
                String type = txn.getType();
                if (type.equals("D") || type.equals("I")) {
                    runningBalance += amount;
                } else if (type.equals("W")) {
                    runningBalance -= amount;
                }

                System.out.printf("| %s | %-10s | %-4s | %6.2f | %7.2f |%n",
                        date.toString().replaceAll("-", ""),
                        txn.getTxnId(),
                        type,
                        amount,
                        runningBalance);
            }
        }
    }
}