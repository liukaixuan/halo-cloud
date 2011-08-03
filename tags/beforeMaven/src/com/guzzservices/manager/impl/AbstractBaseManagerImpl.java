package com.guzzservices.manager.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.guzz.dao.GuzzBaseDao;

import com.guzzservices.manager.IBaseManager;

/**
 * 
 * 
 * @author liu kaixuan
 */
public abstract class AbstractBaseManagerImpl<T> extends GuzzBaseDao implements IBaseManager<T> {
	
	protected final Class<T> entityClass ;
	
	{
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();   
		entityClass =  (Class)params[0];   
	}
	
	public T getForRead(int id) {
		return (T) super.getForRead(entityClass, id) ;
	}
	
	public T getForUpdate(int id) {
		return (T) super.getForUpdate(entityClass, id) ;
	}
	
}
