package bmatser.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import bmatser.util.Constants;

public class AuthInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request,    
            HttpServletResponse response, Object handler){
		String auth = request.getParameter("auth");
        if(StringUtils.isBlank(auth) || !StringUtils.equals(auth, Constants.AUTH)){     	
            return false;
        }
		return true;
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
