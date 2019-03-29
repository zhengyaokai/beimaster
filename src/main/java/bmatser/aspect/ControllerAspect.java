package bmatser.aspect;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import bmatser.annotations.Login;
import bmatser.exception.GdbmroException;
import bmatser.model.LoginInfoUtil;
import bmatser.pageModel.PageMode;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;

@Aspect
@Component
public class ControllerAspect



{
  @Around("@annotation(bmatser.annotations.Login)")
  public Object doAround(ProceedingJoinPoint joinPoint)
    throws Throwable
  {
    HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    String url = request.getRequestURL().toString().replaceAll("/", "");
    MethodSignature ms = (MethodSignature)joinPoint.getSignature();
    Object[] arg = joinPoint.getArgs();
    Class<?> head= ms.getDeclaringType();
    Method method = ms.getMethod();

    RequestMapping mapper = (RequestMapping) head.getAnnotation(RequestMapping.class);
    String value = "";
    if(mapper.value().length>0){
    	value = mapper.value()[0];
    }
    Login login = (Login)method.getAnnotation(Login.class);
    String saas = login.saas();
    String app = login.app();
    String mall = login.mall();
    LoginInfo loginInfo = null;
    try {
      if ((saas.length() == 0) && (mall.length() == 0) && (app.length() == 0)){
    	  throw new RuntimeException("请配置@Login注解拦截登陆地址!");
      }
      if (saas.length() > 0) {
    	  if (url.indexOf((value+saas).replaceAll("/", "")) >= 0) {
    		  loginInfo = MemberTools.isSaasLogin(request);
    		  if(loginInfo!=null){
    			  loginInfo.setChannel(PageMode.Channel.SAAS);
    		  }
    	  }
      }
      if (mall.length() > 0){
    	  if (url.indexOf((value+mall).replaceAll("/", "")) >= 0) {
    		  loginInfo = MemberTools.isLogin(request);
    		  if(loginInfo!=null){
    			  loginInfo.setChannel(PageMode.Channel.MALL);
    		  }
    	  }
      }
      if (app.length() > 0) {
	        String dealerId = request.getParameter("dealerId");
	        String loginId = request.getParameter("loginId");
	        String buyerId = request.getParameter("buyerId");
	        if(ServletFileUpload.isMultipartContent(request)){
	        	for (int i = 0,size = arg.length; i < size; i++) {
		          	  if (arg[i] instanceof HttpServletRequest){
		          		  dealerId = ((HttpServletRequest)arg[i]).getParameter("dealerId");
		          		  break;
		            	}
		          	  if(i==(size-1)){
		          		  throw new RuntimeException("未找到HttpServletRequest参数");
		          	  }
				}
	        }
	        if ((url.indexOf((value+app).replaceAll("/", "")) >= 0) && ((StringUtils.isNotBlank(dealerId)) || (StringUtils.isNotBlank(buyerId)) || (StringUtils.isNotBlank(loginId)))){
	        	loginInfo = new LoginInfo();
	        	loginInfo.setLoginId(loginId);
	        	loginInfo.setDealerId((StringUtils.isNotBlank(dealerId)) ? dealerId : buyerId);
	        	loginInfo.setChannel(PageMode.Channel.APP);
	        }
      }

      if (loginInfo == null) {
        throw new GdbmroException(-1, "请先登陆!");
      }
      for (Object object : arg) {
    	  if (object instanceof LoginInfoUtil){
    		  ((LoginInfoUtil)object).write(loginInfo);
      		}
      }
    }
    catch (Exception e)
    {
        String ReturnType = method.getReturnType().getName();
        if ((Object.class.getName().equals(ReturnType)) 
      		  || (ResponseMsg.class.getName().equals(ReturnType))) {
         
          ResponseMsg msg = new ResponseMsg();
          msg.setError(e);
          return msg;
        }else{
        	 throw e;
        }
    }
    return joinPoint.proceed(arg);
  }

  @Around("@annotation(bmatser.annotations.ResponseDefault)")
  public Object doAfterThrowing(ProceedingJoinPoint joinPoint) {
    ResponseMsg msg = new ResponseMsg();
    MethodSignature ms = (MethodSignature)joinPoint.getSignature();
    Method method = ms.getMethod();
    String ReturnType = method.getReturnType().getName();
    if (!(Object.class.getName().equals(ReturnType)) 
    		&& !(ResponseMsg.class.getName().equals(ReturnType))) {
    	throw new RuntimeException("@ResponseDefault注解,注解方法返回类型为"+Object.class.getName()+" 或 "+ResponseMsg.class.getName());
    }
    try {
      Object o = joinPoint.proceed();
      if (o instanceof ResponseMsg) {
        return o;
      }
      msg.setData(o);
    } catch (Throwable e) {
      msg.setError(e);
    }
    return msg;
  }
}