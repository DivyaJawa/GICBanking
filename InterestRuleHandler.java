package gic;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class InterestRuleHandler {
    private Bank bank;
    private Scanner scanner;

    public InterestRuleHandler(Bank bank, Scanner scanner) {
        this.bank = bank;
        this.scanner = scanner;
    }

    public void handle() {
        while (true) {
            System.out.println("Please enter interest rules details in <Date> <RuleId> <Rate in %> format \n(or enter blank to go back to main menu):");
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) break;

            String[] parts = input.split("\\s+");
            if (parts.length != 3) {
                System.out.println("Invalid input format.");
                continue;
            }

            try {
                LocalDate date = LocalDate.parse(parts[0], java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
                String ruleId = parts[1];
                double rate = Double.parseDouble(parts[2]);
                if (rate <= 0 || rate >= 100) {
                    System.out.println("Rate must be between 0 and 100.");
                    continue;
                }
                bank.addInterestRule(date, new InterestRule(date, ruleId, rate));
                List<InterestRule> rules = bank.getInterestRules();
                System.out.println("Interest rules:");
                System.out.println("| Date     | RuleId | Rate (%) |");
                for (InterestRule rule : rules) {
                    System.out.printf("| %s | %s | %8.2f |%n", rule.getDate(), rule.getRuleId(), rule.getRate());
                }
            } catch (Exception e) {
                System.out.println("Invalid input format or error in processing.");
            }
        }
    }
}
