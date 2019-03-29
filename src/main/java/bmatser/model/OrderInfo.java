package bmatser.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import bmatser.util.PaymentIDProduce;

public class OrderInfo implements Cloneable{
    private Long id;

    private Integer buyerId;

    private Integer sellerId;

    private BigDecimal orderAmount;

    private BigDecimal goodsAmount;

    private BigDecimal freightAmount;

    private BigDecimal goodsSaleAmount;

    private BigDecimal freightSaleAmount;

    private BigDecimal taxSaleAmount;

    private Date orderTime;

    private String orderStatus;

    private String payStatus;

    private Date shippingTime;

    private String shippingStatus;

    private Date consignTime;

    private Integer consignAddressId;

    private String consignAddressInfo;
    
    private String consignAddressInfoSecret;

    private Integer shippingAddressId;

    private String shippingAddressInfo;

    private Integer sellerLogisticsId;

    private String logisticsRemark;

    private String logisticsCode;

    private String logisticsMultiCode;

    private String buyerRemark;

    private String sellerRemark;

    private String rank;

    private String settleStatus;

    private Date settleTime;

    private Integer settleUserId;

    private String accountStatus;

    private String hasRefundBill;

    private Date delayConsignTime;

    private String delayConsignStatus;

    private String isNeedInvoice;

    private Integer score;

    private String isIssueInvoice;

    private Date payTime;

    private String payType;

    private Long tradeId;

    private BigDecimal scoreDeductionAmout;
    
    private String thirdId;
    
    private String orderStore;
    
    private String orderChannel;
    
    private String serviceRemark;
    
    private String hasChild;
    
    private String originalOrderId;
    
    private BigDecimal ShouldAmount;
    
    private Integer companyAccount;
    
    private List<OrderGoods> goodsList;
    
    private String orderSource;
    
    private BigDecimal payAmount;//订制支付金额
    
    private BigDecimal payShouldAmount;//定制应付金额
    
    private Integer customerServiceId;
    
    private Integer customerManagerId;
    
    private BigDecimal saleAmount;
    
    private Double fullCutAmount;
    
    private Integer paymentAccount;
    private String jdOrderId;
    private Double tail;
    private String childId;
    private Double groupOrderSale;
    private BigDecimal balanceDeduction;
    private Integer rateType;
    
    private int orderType;

    public Integer getPaymentAccount() {
		return paymentAccount;
	}

	public void setPaymentAccount(Integer paymentAccount) {
		this.paymentAccount = paymentAccount;
	}

	public String getConsignAddressInfoSecret() {
		return consignAddressInfoSecret;
	}

	public void setConsignAddressInfoSecret(String consignAddressInfoSecret) {
		this.consignAddressInfoSecret = consignAddressInfoSecret;
	}

	public String getOrderChannel() {
		return orderChannel;
	}

	public void setOrderChannel(String orderChannel) {
		this.orderChannel = orderChannel;
	}

	public String getServiceRemark() {
		return serviceRemark;
	}

	public void setServiceRemark(String serviceRemark) {
		this.serviceRemark = serviceRemark;
	}

	public String getHasChild() {
		return hasChild;
	}

	public void setHasChild(String hasChild) {
		this.hasChild = hasChild;
	}

	public String getOriginalOrderId() {
		return originalOrderId;
	}

	public void setOriginalOrderId(String originalOrderId) {
		this.originalOrderId = originalOrderId;
	}

	public BigDecimal getShouldAmount() {
		return ShouldAmount;
	}

