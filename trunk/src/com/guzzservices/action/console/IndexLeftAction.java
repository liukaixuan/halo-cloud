package com.guzzservices.action.console;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.guzzservices.manager.ISessionManager;
import com.guzzservices.sso.LoginUser;
import com.guzzservices.util.tree.DHtmlTree;

/**
 * 
 */
public class IndexLeftAction implements Controller {
	
	private ISessionManager sessionManager ;

	private String successView ;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		boolean isSuperAdmin = loginUser.isLogin() ;
		
		DHtmlTree root = new DHtmlTree("服务管理(" + loginUser.getDisplayName() + ")");

        DHtmlTree node = new DHtmlTree("安全服务") ;
        root.addSon(node);
        node.addSon(new DHtmlTree("内容过滤","filterWordGroupList.do"));
        node.addSon(new DHtmlTree("IP黑名单","comingsoon.jsp"));
        node.addSon(new DHtmlTree("用户黑名单","comingsoon.jsp"));
        
        node = new DHtmlTree("通用计算") ;
        root.addSon(node);
        node.addSon(new DHtmlTree("IP反查","IPService.jsp"));
        
        node = new DHtmlTree("系统管理") ;
        root.addSon(node);
        node.addSon(new DHtmlTree("应用配置","configGroupList.do	"));
        node.addSon(new DHtmlTree("排行榜","stat/statItemGroupList.do"));
        node.addSon(new DHtmlTree("统计","comingsoon.jsp"));
        node.addSon(new DHtmlTree("任务调度","task/taskGroupList.do"));
        
        root.addSon(new DHtmlTree("退出","../logout.do", "_top"));
        
        Map model = new HashMap();
        model.put("htmlTree", root);
        
		return new ModelAndView(getSuccessView(), model) ;
	}

	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
	
}
