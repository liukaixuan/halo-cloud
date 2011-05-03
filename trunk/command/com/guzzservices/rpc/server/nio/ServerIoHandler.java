/**
 * 
 */
package com.guzzservices.rpc.server.nio;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.guzzservices.rpc.server.CommandRequest;
import com.guzzservices.rpc.server.CommandResponse;
import com.guzzservices.rpc.server.CommandServerService;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class ServerIoHandler extends IoHandlerAdapter {
	private static final Log log = LogFactory.getLog(ServerIoHandler.class) ;
	
	private final CommandServerService commandServerService ;
	
	public ServerIoHandler(CommandServerService commandServerService){
		this.commandServerService = commandServerService ;
	}
	
	public void messageReceived(IoSession session, Object message) throws Exception {
		CommandRequest request = (CommandRequest) message ;
		
		CommandResponse rp = this.commandServerService.executeCommand(request) ;
		
		session.write(rp);
	}
	
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
	    log.warn(cause.getMessage(), cause);
	    
	    session.close(true) ;
	}

	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		session.close(false) ;
	}

}
