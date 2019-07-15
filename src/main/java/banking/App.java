package banking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import banking.entities.Account;
import banking.observers.AccountObserver;
import banking.observers.AccountSubject;
import banking.services.AccountService;
import banking.services.CustomerService;

@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
public class App implements CommandLineRunner{
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
	
	@Bean
	@Qualifier("new-account-subject")
	public AccountSubject newAccountSubject(@Autowired @Qualifier("new-account-subject") List<AccountObserver> observers) {
		AccountSubject sub = new AccountSubject();
		sub.addObservers(observers);
		return sub;
	}
	
	@Bean
	@Qualifier("updated-account-subject")
	public AccountSubject updatedAccountSubject(@Autowired @Qualifier("updated-account-subject") List<AccountObserver> observers) {
		AccountSubject sub = new AccountSubject();
		sub.addObservers(observers);
		return sub;
	}
	
	@Autowired
	private ApplicationContext context;

	@Override
	public void run(String... args) throws Exception {
		AccountService accountService = context.getBean(AccountService.class);
		CustomerService customerService = context.getBean(CustomerService.class);
		customerService.createCustomer("Test Customer 1");
		customerService.createCustomer("Test Customer 2");
		Account acc1 = accountService.createAccount("Test Customer 1");
		Account acc2 = accountService.createAccount("Test Customer 2");
		accountService.deposit(acc1.getNumber(), "Test Customer 1", 10);
		accountService.withdraw(acc1.getNumber(), "Test Customer 1", 5);
		accountService.transferFunds(acc1.getNumber(), acc2.getNumber(), "Test Customer 1", 5);
	}	
}
