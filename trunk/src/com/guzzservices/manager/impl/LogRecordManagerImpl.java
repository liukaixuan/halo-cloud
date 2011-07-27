package com.guzzservices.manager.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.guzz.dao.GuzzBaseDao;
import org.guzz.dao.PageFlip;
import org.guzz.exception.ServiceExecutionException;
import org.guzz.orm.BusinessInterpreter;
import org.guzz.orm.ObjectMapping;
import org.guzz.orm.se.SearchExpression;
import org.guzz.util.StringUtil;

import com.guzzservices.business.LogRecord;
import com.guzzservices.management.alog.AppLogServiceImpl;
import com.guzzservices.management.alog.AppLogServiceImpl.AppLogQueryRequest;
import com.guzzservices.manager.ILogAppManager;
import com.guzzservices.manager.ILogRecordManager;
import com.guzzservices.rpc.server.ClientInfo;
import com.guzzservices.rpc.server.CommandHandler;
import com.guzzservices.rpc.server.CommandHandlerAdapter;
import com.guzzservices.rpc.server.CommandServerService;
import com.guzzservices.rpc.util.JsonPageFlip;
import com.guzzservices.rpc.util.JsonUtil;

/**
 * 
 * 
 * @author liu kaixuan
 */
public class LogRecordManagerImpl extends GuzzBaseDao implements ILogRecordManager {
	private static final Log log = LogFactory.getLog(LogRecordManagerImpl.class) ;
	
	private ILogAppManager logAppManager ;
	
	private int maxPageSize = 2000 ;
	
	public void insert(String appIP, String secureCode, int userId, LogRecord record) {
		int appId = this.logAppManager.getAppIdBySecureCode(secureCode) ;
		
		//not exist.
		if(appId < 1){
			throw new ServiceExecutionException("unknown secure code:[" + secureCode + "] from app server:" + appIP) ;
		}
		
		record.setAppId(appId) ;
		record.setAppIP(appIP) ;
		record.setUserId(userId) ;
		record.setCreatedTime(new Date()) ;
		
		this.insert(record, appId) ;
	}
	
	public PageFlip queryLogs(String appIP, String secureCode, List<String> conditions, int pageNo, int pageSize, String orderBy){
		int appId = this.logAppManager.getAppIdBySecureCode(secureCode) ;
		
		//not exist.
		if(appId < 1){
			throw new ServiceExecutionException("unknown secure code:[" + secureCode + "] from app server:" + appIP) ;
		}
		
		if(pageSize > maxPageSize){
			pageSize = maxPageSize ;
		}
		
		LinkedList<Object> terms = new LinkedList<Object>() ;
		BusinessInterpreter gi =  super.getGuzzContext().getBusiness(LogRecord.class.getName()).getInterpret() ;
		ObjectMapping mapping = super.getGuzzContext().getObjectMappingManager().getObjectMapping(LogRecord.class.getName(), appId) ;
		
		if(conditions != null && !conditions.isEmpty()){
			for(int i = 0 ; i < conditions.size() ; i++){
				Object condition = conditions.get(i) ;
				
				try {
					if(condition != null){
						Object mc = gi.explainCondition(mapping, condition) ;
						if(mc != null){
							terms.addLast(mc) ;
						}
					}
				} catch (Exception e) {
					throw new ServiceExecutionException("error to translate condition:[" + condition + "], msg:" + e.getMessage()) ;
				}
			}
		}
		
		if(terms.isEmpty()){
			return null ;
		}else{
			//query
			SearchExpression se = SearchExpression.forClass(LogRecord.class, pageNo, pageSize) ;
			se.setTableCondition(appId) ;
			se.and(terms) ;
			if(StringUtil.notEmpty(orderBy)){
				se.setOrderBy(orderBy) ;
			}
			
			return super.page(se) ;
		}
	}

	public LogRecord getForRead(int id) {
		return (LogRecord) super.getForRead(LogRecord.class, id) ;
	}

	public LogRecord getForUpdate(int id) {
		return (LogRecord) super.getForUpdate(LogRecord.class, id) ;
	}
	
	public void setCommandServerService(CommandServerService css){
		css.addCommandHandler(AppLogServiceImpl.COMMAND_NEW_LOG, handler) ;
		css.addCommandHandler(AppLogServiceImpl.COMMAND_QUERY_LOG, handler) ;
	}
	
	private final CommandHandler handler = new CommandHandlerAdapter(){

		public String executeCommand(ClientInfo client, String command, String param) throws Exception {
			if(AppLogServiceImpl.COMMAND_NEW_LOG.equals(command)){
				Map<String, Object> params = JsonUtil.fromJson(param, HashMap.class) ;
				String scode = (String) params.remove(AppLogServiceImpl.KEY_APP_SECURE_CODE) ;
				Integer userId = (Integer) params.remove(AppLogServiceImpl.KEY_APP_USER_ID) ;
					
				LogRecord r = new LogRecord() ;
				r.setOtherProps(params) ;
				
				insert(client.getIP(), scode, userId.intValue(), r) ;
				
				return null ;
				
			}else if(AppLogServiceImpl.COMMAND_QUERY_LOG.equals(command)){
				AppLogQueryRequest request = JsonUtil.fromJson(param, AppLogQueryRequest.class) ;
				
				PageFlip data = queryLogs(client.getIP(), request.getSecureCode(), request.getConditions(), request.getPageNo(), request.getPageSize(), request.getOrderBy()) ;
				
				return JsonPageFlip.fromPageFlip(data, LogRecord.class).toJson() ;
			}
			
			return null ;
		}
		
	} ;

	public ILogAppManager getLogAppManager() {
		return logAppManager;
	}

	public void setLogAppManager(ILogAppManager logAppManager) {
		this.logAppManager = logAppManager;
	}

	public int getMaxPageSize() {
		return maxPageSize;
	}

	public void setMaxPageSize(int maxPageSize) {
		this.maxPageSize = maxPageSize;
	}

}
