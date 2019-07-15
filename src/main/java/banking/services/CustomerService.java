package banking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import banking.dao.CustomerDAO;
import banking.entities.Customer;

@Service
@Transactional
public class CustomerService {
	
	@Autowired
	private CustomerDAO customerDAO;
	
	public Customer createCustomer(String name) {
		Customer customer = new Customer();
		customer.setName(name);
		this.customerDAO.save(customer);
		return customer;
	}
}
