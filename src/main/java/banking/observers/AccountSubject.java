package banking.observers;

import java.util.ArrayList;
import java.util.List;

import banking.entities.Account;


public class AccountSubject {
	private List<AccountObserver> accountObsevers = new ArrayList<>();
	
	public void addObserver(AccountObserver observer) {
		this.accountObsevers.add(observer);
	}	
	
	public void addObservers(List<AccountObserver> observers) {
		this.accountObsevers.addAll(observers);
	}	
	
	public void removeObserver(AccountObserver observer) {
		this.accountObsevers.remove(observer);
	}
	
	public void notifyAccountChanged(Account account) {
		for(AccountObserver obs:this.accountObsevers) {
			obs.onNotified(account);
		}
	}
}
