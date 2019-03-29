package bmatser.util;

import javax.servlet.http.HttpServletRequest;

/**
 * IP工具类
 * 
 * @author felx
 * 
 */
public class IpUtil {

	/**
	 * 获取登录用户IP地址
	 * 
	 * @param request
	 * @return String
	 */
	public static String getIpAddr(HttpServletRequest request) {
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
		if (ip.indexOf("0:") != -1) {
			ip = "本地";
		}
		
		if(ip!=null && ip.length()>15){ //"***.***.***.***".length() = 15   
	         if(ip.indexOf(",")>0){   
	        	 ip = ip.substring(0,ip.indexOf(","));   
	         }   
	     }   
		return ip;
	}

}
