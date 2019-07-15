package banking.observers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import banking.entities.Account;
import banking.transactional.TransactionalService;


@Component
@Qualifier("updated-account-subject")
public class EmailSender implements AccountObserver{
	private static final Logger logger = LoggerFactory.getLogger(AccountLogger.class);

    @Autowired 
    private TransactionalService transactionalService;
    
	@Override
	public void onNotified(Account account) {
		transactionalService.registerAction(()->{
			logger.info("(Email) account updated: {}", account.getNumber());
		});
	}

}
