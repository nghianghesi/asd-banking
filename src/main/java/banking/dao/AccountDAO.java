package banking.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import banking.entities.Account;


public interface AccountDAO extends JpaRepository<Account, Long>{
	public Account findByNumber(String number);
}
