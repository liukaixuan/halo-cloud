/**
 * 
 */
package com.guzzservices.rpc.server;

import java.net.InetSocketAddress;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class ClientInfo {
	
	private String hostName ;
	
	private String IP ;
	
	private int port ;
	
	public ClientInfo(InetSocketAddress addr){
		this.hostName = addr.getHostName() ;
		this.port = addr.getPort() ;
		if(addr.getAddress() != null){
			this.IP = addr.getAddress().getHostAddress() ;
		}
	}

	public String getHostName() {
		return hostName;
	}

	public String getIP() {
		return IP;
	}

	public int getPort() {
		return port;
	}

}
