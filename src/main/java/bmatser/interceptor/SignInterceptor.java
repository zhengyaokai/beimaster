package bmatser.interceptor;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.igfay.jfig.JFig;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;

import bmatser.util.ParamSignUtils;

/**
 * 对请求进行签名
 * @author felx
 * @date 2016-1-4
 */
public class SignInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request,    
            HttpServletResponse response, Object handler) throws Exception{
//		String serverName = request.getServerName();
//		String host = request.getHeader("host");
//		String pathInfo = request.getRequestURI();
//		String queryString = request.getQueryString();
		request.setCharacterEncoding("UTF-8");
		String appSecret = "17ysa9eb0c6534787b6d1a73917843";
		
		//请求来源，如果没有来源将不能访问接口
//		String referer=request.getHeader("Referer");
//		if(null==referer || "".equals(referer)){
//			if(request.getRequestURI().contains("/payment") || request.getRequestURI().contains("/pingxx") || request.getRequestURI().contains("/cacheManager")
//				|| request.getRequestURI().contains("/kingnor")|| request.getRequestURI().contains("/jtl")){
//				//直接访问接口的白名单
//				return true;
//			}
//			return false;
//		}
		
//		System.out.println(request.getHeader("host"));
		String serverName = request.getHeader("host");
		String scheme = request.getScheme();
		
		/*String url = "http://" + request.getServerName() //服务器地址  
                + ":"   
                + request.getServerPort()
                + request.getRequestURI();*/
		String url = scheme + "://" + serverName //服务器地址  
                + request.getRequestURI();
//		System.out.println("url:"+url);
		
		//过滤不用拦截的请求
		if(request.getRequestURI().contains("/payment") || request.getRequestURI().contains("/pingxx") || request.getRequestURI().contains("/beecloud") || request.getRequestURI().contains("/cacheManager")
				|| request.getRequestURI().contains("/dealerBusinessLicense/showPic")|| request.getRequestURI().contains("/dealerBusinessLicense/addPC") || request.getRequestURI().contains("/dealerBusinessLicense/addWx")
				|| request.getRequestURI().contains("/erp") || request.getRequestURI().contains("shunfeng")|| request.getRequestURI().contains("/getAppkey")
				|| request.getRequestURI().contains("/kingnor")|| request.getRequestURI().contains("/jtl") || request.getRequestURI().contains("/weixin/getLoginInfoByOpenId") || request.getRequestURI().contains("/weixin/getLoginInfoBySaasOpenId")
				|| request.getRequestURI().contains("/invoice")){
			return true;
		}
		
		//过滤不是本域名过来的访问
//		Enumeration<String> headerNames = request.getHeaderNames();
//
//        while (headerNames.hasMoreElements()) {
//
//            String headerName = headerNames.nextElement();
//            System.out.println("headerName---"+headerName);
//
//            Enumeration<String> headers = request.getHeaders(headerName);
//            while (headers.hasMoreElements()) {
//                String headerValue = headers.nextElement();
//                System.out.println("headerValue---"+headerValue);
//            }
//
//        }
		
//		String host = request.getHeader("Host");
//		String referer = request.getHeader("Referer");
//		String netReferer = JFig.getInstance().getValue("system_options", "netReferer", "http://www.bmatser.com");
//		String comReferer = JFig.getInstance().getValue("system_options", "comReferer", "http://www.gdbmromall.com");
//		if(!referer.contains(netReferer) && !referer.contains(comReferer)){
//			return false;
//		}
		
		
		TreeMap<String, String> paramsMap = new TreeMap<String, String>();
		
		Map<String,String> map = this.getParameterMap(request);
		if(map != null && !map.isEmpty()){
        	String json = JSONObject.toJSONString(map);
//        	System.out.println("----json:"+json);
        	paramsMap.put("url", url);
        	for (String pName : map.keySet()) {
        		Object obj = map.get(pName);
        		String value = ((String[])obj)[0];
        		paramsMap.put(pName, value);//兼容ie8  
        		
        	}
        }
		
		
		
		/*if(null != queryString && !"".equals(queryString)){
			String[] querys = queryString.split("&");
			for(int i=0;i<querys.length;i++){
				String[] query = querys[i].split("=");
				if(null!=query && query.length>0){// && !"sign".equals(query[0])
					String key = null==query[0]?"":query[0];
					String value = "";
					if(query.length>1){
						value = query[1];
					}
					paramsMap.put(key, value);
				}
			}
		}*/
		
		
		List<String> ignoreParamNames=new ArrayList<String>();  
	    ignoreParamNames.add("sign");  
	    ignoreParamNames.add("callback");  
	    ignoreParamNames.add("_");  
		String sign = ParamSignUtils.sign(paramsMap, ignoreParamNames, appSecret);
		//System.out.println("signValue:"+sign);
		String urlSign = paramsMap.get("sign");
		//System.out.println("urlSign:"+urlSign);
		if(null!=urlSign && urlSign.equals(sign)){
			return true;
		}else{
			return false;
		}
		
//		return true;
	}
	
	
	private Map getParameterMap(HttpServletRequest request) {  
        try{  
            if(!request.getMethod().equals("GET")){//判断是否是get请求方式  
                return request.getParameterMap();  
            }  
              
            Map<String,String[]> map = request.getParameterMap();  //接受客户端的数据  
            Map<String,String[]> newmap = new HashMap();     
            for(Map.Entry<String, String[]> entry : map.entrySet()){  
                String name = entry.getKey();  
                String values[] = entry.getValue();  
                  
                if(values==null){  
                    newmap.put(name, new String[]{});  
                    continue;  
                }  
                String newvalues[] = new String[values.length];  
                for(int i=0; i<values.length;i++){  
                    String value = values[i];  
                    value = new String(value.getBytes("iso8859-1"),request.getCharacterEncoding());  
                    newvalues[i] = value; //解决乱码后封装到Map中  
                }  
                  
                newmap.put(name, newvalues);  
            }  
          
            return newmap;  
        }catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
	
	@Override
	public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }  
	
	@Override
	public void afterCompletion(HttpServletRequest request,  
            HttpServletResponse response, Object handler, Exception ex)  
            throws Exception {
    } 
}
