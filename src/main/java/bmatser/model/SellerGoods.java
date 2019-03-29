package bmatser.model;

import java.math.BigDecimal;
import java.util.Date;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
@ApiModel(value="商品",description="商品")
public class SellerGoods {
    private Long id;
    @ApiModelProperty(value="商品ID，关联商品表goods")
    private Long goodsId;

    private String goodsNo;
    @ApiModelProperty(value="商家ID，关联商家表dealer")
    private Integer sellerId;
    @ApiModelProperty(value="订货号")
    private String buyNo;
    @ApiModelProperty(value="库存")
    private Integer stock;
    @ApiModelProperty(value="单价")
    private BigDecimal price;
    @ApiModelProperty(value="进货价")
    private BigDecimal costPrice;
    @ApiModelProperty(value="商品名称")
    private String goodsName;
    @ApiModelProperty(value="发货城市ID")
    private Integer shippingCityId;
    @ApiModelProperty(value="发货地")
    private String shippingCityName;
    @ApiModelProperty(value="是否包邮")
    private String isFreepost;
    @ApiModelProperty(value="物流模板ID")
    private Integer fareTempId;
    @ApiModelProperty(value="定时上架时间")
    private Date fixedUpTime;
    @ApiModelProperty(value="定时下架时间")
    private Date fixedDownTime;
    @ApiModelProperty(value="状态（1：已上架 2：未上架 3：删除）")
    private String status;
    @ApiModelProperty(value="添加时间")
    private Date createTime;
    @ApiModelProperty(value="添加人")
    private Integer createUserId;
    @ApiModelProperty(value="最后修改时间")
    private Date modifyTime;
    @ApiModelProperty(value="修改人")
    private Integer modifyUserId;
    @ApiModelProperty(value="销量")
    private Integer salesVolume;
    @ApiModelProperty(value="优惠券金额")
    private Integer couponAmount;
    @ApiModelProperty(value="显示金额")
    private BigDecimal showAmount;
    @ApiModelProperty(value="是否推荐（0：否 1：是），如果设为不推荐，同时将排序字段设置为0")
    private String isRecomm;
    @ApiModelProperty(value="排序，数字越大排列越靠前")
    private Integer orderSn;
    @ApiModelProperty(value="是否显示（0：否 1：是）")
    private String isShow;
    @ApiModelProperty(value="单位")
    private String skUnit;
    @ApiModelProperty(value="备注")
    private String remark;
    @ApiModelProperty(value="起订量")
    private Integer orderQuantity;
    @ApiModelProperty(value="是否参加满减活动（0：无  1：是）")
    private int isFullcut;
    @ApiModelProperty(value="是否有折扣活动（0：无  1：是）")
    private int isRate;
    @ApiModelProperty(value="是否返现（0：否 1：是）")
    private int isRebates;
    @ApiModelProperty(value="是否特卖（0：否  1：是）")
    private int isSale;
    @ApiModelProperty(value="是否满送（1：满送）")
    private int isFullgive;
    @ApiModelProperty(value="同步码")
    private String synCode;
    @ApiModelProperty(value="批量（每次增加减少商品的数量）")
    private Integer batchQuantity;

    private Integer addRange;
    @ApiModelProperty(value="渠道（1：saas  2：商城）")
    private Integer channel;
    @ApiModelProperty(value="店铺分类ID，关联店铺分类表delaer_category")
    private Long sellerCateId;
    @ApiModelProperty(value="折扣率")
    private BigDecimal rate;
    @ApiModelProperty(value="是否团")
    private int isGroupon;
    @ApiModelProperty(value="是否参加工币")
    private int isBean;
    @ApiModelProperty(value="工币比例")
    private BigDecimal beanRate;

    @ApiModelProperty(value="特卖价")    
    private BigDecimal salePrice;
    @ApiModelProperty(value="商品型号")
    private String model;  
    @ApiModelProperty(value="计量单位")
    private String measure;  
    public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getMeasure() {
		return measure;
	}

