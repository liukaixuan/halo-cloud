/**
 * 
 */
package com.guzzservices.velocity.api;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.guzz.taglib.db.GhostAddLimitTag;

import com.guzzservices.velocity.api.GuzzBoundaryDirective.BoundaryChain;

/**
 * 
 * See {@link GhostAddLimitTag}.
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class GuzzAddLimitDirective extends Directive {

	public int getType() {
		return LINE ;
	}

	public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
		if(node.jjtGetNumChildren() != 2){
			throw new RuntimeException(getName() + " accepts 2 parameter. The first is a boolean value indicating whether or not to add this conditon; the second is a List containing your conditions!") ;
		}
		
		Boolean test = (Boolean) node.jjtGetChild(0).value(context) ;
		if(Boolean.FALSE.equals(test)){ //ignore this condition.
			return true ;
		}
		
		Collection conditions = (Collection) node.jjtGetChild(1).value(context) ;
		
		BoundaryChain chain = (BoundaryChain) context.get(GuzzBoundaryDirective.BOUNDARY_CONTEXT_NAME) ;
		if(chain == null){
			throw new ParseErrorException(getName() + " must be resides inside a guzzBoundary directive.") ;
		}
		
		chain.addLimitConditions(conditions) ;
        
        return true;
	}
	public void init(RuntimeServices rs, InternalContextAdapter context, Node node) throws TemplateInitException {
		super.init(rs, context, node);
	}

	public String getName() {
		return "guzzAddLimit" ;
	}
}
