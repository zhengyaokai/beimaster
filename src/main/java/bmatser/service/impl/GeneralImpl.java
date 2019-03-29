package bmatser.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.codec.binary.StringUtils;
import org.igfay.jfig.JFig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import bmatser.dao.RegisterMapper;
import bmatser.model.Register;
import bmatser.service.GeneralI;
import bmatser.service.RegisterI;
import bmatser.util.Constants;
import bmatser.util.CookieTools;
import bmatser.util.DESCoder;
import bmatser.util.Email;
import bmatser.util.LogoException;
import bmatser.util.MemberTools;
import bmatser.util.SMS;

/**
 * 通用业务处理类
 * @author felx
 * 2017-7-23
 */
@Service("generalService")
public class GeneralImpl implements GeneralI {
	@Autowired
	private RegisterMapper registerDao;
	@Autowired
	private RegisterI registerService;
	@Autowired
	private MemcachedClient memcachedClient;
	

	/**
	 * 发送手机注册验证码
	 * @param mobile
	 * @param session
	 * @return String
	 * 2017-7-23
	 * @throws Exception 
	 *
	 */
	@Override
	public void sendSMSVerifyCode(String mobile,String vcode,HttpServletRequest request, HttpSession session,String channel) throws Exception {
		if (vcode == null){
			throw new Exception("请输入验证码");
		}
		try{
			String key = MemberTools.getFingerprint(request,CookieTools.getCookieValue(Constants.checkCodeCookieName,request));
			String checkCode = (String) memcachedClient.get(key);
			if (checkCode == null || !vcode.equalsIgnoreCase(checkCode)){
				throw new Exception("验证码输入错误");
			}
//			checkCode = null;
//			memcachedClient.set(key,60, checkCode);
			memcachedClient.delete(key);
		}catch (Exception e) {
			throw new LogoException(e,e.getMessage());
		}
		/**验证手机号码是否存在*/
		DESCoder.instanceMobile();
		if("4".equals(channel)){
			channel = "4,5";
		}
		long count = registerService.existMobile(mobile,channel);
		if(count > 0){
			throw new Exception("手机号码已存在");
		}
		/**生成6位数字随机数 */
		Random rand = new Random();
		String code = "";
		for(int i = 0; i < 6; i++){
			int sub = rand.nextInt(10);
			code = code+sub;
		}
		/**调用短信接口，发送验证码*/
		String result = SMS.sendRegisterCode(mobile, code);
		if(StringUtils.equals(result, "OK")){
			session.setAttribute("REGISTER_VERIFY_CODE", code);
			session.setAttribute("REGISTER_MOBILE", mobile);
			session.setMaxInactiveInterval(60 * 10);
			/**将手机号码和验证码放入memcached,设置有效时间10分钟*/
			String regCodeKey = Constants.regCodeKey;
			regCodeKey = MemberTools.getFingerprint(request,regCodeKey);
			memcachedClient.set(regCodeKey, 60 * 10,code);//放10分钟
			String regMobileKey = Constants.regMobileKey;
			regMobileKey = MemberTools.getFingerprint(request,regMobileKey);
			memcachedClient.set(regMobileKey, 60 * 10,mobile);//放10分钟
		} else{
			throw new Exception("发送失败");
		}
	}
	
