package bmatser.util;

import org.apache.log4j.Logger;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import bmatser.exception.GdbmroException;

@ApiModel(value="返回结果",description="返回结果数据")
public class   ResponseMsg  {

	Logger log = Logger.getLogger(ResponseMsg.class);

	@ApiModelProperty(value="错误码")
	private int code;
	@ApiModelProperty(value="消息提示")
	private String msg = "";
	@ApiModelProperty(value="数据")
	private Object data ;

	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public ResponseMsg setError(String str) {
		this.setCode(-1);
		this.setMsg(str);
		return this;
	}
	public ResponseMsg setError(int code,String str) {
		this.setCode(code);
		this.setMsg(str);
		return this;
	}
	public ResponseMsg setError(Throwable e) {
		this.log.info(e.toString());
		e.printStackTrace();
		if(e instanceof GdbmroException){
			this.setCode(((GdbmroException) e).getCode());
			this.setMsg(e.getMessage());
			return this;
		}else{
			this.setCode(-1);
		}
		if(e.getMessage() !=null &&e.getMessage().length()!=e.getMessage().getBytes().length && e.getMessage().length() < 50){
			this.setMsg(e.getMessage());
		}else if(e.getMessage() ==null){
			this.setMsg("程序出错,请联系客服");
		}else{
			this.setMsg("程序出错,请联系客服");
		}
		this.data = null;
		return this;
	}


}
