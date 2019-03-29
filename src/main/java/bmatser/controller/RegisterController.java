package bmatser.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

//import bmatser.service.MongoServiceI;
import bmatser.annotations.Login;
import bmatser.annotations.ResponseDefault;
import bmatser.exception.GdbmroException;
import bmatser.model.LoginInfoUtil;
import bmatser.pageModel.LoginLogPage;
import bmatser.pageModel.PageMode;
import bmatser.pageModel.RegisterPage;
import bmatser.pageModel.util.RegisterPageUtil;
import bmatser.service.RegisterI;
import bmatser.util.Constants;
import bmatser.util.CookieTools;
import bmatser.util.DateTypeHelper;
import bmatser.util.Encrypt;
import bmatser.util.IdBuildTools;
import bmatser.util.IpUtil;
import bmatser.util.JSONUtil;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.RSAUtil;
import bmatser.util.ResponseMsg;

@Controller
@RequestMapping("user")
@Api(value="user", description="用户注册")
public class RegisterController {

	@Autowired
	private RegisterI registerService;
	@Autowired
	private MemcachedClient memcachedClient;
//	@Autowired
//	private MongoServiceI mongoService;
	
	@Value("#{configProperties['companyNature']}")//商城公司性质
	private  String companyNature;
	
	/**
	 * 登录页面获取key
	 */
	@RequestMapping(value="/to_login", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "登录页面获取key", 
	notes = "登录页面获取key")
	public ResponseMsg loginRequest () {
		ResponseMsg msg = new ResponseMsg();
		try {
			RSAPublicKey rsap = (RSAPublicKey) RSAUtil.generateKeyPair().getPublic();
			String module = rsap.getModulus().toString(16);
			String empoent = rsap.getPublicExponent().toString(16);
			Map<String,String> map = new HashMap<String,String>();
			map.put("mm", module);
			map.put("ee", empoent);	
			msg.setData(map);
			msg.setCode(0);
		} catch (Exception e) {
			/*程序错误*/	         
			e.printStackTrace();
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}	
	
	/**
	 * Saas登陆(旧版)
	 * @param loginName
	 * @param password
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/login/{loginName}/{password}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Saas登陆(旧版)")
	public ResponseMsg login(HttpServletRequest request,
			@ApiParam(name="loginName",required=true,value="用户名") @RequestParam String loginName, 
			@ApiParam(name="password",required=true,value="密码") @RequestParam String password,
			HttpSession session,HttpServletResponse response){
		return saasLoginUtil(request,response,loginName,password);
	}
	/**
	 * Saas登陆(新版) APP  
	 * /saasLogin 手机端 ,
	 * /user_login pc端 
	 * @param loginName
	 * @param password
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/{channel}", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "Saas登陆(新版) APP", 
	notes = "Saas登陆(新版) APP  ")
	public ResponseMsg saasLogin(HttpServletRequest request,HttpServletResponse response,HttpSession session,String loginName,String password,String vcode,@PathVariable("channel")String channel,String type){
			if(!StringUtils.equals(channel, "saasLogin")){
				ResponseMsg msg = loginVaild(request,vcode);
				if(msg.getCode() !=0){
					return msg;
				}
				try {
					/* 解密后的原文 */
					if(type==null||!type.equals("ios")){
                        loginName = RSAUtil.getDecryptedString(loginName);
                        password = RSAUtil.getDecryptedString(password);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return saasLoginUtil(request,response,loginName,password);
	}
	
	
	
	/**
	 * 微信小程序Saas登陆(新版) 
	 * /saasLogin 手机端 ,
	 * /user_login pc端 
	 * @param loginName
	 * @param password
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/wx/{channel}", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "Saas登陆(新版) APP", 
	notes = "Saas登陆(新版) APP  ")
	public ResponseMsg wxSaasLogin(HttpServletRequest request,HttpServletResponse response,HttpSession session,String loginName,String password,String vcode,@PathVariable("channel")String channel){
			if(!StringUtils.equals(channel, "saasLogin")){
				ResponseMsg msg = loginVaild(request,vcode);
				if(msg.getCode() !=0){
					return msg;
				}
				try {
					/* 解密后的原文 */
					password = RSAUtil.getDecryptedString(password);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return saasLoginUtil(request,response,loginName,password);
	}

//	/**
//	 * 积分商城登录
//	 * @param loginName
//	 * @param password
//	 * @param session
//	 * @return
//	 */
//	@RequestMapping(value="/{channel}/jf", method=RequestMethod.GET,produces="text/html;charset=UTF-8")
//	@ResponseBody
//	@ApiOperation(value = "积分商城登录")
//	public String jfscLogin(HttpServletRequest request,HttpServletResponse response,
//			HttpSession session,String loginName,String password,String vcode,
//			@PathVariable("channel")String channel,
//			@ApiParam(value="jsonp返回")@RequestParam String callback){
//		ResponseMsg msg = new ResponseMsg();	
//		if(!StringUtils.equals(channel, "saasLogin")){
//				msg = loginVaild(request,vcode);
//				if(msg.getCode() !=0){
//					return callback + "(" + JSONUtil.objectToJson(msg).toString() + ")";
//				}
//				try {
//					 /*解密后的原文 */
//					loginName = RSAUtil.getDecryptedString(loginName);
//					password = RSAUtil.getDecryptedString(password);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//		}
//		msg=saasLoginUtil(request,response,loginName,password);
//		return callback + "(" + JSONUtil.objectToJson(msg).toString() + ")";
//	}
//	
	/**
	 * Saas修改密码(旧版)
	 * @param oldPassword
	 * @param newPassword
	 * @param loginId
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/mobeditpwd", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "Saas修改密码(旧版)", 
	notes = "Saas修改密码(旧版)")
	public ResponseMsg editPassword(String oldPassword,String newPassword,String loginId,HttpServletRequest request) {
		return saasEditPassword(request,loginId,newPassword,oldPassword);
	}
	/**
	 * Saas修改密码(新版)
	 * @param oldPassword
	 * @param newPassword
	 * @param loginId
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saasEditpwd", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "Saas修改密码(新版)", 
	notes = "Saas修改密码(新版)")
	public ResponseMsg saasEditPassword(String oldPassword,String newPassword,HttpServletRequest request) {
		return saasEditPassword(request,null,newPassword,oldPassword);
	}
	/**
	 * Saas找回密码(兼容)
	 * @param request
	 * @param code
	 * @param mobile
	 * @param newPassword
	 * @return
	 */
	@RequestMapping(value="/getpwd", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "Saas找回密码(兼容)", 
	notes = "Saas找回密码(兼容)")
	public ResponseMsg getpwd(HttpServletRequest request,String code,String mobile,String newPassword) {
		return saasResetPassword(request,code,mobile,newPassword);
	}

