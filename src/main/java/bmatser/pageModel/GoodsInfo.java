package bmatser.pageModel;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.solr.client.solrj.beans.Field;

import bmatser.util.Constants;

/**
 * 商品信息
 * @author felx
 * @version 1.0
 *
 */
public class GoodsInfo implements Serializable{

//	private String sellerGoodsId;
	/** 供应商商品ID */
	@Field
	private String id;
	
	/** 商品ID */
	@Field
	private String goodsId;
	
	/** 商品标题  */
	@Field
	private String title;
	
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
//	@Field
//	private String categoryId;
	
	/** 订货号 */
	@Field
	private String orderNum;
	
	@Field
	private String series;
	
	/** 供应商定义的商品名称 */
	@Field
	private String goodsName;
	
	/** 供应商定义的商品价格 */
	@Field
	private String price;
	
	
	
	/** 商品特卖价格 */
	@Field
	private Double salePrice;
	@Field
	private Double marketPrice;
	
	/** 计量单位 */
	@Field
	private String measure;
	
	/** 库存 */
	@Field
	private int stock;
	/** 库存 */
//	@Field
//	private Integer salesVolume;
//	@Field
//	private Integer sellerId;
//	
//	/** 供应商ID */
//	@Field
//	private Integer dealerId;
	
//	/** 店铺名称  */
//	@Field
//	private String companyName;
	
//	/** 供应商简称  */
//	@Field
//	private String shortName;
	
	/** 发货地区ID */
	@Field
	private Integer cityId;
	
	/** 发货地区名称 */
	@Field
	private String city;
	
	/** 品牌中文名称 */
//	@Field
//	private String brandNameCn;
//	/** 品牌英文名称 */
//	@Field
//	private String brandNameEn;
	/** 是否返现 */
//	@Field
//	private Integer isRate;
	
	/** 是否特卖 */
	@Field
	private Integer isSale;
	/** 是否满减 */
//	@Field
//	private Integer isFullcut;
	/** 是否返现 */
//	@Field
//	private Integer isRebates;
//	@Field
//	private Integer isFullgive;
	@Field
	private Integer isJhs;
	
	
	
//	/** 店铺别名 */
//	@Field
//	private String alias;
	/** 现金券抵扣 */
	@Field
	private Double rate;
	
	private Double ratePrice;
//	private String fullAmount;
//	private String cutExt;
//	private String groupDesc;
//	private String groupId;
	@Field
	private Integer orderQuantity;
	@Field
	private Integer batchQuantity;
	@Field
	private int canCoin;
	@Field
	private Double beanRate;
	private int isCanJhs;
	
	
	public int getCanCoin() {
		return canCoin;
	}
	public void setCanCoin(int canCoin) {
		this.canCoin = canCoin;
	}
	//	public Integer getIsRate() {
//		return isRate;
//	}
//	public void setIsRate(Integer isRate) {
//		this.isRate = isRate;
//	}
	public Integer getIsSale() {
		return isSale;
	}
	public void setIsSale(Integer isSale) {
		this.isSale = isSale;
	}
//	public Integer getIsFullcut() {
//		return isFullcut;
//	}
//	public void setIsFullcut(Integer isFullcut) {
//		this.isFullcut = isFullcut;
//	}
//	public Integer getIsRebates() {
//		return isRebates;
//	}
//	public void setIsRebates(Integer isRebates) {
//		this.isRebates = isRebates;
//	}
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
//	public String getCategoryId() {
//		return categoryId;
//	}
//	public void setCategoryId(String categoryId) {
//		this.categoryId = categoryId;
//	}
	
	
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
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
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
//	public String getBrandNameCn() {
//		return brandNameCn;
//	}
//	public void setBrandNameCn(String brandNameCn) {
//		this.brandNameCn = brandNameCn;
//	}
//	public String getBrandNameEn() {
//		return brandNameEn;
//	}
//	public void setBrandNameEn(String brandNameEn) {
//		this.brandNameEn = brandNameEn;
//	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
//	public String getCompanyName() {
//		return companyName;
//	}
//	public void setCompanyName(String companyName) {
//		this.companyName = companyName;
//	}
//	public String getShortName() {
//		return shortName;
//	}
//	public void setShortName(String shortName) {
//		this.shortName = shortName;
//	}
//	public Integer getDealerId() {
//		return dealerId;
//	}
//	public void setDealerId(Integer dealerId) {
//		this.dealerId = dealerId;
//	}
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
//	public String getAlias() {
//		return alias;
//	}
//	public void setAlias(String alias) {
//		this.alias = alias;
//	}
	public Double getRatePrice() {
		return ratePrice;
	}
	public void setRatePrice(Double ratePrice) {
		this.ratePrice = ratePrice;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Double getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(Double marketPrice) {
		this.marketPrice = marketPrice;
	}
//	public String getSellerGoodsId() {
//		return sellerGoodsId;
//	}
//	public void setSellerGoodsId(String sellerGoodsId) {
//		this.sellerGoodsId = sellerGoodsId;
//	}
//	public String getFullAmount() {
//		return fullAmount;
//	}
//	public void setFullAmount(String fullAmount) {
//		this.fullAmount = fullAmount;
//	}
//	public String getCutExt() {
//		return cutExt;
//	}
//	public void setCutExt(String cutExt) {
//		this.cutExt = cutExt;
//	}
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
//	public Integer getIsFullgive() {
//		return isFullgive;
//	}
//	public void setIsFullgive(Integer isFullgive) {
//		this.isFullgive = isFullgive;
//	}
	
	
//	public String getGroupDesc() {
//		return groupDesc;
//	}
//	public void setGroupDesc(String groupDesc) {
//		this.groupDesc = groupDesc;
//	}
	
	public Integer getIsJhs() {
		return isJhs;
	}
	public void setIsJhs(Integer isJhs) {
		this.isJhs = isJhs;
	}
	
//	public String getGroupId() {
//		return groupId;
//	}
//	public void setGroupId(String groupId) {
//		this.groupId = groupId;
//	}

	
	public int getIsCanJhs() {
		return isCanJhs;
	}
	public void setIsCanJhs(int isCanJhs) {
		this.isCanJhs = isCanJhs;
	}
	//	public Integer getSalesVolume() {
//		return salesVolume;
//	}
//	public void setSalesVolume(Integer salesVolume) {
//		this.salesVolume = salesVolume;
//	}
	public void initSet() {
		this.setImage(Constants.IMAGE_URL+"goodsImg/"+this.getImage());
//		this.setIsRate((this.getIsRate()!=null && this.getIsSale() != 1)?this.getIsRate() : 0);
		this.setIsSale(this.getIsSale()!=null?this.getIsSale() : 0);
//		this.setIsFullcut(this.getIsFullcut()!=null?this.getIsFullcut() : 0);
//		this.setIsRebates(this.getIsRebates()!=null?this.getIsRebates() : 0);
//		if(this.getIsRate() == 1 && this.getIsSale() != 1){
//			if(this.getRate()!=null){
//				double  ratePrice = this.getSalePrice()*this.getRate();
//				this.setRatePrice(Math.floor(ratePrice));
//			}
//		}
	}
//	public Integer getSellerId() {
//		return sellerId;
//	}
//	public void setSellerId(Integer sellerId) {
//		this.sellerId = sellerId;
//	}
//	public int getIsBean() {
//		if((this.isGroupon !=null &&this.isGroupon ==1) || (this.isFullcut !=null &&this.isFullcut ==1)){
//			return 0;
//		}
//		return isBean;
//	}
//	public void setIsBean(int isBean) {
//		this.isBean = isBean;
//	}


}
