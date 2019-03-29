package bmatser.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.igfay.jfig.JFig;
import org.igfay.jfig.JFigException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.alibaba.fastjson.JSONObject;

//import bmatser.service.InterfaceLogI;
//import bmatser.service.impl.InterfaceLogImpl;


public class ErpConnect {
	
//	private static InterfaceLogI interfaceLogI;
//	
//	static{
//		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
//		interfaceLogI = context.getBean("InterfaceLog", InterfaceLogI.class);
//	}
//	
	
	public static String getSession() throws Exception{
		String key = null;
		String sessionUrl = null;
		String cid = null;
		String secret = null;
		String username = null;
		String password = null;
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		UrlEncodedFormEntity uefEntity = null;
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		try {
			sessionUrl = JFig.getInstance().getValue("erp_options", "SESSIONURL");
			cid = JFig.getInstance().getValue("erp_options", "CID");
			secret = JFig.getInstance().getValue("erp_options", "SECRET");
			username = JFig.getInstance().getValue("erp_options", "USERNAME");
			password = JFig.getInstance().getValue("erp_options", "PASSWORD");
			formparams.add(new BasicNameValuePair("cid",cid));
			formparams.add(new BasicNameValuePair("secret",secret));
			formparams.add(new BasicNameValuePair("username",username));
			formparams.add(new BasicNameValuePair("pwd",password));
		} catch (JFigException e) {
			throw new Exception("连接配置文件异常");
		}
		try {
			httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(sessionUrl);
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String str =EntityUtils.toString(entity, "UTF-8");
				JSONObject jo = JSONObject.parseObject(str);
				System.out.println(jo);
				key = jo.getString("session");
			}
		} catch (Exception e) {
			
			throw new Exception("响应失败"+e.toString());
		}finally{
			if(response!=null){
				response.close();
			}
			if(httpclient != null){
				httpclient.close();
			}
			
		}
		if(StringUtils.isBlank(key)){
			throw new Exception("sessionkey为空");
		}
		return key;
	}
	
	public static JSONObject postFrom(String sessionKey, String m, String value,String param) throws Exception{
		String method = m;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timestamp = sdf.format(new Date());
        String sign = null;
		String session = sessionKey;
		String trade = value;
		String secret = JFig.getInstance().getValue("erp_options", "SECRET");
		JSONObject jo = null ; 
		String doWhat = "";
		String methodName = "";
		StringBuffer signStr= new StringBuffer(secret);
		signStr.append("method"+method);
		if("outnumber".equals(param)){
			signStr.append(param+trade);
			doWhat = "查询商品库存";
			methodName = "getGoodsStock";
		}
		signStr.append("session"+session);
		signStr.append("timestamp"+timestamp);
		if("trade".equals(param)){
			signStr.append(param+trade);
			doWhat = "同步订单到ERP中";
			methodName = "saveOrderToERP";
		}
		signStr.append(secret);
		try {
			sign = Encrypt.md5(signStr.toString().toLowerCase());
		} catch (Exception e) {
			throw new Exception("32位MD5生成错误");
		}
		String url = null;
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		UrlEncodedFormEntity uefEntity = null;
		String errmsg = null;
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("method",method));
		formparams.add(new BasicNameValuePair("session",session));
		formparams.add(new BasicNameValuePair("timestamp",timestamp));
		formparams.add(new BasicNameValuePair(param,trade));
		formparams.add(new BasicNameValuePair("sign",sign.toUpperCase()));
		String json = JSONObject.toJSONString(formparams);
		String msg = "";
		String code = "-1";
		try {
			url = JFig.getInstance().getValue("erp_options", "URL");
		} catch (Exception e) {
			throw new Exception("配置文件读取失败");
		}
		try {
			httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(url);
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			response = httpclient.execute(httppost);
			HttpEntity e =response.getEntity();
			if(response!=null){
				if (response.getEntity() != null) {
					msg=EntityUtils.toString(response.getEntity(), "UTF-8");				
					jo = JSONObject.parseObject(msg);
					errmsg = jo.getString("errmsg");
					if(errmsg==null || errmsg==""){
						code = "0";
					}
				}else{
					throw new NullPointerException();
				}
			}
			//接口调用时保存日志到mongodb的InterfaceLog表
			if(!"outnumber".equals(param)){
//				interfaceLogI.saveLogToMongoDB(doWhat,
//						url,
//						methodName,json,
//						"gdbmro","erp",code,msg);
			}//查询商品库存不用保存日志
						
		} catch (Exception e) {		
//			//接口调用时保存日志到mongodb的InterfaceLog表
//			if(!"outnumber".equals(param)){
//				interfaceLogI.saveLogToMongoDB(doWhat,
//						url,
//						methodName,json,
//						"gdbmro","erp","-1",StringUtils.isNotBlank(msg)?msg : e.getMessage());
//			}//查询商品库存不用保存日志
//			throw new Exception("响应失败");			
		}finally{	
			if(response!=null){
				response.close();
			}
			if(httpclient != null){
				httpclient.close();
			}
		}
		if(StringUtils.isNotBlank(errmsg)){
			throw new Exception(errmsg);
		}
		return jo;
	}
	

}
