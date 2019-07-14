package banking;

import banking.entities.Account;

public interface AccountObserver {
	void onChanged(Account account);
}
