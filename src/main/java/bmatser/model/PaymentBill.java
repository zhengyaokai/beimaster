package bmatser.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PaymentBill {
	
	private String id;
	private Integer buyerId;
	private Double shouldAmount;
	private Double realAmount;
	private Double paymentScoreAmount;
	private Double goodsSaleAmount;
	private String thirdTradeCode;
	private String payType;
	private String payStatus;
	private Timestamp payTime;
	private String remark;
	private Integer orderNum;
	private String paymentNo;
	private Timestamp paymentTime;
	private String status;
	private String rejectStatus;
	private String rejectReason;
	private String companyAccount;
	private String paymentAccount;
	private String paymentAccountOnline;
	private BigDecimal balanceDeduction;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(Integer buyerId) {
		this.buyerId = buyerId;
	}
	public Double getShouldAmount() {
		return shouldAmount;
	}
	public void setShouldAmount(Double shouldAmount) {
		this.shouldAmount = shouldAmount;
	}
	public Double getRealAmount() {
		return realAmount;
	}
	public void setRealAmount(Double realAmount) {
		this.realAmount = realAmount;
	}
	public Double getPaymentScoreAmount() {
		return paymentScoreAmount;
	}
	public void setPaymentScoreAmount(Double paymentScoreAmount) {
		this.paymentScoreAmount = paymentScoreAmount;
	}
	public String getThirdTradeCode() {
		return thirdTradeCode;
	}
	public void setThirdTradeCode(String thirdTradeCode) {
		this.thirdTradeCode = thirdTradeCode;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public Timestamp getPayTime() {
		return payTime;
	}
	public void setPayTime(Timestamp payTime) {
		this.payTime = payTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public String getPaymentNo() {
		return paymentNo;
	}
	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}
	public Timestamp getPaymentTime() {
		return paymentTime;
	}
	public void setPaymentTime(Timestamp paymentTime) {
		this.paymentTime = paymentTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRejectStatus() {
		return rejectStatus;
	}
	public void setRejectStatus(String rejectStatus) {
		this.rejectStatus = rejectStatus;
	}
	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	public String getCompanyAccount() {
		return companyAccount;
	}
	public void setCompanyAccount(String companyAccount) {
		this.companyAccount = companyAccount;
	}
	public Double getGoodsSaleAmount() {
		return goodsSaleAmount;
	}
	public void setGoodsSaleAmount(Double goodsSaleAmount) {
		this.goodsSaleAmount = goodsSaleAmount;
	}
	public BigDecimal getBalanceDeduction() {
		return balanceDeduction;
	}
	public void setBalanceDeduction(BigDecimal balanceDeduction) {
		this.balanceDeduction = balanceDeduction;
	}
	public String getPaymentAccount() {
		return paymentAccount;
	}
	public void setPaymentAccount(String paymentAccount) {
		this.paymentAccount = paymentAccount;
	}
	public String getPaymentAccountOnline() {
		return paymentAccountOnline;
	}
	public void setPaymentAccountOnline(String paymentAccountOnline) {
		this.paymentAccountOnline = paymentAccountOnline;
	}
	
	
}
