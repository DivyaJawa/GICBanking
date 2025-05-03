package gic;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class TransactionHandler {
    private Bank bank;
    private Scanner scanner;

    public TransactionHandler(Bank bank, Scanner scanner) {
        this.bank = bank;
        this.scanner = scanner;
    }

    public void handle() {
        while (true) {
            System.out.println("Please enter transaction details in <Date> <Account> <Type> <Amount> format \n(or enter blank to go back to main menu):");
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) break;

            handleTransactionInput(input);
        }
    }

    public void handleTransactionInput(String input) {
        String[] parts = input.split("\\s+");
        if (parts.length != 4) {
            System.out.println("Invalid input format.");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(parts[0], java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
            String accountId = parts[1];
            String type = parts[2];
            double amount = Double.parseDouble(parts[3]);

            Account account = bank.getOrCreateAccount(accountId);
            if (!account.addTransaction(date, type, amount)) {
                System.out.println("Transaction failed due to constraints.");
                return;
            }

            System.out.println("Account: " + account.getAccountId());
            System.out.println("| Date     | Txn Id      | Type | Amount |");
            for (Transaction t : account.getTransactions()) {
                System.out.printf("| %s | %s | %s    | %6.2f |%n",
                        t.getDate(), t.getTxnId(), t.getType(), t.getAmount());
            }

        } catch (DateTimeParseException | NumberFormatException e) {
            System.out.println("Invalid date or amount format.");
        }
    }
}