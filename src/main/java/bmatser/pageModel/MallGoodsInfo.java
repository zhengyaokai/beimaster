package bmatser.pageModel;

import java.io.Serializable;
import java.util.List;

import org.apache.solr.client.solrj.beans.Field;

public class MallGoodsInfo implements Serializable{

	/**
	 * 
	 */
	private static long serialVersionUID = -6436703476811537827L;
	
	@Field
	private String id;
	
	/** 商品ID */
	@Field
	private String goodsId;
	
	
	/** 商品图片  */
	@Field
	private String image;
	
	/** 商品型号  */
	@Field
	private String model;
	
	/** 品牌ID */
	@Field
	private Integer brandId;
	
	/** 分类ID */
	@Field
	private String categoryId;
	
	/** 订货号 */
	@Field
	private String buyNo;
	
	/** 供应商定义的商品名称 */
	@Field
	private String goodsName;
	
	/** 供应商定义的商品价格 */
	@Field
	private String price;
	
	/** 商品特卖价格 */
	@Field
	private Double salePrice;
	
	/** 计量单位 */
	@Field
	private String measure;
	
	/** 库存 */
	@Field
	private Integer salesVolume;
	@Field
	private Integer sellerId;
	
	/** 供应商ID */
	@Field
	private Integer dealerId;
	
	/** 店铺名称  */
	@Field
	private String shopName;
	
	/** 是否特卖 */
	@Field
	private Integer isSale;
	/** 是否满减 */
	@Field
	private Integer isFullcut;
	/** 店铺别名 */
	@Field
	private String alias;
	@Field
	private Integer orderQuantity;
	@Field
	private Integer batchQuantity;
	@Field
	private List<Integer> attrId;
	@Field
	private List<Integer> attrValueId;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public static void setSerialversionuid(long serialversionuid) {
		serialVersionUID = serialversionuid;
	}
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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public Integer getBrandId() {
		return brandId;
	}
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getBuyNo() {
		return buyNo;
	}
	public void setBuyNo(String buyNo) {
		this.buyNo = buyNo;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public Double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}
	public String getMeasure() {
		return measure;
	}
	public void setMeasure(String measure) {
		this.measure = measure;
	}
	public Integer getSalesVolume() {
		return salesVolume;
	}
	public void setSalesVolume(Integer salesVolume) {
		this.salesVolume = salesVolume;
	}
	public Integer getSellerId() {
		return sellerId;
	}
	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}
	public Integer getDealerId() {
		return dealerId;
	}
	public void setDealerId(Integer dealerId) {
		this.dealerId = dealerId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Integer getIsSale() {
		return isSale;
	}
	public void setIsSale(Integer isSale) {
		this.isSale = isSale;
	}
	public Integer getIsFullcut() {
		return isFullcut;
	}
	public void setIsFullcut(Integer isFullcut) {
		this.isFullcut = isFullcut;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Integer getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	public Integer getBatchQuantity() {
		return batchQuantity;
	}
	public void setBatchQuantity(Integer batchQuantity) {
		this.batchQuantity = batchQuantity;
	}
	public List<Integer> getAttrId() {
		return attrId;
	}
	public void setAttrId(List<Integer> attrId) {
		this.attrId = attrId;
	}
	public List<Integer> getAttrValueId() {
		return attrValueId;
	}
	public void setAttrValueId(List<Integer> attrValueId) {
		this.attrValueId = attrValueId;
	}
	
	

}
