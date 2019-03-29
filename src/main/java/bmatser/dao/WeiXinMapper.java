package bmatser.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface WeiXinMapper {

	Map getDealerInfo(@Param("openId")String openId);

	Map getWXSaasDealerInfo(@Param("openId")String openId);
	
	void saveOpenId(@Param("openId") String openId);

	void saveOpenIdByDealerId(@Param("openId") String openId,@Param("dealerId") String dealerId);

	int selectDealerInvitedInfo(@Param("invitedOpenId") String invitedOpenId);

	void insertDealerInvited(@Param("dealerId") String dealerId,@Param("invitedOpenId") String invitedOpenId);

}
