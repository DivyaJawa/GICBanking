package gic;
import java.util.*;
import java.time.LocalDate;

public class Account {
    private String accountId;
    private List<Transaction> transactions;
    private double balance;
    private Map<LocalDate, Integer> txnCounterByDate;

    public Account(String accountId) {
        this.accountId = accountId;
        this.transactions = new ArrayList<>();
        this.txnCounterByDate = new HashMap<>();
        this.balance = 0.0;
    }

    public String getAccountId() { return accountId; }
    public List<Transaction> getTransactions() { return transactions; }
    public double getBalance() { return balance; }

    public boolean addTransaction(LocalDate date, String type, double amount) {
        type = type.toUpperCase();

        if (amount <= 0) return false;
        if (type.equals("W") && balance < amount) return false;
        if (type.equals("W") && transactions.isEmpty()) return false;

        int count = txnCounterByDate.getOrDefault(date, 0) + 1;
        txnCounterByDate.put(date, count);
        String txnId = date.toString().replaceAll("-", "") + "-" + String.format("%02d", count);

        Transaction txn = new Transaction(date, txnId, type, amount);
        transactions.add(txn);
        if (type.equals("D")) balance += amount;
        else if (type.equals("W")) balance -= amount;

        return true;
    }

    public void addInterest(LocalDate date, double interestAmount) {
        Transaction interestTxn = new Transaction(date, "", "I", interestAmount);
        transactions.add(interestTxn);
        balance += interestAmount;
    }
}