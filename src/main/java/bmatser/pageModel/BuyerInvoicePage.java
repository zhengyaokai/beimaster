package bmatser.pageModel;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
@ApiModel(value="零售商发票")
public class BuyerInvoicePage implements Serializable {

    private Integer id;
    @ApiModelProperty(value="零售商ID")
    private Integer buyerId;//零售商ID
    @ApiModelProperty(value="发票类型(1:普通发票, 2:增值税发票)")
    private String invType;//发票类型(1:普通发票, 2:增值税发票)
    @ApiModelProperty(value="单位名称(抬头)，如果是个人就写个人名称")
    private String invTitle;//单位名称(抬头)，如果是个人就写个人名称
    @ApiModelProperty(value="发票内容")
    private String invContent;//发票内容
    @ApiModelProperty(value="纳税人识别号")
    private String taxpayeRno;//纳税人识别号
    @ApiModelProperty(value="注册地址")
    private String regAddress;//注册地址
    @ApiModelProperty(value="电话")
    private String regTelphone;//电话

    private String regTelphoneSecret;//密码
    @ApiModelProperty(value="开户银行")
    private String bank;//开户银行
    @ApiModelProperty(value="银行帐户")
    private String bankAccount;//银行帐户
    @ApiModelProperty(value="收票人姓名")
    private String recvName;//收票人姓名
    @ApiModelProperty(value="收票人手机号")
    private String recvMobile;//收票人手机号

