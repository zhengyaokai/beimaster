package bmatser.model;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 出账明细实体
 * @author 027-20160318
 *
 */
public class BillInfo {
	private String customerName;//贷款客户名称
	private String customerCode;//工电宝系统内为商户创建唯一编码			
	private String accountNo;//出账单号	
	private String associatedOrderNo;//订单号	
	private String orderStatus;//订单类型：采购、退货
	private BigDecimal paymentAmount;//出账金额		
	private Long paymentTerm;//贷款账期     单位：天，数字为30,60,90
	private BigDecimal paymentInterest;//贷款利息 
	private BigDecimal overdueAmount;//罚息		
	private BigDecimal returnInterest;//退货利息
	private String repaymentStatus;//状态：1：未还，2:已结清，3:逾期，4:已退货
	private String repaymentType;//还款方式 1：一次性还本付息
	private String repaymentAccountName;//还款账户名
	private String repaymentAccountBank;//还款账户所属银行
	private String repaymentAccountNo;//还款账号
	private String paymentRate;//贷款利率
	private String expiryDate;//到期日  Date  yyyy-mm-dd
	private String paymentDate;//出账日期  DateTime  yyyy-mm-dd HH:MM:SS
	private String repaymentTime;//还款时间
	private Long updateStatus;//0：新增，1：更新
	private String updateTime;//更新时间
	/*private String statusCode;//状态码：-1:失败,1:成功
	private Date responseTime;//服务器时间
	private String Msg;//失败信息
*/	public String getCustomerName() {
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
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAssociatedOrderNo() {
		return associatedOrderNo;
	}
	public void setAssociatedOrderNo(String associatedOrderNo) {
		this.associatedOrderNo = associatedOrderNo;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public Long getPaymentTerm() {
		return paymentTerm;
	}
	public void setPaymentTerm(Long paymentTerm) {
		this.paymentTerm = paymentTerm;
	}
	public BigDecimal getPaymentInterest() {
		return paymentInterest;
	}
	public void setPaymentInterest(BigDecimal paymentInterest) {
		this.paymentInterest = paymentInterest;
	}
	public BigDecimal getOverdueAmount() {
		return overdueAmount;
	}
	public void setOverdueAmount(BigDecimal overdueAmount) {
		this.overdueAmount = overdueAmount;
	}
	public BigDecimal getReturnInterest() {
		return returnInterest;
	}
	public void setReturnInterest(BigDecimal returnInterest) {
		this.returnInterest = returnInterest;
	}
	public String getRepaymentStatus() {
		return repaymentStatus;
	}
	public void setRepaymentStatus(String repaymentStatus) {
		this.repaymentStatus = repaymentStatus;
	}
	public String getRepaymentType() {
		return repaymentType;
	}
	public void setRepaymentType(String repaymentType) {
		this.repaymentType = repaymentType;
	}
	public String getPaymentRate() {
		return paymentRate;
	}
	public void setPaymentRate(String paymentRate) {
		this.paymentRate = paymentRate;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getRepaymentTime() {
		return repaymentTime;
	}
	public void setRepaymentTime(String repaymentTime) {
		this.repaymentTime = repaymentTime;
	}
	/*public String getStatusCode() {
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
	}*/
	public Long getUpdateStatus() {
		return updateStatus;
	}
	public void setUpdateStatus(Long updateStatus) {
		this.updateStatus = updateStatus;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getRepaymentAccountName() {
		return repaymentAccountName;
	}
	public void setRepaymentAccountName(String repaymentAccountName) {
		this.repaymentAccountName = repaymentAccountName;
	}
	public String getRepaymentAccountBank() {
		return repaymentAccountBank;
	}
	public void setRepaymentAccountBank(String repaymentAccountBank) {
		this.repaymentAccountBank = repaymentAccountBank;
	}
	public String getRepaymentAccountNo() {
		return repaymentAccountNo;
	}
	public void setRepaymentAccountNo(String repaymentAccountNo) {
		this.repaymentAccountNo = repaymentAccountNo;
	}

}
