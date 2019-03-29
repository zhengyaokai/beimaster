package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.model.GiftExchangeRecord;
/**
 * 礼品兑换记录
 * @author felx
 * @date 2016.03.09 
 */
public interface GiftExchangeRecordMapper {

	List selectCreExchange(Map mp);
	
	Integer count(Map mp);
	
    int updateScoreById(Map mp);
	
	Integer selectScoreRemain(Integer dealerId);
	
	Integer insert(GiftExchangeRecord gr);

	int getExistEZhuGiftStatus(String id);

	void updateEzhuInfo(@Param("id")String id, @Param("logisticsId")String logisticsId, @Param("logisticsNo")String logisticsNo);

	Map selectAppGiftInfo(@Param("dealerId") String dealerId,@Param("id") String id);
	
}