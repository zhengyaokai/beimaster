package bmatser.pageModel;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import bmatser.util.Constants;
//@ApiModel(value="商品详情",description="商品详情")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class GoodsDetail {
	@ApiModelProperty(value="商品ID")
	private Long gId;
	@ApiModelProperty(value="供应商商品ID")
	private Long saleGId;
	@ApiModelProperty(value="是否团")
    private int isJhs;
	@ApiModelProperty(value="是否有折扣活动（0：无 1：是）")
    private int isRate;
	@ApiModelProperty(value="是否特卖（0：否 1：是）")
    private int isxsms;
//	@ApiModelProperty(value="是否满送（1：满送）")
//    private Integer isFullgive;
//	@ApiModelProperty(value="是否参加工币")
//    private Integer isBean;
	@ApiModelProperty(value="商品型号")
    private String model;
	@ApiModelProperty(value="商品详情")
    private String detail;
	@ApiModelProperty(value="计量单位")
    private String measure;
	@ApiModelProperty(value="备注")
    private String remark;
	@ApiModelProperty(value="品牌名称")
    private String brandName;
	@ApiModelProperty(value="品牌ID")
    private String brandId;
	@ApiModelProperty(value="团购描述")
    private String grouponDesc;
	@ApiModelProperty(value="商品标题")
    private String title;
	@ApiModelProperty(value="批量（每次增加减少商品的数量）")
    private int batchNum;
	@ApiModelProperty(value="折扣率")
    private BigDecimal rate;
	@ApiModelProperty(value="库存")
    private Long stock;
	@ApiModelProperty(value="QQ")
    private String qq;
	@ApiModelProperty(value="商家ID")
    private Long dealerId;
	@ApiModelProperty(value="市场价")
    private BigDecimal marketPrice;
	@ApiModelProperty(value="别名")
    private String alias;
	@ApiModelProperty(value="产品图片路径")
    private String image;
	@ApiModelProperty(value="商品编号（内部编号）")
    private String goodsNo;
	@ApiModelProperty(value="起定量")
    private Integer leastNum;
//	@ApiModelProperty(value="是否参加满减活动（0：无 1：是）")
//    private Integer isFullcut;
	@ApiModelProperty(value="单价")
    private BigDecimal price;
	@ApiModelProperty(value="商家ID，关联商家表dealer")
    private Long sellerId;
	@ApiModelProperty(value="店铺名称")
    private String shopName;
	@ApiModelProperty(value="是否支持团购")
    private int isCanJhs;
	@ApiModelProperty(value="特卖价")
    private BigDecimal salePrice;
	@ApiModelProperty(value="起订量")
    private int orderCount;
//	@ApiModelProperty(value="是否返现（0：否 1：是）")
//    private int isRebates;
	@ApiModelProperty(value="电话")
    private String telephone;
	@ApiModelProperty(value="订货号,商家自定义")
    private String orderNum;

	private String series;
    private String images;
	@ApiModelProperty(value="销售授权书/进货发票")
    private String proxyCertificate;

    private String logo;
	@ApiModelProperty(value="属性")
    private List attrs;
//	@ApiModelProperty(value="活动list")
//    private List activityList;
//	@ApiModelProperty(value="广告list")
//    private List adverList;
//	@ApiModelProperty(value="热门商品")
//    private List hotGoods;
	@ApiModelProperty(value="团购开始时间")
    private Timestamp groupStartTime;
	@ApiModelProperty(value="团购结束时间")
    private Timestamp groupEndTime;

//    private Integer isSignUp;
	@ApiModelProperty(value="团购ID")
    private Long grouponId;
	@ApiModelProperty(value="京东商品参数")
    private String jdParam;
	@ApiModelProperty(value="商品活动描述")
    private String activityDesc;//商品活动描述
	@ApiModelProperty(value="店铺描述")
    private String note;//店铺描述

	private String channel;//渠道不显示在页面

//    private Integer bean;
    
    private Integer cateNo;//分类编号
    
//    private int isBigCustomer;//0:否；1：是
//    private int isCombination;//0:否；1：是(该商品是否存在组合套餐)
    
//    public int getIsCombination() {
//		return isCombination;
//	}
//	public void setIsCombination(int isCombination) {
//		this.isCombination = isCombination;
//	}
//	public int getIsBigCustomer() {
//		return isBigCustomer;
//	}
//	public void setIsBigCustomer(int isBigCustomer) {
//		this.isBigCustomer = isBigCustomer;
//	}
	public GoodsDetail() {
		this.attrs = new ArrayList();
//		this.activityList = new ArrayList();
//		this.adverList = new ArrayList();
//		this.setHotGoods(new ArrayList());
	}
	
	
	public Long getgId() {
		return gId;
	}
	public void setgId(Long gId) {
		this.gId = gId;
	}
	
	public Long getSaleGId() {
		return saleGId;
	}
	public void setSaleGId(Long saleGId) {
		this.saleGId = saleGId;
	}
	//	public Long getGoodsId() {
//		return goodsId;
//	}
//	public void setGoodsId(Long goodsId) {
//		this.goodsId = goodsId;
//	}
//	public Long getSellerGoodsId() {
//		return sellerGoodsId;
//	}
//	public void setSellerGoodsId(Long sellerGoodsId) {
//		this.sellerGoodsId = sellerGoodsId;
//	}
	
	public int getIsRate() {
		return isRate;
	}
	public int getIsJhs() {
		return isJhs;
	}
	public void setIsJhs(int isJhs) {
		this.isJhs = isJhs;
	}
	public void setIsRate(int isRate) {
		this.isRate = isRate;
	}
