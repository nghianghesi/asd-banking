package banking.interest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import banking.entities.AccountType;

@Component
@Qualifier("interest-calculator")
@Lazy
public class CheckingInterestCalculator implements InterestCalculator{

	@Override
	public double calculate(double balance) {
		if (balance > 0 && balance <= 1000) return balance * 0.015;
		if (balance > 1000) return balance * 0.025;
		return 0;
	}
	

	public AccountType getAccountTypeKey() {
		return AccountType.Checking;
	}

}
