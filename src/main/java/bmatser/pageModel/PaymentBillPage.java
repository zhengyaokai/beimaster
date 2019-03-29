
package bmatser.pageModel;

import java.math.BigDecimal;
import java.sql.Timestamp;

import bmatser.model.PaymentBill;

public class PaymentBillPage extends PaymentBill{

	private int page=0;
	
	private int rows=10;
	
	private String payStartTime;
	
	private String payEndTime;
	
	private Long orderId;
	
	private String dealerName;
	
	private BigDecimal scoreDeductionAmout;
	
	private Timestamp bankReceiveTime;
	
	private String tradeNo;

	public int getPage() {
		return page;
	}

	public int getRows() {
		return rows;
	}

	public String getPayStartTime() {
		return payStartTime;
	}

	public String getPayEndTime() {
		return payEndTime;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void setPayStartTime(String payStartTime) {
		this.payStartTime = payStartTime;
	}

	public void setPayEndTime(String payEndTime) {
		this.payEndTime = payEndTime;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public BigDecimal getScoreDeductionAmout() {
		return scoreDeductionAmout;
	}

	public void setScoreDeductionAmout(BigDecimal scoreDeductionAmout) {
		this.scoreDeductionAmout = scoreDeductionAmout;
	}

	public Timestamp getBankReceiveTime() {
		return bankReceiveTime;
	}

	public void setBankReceiveTime(Timestamp bankReceiveTime) {
		this.bankReceiveTime = bankReceiveTime;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
}
