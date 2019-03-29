package bmatser.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.GoodsMapper;
import bmatser.dao.RegisterMapper;
import bmatser.model.Register;
import bmatser.pageModel.RegisterPage;
import bmatser.service.SecuritySettingsI;
import bmatser.util.Constants;
import bmatser.util.CookieTools;
import bmatser.util.DESCoder;
import bmatser.util.LogoException;
import bmatser.util.MemberTools;
@Service("securitySettingsI")
public class SecuritySettingsImpl implements SecuritySettingsI{
	@Autowired
	private RegisterMapper registerDao;
	@Autowired
	private MemcachedClient memcachedClient;
	
	@Override
	public String findMobile(Integer dealerId) {
		Register register = registerDao.selectIdByDealerId(dealerId);
		return register.getMobile();
	}
	
	@Override
	public void modifyMobile(String verifyCode, String newMobile, String dealerId,HttpServletRequest request) throws Exception{
		/**校验手机号码和接收验证码的手机号码是否一致*/
		String regMobileKey = Constants.modifyMobileKey;
		regMobileKey = MemberTools.getFingerprint(request,regMobileKey);
		Object regMobileObj = memcachedClient.get(regMobileKey);
		if(null!=regMobileObj){
			if(StringUtils.isBlank(regMobileObj.toString()) || !StringUtils.equals(regMobileObj.toString(), newMobile)){
				throw new Exception("手机号不正确");
			}
		}else{
			throw new Exception("手机号不正确");
		}
		
		/**校验验证码是否和发送的验证码一致*/
		String regCodeKey = Constants.modifyMobCodeKey;
		regCodeKey = MemberTools.getFingerprint(request,regCodeKey);
		Object regCodeObj = memcachedClient.get(regCodeKey);
		
		if(null!=regCodeObj){
			if(StringUtils.isBlank(regCodeObj.toString()) || !StringUtils.equals(regCodeObj.toString(), verifyCode)){
				throw new Exception("手机验证码不正确");
			}
		}else{
			throw new Exception("手机验证码不正确");
		}
		/*if (vcode == null){
			throw new Exception("请输入验证码");
		}*/
		RegisterPage registerPage = new RegisterPage();
/*		String key = MemberTools.getFingerprint(request,CookieTools.getCookieValue(Constants.checkCodeCookieName,request));
		String checkCode = (String) memcachedClient.get(key);
		if (checkCode == null || !vcode.equalsIgnoreCase(checkCode)){
			throw new Exception("验证码输入错误");
		}
		memcachedClient.delete(key);*/
		if(newMobile!=null && newMobile!=""){
			DESCoder.instanceMobile();
			registerPage.setMobileSecret(DESCoder.encoder(newMobile).trim());//对手机号码加密
			String mobileSecret = registerPage.getMobileSecret();
			registerPage.setMobile(DESCoder.getNewTel(newMobile));//隐藏手机号码
			String mobile = registerPage.getMobile();
			registerDao.updateMobile(mobileSecret,mobile,dealerId);
		}
		
		
	}

}