    private String recvMobileSecret;//
    @ApiModelProperty(value="省")
    private Integer recvProvince;//省
    @ApiModelProperty(value="市")
    private Integer recvCity;//市
    @ApiModelProperty(value="区")
    private Integer recvArea;//区
    @ApiModelProperty(value="送票地址")
    private String recvAddress;//送票地址
    @ApiModelProperty(value="是否默认(0：否  1：是)")
    private String isDefault;//是否默认(0：否  1：是)
    @ApiModelProperty(value="发票抬头类型（1：个人，2：企业）")
    private String invTitleType;//发票抬头类型（1：个人，2：企业）
    @ApiModelProperty(value="授权委托书")
    private String authorization;//发票抬头类型（1：个人，2：企业）
    @ApiModelProperty(value="委托公司营业执照")
    private String clientCompanyLicenseFile;//发票抬头类型（1：个人，2：企业）
    @ApiModelProperty(value="公司名称")
    private String dealerName;//发票抬头类型（1：个人，2：企业）
    @ApiModelProperty(value="开票申请书资质")
    private String qualifications;//开票申请书资质(在customer_qualified这张表)
    @ApiModelProperty(value="开票申请是否审核（0：未审核 1：已审核 2审核不通过）")
    private String isReview;//开票申请是否审核（0：未审核 1：已审核 2审核不通过）(在customer_qualified这张表)

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Integer buyerId) {
		this.buyerId = buyerId;
	}

	public String getInvType() {
		return invType;
	}

	public void setInvType(String invType) {
		this.invType = invType;
	}

	public String getInvTitle() {
		return invTitle;
	}

	public void setInvTitle(String invTitle) {
		this.invTitle = invTitle;
	}

	public String getInvContent() {
		return invContent;
	}

	public void setInvContent(String invContent) {
		this.invContent = invContent;
	}

	public String getTaxpayeRno() {
		return taxpayeRno;
	}

	public void setTaxpayeRno(String taxpayeRno) {
		this.taxpayeRno = taxpayeRno;
	}

	public String getRegAddress() {
		return regAddress;
	}

	public void setRegAddress(String regAddress) {
		this.regAddress = regAddress;
	}

	public String getRegTelphone() {
		return regTelphone;
	}

	public void setRegTelphone(String regTelphone) {
		this.regTelphone = regTelphone;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getRecvName() {
		return recvName;
	}

	public void setRecvName(String recvName) {
		this.recvName = recvName;
	}

	public String getRecvMobile() {
		return recvMobile;
	}

	public void setRecvMobile(String recvMobile) {
		this.recvMobile = recvMobile;
	}

	public Integer getRecvProvince() {
		return recvProvince;
	}

	public void setRecvProvince(Integer recvProvince) {
		this.recvProvince = recvProvince;
	}

	public Integer getRecvCity() {
		return recvCity;
	}

	public void setRecvCity(Integer recvCity) {
		this.recvCity = recvCity;
	}

	public Integer getRecvArea() {
		return recvArea;
	}

	public void setRecvArea(Integer recvArea) {
		this.recvArea = recvArea;
	}

	public String getRecvAddress() {
		return recvAddress;
	}

	public void setRecvAddress(String recvAddress) {
		this.recvAddress = recvAddress;
	}

	public String getIsDefault() {
		return isDefault==null?"0":isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getInvTitleType() {
		return invTitleType;
	}

	public void setInvTitleType(String invTitleType) {
		this.invTitleType = invTitleType;
	}
	
    public String getRegTelphoneSecret() {
		return regTelphoneSecret;
	}

	public void setRegTelphoneSecret(String regTelphoneSecret) {
		this.regTelphoneSecret = regTelphoneSecret;
	}

	public String getRecvMobileSecret() {
		return recvMobileSecret;
	}

	public void setRecvMobileSecret(String recvMobileSecret) {
		this.recvMobileSecret = recvMobileSecret;
	}

	public void checkData() throws Exception {
    	if(StringUtils.isBlank(this.getInvType())){
    		throw new Exception("发票类型不能为空");
    	}
    	if(StringUtils.isBlank(this.getInvTitle())&&"2".equals(this.getInvType())){
    		throw new Exception("发票抬头不能为空");
    	}
    	if(StringUtils.isBlank(this.getTaxpayeRno())&&"2".equals(this.getInvType())){
    		throw new Exception("纳税人识别码不能为空");
    	}
    	if(StringUtils.isBlank(this.getRegAddress())&&"2".equals(this.getInvType())){
    		throw new Exception("注册地址不能为空");
    	}
    	if(StringUtils.isBlank(this.getRegTelphone())&&"2".equals(this.getInvType())){
    		throw new Exception("注册电话不能为空");
    	}
    	if(StringUtils.isBlank(this.getBank())&&"2".equals(this.getInvType())){
    		throw new Exception("开户银行不能为空");
    	}
    	if(StringUtils.isBlank(this.getBankAccount())&&"2".equals(this.getInvType())){
    		throw new Exception("银行账户不能为空");
    	}
/*    	if(StringUtils.isBlank(this.getInvContent())){
    		throw new Exception("发票内容不能为空");
    	}*/
    	if(StringUtils.isBlank(this.getRecvName())&&"2".equals(this.getInvType())){
    		throw new Exception("发票接收人姓名不能为空");
    	}
    	if(this.getRecvProvince()==null || "".equals(this.getRecvProvince())){
    		throw new Exception("发票接收省不能为空");
    	}
    	if(this.getRecvCity()==null || "".equals(this.getRecvCity())){
    		throw new Exception("发票接收市不能为空");
    	}
    	if(this.getRecvArea()==null || "".equals(this.getRecvArea())){
    		throw new Exception("发票接收区不能为空");
    	}
    	if(StringUtils.isBlank(this.getRecvAddress())){
    		throw new Exception("发票接收地址不能为空");
    	}
    	if(StringUtils.isBlank(this.getRecvMobile())){
    		throw new Exception("发票接收电话不能为空");
    	}	   	    	
    }

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	public String getClientCompanyLicenseFile() {
		return clientCompanyLicenseFile;
	}

	public void setClientCompanyLicenseFile(String clientCompanyLicenseFile) {
		this.clientCompanyLicenseFile = clientCompanyLicenseFile;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getQualifications() {
		return qualifications;
	}

	public void setQualifications(String qualifications) {
		this.qualifications = qualifications;
	}

	public String getIsReview() {
		return isReview;
	}

	public void setIsReview(String isReview) {
		this.isReview = isReview;
	}

	
}
