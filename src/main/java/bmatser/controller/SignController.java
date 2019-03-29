package bmatser.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.annotations.Api;

import bmatser.util.ParamSignUtils;

@Controller
@Api(value="", description="getAppkey")
public class SignController {

	@RequestMapping(value="/getAppkey",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String sign(HttpServletRequest request,HttpSession session,HttpServletResponse response,
			String url,String queryString) throws Exception {
		String appSecret = "bmatser.controller";//秘钥
//		String queryString = request.getQueryString();
		
//		url = "http://" + request.getServerName() //服务器地址  
//                + ":"   
//                + request.getServerPort()
//                +"/gdbmro_serviceApi"
//                + url;
//		System.out.println("url:"+url);
//		System.out.println("queryString:"+queryString);
		
		TreeMap<String, String> paramsMap = new TreeMap<String, String>();
		paramsMap.put("url", url);
		if(null != queryString && !"".equals(queryString)){
			String[] querys = queryString.split("&");
			for(int i=0;i<querys.length;i++){
				String[] query = querys[i].split("=");
				if(null!=query && query.length>0){// && !"sign".equals(query[0])
					String key = null==query[0]?"":query[0];
					String value = "";
					if(query.length>1){
						value = URLDecoder.decode(query[1], "UTF-8");
					}
					paramsMap.put(key, value);
				}
			}
		}
		
		List<String> ignoreParamNames=new ArrayList<String>();  
	    ignoreParamNames.add("sign");  
		String sign = ParamSignUtils.sign(paramsMap, ignoreParamNames, appSecret);
		ObjectMapper mapper = new ObjectMapper();
		sign = mapper.writeValueAsString(sign);
		return sign;
		
//		model.addObject("sign", sign);
//	    model.setViewName("redirect:"+url);//+"?"+queryString+"&sign="+sign
////		return "redirect:"+url+"?"+queryString+"sign="+sign;
////	    return model;
////	    return new RedirectView(url+"?"+queryString+"&sign="+sign,true,false,false);
//	    this.doBgPostReq(response, url, paramsMap);
	}
	
	
	
}
