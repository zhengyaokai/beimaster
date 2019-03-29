
package bmatser.plugin.unionpay;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import bmatser.plugin.PaymentPlugin;
import bmatser.plugin.unionpay.config.QuickPayConf;
import bmatser.plugin.unionpay.config.QuickPayUtils;
import bmatser.plugin.unionpay.sdk.CertUtil;
import bmatser.plugin.unionpay.sdk.HttpClient;
import bmatser.plugin.unionpay.sdk.SDKConfig;
import bmatser.plugin.unionpay.sdk.SDKUtil;
import bmatser.util.Constants;
import bmatser.util.IpUtil;

/**
 * Plugin - 银联在线支付
 * 
 * @author felx
 * @version 3.0
 */
@Component("unionpayPlugin")
public class UnionpayPlugin extends PaymentPlugin {
	
	static {
		SDKConfig.getConfig().loadPropertiesFromSrc();
	}

	/** 货币 */
	private static final String CURRENCY = "156";
	/** 商户号 */
	private static final String merId = "898320548160153";

	@Override
	public String getName() {
		return "银联在线支付";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getRequestUrl() {
//		return "https://unionpaysecure.com/api/Pay.action";
//		SDKConfig.getConfig().loadPropertiesFromSrc();
//		String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();
		String requestFrontUrl = QuickPayConf.gateWay;
		return requestFrontUrl;
	}

	@Override
	public RequestMethod getRequestMethod() {
		return RequestMethod.post;
	}

	@Override
	public String getRequestCharset() {
		return "UTF-8";
	}

	@Override
	public Map<String, String> getParameterMap(String orderId, BigDecimal txnAmt, HttpServletRequest request,String openId,Map<String, String> otherMap) {

		/**
		 * 组装请求报文
		 */
		Map<String, String> data = new HashMap<String, String>();
		/*// 版本号
		data.put("version", "5.0.0");
		// 字符集编码 默认"UTF-8"
		String encoding = "UTF-8";
		data.put("encoding", "UTF-8");
		// 签名方法 01 RSA
		data.put("signMethod", "01");
		// 交易类型 01-消费
		data.put("txnType", "01");
		// 交易子类型 01:自助消费 02:订购 03:分期付款
		data.put("txnSubType", "01");
		// 业务类型
		data.put("bizType", "000201");
		// 渠道类型，07-PC，08-手机
		data.put("channelType", "07");
		// 前台通知地址 ，控件接入方式无作用
		data.put("frontUrl", getNotifyUrl(orderId, NotifyMethod.sync));
		// 后台通知地址
		data.put("backUrl", getNotifyUrl(orderId, NotifyMethod.async));
		// 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
		data.put("accessType", "0");
		// 商户号码，请改成自己的商户号
		data.put("merId", merId);
		// 商户订单号，8-40位数字字母
		data.put("orderId", orderId);
		// 订单发送时间，取系统时间
		data.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		// 交易金额，单位分
		data.put("txnAmt", txnAmt.multiply(new BigDecimal(100)).setScale(0).toString());
		// 交易币种
		data.put("currencyCode", CURRENCY);
		// 请求方保留域，透传字段，查询、通知、对账文件中均会原样出现
		// data.put("reqReserved", "透传信息");
		// 订单描述，可不上送，上送时控件中会显示该信息
		// data.put("orderDesc", "订单描述");
		// 证书ID 调用 SDK 可自动获取并赋值
		 data.put("certId", CertUtil.getSignCertId());
		// 签名 调用 SDK 可自动运算签名并赋值
		// data.put("signature", "");

//			data = signData(data);
		SDKUtil.sign(data, encoding);

//			// 交易请求url 从配置文件读取
//			String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();
//			data.put("signature", SDKUtil.signByMd5(data, encoding));
		*/
		
		
		String ip = getRemortIP(request);
		String url =getNotifyUrl(orderId, NotifyMethod.async);
		if(otherMap !=null &&StringUtils.isNotBlank(otherMap.get("notify_url"))){
			url=otherMap.get("notify_url");
		}
		// 商户需要组装如下对象的数据
		String[] valueVo = new String[] { 
				QuickPayConf.version,// 协议版本
				QuickPayConf.charset,// 字符编码
				"01",// 交易类型
				"",// 原始交易流水号
				QuickPayConf.merCode,// 商户代码
				QuickPayConf.merName,// 商户简称
				"",// 收单机构代码（仅收单机构接入需要填写）
				"",// 商户类别（收单机构接入需要填写）
				"",// 商品URL
				"",// 商品名称
				"",// 商品单价 单位：分
				"1",// 商品数量
				"0",// 折扣 单位：分
				"0",// 运费 单位：分
				orderId,// 订单号（需要商户自己生成）
				txnAmt.multiply(new BigDecimal(100)).setScale(0).toString(),// 交易金额 单位：分
				"156",// 交易币种
				new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()),// 交易时间
				ip,// 用户IP
//				"58.240.196.6",
				"",// 用户真实姓名
				"",// 默认支付方式
				"",// 默认银行编号
				"300000",// 交易超时时间
				getNotifyUrl(orderId, NotifyMethod.sync),// 前台回调商户URL    //QuickPayConf.merFrontEndUrl
				url,// 后台回调商户URL   //QuickPayConf.merBackEndUrl
				""// 商户保留域
		};
		
		String signType = request.getParameter("sign");
		if (!QuickPayConf.signType_SHA1withRSA.equalsIgnoreCase(signType)) {
			signType = QuickPayConf.signType;
		}

		data = new QuickPayUtils().createPayMap(valueVo, signType);
		return data;
	}
	
