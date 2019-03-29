package bmatser.model;

import bmatser.util.PropertyUtil;
/**
 * 
 * @author felx
 * @describe 民生银行支付类
 */
public class CMBC {
	
	
	public String service;//接口名称Y
	public String partner_id;//合作商IDY
	public String input_charset;//编码方式Y
	public String sign_type;//签名类型Y
	public String notify_url;//通知地址Y
	public String out_trade_no;//外部订单号Y
	public String subject;//交易标题Y
	//public String buyer_email;//买家账号
	public String seller_email;//卖家账号Y
	public String amount;//交易金额Y
	public String body;//交易详细内容Y
	//public String show_url;//商品展示网址
	//public String payMethod;//支付方式
	public String default_bank;//默认银行
	//public String royalty_parameters;//分润账号
	public String return_url;//跳转地址Y
	//public String sign;//签名Y
	
	
	public CMBC() {
		this.service = PropertyUtil.getPropertyValue("/properties/info.properties", "cmbc.service");
		this.partner_id = PropertyUtil.getPropertyValue("/properties/info.properties", "cmbc.partnerId");
		this.sign_type = PropertyUtil.getPropertyValue("/properties/info.properties", "cmbc.signType");
		this.subject = PropertyUtil.getPropertyValue("/properties/info.properties", "cmbc.subject");
		this.seller_email = PropertyUtil.getPropertyValue("/properties/info.properties", "cmbc.sellerEmail");
		this.body = PropertyUtil.getPropertyValue("/properties/info.properties", "cmbc.body");
		this.input_charset = PropertyUtil.getPropertyValue("/properties/info.properties", "cmbc.inputCharset");
	}


	public String getService() {
		return service;
	}


	public void setService(String service) {
		this.service = service;
	}


	public String getPartner_id() {
		return partner_id;
	}


	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
	}


	public String getInput_charset() {
		return input_charset;
	}


	public void setInput_charset(String input_charset) {
		this.input_charset = input_charset;
	}


	public String getSign_type() {
		return sign_type;
	}


	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}


	public String getNotify_url() {
		return notify_url;
	}


	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}


	public String getOut_trade_no() {
		return out_trade_no;
	}


	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getSeller_email() {
		return seller_email;
	}


	public void setSeller_email(String seller_email) {
		this.seller_email = seller_email;
	}


	public String getAmount() {
		return amount;
	}


	public void setAmount(String amount) {
		this.amount = amount;
	}


	public String getBody() {
		return body;
	}


	public void setBody(String body) {
		this.body = body;
	}


	public String getReturn_url() {
		return return_url;
	}


	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}


	public String getDefault_bank() {
		return default_bank;
	}


	public void setDefault_bank(String default_bank) {
		this.default_bank = default_bank;
	}
	
}
