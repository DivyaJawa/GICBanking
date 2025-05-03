package gic;
import java.time.LocalDate;

public class InterestRule {
    private LocalDate date;
    private String ruleId;
    private double rate;

    public InterestRule(LocalDate date, String ruleId, double rate) {
        this.date = date;
        this.ruleId = ruleId;
        this.rate = rate;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getRuleId() {
        return ruleId;
    }

    public double getRate() {
        return rate;
    }
}