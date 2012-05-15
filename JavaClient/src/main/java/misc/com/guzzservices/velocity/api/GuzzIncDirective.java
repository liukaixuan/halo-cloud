/**
 * 
 */
package com.guzzservices.velocity.api;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.guzz.GuzzContext;
import org.guzz.Service;
import org.guzz.service.core.SlowUpdateService;
import org.guzz.taglib.db.SummonTag;
import org.guzz.util.Assert;

import com.guzzservices.velocity.api.GuzzBoundaryDirective.BoundaryChain;

/**
 * 
 * See {@link SummonTag}.
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class GuzzIncDirective extends Directive {

	protected GuzzContext guzzContext ;
	
	protected SlowUpdateService slowUpdateService ;

	public int getType() {
		return LINE ;
	}

	public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
		if(node.jjtGetNumChildren() != 1){
			throw new RuntimeException(getName() + " only and must accept one Map parameter!") ;
		}
		
		if(this.slowUpdateService == null){
			throw new ResourceNotFoundException("slowUpdateService is not available.") ;
		}
		
		Map params = (Map) node.jjtGetChild(0).value(context) ;
		
		Object business = params.get("business") ;
		String updatePropName = (String) params.get("updatePropName") ;
		int count = LangUtil.getIntParam(params, "count", 1) ;
		Serializable pkValue = (Serializable) params.get("pkValue") ;
		Object tableCondition = params.get("tableCondition") ;
		
		Assert.assertResouceNotNull(business, "parameter [business] in Map is requried.") ;
		Assert.assertResouceNotNull(updatePropName, "parameter [updatePropName] in Map is requried.") ;
		Assert.assertResouceNotNull(pkValue, "parameter [pkValue] in Map is requried.") ;
		
		String ghostName ;
		
		if(business instanceof java.lang.String){
			ghostName = (String) business ;
		}else{
			ghostName = business.getClass().getName() ;
		}
		
		if(tableCondition == null){
			BoundaryChain chain = (BoundaryChain) context.get(GuzzBoundaryDirective.BOUNDARY_CONTEXT_NAME) ;
			if(chain != null){
				tableCondition = chain.getTableCondition() ;
			}
		}
		
		this.slowUpdateService.updateCount(ghostName, tableCondition, updatePropName, pkValue, count) ;
        
        return true;
	}
	public void init(RuntimeServices rs, InternalContextAdapter context, Node node) throws TemplateInitException {
		super.init(rs, context, node);
		
		this.guzzContext = (GuzzContext) rs.getApplicationAttribute(SummonDirective.GUZZ_CONTEXT_NAME) ;
		this.slowUpdateService = (SlowUpdateService) this.guzzContext.getService(Service.FAMOUSE_SERVICE.SLOW_UPDATE) ;
	}

	public String getName() {
		return "guzzInc" ;
	}
}