	/**
	 * 注册用户(兼容)
	 * @param registerPage
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mobRegister",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "注册用户(兼容)", 
	notes = "注册用户(兼容)")
	public ResponseMsg mobRegister(RegisterPage registerPage, HttpServletRequest request, HttpSession session){
		ResponseMsg msg = new ResponseMsg();
		registerPage.setRegisterIp(IpUtil.getIpAddr(request));
		try {
			registerPage.setDealerName(registerPage.getMobile());
			msg.setData(registerService.saveMobRegister(registerPage, request,session));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * 商城登陆出错次数
	 * @param loginName
	 * @param password
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/toMallLogin", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "商城登陆出错次数", 
	notes = "商城登陆出错次数")
	public ResponseMsg toMallLogin(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		ResponseMsg msg = new ResponseMsg();
		String cookieId = CookieTools.getCookieValue(Constants.loginCookieName,request);
		if(cookieId!=null){
			try{
				String loginCountKey = MemberTools.getFingerprint(request,Constants.loginCountKey+cookieId);
				Integer loginCount = memcachedClient.get(loginCountKey);
				Map map = new HashMap();
				map.put("loginCount", loginCount==null?0:loginCount);
				msg.setData(map);
				msg.setCode(0);
			}catch (Exception e) {
				msg.setError(e);
			}
		}
		return msg;
	}

	@RequestMapping(value="/getOpenId")
	@ResponseBody
	@Deprecated
	public ResponseMsg getOpenId(String code,String state,HttpServletRequest request,HttpServletResponse response){
		ResponseMsg msg = new ResponseMsg();
		try {
			registerService.getOpenId(code,state,request,response);
			
			msg.setCode(0);
		}catch (GdbmroException e) {
			msg.setError(e);
			msg.setCode(e.getCode());
		} catch (Exception e) {
			msg.setError(e);
		}
		
		return msg;
	}
	/**
	 * 商城登陆
	 * @param loginName
	 * @param password
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mallLogin", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "商城登陆", 
	notes = "商城登陆")
	public ResponseMsg mallLogin(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		ResponseMsg msg = new ResponseMsg();
		String loginName = request.getParameter("loginName");
		String password = request.getParameter("password");
		String vcode = request.getParameter("vcode");
//		if (vcode == null){
//			msg.setCode(-1);
//			msg.setMsg("请输入验证码！");
//			return msg;
//		}
		try{
			//获取登录输错次数
			String cookieId = CookieTools.getCookieValue(Constants.loginCookieName,request);
			if(cookieId!=null){
				String loginCountKey = MemberTools.getFingerprint(request,Constants.loginCountKey+cookieId);
				Integer loginCount = memcachedClient.get(loginCountKey);
				if(null != loginCount && loginCount >= 3){
					if(vcode == null){
						msg.setCode(-1);
						msg.setMsg("请输入验证码");
						Map map = new HashMap();
						map.put("loginCount", loginCount);
						msg.setData(map);
						return msg;
					}
					String checkCode = (String) memcachedClient.get(MemberTools.getFingerprint(request,CookieTools.getCookieValue(Constants.checkCodeCookieName,request)));
					if (checkCode == null || !vcode.equalsIgnoreCase(checkCode)){
						msg.setCode(-1);
						msg.setMsg("验证码输入错误");
						Map map = new HashMap();
						map.put("loginCount", loginCount);
						msg.setData(map);
						return msg;
					}
					checkCode = null;
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		
		if (loginName == null || password == null || "".equals(loginName.trim()) || "".equals(password.trim())){
			msg.setCode(-1);
			msg.setMsg("用户名或者密码不能为空");
			return msg;
		}
		String cookieId = CookieTools.getCookieValue(Constants.loginCookieName,request);
		if (cookieId == null) {
			cookieId = IdBuildTools.creatId("1DD98F367");
			CookieTools.setCookie(Constants.loginCookieName, cookieId,31536000, response);
		}
			
		LoginInfo loginInfo = new LoginInfo();
		try {
			Map map=registerService.mallLogin(request,loginName.trim(), Encrypt.md5AndSha(password.trim()),"4,5");
			msg.setData(map);
			loginInfo.setDealerId(map.get("dealerId").toString());
			loginInfo.setDealerName(map.get("dealerName")==null?map.get("userName").toString():map.get("dealerName").toString());
			loginInfo.setLoginId(map.get("loginId").toString());
			loginInfo.setUsername(map.get("userName").toString());
			if(null != map.get("dealerType")){
				loginInfo.setDealerType(map.get("dealerType").toString());
			}
//			loginInfo.setCash(map.get("cash").toString());
			//清除用户登录错误数
			String loginCountKey = MemberTools.getFingerprint(request,Constants.loginCountKey+cookieId);
			memcachedClient.delete(loginCountKey);
			
			cookieId = MemberTools.getFingerprint(request,cookieId);
			memcachedClient.set(cookieId, 60 * 60*24,loginInfo);//30分钟
			msg.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			msg.setCode(-1);
			if("error".equals(e.getMessage())){
				msg.setError("帐号不存在或密码错误");
				//记录用户登录错误数
				try{
					String loginCountKey = MemberTools.getFingerprint(request,Constants.loginCountKey+cookieId);
					Integer loginCount = memcachedClient.get(loginCountKey);
					loginCount = (loginCount==null?0:loginCount)+1;
					memcachedClient.set(loginCountKey, 60 * 30,loginCount);//30分钟
//					memcachedClient.delete(loginCountKey);
					Map map = new HashMap();
					map.put("loginCount", loginCount);
					msg.setData(map);
					msg.setCode(-1);
				}catch (Exception ex) {
					ex.printStackTrace();
					msg.setError(ex.getMessage());
				}
			}
		}
		
		return msg;
	}
	
	/**
	 * APP商城登陆
	 * @param loginName
	 * @param password
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/appMallLogin", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "APP商城登陆", 
	notes = "APP商城登陆")
	public ResponseMsg appMallLogin(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		ResponseMsg msg = new ResponseMsg();
		String loginName = request.getParameter("loginName");
		String password = request.getParameter("password");
		
		if (loginName == null || password == null || "".equals(loginName.trim()) || "".equals(password.trim())){
			msg.setCode(-1);
			msg.setMsg("用户名或者密码不能为空");
			return msg;
		}
		
		String cookieId = CookieTools.getCookieValue(Constants.loginCookieName,request);
		if (cookieId == null) {
			cookieId = IdBuildTools.creatId("1DD98F367");
			CookieTools.setCookie(Constants.loginCookieName, cookieId,31536000, response);
		}
			
		LoginInfo loginInfo = new LoginInfo();
		try {
			Map map=registerService.mallLogin(request,loginName.trim(), password.trim(),"4,5");
			msg.setData(map);
			loginInfo.setDealerId(map.get("dealerId").toString());
			loginInfo.setDealerName(map.get("dealerName")==null?map.get("userName").toString():map.get("dealerName").toString());
			loginInfo.setLoginId(map.get("loginId").toString());
			loginInfo.setUsername(map.get("userName").toString());
			if(null != map.get("dealerType")){
				loginInfo.setDealerType(map.get("dealerType").toString());
			}
			
			cookieId = MemberTools.getFingerprint(request,cookieId);
			memcachedClient.set(cookieId, 60 * 60*24,loginInfo);//30分钟
			msg.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			msg.setCode(-1);
			if("error".equals(e.getMessage())){
				msg.setError("帐号不存在或密码错误");
			}
		}
		return msg;

	}
	

	/**
	 * 商城退出
	 * @param loginName
	 * @param password
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mallLoginOut", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "商城退出", 
	notes = "商城退出")
	public ResponseMsg mallLoginOut(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		ResponseMsg msg = new ResponseMsg();
		
		try {
			String cookieId = CookieTools.getCookieValue(Constants.loginCookieName,request);
			if (cookieId != null) {
				cookieId = MemberTools.getFingerprint(request,cookieId);
				memcachedClient.delete(cookieId);
					msg.setCode(0);
			}
		} catch (Exception e) {
			msg.setError(e);
		}
		
		return msg;
	}
	/**
	 * 积分商城登录退出
	 * @param loginName
	 * @param password
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/jfscLoginOut",method=RequestMethod.GET,produces="text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "积分商城登录退出", 
	notes = "积分商城登录退出")
	public String jfscLoginOut(HttpServletRequest request,HttpServletResponse response,HttpSession session,
			@ApiParam(value="jsonp返回")@RequestParam String callback){
		ResponseMsg msg = new ResponseMsg();
		try {
			String cookieId = CookieTools.getCookieValue(Constants.saasLoginCookieName,request);
			if (cookieId != null) {
				//将浏览器cookie删除
				CookieTools.deleteCooke(cookieId, response);
				cookieId = MemberTools.getFingerprint(request,cookieId);
				memcachedClient.delete(cookieId);
				msg.setCode(0);
			}
		} catch (Exception e) {
			msg.setError(e);
		}
		return callback + "(" + JSONUtil.objectToJson(msg).toString() + ")";
	}
	
	/**
	 * saas登录退出
	 * @param loginName
	 * @param password
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/saasLoginOut", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "saas登录退出", 
	notes = "saas登录退出")
	public ResponseMsg saasLoginOut(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		ResponseMsg msg = new ResponseMsg();
		try {
			String cookieId = CookieTools.getCookieValue(Constants.saasLoginCookieName,request);
			if (cookieId != null) {
				//将浏览器cookie删除
				CookieTools.deleteCooke(Constants.saasLoginCookieName, response);
				cookieId = MemberTools.getFingerprint(request,cookieId);
				memcachedClient.delete(cookieId);
				msg.setCode(0);
			}
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 判断是否登录
	 * @author felx
	 */
	@RequestMapping(value="/isSaasLogin", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "判断是否登录", 
	notes = "判断是否登录")
	public ResponseMsg isjfLogin(HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		HttpSession session = request.getSession();
		try {
			LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
			if(loginInfo !=null && loginInfo.getLoginId() != null){
				msg.setCode(0);
				msg.setData(loginInfo);
			}else{
				msg.setCode(-1);
				msg.setMsg("请重新登陆");
			}
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 判断是否登录
	 * @author felx
	 */
	@RequestMapping(value="/isLogin", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "判断是否登录", 
	notes = "判断是否登录")
	public ResponseMsg isLogin(HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		msg.setCode(-1);
		try {
			String cookieId = CookieTools.getCookieValue(Constants.loginCookieName,request);
			if (cookieId != null) {
				cookieId = MemberTools.getFingerprint(request,cookieId);
				LoginInfo loginInfo = (LoginInfo) memcachedClient.get(cookieId);
				if (loginInfo != null) {
					msg.setCode(0);
					/*Member member = userPageService.checkMemberLogin(memberId);
					if (member != null) {
						loginObj.put("isLogin", true);
						member.setLoginPassword(null);
						loginObj.put("member", member);
					}*/
					msg.setData(loginInfo);
				}
			}
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 注册（新版）
	 * @param request
	 * @param response
	 * @param registerPage
	 * @param channel mall:商城 
	 * @return
	 */
	@RequestMapping(value="/{channel}/register",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "注册（新版）", 
	notes = "注册（新版）")
	public ResponseMsg toMallRegister(HttpServletRequest request,HttpServletResponse response,
			RegisterPageUtil registerPage,@PathVariable("channel")String channel)
	{
		ResponseMsg msg = new ResponseMsg();
		Map<String,Object> data = null;
		registerPage.setRegisterIp(IpUtil.getIpAddr(request));
		try {
			switch (channel) {
				case "mall":
					registerPage.setMobile(registerPage.getUserName());
					registerService.isMallRegisterSms(registerPage,request);
					data=registerService.toMallRegister(registerPage);
					addLoginInfoToMemcache(data,request,response);
				break;
			}
			msg.setData(data);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}

//	/**
//	 * 商城APP注册查询公司性质
//	 * @return
//	 */
//	@RequestMapping(value="/getCompanyNature",method=RequestMethod.GET)
//	@ResponseBody
//	@ApiOperation(value = "商城APP注册查询公司性质", 
//	notes = "商城APP注册查询公司性质")
//	public ResponseMsg getCompanyNature(){
//		ResponseMsg msg = new ResponseMsg();
//		try {
//			JSONArray json = JSONArray.parseArray(companyNature);
//			msg.setData(json);
//		} catch (Exception e) {
//			msg.setError(e);
//		}
//		return msg;
//	}

	/**
	 * 商城注册用户
	 * @param registerPage
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/mallRegister", method=RequestMethod.POST,name="商城注册")
	@ResponseBody
	@ApiOperation(value="商城注册")
	public ResponseMsg mallRegister(@ModelAttribute RegisterPage registerPage, HttpServletRequest request, HttpSession session,HttpServletResponse response){
		ResponseMsg msg = new ResponseMsg();
		registerPage.setRegisterIp(IpUtil.getIpAddr(request));
		try {
			Map regMap = registerService.addMallRegister(registerPage,request, session);
			msg.setData(regMap);
			msg.setCode(0);
			
			//注册完成后登陆 start
			if(null != regMap){
				try{
					LoginInfo loginInfo = new LoginInfo();
//					Map map=registerService.mallLogin(request,registerPage.getUserName().trim(), registerPage.getPassword(),"4");
					loginInfo.setDealerId(regMap.get("dealerId").toString());
					loginInfo.setDealerName(registerPage.getUserName());
					loginInfo.setLoginId(regMap.get("loginId").toString());
					loginInfo.setUsername(registerPage.getUserName());
//					loginInfo.setCash(map.get("cash").toString());
					
					String cookieId = IdBuildTools.creatId("1DD98F367");
					CookieTools.setCookie(Constants.loginCookieName, cookieId, response);
					cookieId = MemberTools.getFingerprint(request,cookieId);
					memcachedClient.set(cookieId, 60 * 30,loginInfo);//30分钟
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			//注册完成后登陆 end
			
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 注册用户SAAS
	 * @param registerPage
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "注册用户SAAS", 
	notes = "注册用户SAAS")
	public ResponseMsg add(RegisterPage registerPage, HttpServletResponse response,HttpServletRequest request, HttpSession session){
		ResponseMsg msg = new ResponseMsg();
		registerPage.setRegisterIp(IpUtil.getIpAddr(request));
		try {//registerService.add(registerPage, session)
			registerPage.setRegChannel("1");
			msg.setData(registerService.saveMobRegister(registerPage, request,session));
			saasLoginUtil(request,response,registerPage.getUserName(),registerPage.getPassword());
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e.getMessage());
		}
		return msg;
	}

	
	/**
	 * 修改密码
	 * @author felx
	 */
	@RequestMapping(value="/editpwd", method=RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "修改密码", 
	notes = "修改密码")
	public ResponseMsg editPassword(String oldPassword,String newPassword,HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return msg.setError("请先登录");
		}
		try {
			int result = registerService.editPassword(Integer.valueOf(loginInfo.getLoginId()), Encrypt.md5AndSha(oldPassword), Encrypt.md5AndSha(newPassword));
			if(result==1){
				msg.setCode(0);
			}else{
				msg.setError("原密码不正确");
			}
		} catch (Exception e) {
			msg.setError(e);
		}
		
		return msg;
	}


	/**
	 * 找回密码
	 * @author felx
	 */
	@RequestMapping(value="/resetMallPwd", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "找回密码", 
	notes = "找回密码")
	public ResponseMsg resetMallpwd(HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		String mobile = request.getParameter("mobile");
		String code = request.getParameter("code");
		String newPassword = request.getParameter("newPassword");
		try {
			int i = registerService.updateMallPasswprd(mobile,code,newPassword,request);
			if(i>0){
				msg.setCode(0);
			}else{
				msg.setCode(-1);
				msg.setMsg("找回密码错误");
			}
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		
		return msg;
	}
	/**
	 * 查看账户信息
	 * @param loginId
	 * @return
	 */
	@RequestMapping(value="/account/{dealerId}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查看账户信息", 
	notes = "查看账户信息")
	public ResponseMsg getUserAccount(HttpServletRequest request,@PathVariable Integer dealerId) {
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(loginInfo==null){
			throw new GdbmroException(-1, "请先登陆!");
		}
		try {
			msg.setData(registerService.getUserAccount(dealerId));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/*------------------------------------------------分割线-------------------------------------------------------*/
	/**
	 * Saas通用登录
	 * @param loginName
	 * @param password
	 * @return
	 */
	private ResponseMsg saasLoginUtil(HttpServletRequest request,
			HttpServletResponse response,String loginName, String password) {
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = null;
		HttpSession session = request.getSession();
		if (StringUtils.isBlank(loginName) || StringUtils.isBlank(password)){
			return msg.setError("用户名或者密码不能为空");
		}
		//微信saas 登录openId更新到dealer
		String openId = request.getParameter("openId");
		try {		
			Map<String, Object> map=registerService.login(request,loginName, password.length()!=40?Encrypt.md5AndSha(password) : password,"2");
			if(map==null){
				map = registerService.login(request,loginName, password.length()!=40?Encrypt.md5AndSha(password) : password,"1");
				if(map==null){
					return msg.setError("帐号不存在或密码错误");
				}
			}
			map.put("cartNum", registerService.loadUserCart(map.get("dealerId").toString()));
			//微信saas 登录获得openId
			if(StringUtils.isNotBlank(openId)){
				registerService.updateSaasOpenId(openId,map.get("dealerId").toString());
				map.put("openId", openId);	
			}
//			msg.setData(map);
			loginInfo = new LoginInfo();
			loginInfo.setDealerId(map.get("dealerId").toString());
			loginInfo.setDealerName(map.get("dealerName")==null?map.get("userName").toString():map.get("dealerName").toString());
			loginInfo.setLoginId(map.get("loginId").toString());
			loginInfo.setUsername(map.get("userName").toString());
			map.put("username", map.get("userName").toString());
			loginInfo.setCash(map.get("cash").toString());
			loginInfo.setScore(map.get("score").toString());
			loginInfo.setCartNum(registerService.loadUserCart(loginInfo.getDealerId()));
			loginInfo.setDealerType(map.get("dealerType").toString());
			loginInfo.setCheckStatus(map.get("checkStatus").toString());
			loginInfo.setRank (Integer.parseInt(map.get("rank").toString()));
			loginInfo.setBean (Integer.parseInt(map.get("bean").toString()));
//			CookieTools.deleteCooke(Constants.saasLoginCookieName, response);
			String cookieId = CookieTools.getCookieValue(Constants.saasLoginCookieName,request);
			if (cookieId == null) {
				cookieId = IdBuildTools.creatId("1DD98F367");
				CookieTools.setCookie(Constants.saasLoginCookieName, cookieId, response);
			}
/*			GenTLSSignatureResult result = tls_sigature.GenTLSSignatureEx(1400022010, loginInfo.getDealerName());
			if (0 == result.urlSig.length()) {
				System.out.println("GenTLSSignatureEx failed: " + result.errMessage);
			}else{
				map.put("zbSign", result.urlSig);
			}*/
			
			map.put("cookieId", cookieId);
			cookieId = MemberTools.getFingerprint(request,cookieId);
            memcachedClient.set(cookieId, 60 * 60 * 24 * 10,loginInfo);//24小时
            memcachedClient.set(loginInfo.getDealerId(), 60 * 60 * 24 * 10,cookieId);//将会员对应的cookid放入缓存
			LoginInfo o = memcachedClient.get(cookieId);
//			LoginInfo loginInfoPage = memcachedClient.get(cookieId);
//			System.out.println("cookieId============================"+cookieId);
//			System.out.println("loginInfo============================"+loginInfoPage.toString());
			session.setAttribute("loginInfo", loginInfo);
			
			msg.setData(map);
			
			//保存登录到mongo
			String loginTime = DateTypeHelper.getCurrentDateTimeString();
			LoginLogPage loginLogPage = new LoginLogPage();
			loginLogPage.setLoginId(loginInfo.getLoginId());
			loginLogPage.setLoginName(loginInfo.getUsername());
			loginLogPage.setDealerId(loginInfo.getDealerId());
			loginLogPage.setLoginTime(loginTime);
			loginLogPage.setType("1");
			loginLogPage.setIp(IpUtil.getIpAddr(request));
			String day = loginTime.split(" ")[0];
			String year = day.split("-")[0];
			String month = year +"-"+ day.split("-")[1];
			String standard = "2017-04-06";
			Date d = new Date();
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			
			Date firstDayOfWeek = getFirstDayOfWeek(d);
			Date lastDayOfWeek = getLastDayOfWeek(d);
			String fd = sd.format(firstDayOfWeek);
			String ld = sd.format(lastDayOfWeek);
			String week = fd+"/"+ld;
			loginLogPage.setDay(day);
			loginLogPage.setMonth(month);
			loginLogPage.setYear(year);
			loginLogPage.setWeek(week);
//			mongoService.save(loginLogPage);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
			e.printStackTrace();
		}
		return msg;
	}
	
	/** 
	  * 取得指定日期所在周的第一天 

	  */ 
	  public static Date getFirstDayOfWeek(Date date) { 
	  Calendar c = new GregorianCalendar(); 
	  c.setFirstDayOfWeek(Calendar.MONDAY); 
	  c.setTime(date); 
	  c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday 
		System.out.println(c.getTime ());
	  return c.getTime (); 
	  }

	  /** 
	  * 取得指定日期所在周的最后一天 
	   */ 
	  public static Date getLastDayOfWeek(Date date) { 
	  Calendar c = new GregorianCalendar(); 
	  c.setFirstDayOfWeek(Calendar.MONDAY); 
	  c.setTime(date); 
	  c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday 
	  System.out.println(c.getTime ());
	  return c.getTime(); 
	  }
	
	/**
	 * Saas修改密码通用
	 * @param request
	 * @param loginId
	 * @param newPassword
	 * @param oldPassword
	 * @return
	 */
	private ResponseMsg saasEditPassword(HttpServletRequest request,
			String loginId, String newPassword, String oldPassword) {
		ResponseMsg msg = new ResponseMsg();
		if(StringUtils.isBlank(loginId)){
			LoginInfo loginInfo = MemberTools.isSaasLogin(request);
			if(null == loginInfo || null == loginInfo.getDealerId()){
				return msg.setError("请先登录");
			}else{
				loginId = loginInfo.getLoginId();
			}
		}
		if(StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword) || StringUtils.isBlank(loginId)){
			return msg.setError("用户名或者密码不能为空");
		}
		oldPassword = oldPassword.length()!=40?Encrypt.md5AndSha(oldPassword) : oldPassword;
		newPassword = newPassword.length()!=40?Encrypt.md5AndSha(newPassword) : newPassword;
		try {
			int code= registerService.editPassword(Integer.valueOf(loginId), oldPassword, newPassword);
			if(code>0){
				msg.setCode(0);
			}else{
				msg.setError("原密码错误");
			}
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 找回密码通用
	 * @param request
	 * @param code
	 * @param mobile
	 * @param newPassword
	 * @return
	 */
	private ResponseMsg saasResetPassword(HttpServletRequest request,
			String code, String mobile, String newPassword) {
		ResponseMsg msg = new ResponseMsg();
		if(StringUtils.isBlank(code) || StringUtils.isBlank(mobile) || StringUtils.isBlank(newPassword)){
			return msg.setError("信息未填写完整");
		}
		newPassword = newPassword.length()!=40?Encrypt.md5AndSha(newPassword) : newPassword;
		try {
			registerService.getPasswprd(mobile,code,newPassword,request);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * saas图片验证
	 * @param request
	 * @param vcode
	 * @return
	 */
	private ResponseMsg loginVaild(HttpServletRequest request, String vcode) {
		ResponseMsg msg = new ResponseMsg();
		msg.setCode(0);
		try{
			//获取登录输错次数
			String cookieId = CookieTools.getCookieValue(Constants.loginCookieName,request);
			if(cookieId!=null){
				String loginCountKey = MemberTools.getFingerprint(request,Constants.loginCountKey+cookieId);
				Integer loginCount = memcachedClient.get(loginCountKey);
				if(null != loginCount && loginCount >= 1){
					if(vcode == null){
						msg.setCode(-1);
						msg.setMsg("请输入验证码");
						Map map = new HashMap();
						map.put("loginCount", loginCount);
						msg.setData(map);
						return msg;
					}
					String checkCode = (String) memcachedClient.get(MemberTools.getFingerprint(request,CookieTools.getCookieValue(Constants.checkCodeCookieName,request)));
					if (checkCode == null || !vcode.equalsIgnoreCase(checkCode)){
						msg.setCode(-1);
						msg.setMsg("验证码输入错误");
						Map map = new HashMap();
						map.put("loginCount", loginCount);
						msg.setData(map);
						return msg;
					}
					checkCode = null;
				}
			}
//			memcachedClient.set(CookieTools.getCookieValue(Constants.checkCodeCookieName, request),60, checkCode);
		}catch (Exception e) {
			return msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 查看账户信息
	 * @param loginId
	 * @return
	 */
	@RequestMapping(value="/loadUserInfo", method=RequestMethod.GET)
	@ResponseBody
	public ResponseMsg loadUserInfo(String dealerId,HttpServletResponse response) {
		ResponseMsg msg = new ResponseMsg();
		try {
			String cookieId = memcachedClient.get(dealerId);//将会员对应的cookid放入缓存
			if(null != cookieId){
				//将浏览器cookie删除
				CookieTools.deleteCooke(cookieId, response);
				
				Map map = registerService.getUserInfo(dealerId);
				LoginInfo loginInfo = new LoginInfo();
				loginInfo.setDealerId(map.get("dealerId").toString());
				loginInfo.setDealerName(map.get("dealerName")==null?map.get("userName").toString():map.get("dealerName").toString());
				loginInfo.setLoginId(map.get("loginId").toString());
				loginInfo.setUsername(map.get("userName").toString());
				loginInfo.setCash(map.get("cash")==null?"0":map.get("cash").toString());
				loginInfo.setScore(map.get("score")==null?"0":map.get("score").toString());
				loginInfo.setCartNum(map.get("cartNum")==null?"0":map.get("cartNum").toString());
				loginInfo.setDealerType(map.get("dealerType").toString());
				loginInfo.setCheckStatus(map.get("checkStatus").toString());
				loginInfo.setBean (Integer.parseInt(map.get("bean").toString()));
				memcachedClient.set(cookieId, 60 * 60 * 24,loginInfo);
			}
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 商城注册保存到memcache
	 * @param data
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void addLoginInfoToMemcache(Map<String, Object> data,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setDealerId(data.get("dealerId").toString());
		loginInfo.setDealerName(data.get("userName").toString());
		loginInfo.setLoginId(data.get("loginId").toString());
		loginInfo.setUsername(data.get("userName").toString());
		String cookieId = IdBuildTools.creatId("1DD98F367");
		CookieTools.setCookie(Constants.loginCookieName, cookieId, response);
		cookieId = MemberTools.getFingerprint(request,cookieId);
		memcachedClient.set(cookieId, 60 * 30,loginInfo);
	}
	
	/**
	 * 余额
	 * @param request
	 * @return
	 */
	@RequestMapping(value={"/saas/balance","/mall/balance","/app/balance"},method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "余额", 
	notes = "余额")
	public ResponseMsg getBalance(HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		PageMode model = new PageMode(request);
		try {
			msg.setData(registerService.getBalance(model));
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
		
	}
	
	/**
	 * saas获取用户的积分，抵用券，工币
	 * @param request
	 * @return
	 */
	@RequestMapping(value={"/gdbmall/userDetail"},method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "saas获取用户的积分，抵用券，工币")
	@Login(saas="/gdbmall/userDetail")
	@ResponseDefault
	public Object getSaasUserInfo(HttpServletRequest request,LoginInfoUtil util){
		return registerService.getSaasUserInfo(util.getDealerId());
		
	}
//	/**
//	 * 获取直播密钥
//	 * @param request
//	 * @return
//	 * @throws IOException 
//	 */
//	@RequestMapping(value={"/app/zbSign"},method=RequestMethod.GET)
//	@ResponseBody
//	@ApiOperation(value = "获取直播密钥")
//	//@Login(app="/app/zbSign")
//	@ResponseDefault
//	public Object getZbSign(HttpServletRequest request,
//			String dealerId,
//			String dealerName
//			) throws IOException{
//		if(StringUtils.isBlank(dealerName)){
//			if(StringUtils.isBlank(dealerId)){
//				LoginInfo info = MemberTools.isSaasLogin(request);
//				if(info==null){
//					throw new RuntimeException("无用户名称");
//				}
//				else{
//					dealerName = info.getDealerName();
//				}
//			}
//			else{
//				Map user = registerService.getUserInfo(dealerId);
//				dealerName = user.get("dealerName").toString();
//			}
//		}
//		else{
//			dealerName=URLDecoder.decode(dealerName, "UTF-8");
//		}
//		GenTLSSignatureResult result = tls_sigature.GenTLSSignatureEx(1400024831, dealerName);
//		Map<String, Object> map = new HashMap<String, Object>();
//		if (0 == result.urlSig.length()) {
//			System.out.println("GenTLSSignatureEx failed: " + result.errMessage);
//			throw new RuntimeException("sign错误");
//		}
//		else{
//			map.put("zbSign", result.urlSig);
//			return map;
//		}
//		
//	}
	
}
