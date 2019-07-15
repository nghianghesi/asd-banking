package banking.services;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import banking.dao.AccountDAO;
import banking.dao.CustomerDAO;
import banking.entities.Account;
import banking.entities.AccountEntryAction;
import banking.entities.AccountType;
import banking.entities.Customer;
import banking.interest.InterestCalculator;
import banking.observers.AccountLogger;
import banking.observers.AccountSubject;


@Service
@Transactional
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountLogger.class);
	@Resource
	@Qualifier("new-account-subject")
	private AccountSubject newAccountSubject;
	
	@Resource
	@Qualifier("updated-account-subject")	
	private AccountSubject updatedAccountSubject;
	
	private Map<AccountType, InterestCalculator> interestCalculators = new HashMap<>();
	
	public AccountService(@Autowired @Qualifier("interest-calculator") List<InterestCalculator> interestCalculators) {
		for(InterestCalculator cal:interestCalculators) {
			this.interestCalculators.put(cal.getAccountTypeKey(), cal);
		}
	}
	
	private void onAccountCreated(Account account) {
		this.newAccountSubject.notifyAccountChanged(account);
	}	
	
	private void onAccountUpdated(Account account) {
		this.updatedAccountSubject.notifyAccountChanged(account);
	}
	
	@Autowired
	private CustomerDAO customerDAO;
	
	@Autowired
	private AccountDAO acountDAO;
		
	public String generateNextAccountNumber() {
		return RandomStringUtils.randomAlphanumeric(10);
	}

	public Account createAccount(String customerName) {
		Customer customer = this.customerDAO.findByName(customerName);
		if(customer!=null) {
			Account	account = new Account();
			account.setCustomer(customer);
			account.setNumber(this.generateNextAccountNumber());
			this.acountDAO.save(account);
			this.onAccountCreated(account);
			return account;
		}
		return null;
	}
	
	private Account changeAccountBalance(AccountEntryAction action, Account account, String byUser, String otherAccount, double amount){
		if(account!=null && account.modifyBalance(action, byUser, otherAccount, amount)) {
			this.acountDAO.save(account);
			this.onAccountUpdated(account);
			return account;
		}else {
			return null;
		}
	}
	
	private Account changeAccountBalance(AccountEntryAction action, String accountNumber, String byUser, String otherAccount, double amount){
		Account account = this.acountDAO.findByNumber(accountNumber);
		return this.changeAccountBalance(action, account, byUser, otherAccount, amount);
	}	
	
	public void deposit(String accountNumber, String byUser, double amount){
		this.changeAccountBalance(AccountEntryAction.Deposite, accountNumber,  byUser, null, amount);
	}	
	
	public void withdraw(String accountNumber, String byUser, double amount){
		this.changeAccountBalance(AccountEntryAction.Withdraw, accountNumber,  byUser, null, -amount);
	}
	
	public void transferFunds(String fromAccountNumber, String toAccountNumber, String byUser, double amount){
		if(this.changeAccountBalance(AccountEntryAction.WithdrawTransfer, fromAccountNumber,  byUser, toAccountNumber, -amount)!=null) {
			this.changeAccountBalance(AccountEntryAction.TransferDeposite, toAccountNumber,  byUser, fromAccountNumber, amount);
		}
	}	
	
	public void addInterest() {
		for(Account acc:this.getAllAccounts()) {
			InterestCalculator cal = this.interestCalculators.getOrDefault(acc.getType(),null);
			if(cal!=null) {
				double interest = cal.calculate(acc.getBalance());
				if(interest >0) {
					this.changeAccountBalance(AccountEntryAction.InterestDeposite, acc, null, null, interest);
				}
			}else {
				logger.warn("Missing interest calculator for {}", acc.getType());
			}
		}
	}
	
	public List<Account> getAllAccounts(){
		return this.acountDAO.findAll();
	}
	
	public Account getAccount(String accountNumber){
		return this.acountDAO.findByNumber(accountNumber);
	}

}
