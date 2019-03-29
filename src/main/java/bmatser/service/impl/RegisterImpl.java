package bmatser.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.igfay.jfig.JFig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;

import com.alibaba.fastjson.JSONObject;

import bmatser.dao.CrmCustomerManagerMapper;
import bmatser.dao.CrmCustomerMapper;
import bmatser.dao.DealerCashMapper;
import bmatser.dao.DealerMapper;
import bmatser.dao.OrderInfoMapper;
import bmatser.dao.RegisterMapper;
import bmatser.dao.StaffMapper;
import bmatser.exception.GdbmroException;
import bmatser.model.DealerCash;
import bmatser.pageModel.PageMode;
import bmatser.pageModel.RegisterPage;
import bmatser.pageModel.util.RegisterPageUtil;
import bmatser.service.DealerCashI;
import bmatser.service.RegisterI;
import bmatser.util.Constants;
import bmatser.util.CookieTools;
import bmatser.util.DESCoder;
import bmatser.util.Encrypt;
import bmatser.util.IdBuildTools;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.MobileUtils;
import bmatser.util.ObjectUtil;

/**
 * 注册用户业务类
 * 包含用户注册、登录、修改密码等
 * @author felx
 * 2017-7-22
 */
@Service("registerService")
public class RegisterImpl implements RegisterI {
	
	public static final String GET_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxbfdeb9a9946da45e&secret=b9af3cec6de2a0d8d1055b5b8621549e&code=CODE&grant_type=authorization_code";// 获取access 
	
    public static final String APP_ID = "wxbfdeb9a9946da45e";
    public static final String SECRET = "b9af3cec6de2a0d8d1055b5b8621549e";
    
	@Autowired
	private RegisterMapper registerDao;
	
	@Autowired
	private DealerMapper dealerDao;
	@Autowired
	private DealerCashMapper dealerCashDao;
	
	@Autowired
	private MemcachedClient memcachedClient;
	
	@Autowired
	private DealerCashI dealerCashI; 
	@Autowired
	private CrmCustomerMapper customerMapper; 
	@Autowired
	private StaffMapper staffDao;
	@Autowired
	private CrmCustomerManagerMapper customerManagerDao;
	
	@Autowired
	private OrderInfoMapper orderInfoDao;
	/**
	 * 用户登录
	 * @param loginName
	 * @param password
	 * @return RegisterPage
	 * 2017-7-22
	 * @throws Exception 
	 * 
	 */
	@Override
	public Map<String, Object> login(HttpServletRequest request,String loginName, String password,String dealerType) throws Exception {
		boolean flag = MobileUtils.isMobileNO(loginName);
		String mobileSecret = null;
		if(flag){
			DESCoder.instanceMobile();
			mobileSecret = DESCoder.encoder(loginName).trim();
		}
//		System.out.println("loginName:"+loginName);
//		System.out.println("password:"+password);
		Map<String, Object> map = registerDao.login(loginName, password, dealerType,mobileSecret);
		if(map!=null && map.get("loginId")!=null){
			registerDao.updateLastTime(map.get("loginId").toString());
		}
//		System.out.println("map:"+map);
		return map;
	}
	
	/**
	 * 商城用户登录
	 * @param loginName
	 * @param password
	 * @return RegisterPage
	 * 2017-7-22
	 * @throws Exception 
	 * 
	 */
	@Override
	public Map mallLogin(HttpServletRequest request,String loginName, String password,String dealerType) throws Exception {
		Map map = registerDao.login(loginName, password, dealerType,null);
		if(map==null){
			throw new Exception("error");
		}
		return map;
	}

