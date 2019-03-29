package bmatser.model;

import java.math.BigDecimal;

public class FreightAmountInfo {
	
	private String fareTempId;//运费模板
	private BigDecimal amount;//模板总金额
	private BigDecimal orderAmount;//满金额
	private BigDecimal freightAmount;//不满运费
	private int billRule;//模板类型
	private BigDecimal charge;//首重收费
	private BigDecimal firstItem;//首件（量、体积）
	private BigDecimal renewItem;//续件（重、体积）
	private BigDecimal renewCharge;//续费（元）
	private BigDecimal weight;//模板总重量
	private BigDecimal cubage;//模板总体积
	private String provinceId;//省id
	
	
	public String getFareTempId() {
		return fareTempId;
	}
	public void setFareTempId(String fareTempId) {
		this.fareTempId = fareTempId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}
	public BigDecimal getFreightAmount() {
		return freightAmount;
	}
	public void setFreightAmount(BigDecimal freightAmount) {
		this.freightAmount = freightAmount;
	}
	public int getBillRule() {
		return billRule;
	}
	public void setBillRule(int billRule) {
		this.billRule = billRule;
	}
	public BigDecimal getFirstItem() {
		return firstItem;
	}
	public void setFirstItem(BigDecimal firstItem) {
		this.firstItem = firstItem;
	}
	public BigDecimal getRenewItem() {
		return renewItem;
	}
	public void setRenewItem(BigDecimal renewItem) {
		this.renewItem = renewItem;
	}
	public BigDecimal getRenewCharge() {
		return renewCharge;
	}
	public void setRenewCharge(BigDecimal renewCharge) {
		this.renewCharge = renewCharge;
	}
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public BigDecimal getCharge() {
		return charge;
	}
	public void setCharge(BigDecimal charge) {
		this.charge = charge;
	}
	public BigDecimal getCubage() {
		return cubage;
	}
	public void setCubage(BigDecimal cubage) {
		this.cubage = cubage;
	}
	/**
	 * 计算模板最终运费
	 *	@author jxw
	 *	@Date 2016-12-14
	 *	@description 
	 * @return
	 */
	public BigDecimal toCountFreight() {
		//判断金额是否大于满金额
		if(this.orderAmount!=null&&this.amount.compareTo(this.orderAmount)>=0){
			return BigDecimal.ZERO;
		}else{
			//判断运费模板类型
			if(this.billRule==3){
				//取最大重量
				BigDecimal cubageWeight = this.cubage.divide(new BigDecimal(6000),2,BigDecimal.ROUND_HALF_UP);
				BigDecimal goodsWeight = this.weight.compareTo(cubageWeight)>0?this.weight:cubageWeight;
				//判断重量是否小于首重
				if(goodsWeight.compareTo(this.firstItem)<=0){
					return this.charge;
				}else{
					//（重量-首重）/ 续重 * 续费 + 首费
					return goodsWeight.subtract(this.firstItem)
										.divide(this.renewItem,0,BigDecimal.ROUND_UP)
										.multiply(this.renewCharge)
										.add(this.charge);
				}
			}else{
				return this.freightAmount;
			}
		}
	}
	
	
	
	

}