	public String getRemortIP(HttpServletRequest request) {

		/*if (request.getHeader("x-forwarded-for") == null) {

		return request.getRemoteAddr();

		}

		return request.getHeader("x-forwarded-for");*/
		String ip = IpUtil.getIpAddr(request);
		return ip;

		}
	
	/**
	 * java main方法 数据提交 　　 对数据进行签名
	 * 
	 * @param contentData
	 * @return　签名后的map对象
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> signData(Map<String, ?> contentData) {
		Entry<String, String> obj = null;
		Map<String, String> submitFromData = new HashMap<String, String>();
		for (Iterator<?> it = contentData.entrySet().iterator(); it.hasNext();) {
			obj = (Entry<String, String>) it.next();

			if (null != obj.getValue()
					&& StringUtils.isNotEmpty(String.valueOf(obj.getValue()))) {
				submitFromData
						.put(obj.getKey(), String.valueOf(obj.getValue()));
				System.out.println(obj.getKey() + "-->"
						+ String.valueOf(obj.getValue()));
			}
		}

		/**
		 * 签名
		 */
		SDKUtil.sign(submitFromData, "UTF-8");

		return submitFromData;
	}

	@Override
	public Map<String, Object> getRefundsParameterMap(String sn, @SuppressWarnings("rawtypes") Map paramMap, HttpServletRequest request) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		/**
		 * 组装请求报文
		 *//*
		Map<String, String> data = new HashMap<String, String>();
		// 版本号
		data.put("version", "5.0.0");
		// 字符集编码 默认"UTF-8"
		data.put("encoding", "UTF-8");
		// 签名方法 01 RSA
		data.put("signMethod", "01");
		// 交易类型 
		data.put("txnType", "04");
		// 交易子类型 
		data.put("txnSubType", "00");
		// 业务类型
		data.put("bizType", "000201");
		// 渠道类型，07-PC，08-手机
		data.put("channelType", "08");
		// 前台通知地址 ，控件接入方式无作用
		data.put("frontUrl", getNotifyUrl(sn, NotifyMethod.sync));
		// 后台通知地址
		data.put("backUrl", getNotifyUrl(sn, NotifyMethod.async));
		// 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
		data.put("accessType", "0");
		// 商户号码，请改成自己的商户号
		data.put("merId", merId);
		//原消费的queryId，可以从查询接口或者通知接口中获取
		data.put("origQryId", sn);
		// 商户订单号，8-40位数字字母，重新产生，不同于原消费
		data.put("orderId", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		// 订单发送时间，取系统时间
		data.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		// 交易金额
		data.put("txnAmt", payment.getAmount().multiply(new BigDecimal(100)).setScale(0).toString());
		// 交易币种
		data.put("currencyCode", "156");
		// 请求方保留域，透传字段，查询、通知、对账文件中均会原样出现
		// data.put("reqReserved", "透传信息");

		data = signData(data);

		// 交易请求url 从配置文件读取
		String url = SDKConfig.getConfig().getBackRequestUrl();
		String result = submitUrl(data,url);*/
		
		return parameterMap;
	}
	
	@Override
	public boolean verifyNotify(String orderId, HttpServletRequest request) {
//		PluginConfig pluginConfig = getPluginConfig();
//		Payment payment = getPayment(sn);
		//request.getParameterMap().get("signature")[0].equals(request.getParameter("signature")) && 
		/*if (merId.equals(request.getParameter("merId")) && orderId.equals(request.getParameter("orderId")) 
				&& CURRENCY.equals(request.getParameter("currencyCode")) && "00".equals(request.getParameter("respCode"))) {
			Map<String, String> parameterMap = new HashMap<String, String>();
			parameterMap.put("version", "5.0.0");
			// 字符集编码 默认"UTF-8"
			parameterMap.put("encoding", "UTF-8");
			// 签名方法 01 RSA
			parameterMap.put("signMethod", "01");
			// 交易类型 
			parameterMap.put("txnType", "00");
			// 交易子类型 
			parameterMap.put("txnSubType", "00");
			// 业务类型
			parameterMap.put("bizType", "000000");
			// 渠道类型，07-PC，08-手机
			parameterMap.put("channelType", "07");
			// 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
			parameterMap.put("accessType", "0");
			// 商户号码，请改成自己的商户号
			parameterMap.put("merId", merId);
			// 商户订单号，请修改被查询的交易的订单号
			parameterMap.put("orderId", orderId);
			// 订单发送时间，请修改被查询的交易的订单发送时间
			parameterMap.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			
			
			SDKUtil.sign(parameterMap, "UTF-8");
			
			String url = SDKConfig.getConfig().getSingleQueryUrl();
			String result = submitUrl(parameterMap,url);
			System.out.println("result-->"+result);
			if (ArrayUtils.contains(result.split("&"), "respCode=00")) {
				return true;
			}
		}*/
		
		try {
			request.setCharacterEncoding(QuickPayConf.charset);

			
			String[] resArr = new String[QuickPayConf.notifyVo.length];
			for (int i = 0; i < QuickPayConf.notifyVo.length; i++) {
				resArr[i] = request.getParameter(QuickPayConf.notifyVo[i]);
			}
			Map<String,String> map = new HashMap<String,String>();
			map.put("paymentBillId", resArr[8]);//交易号
			map.put("orderAmount", resArr[6]);//交易金额
			map.put("traceNumber", resArr[16]);//第三方交易号
			map.put("traceTime", resArr[17]);//交易时间
			
			
			String signature = request.getParameter(QuickPayConf.signature);
			String signMethod = request.getParameter(QuickPayConf.signMethod);

			Boolean signatureCheck = new QuickPayUtils().checkSign(resArr, signMethod, signature);
			if ("00".equals(resArr[10])) {
				return true;
			}
		}catch(Exception e){
		}
		return false;
	}
	
	/**
	 * java main方法 数据提交 提交到后台
	 * 
	 * @param contentData
	 * @return 返回报文 map
	 */
	public static String submitUrl(
			Map<String, String> submitFromData,String requestUrl) {
		String resultString = "";
		String encoding = "UTF-8";
		/**
		 * 发送
		 */
		HttpClient hc = new HttpClient(requestUrl, 30000, 30000);
		try {
			int status = hc.send(submitFromData, encoding);
			if (200 == status) {
				resultString = hc.getResult();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		Map<String, String> resData = new HashMap<String, String>();
//		/**
//		 * 验证签名
//		 */
//		if (null != resultString && !"".equals(resultString)) {
//			// 将返回结果转换为map
//			resData = SDKUtil.convertResultStringToMap(resultString);
//			if (SDKUtil.validate(resData, encoding)) {
//				System.out.println("验证签名成功");
//			} else {
//				System.out.println("验证签名失败");
//			}
//			// 打印返回报文
//			System.out.println("打印返回报文：" + resultString);
//		}
		return resultString;
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
	 * @param parameterMap
	 *            参数
	 * @return 签名
	 */
	private String generateSign(Map<String, ?> parameterMap) {
//		PluginConfig pluginConfig = getPluginConfig();
//		return DigestUtils.md5Hex(joinKeyValue(new TreeMap<String, Object>(parameterMap), null, "&key=" + DigestUtils.md5Hex(pluginConfig.getAttribute("key")), "&", true, "signMethod", "signature"));
		return null;
	}

	@Override
	public boolean verifyRefundsNotify(String sn, HttpServletRequest request) {
//		PluginConfig pluginConfig = getPluginConfig();
//		if (generateSign(request.getParameterMap()).equals(request.getParameter("sign"))
//				&& pluginConfig.getAttribute("partner").equals(request.getParameter("seller_id"))) {
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
		Map<String, String> data = new HashMap<String, String>();
		// 版本号
		data.put("version", "5.0.0");
		// 字符集编码 默认"UTF-8"
		data.put("encoding", "UTF-8");
		// 签名方法 01 RSA
		data.put("signMethod", "01");
		// 交易类型 01-消费
		data.put("txnType", "01");
		// 交易子类型 01:自助消费 02:订购 03:分期付款
		data.put("txnSubType", "01");
		// 业务类型
		data.put("bizType", "000201");
		// 渠道类型，07-PC，08-手机
		data.put("channelType", "07");
		// 前台通知地址 ，控件接入方式无作用
		data.put("frontUrl", getNotifyUrl(sn, NotifyMethod.sync));
		// 后台通知地址
		data.put("backUrl", getNotifyUrl(sn, NotifyMethod.async));
		// 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
		data.put("accessType", "0");
		// 商户号码，请改成自己的商户号
		data.put("merId", merId);
		// 商户订单号，8-40位数字字母
		data.put("orderId", sn);
		// 订单发送时间，取系统时间
		data.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		// 交易金额，单位分
		data.put("txnAmt", txnAmt.multiply(new BigDecimal(100)).setScale(0).toString());
		// 交易币种
		data.put("currencyCode", "156");
		// 请求方保留域，透传字段，查询、通知、对账文件中均会原样出现
		// data.put("reqReserved", "透传信息");
		// 订单描述，可不上送，上送时控件中会显示该信息
		// data.put("orderDesc", "订单描述");

		data = signData(data);

		// 交易请求url 从配置文件读取
		String requestAppUrl = SDKConfig.getConfig().getAppRequestUrl();
//		String requestAppUrl = "https://101.231.204.80:5000/gateway/api/appTransReq.do";

		String result = submitUrl(data, requestAppUrl);
		
		String tn = null;
		if (ArrayUtils.contains(result.split("&"), "respCode=00")) {
			Map<String, String> resData = new HashMap<String, String>();
			/**
			 * 验证签名
			 */
			if (null != result && !"".equals(result)) {
				// 将返回结果转换为map
				resData = SDKUtil.convertResultStringToMap(result);
				tn = resData.get("tn");
			}
			
			
			return tn;
		}
		return tn;
	}
	
	/**
	 * 银联退款
	 * @param sn
	 * @param queryId
	 * @param request
	 * @return
	 */
	public boolean unionpayRefunds(BigDecimal amount, String queryId, HttpServletRequest request){
		/**
		 * 组装请求报文
		 */
		Map<String, String> data = new HashMap<String, String>();
		// 版本号
		data.put("version", "5.0.0");
		// 字符集编码 默认"UTF-8"
		data.put("encoding", "UTF-8");
		// 签名方法 01 RSA
		data.put("signMethod", "01");
		// 交易类型 
		data.put("txnType", "04");
		// 交易子类型 
		data.put("txnSubType", "00");
		// 业务类型
		data.put("bizType", "000201");
		// 渠道类型，07-PC，08-手机
		data.put("channelType", "08");
		// 前台通知地址 ，控件接入方式无作用
		data.put("frontUrl", getRefundsNotifyUrl(queryId));
		// 后台通知地址
		data.put("backUrl", getRefundsNotifyUrl(queryId));
		// 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
		data.put("accessType", "0");
		// 商户号码，请改成自己的商户号
		data.put("merId", merId);
		//原消费的queryId，可以从查询接口或者通知接口中获取
		data.put("origQryId", queryId);
		// 商户订单号，8-40位数字字母，重新产生，不同于原消费
		data.put("orderId", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		// 订单发送时间，取系统时间
		data.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		// 交易金额
		data.put("txnAmt", amount.multiply(new BigDecimal(100)).setScale(0).toString());
		// 交易币种
		data.put("currencyCode", "156");
		// 请求方保留域，透传字段，查询、通知、对账文件中均会原样出现
		// data.put("reqReserved", "透传信息");

		data = signData(data);

		// 交易请求url 从配置文件读取
		String url = SDKConfig.getConfig().getBackRequestUrl();
		String result = submitUrl(data,url);
		if (ArrayUtils.contains(result.split("&"), "respCode=00")) {
			return true;
		}
		return false;
	}
	
	private String getRefundsNotifyUrl(String sn) {
		return Constants.BASE_URL_COM + "/unionpay_refund/notify/" + sn + ".jhtml";
	}

	@Override
	public int compareTo(PaymentPlugin o) {
		// TODO Auto-generated method stub
		return 0;
	}
}