	public void setShouldAmount(BigDecimal shouldAmount) {
		ShouldAmount = shouldAmount;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
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

    public BigDecimal getGoodsSaleAmount() {
        return goodsSaleAmount;
    }

    public void setGoodsSaleAmount(BigDecimal goodsSaleAmount) {
        this.goodsSaleAmount = goodsSaleAmount;
    }

    public BigDecimal getFreightSaleAmount() {
        return freightSaleAmount;
    }

    public void setFreightSaleAmount(BigDecimal freightSaleAmount) {
        this.freightSaleAmount = freightSaleAmount;
    }

    public BigDecimal getTaxSaleAmount() {
        return taxSaleAmount;
    }

    public void setTaxSaleAmount(BigDecimal taxSaleAmount) {
        this.taxSaleAmount = taxSaleAmount;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus == null ? null : orderStatus.trim();
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus == null ? null : payStatus.trim();
    }

    public Date getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(Date shippingTime) {
        this.shippingTime = shippingTime;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus == null ? null : shippingStatus.trim();
    }

    public Date getConsignTime() {
        return consignTime;
    }

    public void setConsignTime(Date consignTime) {
        this.consignTime = consignTime;
    }

    public Integer getConsignAddressId() {
        return consignAddressId;
    }

    public void setConsignAddressId(Integer consignAddressId) {
        this.consignAddressId = consignAddressId;
    }

    public String getConsignAddressInfo() {
        return consignAddressInfo;
    }

    public void setConsignAddressInfo(String consignAddressInfo) {
        this.consignAddressInfo = consignAddressInfo == null ? null : consignAddressInfo.trim();
    }

    public Integer getShippingAddressId() {
        return shippingAddressId;
    }

    public void setShippingAddressId(Integer shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

    public String getShippingAddressInfo() {
        return shippingAddressInfo;
    }

    public void setShippingAddressInfo(String shippingAddressInfo) {
        this.shippingAddressInfo = shippingAddressInfo == null ? null : shippingAddressInfo.trim();
    }

    public Integer getSellerLogisticsId() {
        return sellerLogisticsId;
    }

    public void setSellerLogisticsId(Integer sellerLogisticsId) {
        this.sellerLogisticsId = sellerLogisticsId;
    }

    public String getLogisticsRemark() {
        return logisticsRemark;
    }

    public void setLogisticsRemark(String logisticsRemark) {
        this.logisticsRemark = logisticsRemark == null ? null : logisticsRemark.trim();
    }

    public String getLogisticsCode() {
        return logisticsCode;
    }

    public void setLogisticsCode(String logisticsCode) {
        this.logisticsCode = logisticsCode == null ? null : logisticsCode.trim();
    }

    public String getLogisticsMultiCode() {
        return logisticsMultiCode;
    }

    public void setLogisticsMultiCode(String logisticsMultiCode) {
        this.logisticsMultiCode = logisticsMultiCode == null ? null : logisticsMultiCode.trim();
    }

    public String getBuyerRemark() {
        return buyerRemark;
    }

    public void setBuyerRemark(String buyerRemark) {
        this.buyerRemark = buyerRemark == null ? null : buyerRemark.trim();
    }

    public String getSellerRemark() {
        return sellerRemark;
    }

    public void setSellerRemark(String sellerRemark) {
        this.sellerRemark = sellerRemark == null ? null : sellerRemark.trim();
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank == null ? null : rank.trim();
    }

    public String getSettleStatus() {
        return settleStatus;
    }

    public void setSettleStatus(String settleStatus) {
        this.settleStatus = settleStatus == null ? null : settleStatus.trim();
    }

    public Date getSettleTime() {
        return settleTime;
    }

    public void setSettleTime(Date settleTime) {
        this.settleTime = settleTime;
    }

    public Integer getSettleUserId() {
        return settleUserId;
    }

    public void setSettleUserId(Integer settleUserId) {
        this.settleUserId = settleUserId;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus == null ? null : accountStatus.trim();
    }

    public String getHasRefundBill() {
        return hasRefundBill;
    }

    public void setHasRefundBill(String hasRefundBill) {
        this.hasRefundBill = hasRefundBill == null ? null : hasRefundBill.trim();
    }

    public Date getDelayConsignTime() {
        return delayConsignTime;
    }

    public void setDelayConsignTime(Date delayConsignTime) {
        this.delayConsignTime = delayConsignTime;
    }

    public String getDelayConsignStatus() {
        return delayConsignStatus;
    }

    public void setDelayConsignStatus(String delayConsignStatus) {
        this.delayConsignStatus = delayConsignStatus == null ? null : delayConsignStatus.trim();
    }

    public String getIsNeedInvoice() {
        return isNeedInvoice;
    }

    public void setIsNeedInvoice(String isNeedInvoice) {
        this.isNeedInvoice = isNeedInvoice == null ? null : isNeedInvoice.trim();
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getIsIssueInvoice() {
        return isIssueInvoice;
    }

    public void setIsIssueInvoice(String isIssueInvoice) {
        this.isIssueInvoice = isIssueInvoice == null ? null : isIssueInvoice.trim();
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public BigDecimal getScoreDeductionAmout() {
        return scoreDeductionAmout;
    }

    public void setScoreDeductionAmout(BigDecimal scoreDeductionAmout) {
        this.scoreDeductionAmout = scoreDeductionAmout;
    }

	public String getThirdId() {
		return thirdId;
	}

	public void setThirdId(String thirdId) {
		this.thirdId = thirdId;
	}

	public String getOrderStore() {
		return orderStore;
	}

	public void setOrderStore(String orderStore) {
		this.orderStore = orderStore;
	}

	public Integer getCompanyAccount() {
		return companyAccount;
	}

	public void setCompanyAccount(Integer companyAccount) {
		this.companyAccount = companyAccount;
	}

	public List<OrderGoods> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<OrderGoods> goodsList) {
		this.goodsList = goodsList;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public Integer getCustomerServiceId() {
		return customerServiceId;
	}

	public void setCustomerServiceId(Integer customerServiceId) {
		this.customerServiceId = customerServiceId;
	}

	public Integer getCustomerManagerId() {
		return customerManagerId;
	}

	public void setCustomerManagerId(Integer customerManagerId) {
		this.customerManagerId = customerManagerId;
	}

	public BigDecimal getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}

	public Double getFullCutAmount() {
		return fullCutAmount;
	}

	public void setFullCutAmount(Double fullCutAmount) {
		this.fullCutAmount = fullCutAmount;
	}

	public BigDecimal getPayShouldAmount() {
		return payShouldAmount;
	}

	public void setPayShouldAmount(BigDecimal payShouldAmount) {
		this.payShouldAmount = payShouldAmount;
	}

	public Double getTail() {
		return tail;
	}

	public void setTail(Double tail) {
		this.tail = tail;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public String getChildId() {
		return childId;
	}

	public void setChildId(String childId) {
		this.childId = childId;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public Double getGroupOrderSale() {
		return groupOrderSale;
	}

	public void setGroupOrderSale(Double groupOrderSale) {
		this.groupOrderSale = groupOrderSale;
	}

	public BigDecimal getBalanceDeduction() {
		return balanceDeduction;
	}

	public void setBalanceDeduction(BigDecimal balanceDeduction) {
		this.balanceDeduction = balanceDeduction;
	}
	
	
	/**
	 * 创建新的商城订单
	 * @param dealerDeductionIds 
	 * @param map 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public OrderInfo addOrderInfoByMall(Map<String, Object> map, String dealerDeductionIds){
		dealerDeductionIds= dealerDeductionIds==null?"":dealerDeductionIds;
		Long orderId = PaymentIDProduce.getPaymentID();
		BigDecimal goodsAmount = toBigDecimal(map.get("orderTotalGoodsPrice"));
		BigDecimal freightAmount = toBigDecimal(map.get("orderTotalFreight"));
		BigDecimal fullCut = toBigDecimal(map.get("orderTotalFullcut"));
		BigDecimal orderAmount = goodsAmount.add(freightAmount);
		List<Map<String, Object>> shopList = (List<Map<String, Object>>) map.get("shops");
		BigDecimal totalCash = BigDecimal.ZERO;
		if(this.goodsList == null){
			this.goodsList = new ArrayList<>();
		}
		for (Map<String, Object> shop : shopList) {
			String sellerId = shop.get("sellerId").toString();
			boolean isUse = dealerDeductionIds.contains(sellerId);
			if(isUse){
				BigDecimal cash = toBigDecimal(map.get("totalCash"));
				totalCash = totalCash.add(cash);
			}
			List<FindShoppingCartByIdsMall> goodsList = (List<FindShoppingCartByIdsMall>) shop.get("goods");
			for (FindShoppingCartByIdsMall goods : goodsList) {
				OrderGoods orderGoods = new OrderGoods();
				orderGoods.setOrderId(orderId);
				orderGoods.setGoodsId(Long.parseLong(goods.getGoodsId()));
				orderGoods.setSellerGoodsId(goods.getSellerGoodsId());
				orderGoods.setNum(goods.getNum());
				orderGoods.setUnitPrice(goods.getPrice());
				orderGoods.setPrice(goods.getPrice());
				orderGoods.setSaleUnitPrice(goods.getSaleUnitPrice());
				orderGoods.setSalePrice(goods.getSaleUnitPrice().
						multiply(toBigDecimal(goods.getNum()))
						);
				this.goodsList.add(orderGoods);
			}
		}
		this.setId(orderId);
		this.setSellerId(0);
		this.setOrderAmount(orderAmount);
		this.setGoodsAmount(goodsAmount);
		this.setFreightAmount(freightAmount);
		this.setScoreDeductionAmout(totalCash);
		this.setFullCutAmount(fullCut.doubleValue());
		this.setHasChild("0");
		this.setOrderSource("5");//订单来源:其他
		this.setOrderStore("1");//订单店铺:商城
		this.setOrderChannel("2");//商城订单
		return this;
	}
	
	public BigDecimal toBigDecimal(Object o){
		if(o != null){
			return new BigDecimal(o.toString());
		}else{
			return BigDecimal.ZERO;
		}
	}
	
	public String toStringVal(Object o){
		if(o != null){
			return o.toString();
		}else{
			return "";
		}
	}
	public Integer toIntegerVal(Object o){
		if(o != null){
			try {
				return Integer.parseInt(o.toString());
			} catch (Exception e) {
				return null;
			}
		}else{
			return null;
		}
	}
	/**
	 * 创建新的订单发票
	 * @param invoiceMap
	 * @return
	 */
	public OrderInvoice addOrderInvoiceByMall(Map<String, Object> invoiceMap){
		OrderInvoice invoice = null;
		if(invoiceMap !=null && !invoiceMap.isEmpty()){
			invoice = new OrderInvoice();
			this.setIsNeedInvoice("1");
			invoice.setBuyerId(this.buyerId);
			invoice.setOrderId(this.id);
			invoice.setInvType(invoiceMap.get("invType").toString());
			invoice.setInvAmount( toPayAmount());
			invoice.setInvTitle(toStringVal(invoiceMap.get("invTitle")));
			invoice.setInvTitleType(toStringVal(invoiceMap.get("invTitleType")));
			invoice.setInvContent(toStringVal(invoiceMap.get("invContent")));
			invoice.setTaxpayeRno(toStringVal(invoiceMap.get("taxpayeRno")));
			invoice.setRegAddress(toStringVal(invoiceMap.get("regAddress")));
			invoice.setRegTelphone(toStringVal(invoiceMap.get("regTelphone")));
			invoice.setRegTelphoneSecret(toStringVal(invoiceMap.get("regTelphoneSecret")));
			invoice.setBank(toStringVal(invoiceMap.get("bank")));
			invoice.setBankAccount(toStringVal(invoiceMap.get("bankAccount")));
			invoice.setRecvName(toStringVal(invoiceMap.get("recvName")));
			invoice.setRecvMobile(toStringVal(invoiceMap.get("recvMobile")));
			invoice.setRecvMobileSecret(toStringVal(invoiceMap.get("recvMobileSecret")));
			invoice.setRecvProvince(toIntegerVal(invoiceMap.get("recvProvince")));
			invoice.setRecvCity(toIntegerVal(invoiceMap.get("recvCity")));
			invoice.setRecvArea(toIntegerVal(invoiceMap.get("recvArea")));
			invoice.setRecvAddress(toStringVal(invoiceMap.get("recvAddress")));
			invoice.setCreateTime(new Date());
		}else{
			this.setIsNeedInvoice("0");
		}
		return invoice;
	}

	
	public BigDecimal toPayAmount(){
		return this.getOrderAmount().subtract(this.scoreDeductionAmout)
					.subtract(new BigDecimal(this.fullCutAmount));
		
	}
	/**
	 * 整理订单商品
	 * @return
	 */
	public List<OrderGoods> addOrderGoodsByMall() {
		return this.getGoodsList();
	}

	public String getJdOrderId() {
		return jdOrderId;
	}

	public void setJdOrderId(String jdOrderId) {
		this.jdOrderId = jdOrderId;
	}

	public Integer getRateType() {
		return rateType;
	}

	public void setRateType(Integer rateType) {
		this.rateType = rateType;
	}
	
}