package bmatser.model;

import java.math.BigDecimal;

import bmatser.util.PaymentIDProduce;

public class OrderInfoSave {
	
	private Long orderId;
	private String dealerId;
	private BigDecimal orderAmount;
	private BigDecimal goodsAmount;
	private BigDecimal freightAmount;
	private String consignAddressId;
	private String consignAddressInfo;
	private String consignAddressInfoSecret;
	private String buyerRemark;
	private int isNeedInvoice;
	private String score;
	private int isIssueInvoice;
	private BigDecimal scoreDeductionAmout;
	private int orderSource;
	private int orderChannel;
	private BigDecimal fullCutAmount;
	private String customerServiceId;
	private String customerManagerId;
	private int orderType;
	private BigDecimal groupOrderSale;
	
	private String packageInfo;
	
	
	public OrderInfoSave(String dealerId) {
		this.setOrderId(PaymentIDProduce.getPaymentID());
		this.dealerId = dealerId;
	}
	
	
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getDealerId() {
		return dealerId;
	}
	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}
	public BigDecimal getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}
	public BigDecimal getGoodsAmount() {
		return goodsAmount;
	}
	public void setGoodsAmount(BigDecimal goodsAmount) {
		this.goodsAmount = goodsAmount;
	}
	public BigDecimal getFreightAmount() {
		return freightAmount;
	}
	public void setFreightAmount(BigDecimal freightAmount) {
		this.freightAmount = freightAmount;
	}
	public String getConsignAddressId() {
		return consignAddressId;
	}
	public void setConsignAddressId(String consignAddressId) {
		this.consignAddressId = consignAddressId;
	}
	public String getConsignAddressInfo() {
		return consignAddressInfo;
	}
	public void setConsignAddressInfo(String consignAddressInfo) {
		this.consignAddressInfo = consignAddressInfo;
	}
	public String getConsignAddressInfoSecret() {
		return consignAddressInfoSecret;
	}
	public void setConsignAddressInfoSecret(String consignAddressInfoSecret) {
		this.consignAddressInfoSecret = consignAddressInfoSecret;
	}
	public String getBuyerRemark() {
		return buyerRemark;
	}
	public void setBuyerRemark(String buyerRemark) {
		this.buyerRemark = buyerRemark;
	}
	public int getIsNeedInvoice() {
		return isNeedInvoice;
	}
	public void setIsNeedInvoice(int isNeedInvoice) {
		this.isNeedInvoice = isNeedInvoice;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public int getIsIssueInvoice() {
		return isIssueInvoice;
	}
	public void setIsIssueInvoice(int isIssueInvoice) {
		this.isIssueInvoice = isIssueInvoice;
	}
	public BigDecimal getScoreDeductionAmout() {
		return scoreDeductionAmout;
	}
	public void setScoreDeductionAmout(BigDecimal scoreDeductionAmout) {
		this.scoreDeductionAmout = scoreDeductionAmout;
	}
	public int getOrderSource() {
		return orderSource;
	}
	public void setOrderSource(int orderSource) {
		this.orderSource = orderSource;
	}
	public int getOrderChannel() {
		return orderChannel;
	}
	public void setOrderChannel(int orderChannel) {
		this.orderChannel = orderChannel;
	}
	public BigDecimal getFullCutAmount() {
		return fullCutAmount;
	}
	public void setFullCutAmount(BigDecimal fullCutAmount) {
		this.fullCutAmount = fullCutAmount;
	}
	public String getCustomerServiceId() {
		return customerServiceId;
	}
	public void setCustomerServiceId(String customerServiceId) {
		this.customerServiceId = customerServiceId;
	}
	public String getCustomerManagerId() {
		return customerManagerId;
	}
	public void setCustomerManagerId(String customerManagerId) {
		this.customerManagerId = customerManagerId;
	}
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	public BigDecimal getGroupOrderSale() {
		return groupOrderSale;
	}
	public void setGroupOrderSale(BigDecimal groupOrderSale) {
		this.groupOrderSale = groupOrderSale;
	}
	
	
	public void saveOrderCountModel(OrderCountModel order){
		this.setOrderAmount(order.getTotalOrderPrice());
		this.setGoodsAmount(order.getTotalGoodsPrice());
		this.setFreightAmount(order.getTotalFreight());
		this.setScore(order.getTotalScore().toString());
		this.setScoreDeductionAmout(order.getTotalCash());
		this.setFullCutAmount(order.getTotalFullcut());
		this.setOrderSource(2);
		this.setIsIssueInvoice(1);
		if(order.getGroupList().size()>0 && order.getPackageGoodsList().size()>0){
			this.setOrderType(4);
		}else if(order.getGroupList().size()>0){
			this.setOrderType(1);
		}else if(order.getPackageGoodsList().size()>0){
			this.setOrderType(3);
		}else{
			this.setOrderType(0);
		}
		this.setGroupOrderSale(order.getGroupSale());
	}


	public String getPackageInfo() {
		if(this.packageInfo==null){
			return "";
		}
		return packageInfo;
	}


	public void setPackageInfo(String packageInfo) {
		this.packageInfo = packageInfo;
	}

}
