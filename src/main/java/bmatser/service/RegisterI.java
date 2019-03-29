package bmatser.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bmatser.exception.GdbmroException;
import bmatser.pageModel.PageMode;
import bmatser.pageModel.RegisterPage;
import bmatser.pageModel.util.RegisterPageUtil;

public interface RegisterI {

	Map<String, Object> login(HttpServletRequest request,String loginName, String password, String dealerType) throws Exception ;
	
	/**
	 * 商城用户登录
	 * @param loginName
	 * @param password
	 * @return RegisterPage
	 * 2017-7-22
	 * @throws Exception 
	 * 
	 */
	public Map mallLogin(HttpServletRequest request,String loginName, String password,String dealerType) throws Exception;
	
	Map add(RegisterPage registerPage, HttpSession session)  throws Exception;
	
	int editPassword(Integer loginId, String oldPassword, String newPassword);
	
	long existUserName(String userName,String dealerType);
	
	long existMobile(String mobile,String dealerType);
	
	long existEmail(String email,String dealerType);
	
	Map getUserAccount(Integer loginId) throws Exception;
	
	Map addMallRegister(RegisterPage registerPage,HttpServletRequest request,HttpSession session)  throws Exception;

	Map saveMobRegister(RegisterPage registerPage, HttpServletRequest request, HttpSession session) throws Exception;

	int getPasswprd(String mobile, String code, String newPassword,
			HttpServletRequest request) throws Exception;

	int updateMallPasswprd(String mobile, String code, String newPassword,
			HttpServletRequest request) throws Exception;

	void loadUserInfo(HttpServletRequest request);
	
	public Map<String, Object> getUserInfo(String dealerId);

	String loadUserCart(String dealerId);

	Map<String, Object> toMallRegister(RegisterPageUtil registerPage) throws Exception;

	void isMallRegisterSms(RegisterPageUtil registerPage, HttpServletRequest request) throws Exception;

	void modifyMobileSms(RegisterPageUtil registerPage, HttpServletRequest request) throws Exception;

	double getBalance(PageMode model) throws Exception;

	String getAccessToken(String code)throws Exception,GdbmroException;

	void getOpenId(String code, String state, HttpServletRequest request,HttpServletResponse response)throws Exception,GdbmroException;

	Map<String, Object> getSaasUserInfo(String dealerId);

	void updateSaasOpenId(String openId, String dealerId);
}
