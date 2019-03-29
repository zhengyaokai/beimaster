package bmatser.service;

import java.util.Map;

public interface DealerScoreI {

	Map<String, Object> findByBuyerId(Integer buyerId, Integer page, Integer rows);
	
	/**
	 * 获取用户成长值
	 * @return
	 */
	Map<String, Object> getGrowthValueByBuyerId(Integer buyerId, Integer page, Integer rows);
}
