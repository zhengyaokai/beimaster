/**
 * 
 */
package bmatser.model;

import java.sql.Timestamp;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 */
@ApiModel(value="客户信息",description="客户信息")
public class CrmCustomer {
	
    private Integer id;
    @ApiModelProperty(value="等级名称")
    private String rankName;
    @ApiModelProperty(value="客户等级ID，关联crm_customer_rank表")
    private Integer rankId;
    @ApiModelProperty(value="公司名称")
    private String name;
    @ApiModelProperty(value="省") 
    private Long provinceId;
    @ApiModelProperty(value="市")   
    private Long cityId;
    @ApiModelProperty(value="所属市场ID，关联crm_marketplace")
    private Integer placeId;
    @ApiModelProperty(value="详细地址")
    private String detailAddress;
    @ApiModelProperty(value="进货渠道")
    private String buyChannel;
    @ApiModelProperty(value="进货渠道")
    private String linkMan;
    @ApiModelProperty(value="职务，关联crm_customer_duty表")
    private Integer dutyId;
    @ApiModelProperty(value="手机号码")
    private String mobile;
  
    private String mobileSecret;
    @ApiModelProperty(value="固话")
    private String telephone;
    @ApiModelProperty(value="qq号码")
    private String qq;
    @ApiModelProperty(value="电子邮箱")
    private String email;
    @ApiModelProperty(value="经度")
    private String lng;
    @ApiModelProperty(value="纬度")
    private String lat;
    @ApiModelProperty(value="状态（1：有效（已审核） 2：删除 3：未审核 4：未注册）")
    private String status;
    @ApiModelProperty(value="创建时间")
    private Timestamp createTime;
    @ApiModelProperty(value="创建人，关联crm_staff表")
    private Integer createStaffId;
    @ApiModelProperty(value="其他主营品牌（在基础库里不存在的）")  
    private String otherBrandName;
    @ApiModelProperty(value="客户对应客户经理Id")  
    private Integer customerManagerId;
    @ApiModelProperty(value="客户对应客户经理名称")  
    private String customerManagerName;
    @ApiModelProperty(value="客户所对应的客服ID")  
    private Integer customerServiceId;
    @ApiModelProperty(value="客户所对应的客服名称") 
    private String customerServiceName;
 
    private String marketPlaceName;
    @ApiModelProperty(value="dealerId")
    private Integer dealerId;
    @ApiModelProperty(value="注册手机") 
    private String registerMobile;
    @ApiModelProperty(value="传真")
    private String fax;
    @ApiModelProperty(value="备注")
    private String remark;

    private String visitRecodes;

    private String returnVisitRecodes;
    private int hasInvit;
    @ApiModelProperty(value="是否手工录入(1为是0,null为不是)")
    private String isManual ;
    
    private String erpCustomerCode;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public Integer getRankId() {
		return rankId;
	}

	public void setRankId(Integer rankId) {
		this.rankId = rankId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Integer getPlaceId() {
		return placeId;
	}

	public void setPlaceId(Integer placeId) {
		this.placeId = placeId;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public String getBuyChannel() {
		return buyChannel;
	}

	public void setBuyChannel(String buyChannel) {
		this.buyChannel = buyChannel;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public Integer getDutyId() {
		return dutyId;
	}

	public void setDutyId(Integer dutyId) {
		this.dutyId = dutyId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getCreateStaffId() {
		return createStaffId;
	}

	public void setCreateStaffId(Integer createStaffId) {
		this.createStaffId = createStaffId;
	}

	public String getOtherBrandName() {
		return otherBrandName;
	}

	public void setOtherBrandName(String otherBrandName) {
		this.otherBrandName = otherBrandName;
	}

	public Integer getCustomerManagerId() {
		return customerManagerId;
	}

	public void setCustomerManagerId(Integer customerManagerId) {
		this.customerManagerId = customerManagerId;
	}

	public String getCustomerManagerName() {
		return customerManagerName;
	}

	public void setCustomerManagerName(String customerManagerName) {
		this.customerManagerName = customerManagerName;
	}

	public Integer getCustomerServiceId() {
		return customerServiceId;
	}

	public void setCustomerServiceId(Integer customerServiceId) {
		this.customerServiceId = customerServiceId;
	}

	public String getCustomerServiceName() {
		return customerServiceName;
	}

	public void setCustomerServiceName(String customerServiceName) {
		this.customerServiceName = customerServiceName;
	}

	public String getMarketPlaceName() {
		return marketPlaceName;
	}

	public void setMarketPlaceName(String marketPlaceName) {
		this.marketPlaceName = marketPlaceName;
	}

	public Integer getDealerId() {
		return dealerId;
	}

	public void setDealerId(Integer dealerId) {
		this.dealerId = dealerId;
	}

	public String getRegisterMobile() {
		return registerMobile;
	}

	public void setRegisterMobile(String registerMobile) {
		this.registerMobile = registerMobile;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getVisitRecodes() {
		return visitRecodes;
	}

	public void setVisitRecodes(String visitRecodes) {
		this.visitRecodes = visitRecodes;
	}

	public String getReturnVisitRecodes() {
		return returnVisitRecodes;
	}

	public void setReturnVisitRecodes(String returnVisitRecodes) {
		this.returnVisitRecodes = returnVisitRecodes;
	}

	public String getIsManual() {
		return isManual;
	}

	public void setIsManual(String isManual) {
		this.isManual = isManual;
	}

	public String getMobileSecret() {
		return mobileSecret;
	}

	public void setMobileSecret(String mobileSecret) {
		this.mobileSecret = mobileSecret;
	}

	public CustomerLinkman toCrmLinkMan() {
		CustomerLinkman linkMan = new CustomerLinkman();
		linkMan.setCustomerId(this.id);
		linkMan.setIsDefault("1");
		linkMan.setLinkMan(this.linkMan);
		linkMan.setMobile(this.mobile);
		linkMan.setMobileSecret(this.mobileSecret);
		linkMan.setStatus("1");
		linkMan.setDutyId(3);
		return linkMan;
	}

	public int getHasInvit() {
		return hasInvit;
	}

	public void setHasInvit(int hasInvit) {
		this.hasInvit = hasInvit;
	}

	public String getErpCustomerCode() {
		return erpCustomerCode;
	}

	public void setErpCustomerCode(String erpCustomerCode) {
		this.erpCustomerCode = erpCustomerCode;
	}
    
    

}
