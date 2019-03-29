package bmatser.model;

public class PaymentBillOrder {
	
	
	private Long id;
	private String paymentBillId;
	private Long orderId;
	private String status;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPaymentBillId() {
		return paymentBillId;
	}
	public void setPaymentBillId(String paymentBillId) {
		this.paymentBillId = paymentBillId;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
