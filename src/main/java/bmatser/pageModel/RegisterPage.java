package bmatser.pageModel;
import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.wordnik.swagger.annotations.ApiParam;

@ApiModel(value="注册参数类",description="注册时使用")
public class RegisterPage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4862733980853057363L;

	
	protected int loginId;
	@ApiModelProperty(value="用户名")
	protected String userName;
	
	protected String password;
	
	protected String mobile;
	
	protected String mobileSecret;
	
	protected String email;
	
	protected String registerIp;
	
	protected int dealerId;
	
	protected String dealerName;
	
	protected int score;
	
	protected int cash;
	
	protected String verifyCode;//手机验证码
	
	protected String userNameFlag;//用户名类型(M 手机， E 邮箱）
	
	protected String vCode;//图片验证码
	
	protected String checkStatus;//审核状态
	
	protected String dealerType;//商户类型 4商城普通用户5商城企业用户
	
	protected String regChannel;//商户类型  //注册渠道（1：web 2：android 3：ios）
	
	protected String staffNo;//员工客户专员编号
	
	protected String invitation;//邀请码
	protected String operateScope;//客户行业
	protected String saleCustomerGroup;//客户性质
	protected String linkman;//公司联系人
	protected String linkTel;//公司联系人电话
	protected String linkTelSecret;//公司联系人电话加密
	protected String qq;//qq
	protected String openId;//微信返回的用户唯一标识
	protected String sourceType;//来源渠道
	
    
	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRegisterIp() {
		return registerIp;
	}

	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}

	public int getLoginId() {
		return loginId;
	}

	public void setLoginId(int loginId) {
		this.loginId = loginId;
	}

	public String getUserName() {
		if(null==userName){
			userName="";
		}
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getDealerId() {
		return dealerId;
	}

	public void setDealerId(int dealerId) {
		this.dealerId = dealerId;
	}

	public String getDealerName() {
		if(null==dealerName){
			dealerName="";
		}
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getCash() {
		return cash;
	}

	public void setCash(int cash) {
		this.cash = cash;
	}

	public String getUserNameFlag() {
		return userNameFlag;
	}

	public void setUserNameFlag(String userNameFlag) {
		this.userNameFlag = userNameFlag;
	}

	public String getvCode() {
		return vCode;
	}

	public void setvCode(String vCode) {
		this.vCode = vCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getDealerType() {
		return dealerType;
	}

	public void setDealerType(String dealerType) {
		this.dealerType = dealerType;
	}

	public String getRegChannel() {
		return regChannel;
	}

	public void setRegChannel(String regChannel) {
		this.regChannel = regChannel;
	}

	public String getStaffNo() {
		return staffNo;
	}

	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}

	public String getMobileSecret() {
		return mobileSecret;
	}

	public void setMobileSecret(String mobileSecret) {
		this.mobileSecret = mobileSecret;
	}

	public String getInvitation() {
		return invitation;
	}

	public void setInvitation(String invitation) {
		this.invitation = invitation;
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

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getLinkTel() {
		return linkTel;
	}

	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}

	public String getLinkTelSecret() {
		return linkTelSecret;
	}

	public void setLinkTelSecret(String linkTelSecret) {
		this.linkTelSecret = linkTelSecret;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}
	
}
