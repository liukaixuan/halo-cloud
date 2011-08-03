package com.guzzservices.action.vo;


/**
 * 
 * 
 * @author liu kaixuan
 */
public class AuthMembersForm {
	
	private String owners ;
	
	private String commiters ;

	public AuthMembersForm(String owners, String commiters) {
		this.owners = owners ;
		this.commiters = commiters ;
	}

	public String getOwners() {
		return owners;
	}

	public void setOwners(String owners) {
		this.owners = owners;
	}

	public String getCommiters() {
		return commiters;
	}

	public void setCommiters(String commiters) {
		this.commiters = commiters;
	}
}