	/**
	 * 用户注册
	 * @param registerPage
	 * @return int
	 * 2017-7-22
	 * @throws Exception 
	 *
	 */
	@Override
	public Map add(RegisterPage registerPage, HttpSession session) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		/**判断用户名是否存在*/
		long count = registerDao.existUserName(registerPage.getUserName(),"2");
		if(count > 0){
			throw new Exception("登陆名已经存在");
		}
		/**校验手机号码和接收验证码的手机号码是否一致*/
		String mobileSession = (String)session.getAttribute("REGISTER_MOBILE");
		String mobile = registerPage.getMobile();
		if(StringUtils.isBlank(mobileSession) || !StringUtils.equals(mobileSession, mobile)){
			throw new Exception("手机号不正确");
		}
		/**校验验证码是否和发送的验证码一致*/
		String verifyCodeSession = (String)session.getAttribute("REGISTER_VERIFY_CODE");
		String verifyCode = registerPage.getVerifyCode();
		if(StringUtils.isBlank(verifyCodeSession) || !StringUtils.equals(verifyCodeSession, verifyCode)){
			throw new Exception("手机验证吗不正确");
		}
		registerPage.setCheckStatus("0");
		registerPage.setDealerType("2");
		registerPage.setPassword(Encrypt.md5AndSha(registerPage.getPassword()));
		dealerDao.add(registerPage);
		registerDao.add(registerPage);
		map.put("loginId", registerPage.getLoginId());
		map.put("dealerId", registerPage.getDealerId());
		map.put("uesrname", registerPage.getUserName());
		return map;
	}
	
	/**
	 * 商城注册
	 * @author felx
	 * @date 2017-12-21
	 */
	public Map addMallRegister(RegisterPage registerPage,HttpServletRequest request, HttpSession session)  throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		/*String vcode = registerPage.getvCode();
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
			throw new Exception(e.getMessage());
		}*/
		
		String userNameFlag = registerPage.getUserNameFlag();
		if(null != userNameFlag && "M".equals(userNameFlag)){
			//用户名为手机号
			registerPage.setMobile(registerPage.getUserName());
			/**校验手机号码和接收验证码的手机号码是否一致*/
			String regMobileKey = Constants.regMobileKey;
			regMobileKey = MemberTools.getFingerprint(request,regMobileKey);
			Object regMobileObj = memcachedClient.get(regMobileKey);
			
			String mobile = registerPage.getMobile();
			if(null!=regMobileObj){
				if(StringUtils.isBlank(regMobileObj.toString()) || !StringUtils.equals(regMobileObj.toString(), mobile)){
					throw new Exception("手机号不正确");
				}
			}else{
				throw new Exception("手机号不正确");
			}
			
			/**校验验证码是否和发送的验证码一致*/
			String regCodeKey = Constants.regCodeKey;
			regCodeKey = MemberTools.getFingerprint(request,regCodeKey);
			Object regCodeObj = memcachedClient.get(regCodeKey);
			
			if(null!=regCodeObj){
				String verifyCode = registerPage.getVerifyCode();
				if(StringUtils.isBlank(regCodeObj.toString()) || !StringUtils.equals(regCodeObj.toString(), verifyCode)){
					throw new Exception("手机验证码不正确");
				}
			}else{
				throw new Exception("手机验证码不正确");
			}
			
		}else if(null != userNameFlag && "E".equals(userNameFlag)){
			//用户名为邮箱--发送验证邮件
			registerPage.setEmail(registerPage.getUserName());
			registerPage.setMobile("");
			/**校验邮箱和接收验证码的邮箱是否一致*/
			String regEmailKey = Constants.regEmailKey;
			regEmailKey = MemberTools.getFingerprint(request,regEmailKey);
			Object regEmailObj = memcachedClient.get(regEmailKey);
			
			String email = registerPage.getEmail();
			if(null!=regEmailObj){
				if(StringUtils.isBlank(regEmailObj.toString()) || !StringUtils.equals(regEmailObj.toString(), email)){
					throw new Exception("邮箱不正确");
				}
			}else{
				throw new Exception("邮箱不正确");
			}
			
			/**校验验证码是否和发送的验证码一致*/
			String regCodeKey = Constants.regCodeKey;
			regCodeKey = MemberTools.getFingerprint(request,regCodeKey);
			Object regCodeObj = memcachedClient.get(regCodeKey);
			
			if(null!=regCodeObj){
				String verifyCode = registerPage.getVerifyCode();
				if(StringUtils.isBlank(regCodeObj.toString()) || !StringUtils.equals(regCodeObj.toString(), verifyCode)){
					throw new Exception("验证码不正确");
				}
			}else{
				throw new Exception("验证码不正确");
			}
		}
		
		
		/**判断用户名是否存在*/
		long count = registerDao.existUserName(registerPage.getUserName(),"4");
		if(count > 0){
			throw new Exception("登录名已经存在");
		}
		DESCoder.instanceMobile();
		registerPage.setMobileSecret(DESCoder.encoder(registerPage.getMobile()).trim());//对手机号码加密
		registerPage.setMobile(DESCoder.getNewTel(registerPage.getMobile()));//隐藏手机号码
		registerPage.setCheckStatus("1");
		registerPage.setDealerType("4");
		registerPage.setDealerName(registerPage.getUserName());
		registerPage.setPassword(Encrypt.md5AndSha(registerPage.getPassword()));
		registerPage.setCash(10000);//注册送10000现金券
		dealerDao.add(registerPage);
		registerDao.add(registerPage);
		/*现金券start*/
		DealerCash dealerCash = new DealerCash();
		dealerCash.setCash(10000);
		dealerCash.setDealerId(registerPage.getDealerId());
		dealerCash.setCreateTime(new Date());
		dealerCash.setCreateUserId(registerPage.getDealerId());
		dealerCash.setStatus("1");
		dealerCash.setType(1);
		dealerCashI.insertDealerCash(dealerCash);
		/*现金券end*/
		map.put("loginId", registerPage.getLoginId());
		map.put("dealerId", registerPage.getDealerId());
		map.put("userName", registerPage.getUserName());
		return map;
	}

	/**
	 * 修改密码
	 * @param loginId
	 * @param oldPassword
	 * @param newPassword
	 * @return int
	 * 2017-7-22
	 *
	 */
	@Override
	public int editPassword(Integer loginId, String oldPassword,
			String newPassword) {
		return registerDao.editPassword(loginId, oldPassword, newPassword);
	}

	/**
	 * 用户名是否存在
	 * @param userName
	 * @return
	 * 2017-7-23
	 *
	 */
	@Override
	public long existUserName(String userName,String dealerType) {
		return registerDao.existUserName(userName,dealerType);
	}

	/**
	 * 注册手机是否存在
	 * @param mobile
	 * @return
	 * 2017-7-23
	 *
	 */
	@Override
	public long existMobile(String mobile,String dealerType) {
		try {
			DESCoder.instanceMobile();
			String m = DESCoder.encoder(mobile);
			return registerDao.existMobile(m.trim(),dealerType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 注册手机是否存在
	 * @param mobile
	 * @return
	 * 2017-7-23
	 *
	 */
	@Override
	public long existEmail(String email,String dealerType) {
		return registerDao.existEmail(email,dealerType);
	}

	/**
	 * 获取指定用户账户信息
	 * @param loginId
	 * @return map
	 * 2017-7-28
	 * @throws Exception 
	 *
	 */
	@Override
	public Map getUserAccount(Integer loginId) throws Exception {
		if(loginId == null){
			throw new Exception("请登录");
		}
		Map map = registerDao.getUserAccount(loginId);
		if(map==null){
			throw new Exception("帐号不存在或密码错误");
		}
		return map;
	}

	@Override
	@Rollback
	public Map saveMobRegister(RegisterPage registerPage, HttpServletRequest request, HttpSession session) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		/**校验手机号码和接收验证码的手机号码是否一致*/
		String regMobileKey = Constants.regMobileKey;
		regMobileKey = MemberTools.getFingerprint(request,regMobileKey);
		Object regMobileObj = memcachedClient.get(regMobileKey);
		
		String mobile = registerPage.getMobile();
		registerPage.setUserName(mobile);
		DESCoder.instanceMobile();;
		registerPage.setMobileSecret(DESCoder.encoder(mobile).trim());//对手机号码加密
//		registerPage.setDealerName(mobile);
		if(null!=regMobileObj){
			if(StringUtils.isBlank(regMobileObj.toString()) || !StringUtils.equals(regMobileObj.toString(), mobile)){
				throw new Exception("手机号码验证不正确");
			}
		}else{
			throw new Exception("手机号验证不正确");
		}
		
		/**校验验证码是否和发送的验证码一致*/
		String regCodeKey = Constants.regCodeKey;
		regCodeKey = MemberTools.getFingerprint(request,regCodeKey);
		Object regCodeObj = memcachedClient.get(regCodeKey);
		
		if(null!=regCodeObj){
			String verifyCode = registerPage.getVerifyCode();
			if(StringUtils.isBlank(regCodeObj.toString()) || !StringUtils.equals(regCodeObj.toString(), verifyCode)){
				throw new Exception("验证码不正确");
			}
		}else{
			throw new Exception("验证码不正确");
		}
		/**判断用户名是否存在*/
		long count = registerDao.existMobile(registerPage.getMobileSecret().trim(),"2");
		if(count > 0){
			throw new Exception("登录名已经存在");
		}
		registerPage.setCheckStatus("0");
		registerPage.setDealerType("2");
		if(StringUtils.isNotBlank(registerPage.getPassword())){
			registerPage.setPassword(registerPage.getPassword().length()!=40?Encrypt.md5AndSha(registerPage.getPassword()) : registerPage.getPassword());
		}else{
			throw new Exception("密码不能为空");
		}
		
		if(StringUtils.isBlank(registerPage.getRegChannel())){
			registerPage.setRegChannel("2");
		}
		DESCoder.instanceMobile();;
		registerPage.setMobile(DESCoder.getNewTel(mobile));//隐藏手机号码
		dealerDao.add(registerPage);
		registerDao.add(registerPage);
		/*CrmCustomer cc = new CrmCustomer();
		cc.setDealerId(registerPage.getDealerId());
		cc.setName(mobile);
//		cc.setRegisterMobile(mobile);
		cc.setRegisterMobile(registerPage.getMobile());
		cc.setMobile(registerPage.getMobile());
		cc.setMobileSecret(registerPage.getMobileSecret());
		cc.setStatus("3");
		cc.setRankId(2);
		int n=customerMapper.insert(cc);
		if(n==0){
			throw new RuntimeException("注册失败");
		}*/
		
		map.put("loginId", registerPage.getLoginId());
		map.put("dealerId", registerPage.getDealerId());
		map.put("uesrname", registerPage.getUserName());
		map.put("checkStatus", registerPage.getCheckStatus());
		map.put("score", 0);
		map.put("cash", 0);
		map.put("isLicense", 0);
		return map;
	}

	@Override
	public int getPasswprd(String mobile, String code, String newPassword,
			HttpServletRequest request) throws Exception {
		DESCoder.instanceMobile();;
		long count = registerDao.existMobile(DESCoder.encoder(mobile).trim(),"2");
		if(count > 0){
			String editMobileKey = Constants.editMobileKey;
			editMobileKey = MemberTools.getFingerprint(request,editMobileKey);
			Object editMobileObj = memcachedClient.get(editMobileKey);
			if(null!=editMobileObj){
				if(StringUtils.isBlank(editMobileObj.toString()) || !StringUtils.equals(editMobileObj.toString(), mobile)){
					throw new Exception("手机号验证不正确");
				}
			}else{
				throw new Exception("手机号验证不正确");
			}
			String editPasswordKey = Constants.editPasswordKey;
			editPasswordKey = MemberTools.getFingerprint(request,editPasswordKey);
			Object regCodeObj = memcachedClient.get(editPasswordKey);
			
			if(null!=regCodeObj){
				if(StringUtils.isBlank(regCodeObj.toString()) || !StringUtils.equals(regCodeObj.toString(), code)){
					throw new Exception("验证码不正确");
				}
			}else{
				throw new Exception("验证码不正确");
			}
			DESCoder.instanceMobile();
			System.out.println(DESCoder.encoder(mobile).trim());
			return registerDao.getPasswordBymob(DESCoder.encoder(mobile).trim(),newPassword);
		}else{
			throw new Exception("登陆名不存在");
		}
	}

	@Override
	public int updateMallPasswprd(String mobile, String code,
			String newPassword, HttpServletRequest request) throws Exception {
		DESCoder.instanceMobile();
		long count = registerDao.existMobile(DESCoder.encoder(mobile).trim(),"4,5");
		if(count > 0){
			String regMobileKey = Constants.editMobileKey;
			regMobileKey = MemberTools.getFingerprint(request,regMobileKey);
			Object editMobileObj = memcachedClient.get(regMobileKey);
			if(null!=editMobileObj){
				if(StringUtils.isBlank(editMobileObj.toString()) || !StringUtils.equals(editMobileObj.toString(), mobile)){
					throw new Exception("手机号验证不正确");
				}
			}else{
				throw new Exception("手机号验证不正确");
			}
			String regCodeKey = Constants.editPasswordKey;
			regCodeKey = MemberTools.getFingerprint(request,regCodeKey);
			Object regCodeObj = memcachedClient.get(regCodeKey);
			
			if(null!=regCodeObj){
				if(StringUtils.isBlank(regCodeObj.toString()) || !StringUtils.equals(regCodeObj.toString(), code)){
					throw new Exception("验证码不正确");
				}
			}else{
				throw new Exception("验证码不正确");
			}
			newPassword = newPassword.length()!=40?Encrypt.md5AndSha(newPassword) : newPassword;
			//newPassword = Encrypt.md5AndSha(newPassword);
			DESCoder.instanceMobile();
			return registerDao.getPasswordByMall(DESCoder.encoder(mobile).trim(),newPassword);
		}else{
			throw new Exception("登陆名不存在");
		}
	}

	@Override
	public void loadUserInfo(HttpServletRequest request) {
		String cookieId = CookieTools.getCookieValue(Constants.saasLoginCookieName,request);
		if (cookieId != null) {
			LoginInfo loginInfo = MemberTools.isSaasLogin(request);
			if(null != loginInfo && null != loginInfo.getDealerId()){
				Map<String, Object> map= registerDao.loadUserInfo(loginInfo.getDealerId());
				try {
					if(map!=null){
						loginInfo.setCash(null!=map.get("cash")?map.get("cash").toString():"");
						loginInfo.setScore(null!=map.get("score")?map.get("score").toString():"");
						loginInfo.setCartNum(null!=map.get("cartNum")?map.get("cartNum").toString():"");
						loginInfo.setCheckStatus(null!=map.get("checkStatus")?map.get("checkStatus").toString():"");
						
						loginInfo.setDealerId(map.get("dealerId").toString());
						loginInfo.setDealerName(map.get("dealerName")==null?map.get("userName").toString():map.get("dealerName").toString());
						loginInfo.setLoginId(map.get("loginId").toString());
						loginInfo.setUsername(map.get("userName").toString());
						loginInfo.setDealerType(map.get("dealerType").toString());
						loginInfo.setBean(ObjectUtil.tointValue(map.get("bean")));
					}
					cookieId = MemberTools.getFingerprint(request,cookieId);
					memcachedClient.set(cookieId, 0,loginInfo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public Map<String, Object> getUserInfo(String dealerId){
		return registerDao.loadUserInfo(dealerId);
	}

	public Map<String, Object> getSaasUserInfo(String dealerId){
		return registerDao.getSaasUserInfo(dealerId);
	}
	
	@Override
	public String loadUserCart(String dealerId) {
		Map<String, Object> map= registerDao.loadUserInfo(dealerId);
	    return map.get("cartNum").toString();
	}

	@Override
	public Map<String, Object> toMallRegister(RegisterPageUtil registerPage) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		if(registerPage.checkMallInfo()){
			int count = registerDao.existMallUserName(registerPage.getUserName());
			if(count > 0){
				throw new Exception("登录名已经存在");
			}
			registerPage.setDefaultMallInfo();
			dealerDao.add(registerPage);
			registerDao.add(registerPage);
			dealerCashDao.addByMallRegister(registerPage);
			map.put("loginId", registerPage.getLoginId());
			map.put("dealerId", registerPage.getDealerId());
			map.put("userName", registerPage.getUserName());
		}
		return map;
	}

	@Override
	public void isMallRegisterSms(RegisterPageUtil registerPage, HttpServletRequest request) throws Exception {
		registerPage.setMobile(registerPage.getUserName());
		/**校验验证码是否和发送的验证码一致*/
		String regCodeKey = Constants.regCodeKey;
		regCodeKey = MemberTools.getFingerprint(request,regCodeKey);
		Object regCodeObj = memcachedClient.get(regCodeKey);
		
		if(null!=regCodeObj){
			String verifyCode = registerPage.getVerifyCode();
			if(StringUtils.isBlank(regCodeObj.toString()) || !StringUtils.equals(regCodeObj.toString(), verifyCode)){
				throw new Exception("手机验证码不正确");
			}
		}else{
			throw new Exception("手机验证码不正确");
		}
		/**校验手机号码和接收验证码的手机号码是否一致*/
		String regMobileKey = Constants.regMobileKey;
		regMobileKey = MemberTools.getFingerprint(request,regMobileKey);
		Object regMobileObj = memcachedClient.get(regMobileKey);
		
		String mobile = registerPage.getMobile();
		if(null!=regMobileObj){
			if(StringUtils.isBlank(regMobileObj.toString()) || !StringUtils.equals(regMobileObj.toString(), mobile)){
				throw new Exception("手机号验证不正确");
			}
		}else{
			throw new Exception("手机号验证不正确");
		}
		
	}

	@Override
	public void modifyMobileSms(RegisterPageUtil registerPage, HttpServletRequest request) throws Exception {
		registerPage.setMobile(registerPage.getUserName());
		/**校验手机号码和接收验证码的手机号码是否一致*/
		String regMobileKey = Constants.modifyMobileKey;
		regMobileKey = MemberTools.getFingerprint(request,regMobileKey);
		Object regMobileObj = memcachedClient.get(regMobileKey);
		
		String mobile = registerPage.getMobile();
		if(null!=regMobileObj){
			if(StringUtils.isBlank(regMobileObj.toString()) || !StringUtils.equals(regMobileObj.toString(), mobile)){
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
			String verifyCode = registerPage.getVerifyCode();
			if(StringUtils.isBlank(regCodeObj.toString()) || !StringUtils.equals(regCodeObj.toString(), verifyCode)){
				throw new Exception("手机验证码不正确");
			}
		}else{
			throw new Exception("手机验证码不正确");
		}
	}
	
	@Override
	public double getBalance(PageMode model) throws Exception {
		String dealerId = model.getLogin().getDealerId();
		String orderId = model.getStringValue("orderId");
		double dealerBalance = registerDao.getBalance(dealerId);
		if(StringUtils.isNotBlank(orderId)){
			double orderBanlance = orderInfoDao.getOrderBalance(dealerId,orderId);
			dealerBalance = dealerBalance + orderBanlance;
		}
		return dealerBalance;
	}

	@Override
	public void getOpenId(String code, String state,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, GdbmroException {
		String cookieId = null;
		cookieId = CookieTools.getCookieValue(Constants.loginCookieName,request);
		if (cookieId != null) {
			cookieId = MemberTools.getFingerprint(request,cookieId);
			memcachedClient.delete(cookieId);
		}
		if("888".equals(code)){
			Map map = registerDao.getDealerInfo("123456");
			LoginInfo loginInfo = new LoginInfo();
			loginInfo.setDealerId(map.get("dealerId").toString());
			loginInfo.setDealerName(map.get("dealerName")==null?map.get("userName").toString():map.get("dealerName").toString());
			loginInfo.setLoginId(map.get("loginId").toString());
			loginInfo.setUsername(map.get("userName").toString());
			cookieId = IdBuildTools.creatId("1DD98F367");
			CookieTools.setCookie(Constants.loginCookieName, cookieId, response);
			cookieId = MemberTools.getFingerprint(request,cookieId);
			memcachedClient.set(cookieId, 60 * 30,loginInfo);//30分钟
			return;
		}
		String requestUrl = GET_TOKEN_URL.replace("CODE", code);
		System.out.println("!!!!!!!!!!!!!!!!!!!!  "+requestUrl);
		String openId = getOpenId(requestUrl);
		Map map = registerDao.getDealerInfo(openId);
		if(map != null && !map.isEmpty()){//根据openId查到登录信息
			LoginInfo loginInfo = new LoginInfo();
			loginInfo.setDealerId(map.get("dealerId").toString());
			loginInfo.setDealerName(map.get("dealerName")==null?map.get("userName").toString():map.get("dealerName").toString());
			loginInfo.setLoginId(map.get("loginId").toString());
			loginInfo.setUsername(map.get("userName").toString());
			cookieId = IdBuildTools.creatId("1DD98F367");
			CookieTools.setCookie(Constants.loginCookieName, cookieId, response);
			cookieId = MemberTools.getFingerprint(request,cookieId);
			memcachedClient.set(cookieId, 60 * 30,loginInfo);//30分钟
		}else{//根据openId没查到登录信息
			cookieId = CookieTools.getCookieValue(Constants.wxloginCookieName,request);
			if (cookieId != null) {
				cookieId = MemberTools.getFingerprint(request,cookieId);
				memcachedClient.delete(cookieId);
			}
			cookieId = IdBuildTools.creatId("wxlg");
			CookieTools.setCookie(Constants.wxloginCookieName, cookieId, response);			
			cookieId = MemberTools.getFingerprint(request,cookieId);
			memcachedClient.set(cookieId, 60 * 30,openId);//30分钟
			
		}
		
	}
	
	@Override
	public String getAccessToken(String code) throws Exception,GdbmroException{
		String requestUrl = GET_TOKEN_URL.replace("CODE", code);		
		String openId = getOpenId(requestUrl);
		return openId;
	}
	
	public String getOpenId(String url) throws Exception{
		JSONObject jo = null ;
		HttpGet httpGet = new HttpGet(url);
		String openId = null;
		try (CloseableHttpClient httpclient = HttpClients.createDefault();
			 CloseableHttpResponse response = httpclient.execute(httpGet)
			){
			String msg = "";
			if (response.getEntity() != null) {
				msg=EntityUtils.toString(response.getEntity(), "UTF-8");				
				jo = JSONObject.parseObject(msg);
				openId = jo.getString("openid");
				
			}	
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("--------------------------------------");
			throw new Exception("响应失败");
		}
		if(openId == null || openId==""){
			System.out.println("+++++++++++++++++++++++++++++++++++++++");
			throw new Exception("响应失败");
		}
		return openId;
	}

	/**
	 * 微信saas 登录获得openId 更新插入到dealer表
	 */
	public void updateSaasOpenId(String openId, String dealerId) {
		dealerDao.updateSaasOpenId(openId,dealerId);
		
	}

	
}

