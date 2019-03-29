package bmatser.model;

import java.util.Date;

import bmatser.util.DateTypeHelper;

public class UserBlackList {
	
	private String ip;
	private Long  ipCode;//ip（long型）
	private String dealerId;
	private int searchCount;//查询次数
	private int loginStatus; //0正常 1黑名单 2白名单
	private Long lastVerifyTime;//最后校验时间（long型）
	private String lastVDateFormat;//最后校验时间（字符型）
	private int verifyCode;//1 提交验证码 0验证通过
	
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDealerId() {
		return dealerId;
	}
	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}

	public int getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(int loginStatus) {
		this.loginStatus = loginStatus;
	}
	
	
	/**
	 * 首次保存
	 * @param ip
	 * @param dealerId
	 */
	public void InsertForFirst(String ip, String dealerId,Long ipCode){
		this.setIp(ip);
		this.setDealerId(dealerId);
		this.setSearchCount(0);
		this.setLastVerifyTime(new Date().getTime());
		this.setVerifyCode(1);
		this.setIpCode(ipCode);
		this.setLastVDateFormat(DateTypeHelper.getCurrentDateTimeString());
	}
	
	
	
	public int getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(int verifyCode) {
		this.verifyCode = verifyCode;
	}

	public int getSearchCount() {
		return searchCount;
	}
	public void setSearchCount(int searchCount) {
		this.searchCount = searchCount;
	}
	/**
	 * 黑名单
	 * @return
	 */
	public boolean isWhite() {
		if(this.getLoginStatus()==2){
			return true;
		}else{
			return false;
		}
	}
	public Long getIpCode() {
		return ipCode;
	}
	public void setIpCode(Long ipCode) {
		this.ipCode = ipCode;
	}
	/**
	 * 白名单
	 * @return
	 */
	public boolean isBlack() {
		if(this.getLoginStatus() ==1){
			return true;
		}else{
			return false;
		}
	}
	public Long getLastVerifyTime() {
		return lastVerifyTime;
	}
	public void setLastVerifyTime(Long lastVerifyTime) {
		this.lastVerifyTime = lastVerifyTime;
	}
	public String getLastVDateFormat() {
		return lastVDateFormat;
	}
	public void setLastVDateFormat(String lastVDateFormat) {
		this.lastVDateFormat = lastVDateFormat;
	}


	
	
	
	
	
	
}
