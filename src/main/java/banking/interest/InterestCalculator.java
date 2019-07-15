package banking.interest;

import banking.entities.AccountType;

public interface InterestCalculator {
	public double calculate(double balance);
	public AccountType getAccountTypeKey();
}
