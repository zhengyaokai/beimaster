package bmatser.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bmatser.exception.GdbmroException;
import bmatser.util.ResponseMsg;

public interface WeiXinI {
	String getWXSaasOpenId(String code)throws Exception,GdbmroException;

	ResponseMsg getOpenId(String code, String state, HttpServletRequest request,HttpServletResponse response)throws Exception,GdbmroException;

	void saveDealerInvited(HttpServletRequest request)throws Exception;

	void saveOpenId(String openId);

	void saveOpenIdByDealerId(String openId, String dealerId);
	
	/**
	 * 根据openId获取登录信息
	 * @author felx
	 * @date 2016-10-19
	 */
	public Map getLoginInfo(String openId);
	//根据openId获取微信saas登录信息
	public Map getWXSaasLoginInfo(String openId);
}
