package bmatser.model;

import java.math.BigDecimal;

public class EditGoods {
	
	private Long id; 
	private BigDecimal salePrice;
	private int isSale;
	private int isRate;
	private BigDecimal rate;
	private Long goodsId;
	private int num;
	private String buyNo;
	private String model;
	private String brandName;
	private int batchQuantity;
	private int orderQuantity;
	private String image;
	private String title;
	private String packageId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public BigDecimal getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
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
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public int getNum() {
		return num;
	}
	
	
	public String getBuyNo() {
		return buyNo;
	}
	public void setBuyNo(String buyNo) {
		this.buyNo = buyNo;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public int getBatchQuantity() {
		return batchQuantity;
	}
	public void setBatchQuantity(int batchQuantity) {
		this.batchQuantity = batchQuantity;
	}
	public int getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(int orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	/**
	 * 获取计算的数量
	 * @return
	 */
	public BigDecimal getBNum() {
		return new BigDecimal(this.num);
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	
	
}
