/**
 * 
 */
package com.guzzservices.manager.impl;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.guzz.util.StringUtil;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class Members implements Serializable {
	
	private List<String> owners ;
	
	private List<String> commiters ;
	
	/**
	 * 通过用户填写的所有者和贡献者列表，构建成员对象。提交用户列表通过逗号或者换行，每行代表一个用户，和google code members策略一样。
	 * <br>
	 * 如果一个用户同时处在ownersList和commitersList，算作owner，同时自动从commitersList中删除。
	 */
	public static Members buildMembers(String ownersList, String commitersList){
		Members m = new Members() ;
		
		List<String> owners = new LinkedList<String>() ;
		if(ownersList != null){
			ownersList = StringUtil.replaceString(ownersList, ",", " ") ;
			ownersList = StringUtil.replaceString(ownersList, "，", " ") ;
			ownersList = StringUtil.squeezeWhiteSpace(ownersList) ;
			String[] os = StringUtil.splitString(ownersList, " ") ;
			
			for(String o : os){
				o = o.trim().toLowerCase() ;
				if(StringUtil.notEmpty(o) && !owners.contains(o)){
					owners.add(o) ;
				}
			}
		}
		
		List<String> commiters = new LinkedList<String>() ;
		if(commitersList != null){
			commitersList = StringUtil.replaceString(commitersList, ",", " ") ;
			commitersList = StringUtil.replaceString(commitersList, "，", " ") ;
			commitersList = StringUtil.squeezeWhiteSpace(commitersList) ;
			String[] os = StringUtil.splitString(commitersList, " ") ;
			
			for(String o : os){
				o = o.trim().toLowerCase() ;
				if(StringUtil.notEmpty(o) && !commiters.contains(o) && !owners.contains(o)){
					commiters.add(o) ;
				}
			}
		}
		
		m.owners = owners ;
		m.commiters = commiters ;
		
		return m ;
	}
	
	/**
	 * 是否有owner
	 */
	public boolean hasOwner(){
		return !owners.isEmpty() ;
	}
	
	public String getOwnersString(){
		if(this.owners == null || this.owners.isEmpty()) return "" ;
		
		StringBuilder sb = new StringBuilder() ;
		for(String o : this.owners){
			sb.append(o).append(", \n") ;
		}
		
		return sb.toString() ;
	}
	
	public String getCommitersString(){
		if(this.commiters == null || this.commiters.isEmpty()) return "" ;
		
		StringBuilder sb = new StringBuilder() ;
		for(String o : this.commiters){
			sb.append(o).append(", \n") ;
		}
		
		return sb.toString() ;
	}
	
	public boolean isOwner(String userName){
		return owners.contains(userName) ;
	}
	
	public boolean isCommiter(String userName){
		return commiters.contains(userName) ;
	}

	public final List<String> getOwners() {
		return owners;
	}

	public final void setOwners(List<String> owners) {
		this.owners = owners;
	}

	public final List<String> getCommiters() {
		return commiters;
	}

	public final void setCommiters(List<String> commiters) {
		this.commiters = commiters;
	}

}
