package com.guzzservices.serviceimpl;

import java.util.HashMap;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.guzz.exception.ServiceExecutionException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.guzzservices.mail.SendMailService;
import com.guzzservices.mail.impl.RemoteSendMailServiceImpl;
import com.guzzservices.rpc.server.CommandHandler;
import com.guzzservices.rpc.server.CommandServerService;
import com.guzzservices.rpc.util.JsonUtil;

/**
 * 
 * 使用spring 的 javamail发送邮件。
 * 
 * @author liu kaixuan(liukaixuan@gmail.com)
 */
public class SpringSendMailProvider implements SendMailService, CommandHandler {
	private static final Log log = LogFactory.getLog(SpringSendMailProvider.class) ;
	
	private JavaMailSender mailSender;
	
	public boolean sendPlainMail(String from, String to, String subject, String content) {
		SimpleMailMessage msg=new SimpleMailMessage();  
		msg.setFrom(from);  
		msg.setTo(to);  
		msg.setSubject(subject);  
		msg.setText(content);  
		
		try{
			mailSender.send(msg);
		}catch(Exception e){
			log.warn("mail sent failed. trying to send to [" + to + "]. error message is:" + e.getMessage()) ;
			return false ;
		}
		  
		return true;
	}
	
	public boolean sendHtmlMail(final String from, final String to, final String subject, final String content) {
		try{
			mailSender.send(new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws MessagingException {
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
					message.setFrom(from);
					message.setTo(to);
					message.setSubject(subject);
					message.setText(content, true);
				}
			});
		}catch(Exception e){
			log.warn("mail sent failed. trying to send to [" + to + "]. error message is:" + e.getMessage()) ;
			return false ;
		}

		return true;
	}

	public String executeCommand(String command, String param) throws Exception {
		boolean result = false ;
		
		if(RemoteSendMailServiceImpl.SEND_HTML_MAIL.equals(command)){
			HashMap<String, String> params = JsonUtil.fromJson(param, HashMap.class) ;
			
			result = this.sendHtmlMail(
					params.get("from"), 
					params.get("to"), 
					params.get("subject"), 
					params.get("content")
					) ;
		}else if(RemoteSendMailServiceImpl.SEND_PLAIN_TEXT_MAIL.equals(command)){
			HashMap<String, String> params = JsonUtil.fromJson(param, HashMap.class) ;
			
			result = this.sendPlainMail(
					params.get("from"), 
					params.get("to"), 
					params.get("subject"), 
					params.get("content")
					) ;
		}else{
			throw new ServiceExecutionException("unknown command to send mail service:" + command) ;
		}
		
		return String.valueOf(result) ;
	}

	public byte[] executeCommand(String command, byte[] param) throws Exception {
		throw new ServiceExecutionException("not supported") ;
	}

	public void setCommandServerService(CommandServerService commandServerService) {
		commandServerService.addCommandHandler(RemoteSendMailServiceImpl.SEND_HTML_MAIL, this) ;
		commandServerService.addCommandHandler(RemoteSendMailServiceImpl.SEND_PLAIN_TEXT_MAIL, this) ;
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

}
