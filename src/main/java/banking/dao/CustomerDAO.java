package banking.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import banking.entities.Customer;

public interface CustomerDAO extends JpaRepository<Customer, Long>{
	public Customer findByName(String name);
}
