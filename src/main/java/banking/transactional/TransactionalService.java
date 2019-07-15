package banking.transactional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class TransactionalService {	
	public void registerAction(Runnable action) {
		TransactionSynchronizationManager.registerSynchronization(
				new TransactionSynchronizationAdapter(){
		            public void afterCommit(){
		            	action.run();
		            }				
		});		
	}	
}
