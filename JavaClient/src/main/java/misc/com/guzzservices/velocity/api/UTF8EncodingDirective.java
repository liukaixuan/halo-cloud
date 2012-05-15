/**
 * 
 */
package com.guzzservices.velocity.api;

import java.io.IOException;
import java.io.Writer;
import java.net.URLEncoder;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

/**
 * 
 * 将输入内容按照UTF-8编码后输出。
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class UTF8EncodingDirective extends Directive {

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
			writer.append(URLEncoder.encode(String.valueOf(param), "UTF-8")) ;
		}
        
        return true;
	}

	public String getName() {
		return "utf8encoding" ;
	}
}
