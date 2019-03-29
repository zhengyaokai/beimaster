package bmatser.plugin.b2bPay;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
@Component
public class B2bPayment implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4304544529725405806L;
	
	
	/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
	private String version= "5.0.0";//版本号，全渠道默认值
	private String encoding = "UTF-8";//字符集编码，可以使用UTF-8,GBK两种方式
	private String signMethod = "01"; //签名方法，只支持 01：RSA方式证书加密
	private String txnType = "01";//交易类型 ，01：消费
	private String txnSubType = "01";//交易子类型， 01：自助消费
	private String bizType = "000202";//业务类型 000202: B2B
	private String channelType = "07";//渠道类型 固定07
	/***商户接入参数***/
	private String merId = "898320548160547";//商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
	private String accessType = "0";//接入类型，0：直连商户 
	
	private String orderId;//商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则

	private String txnTime;//订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
	private String currencyCode = "156";//交易币种（境内商户一般是156 人民币）
	
	private String txnAmt;//交易金额，单位分，不要带小数点
	
	//前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
	//如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
	//异步通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
	private String frontUrl = "http://127.0.0.1:8080/ACPSample_B2B/frontRcvResponse";
	//后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
	//后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
	//注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码 
	//    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
	//    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
	private String backUrl = "http://222.222.222.222:8080/ACPSample_B2B/BackRcvResponse";
	
	public B2bPayment() {
		this.txnTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public String getSignMethod() {
		return signMethod;
	}
	public void setSignMethod(String signMethod) {
		this.signMethod = signMethod;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getTxnSubType() {
		return txnSubType;
	}
	public void setTxnSubType(String txnSubType) {
		this.txnSubType = txnSubType;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public String getAccessType() {
		return accessType;
	}
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTxnTime() {
		return txnTime;
	}
	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getTxnAmt() {
		return txnAmt;
	}
	public void setTxnAmt(String txnAmt) {
		this.txnAmt = txnAmt;
	}
	public String getFrontUrl() {
		return frontUrl;
	}
	public void setFrontUrl(String frontUrl) {
		this.frontUrl = frontUrl;
	}
	public String getBackUrl() {
		return backUrl;
	}
	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}
	
	public Map<String, String> toHashMap(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("version", this.version);
		map.put("encoding", this.encoding);
		map.put("signMethod", this.signMethod);
		map.put("txnType", this.txnType);
		map.put("txnSubType", this.txnSubType);
		map.put("bizType", this.bizType);
		map.put("channelType", this.channelType);
		map.put("merId", this.merId);
		map.put("accessType", this.accessType);
		map.put("orderId", this.orderId);
		map.put("txnTime", this.txnTime);
		map.put("currencyCode", this.currencyCode);
		map.put("txnAmt", this.txnAmt);
		map.put("frontUrl", this.frontUrl);
		map.put("backUrl", this.backUrl);
		return map;
	}
	
	public Map<String, String> toStatusHashMap(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("version", this.version);
		map.put("encoding", this.encoding);
		map.put("signMethod", this.signMethod);
		map.put("txnType", "00");                             //交易类型 00-默认
		map.put("txnSubType", "00");                          //交易子类型  默认00
		map.put("bizType", this.bizType);
		map.put("merId", this.merId);
		map.put("accessType", this.accessType);
		map.put("orderId", this.orderId); 
		map.put("txnTime", this.txnTime);      
		return map;
	}
}
