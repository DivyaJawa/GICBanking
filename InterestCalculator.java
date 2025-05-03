package gic;
import java.util.*;
import java.time.LocalDate;

import java.time.YearMonth;


public class InterestCalculator {
    private Bank bank;

    public InterestCalculator(Bank bank) {
        this.bank = bank;
    }

    public void applyInterestForMonth(Account account, YearMonth month) {
        
        for (Transaction txn : account.getTransactions()) {
            if (txn.getType().equals("I") && YearMonth.from(txn.getDate()).equals(month)) {
                return; 
            }
        }

        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();

        
        TreeMap<LocalDate, Double> eodBalances = getEODBalances(account, startDate, endDate);

        
        double totalInterest = 0.0;
        LocalDate periodStart = startDate;
        while (!periodStart.isAfter(endDate)) {
            InterestRule rule = bank.getApplicableRule(periodStart);
            if (rule == null) break;

            LocalDate periodEnd = getNextRuleChangeDate(rule.getDate().plusDays(1), endDate);
            double balance = eodBalances.getOrDefault(periodStart, 0.0);
            long numDays = periodStart.until(periodEnd.plusDays(1)).getDays();

            double interest = balance * rule.getRate() / 100.0 * numDays / 365.0;
            totalInterest += interest;

            periodStart = periodEnd.plusDays(1);
        }

       
        double roundedInterest = Math.round(totalInterest * 100.0) / 100.0;
        if (roundedInterest > 0) {
            account.addInterest(endDate, roundedInterest);
        }
    }

    public void applyInterestToAllAccounts() {
        for (Account account : bank.getAllAccounts().values()) {
            LocalDate lastTxnDate = getLastTransactionDate(account);
            if (lastTxnDate == null) continue;
            applyInterestForMonth(account, YearMonth.from(lastTxnDate));
        }
    }

    private TreeMap<LocalDate, Double> getEODBalances(Account account, LocalDate from, LocalDate to) {
        TreeMap<LocalDate, Double> balances = new TreeMap<>();
        double runningBalance = 0.0;

        
        List<Transaction> transactions = new ArrayList<>(account.getTransactions());
        transactions.sort(Comparator.comparing(Transaction::getDate));

        LocalDate current = from;
        int i = 0;
        while (!current.isAfter(to)) {
            while (i < transactions.size() && !transactions.get(i).getDate().isAfter(current)) {
                Transaction txn = transactions.get(i);
                if (txn.getType().equals("D") || txn.getType().equals("I")) {
                    runningBalance += txn.getAmount();
                } else if (txn.getType().equals("W")) {
                    runningBalance -= txn.getAmount();
                }
                i++;
            }
            balances.put(current, runningBalance);
            current = current.plusDays(1);
        }

        return balances;
    }

    private LocalDate getNextRuleChangeDate(LocalDate after, LocalDate max) {
        for (InterestRule rule : bank.getInterestRules()) {
            LocalDate date = rule.getDate();
            if (date.isAfter(after) && !date.isAfter(max)) {
                return date.minusDays(1);
            }
        }
        return max;
    }

    private LocalDate getLastTransactionDate(Account account) {
        return account.getTransactions().stream()
                .map(Transaction::getDate)
                .max(LocalDate::compareTo)
                .orElse(null);
    }
}