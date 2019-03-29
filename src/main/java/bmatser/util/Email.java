package bmatser.util;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import freemarker.template.Template;

/**
 * 邮件服务接口
 * @author felx
 * 2017-12-22
 */
public class Email {

	private static JavaMailSender mailSender;
	private static SimpleMailMessage simpleMailMessage;
	private static FreeMarkerConfigurer freeMarkerConfigurer;
	private static TaskExecutor taskExecutor;

	/**
	 * 发送文本邮件
	 * @param subject 邮件主题
	 * @param content 邮件主题内容
	 * @param address 收件人Email地址,多个地址通过英文半角逗号(,)分隔
	 * @return boolean 是否发送成功
	 */
	public static boolean sendTextMail(String address, String subject, String content) {
		try{
			simpleMailMessage.setSubject(subject);
			simpleMailMessage.setTo(address.split(","));
			simpleMailMessage.setText(content);
			mailSender.send(simpleMailMessage);
			return true;
		} catch (MailException e) {
			return false;
		} 
	}

	/**
	 * 生成html模板字符串
	 * @param data 存储动态数据的map
	 * @return htmlText 返回生成html模板字符串
	 */
	private static String getMailText(Map<String, Object> data, String templateName) {
		String htmlText = "";
		try {
			Template tpl = freeMarkerConfigurer.getConfiguration().getTemplate(
					templateName);
			htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(tpl,
					data);
		} catch (Exception e) {
		}
		return htmlText;
	}

	/**
	 * 发送HTML邮件
	 * @param data 存储动态数据的map
	 * @param address 邮件地址, 多个地址通过英文半角逗号(,)分隔
	 * @param subject 邮件主题
	 * @return boolean 邮件发送状态（true：成功  false：失败）
	 */
	public static boolean sendHtmlMail(Map<String, Object> data, String address,
			String subject, String templateName) {
		try{
			if(address != null){
				String[] addressArr = address.split(",");
				if(addressArr != null && addressArr.length > 10){
					sendMailByAsynchronousMode(data, addressArr, subject, templateName, null, null);
				} else{
					sendMailBySynchronizationMode(data, addressArr, subject, templateName, null, null);
				}
			} else{
				return false;
			}
		} catch(Exception e){
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * 发送带附件邮件
	 * @param data 动态数据
	 * @param address 邮件地址, 多个地址通过英文半角逗号(,)分隔
	 * @param subject 邮件主题
	 * @param templateName 邮件模板名称
	 * @param attachments 附件
	 * @return boolean 邮件发送状态（true：成功  false：失败）
	 */
	public static boolean sendAttachmentMail(Map<String, Object> data, String address,
			String subject, String templateName, File[] attachments) {
		try{
			if(address != null){
				String[] addressArr = address.split(",");
				if(addressArr != null && addressArr.length > 10){
					sendMailByAsynchronousMode(data, addressArr, subject, templateName, null, attachments);
				} else{
					sendMailBySynchronizationMode(data, addressArr, subject, templateName, null, attachments);
				}
			} else{
				return false;
			}
		} catch(Exception e){
			return false;
		}
		return true;

	}
	
	/**
	 * 
	 * 发送嵌套图片邮件
	 * @param data 动态数据
	 * @param address 邮件地址, 多个地址通过英文半角逗号(,)分隔
	 * @param subject 邮件主题
	 * @param templateName 邮件模板名称
	 * @param images 嵌套图片
	 * @return boolean 邮件发送状态（true：成功  false：失败）
	 */
	public static boolean sendImageMail(Map<String, Object> data, String address,
			String subject, String templateName, Map<String, File> images) {
		try{
			if(address != null){
				String[] addressArr = address.split(",");
				if(addressArr != null && addressArr.length > 10){
					sendMailByAsynchronousMode(data, addressArr, subject, templateName, images, null);
				} else{
					sendMailBySynchronizationMode(data, addressArr, subject, templateName, images, null);
				}
			} else{
				return false;
			}
		} catch(Exception e){
			return false;
		}
		return true;

	}
	
	/**
	 * 发送复合邮件（html、嵌套图片、附件）
	 * @param data 动态数据
	 * @param address 邮件地址, 多个地址通过英文半角逗号(,)分隔
	 * @param subject 邮件主题
	 * @param templateName 邮件模板名称
	 * @param images 嵌套图片
	 * @param attachments 附件
	 * @return boolean 邮件发送状态（true：成功  false：失败）
	 * 
	 */
	public static boolean sendComplexMail(Map<String, Object> data, String address,
			String subject, String templateName, Map<String, File> images, File[] attachments) {
		try{
			if(address != null){
				String[] addressArr = address.split(",");
				if(addressArr != null && addressArr.length > 10){
					sendMailByAsynchronousMode(data, addressArr, subject, templateName, images, attachments);
				} else{
					sendMailBySynchronizationMode(data, addressArr, subject, templateName, images, attachments);
				}
			} else{
				return false;
			}
		} catch(Exception e){
			return false;
		}
		return true;
	}
	
	/**
	 * 同步发送
	 */
	private static void sendMailBySynchronizationMode(Map<String, Object> data, String[] address,
			String subject, String templateName, Map<String, File> images, File[] attachments) throws MessagingException, MailException {
		
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true,
					"utf-8");
			helper.setFrom(simpleMailMessage.getFrom());
			helper.setTo(address);
			helper.setSubject(subject);
			String htmlText = getMailText(data, templateName);
			helper.setText(htmlText, true);
			if(images != null && !images.isEmpty()){
				Iterator iter = images.entrySet().iterator();
			    while(iter.hasNext()){
			    	Map.Entry<String, File> entry = (Map.Entry<String, File>) iter.next();
			    	helper.addInline(entry.getKey(), entry.getValue());
			    }
			}
			if(attachments != null && attachments.length > 0){
				for(File file : attachments){
					helper.addAttachment(file.getName(), file);
				}
			}
			mailSender.send(msg);
	}
	
	/**
	 * 异步发送
	 */
	private static void sendMailByAsynchronousMode(final Map<String, Object> data, final String[] address,
			final String subject, final String templateName, final Map<String, File> images, final File[] attachments) throws Exception{
		 taskExecutor.execute(new Runnable(){
		   public void run(){
			   try {
				   sendMailBySynchronizationMode(data, address, subject, templateName, images, attachments);
			   } catch (Exception e) {
			   }
		   }
		});
	}

	public static void setMailSender(JavaMailSender mailSender) {
		Email.mailSender = mailSender;
	}

	public static void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
		Email.simpleMailMessage = simpleMailMessage;
	}

	public static void setFreeMarkerConfigurer(
			FreeMarkerConfigurer freeMarkerConfigurer) {
		Email.freeMarkerConfigurer = freeMarkerConfigurer;
	}

	public static void setTaskExecutor(TaskExecutor taskExecutor) {
		Email.taskExecutor = taskExecutor;
	}
	

}
