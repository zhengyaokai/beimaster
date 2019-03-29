package bmatser.model;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
@ApiModel(value="商家",description="商家")
public class Dealer {
    private Integer id;
    @ApiModelProperty(value="公司名称")
    private String dealerName;
    @ApiModelProperty(value="公司logo")
    private String logo;
    @ApiModelProperty(value="公司电话")
    private String telephone;
    @ApiModelProperty(value="传真")
    private String fax;
    @ApiModelProperty(value="公司所在国家")
    private Integer countryId;
    @ApiModelProperty(value="公司联系地址所在省份")
    private Integer provinceId;
    @ApiModelProperty(value="公司联系地址所在城市")
    private Integer cityId;
    @ApiModelProperty(value="公司联系地址所在辖区")
    private Integer areaId;
    @ApiModelProperty(value="公司联系详细地址")
    private String address;
    @ApiModelProperty(value="公司联系人姓名")
    private String linkMan;
    @ApiModelProperty(value="公司联系人电话")
    private String linkTel;
    @ApiModelProperty(value="在线QQ")
    private Long qq;
    @ApiModelProperty(value="经度")
    private String lng;
    @ApiModelProperty(value="纬度")
    private String lat;
    @ApiModelProperty(value="审核状态（0：待审核 1：审核通过 上架 2：审核不通过 3：已提交资料，未审核 下架）")
    private String checkStatus;
    @ApiModelProperty(value="审核时间")
    private Date checkTime;
    @ApiModelProperty(value="审核人")
    private Integer checkUserId;
    @ApiModelProperty(value="驳回理由")
    private String rejectReason;
    @ApiModelProperty(value="等级（1到5）")
    private Integer rank;
    @ApiModelProperty(value="等级积分,等级根据此积分计算")
    private Integer rankScore;
    @ApiModelProperty(value="消费积分，兑换礼品等，扣除此积分")
    private Integer score;
    @ApiModelProperty(value="现金券")
    private Integer cash;
    @ApiModelProperty(value="现金券有效期")
    private Date cashValidDate;
    @ApiModelProperty(value="商户类型（1：供应商 2：零售商 3:后台管理 4:商城会员 5:商城企业会员）")
    private String dealerType;
    @ApiModelProperty(value="授权日期，平台给商家的授权日期")
    private Date authorizeDate;
    @ApiModelProperty(value="状态（1：有效 2：删除）")
    private String status;
    @ApiModelProperty(value="最近修改时间")
    private Date modifyTime;
    @ApiModelProperty(value="修改人，关联用户注册表")
    private Integer modifyUserId;
    @ApiModelProperty(value="排序")
    private Integer orderSn;
    @ApiModelProperty(value=" 运费模板模式（ 1：店铺运费 2：单品运费） ")
    private String logisticsTempModel;
    @ApiModelProperty(value="添加时间")
    private Date createTime;
    @ApiModelProperty(value="电子邮箱")
    private String email;

    private String shortName;
    
    private Byte dealRate;
    @ApiModelProperty(value="是否显示")
    private String isShow;

    private String cooperationMode;

    private String orderHandle;

    private String profitModel;

    private Integer weixinId;
    @ApiModelProperty(value="CRM系统客户ID")
    private Integer crmCostomerId;
    @ApiModelProperty(value="经营范围")   
    private String operateScope;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName == null ? null : dealerName.trim();
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax == null ? null : fax.trim();
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }



    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel == null ? null : linkTel.trim();
    }

    public Long getQq() {
        return qq;
    }

    public void setQq(Long qq) {
        this.qq = qq;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng == null ? null : lng.trim();
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat == null ? null : lat.trim();
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus == null ? null : checkStatus.trim();
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Integer getCheckUserId() {
        return checkUserId;
    }

    public void setCheckUserId(Integer checkUserId) {
        this.checkUserId = checkUserId;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason == null ? null : rejectReason.trim();
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getRankScore() {
        return rankScore;
    }

    public void setRankScore(Integer rankScore) {
        this.rankScore = rankScore;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getCash() {
        return cash;
    }

    public void setCash(Integer cash) {
        this.cash = cash;
    }

    public Date getCashValidDate() {
        return cashValidDate;
    }

    public void setCashValidDate(Date cashValidDate) {
        this.cashValidDate = cashValidDate;
    }

    public String getDealerType() {
        return dealerType;
    }

    public void setDealerType(String dealerType) {
        this.dealerType = dealerType == null ? null : dealerType.trim();
    }

    public Date getAuthorizeDate() {
        return authorizeDate;
    }

    public void setAuthorizeDate(Date authorizeDate) {
        this.authorizeDate = authorizeDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
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

    public Integer getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(Integer orderSn) {
        this.orderSn = orderSn;
    }

    public String getLogisticsTempModel() {
        return logisticsTempModel;
    }

    public void setLogisticsTempModel(String logisticsTempModel) {
        this.logisticsTempModel = logisticsTempModel == null ? null : logisticsTempModel.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName == null ? null : shortName.trim();
    }

    public Byte getDealRate() {
        return dealRate;
    }

    public void setDealRate(Byte dealRate) {
        this.dealRate = dealRate;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow == null ? null : isShow.trim();
    }

    public String getCooperationMode() {
        return cooperationMode;
    }

    public void setCooperationMode(String cooperationMode) {
        this.cooperationMode = cooperationMode == null ? null : cooperationMode.trim();
    }

    public String getOrderHandle() {
        return orderHandle;
    }

    public void setOrderHandle(String orderHandle) {
        this.orderHandle = orderHandle == null ? null : orderHandle.trim();
    }

    public String getProfitModel() {
        return profitModel;
    }

    public void setProfitModel(String profitModel) {
        this.profitModel = profitModel == null ? null : profitModel.trim();
    }

    public Integer getWeixinId() {
        return weixinId;
    }

    public void setWeixinId(Integer weixinId) {
        this.weixinId = weixinId;
    }

    public Integer getCrmCostomerId() {
        return crmCostomerId;
    }

    public void setCrmCostomerId(Integer crmCostomerId) {
        this.crmCostomerId = crmCostomerId;
    }

	public String getOperateScope() {
		return operateScope;
	}

	public void setOperateScope(String operateScope) {
		this.operateScope = operateScope;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
    
    
}