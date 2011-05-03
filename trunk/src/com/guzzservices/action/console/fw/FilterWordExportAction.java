/**
 * FilterWordExportAction.java. created on 2006-8-9  
 */
package com.guzzservices.action.console.fw;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.guzzservices.business.FilterWord;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.IFilterWordManager;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.sso.LoginUser;

/**
 * 过滤词导出功能。
 * 
 * @author liu kaixuan
 */
public class FilterWordExportAction implements Controller {

	private IFilterWordManager filterWordManager ;
	private ISessionManager sessionManager ;
	private String fileEncoding = "UTF-8" ;
	private String fileName = "filterWords.txt" ;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		String groupId = request.getParameter("groupId") ;
		
		//check permission
		this.sessionManager.assertOwner(loginUser, Constants.serviceName.FILTER_WORD, groupId) ;

		List<FilterWord> words = filterWordManager.listBadWordsByGroup(groupId) ;
		
		
		return exportAsText(response, words, groupId) ;
	}
	
	protected ModelAndView exportAsText(HttpServletResponse response, List<FilterWord> words, String groupId) throws IOException{
		response.setContentType("text/plain; charset=" + fileEncoding) ;
		response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, fileEncoding) + "\"") ;
		
		PrintWriter writer = response.getWriter() ;
		
		writer.println("#filter words exported on " + new Date()) ;
		writer.println() ;
		
		for(int i = 0 ; i < words.size() ; i++){
			FilterWord word2 = (FilterWord) words.get(i) ;
			writer.println(word2.getWord()) ;
		}
		
		return null ;
	}

	public IFilterWordManager getFilterWordManager() {
		return filterWordManager;
	}

	public void setFilterWordManager(IFilterWordManager filterWordManager) {
		this.filterWordManager = filterWordManager;
	}

	public String getFileEncoding() {
		return fileEncoding;
	}

	public void setFileEncoding(String fileEncoding) {
		this.fileEncoding = fileEncoding;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

}
