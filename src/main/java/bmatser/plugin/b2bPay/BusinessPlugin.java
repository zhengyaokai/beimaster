package bmatser.plugin.b2bPay;

import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import bmatser.plugin.PaymentPlugin;
import bmatser.util.PropertyUtil;

@Component("businessPlugin")
public class BusinessPlugin extends PaymentPlugin {
	
	static {
		SDKConfig.getConfig().loadPropertiesFromSrc();
	}

	@Override
	public int compareTo(PaymentPlugin o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		return "B2B支付";
	}

	@Override
	public String getVersion() {
		return "5.0.0";
	}

	@Override
	public String getRequestUrl() {
		//正式https://gateway.95516.com/gateway/api/frontTransReq.do
		//测试https://101.231.204.80:5000/gateway/api/frontTransReq.do
		return PropertyUtil.getPropertyValue("acp_sdk.properties", "acpsdk.requestUrl");
	}

	@Override
	public RequestMethod getRequestMethod() {
		return RequestMethod.post;
	}

	@Override
	public String getRequestCharset() {
		// TODO Auto-generated method stub
		return "UTF-8";
	}

	@Override
	public Map<String, String> getParameterMap(String orderId,
			BigDecimal txnAmt, HttpServletRequest request,String openId,Map<String, String> otherMap) {
		B2bPayment pay = new B2bPayment();
		pay.setOrderId(orderId);
		pay.setTxnAmt(txnAmt.toString());
		pay.setBackUrl(getNotifyUrl(orderId, NotifyMethod.async));
		pay.setFrontUrl(getNotifyUrl(orderId, NotifyMethod.sync));
		Map<String, String> reqData = AcpService.sign(pay.toHashMap(),DemoBase.encoding_UTF8);
		return reqData;
	}

	@Override
	public Map<String, Object> getRefundsParameterMap(String sn, Map paramMap,
			HttpServletRequest request) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		return parameterMap;
	}

	@Override
	public boolean verifyNotify(String sn, HttpServletRequest request) {
		String encoding = request.getParameter("encoding");
		// 获取银联通知服务器发送的后台通知参数
		Map<String, String> reqParam;
		try {
			reqParam = getAllRequestParam(request,encoding);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		//重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
		if (!AcpService.validate(reqParam, encoding)) {
			return false;
			//验签失败，需解决验签问题
		} else {
			return true;
		}
		
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
		return 21600;
	}

	@Override
	public String getTn(String sn, HttpServletRequest request, BigDecimal txnAmt) {
		return null;
	}

	@Override
	public boolean unionpayRefunds(BigDecimal amount, String queryId,
			HttpServletRequest request) {
		return false;
	}

	
	/**
	 * 获取请求参数中所有的信息
	 * 
	 * @param request
	 * @param encoding 
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, String> getAllRequestParam(final HttpServletRequest request, String encoding) throws Exception {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = new String(request.getParameter(en).getBytes(encoding), encoding);
				res.put(en, value);
				//在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				//System.out.println("ServletUtil类247行  temp数据的键=="+en+"     值==="+value);
				if (null == res.get(en) || "".equals(res.get(en))) {
					res.remove(en);
				}
			}
		}
		return res;
	}
}
