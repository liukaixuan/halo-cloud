/**
 * 
 */
package com.guzzservices.manager.impl;

import java.util.HashMap;
import java.util.List;

import org.guzz.dao.GuzzBaseDao;
import org.guzz.jdbc.ObjectBatcher;
import org.guzz.orm.se.SearchExpression;
import org.guzz.orm.se.Terms;
import org.guzz.service.core.SlowUpdateService;
import org.guzz.transaction.WriteTranSession;

import com.guzzservices.business.Configuration;
import com.guzzservices.business.ConfigurationGroup;
import com.guzzservices.management.config.ConfigurationServiceImpl;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.IAuthManager;
import com.guzzservices.manager.IConfigurationManager;
import com.guzzservices.rpc.server.CommandHandler;
import com.guzzservices.rpc.server.CommandServerService;
import com.guzzservices.rpc.util.JsonUtil;
import com.guzzservices.sso.LoginUser;
import com.guzzservices.version.VersionControlService;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class ConfigurationManagerImpl extends GuzzBaseDao implements IConfigurationManager, CommandHandler {
	
	private SlowUpdateService slowUpdateService ;
	
	private VersionControlService versionControlService ;
	
	private IAuthManager authManager ;

	public void addGroup(LoginUser loginUser, ConfigurationGroup group) {
		WriteTranSession write = this.getTransactionManager().openRWTran(false) ;
		
		try{
			write.insert(group) ;
			
			authManager.newAuth(write, loginUser.getUserName(), Constants.serviceName.CONFIGURATION, group.getId()) ;
			
			write.commit() ;
		}catch(RuntimeException e){
			write.rollback() ;
			
			throw e ;
		}finally{
			write.close() ;
		}
		
		this.versionControlService.incVersion(Constants.buildVersionControlPath(Constants.serviceName.CONFIGURATION, group.getId())) ;
	}

	public Configuration getById(String id) {
		return (Configuration) super.getForUpdate(Configuration.class, id) ;
	}

	public Configuration getByParameterName(String groupId, String parameter) {
		SearchExpression se = SearchExpression.forClass(Configuration.class) ;
		se.and(Terms.eq("groupId", groupId)) ;
		se.and(Terms.eq("parameter", parameter)) ;
		
		return (Configuration) super.findObject(se) ;
	}

	public ConfigurationGroup getGroup(String groupId) {
		return (ConfigurationGroup) super.getForUpdate(ConfigurationGroup.class, groupId) ;
	}

	public void save(Configuration config) {
		super.insert(config) ;
		
		this.updateVersion(config.getGroupId()) ;
	}

	public void update(Configuration config) {
		super.update(config) ;
		
		this.updateVersion(config.getGroupId()) ;
	}

	public void updateByBatch(String groupId, List<Configuration> configs) {
		if(configs.isEmpty()) return ;
				
		WriteTranSession tran = super.getTransactionManager().openRWTran(false) ;
		
		try{
			ObjectBatcher batcher = tran.createObjectBatcher() ;
			
			for(Configuration c : configs){
				batcher.update(c) ;
			}
			
			batcher.executeUpdate() ;
			
			tran.commit() ;
		}catch(RuntimeException e){
			tran.rollback() ;
			
			throw e ;
		}finally{
			tran.close() ;
		}
		
		this.updateVersion(groupId) ;
	}

	public void updateGroup(ConfigurationGroup group) {
		super.update(group) ;
		
		this.updateVersion(group.getId()) ;
	}
	
	public void updateVersion(String groupId){
		slowUpdateService.updateCount(ConfigurationGroup.class, null, "version", groupId, 1) ;
		
		this.versionControlService.incVersion(Constants.buildVersionControlPath(Constants.serviceName.CONFIGURATION, groupId)) ;
	}
	
	public String executeCommand(String command, String param) throws Exception {
		if(ConfigurationServiceImpl.COMMAND_LOAD_CONFIG.equals(command)){
			SearchExpression se = SearchExpression.forClass(Configuration.class, 1, 200) ;
			se.and(Terms.eq("groupId", param)) ;
			
			List<Configuration> configs = super.list(se) ;
			HashMap<String, Object> result = new HashMap<String, Object>() ;
			
			for(Configuration c : configs){
				Object value = c.toTypedValue() ;
				
				result.put(c.getParameter(), value) ;
			}
			
			return JsonUtil.toJson(result) ;
		}else if(ConfigurationServiceImpl.COMMAND_QUERY_VERSION.equals(command)){
			long v = versionControlService.getVersion(Constants.buildVersionControlPath(Constants.serviceName.CONFIGURATION, param)) ;
			
			return String.valueOf(v) ;
		}else{
			throw new UnsupportedOperationException(command) ;
		}
	}

	public byte[] executeCommand(String command, byte[] param) throws Exception {
		throw new NoSuchMethodException("not supported!") ;
	}
	
	public void setCommandServerService(CommandServerService commandServerService) {
		commandServerService.addCommandHandler(ConfigurationServiceImpl.COMMAND_LOAD_CONFIG, this) ;
		commandServerService.addCommandHandler(ConfigurationServiceImpl.COMMAND_QUERY_VERSION, this) ;
	}

	public SlowUpdateService getSlowUpdateService() {
		return slowUpdateService;
	}

	public void setSlowUpdateService(SlowUpdateService slowUpdateService) {
		this.slowUpdateService = slowUpdateService;
	}

	public VersionControlService getVersionControlService() {
		return versionControlService;
	}

	public void setVersionControlService(VersionControlService versionControlService) {
		this.versionControlService = versionControlService;
	}

	public IAuthManager getAuthManager() {
		return authManager;
	}

	public void setAuthManager(IAuthManager authManager) {
		this.authManager = authManager;
	}

}
