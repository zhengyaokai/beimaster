package bmatser.util;

import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSONObject;
/**
 * 会员登录状态操作类
 * @author felx
 * @date 2017-12-15
 */
public class MemberTools {
	/**判断是否会员是否登陆
	 * @return 会员ID，如果为null则代表，用户没有登陆
	 * */
	public static LoginInfo isLogin(HttpServletRequest request){
		try {
			String cookieId = CookieTools.getCookieValue(Constants.loginCookieName,request);
			if (cookieId != null) {
				cookieId = getFingerprint(request,cookieId);
				WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
				MemcachedClient memcachedClient =  webApplicationContext.getBean(MemcachedClient.class);
				LoginInfo loginInfo = (LoginInfo) memcachedClient.get(cookieId);
				if (loginInfo != null) {
					return loginInfo;
				}
			}
		}catch (Exception e) {
			return null;
		}
		return null;
	}
	
	/**SAAS判断是否会员是否登陆
	 * @return 会员ID，如果为null则代表，用户没有登陆
	 * */
	public static LoginInfo isSaasLogin(HttpServletRequest request){
		try {
			String cookieId = CookieTools.getCookieValue(Constants.saasLoginCookieName,request);
			if (cookieId != null) {
				cookieId = getFingerprint(request,cookieId);
				WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
				MemcachedClient memcachedClient =  webApplicationContext.getBean(MemcachedClient.class);
				LoginInfo loginInfo = (LoginInfo) memcachedClient.get(cookieId);
				if (loginInfo != null) {
					return loginInfo;
				}
			}
		}catch (Exception e) {
			return null;
		}
		return null;
	}
/*	public static LoginInfo isSaasLogin(HttpServletRequest request){
		try {
			String info = request.getParameter("loginInfo");
			Cookie[] c = request.getCookies();
			
			if (StringUtils.isNotBlank(info)) {
				LoginInfo loginInfo = JSONObject.toJavaObject(JSONObject.parseObject(URLDecoder.decode(info,"utf-8")), LoginInfo.class);
				return loginInfo;
			}
		}catch (Exception e) {
			return null;
		}
		return null;
	}*/
	
	/**得到用户的请求的登陆指纹
	 * @throws NoSuchAlgorithmException */
	public static String getFingerprint(HttpServletRequest request,String loginId) throws NoSuchAlgorithmException{
		String Agent = request.getHeader("User-Agent"); 
		String ip=getIp(request);
//		String str=loginId+Agent+ip;
		String str=loginId+Agent;
		return encryption(str);
	}
	
	/**得到用户IP*/
	public static String getIp(HttpServletRequest request){
		 String ip=request.getHeader("X-Forwarded-For");
		    if(ip == null || ip.length() == 0) {
		        ip=request.getHeader("WL-Proxy-Client-IP");
		    }
		    if(ip == null || ip.length() == 0) {
		        ip=request.getRemoteAddr();
		    }
		return ip;
	}
	
	
	
	public static String getRemoteIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("http_client_ip");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip != null) {

			if (ip.indexOf(",") != -1) {
				String[] values = ip.split("\\,");
				ip = values[(Constants.NginxIP).intValue()].trim();
			} else if (ip.indexOf(":") != -1) {
				ip = "127.0.0.1";
			}
		}
		return ip;
	}
	
	
	
	/**得到cookie值*/
	public static String getCookieValue(HttpServletRequest request,String name){
		Cookie[] cookie = request.getCookies();
		if(cookie!=null){
			for(Cookie c:cookie){
				if(c.getName().equals(name)){
					return c.getValue();
				}
			}
		}
		return null;
	}
	
	/**得到cookie值*/
	public static String getCookieContiansValue(HttpServletRequest request,String name){
		Cookie[] cookie = request.getCookies();
		if(cookie!=null){
			for(Cookie c:cookie){
				if(c.getName().indexOf(name) > 0){
					return c.getValue();
				}
			}
		}
		return null;
	}
	
	/**删除cookie*/
	public static void deleteCookie(HttpServletRequest request,HttpServletResponse response,String name){
		Cookie[] cookie = request.getCookies();
		if(cookie!=null){
			for(Cookie c:cookie){
				if(c.getName().equals(name)){
					 c.setMaxAge(0);   
					 response.addCookie(c);   
					 break;
				}
			}
		}
	}
	
	/**得到加密的串
	 * @throws NoSuchAlgorithmException */
	public static String encryption(String data) throws NoSuchAlgorithmException{
		return encryptBASE64(encryptSHA(data));
	}

	public static byte[] encryptSHA(String data) throws NoSuchAlgorithmException  {  
        MessageDigest sha = MessageDigest.getInstance("SHA-1");  
        sha.update(data.getBytes());  
        return sha.digest();  
  
    }  
	public static byte[] decryptBASE64(String key){  
	    return new Base64().decode(key);  
	}  
	/**
	 *  得到浏览器信息
	 * */
	 public static String getUserAgent (HttpServletRequest request){
		 return null;
	 }
	/** 
	 * BASE64加密 
	 *  
	 * @param key 
	 * @return 
	 * @throws Exception 
	 */  
	public static String encryptBASE64(byte[] key){  
		String s=new Base64().encodeToString(key);
	    return s.substring(0,s.length()-1);  
	}  
}
