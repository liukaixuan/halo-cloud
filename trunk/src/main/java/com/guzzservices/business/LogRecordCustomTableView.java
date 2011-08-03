/**
 * 
 */
package com.guzzservices.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.guzz.orm.AbstractCustomTableView;
import org.guzz.orm.mapping.POJOBasedObjectMapping;
import org.guzz.orm.rdms.TableColumn;
import org.guzz.web.context.ExtendedBeanFactory;
import org.guzz.web.context.ExtendedBeanFactoryAware;

import com.guzzservices.manager.ILogAppManager;

/**
 * 
 * 每个日志应用一张表。通过Manager的版本控制ORM缓存。
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class LogRecordCustomTableView extends AbstractCustomTableView implements ExtendedBeanFactoryAware {
	
	private ILogAppManager logAppManager ;
	
	private Map<Integer, MappingHolder> cachedMapping = new HashMap<Integer, MappingHolder>() ;
	
	protected int getTableCondition(Object tableCondition){
		return ((Integer) tableCondition).intValue() ;
	}

	protected void initCustomTableColumn(POJOBasedObjectMapping mapping, Object tableCondition) {
		int appId = getTableCondition(tableCondition) ;
		
		List<LogCustomProperty> properties = logAppManager.listLogCustomProperties(appId) ;
		
		for(LogCustomProperty p : properties){
			TableColumn tc = super.createTableColumn(mapping, p.getPropName(), p.getColName(), p.getDataType(), null) ;
			super.addTableColumn(mapping, tc) ;
		}
	}
	
	public POJOBasedObjectMapping getRuntimeObjectMapping(Object tableCondition) {
		int appId = getTableCondition(tableCondition) ;
		
		MappingHolder holder = this.cachedMapping.get(appId) ;
		int newVersion = this.logAppManager.getLastestVersion(appId) ;
		
		//不需要非常严格的版本和读取一致性，基本一致就能达到要求。
		if(holder == null || holder.version != newVersion){
			POJOBasedObjectMapping newMap = super.createRuntimeObjectMapping(tableCondition) ;
			holder = new MappingHolder(newMap, newVersion) ;
			
			this.cachedMapping.put(appId, holder) ;
		}
		
		return holder.mapping ;
	}

	public Object getCustomPropertyValue(Object beanInstance, String propName) {
		LogRecord record = (LogRecord) beanInstance ;
		
		return record.getOtherProps().get(propName) ;
	}

	public void setCustomPropertyValue(Object beanInstance, String propName, Object value) {
		LogRecord record = (LogRecord) beanInstance ;
		
		record.getOtherProps().put(propName, value) ;
	}

	public String toTableName(Object tableCondition) {
		return super.getConfiguredTableName() + "_" + getTableCondition(tableCondition) ;
	}

	public void setExtendedBeanFactory(ExtendedBeanFactory extendedBeanFactory) {
		this.logAppManager = (ILogAppManager) extendedBeanFactory.getBean("logAppManager") ;
	}
	
	static class MappingHolder{
		
		public MappingHolder(POJOBasedObjectMapping mapping, int version){
			this.mapping = mapping ;
			this.version = version ;
		}
		
		public final POJOBasedObjectMapping mapping ;
		
		public final int version ;
		
	}

}
