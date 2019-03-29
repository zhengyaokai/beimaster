package bmatser.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.igfay.jfig.JFig;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;

import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;

public class LoginInterceptor extends HandlerInterceptorAdapter {
	
	private static final String TO_LOGIN_URL="/page/index";
	private static final String LOGIN_URL="/login";
	private static final String LOGIN_REQUEST="/user/to_login";
	private static final String PCSAASLOGIN = "/user/saaspcLogin";
	private String url = JFig.getInstance().getSection("system_options").get("URL_UPLOAD");
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		LoginInfo loginInfo = MemberTools.isLogin(request);
		
//		LoginInfo  loginInfo= (LoginInfo) request.getSession().getAttribute("loginInfo");
		if(loginInfo==null && (!request.getServletPath().startsWith(LOGIN_REQUEST)) && (!request.getServletPath().startsWith(PCSAASLOGIN))){
			try {
				if(request.getServletPath().contains("/goods/get_list/model_selection.action")
						|| request.getServletPath().contains("goods/down_list_option")
				||request.getServletPath().contains("goods/get_list/goods_list")
                || request.getServletPath().contains("goods/get_list/brand_selection.action")){
					return true;
				}
//				response.sendRedirect(url + LOGIN_URL);
				PrintWriter print = response.getWriter();
				ResponseMsg msg = new ResponseMsg();
				msg.setCode(401);
				msg.setMsg("请先登录");
				print.write(JSONObject.toJSONString(msg));
				print.close();
				return false;
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}



	
	
}
