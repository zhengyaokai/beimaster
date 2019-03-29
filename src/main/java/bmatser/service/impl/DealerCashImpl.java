package bmatser.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.DealerCashMapper;
import bmatser.model.DealerCash;
import bmatser.service.DealerCashI;

/**
 * 用户现金券
 * @author felx
 * 2017-7-28
 */
@Service("cashService")
public class DealerCashImpl implements DealerCashI {
	
	@Autowired
	private DealerCashMapper cashDao;

	/**
	 * 获取指定用户的现金券列表
	 * @param buyerId
	 * @param page
	 * @param rows
	 * @return map
	 * 2017-7-28
	 *
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Object> findByBuyerId(Integer buyerId, Integer page, Integer rows) {
		Map map = new HashMap();
		/**获取现金券列表数据*/
		List<Object> cashList = cashDao.findByBuyerId(buyerId, page, rows);
		map.put("rows", cashList);
		map.put("cash", cashDao.findCash(buyerId));
		/**获取指定用户的现金券记录数*/
		Long count = cashDao.countByBuyerId(buyerId);
		map.put("total", count);
		return map;
	}

	/**
	 * 保存现金券
	 * @author felx
	 * @date 2016-1-22
	 */
	public void insertDealerCash(DealerCash dealerCash){
		cashDao.insertDealerCash(dealerCash);
	}
}
