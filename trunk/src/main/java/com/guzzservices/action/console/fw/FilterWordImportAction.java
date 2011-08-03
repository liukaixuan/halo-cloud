/**
 * FilterWordImportAction.java. created on 2006-8-8  
 */
package com.guzzservices.action.console.fw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guzz.util.CloseUtil;
import org.guzz.util.ViewFormat;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.guzzservices.action.vo.FilterWordImportForm;
import com.guzzservices.business.FilterWord;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.IFilterWordManager;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.sso.LoginUser;

/**
 * 过滤词导入功能。导入的过滤词存在于文本文件中，每个过滤词占用一行，通过参数给定将导入的组名称，-1表示导入到全局过滤词组。
 * 
 * @author liu kaixuan
 */
public class FilterWordImportAction extends SimpleFormController {
	
	private int maxUploadSize = 1024 * 1024 ; //1M
	private String fileEncoding = "UTF-8" ;
	private IFilterWordManager filterWordManager ;
	private ISessionManager sessionManager ;

	public FilterWordImportAction() {
		this.setCommandName("filterWordImportForm") ;
	}
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException, IOException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor()) ;
	}

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		FilterWordImportForm info = (FilterWordImportForm) cmd ;
		
		MultipartFile mfile =  info.getUploadFile() ;
		String groupId = info.getGroupId() ;
		int level = info.getLevel() ;
		
		Set<String> failed = new HashSet<String>() ;
		Set<String> successed = new HashSet<String>() ;
		
		//验证权限
		this.sessionManager.assertCommiter(loginUser, Constants.serviceName.FILTER_WORD, groupId) ;
						
		InputStreamReader isr = null ;
		BufferedReader reader = null ;	
		
		Set<FilterWord> words = new HashSet<FilterWord>() ;
		
		try{
			isr = new InputStreamReader(mfile.getInputStream(), fileEncoding) ;
			reader = new BufferedReader(isr) ;
			String line = null ;
			
			while((line = reader.readLine()) != null){
				line = line.trim() ;
				if(line.length() == 0 || line.indexOf("#") == 0 || line.indexOf("#") == 1) continue ;
				
				if(successed.contains(line)) continue ;
				if(failed.contains(line)) continue ;
				
				if(filterWordManager.getByWord(groupId, line) != null){
					failed.add(line) ;
				}else{
					//添加到数据库中。
					FilterWord word = new FilterWord() ;
					word.setCreatedTime(new Date()) ;
					word.setGroupId(groupId) ;
					word.setWord(line) ;
					word.setLevel(level) ;
//					filterWordManager.save(word) ;
					words.add(word) ;
					
					successed.add(line) ;
				}
			}
		}finally{
			CloseUtil.close(reader) ;
			CloseUtil.close(isr) ;
		}
		
		filterWordManager.addImport(groupId, words) ;
		
		HashMap<String, Object> model = new HashMap<String, Object>() ;
		model.put("failed", failed) ;
		model.put("successed", successed) ;
		model.put("groupId", groupId) ;
		
		return new ModelAndView(getSuccessView(), model) ;
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		return new FilterWordImportForm() ;
	}

	protected void onBindAndValidate(HttpServletRequest request, Object arg1, BindException errors) throws Exception {
		super.onBindAndValidate(request, arg1, errors);
		FilterWordImportForm form = (FilterWordImportForm) arg1 ;
		
		if(form.getUploadFile() == null || form.getUploadFile().isEmpty()){ //没有上传文件
			errors.rejectValue("uploadFile", "filterword.error.uploadFile.empty", "请选择过滤词文件") ;
		}else if(form.getUploadFile().getSize() > maxUploadSize){
			errors.rejectValue("uploadFile", "filterword.error.uploadFile.empty", "过滤词文件太大，最大：" + ViewFormat.formatFileLength(maxUploadSize)) ;
		}
	}

	public String getFileEncoding() {
		return fileEncoding;
	}

	public void setFileEncoding(String fileEncoding) {
		this.fileEncoding = fileEncoding;
	}

	public IFilterWordManager getFilterWordManager() {
		return filterWordManager;
	}

	public void setFilterWordManager(IFilterWordManager filterWordManager) {
		this.filterWordManager = filterWordManager;
	}

	public int getMaxUploadSize() {
		return maxUploadSize;
	}

	public void setMaxUploadSize(int maxUploadSize) {
		this.maxUploadSize = maxUploadSize;
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
}
