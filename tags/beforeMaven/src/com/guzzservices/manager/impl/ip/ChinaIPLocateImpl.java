package com.guzzservices.manager.impl.ip;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.guzz.exception.ServiceExecutionException;
import org.guzz.io.FileResource;
import org.springframework.beans.factory.InitializingBean;

import com.guzzservices.dir.ip.ChinaIPLocateServiceImpl;
import com.guzzservices.dir.ip.LocateResult;
import com.guzzservices.rpc.server.CommandHandlerAdapter;
import com.guzzservices.rpc.server.CommandServerService;
import com.guzzservices.rpc.util.JsonUtil;

/**
 * IPLocator based on the cz's IP database. 
 */
public class ChinaIPLocateImpl extends CommandHandlerAdapter implements InitializingBean {
	protected static final Logger log = Logger.getLogger(ChinaIPLocateImpl.class);
	
	private CommandServerService commandServerService ;
	
	private String czFile ;
	
	private BSTree<CityMark> tree ;
	
	public void reloadIPTable() throws IOException{
		FileResource fr = new FileResource(this.czFile) ;
		
		try{
			CZIPLoader loader = new CZIPLoader(fr.getFile()) ;
			
			log.info("Loading IP Tables...") ;
			
			BSTree<CityMark> newTree = new AVLBSTree<CityMark>() ;
			int count = loader.loadIPTable(newTree) ;
			
			log.info("load " + count + " IP records from cz file:" + fr.getFile().getAbsolutePath()) ;
			
			//TODO: load tuning records from the database.
			this.tree = newTree ;
			
			if(this.commandServerService != null){
				this.commandServerService.addCommandHandler(ChinaIPLocateServiceImpl.COMMAND_IP_QUERY, this) ;
				this.commandServerService = null ;
			}
			
		}finally{
			fr.close() ;
		}
	}

	public String executeCommand(String command, String param) throws Exception {
		if(this.tree == null){
			throw new ServiceExecutionException("IPLocation Service not available.") ;
		}
		
		CityMark mark = null ;
		
		if(ChinaIPLocateServiceImpl.COMMAND_IP_QUERY.equals(command)){
			mark = this.tree.searchMatchedIP(CityMark.stringIP2Number(param)) ;
		}else{
			throw new UnsupportedOperationException(command) ;
		}
		
		if(mark == null) return null ;
		
		LocateResult loc = new LocateResult() ;
		loc.setCityName(mark.getCityName()) ;
		loc.setDetailLocation(mark.getDetailLocation()) ;
		loc.setCityMarker(mark.getCityMarker()) ;
		
		return JsonUtil.toJson(loc) ;
	}
	
	public void setCommandServerService(CommandServerService commandServerService) {
		if(this.tree != null){
			commandServerService.addCommandHandler(ChinaIPLocateServiceImpl.COMMAND_IP_QUERY, this) ;
		}else{
			this.commandServerService = commandServerService ;
		}
	}

	public String getCzFile() {
		return czFile;
	}

	public void setCzFile(String czFile) {
		this.czFile = czFile;
	}

	public void afterPropertiesSet() throws Exception {
		reloadIPTable() ;
	}
}
