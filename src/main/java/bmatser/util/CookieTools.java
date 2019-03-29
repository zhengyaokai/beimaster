package bmatser.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.igfay.jfig.JFig;

/**
 * cookie操作类
 * @author felx
 * @date 2017-12-15
 */
public class CookieTools {
	/**写cookie*/
	public static void setCookie(String key,String value,HttpServletResponse response){
		setCookie(key,value,null,response);
	}
	public static void setCookie(String key,String value,Integer time,HttpServletResponse response){
		Cookie cookie = new Cookie(key,value);
		if(time!=null){
			cookie.setMaxAge(time);
		}else{
			cookie.setMaxAge(864000);//10天
		}
		cookie.setPath("/");
		String domain = JFig.getInstance().getValue("system_options", "DOMAIN",".gdbzyk.vicp.io");
		if(null!=domain){
			cookie.setDomain(domain);
		}
		response.addCookie(cookie);
	}
	/**获取cookie*/
	public static String getCookieValue(String key,HttpServletRequest request){
		if( request.getCookies() != null)
			for(Cookie cookie : request.getCookies()){
				if(cookie.getName().equals(key))
					return cookie.getValue();
			}
		return null;
	}
	
	/**删除cookie*/
	public static void deleteCooke(String key,HttpServletResponse response){
		Cookie cookie = new Cookie(key,null);
		cookie.setMaxAge(0);
		String domain = JFig.getInstance().getValue("system_options", "DOMAIN",".gdbzyk.vicp.io");
		if(null!=domain){
			cookie.setDomain(domain);
		}
		cookie.setPath("/");
		response.addCookie(cookie);
		
		/********处理旧数据，有可能已经存在cookie**********/
		/*cookie = new Cookie(key,null);
		cookie.setMaxAge(0);
		cookie.setDomain("www.bmatser.com");
		cookie.setPath("/");
		response.addCookie(cookie);*/
		/********处理旧数据，有可能已经存在cookie**********/
	}
}
