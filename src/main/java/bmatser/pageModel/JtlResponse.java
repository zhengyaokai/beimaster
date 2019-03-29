package bmatser.pageModel;

import org.apache.commons.lang3.StringUtils;

import bmatser.util.DateTypeHelper;

public class JtlResponse {
	
	private String statusCode = "1";
	private String DateTime ;
	private String msg;
	private Exception e;
	
	public JtlResponse() {
		this.setDateTime(DateTypeHelper.getCurrentDateTimeString());
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public void setError(Exception e) {
		this.e=e;
		e.printStackTrace();
		this.setStatusCode("-1");
		if(StringUtils.isBlank(e.getMessage()) || e.getMessage().getBytes().length>50){
			this.setMsg("程序错误，请联系客服");
		}else{
			this.setMsg(e.getMessage());
		}
	}
	public String getDateTime() {
		return DateTime;
	}
	public void setDateTime(String dateTime) {
		DateTime = dateTime;
	}
	public Object response() {
		if(e!=null){
			return e;
		}else{
			return "success";
		}
	}
	
	
	

}
