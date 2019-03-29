package bmatser.model;

import bmatser.util.DateTypeHelper;

public class ToPayLogger {
	
	private String orderId;
	private String responseMsg;
	private String createTime;
	private String paymentType;
	private boolean isbalance;
	private String requestUrl;
	private int type;
	
	
	
	
	public ToPayLogger(String orderId, String responseMsg, String paymentType,
			boolean isbalance, String requestUrl) {
		this.createTime = DateTypeHelper.getCurrentDateTimeString();
		this.orderId = orderId;
		this.responseMsg = responseMsg;
		this.paymentType = paymentType;
		this.isbalance = isbalance;
		this.requestUrl = requestUrl;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getResponseMsg() {
		return responseMsg;
	}
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	public boolean isIsbalance() {
		return isbalance;
	}
	public void setIsbalance(boolean isbalance) {
		this.isbalance = isbalance;
	}
	public int getType() {
		return type;
	}
	public ToPayLogger setType(int type) {
		this.type = type;
		return this;
	}
	
	
	
	
	
	

}
