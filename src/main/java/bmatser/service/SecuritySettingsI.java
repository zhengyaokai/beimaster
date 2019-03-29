package bmatser.service;

import javax.servlet.http.HttpServletRequest;

public interface SecuritySettingsI {

	String findMobile(Integer dealerId);

	void modifyMobile(String verifyCode, String newMobile, String dealerId, HttpServletRequest request) throws Exception;

}
