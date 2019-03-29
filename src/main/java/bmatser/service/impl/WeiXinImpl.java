package bmatser.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import bmatser.dao.WeiXinMapper;
import bmatser.exception.GdbmroException;
import bmatser.service.WeiXinI;
import bmatser.util.Constants;
import bmatser.util.CookieTools;
import bmatser.util.IdBuildTools;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;
@Service("weiXinService")
public class WeiXinImpl implements WeiXinI{
	//微信商城获取openId
    public static final String GET_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxd8fd26a586e9708a&secret=7beb85850e3764cdd1428a7d0ea82ac6&code=CODE&grant_type=authorization_code";
    
    //微信SAAS appId  
    @Value("#{configProperties['wxsaasAppId']}")
	private  String wxsaasAppId;
    //微信SAAS appId 密码
    @Value("#{configProperties['wxsaasAppSecret']}")
	private  String wxsaasAppSecret;
    
    //微信SAAS获取openId
    //public  String WXSAAS_OPENID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code"; 
    
    @Autowired
	private MemcachedClient memcachedClient;
    @Autowired
	private WeiXinMapper weiXinDao;
	@Override
	public ResponseMsg getOpenId(String code, String state,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, GdbmroException {
		System.out.println("----in getOpenId");
		ResponseMsg msg = new ResponseMsg();
		String cookieId = null;
		cookieId = CookieTools.getCookieValue(Constants.loginCookieName,request);
		if (cookieId != null) {
			cookieId = MemberTools.getFingerprint(request,cookieId);
			memcachedClient.delete(cookieId);
		}
		/*if("888".equals(code)){
			Map map = weiXinDao.getDealerInfo("123456");
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
		}*/
		String requestUrl = GET_TOKEN_URL.replace("CODE", code);	
		String openId = getOpenId(requestUrl);
		Map map = weiXinDao.getDealerInfo(openId);
		if(map != null && !map.isEmpty()){//根据openId查到登录信息
			LoginInfo loginInfo = new LoginInfo();
			loginInfo.setDealerId(map.get("dealerId").toString());
			loginInfo.setDealerName(map.get("dealerName")==null?map.get("userName").toString():map.get("dealerName").toString());
			loginInfo.setLoginId(map.get("loginId").toString());
			loginInfo.setUsername(map.get("userName").toString());
			cookieId = CookieTools.getCookieValue(Constants.loginCookieName,request);
			if (cookieId == null) {
				cookieId = IdBuildTools.creatId("1DD98F367");
				CookieTools.setCookie(Constants.loginCookieName, cookieId,31536000, response);
			}
			cookieId = MemberTools.getFingerprint(request,cookieId);
			memcachedClient.set(cookieId, 60 * 30,loginInfo);//30分钟
			
			String openIdCookieId = CookieTools.getCookieValue(Constants.wxloginCookieName,request);
			if (openIdCookieId != null) {
				openIdCookieId = MemberTools.getFingerprint(request,cookieId);
				memcachedClient.delete(openIdCookieId);
				openIdCookieId = IdBuildTools.creatId("wxlg");
				CookieTools.setCookie(Constants.wxloginCookieName, openIdCookieId,31536000, response);			
			}
			openIdCookieId = MemberTools.getFingerprint(request,openIdCookieId);
			memcachedClient.set(openIdCookieId, 60 * 1,openId);//1分钟
			
			msg.setCode(32006);
		}else{//根据openId没查到登录信息
			cookieId = CookieTools.getCookieValue(Constants.wxloginCookieName,request);
			if (cookieId != null) {
				cookieId = MemberTools.getFingerprint(request,cookieId);
				memcachedClient.delete(cookieId);
				cookieId = IdBuildTools.creatId("wxlg");
				CookieTools.setCookie(Constants.wxloginCookieName, cookieId,31536000, response);			
			}
			cookieId = MemberTools.getFingerprint(request,cookieId);
			memcachedClient.set(cookieId, 60 * 1,openId);//1分钟
			
			msg.setCode(32005);
		}
		
		msg.setData(openId);
		return msg;
		
	}
	
	@Override
	public String getWXSaasOpenId(String code) throws Exception,GdbmroException{
		
		String requestUrl =  "https://api.weixin.qq.com/sns/oauth2/access_token?" +
				"appid="+wxsaasAppId+"&secret="+wxsaasAppSecret+"&code="+code+"&grant_type=authorization_code";		
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
				System.out.println(jo);
				openId = jo.getString("openid");
				
			}	
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("响应失败");
		}
		if(openId == null || openId==""){
			throw new Exception("");
		}
		return openId;
	}

	@Override
	public void saveDealerInvited(HttpServletRequest request) throws Exception {
		// 将解析结果存储在HashMap中  
        Map<String, String> map = new HashMap<String, String>();  
        // 从request中取得输入流  
        StringBuffer sb = new StringBuffer(); 
        InputStream inputStream = request.getInputStream();  
        /*InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");  
        BufferedReader br = new BufferedReader(isr);
        while((br.readLine())!=null){
        	String s = br.readLine();
        	sb.append(s);
        }
        String xml = sb.toString();*/
        // 读取输入流  
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream); 
        // 得到xml根元素  
        Element root = document.getRootElement();  
        // 得到根元素的全部子节点  
        List<Element> elementList = root.elements();  
        // 遍历全部子节点  
        for (Element e : elementList)  {
            map.put(e.getName(), e.getText()); 
        }
        // 释放资源  
        inputStream.close();  
        inputStream = null; 
        if(map != null && !map.isEmpty()){
        	String dealerId = "";
        	if("subscribe".equals(map.get("Event"))){//用户未关注，关注后推送过来的信息
        		dealerId = map.get("EventKey").replace("qrscene_", "").toString().trim();
        	}else if("SCAN".equals(map.get("Event"))){//用户已关注 推送过来的信息
        		dealerId = map.get("EventKey").toString().trim();
        	}
        	String invitedOpenId = map.get("FromUserName").toString().trim();
        	if(StringUtils.isNotBlank(dealerId)&&StringUtils.isNotBlank(invitedOpenId)){
        	    int i = weiXinDao.selectDealerInvitedInfo(invitedOpenId);
        	    if(i==0){
        	    	weiXinDao.insertDealerInvited(dealerId,invitedOpenId);
        	    }
        	}
        }

	
	}

	@Override
	public void saveOpenId(String openId) {
		if(openId!=null && openId!=""){
			weiXinDao.saveOpenId(openId);
		}		
	}

	@Override
	public void saveOpenIdByDealerId(String openId, String dealerId) {
		if(openId!=null && openId!=""){
			weiXinDao.saveOpenIdByDealerId(openId,dealerId);
		}	
		
	}
	
	/**
	 * 根据openId获取登录信息
	 * @author felx
	 * @date 2016-10-19
	 */
	public Map getLoginInfo(String openId){
		Map map = weiXinDao.getDealerInfo(openId);
		return map;
	}

	/**
	 * 根据openId获取微信saas登录信息
	 * @author felx
	 * @date 2017-01-04
	 */
	public Map getWXSaasLoginInfo(String openId) {
		Map map = weiXinDao.getWXSaasDealerInfo(openId);
		return map;
	}

}
