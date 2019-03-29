package bmatser.pageModel;

import java.io.Serializable;
/**
 * @description 账单和开票的查询操作
 * @author shizm
 * @version 1.0
 * @date 2014-12-10
 */
public class ShunfengOperatePage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 351495222484901706L;
	private String orderId;
	private Integer num;
	private String appKey;
	private String sign;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

}
