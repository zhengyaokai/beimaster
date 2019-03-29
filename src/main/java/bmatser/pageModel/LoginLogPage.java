package bmatser.pageModel;

import java.io.Serializable;

/**
 * @description 登录日志
 * @author shizm
 * @version 1.0
 * @date 2017-03-31 上午
 */
public class LoginLogPage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3778878145984468583L;
	/* 登录人Id */
	private String loginId;
	/* 用户名 */
	private String loginName;
	/* 商家Id */
	private String dealerId;
	/* 登录时间 */
	private String loginTime;
	/* 退出时间 */
	private String logoutTime;
	/* ip地址 */
	private String ip;	
	/* 登录渠道 */
	private String type;
	
	private String year ;
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	private String month ;
	 
	private String day ;
	
	private String week ;
	

	public String getDealerId() {
		return dealerId;
	}
	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	public String getLogoutTime() {
		return logoutTime;
	}
	public void setLogoutTime(String logoutTime) {
		this.logoutTime = logoutTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}


}
