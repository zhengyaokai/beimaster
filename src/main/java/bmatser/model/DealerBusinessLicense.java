package bmatser.model;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;



import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
/**
 * 营业执照
 * @author felx
 * @date 2016.02.23 
 */
@ApiModel(value="营业执照",description="营业执照")
public class DealerBusinessLicense {
    private Integer id;
    @ApiModelProperty(value="dealerId")
    private Integer dealerId;
    @ApiModelProperty(value="注册名称")
    private String regName;
    @ApiModelProperty(value="公司名称")
    private String dealerName;
    @ApiModelProperty(value="成立时间")
    private Date regDate;
    @ApiModelProperty(value="注册所在地")
    private Integer provinceId;
    @ApiModelProperty(value="注册所在城市")
    private Integer cityId;
    @ApiModelProperty(value="注册所在辖区")
    private Integer areaId;
    @ApiModelProperty(value="注册详细地址")
    private String address;
    @ApiModelProperty(value="注册资本，以万元为单位")
    private BigDecimal regCapital;
    @ApiModelProperty(value="经营范围")
    private String businessScope;
    @ApiModelProperty(value="经营范围")
    private String operateScope;
    @ApiModelProperty(value="营业执照号")
    private String businessLicenseNo;
    @ApiModelProperty(value="统一社会信用代码")    
    //新增统一社会信用代码字段  modify 20160602
    private String creditCode;
    @ApiModelProperty(value="营业开始日期")
    private Date businessBeginDate;
    @ApiModelProperty(value="营业结束日期")
    private Date businessEndDate;
    @ApiModelProperty(value="法人代表姓名")
    private String corporateName;
    @ApiModelProperty(value="法人身份证电子版")
    private String idCardFile;
    @ApiModelProperty(value="营业执照副本电子版")
    private String businessLicenseFile;
    @ApiModelProperty(value="状态（1：有效 2：删除）")
    private String status;
    @ApiModelProperty(value="税务登记电子版")   
    private String taxCertificateFile;
    @ApiModelProperty(value="组织机构代码证电子版")    
    private String organizeCodeFile;
    @ApiModelProperty(value="联系人")    
    private String linkMan;
    @ApiModelProperty(value="邀请码")    
    private String staffNo;
    
	public String getCreditCode() {
		return creditCode;
	}

	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDealerId() {
		return dealerId;
	}

	public void setDealerId(Integer dealerId) {
		this.dealerId = dealerId;
	}

	public String getRegName() {
		if(null==regName){
			regName="";
		}
		return regName;
	}

	public void setRegName(String regName) {
		this.regName = regName;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
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
		this.address = address;
	}

	public BigDecimal getRegCapital() {
		return regCapital;
	}

	public void setRegCapital(BigDecimal regCapital) {
		this.regCapital = regCapital;
	}

	public String getBusinessScope() {
		return businessScope;
	}

	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}

	public String getBusinessLicenseNo() {
		if(null==businessLicenseNo){
			businessLicenseNo="";
		}
		return businessLicenseNo;
	}

	public void setBusinessLicenseNo(String businessLicenseNo) {
		this.businessLicenseNo = businessLicenseNo;
	}

	public Date getBusinessBeginDate() {
		return businessBeginDate;
	}

	public void setBusinessBeginDate(Date businessBeginDate) {
		this.businessBeginDate = businessBeginDate;
	}

	public Date getBusinessEndDate() {
		return businessEndDate;
	}

	public void setBusinessEndDate(Date businessEndDate) {
		this.businessEndDate = businessEndDate;
	}

	public String getCorporateName() {
		return corporateName;
	}

	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}

	public String getIdCardFile() {
		return idCardFile;
	}

	public void setIdCardFile(String idCardFile) {
		this.idCardFile = idCardFile;
	}

	public String getBusinessLicenseFile() {
		if(null==businessLicenseFile){
			businessLicenseFile="";
		}
		return businessLicenseFile;
	}

	public void setBusinessLicenseFile(String businessLicenseFile) {
		this.businessLicenseFile = businessLicenseFile;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaxCertificateFile() {
		return taxCertificateFile;
	}

	public void setTaxCertificateFile(String taxCertificateFile) {
		this.taxCertificateFile = taxCertificateFile;
	}

	public String getOrganizeCodeFile() {
		return organizeCodeFile;
	}

	public void setOrganizeCodeFile(String organizeCodeFile) {
		this.organizeCodeFile = organizeCodeFile;
	}



	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	
	
	
	public Boolean isEmpty(){
		if(StringUtils.isBlank(this.operateScope)){
			this.setOperateScope("50");
		}
		if(StringUtils.isBlank(this.operateScope) || StringUtils.isBlank(this.dealerName) || StringUtils.isBlank(this.linkMan)){
			return true;
		}
		return false;
	}

	public String getStaffNo() {
		return staffNo;
	}

	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}

	public CrmCustomer toCustomer() throws Exception {
		CrmCustomer crm = new CrmCustomer();
		if(null!=this.linkMan){
			crm.setLinkMan(URLDecoder.decode(this.linkMan, "UTF-8"));
		}
		crm.setDealerId(dealerId);
		crm.setName(this.dealerName);
		crm.setStatus("3");
		crm.setRankId(2);
		crm.setCityId(Long.valueOf(this.cityId));
		crm.setProvinceId(Long.valueOf(this.provinceId));
		return crm;
	}

	public String getOperateScope() {
		return operateScope;
	}

	public void setOperateScope(String operateScope) {
		this.operateScope = operateScope;
	}
	
    
}