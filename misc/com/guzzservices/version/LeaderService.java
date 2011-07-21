/**
 * 
 */
package com.guzzservices.version;

/**
 * 
 * Leader Election Service.
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public interface LeaderService {
	
	/**
	 * Is this machine the leader in the cluster?
	 */
	public boolean amILeader() ;

}