//	public int getIsSale() {
//		return isSale;
//	}
//	public void setIsSale(int isSale) {
//		this.isSale = isSale;
//	}
	
//	public int getIsFullgive() {
//		return isFullgive;
//	}
//	public void setIsFullgive(int isFullgive) {
//		this.isFullgive = isFullgive;
//	}
	
	
	public int getIsxsms() {
		return isxsms;
	}
	public int getIsCanJhs() {
		return isCanJhs;
	}


	public void setIsCanJhs(int isCanJhs) {
		this.isCanJhs = isCanJhs;
	}


	public void setIsxsms(int isxsms) {
		this.isxsms = isxsms;
	}
	
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getMeasure() {
		return measure;
	}
	public void setMeasure(String measure) {
		this.measure = measure;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getGrouponDesc() {
		return grouponDesc;
	}
	public void setGrouponDesc(String grouponDesc) {
		this.grouponDesc = grouponDesc;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	public int getBatchNum() {
		return batchNum;
	}


	public void setBatchNum(int batchNum) {
		this.batchNum = batchNum;
	}


	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public Long getStock() {
		return stock;
	}
	public void setStock(Long stock) {
		this.stock = stock;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public Long getDealerId() {
		return dealerId;
	}
	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}
	public BigDecimal getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public Integer getLeastNum() {
		return leastNum;
	}
	public void setLeastNum(Integer leastNum) {
		this.leastNum = leastNum;
	}

	//	public int getIsFullcut() {
//		return isFullcut;
//	}
//	public void setIsFullcut(int isFullcut) {
//		this.isFullcut = isFullcut;
//	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
//	public int getIsSupportGroupon() {
//		return isSupportGroupon;
//	}
//	public void setIsSupportGroupon(int isSupportGroupon) {
//		this.isSupportGroupon = isSupportGroupon;
//	}
	public BigDecimal getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}
	public int getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	//	public int getIsRebates() {
//		return isRebates;
//	}
//	public void setIsRebates(int isRebates) {
//		this.isRebates = isRebates;
//	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public List getAttrs() {
		return attrs;
	}
	public void setAttrs(List attrs) {
		this.attrs = attrs;
	}
//	public List getActivityList() {
//		return activityList;
//	}
//	public void setActivityList(List activityList) {
//		this.activityList = activityList;
//	}
	public void setImages(String images) {
		this.images = images;
	}
	public String getProxyCertificate() {
		return proxyCertificate;
	}
	public void setProxyCertificate(String proxyCertificate) {
		this.proxyCertificate = proxyCertificate;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
//	public List getAdverList() {
//		return adverList;
//	}
//	public void setAdverList(List adverList) {
//		this.adverList = adverList;
//	}
//	public List getHotGoods() {
//		return hotGoods;
//	}
//	public void setHotGoods(List hotGoods) {
//		this.hotGoods = hotGoods;
//	}

	public Timestamp getGroupStartTime() {
		return groupStartTime;
	}
	public void setGroupStartTime(Timestamp groupStartTime) {
		this.groupStartTime = groupStartTime;
	}
	public Timestamp getGroupEndTime() {
		return groupEndTime;
	}
	public void setGroupEndTime(Timestamp groupEndTime) {
		this.groupEndTime = groupEndTime;
	}
//	public int getIsSignUp() {
//		return isSignUp;
//	}
//	public void setIsSignUp(int isSignUp) {
//		this.isSignUp = isSignUp;
//	}
	public Long getGrouponId() {
		return grouponId;
	}
	public void setGrouponId(Long grouponId) {
		this.grouponId = grouponId;
	}
	
	public String getActivityDesc() {
		return activityDesc;
	}
	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * 处理多个图片
	 * @return
	 */
	public List getImages() {
		List imageList = new ArrayList();
		if(null!=this.images){
			String[] imagesStr = this.images.split(",");
			for(int i=0;i<imagesStr.length;i++){
				if(null != imagesStr[i] && !"".equals(imagesStr[i]) && !"0.jpg".equals(imagesStr[i]) && imagesStr[i].length()>6){
					String image = "";
					if(this.sellerId!=null &&this.sellerId==9407609){
						
						image = "http://img10.360buyimg.com/imgzone/" + imagesStr[i];
					}else{
						image = Constants.IMAGE_URL + "goodsImg/" + imagesStr[i];
					}
					imageList.add(image);
				}
			}
		}
		return imageList;
	}

	
	
	/**
	 * APP端新增团购的明细
	 * @param param
	 */
	public void toGroupInfoByApp(Map param){
		if(param != null){
			if(param.get("groupStartTime")!= null){
				this.groupStartTime = (Timestamp) param.get("groupStartTime");
			}
	 		if(param.get("groupEndTime") != null){
				this.groupEndTime = (Timestamp) param.get("groupEndTime");
			}
//			if(param.get("isSignUp") != null){
//				this.isSignUp = new BigDecimal(param.get("isSignUp").toString()).intValue();
//			}
			if(param.get("grouponId") != null){
				this.grouponId = new BigDecimal(param.get("grouponId").toString()).longValue();
			}
		}		
	}
	public String getJdParam() {
		return jdParam;
	}
	public void setJdParam(String jdParam) {
		this.jdParam = jdParam;
	}
//	public int getBean() {
//		return bean;
//	}
//	public void setBean(int bean) {
//		this.bean = bean;
//	}
//	public int getIsBean() {
//		return isBean;
//	}
//	public void setIsBean(int isBean) {
//		this.isBean = isBean;
//	}
	public String showChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public Integer getCateNo() {
		return cateNo;
	}
	public void setCateNo(Integer cateNo) {
		this.cateNo = cateNo;
	}


}
