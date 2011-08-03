package com.guzzservices.business;

import java.util.Arrays;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Transient;

import org.guzz.annotations.GenericGenerator;
import org.guzz.annotations.Parameter;
import org.guzz.annotations.Table;
import org.guzz.exception.IllegalParameterException;
import org.guzz.util.StringUtil;
import org.guzz.util.ViewFormat;

/**
 * 系统配置信息。
 * 
 * @author liukaixuan
 */

@javax.persistence.Entity 
@Table(name="gs_configuration")
public class Configuration{

	public static final String TYPE_BOOLEAN = "boolean" ;
	public static final String TYPE_STRING = "string" ;
	public static final String TYPE_TEXT = "text" ;
	public static final String TYPE_INT = "int" ;
	public static final String TYPE_SHORT = "short" ;
	public static final String TYPE_LONG = "long" ;
	public static final String TYPE_FLOAT = "float" ;
	public static final String TYPE_DOUBLE = "double" ;
	
	private String id ;
	
	/**
	 * 参数名称
	 */
	private String parameter ;
	
	private String name ;
	
	/**
	 * 参数值
	 */
	private String value ;
	
	private String groupId ;
	
	/**应该存储的数据类型，必须为string,int,float一种*/
	private String type = Configuration.TYPE_STRING ;
	
	/**有效的几种数据，用;分开。如果为空表示可以为数据*/
	private String validValues ;
	
	private Date createdTime ;
	
	private String description ;
	
	@javax.persistence.Id
	@GenericGenerator(name="randomGen", strategy="random", parameters={@Parameter(name="length", value="64") })
	@GeneratedValue(generator="randomGen")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValidValues() {
		return validValues;
	}
	
	@Transient
	public String[] getValidValuesArray() {
		return ViewFormat.splitKeywords(validValues) ;
	}
	
	/**配置是否是枚举的几种数据*/
	@Transient
	public boolean getEnumValues(){
		return !StringUtil.isEmpty(validValues) ;
	}

	public void setValidValues(String validValues) {
		this.validValues = validValues;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupName) {
		this.groupId = groupName;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Object toTypedValue(){
		if(Configuration.TYPE_INT.equals(getType())){
			return Integer.parseInt(this.value) ;
		}
		
		if(Configuration.TYPE_FLOAT.equals(getType())){
			return Float.parseFloat(this.value) ;
		}
		
		if(Configuration.TYPE_DOUBLE.equals(getType())){
			return Double.parseDouble(this.value) ;
		}
		
		if(Configuration.TYPE_BOOLEAN.equals(getType())){
			return Boolean.valueOf(this.value) ;
		}
		
		if(Configuration.TYPE_SHORT.equals(getType())){
			return Short.parseShort(this.value) ;
		}
		
		if(Configuration.TYPE_LONG.equals(getType())){
			return Long.parseLong(this.value) ;
		}
		
		return this.value ;
	}
	
	public void assertValueValid(String newValue) throws IllegalParameterException{
		//验证有效性
		//1。验证数据类型
		if(Configuration.TYPE_INT.equals(getType())){
			try{
				Integer.parseInt(newValue) ;
			}catch(Exception e){
				throw new IllegalParameterException("config [" + name + "] must be a integer.") ;
			}
		}
		
		if(Configuration.TYPE_SHORT.equals(getType())){
			try{
				Short.parseShort(newValue) ;
			}catch(Exception e){
				throw new IllegalParameterException("config [" + name + "] must be a short.") ;
			}
		}
		
		if(Configuration.TYPE_LONG.equals(getType())){
			try{
				Long.parseLong(newValue) ;
			}catch(Exception e){
				throw new IllegalParameterException("config [" + name + "] must be a long.") ;
			}
		}
		
		if(Configuration.TYPE_FLOAT.equals(getType())){
			try{
				Float.parseFloat(newValue) ;
			}catch(Exception e){
				throw new IllegalParameterException("config [" + name + "] must be a float.") ;
			}
		}
		
		if(Configuration.TYPE_DOUBLE.equals(getType())){
			try{
				Double.parseDouble(newValue) ;
			}catch(Exception e){
				throw new IllegalParameterException("config [" + name + "] must be a double.") ;
			}
		}
		
		if(Configuration.TYPE_BOOLEAN.equals(getType())){
			try{
				Boolean.parseBoolean(newValue) ;
			}catch(Exception e){
				throw new IllegalParameterException("config [" + name + "] must be a boolean. true or false") ;
			}
		}
		
		//2。验证是可选值中的一个。
		if(getEnumValues()){ //只能是其中的一个
			String[] values = getValidValuesArray() ;
			boolean m_bPassed = false ;
			for(int i = 0 ; i < values.length ; i++){
				String m_sValue = values[i] ;
				if(m_sValue.equals(newValue)){
					m_bPassed = true ;
					break ;
				}
			}
			
			if(!m_bPassed){ //无效参数
				throw new IllegalParameterException("config [" + name + "] must be one of:" + Arrays.asList(values)) ;
			}
		}
	}
	
}
