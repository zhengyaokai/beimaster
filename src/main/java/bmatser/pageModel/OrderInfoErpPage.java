package bmatser.pageModel;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="E助订单",description="E助订单信息")
public class OrderInfoErpPage {
	private String id;
	@ApiModelProperty(value="商家ID")
	private Integer buyerId;
	@ApiModelProperty(value="供应商ID")
	private Integer sellerId;
	@ApiModelProperty(value="收货地址，关联收货地址表")
	private Integer consignAddressId;
	@ApiModelProperty(value="发票ID")
	private Integer invoiceId;

	private String cartIds;
	@ApiModelProperty(value="物流公司ID")
	private String logisticsId;

	private String logisticsTime;

	private String logisticsNo;
	@ApiModelProperty(value="订单状态【1:待付款 2：待发货 3：已发货（待收货） 4：已收货（待结算） 5：对账中（已结算） 6：已完成 7：退货中 8：已取消 9:待确认付款】")
	private String orderStatus;
	@ApiModelProperty(value="供应商备注")
	private String sellerRemark;
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
	public Integer getSellerId() {
		return sellerId;
	}
	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}
	public Integer getConsignAddressId() {
		return consignAddressId;
	}
	public void setConsignAddressId(Integer consignAddressId) {
		this.consignAddressId = consignAddressId;
	}
	public Integer getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}
	public String getCartIds() {
		return cartIds;
	}
	public void setCartIds(String cartIds) {
		this.cartIds = cartIds;
	}
	public String getLogisticsId() {
		return logisticsId;
	}
	public void setLogisticsId(String logisticsId) {
		this.logisticsId = logisticsId;
	}
	public String getLogisticsTime() {
		return logisticsTime;
	}
	public void setLogisticsTime(String logisticsTime) {
		this.logisticsTime = logisticsTime;
	}
	public String getLogisticsNo() {
		return logisticsNo;
	}
	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getSellerRemark() {
		return sellerRemark;
	}
	public void setSellerRemark(String sellerRemark) {
		this.sellerRemark = sellerRemark;
	}
	
	
}
