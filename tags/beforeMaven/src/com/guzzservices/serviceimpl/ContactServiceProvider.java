/**
 * 
 */
package com.guzzservices.serviceimpl;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import net.sf.jml.MsnContact;
import net.sf.jml.MsnContactList;
import net.sf.jml.MsnMessenger;
import net.sf.jml.event.MsnContactListAdapter;
import net.sf.jml.event.MsnMessengerAdapter;
import net.sf.jml.impl.MsnMessengerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.guzz.exception.ServiceExecutionException;

import com.guzzservices.dir.Contact;
import com.guzzservices.dir.contact.RemoteContactServiceImpl;
import com.guzzservices.dir.contact.RemoteContactServiceImpl.ContactQueryRequest;
import com.guzzservices.rpc.server.CommandHandlerAdapter;
import com.guzzservices.rpc.server.CommandServerService;
import com.guzzservices.rpc.util.JsonUtil;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class ContactServiceProvider extends CommandHandlerAdapter {
	private static final Log log = LogFactory.getLog(ContactServiceProvider.class) ;
	
	private int timeoutInSeconds = 30 ;
	
	public void setCommandServerService(CommandServerService css){
		css.addCommandHandler(RemoteContactServiceImpl.COMMAND, this) ;
	}
	
	public String executeCommand(String command, String param) throws Exception {
		if(RemoteContactServiceImpl.COMMAND.equals(command)){
			ContactQueryRequest request = JsonUtil.fromJson(param, ContactQueryRequest.class) ;
			
			if("msn".equals(request.getFrom())){
				return JsonUtil.toJson(queryMsnContacts(request.getName(), request.getPassword())) ;
			}
			
			throw new ServiceExecutionException("unknown from: " + request.getFrom()) ;
		}
		
		throw new ServiceExecutionException("unknown") ;
	}
	
	public List<Contact> queryMsnContacts(String email, String password) throws InterruptedException{
		final MsnMessenger msn = MsnMessengerFactory.createMsnMessenger(email, password);
		final CountDownLatch waitForLogin = new CountDownLatch(1) ;
		final AtomicBoolean success = new AtomicBoolean(false) ;
		
		msn.addMessengerListener(new MsnMessengerAdapter() {

            public void loginCompleted(MsnMessenger messenger) {
            	if(log.isDebugEnabled()){
            		log.debug(messenger.getOwner().getEmail() + ": login") ;
            	}
            	
                success.set(true) ;
                
                msn.addContactListListener( new MsnContactListAdapter(){
                	
        			public void contactListInitCompleted(MsnMessenger messenger) {
        				waitForLogin.countDown() ;
        				
        				if(log.isDebugEnabled()){
                    		log.debug(messenger.getOwner().getEmail() + ": contactListInitCompleted") ;
                    	}
        			}
        		}) ;
            }

            public void logout(MsnMessenger messenger) {
                waitForLogin.countDown() ;
                
            	if(log.isDebugEnabled()){
            		log.debug(messenger.getOwner().getEmail() + ": logout") ;
            	}
            }

            public void exceptionCaught(MsnMessenger messenger, Throwable t) {
                waitForLogin.countDown() ;
                
                if(log.isDebugEnabled()){
            		log.debug(messenger.getOwner().getEmail() + ": exception caught.", t) ;
            	}
            }
        });
		
		msn.login() ;
		waitForLogin.await(timeoutInSeconds, TimeUnit.SECONDS) ;
		
		if(success.get()){
			MsnContactList contactList = msn.getContactList() ;
			
			if(log.isDebugEnabled()){
	    		log.debug(msn.getOwner().getEmail() + ": " + contactList.getContacts().length + " contacts fetched.") ;
	    	}
			
			LinkedList<Contact> cs = new LinkedList<Contact>() ;
			
			for(MsnContact c : contactList.getContacts()){
				Contact ct = new Contact(c.getFriendlyName(), c.getEmail().getEmailAddress()) ;
				
				cs.addLast(ct) ;
			}
			
			msn.logout() ;
			
			return cs ;
		}
		
		throw new ServiceExecutionException("login failed.") ;
	}

	public int getTimeoutInSeconds() {
		return timeoutInSeconds;
	}

	public void setTimeoutInSeconds(int timeoutInSeconds) {
		this.timeoutInSeconds = timeoutInSeconds;
	}

}
