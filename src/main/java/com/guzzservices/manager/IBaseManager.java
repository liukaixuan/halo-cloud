package com.guzzservices.manager;

import java.io.Serializable;


/**
 * 
 * 
 * @author liu kaixuan
 */
public interface IBaseManager<T> {
		
	public T getForRead(int id) ;
	
	public T getForUpdate(int id) ;
	
	public Serializable insert(Object obj) ;
	
	public void update(Object obj) ;
	
	public void delete(Object obj) ;	

}
