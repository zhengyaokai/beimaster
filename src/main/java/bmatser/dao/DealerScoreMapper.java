package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface DealerScoreMapper {

    
	List findByBuyerId(@Param("buyerId")Integer buyerId, @Param("page")int page, @Param("rows")int rows);
	/**
	 * 获取用户成长值
	 * @return
	 */
	List getGrowthValueByBuyerId(@Param("buyerId")Integer buyerId, @Param("page")int page, @Param("rows")int rows);
	/**获取当前成长值*/
	int getRankScoreByBuyerId(@Param("buyerId")Integer buyerId);
	
	//add 20160705 根据buyerId获取成长值总数	
	Long findCountGrowthValue(@Param("buyerId")Integer buyerId);
	
	Long countByBuyerId(@Param("buyerId")Integer buyerId);
	
	int addScore(@Param("buyerId")Integer buyerId, @Param("orderId")Long orderId, @Param("score")int score);

	int findScore(Integer buyerId);
	 /**
		 * 积分扣减需要操作dealer_score表
		 * @param int dealerId, int score, Long relatedId
		 * @return 
		 */
	int addDealerScore(@Param("dealerId")int dealerId,@Param("score")int score,@Param("relatedId")Long relatedId);
	 /**
	 * 查询表gift_exchange_record的最大的id
	 * @param 
	 * @return 
	 */
    Integer selectDealerScoreMaxId();

	Map findRankScore(Integer dealerId);
}