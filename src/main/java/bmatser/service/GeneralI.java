package bmatser.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface GeneralI {

	void sendSMSVerifyCode(String mobile,String vcode,HttpServletRequest request, HttpSession session,String channel)  throws Exception;
	
	void sendSMSVerifyCode1(String mobile,HttpServletRequest request, HttpSession session,String channel)  throws Exception;
	/**
	 * 发送邮箱验证码
	 * @author felx
	 * @date 2017-12-22
	 */
	void sendCode2Email(String email,String vcode,HttpServletRequest request) throws Exception;

	void sendSMSVerifyCode(String mobile, HttpServletRequest request,
			HttpSession session) throws Exception ;

	void sendSMSGetPassword(String mobile,String channel,String vcode, HttpServletRequest request,
			HttpSession session) throws Exception;

	void sendSMSGetPassword1(String mobile,String channel, HttpServletRequest request,
			HttpSession session) throws Exception;
	
	String findMobile(Integer dealerId) throws Exception;
	
	void sendMobileSMSVerifyCode(String mobile,String vcode,HttpServletRequest request, HttpSession session,String channel)  throws Exception;

	void sendCardSMSVerifyCode(String mobile, String vcode,HttpServletRequest request, HttpSession session, String channel)  throws Exception;
	
}
