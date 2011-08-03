/**
 * 
 */
package com.guzzservices.action.open;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.guzzservices.business.User;
import com.guzzservices.manager.IUserManager;
import com.guzzservices.util.PasswordUtil;
import com.guzzservices.util.ValidationUtil;


/**
 * 
 * user register
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class UserAction extends SimpleFormController {

	private IUserManager userManager;

	public UserAction(){
		this.setCommandName("user") ;
	}
		
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object form, BindException error) throws Exception {
		User user = (User) form ;
		
		user.setAdmin(false) ;
		user.setCreatedTime(new Date()) ;
		user.setPassword(PasswordUtil.md5(user.getPassword())) ;
		
		this.userManager.addUser(user) ;
		
		return new ModelAndView(getSuccessView());
	}

	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
		super.onBindAndValidate(request, command, errors);
		
		User user = (User) command ;
		if(this.userManager.getByEmail(user.getEmail()) != null){
			errors.rejectValue("email", null, null, "邮箱已经被注册过!");
		}
		
		ValidationUtil.rejectIfNotEmail(errors, "email", null, "邮箱格式错误!");
		ValidationUtil.rejectIfEmptyOrWhitespace(errors, "nickName", null, "昵称不能为空!");
		ValidationUtil.rejectIfEmptyOrWhitespace(errors, "password", null, "密码不能为空!");
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		return new User() ;
	}

	public IUserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(IUserManager userManager) {
		this.userManager = userManager;
	}

}