	/**
	 * 商城app注册发送手机验证码
	 * @param mobile
	 * @param session
	 * @return String
	 * @throws Exception 
	 *
	 */
	@Override
	public void sendSMSVerifyCode1(String mobile,HttpServletRequest request, HttpSession session,String channel) throws Exception {
		/**验证手机号码是否存在*/
		DESCoder.instanceMobile();
		if("4".equals(channel)){
			channel = "4,5";
		}
		long count = registerService.existMobile(mobile,channel);
		if(count > 0){
			throw new Exception("手机号码已存在");
		}
		/**生成6位数字随机数 */
		Random rand = new Random();
		String code = "";
		for(int i = 0; i < 6; i++){
			int sub = rand.nextInt(10);
			code = code+sub;
		}
//		String sendMsg = JFig.getInstance().getValue("SMS", "SMS_REGISTER", "验证码：%s ，保密哦！您正在注册工电宝账号。(10分钟内有效)");
//		sendMsg = String.format(sendMsg, code);
		/**调用短信接口，发送验证码*/
//		String sendNum = SMS.send(mobile, sendMsg);
		String result = SMS.sendRegisterCode(mobile, code);
		JSONObject obj = JSONObject.parseObject(result);
		Object o = obj.get("alibaba_aliqin_fc_sms_num_send_response");
		/**发送成功后，将手机号码和验证码保存在session中，并设置有效时间10分钟*/
//		if(Integer.parseInt(sendNum) > 0){
		if(null != o){
			session.setAttribute("REGISTER_VERIFY_CODE", code);
			session.setAttribute("REGISTER_MOBILE", mobile);
			session.setMaxInactiveInterval(60 * 10);
			
			/**将手机号码和验证码放入memcached,设置有效时间10分钟*/
			String regCodeKey = Constants.regCodeKey;
			regCodeKey = MemberTools.getFingerprint(request,regCodeKey);
			memcachedClient.set(regCodeKey, 60 * 10,code);//放10分钟
			String regMobileKey = Constants.regMobileKey;
			regMobileKey = MemberTools.getFingerprint(request,regMobileKey);
			memcachedClient.set(regMobileKey, 60 * 10,mobile);//放10分钟
		} else{
			throw new Exception("发送失败");
		}
	}
	
	/**
	 * 发送邮箱验证码
	 * @author felx
	 * @date 2017-12-22
	 */
	public void sendCode2Email(String email,String vcode,HttpServletRequest request) throws Exception {
		if (vcode == null){
			throw new Exception("请输入验证码");
		}
		try{
			String key = MemberTools.getFingerprint(request,CookieTools.getCookieValue(Constants.checkCodeCookieName,request));
			String checkCode = (String) memcachedClient.get(key);
			if (checkCode == null || !vcode.equalsIgnoreCase(checkCode)){
				throw new Exception("验证码输入错误");
			}
//			checkCode = null;
//			memcachedClient.set(key,60, checkCode);
			memcachedClient.delete(key);
		}catch (Exception e) {
			throw new LogoException(e,e.getMessage());
		}
		
		/**验证邮箱是否存在*/
		long count = registerService.existEmail(email,"4");
		if(count > 0){
			throw new Exception("邮箱已经存在");
		}
		
		try {
			/* 获取验证码 */
			/**生成6位数字随机数 */
			Random rand = new Random();
			String code = "";
			for(int i = 0; i < 6; i++){
				int sub = rand.nextInt(10);
				code = code+sub;
			}
			/* 测试邮箱 email ="  "; */
			/* 设置邮箱的主题 */
			String subject = "工电宝注册验证";
			/* 设置发件内容 */
//			String content = "欢迎您注册工电宝账号，您的验证码为" + code + "。【10分钟内有效】";
			/* 发送结果 */
//			boolean feedback = Email.sendTextMail(email, subject, content);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("code", code);
			boolean feedback = Email.sendHtmlMail(data, email, subject, "registerMail.ftl");
			if (feedback) {
				/**将邮箱和验证码放入memcached,设置有效时间10分钟*/
				String regCodeKey = Constants.regCodeKey;
				regCodeKey = MemberTools.getFingerprint(request,regCodeKey);
				memcachedClient.set(regCodeKey, 60 * 10,code);//放10分钟
				String regEmailKey = Constants.regEmailKey;
				regEmailKey = MemberTools.getFingerprint(request,regEmailKey);
				memcachedClient.set(regEmailKey, 60 * 10,email);//放10分钟
			} else {
				throw new Exception("发送失败");
			}
		} catch (Exception e) {
			/* 程序错误 */
			throw new LogoException(e,"程序错误");
		}
	}

