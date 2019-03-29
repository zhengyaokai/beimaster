package bmatser.pageModel;

import java.math.BigDecimal;

public class ShoppingCartShowPage {
	
	private String id;//购物车ID
	private String goodsId;
	private String goodsNo;
	private String sellerGoodsId;
	private String goodsName;
	private String image;
	private int num;
	private BigDecimal salePrice;//为实际售价
	private BigDecimal cartPrice;// 购物车 price
	private BigDecimal price;//seller_goods price
	private String brandId;
	private String model;
	private BigDecimal marketPrice;
	private String brandName;
	private String skuUnit;
	private BigDecimal rate;
	private BigDecimal orderAmount;
	private BigDecimal freightAmount;
	private String buyNo;
	private int isSale;
	private int isRate;
	private int activityType;
	private String activityId;
	private BigDecimal costPrice;//原售价
	private BigDecimal saleUnitPrice;//原售价
	private int cash;
	private BigDecimal payPrice;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getSellerGoodsId() {
		return sellerGoodsId;
	}
	public void setSellerGoodsId(String sellerGoodsId) {
		this.sellerGoodsId = sellerGoodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public BigDecimal getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}
	public BigDecimal getCartPrice() {
		return cartPrice;
	}
	public void setCartPrice(BigDecimal cartPrice) {
		this.cartPrice = cartPrice;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public BigDecimal getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getSkuUnit() {
		return skuUnit;
	}
	public void setSkuUnit(String skuUnit) {
		this.skuUnit = skuUnit;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
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
	public String getBuyNo() {
		return buyNo;
	}
	public void setBuyNo(String buyNo) {
		this.buyNo = buyNo;
	}
	public int getIsSale() {
		return isSale;
	}
	public void setIsSale(int isSale) {
		this.isSale = isSale;
	}
	public int getIsRate() {
		return isRate;
	}
	public void setIsRate(int isRate) {
		this.isRate = isRate;
	}
	public int getActivityType() {
		return activityType;
	}
	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public BigDecimal getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}
	
	/**
	 * 商品总价(不算现金券)
	 * @return
	 */
	public BigDecimal getGoodsPrice(){
		return this.salePrice.multiply(new BigDecimal(this.num));
	}
	/**
	 * 商品总现金券
	 * @return
	 */
	public int getGoodsCash(){
		if(this.isRate==1){
			return this.salePrice.multiply(new BigDecimal(this.num)).multiply(this.rate).intValue();
		}
		return 0;
	}
	/**
	 * 商品单价(加上现金券)
	 * @param scale
	 * @return
	 */
	public BigDecimal getSingleGoods(Integer scale){
		if(scale==null){
			scale = 2;
		}
		if(this.isRate==1){
			int cash = this.salePrice.multiply(new BigDecimal(this.num)).multiply(this.rate).intValue();
			BigDecimal saleUnitPrice= this.salePrice.subtract((new BigDecimal(cash).divide(new BigDecimal(this.num),scale,BigDecimal.ROUND_HALF_UP)));
			return saleUnitPrice;
		}
		return this.salePrice;
	}
	/**
	 * 获得活动优惠(排除现金券)
	 * @return
	 */
	public BigDecimal getGoodsGroupSale(){
		if(this.activityType==1){
			return this.costPrice.abs().subtract(this.salePrice).multiply(new BigDecimal(this.num));
		}
		return BigDecimal.ZERO;
	}
	public BigDecimal getSaleUnitPrice() {
		return saleUnitPrice;
	}
	public void setSaleUnitPrice(BigDecimal saleUnitPrice) {
		this.saleUnitPrice = saleUnitPrice;
	}
	public int getCash() {
		return cash;
	}
	public void setCash(int cash) {
		this.cash = cash;
	}
	public BigDecimal getPayPrice() {
		return payPrice;
	}
	public void setPayPrice(BigDecimal payPrice) {
		this.payPrice = payPrice;
	}
}
