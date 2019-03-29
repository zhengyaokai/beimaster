package bmatser.pageModel;

import java.math.BigDecimal;

public class GoldOrderApply {
	
	private String previewStatus;//状态码：-1：异常，0：审核失败，1：审核成功
	private String accountNo;//出账单号
	private String orderNo;//订单号
	private BigDecimal principal;//本金
	private BigDecimal interest;//利息Interest
	private BigDecimal totalAmount;//本息合计
	private String currency;//币种
	private String endDate;//到期日
	private String responseTime;//服务器时间
	private String msg;//失败信息
	
	public String getPreviewStatus() {
		return previewStatus;
	}
	public void setPreviewStatus(String previewStatus) {
		this.previewStatus = previewStatus;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public BigDecimal getPrincipal() {
		return principal;
	}
	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	
	

}