	public void setMeasure(String measure) {
		this.measure = measure;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo == null ? null : goodsNo.trim();
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getBuyNo() {
        return buyNo;
    }

    public void setBuyNo(String buyNo) {
        this.buyNo = buyNo == null ? null : buyNo.trim();
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public Integer getShippingCityId() {
        return shippingCityId;
    }

    public void setShippingCityId(Integer shippingCityId) {
        this.shippingCityId = shippingCityId;
    }

    public String getShippingCityName() {
        return shippingCityName;
    }

    public void setShippingCityName(String shippingCityName) {
        this.shippingCityName = shippingCityName == null ? null : shippingCityName.trim();
    }

    public String getIsFreepost() {
        return isFreepost;
    }

    public void setIsFreepost(String isFreepost) {
        this.isFreepost = isFreepost == null ? null : isFreepost.trim();
    }

    public Integer getFareTempId() {
        return fareTempId;
    }

    public void setFareTempId(Integer fareTempId) {
        this.fareTempId = fareTempId;
    }

    public Date getFixedUpTime() {
        return fixedUpTime;
    }

    public void setFixedUpTime(Date fixedUpTime) {
        this.fixedUpTime = fixedUpTime;
    }

    public Date getFixedDownTime() {
        return fixedDownTime;
    }

    public void setFixedDownTime(Date fixedDownTime) {
        this.fixedDownTime = fixedDownTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(Integer modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public Integer getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(Integer salesVolume) {
        this.salesVolume = salesVolume;
    }

    public Integer getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(Integer couponAmount) {
        this.couponAmount = couponAmount;
    }

    public BigDecimal getShowAmount() {
        return showAmount;
    }

    public void setShowAmount(BigDecimal showAmount) {
        this.showAmount = showAmount;
    }

    public String getIsRecomm() {
        return isRecomm;
    }

    public void setIsRecomm(String isRecomm) {
        this.isRecomm = isRecomm == null ? null : isRecomm.trim();
    }

    public Integer getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(Integer orderSn) {
        this.orderSn = orderSn;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow == null ? null : isShow.trim();
    }

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public String getSkUnit() {
		return skUnit;
	}

	public void setSkUnit(String skUnit) {
		this.skUnit = skUnit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public int getIsFullcut() {
		return isFullcut;
	}

	public void setIsFullcut(int isFullcut) {
		this.isFullcut = isFullcut;
	}

	public int getIsRate() {
		return isRate;
	}

	public void setIsRate(int isRate) {
		this.isRate = isRate;
	}

	public int getIsRebates() {
		return isRebates;
	}

	public void setIsRebates(int isRebates) {
		this.isRebates = isRebates;
	}

	public int getIsSale() {
		return isSale;
	}

	public void setIsSale(int isSale) {
		this.isSale = isSale;
	}

	public int getIsFullgive() {
		return isFullgive;
	}

	public void setIsFullgive(int isFullgive) {
		this.isFullgive = isFullgive;
	}

	public String getSynCode() {
		return synCode;
	}

	public void setSynCode(String synCode) {
		this.synCode = synCode;
	}

	public Integer getBatchQuantity() {
		return batchQuantity;
	}

	public void setBatchQuantity(Integer batchQuantity) {
		this.batchQuantity = batchQuantity;
	}

	public Integer getAddRange() {
		return addRange;
	}

	public void setAddRange(Integer addRange) {
		this.addRange = addRange;
	}

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	public Long getSellerCateId() {
		return sellerCateId;
	}

	public void setSellerCateId(Long sellerCateId) {
		this.sellerCateId = sellerCateId;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public int getIsGroupon() {
		return isGroupon;
	}

	public void setIsGroupon(int isGroupon) {
		this.isGroupon = isGroupon;
	}

	public int getIsBean() {
		return isBean;
	}

	public void setIsBean(int isBean) {
		this.isBean = isBean;
	}

	public BigDecimal getBeanRate() {
		return beanRate;
	}

	public void setBeanRate(BigDecimal beanRate) {
		this.beanRate = beanRate;
	}
	
	
}