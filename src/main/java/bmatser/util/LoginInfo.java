package bmatser.util;

import java.io.Serializable;

import bmatser.pageModel.PageMode.Channel;

public class LoginInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3799149514942574274L;
	
	private String username;
	private String loginId;
	private String dealerName;
	private String dealerId;
	private String cash;
	private String checkStatus;
	private String isLicense;
	private String score;
	private String cartNum;
	private String dealerType;
	private int rank;
	private int bean;
	private Channel channel;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	public String getDealerId() {
		return dealerId;
	}
	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}
	public String getCash() {
		return cash;
	}
	public void setCash(String cash) {
		this.cash = cash;
	}
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getIsLicense() {
		return isLicense;
	}
	public void setIsLicense(String isLicense) {
		this.isLicense = isLicense;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getCartNum() {
		return cartNum;
	}
	public void setCartNum(String cartNum) {
		this.cartNum = cartNum;
	}
	public String getDealerType() {
		return dealerType;
	}
	public void setDealerType(String dealerType) {
		this.dealerType = dealerType;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoginInfo [username=");
		builder.append(username);
		builder.append(", loginId=");
		builder.append(loginId);
		builder.append(", dealerName=");
		builder.append(dealerName);
		builder.append(", dealerId=");
		builder.append(dealerId);
		builder.append(", cash=");
		builder.append(cash);
		builder.append(", checkStatus=");
		builder.append(checkStatus);
		builder.append(", isLicense=");
		builder.append(isLicense);
		builder.append(", score=");
		builder.append(score);
		builder.append(", cartNum=");
		builder.append(cartNum);
		builder.append(", dealerType=");
		builder.append(dealerType);
		builder.append("]");
		return builder.toString();
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getBean() {
		return bean;
	}
	public void setBean(int bean) {
		this.bean = bean;
	}
	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	
	
	
}
