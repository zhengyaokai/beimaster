package bmatser.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class JnOrderDetails {
	
	private Long id;
	private String paidNo;
	private String orderNo;
	private String returnNo;
	private BigDecimal orderAmount;
	private Date orderDate;
	private Integer dealerId;
	private String customerName;
	private String isReturn;
	private String status;
	private Timestamp createTime;
	
	public Integer getDealerId() {
		return dealerId;
	}
	public void setDealerId(Integer dealerId) {
		this.dealerId = dealerId;
	}
	public String getReturnNo() {
		return returnNo;
	}
	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}
	public Long getId() {
		return id;
	}
	public String getPaidNo() {
		return paidNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public BigDecimal getOrderAmount() {
		return orderAmount;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public String getCustomerName() {
		return customerName;
	}
	public String getIsReturn() {
		return isReturn;
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
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public void setIsReturn(String isReturn) {
		this.isReturn = isReturn;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
}
