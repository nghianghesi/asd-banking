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

@Entity
public class Account {
	@Id 
	@GeneratedValue
	Long id;
	
	
	private String number;
	private Double balance;
	
	@ManyToOne
	private Customer customer;

	@OneToMany(mappedBy="account")
	private List<AccountEntry> entries = new ArrayList<AccountEntry>();

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	

	public Double getBalance() {
		return balance;
	}


	public void setBalance(Double balance) {
		this.balance = balance;
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

	
	public void addEntry(AccountEntry entry) {
		entry.account = this;
		this.entries.add(entry);
	}

	public void removeEntry(AccountEntry entry) {
		entry.account = null;
		this.entries.remove(entry);
	}
	
	public Long getId() {
		return id;
	}
	
	
	@Entity
	public static class AccountEntry {
		@Id 
		@GeneratedValue
		Long id;
		
		@ManyToOne
		private Account account;
		
		
		private Date date;
		private Double amount;
		private String description;
		private String otherAccountNumber;
		private String byPersonName;
		
		public AccountEntry() {
			
		}
		
		public AccountEntry(Date date, Double amount, String description, String otherAccountNumber, String byPersonName) {
			super();
			this.date = date;
			this.amount = amount;
			this.description = description;
			this.otherAccountNumber = otherAccountNumber;
			this.byPersonName = byPersonName;
		}
		public Date getDate() {
			return date;
		}
		
		public Double getAmount() {
			return amount;
		}
		
		public String getDescription() {
			return description;
		}
		
		public String getOtherAccountNumber() {
			return otherAccountNumber;
		}

		public String getByPersonName() {
			return byPersonName;
		}
		
	}
	
}
