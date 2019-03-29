package bmatser.plugin.alipayDirect;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;


import bmatser.plugin.PaymentPlugin;
import bmatser.util.Constants;


/**
 * Plugin - 支付宝(即时交易)
 * 
 * @author www.flycent.com.cn
 * @version 3.0
 */
@Component("alipayDirectPlugin")
public class AlipayDirectPlugin extends PaymentPlugin {
	
	private static final String partner = "2088111578155640";
	private static final String key = "3k1mqsme067uchakf17q3rc5pq8ymdek";
	private static final String seller_email = "tm@gdbmromall.com";

	@Override
	public String getName() {
		return "支付宝(即时交易)";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}


	@Override
	public String getRequestUrl() {
		return "https://mapi.alipay.com/gateway.do";
	}

	@Override
	public RequestMethod getRequestMethod() {
		return RequestMethod.get;
	}

	@Override
	public String getRequestCharset() {
		return "UTF-8";
	}

	@Override
	public Map<String, String> getParameterMap(String orderId, BigDecimal txnAmt, HttpServletRequest request,String openId,Map<String, String> otherMap) {
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("service", "create_direct_pay_by_user");
		parameterMap.put("partner", partner);
		parameterMap.put("_input_charset", "utf-8");
		parameterMap.put("sign_type", "MD5");
		parameterMap.put("return_url", getNotifyUrl(orderId, NotifyMethod.sync));
		parameterMap.put("notify_url", getNotifyUrl(orderId, NotifyMethod.async));
		if(otherMap!= null && StringUtils.isNotBlank(otherMap.get("notify_url"))){
			parameterMap.put("notify_url", otherMap.get("notify_url"));
		}
		parameterMap.put("out_trade_no", orderId);
//		parameterMap.put("subject", StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 60));
//		parameterMap.put("body", StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 600));
		parameterMap.put("subject", orderId);
		parameterMap.put("payment_type", "1");
		parameterMap.put("seller_id", partner);
//		parameterMap.put("seller_email", seller_email);
		
		parameterMap.put("total_fee", txnAmt.toString());
		parameterMap.put("show_url", Constants.BASE_URL_COM);
		parameterMap.put("paymethod", "directPay");
		parameterMap.put("exter_invoke_ip", request.getLocalAddr());
//		parameterMap.put("extra_common_param", "gdbmro");
		parameterMap.put("sign", generateSign(parameterMap));
		System.out.println(parameterMap);
		return parameterMap;
	}

	@Override
	public Map<String, Object> getRefundsParameterMap(String sn, @SuppressWarnings("rawtypes") Map paramMap, HttpServletRequest request) {
//		PluginConfig pluginConfig = getPluginConfig();
		Map<String, Object> parameterMap = new HashMap<String, Object>();
//		parameterMap.put("service", "refund_fastpay_by_platform_pwd");
//		parameterMap.put("partner", pluginConfig.getAttribute("partner"));
//		parameterMap.put("_input_charset", "utf-8");
//		parameterMap.put("sign_type", "MD5");
//		parameterMap.put("notify_url", getRefundsNotifyUrl(sn));
//		parameterMap.put("seller_email", pluginConfig.getAttribute("seller_email"));
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		parameterMap.put("refund_date", df.format(new Date()));
//		parameterMap.put("batch_no", paramMap.get("batch_no"));
//		parameterMap.put("batch_num", paramMap.get("batch_num"));
//		parameterMap.put("detail_data", paramMap.get("detail_data"));
//		parameterMap.put("sign", generateSign(parameterMap));
		return parameterMap;
	}

	private String getRefundsNotifyUrl(String sn) {
		return Constants.BASE_URL_COM + "alipay_direct_refund/notify/" + sn + ".jhtml";
	}

	@Override
	public boolean verifyNotify(String sn,  HttpServletRequest request) {
		/*if (generateSign(request.getParameterMap()).equals(request.getParameter("sign"))
				&& sn.equals(request.getParameter("out_trade_no"))
				&& ("TRADE_SUCCESS".equals(request.getParameter("trade_status")) || "TRADE_FINISHED".equals(request.getParameter("trade_status")))
				) {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("service", "notify_verify");
			parameterMap.put("partner", partner);
			parameterMap.put("notify_id", request.getParameter("notify_id"));
			if ("true".equals(post("https://mapi.alipay.com/gateway.do", parameterMap))) {
				return true;
			}
		}
		return false;*/
		return true;
	}

	@Override
	public String getNotifyMessage(HttpServletRequest request) {
			return "success";
	}

	@Override
	public Integer getTimeout() {
		return 21600;
	}

	/**
	 * 生成签名
	 * 
	 * @param parameterMap 参数
	 * @return 签名
	 */
	private String generateSign(Map<String, ?> parameterMap) {
		return DigestUtils.md5Hex(joinKeyValue(new TreeMap<String, Object>(parameterMap), null, key, "&", true,
				"sign_type", "sign"));
	}

	@Override
	public boolean verifyRefundsNotify(String sn, HttpServletRequest request) {
//		PluginConfig pluginConfig = getPluginConfig();
//		if (generateSign(request.getParameterMap()).equals(request.getParameter("sign")) && sn.equals(request.getParameter("batch_no"))) {
//			Map<String, Object> parameterMap = new HashMap<String, Object>();
//			parameterMap.put("service", "notify_verify");
//			parameterMap.put("partner", pluginConfig.getAttribute("partner"));
//			parameterMap.put("notify_id", request.getParameter("notify_id"));
//			if ("true".equals(post("https://mapi.alipay.com/gateway.do", parameterMap))) {
//				return true;
//			}
//		}
		return false;
	}
	
	/**
	 * 获取交易号
	 * @param sn
	 * @param notifyMethod
	 * @param request
	 * @return
	 */
	public String getTn(String sn, HttpServletRequest request,BigDecimal txnAmt) {
		return null;
	}
	
	/**
	 * 银联退款
	 * @param sn
	 * @param queryId
	 * @param request
	 * @return
	 */
	public boolean unionpayRefunds(BigDecimal amount, String queryId, HttpServletRequest request){
		return false;
	}

	@Override
	public int compareTo(PaymentPlugin o) {
		// TODO Auto-generated method stub
		return 0;
	}
}