	@Override
	public void sendSMSVerifyCode(String mobile, HttpServletRequest request,
			HttpSession session) throws Exception {
		
		/**验证手机号码是否存在*/
		long count = registerService.existMobile(mobile,"2");
		if(count > 0){
			throw new Exception("手机号码已经存在");
		}
		/**生成6位数字随机数 */
		Random rand = new Random();
		String code = "";
		for(int i = 0; i < 6; i++){
			int sub = rand.nextInt(10);
			code = code+sub;
		}
//		String sendMsg = JFig.getInstance().getValue("SMS", "SMS_REGISTER", "验证码：%s ，保密哦！您正在注册工电宝账号。(10分钟内有效)");
//		sendMsg = String.format(sendMsg, code);
		/**调用短信接口，发送验证码*/
//		String sendNum = SMS.send(mobile, sendMsg);
		String result = SMS.sendRegisterCode(mobile, code);
        /**
         * update by yr
         * 2018-10-15
         */
//		JSONObject obj = JSONObject.parseObject(result);
//		Object o = obj.get("alibaba_aliqin_fc_sms_num_send_response");
		/**发送成功后，将手机号码和验证码保存在session中，并设置有效时间10分钟*/
		if("OK".equals(result)){
//		if(Integer.parseInt(sendNum) > 0){
			session.setAttribute("REGISTER_VERIFY_CODE", code);
			session.setAttribute("REGISTER_MOBILE", mobile);
			session.setMaxInactiveInterval(60 * 10);
			
			/**将手机号码和验证码放入memcached,设置有效时间10分钟*/
			String regCodeKey = Constants.regCodeKey;
			regCodeKey = MemberTools.getFingerprint(request,regCodeKey);
			memcachedClient.set(regCodeKey, 60 * 10,code);//放10分钟
			String regMobileKey = Constants.regMobileKey;
			regMobileKey = MemberTools.getFingerprint(request,regMobileKey);
			memcachedClient.set(regMobileKey, 60 * 10,mobile);//放10分钟
		} else{
			throw new Exception("发送失败");
		}

		// TODO Auto-generated method stub

	}
	@Override
	public void sendSMSGetPassword(String mobile,String channel,String vcode, HttpServletRequest request,
			HttpSession session) throws Exception {
		if(null != channel && "4".equals(channel)){
			if (vcode == null){
				throw new Exception("请输入验证码");
			}
			try{
				String key = MemberTools.getFingerprint(request,CookieTools.getCookieValue(Constants.checkCodeCookieName,request));
				String checkCode = (String) memcachedClient.get(key);
				if (checkCode == null || !vcode.equalsIgnoreCase(checkCode)){
					throw new Exception("验证码输入错误");
				}
//			checkCode = null;
//			memcachedClient.set(key,60, checkCode);
				memcachedClient.delete(key);
			}catch (Exception e) {
				throw new LogoException(e,e.getMessage());
			}
		}
		DESCoder.instanceMobile();
		if("4".equals(channel)){
			channel = "4,5";
		}
		long count = registerService.existMobile(mobile,channel);
		if(count == 0){
			throw new Exception("手机号码未注册");
		}
		/**生成6位数字随机数 */
		Random rand = new Random();
		String code = "";
		for(int i = 0; i < 6; i++){
			int sub = rand.nextInt(10);
			code = code+sub;
		}
//		String sendMsg = JFig.getInstance().getValue("SMS", "SMS_REGISTER", "验证码：%s ，保密哦！您正在验证工电宝账号。(10分钟内有效)");
//		sendMsg = String.format(sendMsg, code);
		/**调用短信接口，发送验证码*/
//		String sendNum = SMS.send(mobile, sendMsg);
		String result = SMS.sendResetPwdCode(mobile, code);
		/**发送成功后，将手机号码和验证码保存在session中，并设置有效时间10分钟*/
		if(StringUtils.equals(result, "OK")){
//		if(Integer.parseInt(sendNum) > 0){
/*			session.setAttribute("REGISTER_VERIFY_CODE", code);
			session.setAttribute("REGISTER_MOBILE", mobile);
			session.setMaxInactiveInterval(60 * 10);*/
			
			/**将手机号码和验证码放入memcached,设置有效时间10分钟*/
			String editPasswordKey = Constants.editPasswordKey;
			editPasswordKey = MemberTools.getFingerprint(request,editPasswordKey);
			memcachedClient.set(editPasswordKey, 60 * 10,code);//放10分钟
			String editMobileKey = Constants.editMobileKey;
			editMobileKey = MemberTools.getFingerprint(request,editMobileKey);
			memcachedClient.set(editMobileKey, 60 * 10,mobile);//放10分钟
		} else{
			throw new Exception("发送失败");
		}
	
		// TODO Auto-generated method stub
		
	}

