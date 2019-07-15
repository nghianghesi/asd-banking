package banking.observers;

import banking.entities.Account;

public interface AccountObserver {
	void onNotified(Account account);
}
