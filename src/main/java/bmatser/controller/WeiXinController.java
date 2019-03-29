package bmatser.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.rubyeye.xmemcached.MemcachedClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import bmatser.exception.GdbmroException;
import bmatser.model.Category;
import bmatser.service.RegisterI;
import bmatser.service.WeiXinI;
import bmatser.util.Constants;
import bmatser.util.CookieTools;
import bmatser.util.Encrypt;
import bmatser.util.IdBuildTools;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;

@Controller
@RequestMapping("weixin")
@Api(value="weixin", description="微信")
public class WeiXinController {
	
	@Autowired
	private WeiXinI weiXinService;
	@Autowired
	private RegisterI registerService;
	@Autowired
	private MemcachedClient memcachedClient;
	/**
	 * 解析扫描二维码返回的XML信息并保存
	 * @return
	 */
	@RequestMapping(value="/getXMLInfo",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "解析扫描二维码返回的XML信息并保存", 
	notes = "解析扫描二维码返回的XML信息并保存")
	 public  ResponseMsg parseXml(HttpServletRequest request) throws Exception {  
		 ResponseMsg msg = new ResponseMsg();
		 try {
			 weiXinService.saveDealerInvited(request); 
		} catch (Exception e) {
			msg.setError(e);
		}
		 return msg;
	}  
	
	/**
	 * 微信Saas登录调用此接口：通过code换取openId
	 */
	@RequestMapping(value="/getWXSaasOpenId")
	@ResponseBody
	@ApiOperation(value = "微信Saas登录调用此接口：通过code换取openId", 
	notes = "微信Saas登录调用此接口：通过code换取openId")
	public ResponseMsg getWXSaasOpenId(
			@ApiParam(required=false,name="code",value="微信编码") @RequestParam String code,
			HttpServletRequest request,HttpServletResponse response){
		ResponseMsg msg = new ResponseMsg();
		try {
			String openId = weiXinService.getWXSaasOpenId(code);
			msg.setData(openId);
		}catch (GdbmroException e) {
			msg.setError(e);
			msg.setCode(e.getCode());
		} catch (Exception e) {
			msg.setError(e);
		}	
		return msg;
	}
	
	@RequestMapping(value="/getOpenId")
	@ResponseBody
	@ApiOperation(value = "获得微信openId", 
	notes = "获得微信openId")
	public ResponseMsg getOpenId(@ApiParam(required=false,name="code",value="微信编码") String code,
			@ApiParam(required=false,name="state",value="备注") @RequestParam(required=false)String state,
			HttpServletRequest request,HttpServletResponse response){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg = weiXinService.getOpenId(code,state,request,response);
		}catch (GdbmroException e) {
			msg.setError(e);
			msg.setCode(e.getCode());
		} catch (Exception e) {
			msg.setError(e);
		}
		
		return msg;
	}
	
	/**
	 * 账号绑定页跳过此步骤 保存openId
	 * @param code
	 * @param state
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/saveOpenId")
	@ResponseBody
	@ApiOperation(value = "账号绑定页跳过此步骤 保存openId", 
	notes = "账号绑定页跳过此步骤 保存openId")
	public ResponseMsg saveOpenId(
			@ApiParam(required=false,name="openId",value="公众号Id") @RequestParam(required=false)String openId){
		ResponseMsg msg = new ResponseMsg();
		try {
			weiXinService.saveOpenId(openId);
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
	 * 微信商城登录绑定 保存openId 到dealer表
	 * @param loginName
	 * @param password
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/toWXMallLogin", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "微信商城登录绑定 保存openId 到dealer表", 
	notes = "微信商城登录绑定 保存openId 到dealer表")
	public ResponseMsg mallLogin(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		ResponseMsg msg = new ResponseMsg();
		String loginName = request.getParameter("loginName");
		String password = request.getParameter("password");	
		String openId = request.getParameter("openId");
		if (loginName == null || password == null || "".equals(loginName.trim()) || "".equals(password.trim())){
			msg.setCode(-1);
			msg.setMsg("用户名或者密码不能为空");
			return msg;
		}
		String cookieId = CookieTools.getCookieValue(Constants.loginCookieName,request);
		if (cookieId == null) {
			cookieId = IdBuildTools.creatId("1DD98F367");
			CookieTools.setCookie(Constants.loginCookieName, cookieId, response);
		}
			
		LoginInfo loginInfo = new LoginInfo();
		try {
			Map map=registerService.mallLogin(request,loginName.trim(), Encrypt.md5AndSha(password.trim()),"4,5");
			if(map.get("openId")==null || "".equals(map.get("openId"))){
				weiXinService.saveOpenIdByDealerId(openId,map.get("dealerId").toString());
			}
			msg.setData(map);
			loginInfo.setDealerId(map.get("dealerId").toString());
			loginInfo.setDealerName(map.get("dealerName")==null?map.get("userName").toString():map.get("dealerName").toString());
			loginInfo.setLoginId(map.get("loginId").toString());
			loginInfo.setUsername(map.get("userName").toString());
			
			cookieId = MemberTools.getFingerprint(request,cookieId);
			memcachedClient.set(cookieId, 60 * 60*24,loginInfo);//24小时
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			if("error".equals(e.getMessage())){
				msg.setError("帐号不存在或密码错误");
			}	
		}
		
		return msg;
	}
	
	
	/**
	 * 根据openId获取登录信息
	 * @author felx
	 */
	@RequestMapping(value="/getLoginInfoByOpenId",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "根据openId获取登录信息", 
	notes = "根据openId获取登录信息")
	 public  ResponseMsg getLoginInfoByOpenId(HttpServletRequest request,HttpServletResponse response,
			 @ApiParam(required=false,name="openId",value="公众号Id") @RequestParam(required=false)String openId) throws Exception {  
		ResponseMsg msg = new ResponseMsg();
			
		try {
			Map map = weiXinService.getLoginInfo(openId); 
			msg.setData(map);
		}catch (Exception e) {
			msg.setError(e);
		}
		 return msg;
	} 
	/**
	 * 根据saasopenId获取微信saas登录信息
	 * @author felx
	 */
	@RequestMapping(value="/getLoginInfoBySaasOpenId",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "根据saasopenId获取微信saas登录信息", 
	notes = "根据saasopenId获取微信saas登录信息")
	 public  ResponseMsg getLoginInfoBySaasOpenId(HttpServletRequest request,HttpServletResponse response,
			 @ApiParam(required=false,name="openId",value="公众号Id") @RequestParam(required=false)String openId) throws Exception {  
		ResponseMsg msg = new ResponseMsg();
			
		try {
			Map map = weiXinService.getWXSaasLoginInfo(openId); 
			msg.setData(map);
		}catch (Exception e) {
			msg.setError(e);
		}
		 return msg;
	}  
}
