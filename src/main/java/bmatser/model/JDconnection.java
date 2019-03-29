package bmatser.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

import bmatser.util.DateTypeHelper;

public class JDconnection {
	
	
	private final String APP_KEY = "8d716da0e5534812a2566a67b440f09f";
	private final String APP_SECRET = "b89999cb12f143e4b5681713cd6e2683";
	private final String USERNAME = "%e8%8b%8f%e5%b7%9e%e5%b7%a5%e5%93%81%e6%b1%87";//工电宝 urlEncode
	private final String PASSWORD = "6485ce0e3d9ee07af005da24ca71ccb8";//md5加密
	
	
	private String url;
	private CloseableHttpClient httpclient;
	private CloseableHttpResponse response;
	private UrlEncodedFormEntity uefEntity;
	private JDMethod method;
	
	public enum JDChannel{
		/** 获取token */
		TOKEN,
		/** 接口 */
		INTERFACE,
/*		*//** 查询商品库存 *//*
		STOCK,
		*//** 京东下单 *//*
		ORDER,
		*//**商品可售验证接口 *//*
		ISSALE,
		*//**预占订单 *//*
		OCCUPY,*/
	}
	
	public enum JDMethod{
		/** post */
		POST,
		/** get */
		GET,
	}
	
	public JDconnection(JDChannel channel,JDMethod method) {
		this.url = toUrl(channel);
		this.method = method;
	}
	
	
	/**
	 * 根据不同API获取连接
	 * @param channel
	 * @return
	 */
	private String toUrl(JDChannel channel){
		switch (channel) {
		case TOKEN:
			return "https://kploauth.jd.com/oauth/token";
/*		case REFRESH_TOKEN:
			return "https://kploauth.jd.com/oauth/token";*/
		case INTERFACE:
			return "https://router.jd.com/api";
/*		case ORDER:
			return "https://router.jd.com/api";
		case ISSALE:
			return "https://router.jd.com/api";
		case OCCUPY:
			return "https://router.jd.com/api";*/
		default :
			return "";
		}
	}
	


	/**
	 * 获取访问内容
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String getResultData(Map<String, String> map) throws Exception {
		try {
			httpclient=HttpClients.createDefault();
			JoiningUrl(map);
			switch (this.method) {
			case GET:
				HttpGet get = new HttpGet(this.url);
				response = httpclient.execute(get);
				break;
			case POST:
/*				List<NameValuePair> formparams = new ArrayList<NameValuePair>();
				for (Entry<String, String> entity : map.entrySet()) {
					formparams.add(new BasicNameValuePair(entity.getKey(),entity.getValue()));
				}
				uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");*/
				HttpPost post = new HttpPost(this.url);
				response = httpclient.execute(post);
				break;
			}
			int statusCode = response.getStatusLine().getStatusCode();
			 if(statusCode==200){
				 HttpEntity entity = response.getEntity();
				 String msg =  EntityUtils.toString(entity, "UTF-8");
				 return msg;
			 }else{
				 throw new RuntimeException("接口服务响应状态错误,错误编码"+statusCode);
			 }
		}finally{
			closeStream();
		}
	}
	
	/**
	 * 拼接URl
	 * @param map
	 */
	private void JoiningUrl(Map<String, String> map) {
		StringBuffer buff = new StringBuffer("?");
		for (Entry<String, String> param : map.entrySet()) {
			buff.append(param.getKey()+ "=" + param.getValue() +"&");
		}
		String join = buff.substring(0, buff.length()-1);
		this.url= this.url + join;
	}

	/**
	 * 关闭流
	 */
	private void closeStream() {
		if(this.httpclient != null){
			try {
				this.httpclient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(this.response != null){
			try {
				this.response.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
	
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public CloseableHttpClient getHttpclient() {
		return httpclient;
	}
	public void setHttpclient(CloseableHttpClient httpclient) {
		this.httpclient = httpclient;
	}
	public CloseableHttpResponse getResponse() {
		return response;
	}
	public void setResponse(CloseableHttpResponse response) {
		this.response = response;
	}
	public UrlEncodedFormEntity getUefEntity() {
		return uefEntity;
	}
	public void setUefEntity(UrlEncodedFormEntity uefEntity) {
		this.uefEntity = uefEntity;
	}


	public Map<String, String> getTokenData() {
		Map<String, String> map = new HashMap<>();
		map.put("grant_type", "password");
		map.put("app_key", APP_KEY);
		map.put("app_secret", APP_SECRET);
		map.put("state", "0");
		map.put("username", USERNAME);
		map.put("password", PASSWORD);
		return map;
	}


	public Map<String, String> getStockData(String skuId, String area) throws Exception {
		Map<String, String> map = getConnMap();
		Map<String, String> data = new HashMap<>();
		JSONObject jo = new JSONObject();
		data.put("skuId", skuId);
		data.put("num", "1");
		List<Map<String, String>> list = new ArrayList<>();
		list.add(data);
		jo.put("skuNums", list);
		jo.put("area", area);
		map.put("method", "biz.stock.fororder.batget");
		String paramJson = jo.toJSONString();
		paramJson = URLEncoder.encode(paramJson, "UTF-8");
		map.put("param_json", paramJson);
		return map;
	}


	public JDMethod getMethod() {
		return method;
	}


	public void setMethod(JDMethod method) {
		this.method = method;
	}


	public Map<String, String> getOrderData(JDOrder order) throws Exception {
		Map<String, String> map = getConnMap();
		map.put("method", "biz.order.unite.submit");
		String paramJson = JSONObject.toJSONString(order);
		paramJson = URLEncoder.encode(paramJson, "UTF-8");
		map.put("param_json", paramJson);
		return map;
	}


	public Map<String, String> getGoodsIsSaleData(String skuId) throws Exception {
		Map<String, String> map = getConnMap();
		JSONObject jo = new JSONObject();
		jo.put("skuIds", skuId);
		map.put("method", "biz.product.sku.check");
		String paramJson = jo.toJSONString();
		paramJson = URLEncoder.encode(paramJson, "UTF-8");
		map.put("param_json", paramJson);
		return map;
	}


	public Map<String, String> getOccupyStockData(String id) throws Exception {
		Map<String, String> map = getConnMap();
		JSONObject jo = new JSONObject();
		jo.put("jdOrderId", id);
		map.put("method", "biz.order.occupyStock.confirm");
		String paramJson = jo.toJSONString();
		paramJson = URLEncoder.encode(paramJson, "UTF-8");
		map.put("param_json", paramJson);
		return map;
	}
	
	private Map<String, String> getConnMap(){
		Map<String, String> map = new HashMap<>();
		String date = DateTypeHelper.getCurrentDateTimeString();
		map.put("app_key", APP_KEY);
		map.put("timestamp", date.replace(" ", ""));
		map.put("v", "1.0");
		map.put("format", "json");
		return map;
	}
}
