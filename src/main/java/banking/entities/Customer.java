package banking.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Customer {
	@Id 
	@GeneratedValue
	Long id;
	
	
	private String name;


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Long getId() {
		return id;
	}
	
	
}
