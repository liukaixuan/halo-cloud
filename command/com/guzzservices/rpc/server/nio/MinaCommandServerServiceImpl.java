/**
 * 
 */
package com.guzzservices.rpc.server.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.guzz.exception.ServiceExecutionException;
import org.guzz.service.AbstractService;
import org.guzz.service.ServiceConfig;
import org.guzz.util.StringUtil;

import com.guzzservices.rpc.server.ClientInfo;
import com.guzzservices.rpc.server.CommandHandler;
import com.guzzservices.rpc.server.CommandRequest;
import com.guzzservices.rpc.server.CommandResponse;
import com.guzzservices.rpc.server.CommandServerService;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class MinaCommandServerServiceImpl extends AbstractService implements CommandServerService {

	private ServerIoHandler serverIoHandler ;
	
	private IoAcceptor acceptor ;
	
	private Map<String, CommandHandler> handlers = new HashMap<String, CommandHandler>() ;
	
	public boolean configure(ServiceConfig[] scs) {
		if(scs.length == 0){
			log.warn("Mina Server CommandService is not started. no configuration found.") ;
			return false ;
		}

		acceptor = new NioSocketAcceptor() ;
		
		Properties props = scs[0].getProps() ;
		
		int idleTimeSeconds = StringUtil.toInt(props.getProperty("idleTimeSeconds"), 600) ;
		int port = StringUtil.toInt(props.getProperty("port"), 11546) ;
		
		acceptor.getSessionConfig().setUseReadOperation(true) ;
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, idleTimeSeconds) ;
		acceptor.getSessionConfig().setMaxReadBufferSize(4096) ;
		
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ServerCodecFactory("UTF-8"))) ;
		
		serverIoHandler = new ServerIoHandler(this) ;
		
		acceptor.setHandler(serverIoHandler) ;
		
		try {
			acceptor.bind(new InetSocketAddress(port)) ;
		} catch (IOException e) {
			throw new ServiceExecutionException(e) ;
		}
		
		return true ;
	}

	public boolean isAvailable() {
		return acceptor != null ;
	}

	public void shutdown() {
		acceptor.dispose() ;
	}

	public void startup() {

	}
	
	public CommandResponse executeCommand(ClientInfo client, CommandRequest request){
		String command = request.command ;
	
		if(log.isDebugEnabled()){
			log.debug("executing command:" + command) ;
		}
	
		CommandResponse rp = new CommandResponse() ;
		rp.isStringResult = request.isStringParam ;
		
		CommandHandler handler = queryCommandHandler(command) ;
		
		if(handler == null){
			rp.isException = true ;
			rp.isStringResult = true ;
			rp.resultS = "unknown command:" + command ;
		}else{
			try{
				if(request.isStringParam){
					rp.resultS = handler.executeCommand(client, command, request.paramS) ;
				}else{
					rp.resultB = handler.executeCommand(client, command, request.paramB) ;
				}
			}catch(Throwable t){
				StringBuilder sb = new StringBuilder() ;	
				
				while(t.getCause() != null){
					sb.append('(').append(t.getMessage()).append(')') ;
					t = t.getCause() ;
				}
				
				//last stack
				sb.append('(').append(t.getMessage()).append(')') ;
				
				//error while executing the command
				rp.isException = true ;
				rp.isStringResult = true ;
				rp.resultS = "handler exception:" + sb.toString() ;
				
				if(log.isDebugEnabled()){
					log.debug("fail to execute command " + command, t) ;
				}
			}
		}
		
		return rp ;
	}

	public void addCommandHandler(String command, CommandHandler handler) {
		this.handlers.put(command, handler) ;
	}

	public CommandHandler queryCommandHandler(String command) {
		return handlers.get(command) ;
	}

	public CommandHandler removeCommandHandler(String command) {
		return handlers.remove(command) ;
	}

}
