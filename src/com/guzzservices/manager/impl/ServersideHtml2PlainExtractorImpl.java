/**
 * 
 */
package com.guzzservices.manager.impl;

import com.guzzservices.rpc.server.CommandHandler;
import com.guzzservices.rpc.server.CommandServerService;
import com.guzzservices.rpc.util.JsonUtil;
import com.guzzservices.text.PlainExtractResult;
import com.guzzservices.text.impl.HtmlExtractRequest;
import com.guzzservices.text.impl.LocalHtmlParserExtractServiceImpl;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class ServersideHtml2PlainExtractorImpl implements CommandHandler {
	
	private LocalHtmlParserExtractServiceImpl htmlParserExtractService = new LocalHtmlParserExtractServiceImpl() ;
	
	public String executeCommand(String command, String param) throws Exception {
		HtmlExtractRequest r = JsonUtil.fromJson(param, HtmlExtractRequest.class) ;
		
		PlainExtractResult result = htmlParserExtractService.extractTextWithImage(r.getHtmlText(), r.getResultLengthLimit(), r.getImageCountLimit(), r.getTips()) ;
		
		return JsonUtil.toJson(result) ;
	}

	public byte[] executeCommand(String command, byte[] param) throws Exception {
		throw new NoSuchMethodException("not supported!") ;
	}

	public void setCommandServerService(CommandServerService commandServerService) {
		commandServerService.addCommandHandler(HtmlExtractRequest.COMMAND_EXTRACT_HTML_TO_PLAIN, this) ;
	}

	public void setIgnoreImages(String[] ignoreImages) {
		this.htmlParserExtractService.setIgnoreImages(ignoreImages) ;
	}

}
