package gic;
import java.util.*;
import java.time.LocalDate;

public class Bank {
    private Map<String, Account> accounts;
    private Map<LocalDate, InterestRule> interestRules;

    public Bank() {
        this.accounts = new HashMap<>();
        this.interestRules = new TreeMap<>();
    }

    public Account getOrCreateAccount(String accountId) {
        return accounts.computeIfAbsent(accountId, Account::new);
    }

    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    public void addInterestRule(LocalDate date, InterestRule rule) {
        interestRules.put(date, new InterestRule(date, rule.getRuleId(), rule.getRate()));
    }

    public List<InterestRule> getInterestRules() {
        return new ArrayList<>(interestRules.values());
    }

    public InterestRule getApplicableRule(LocalDate date) {
        InterestRule applicable = null;
        for (Map.Entry<LocalDate, InterestRule> entry : interestRules.entrySet()) {
            if (!entry.getKey().isAfter(date)) {
                applicable = entry.getValue();
            } else {
                break;
            }
        }
        return applicable;
    }

    public Map<String, Account> getAllAccounts() {
        return accounts;
    }
}
