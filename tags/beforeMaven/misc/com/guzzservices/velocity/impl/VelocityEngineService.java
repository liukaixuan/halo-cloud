/**
 * 
 */
package com.guzzservices.velocity.impl;

import java.io.File;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.guzz.GuzzContext;
import org.guzz.exception.InvalidConfigurationException;
import org.guzz.io.FileResource;
import org.guzz.service.AbstractService;
import org.guzz.service.ServiceConfig;
import org.guzz.util.PropertyUtil;
import org.guzz.util.StringUtil;
import org.guzz.web.context.GuzzContextAware;

import com.guzzservices.velocity.VelocityService;
import com.guzzservices.velocity.api.GuzzAddInLimitDirective;
import com.guzzservices.velocity.api.GuzzAddLimitDirective;
import com.guzzservices.velocity.api.GuzzBoundaryDirective;
import com.guzzservices.velocity.api.GuzzCountDirective;
import com.guzzservices.velocity.api.GuzzGetDirective;
import com.guzzservices.velocity.api.GuzzIncDirective;
import com.guzzservices.velocity.api.GuzzListDirective;
import com.guzzservices.velocity.api.GuzzPageDirective;
import com.guzzservices.velocity.api.SummonDirective;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class VelocityEngineService extends AbstractService implements VelocityService, GuzzContextAware {
	
	private String velocityPropertiesFile ;
	private boolean enableDBAccess = true ;
	private GuzzContext guzzContext ;
	protected VelocityEngine ve ;

	public String translate(Map<String, Object> data, String templateName) throws Exception {
		Template template = ve.getTemplate(templateName);
		
		VelocityContext context = new VelocityContext(data) ;

		/* lets render a template */
		StringWriter w = new StringWriter() ;
		template.merge(context, w) ;
		
		return w.toString() ;
	}
	
	public String translateText(Map<String, Object> data, String logTitle, String text) throws Exception {
		VelocityContext context = new VelocityContext(data) ;

		/* lets render a template */
		StringWriter w = new StringWriter() ;
		
		ve.evaluate(context, w, logTitle, text) ;
		
		return w.toString() ;
	}

	public boolean configure(ServiceConfig[] scs) {
		if(scs.length == 0){
			log.error("missing config, the VelocityLocalTemplateService is not started!") ;
			return false ;
		}
		
		this.velocityPropertiesFile = scs[0].getProps().getProperty("velocityPropertiesFile") ;
		this.enableDBAccess = StringUtil.toBoolean(scs[0].getProps().getProperty("enableDBAccess"), true) ;
		
		return true;
	}

	public boolean isAvailable() {
		return this.ve != null ;
	}

	public void shutdown() {
		this.ve = null ;
	}

	public void startup() {
		Properties p = null ;
		
		if(StringUtil.notEmpty(this.velocityPropertiesFile)){
			FileResource fr = new FileResource(this.velocityPropertiesFile) ;
			
			File f = fr.getFile() ;
			if(log.isInfoEnabled()){
				log.info("loading velocity properties file from:" + f) ;
			}
			
			p = PropertyUtil.loadProperties(f) ;
			fr.close() ;
		}else{
			p = new Properties() ;
		}
		
		if(this.enableDBAccess){
			String guzzDirectives = GuzzAddInLimitDirective.class.getName()
			+ ", " + GuzzAddLimitDirective.class.getName()
			+ ", " + GuzzBoundaryDirective.class.getName()
			+ ", " + GuzzCountDirective.class.getName()
			+ ", " + GuzzGetDirective.class.getName()
			+ ", " + GuzzIncDirective.class.getName()
			+ ", " + GuzzListDirective.class.getName()
			+ ", " + GuzzPageDirective.class.getName() ;
			
			String oldDirective = p.getProperty("userdirective") ;
			
			if(StringUtil.isEmpty(oldDirective)){
				p.setProperty("userdirective", guzzDirectives) ;
			}else{
				p.setProperty("userdirective", oldDirective + ", " + guzzDirectives) ;
			}
		}

		this.ve = new VelocityEngine();
		ve.setApplicationAttribute(SummonDirective.GUZZ_CONTEXT_NAME, this.guzzContext) ;

		try {
			ve.init(p) ;
		} catch (Exception e) {
			throw new InvalidConfigurationException(e) ;
		}
	}

	public void setGuzzContext(GuzzContext guzzContext) {
		this.guzzContext = guzzContext ;
	}

}
