package bmatser.model;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 还款信息实体
 * @author 027-20160318
 *
 */
public class ReimbursementInfo {
	private String customerName;//贷款客户名称
	private String customerCode;//工电宝系统内为商户创建唯一编码				
	private String associatedOrderNo;//关联出账单号	
	private String repaymentNo;//还款单号
	private BigDecimal repaymentAmount;//还款总金额		
	private BigDecimal repaymentPrincipal;//还款本金 
	private BigDecimal repaymentInterest;//还款利息		
	private BigDecimal paymentPenalty;//还款罚息
	private BigDecimal paymentTerm;//应还总金额
	private Date paymentDate;//还款日期
	private String statusCode;//状态码：-1:失败,1:成功
	private Date responseTime;//服务器时间
	private String Msg;//失败信息
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getAssociatedOrderNo() {
		return associatedOrderNo;
	}
	public void setAssociatedOrderNo(String associatedOrderNo) {
		this.associatedOrderNo = associatedOrderNo;
	}
	public String getRepaymentNo() {
		return repaymentNo;
	}
	public void setRepaymentNo(String repaymentNo) {
		this.repaymentNo = repaymentNo;
	}
	public BigDecimal getRepaymentAmount() {
		return repaymentAmount;
	}
	public void setRepaymentAmount(BigDecimal repaymentAmount) {
		this.repaymentAmount = repaymentAmount;
	}
	public BigDecimal getRepaymentPrincipal() {
		return repaymentPrincipal;
	}
	public void setRepaymentPrincipal(BigDecimal repaymentPrincipal) {
		this.repaymentPrincipal = repaymentPrincipal;
	}
	public BigDecimal getRepaymentInterest() {
		return repaymentInterest;
	}
	public void setRepaymentInterest(BigDecimal repaymentInterest) {
		this.repaymentInterest = repaymentInterest;
	}
	public BigDecimal getPaymentPenalty() {
		return paymentPenalty;
	}
	public void setPaymentPenalty(BigDecimal paymentPenalty) {
		this.paymentPenalty = paymentPenalty;
	}
	public BigDecimal getPaymentTerm() {
		return paymentTerm;
	}
	public void setPaymentTerm(BigDecimal paymentTerm) {
		this.paymentTerm = paymentTerm;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public Date getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}
	public String getMsg() {
		return Msg;
	}
	public void setMsg(String msg) {
		Msg = msg;
	}

}
