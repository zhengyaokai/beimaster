package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.model.GiftExchangeRecord;

public interface GiftMapper {

	List findGiftList(@Param("isRecommend") String isRecommend,@Param("type") String type);

	List findAppGiftList(@Param("isRecommend") String isRecommend,@Param("type") String type,@Param("page") int page, @Param("row") int row);
	
	int findAppGiftListCount(@Param("isRecommend") String isRecommend,@Param("type") String type);
	
	Map findGiftById(String id);

	Integer selectScoreById(@Param("id") Integer id);
	
	String selectExchangeTime(Integer giftId);

	void updateSurplusNum(GiftExchangeRecord gr);

	int selectSurplusNumById(@Param("giftId") Integer giftId);

}
