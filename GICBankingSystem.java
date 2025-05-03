package gic;
import java.util.Scanner;



public class GICBankingSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Bank bank = new Bank();
        TransactionHandler txnHandler = new TransactionHandler(bank, scanner);
        InterestRuleHandler interestHandler = new InterestRuleHandler(bank, scanner);
        InterestCalculator interestCalculator = new InterestCalculator(bank);
        StatementPrinter statementPrinter = new StatementPrinter(bank, interestCalculator, scanner);

        System.out.println("Welcome to AwesomeGIC Bank! What would you like to do?");

        while (true) {
            System.out.println("[T] Input transactions ");
            System.out.println("[I] Define interest rules");
            System.out.println("[P] Print statement");
            System.out.println("[Q] Quit");
            System.out.print("> ");
            String input = scanner.nextLine().trim().toUpperCase();

            switch (input) {
                case "T":
                    txnHandler.handle();
                    break;
                case "I":
                    interestHandler.handle();
                    break;
                case "P":
                    statementPrinter.handle();
                    break;
                case "Q":
                    System.out.println("Thank you for using AwesomeGIC Bank. Have a nice day!");
                    return;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
    }
}
