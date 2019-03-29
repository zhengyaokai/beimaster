package bmatser.model;

import bmatser.util.DESCoder;

public class JtlRegisterInfo {
	
  	private String dealerName;//公司名称
  	private String  businessLicenseNo;//营业执照号
  	private String  businessLicenseFile;//上传营业执照
  	private String  regCapital;//注册资本
  	private String  corporateName;//法定代表人
  	private String  idCardFile;//上传法人身份证
  	private String  operateScope;//客户行业
  	private String  saleCustomerGroup;//客户性质
  	private String  linkMan;//联系人
  	private String  mobile;//手机号码
  	private String  mobileSecret;//手机号码
  	private String  idCardBackFile;//身份证背面
  	
  	
  	public void encryptMobile() throws Exception{
  		if(vMobile()){
  			DESCoder.instanceMobile();
  			this.mobileSecret = DESCoder.encoder(mobile).trim();
  			this.mobile = DESCoder.getNewTel(this.mobile);
  		}
  	}
  	public boolean vMobile(){
  		if(this.mobile != null&& this.mobile.indexOf("*")<0 && (15>this.mobile.length()&&this.mobile.length()>5)){
  			return true;
  		}
  		return false;
  	}
  	
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	public String getBusinessLicenseNo() {
		return businessLicenseNo;
	}
	public void setBusinessLicenseNo(String businessLicenseNo) {
		this.businessLicenseNo = businessLicenseNo;
	}
	public String getBusinessLicenseFile() {
		return businessLicenseFile;
	}
	public void setBusinessLicenseFile(String businessLicenseFile) {
		this.businessLicenseFile = businessLicenseFile;
	}
	public String getRegCapital() {
		return regCapital;
	}
	public void setRegCapital(String regCapital) {
		this.regCapital = regCapital;
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
	public String getOperateScope() {
		return operateScope;
	}
	public void setOperateScope(String operateScope) {
		this.operateScope = operateScope;
	}
	public String getSaleCustomerGroup() {
		return saleCustomerGroup;
	}
	public void setSaleCustomerGroup(String saleCustomerGroup) {
		this.saleCustomerGroup = saleCustomerGroup;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMobileSecret() {
		return mobileSecret;
	}
	public void setMobileSecret(String mobileSecret) {
		this.mobileSecret = mobileSecret;
	}
	public String getIdCardBackFile() {
		return idCardBackFile;
	}
	public void setIdCardBackFile(String idCardBackFile) {
		this.idCardBackFile = idCardBackFile;
	}
  	
  	

}
