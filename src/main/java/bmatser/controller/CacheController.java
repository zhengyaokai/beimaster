package bmatser.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.rubyeye.xmemcached.MemcachedClient;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import bmatser.pageModel.BuyerInvoicePage;
import bmatser.util.ResponseMsg;

/**
 * 缓存处理
 * @author felx
 */
@Controller
@RequestMapping("cacheManager")
@Api(value="cacheManager", description=" 缓存处理")
public class CacheController {

	@RequestMapping(value="/del", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "缓存处理")
	public ResponseMsg delCache(@ApiParam(value="name")@RequestParam String name,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		ServletContext servletContext = request.getSession().getServletContext();
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		MemcachedClient memcachedClient =  webApplicationContext.getBean(MemcachedClient.class);
		try{
//			Object obj = memcachedClient.get(name);
			memcachedClient.delete(name);
			msg.setCode(0);
		}catch (Exception e) {
			msg.setMsg(e.toString());
			msg.setCode(-1);
		}
		return msg;
	}
	
}
