/**
 * 
 */
package com.guzzservices.manager.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guzz.exception.IllegalParameterException;
import org.guzz.orm.ObjectMapping;
import org.guzz.orm.mapping.RowDataLoader;
import org.guzz.orm.se.SearchExpression;
import org.guzz.orm.se.Terms;
import org.guzz.transaction.WriteTranSession;

import com.guzzservices.business.AuthedService;
import com.guzzservices.exception.NoPermissionException;
import com.guzzservices.manager.IAuthManager;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.rpc.util.JsonUtil;
import com.guzzservices.sso.LoginException;
import com.guzzservices.sso.LoginUser;
import com.guzzservices.sso.SSOService;
import com.guzzservices.store.KVStorageService;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class DefaultSessionManagerImpl extends AbstractBaseManagerImpl<AuthedService> implements ISessionManager, IAuthManager {
	
	private SSOService ssoService ;
	
	private KVStorageService kvStorageService ;

	public LoginUser getLoginUser(HttpServletRequest request, HttpServletResponse response) {
		return (LoginUser) ssoService.getLoginUser(request, response);
	}

	public boolean isLogin(HttpServletRequest request, HttpServletResponse response) {
		return ssoService.isLogin(request, response);
	}

	public void login(HttpServletRequest request, HttpServletResponse response, String userName, String password) throws LoginException {
		ssoService.login(request, response, userName, password);
	}

	public void logout(HttpServletRequest request, HttpServletResponse response) {
		ssoService.logout(request, response);
	}
	
	protected String makeKey(String serviceName, String serviceKey){
		if(serviceKey == null){
			throw new IllegalParameterException("serviceKey is null.") ;
		}
		
		return "/" + serviceName + "/" + serviceKey ;
	}
	
	public void newAuth(WriteTranSession write, String userName, String serviceName, String serviceKey) {
		Members m = Members.buildMembers(userName, null) ;
		
		for(String s : m.getOwners()){
			AuthedService as = new AuthedService() ;
			as.setCreatedTime(new java.util.Date()) ;
			as.setEmail(s) ;
			as.setOwner(true) ;
			as.setServiceKey(serviceKey) ;
			as.setServiceName(serviceName) ;
			
			write.insert(as) ;
		}
		
		//store to KV。如果失败，让DB事务也回滚。
		this.kvStorageService.storeTo(makeKey(serviceName, serviceKey), JsonUtil.toJson(m)) ;
	}
	
	public Members getAuthedMembers(String serviceName, String serviceKey){
		String acls= kvStorageService.get(makeKey(serviceName, serviceKey)) ;
		
		if(acls == null){
			//如果KV失败，尝试从数据库恢复。
			SearchExpression se = SearchExpression.forClass(AuthedService.class, 1, 200) ;
			se.and(Terms.eq("serviceName", serviceName)).and(Terms.eq("serviceKey", serviceKey)) ;
			List<AuthedService> users = super.list(se) ;
			
			if(users.isEmpty()) return null ;
			
			LinkedList<String> owners = new LinkedList<String>() ;
			LinkedList<String> commiters = new LinkedList<String>() ;
			for(AuthedService as : users){
				if(as.isOwner()){
					owners.addLast(as.getEmail().toLowerCase()) ;
				}else{
					commiters.addLast(as.getEmail().toLowerCase()) ;
				}
			}
			
			Members m = new Members() ;
			m.setCommiters(commiters) ;
			m.setOwners(owners) ;
			
			//save to KV
			this.kvStorageService.asyncStoreTo(makeKey(serviceName, serviceKey), JsonUtil.toJson(m)) ;
			
			return m ;
		}
		
		return JsonUtil.fromJson(acls, Members.class) ;
	}
	
	/**
	 * 保存权限表
	 */
	public void storeAuthedMembers(String serviceName, String serviceKey, Members m){
		//备份一下m
		String json = JsonUtil.toJson(m) ;
		
		//store to DB
		SearchExpression se = SearchExpression.forClass(AuthedService.class, 1, 200) ;
		se.and(Terms.eq("serviceName", serviceName)).and(Terms.eq("serviceKey", serviceKey)) ;
		List<AuthedService> toDeleteUsers = super.list(se) ;
		
		List<String> toAddOwners = m.getOwners() ;
		Iterator<String> i = toAddOwners.iterator() ;
		while(i.hasNext()){
			String o = i.next() ;
			
			for(AuthedService as : toDeleteUsers){
				if(!as.isOwner()) continue ;
				
				//已经授权过的用户
				if(o.equalsIgnoreCase(as.getEmail())){
					toDeleteUsers.remove(as) ;
					i.remove() ;
					
					break ;
				}
			}
		}
		
		List<String> toAddcommiters = m.getCommiters() ;
		i = toAddcommiters.iterator() ;
		while(i.hasNext()){
			String o = i.next() ;
			
			for(AuthedService as : toDeleteUsers){
				if(as.isOwner()) continue ;
				
				//已经授权过的用户
				if(o.equalsIgnoreCase(as.getEmail())){
					toDeleteUsers.remove(as) ;
					i.remove() ;
					
					break ;
				}
			}
		}
		
		//store to db
		WriteTranSession write = this.getTransactionManager().openRWTran(false) ;
		
		try{
			for(String s : toAddOwners){
				AuthedService as = new AuthedService() ;
				as.setCreatedTime(new java.util.Date()) ;
				as.setEmail(s) ;
				as.setOwner(true) ;
				as.setServiceKey(serviceKey) ;
				as.setServiceName(serviceName) ;
				
				write.insert(as) ;
			}
			
			for(String s : toAddcommiters){
				AuthedService as = new AuthedService() ;
				as.setCreatedTime(new java.util.Date()) ;
				as.setEmail(s) ;
				as.setOwner(false) ;
				as.setServiceKey(serviceKey) ;
				as.setServiceName(serviceName) ;
				
				write.insert(as) ;
			}
			
			for(AuthedService as : toDeleteUsers){
				write.delete(as) ;
			}
			
			//store to KV。如果失败，让DB事务也回滚。
			this.kvStorageService.storeTo(makeKey(serviceName, serviceKey), json) ;
			
			write.commit() ;
		}catch(RuntimeException e){
			write.rollback() ;
			
			throw e ;
		}finally{
			write.close() ;
		}
	}
	
	public List<String> listAuthedServiceKeys(String userName, String serviceName) {
		SearchExpression se = SearchExpression.forClass(AuthedService.class, 1, 200) ;
		se.and(Terms.eq("email", userName)).and(Terms.eq("serviceName", serviceName)) ;
		se.setRowDataLoader(new RowDataLoader(){

			public Object rs2Object(ObjectMapping mapping, ResultSet rs) throws SQLException {
				return rs.getString("serviceKey");
			}
			
		}) ;
		
		return super.list(se) ;
	}

	public boolean isOwner(LoginUser loginUser, String serviceName, String serviceKey){
		Members m = getAuthedMembers(serviceName, serviceKey) ;
		
		//check table for creator
		if(m == null) return false ;
		
		return m.isOwner(loginUser.getUserName()) ;	
	}
	
	public boolean isCommiter(LoginUser loginUser, String serviceName, String serviceKey) {
		Members m = getAuthedMembers(serviceName, serviceKey) ;
		
		//check table for creator
		if(m == null) return false ;
		
		//owner也是commiter的一种
		return m.isOwner(loginUser.getUserName()) || m.isCommiter(loginUser.getUserName()) ;	
	}
	
	public void assertOwner(LoginUser loginUser, String serviceName, String serviceKey){
		if(!this.isOwner(loginUser, serviceName, serviceKey)){
			throwNoPermissionException(loginUser, serviceName, serviceKey, "Permission denied! Must be an owner!") ;
		}
	}
	
	public void assertCommiter(LoginUser loginUser, String serviceName, String serviceKey) {
		if(!this.isCommiter(loginUser, serviceName, serviceKey)){
			throwNoPermissionException(loginUser, serviceName, serviceKey, null) ;
		}
	}
	
	public void throwNoPermissionException(LoginUser loginUser, String serviceName, String serviceKey, String errorMsg) {
		throw new NoPermissionException(errorMsg == null ? "Permission denied!" : errorMsg) ;
	}

	public SSOService getSsoService() {
		return ssoService;
	}

	public void setSsoService(SSOService ssoService) {
		this.ssoService = ssoService;
	}

	public KVStorageService getKvStorageService() {
		return kvStorageService;
	}

	public void setKvStorageService(KVStorageService kvStorageService) {
		this.kvStorageService = kvStorageService;
	}

}
