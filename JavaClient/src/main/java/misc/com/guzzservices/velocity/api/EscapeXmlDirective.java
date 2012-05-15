/**
 * 
 */
package com.guzzservices.velocity.api;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.guzz.taglib.util.TagSupportUtil;

/**
 * 
 * Safe html/xml output
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class EscapeXmlDirective extends Directive {

	public int getType() {
		return LINE ;
	}

	public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
		if(node.jjtGetNumChildren() != 1){
			throw new RuntimeException(getName() + " only and must accept one parameter!") ;
		}
		
		Object param = node.jjtGetChild(0).value(context) ;
		
		//如果为null，则什么都不输出。
		if(param != null){
			writer.append(TagSupportUtil.escapeXml(String.valueOf(param))) ;
		}
        
        return true;
	}

	public String getName() {
		return "escapeXml" ;
	}
}
