package banking.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class Account {
	@Id 
	@GeneratedValue
	Long id;
	
	
	private String number;
	private double balance;
	private AccountType type;
	
	@ManyToOne
	private Customer customer;

	@OneToMany(mappedBy="account")
	@Cascade(CascadeType.ALL)
	private List<AccountEntry> entries = new ArrayList<AccountEntry>();

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	

	public double getBalance() {
		return balance;
	}

	public AccountType getType() {
		return type;
	}

	public void setType(AccountType type) {
		this.type = type;
	}

	public Customer getCustomer() {
		return customer;
	}


	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


	public List<AccountEntry> getEntries() {
		return Collections.unmodifiableList(entries);
	}

	
	private void addEntry(AccountEntry entry) {
		entry.account = this;
		this.entries.add(entry);
	}
	
	public Long getId() {
		return id;
	}
	
	
	public boolean modifyBalance(AccountEntryAction action, String byUser, String interactingAccount, double amount){
		if(this.getBalance()+amount>=0) {
			this.balance += amount;
			this.addEntry(new AccountEntry(
					new Date(), amount, 
					action, interactingAccount, byUser));
			return true;
		}else {
			return false;
		}
	}	
	
	
	@Entity
	public static class AccountEntry {
		@Id 
		@GeneratedValue
		Long id;
		
		@ManyToOne
		private Account account;
		
		
		private Date date;
		private double amount;
		private AccountEntryAction action;
		private String otherAccountNumber;
		private String byPersonName;
		
		public AccountEntry() {
			
		}
		
		public AccountEntry(Date date, Double amount, AccountEntryAction description, String otherAccountNumber, String byPersonName) {
			super();
			this.date = date;
			this.amount = amount;
			this.action = description;
			this.otherAccountNumber = otherAccountNumber;
			this.byPersonName = byPersonName;
		}
		public Date getDate() {
			return date;
		}
		
		public Double getAmount() {
			return amount;
		}
		
		public AccountEntryAction getAction() {
			return action;
		}
		
		public String getOtherAccountNumber() {
			return otherAccountNumber;
		}

		public String getByPersonName() {
			return byPersonName;
		}
		
	}
	
}
