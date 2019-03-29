package bmatser.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class JnPaid {
	
	private Long id;
	private String paidNo;
	private String bankSerialNo;
	private Timestamp actualPaidTime;
	private BigDecimal paidTotalAmount;
	private Timestamp ackStartTime;
	private Timestamp ackEndTime;
	private BigDecimal currentPaid;
	private BigDecimal currentBalance;
	private BigDecimal priorPaid;
	private String priorPaidNo;
	private Integer totalOrders;
	private BigDecimal orderTotalAmt;
	private Integer totalReturn;
	private BigDecimal returnTotalAmt;
	private String status;
	private Timestamp createTime;
	private BigDecimal actualReceivableAmount;
	
	public BigDecimal getActualReceivableAmount() {
		return actualReceivableAmount;
	}
	public void setActualReceivableAmount(BigDecimal actualReceivableAmount) {
		this.actualReceivableAmount = actualReceivableAmount;
	}
	public Long getId() {
		return id;
	}
	public String getPaidNo() {
		return paidNo;
	}
	public String getBankSerialNo() {
		return bankSerialNo;
	}
	public Timestamp getActualPaidTime() {
		return actualPaidTime;
	}
	public BigDecimal getPaidTotalAmount() {
		return paidTotalAmount;
	}
	public Timestamp getAckStartTime() {
		return ackStartTime;
	}
	public Timestamp getAckEndTime() {
		return ackEndTime;
	}
	public BigDecimal getCurrentPaid() {
		return currentPaid;
	}
	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}
	public BigDecimal getPriorPaid() {
		return priorPaid;
	}
	public String getPriorPaidNo() {
		return priorPaidNo;
	}
	public Integer getTotalOrders() {
		return totalOrders;
	}
	public BigDecimal getOrderTotalAmt() {
		return orderTotalAmt;
	}
	public Integer getTotalReturn() {
		return totalReturn;
	}
	public BigDecimal getReturnTotalAmt() {
		return returnTotalAmt;
	}
	public String getStatus() {
		return status;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setPaidNo(String paidNo) {
		this.paidNo = paidNo;
	}
	public void setBankSerialNo(String bankSerialNo) {
		this.bankSerialNo = bankSerialNo;
	}
	public void setActualPaidTime(Timestamp actualPaidTime) {
		this.actualPaidTime = actualPaidTime;
	}
	public void setPaidTotalAmount(BigDecimal paidTotalAmount) {
		this.paidTotalAmount = paidTotalAmount;
	}
	public void setAckStartTime(Timestamp ackStartTime) {
		this.ackStartTime = ackStartTime;
	}
	public void setAckEndTime(Timestamp ackEndTime) {
		this.ackEndTime = ackEndTime;
	}
	public void setCurrentPaid(BigDecimal currentPaid) {
		this.currentPaid = currentPaid;
	}
	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}
	public void setPriorPaid(BigDecimal priorPaid) {
		this.priorPaid = priorPaid;
	}
	public void setPriorPaidNo(String priorPaidNo) {
		this.priorPaidNo = priorPaidNo;
	}
	public void setTotalOrders(Integer totalOrders) {
		this.totalOrders = totalOrders;
	}
	public void setOrderTotalAmt(BigDecimal orderTotalAmt) {
		this.orderTotalAmt = orderTotalAmt;
	}
	public void setTotalReturn(Integer totalReturn) {
		this.totalReturn = totalReturn;
	}
	public void setReturnTotalAmt(BigDecimal returnTotalAmt) {
		this.returnTotalAmt = returnTotalAmt;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
}
