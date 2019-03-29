package bmatser.plugin.weixin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.corba.se.spi.orbutil.fsm.Input;

import bmatser.dao.DealerMapper;
import bmatser.plugin.PaymentPlugin;
import bmatser.util.IpUtil;
import bmatser.util.XMLStringUtil;
@Component("weixinPlugin")
public class WeixinPlugin extends PaymentPlugin {
	


	@Override
	public int compareTo(PaymentPlugin o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "微信支付";
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestUrl() {
		// TODO Auto-generated method stub
		return "https://api.mch.weixin.qq.com/pay/unifiedorder";
	}

	@Override
	public RequestMethod getRequestMethod() {
		// TODO Auto-generated method stub
		return RequestMethod.post;
	}

	@Override
	public String getRequestCharset() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getParameterMap(String orderId,
			BigDecimal txnAmt, HttpServletRequest request,String openId,Map<String, String> otherMap) {
		ScanPayReqData data = new ScanPayReqData(orderId,txnAmt.intValue(),IpUtil.getIpAddr(request),getNotifyUrl(orderId, NotifyMethod.async),openId);
		final String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		Map<String, String> result = new HashMap<>();
		try{
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(url);

	        //将要提交给API的数据对象转换成XML格式数据Post给API
	        String postDataXML = XMLStringUtil.toXmlString(data.toMap());
	        StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
	        httppost.addHeader("Content-Type", "text/xml");
	        httppost.setEntity(postEntity);
			CloseableHttpResponse response = httpClient.execute(httppost);
			HttpEntity entity = response.getEntity();
			String msg =  EntityUtils.toString(entity, "UTF-8");
			System.out.println(msg);
			if("SUCCESS".equals(XMLStringUtil.parsingXmlString(msg, "return_code"))){
				if("FAIL".equals(XMLStringUtil.parsingXmlString(msg, "result_code"))){
					throw new Exception("微信响应错误："+XMLStringUtil.parsingXmlString(msg, "err_code_des"));
				}
				result.put("prepay_id", XMLStringUtil.parsingXmlString(msg, "prepay_id"));
				result.put("appid", XMLStringUtil.parsingXmlString(msg, "appid"));
				result.put("nonce_str", XMLStringUtil.parsingXmlString(msg, "nonce_str"));
				result.put("sign", XMLStringUtil.parsingXmlString(msg, "sign"));
				
			}else{
				throw new Exception("微信响应错误："+XMLStringUtil.parsingXmlString(msg, "return_msg"));
			}
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

	@Override
	public Map<String, Object> getRefundsParameterMap(String sn, Map paramMap,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean verifyNotify(String sn, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean verifyRefundsNotify(String sn, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNotifyMessage(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return "success";
	}

	@Override
	public Integer getTimeout() {
		// TODO Auto-generated method stub
		return 21600;
	}

	@Override
	public String getTn(String sn, HttpServletRequest request, BigDecimal txnAmt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean unionpayRefunds(BigDecimal amount, String queryId,
			HttpServletRequest request) {
		return false;
	}

}
