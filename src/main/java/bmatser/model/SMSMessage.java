package bmatser.model;

import java.sql.Timestamp;
/**
 * 发送短信验证码保存到mongodb 实体
 * @author felx
 * Date 20160906
 *
 */
public class SMSMessage {
	private String mobile; //手机
	private Timestamp useTime;//发送时间
	private String smsParam;//参数
	private String smsTempCode;//对应调用方法
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Timestamp getUseTime() {
		return useTime;
	}
	public void setUseTime(Timestamp useTime) {
		this.useTime = useTime;
	}
	public String getSmsParam() {
		return smsParam;
	}
	public void setSmsParam(String smsParam) {
		this.smsParam = smsParam;
	}
	public String getSmsTempCode() {
		return smsTempCode;
	}
	public void setSmsTempCode(String smsTempCode) {
		this.smsTempCode = smsTempCode;
	}
	
	
	

}
