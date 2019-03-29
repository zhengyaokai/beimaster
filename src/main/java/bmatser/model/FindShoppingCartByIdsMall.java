package bmatser.model;

import java.math.BigDecimal;

public class FindShoppingCartByIdsMall {
	
	private String goodsId;
	private String goodsNo;
	private Long sellerGoodsId;
	private String goodsName;
	private String image;
	private int num;
	private BigDecimal salePrice;
	private BigDecimal cartPrice;
	private BigDecimal price;
	private Integer brandId;
	private String skuUnit;
	private int isRate;
	private int isSale;
	private BigDecimal rate;
	private String cartId;
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
	public String getSkuUnit() {
		return skuUnit;
	}
	public void setSkuUnit(String skuUnit) {
		this.skuUnit = skuUnit;
	}
	public int getIsRate() {
		return isRate;
	}
	public void setIsRate(int isRate) {
		this.isRate = isRate;
	}
	public int getIsSale() {
		return isSale;
	}
	public void setIsSale(int isSale) {
		this.isSale = isSale;
	}
	public String getCartId() {
		return cartId;
	}
	public void setCartId(String cartId) {
		this.cartId = cartId;
	}
	public Long getSellerGoodsId() {
		return sellerGoodsId;
	}
	public void setSellerGoodsId(Long sellerGoodsId) {
		this.sellerGoodsId = sellerGoodsId;
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
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	
	public BigDecimal getPayPrice(){
		return this.getSalePrice().multiply(new BigDecimal(num));	
	}
	/**
	 * 获取抵扣后价格
	 * @return
	 */
	public BigDecimal getSaleUnitPrice(){
		if(isRateVal()){
			int goodsCash = countCash();
			BigDecimal eachGoodDeduction = new BigDecimal(goodsCash).divide(new BigDecimal(num),4,BigDecimal.ROUND_HALF_UP);
			return this.getSalePrice().subtract(eachGoodDeduction);
		}else{
			return this.getSalePrice();
		}
	}
	public int countCash(){
		if(isRateVal()){
			return (int) this.getPayPrice().multiply(this.rate).doubleValue();
		}else{
			return 0;
		}
	}
	/**
	 * 是否现金券
	 * @return
	 */
	public boolean isRateVal() {
		if(this.isRate ==1 && this.isSale ==0 && this.rate != null){
			return true;
		}else{
			return false;
		}
	}
	public Integer getBrandId() {
		return brandId;
	}
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}


}
