package bmatser.service;

import java.util.List;
import java.util.Map;

public interface DealerBalanceI {
	/**
	 * 获取用户余额及使用记录
	 * @param page
	 * @return
	 */
	Map<String, Object> getDealerBalanceByBuyerId(Integer buyerId, Integer page, Integer rows);

}
