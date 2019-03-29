package bmatser.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

public class FindShoppingCartByType {
	
    private int isSale;
    private Long goodsId;
    private int isRate;
    private String costPrice;
    private String model;
    private BigDecimal beanRate;
    private String brandName;
    private Long id;
    private String activityId;
    private int batchQuantity;
    private BigDecimal rate;
    private BigDecimal marketPrice;
    private int bean;
    private Long sellerGoodsId;
    private String image;
    private String dealerName;
    private String groupPrice;
    private String goodsNo;
    private BigDecimal cartPrice;
    private int num;
    private String price;
    private int isBean;
    private BigDecimal groupSale;
    private String skuUnit;
    private String salePrice;
    private int activityType;
    private int orderQuantity;
    private String goodsName;
    private String buyNo;
    private Boolean selected;
	public int getIsSale() {
		return isSale;
	}
	public void setIsSale(int isSale) {
		this.isSale = isSale;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public int getIsRate() {
		return isRate;
	}
	public void setIsRate(int isRate) {
		this.isRate = isRate;
	}
	public String getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(String costPrice) {
		this.costPrice = costPrice;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public BigDecimal getBeanRate() {
		return beanRate;
	}
	public void setBeanRate(BigDecimal beanRate) {
		this.beanRate = beanRate;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public int getBatchQuantity() {
		return batchQuantity;
	}
	public void setBatchQuantity(int batchQuantity) {
		this.batchQuantity = batchQuantity;
	}
	public BigDecimal getRate() {
		if(isRate==1){
			return rate.multiply(new BigDecimal(this.salePrice));
		}else{
			return BigDecimal.ZERO;
		}
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public BigDecimal getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}
	public int getBean() {
		if(1==this.isBean){
			return beanRate.multiply(new BigDecimal(price))
							.multiply(new BigDecimal(100))
							.setScale(0,BigDecimal.ROUND_HALF_UP)
							.intValue();
		}else{
			return 0;
		}
	}
	public void setBean(int bean) {
		this.bean = bean;
	}
	public Long getSellerGoodsId() {
		return sellerGoodsId;
	}
	public void setSellerGoodsId(Long sellerGoodsId) {
		this.sellerGoodsId = sellerGoodsId;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	public String getGroupPrice() {
		return groupPrice;
	}
	public void setGroupPrice(String groupPrice) {
		this.groupPrice = groupPrice;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public BigDecimal getCartPrice() {
		return cartPrice;
	}
	public void setCartPrice(BigDecimal cartPrice) {
		this.cartPrice = cartPrice;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public int getIsBean() {
		return isBean;
	}
	public void setIsBean(int isBean) {
		this.isBean = isBean;
	}
	public BigDecimal getGroupSale() {
		return groupSale;
	}
	public void setGroupSale(BigDecimal groupSale) {
		this.groupSale = groupSale;
	}
	public String getSkuUnit() {
		return skuUnit;
	}
	public void setSkuUnit(String skuUnit) {
		this.skuUnit = skuUnit;
	}
	public String getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}
	public int getActivityType() {
		return activityType;
	}
	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}
	public int getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(int orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getBuyNo() {
		return buyNo;
	}
	public void setBuyNo(String buyNo) {
		this.buyNo = buyNo;
	}
	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
    
    

}
