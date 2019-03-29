package bmatser.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.model.Register;
import bmatser.pageModel.MyInfoPage;
import bmatser.pageModel.RegisterPage;

public interface RegisterMapper {
	
	Map login(@Param("loginName")String loginName, @Param("password")String password, @Param("dealerType")String dealerType,@Param("mobileSecret")String mobileSecret); 
	
	int add(RegisterPage registerPage);
	
	int editPassword(@Param("loginId")Integer loginId, @Param("oldPassword")String oldPassword,
			@Param("newPassword")String newPassword);
	
	long existUserName(@Param("userName")String userName, @Param("dealerType")String dealerType);
	
	long existMobile(@Param("mobile")String mobile, @Param("dealerType")String dealerType);
	
	long existEmail(@Param("email")String email, @Param("dealerType")String dealerType);
	
	Map getUserAccount(@Param("loginId")Integer loginId);

	void updateByPersonData(MyInfoPage info);

	int isLicense(String dealerId);

	int getPasswordBymob(@Param("mobile")String mobile,@Param("newPassword")String newPassword);

	int getPasswordByMall(@Param("mobile")String mobile,@Param("newPassword")String newPassword);
	
	Register selectIdByDealerId(Integer dealerId);

	Map<String, Object> loadUserInfo(String dealerId);
	
	Map<String, Object> getSaasUserInfo(@Param("dealerId")String dealerId);

	void updateLastTime(@Param("id")String id);

	int existMallUserName(@Param("userName")String userName);

	double getBalance(@Param("dealerId")String dealerId);

	Map getDealerInfo(@Param("openId")String openId);

	void updateMobile(@Param("mobileSecret") String mobileSecret,@Param("mobile") String mobile,@Param("dealerId") String dealerId);

	int existMallMobile(@Param("newMobileSecret") String newMobileSecret);
	
}