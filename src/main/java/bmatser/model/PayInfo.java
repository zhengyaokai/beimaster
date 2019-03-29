/** 
 *	@author jxw
 *	@Date 2016-12-16
 * @description 
 */
package bmatser.model;

import java.math.BigDecimal;

/**
 * @author felx
 *	@Date 2016-12-16
 *	@description 
 */
public class PayInfo {
	
	
	
	 private String orderId;//订单号
	 private BigDecimal payAmount;//付款金额（不扣除余额）
	 private BigDecimal realAmount;//实付金额
	 private BigDecimal balanceDeduction;//抵扣余额
	 private BigDecimal balance;//用户余额
	 private int isUse;//是否使用余额
	 
	 
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public BigDecimal getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	public BigDecimal getRealAmount() {
		return realAmount;
	}
	public void setRealAmount(BigDecimal realAmount) {
		this.realAmount = realAmount;
	}
	public BigDecimal getBalanceDeduction() {
		return balanceDeduction;
	}
	public void setBalanceDeduction(BigDecimal balanceDeduction) {
		this.balanceDeduction = balanceDeduction;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public int getIsUse() {
		return isUse;
	}
	public void setIsUse(int isUse) {
		this.isUse = isUse;
	}
	public void toUseBalance(int isUse) {
		this.isUse = isUse;
		if(isUse==0){
			this.balanceDeduction = BigDecimal.ZERO;
			this.realAmount = this.payAmount;
		}else if(isUse==-1){
			if(BigDecimal.ZERO.compareTo(this.balanceDeduction)!=0){
				this.isUse = 1;
				this.realAmount = this.payAmount.subtract(this.balanceDeduction);
			}else{
				this.realAmount = this.payAmount;
				this.isUse = 0;
			}
		}else{
			if(this.payAmount.compareTo(this.balance)>=0){
				this.realAmount = this.payAmount.subtract(this.balance);
				this.balanceDeduction = this.balance;
			}else{
				this.realAmount = BigDecimal.ZERO;
				this.balanceDeduction = this.payAmount;
			}
		}
	}
}
