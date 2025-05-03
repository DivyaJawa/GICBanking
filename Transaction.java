package gic;
import java.time.LocalDate;

public class Transaction {
    private LocalDate date;
    private String txnId;
    private String type;
    private double amount;

    public Transaction(LocalDate date, String txnId, String type, double amount) {
        this.date = date;
        this.txnId = txnId;
        this.type = type.toUpperCase();
        this.amount = amount;
    }

    public LocalDate getDate() { return date; }
    public String getTxnId() { return txnId; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
}
