package bmatser.pageModel;

import java.io.Serializable;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import bmatser.model.OrderInfo;
@ApiModel(value="订单")
public class OrderInfoPage extends OrderInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6328042394472741968L;
	@ApiModelProperty(value="订单ID")
	private Long id;
	@ApiModelProperty(value="地址ID")
	private Integer consignAddressId;
	@ApiModelProperty(value="发票ID")	
	private Integer invoiceId;
	@ApiModelProperty(value="购物车ID")
	private String cartIds;
	@ApiModelProperty(value="物流ID")	
	private String logisticsId;
	@ApiModelProperty(value="发货时间")	
	private String logisticsTime;
	@ApiModelProperty(value="物流编号")	
	private String logisticsNo;
	@ApiModelProperty(value="使用现金券集合")	
	private String dealerDeductionIds;
	@ApiModelProperty(value="是否使用现金券 1 是 0 否")	
	private String isCash;
	@ApiModelProperty(value="是否使用工币 1 是 0 否")	
	private int isBean;
	
	
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

	public Double getInvAmount() {
		return invAmount;
	}

	public void setInvAmount(Double invAmount) {
		this.invAmount = invAmount;
	}

	private Double invAmount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getDealerDeductionIds() {
		return dealerDeductionIds;
	}

	public void setDealerDeductionIds(String dealerDeductionIds) {
		this.dealerDeductionIds = dealerDeductionIds;
	}

	public String getIsCash() {
		return isCash;
	}

	public void setIsCash(String isCash) {
		this.isCash = isCash;
	}

	public int getIsBean() {
		return isBean;
	}

	public void setIsBean(int isBean) {
		this.isBean = isBean;
	}

}
