package bmatser.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.igfay.jfig.JFig;

public class EzhuHttpclient {
	
	
	private final String DATEF_ORMAT = "yyyy-MM-dd HH:mm:ss";
	private String method;
	private String sessionKey;
	private String timestamp;
	private String sign;
	private String url;
	private CloseableHttpClient httpclient;
	private CloseableHttpResponse response;
	private UrlEncodedFormEntity uefEntity;
	private String msg;
	private String secret;
	private String describe;
	private TreeMap<String, String> paramMap;
	
	
	
	public EzhuHttpclient(String method,String sessionKey) {
		this.method = method;
		this.sessionKey = sessionKey;
		this.url = JFig.getInstance().getValue("erp_options", "URL","");;
		SimpleDateFormat sdf = new SimpleDateFormat(DATEF_ORMAT);
		this.timestamp = sdf.format(new Date());
		this.secret = JFig.getInstance().getValue("erp_options", "SECRET","");
	}
	
	public void put(String key,String value){
		if(paramMap == null){
			paramMap = new TreeMap<>();
		}
		paramMap.put(key, value);
	}
	
	public String getResponseBody() throws Exception{
		try {
			this.httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(url);
			countSign();
			uefEntity = new UrlEncodedFormEntity(getFormparams(), "UTF-8");
			httppost.setEntity(uefEntity);
			this.response = httpclient.execute(httppost);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200){
				HttpEntity entity =response.getEntity();
				this.msg = EntityUtils.toString(entity, "UTF-8");
				return this.msg;
			}else if(statusCode == 500){
				this.msg = url+"响应编码"+ statusCode;
				HttpEntity entity =response.getEntity();
				this.msg = EntityUtils.toString(entity, "UTF-8");
				throw new RuntimeException(url+"响应编码"+ statusCode+"内容为:"+this.msg);
			}else{
				this.msg = url+"响应编码"+ statusCode;
				throw new RuntimeException(url+"响应编码"+ statusCode);
			}
		}finally{
			toCloseStream();

		}
	}
	/**
	 * 关闭输出流
	 */
	private void toCloseStream() {
		if(response != null){
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(httpclient != null){
			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void countSign() {
		paramMap.put("method", this.method);
		paramMap.put("session", this.sessionKey);
		paramMap.put("timestamp", this.timestamp);
		StringBuffer signStr= new StringBuffer(this.secret);
		for (Entry<String, String> param : this.paramMap.entrySet()) {
			signStr.append(param.getKey() + param.getValue());
		}
		signStr.append(this.secret);
		this.sign = Encrypt.md5(signStr.toString().toLowerCase()).toUpperCase();
		paramMap.put("sign", this.sign);
	}

	private List<? extends NameValuePair> getFormparams() {
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		for (Entry<String, String> param : this.paramMap.entrySet()) {
			formparams.add(new BasicNameValuePair(param.getKey(),param.getValue()));
		}
		return formparams;
	}

	public String getMethod() {
		return method;
	}


	public void setMethod(String method) {
		this.method = method;
	}


	public String getSessionKey() {
		return sessionKey;
	}


	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}


	public String getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}


	public String getSign() {
		return sign;
	}


	public void setSign(String sign) {
		this.sign = sign;
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


	public String getSecret() {
		return secret;
	}


	public void setSecret(String secret) {
		this.secret = secret;
	}



	public TreeMap<String, String> getParamMap() {
		return paramMap;
	}



	public void setParamMap(TreeMap<String, String> paramMap) {
		this.paramMap = paramMap;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getParamString() {
		StringBuffer buffer = new StringBuffer("");
		for (Entry<String, String> param : this.paramMap.entrySet()) {
			buffer.append("{"+param.getKey() +":"+ param.getValue()+"}");
		}
		return buffer.toString();
	}

	public String getDescribe() {
		return describe;
	}

	public EzhuHttpclient setDescribe(String describe) {
		this.describe = describe;
		return this;
	}

	
	
}