	/**
	 * app商城 忘记密码 发送验证短信
	 */
	@Override
	public void sendSMSGetPassword1(String mobile,String channel, HttpServletRequest request,
			HttpSession session) throws Exception {
		DESCoder.instanceMobile();
		if("4".equals(channel)){
			channel = "4,5";
		}
		long count = registerService.existMobile(mobile,channel);
		if(count == 0){
			throw new Exception("手机号码未注册");
		}
		/**生成6位数字随机数 */
		Random rand = new Random();
		String code = "";
		for(int i = 0; i < 6; i++){
			int sub = rand.nextInt(10);
			code = code+sub;
		}
//		String sendMsg = JFig.getInstance().getValue("SMS", "SMS_REGISTER", "验证码：%s ，保密哦！您正在验证工电宝账号。(10分钟内有效)");
//		sendMsg = String.format(sendMsg, code);
		/**调用短信接口，发送验证码*/
//		String sendNum = SMS.send(mobile, sendMsg);
		String result = SMS.sendResetPwdCode(mobile, code);
		/**发送成功后，将手机号码和验证码保存在session中，并设置有效时间10分钟*/
		if(StringUtils.equals(result, "OK")){
//		if(Integer.parseInt(sendNum) > 0){
/*			session.setAttribute("REGISTER_VERIFY_CODE", code);
			session.setAttribute("REGISTER_MOBILE", mobile);
			session.setMaxInactiveInterval(60 * 10);*/
			
			/**将手机号码和验证码放入memcached,设置有效时间10分钟*/
			String editPasswordKey = Constants.editPasswordKey;
			editPasswordKey = MemberTools.getFingerprint(request,editPasswordKey);
			memcachedClient.set(editPasswordKey, 60 * 10,code);//放10分钟
			String editMobileKey = Constants.editMobileKey;
			editMobileKey = MemberTools.getFingerprint(request,editMobileKey);
			memcachedClient.set(editMobileKey, 60 * 10,mobile);//放10分钟
		} else{
			throw new Exception("发送失败");
		}
	
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String findMobile(Integer dealerId) throws Exception {
		Register register = registerDao.selectIdByDealerId(dealerId);
		if(register.getMobileSecret()!=null&&register.getMobileSecret()!=""){
			DESCoder.instanceMobile();
			return DESCoder.decoder(register.getMobileSecret()).trim();
		}
		return "";
	}
	
	/**
	 * 商城 修改手机号 验证身份 发送验证短信
	 * @param mobile
	 * @param session
	 * @return String
	 * 20160905
	 * @throws Exception 
	 *
	 */
	@Override
	public void sendMobileSMSVerifyCode(String mobile,String vcode,HttpServletRequest request, HttpSession session,String channel) throws Exception {
		if (vcode == null){
			throw new Exception("请输入验证码");
		}
		try{
			String key = MemberTools.getFingerprint(request,CookieTools.getCookieValue(Constants.checkCodeCookieName,request));
			String checkCode = (String) memcachedClient.get(key);
			if (checkCode == null || !vcode.equalsIgnoreCase(checkCode)){
				throw new Exception("验证码输入错误");
			}
//			checkCode = null;
//			memcachedClient.set(key,60, checkCode);
			memcachedClient.delete(key);
		}catch (Exception e) {
			throw new LogoException(e,e.getMessage());
		}
		/**验证手机号码是否存在*/
		/*DESCoder.instanceMobile();
		if("4".equals(channel)){
			channel = "4,5";
		}
		long count = registerService.existMobile(mobile,channel);
		if(count > 0){
			throw new Exception("手机号码已存在");
		}*/
		/**生成6位数字随机数 */
		Random rand = new Random();
		String code = "";
		for(int i = 0; i < 6; i++){
			int sub = rand.nextInt(10);
			code = code+sub;
		}
//		String sendMsg = JFig.getInstance().getValue("SMS", "SMS_REGISTER", "验证码：%s ，保密哦！您正在注册工电宝账号。(10分钟内有效)");
//		sendMsg = String.format(sendMsg, code);
		/**调用短信接口，发送短信验证码*/
//		String sendNum = SMS.send(mobile, sendMsg);
		String result = SMS.sendModifyMobileCode(mobile, code);
		/**发送成功后，将手机号码和短信验证码保存在session中，并设置有效时间10分钟*/
		if(StringUtils.equals(result, "OK")){
			session.setAttribute("REGISTER_VERIFY_CODE", code);
			session.setAttribute("REGISTER_MOBILE", mobile);
			session.setMaxInactiveInterval(60 * 10);
			
			/**将手机号码和短信验证码放入memcached,设置有效时间10分钟*/
			String regCodeKey = Constants.modifyMobCodeKey;
			regCodeKey = MemberTools.getFingerprint(request,regCodeKey);
			memcachedClient.set(regCodeKey, 60 * 10,code);//放10分钟
			String regMobileKey = Constants.modifyMobileKey;
			regMobileKey = MemberTools.getFingerprint(request,regMobileKey);
			memcachedClient.set(regMobileKey, 60 * 10,mobile);//放10分钟
		} else{
			throw new Exception("发送失败");
		}
	}
	
	/**
	 * saas 修改绑定银行卡号 发送验证短信
	 * @param mobile
	 * @param session
	 * @return String
	 * 20160910
	 * @throws Exception 
	 *
	 */
	@Override
	public void sendCardSMSVerifyCode(String mobile,String vcode,HttpServletRequest request, HttpSession session,String channel) throws Exception {
		if (vcode == null){
			throw new Exception("请输入验证码");
		}
		try{
			String key = MemberTools.getFingerprint(request,CookieTools.getCookieValue(Constants.checkCodeCookieName,request));
			String checkCode = (String) memcachedClient.get(key);
			if (checkCode == null || !vcode.equalsIgnoreCase(checkCode)){
				throw new Exception("验证码输入错误");
			}
//			checkCode = null;
//			memcachedClient.set(key,60, checkCode);
			memcachedClient.delete(key);
		}catch (Exception e) {
			throw new LogoException(e,e.getMessage());
		}
		/**验证手机号码是否存在*/
		/*DESCoder.instanceMobile();
		if("4".equals(channel)){
			channel = "4,5";
		}
		long count = registerService.existMobile(mobile,channel);
		if(count > 0){
			throw new Exception("手机号码已存在");
		}*/
		/**生成6位数字随机数 */
		Random rand = new Random();
		String code = "";
		for(int i = 0; i < 6; i++){
			int sub = rand.nextInt(10);
			code = code+sub;
		}
//		String sendMsg = JFig.getInstance().getValue("SMS", "SMS_REGISTER", "验证码：%s ，保密哦！您正在注册工电宝账号。(10分钟内有效)");
//		sendMsg = String.format(sendMsg, code);
		/**调用短信接口，发送短信验证码*/
//		String sendNum = SMS.send(mobile, sendMsg);
		String result = SMS.sendModifyCardCode(mobile, code);
		JSONObject obj = JSONObject.parseObject(result);
		Object o = obj.get("alibaba_aliqin_fc_sms_num_send_response");
		/**发送成功后，将手机号码和短信验证码保存在session中，并设置有效时间10分钟*/
//		if(Integer.parseInt(sendNum) > 0){
		if(null != o){
			session.setAttribute("REGISTER_VERIFY_CODE", code);
			session.setAttribute("REGISTER_MOBILE", mobile);
			session.setMaxInactiveInterval(60 * 10);
			
			/**将手机号码和短信验证码放入memcached,设置有效时间10分钟*/
			String regCodeKey = Constants.modifyMobCodeKey;
			regCodeKey = MemberTools.getFingerprint(request,regCodeKey);
			memcachedClient.set(regCodeKey, 60 * 10,code);//放10分钟
			String regMobileKey = Constants.modifyMobileKey;
			regMobileKey = MemberTools.getFingerprint(request,regMobileKey);
			memcachedClient.set(regMobileKey, 60 * 10,mobile);//放10分钟
		} else{
			throw new Exception("发送失败");
		}
	}
}
