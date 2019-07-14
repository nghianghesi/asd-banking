package banking;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import banking.dao.AccountDAO;
import banking.dao.CustomerDAO;
import banking.entities.Account;
import banking.entities.Account.AccountEntry;
import banking.entities.Customer;


@Service
public class AccountService {
	
	@Resource()
	@Qualifier("new-account-obsever")
	private List<AccountObserver> newAccountObsevers = new ArrayList<>();
	
	@Resource()
	@Qualifier("updated-account-obsever")	
	private List<AccountObserver> updatedAccountObsevers = new ArrayList<>();
	private void onAccountCreated(Account account) {
		for(AccountObserver obs:newAccountObsevers) {
			obs.onChanged(account);
		}
	}	
	
	private void onAccountUpdated(Account account) {
		for(AccountObserver obs:updatedAccountObsevers) {
			obs.onChanged(account);
		}
	}
	
	@Autowired
	private CustomerDAO customerDAO;
	
	@Autowired
	private AccountDAO acountDAO;
	
	public void registerNewAccountObserver(AccountObserver observer) {
		this.newAccountObsevers.add(observer);
	}
	
	public void registerUpdatedAccountObserver(AccountObserver observer) {
		this.updatedAccountObsevers.add(observer);
	}
	
	public String generateNextAccountNumber() {
		return RandomStringUtils.randomAlphanumeric(10);
	}

	public void createAccount(String customerName) {
		Customer customer = this.customerDAO.findByName(customerName);
		if(customer!=null) {
			Account	account = new Account();
			account.setCustomer(customer);
			account.setNumber(this.generateNextAccountNumber());
			this.acountDAO.save(account);
			this.onAccountCreated(account);
		}
	}
	
	private Account changeAccountBalance(String action, String accountNumber, String byUser, String otherAccount, double amount){
		Account account = this.acountDAO.findByNumber(accountNumber);
		if(account!=null && account.getBalance()+amount>0) {
			account.setBalance(account.getBalance()+amount);
			account.addEntry(new AccountEntry(
					new Date(), amount, 
					action, otherAccount, byUser));
			this.acountDAO.save(account);
			this.onAccountUpdated(account);
			return account;
		}else {
			return null;
		}
	}	
	
	public void deposit(String accountNumber, String byUser, double amount){
		this.changeAccountBalance("deposite", accountNumber,  byUser, null, amount);
	}
	
	
	public void withdraw(String accountNumber, String byUser, double amount){
		this.changeAccountBalance("withdraw", accountNumber,  byUser, null, -amount);
	}
	
	public void transferFunds(String fromAccountNumber,String toAccountNumber, String byUser, double amount){
		if(this.changeAccountBalance("withdraw-transfer", fromAccountNumber,  byUser, toAccountNumber, -amount)!=null) {
			this.changeAccountBalance("transfer-deposit", toAccountNumber,  byUser, fromAccountNumber, amount);
		}
	}
	
	
	public List<Account> getAllAccounts(){
		return this.acountDAO.findAll();
	}
	
	public Account getAccount(String accountNumber){
		return this.acountDAO.findByNumber(accountNumber);
	}

}
