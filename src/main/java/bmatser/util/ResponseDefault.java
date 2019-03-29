package bmatser.util;

import javax.servlet.http.HttpServletRequest;

import bmatser.exception.GdbmroException;

public abstract class ResponseDefault extends ResponseMsg {

	HttpServletRequest request;
	
	public ResponseDefault() {
		// TODO Auto-generated constructor stub
	}
	public ResponseDefault(HttpServletRequest request) {
		this.request = request;
		try {
			this.setData(run());
		} catch (GdbmroException e) {
			this.setError(e);
			this.setCode(e.getCode());
		} catch (Exception e) {
			this.setError(e);
		}
	}


	public abstract Object run() throws GdbmroException,Exception;


	public HttpServletRequest getRequest() {
		return request;
	}


	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	

}
