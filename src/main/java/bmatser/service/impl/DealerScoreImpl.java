package bmatser.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.DealerScoreMapper;
import bmatser.service.DealerScoreI;

@Service("scoreService")
public class DealerScoreImpl implements DealerScoreI {

	@Autowired
	private DealerScoreMapper scoreDao;

	/**
	 * 获取指定用户的积分列表
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
		/**获取积分列表数据*/
		List<Object> cashList = scoreDao.findByBuyerId(buyerId, page, rows);
		map.put("rows", cashList);
		map.put("score", scoreDao.findScore(buyerId));
		/**获取指定用户的积分记录数*/
		Long count = scoreDao.countByBuyerId(buyerId);
		map.put("total", count);
		return map;
	}
	
	/**
	 * 获取用户成长值
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> getGrowthValueByBuyerId(Integer buyerId, Integer page, Integer rows){
		Map map = new HashMap();
		/**获取用户成长值*/
		List<Object> growthValueList = scoreDao.getGrowthValueByBuyerId(buyerId, page, rows);
		//add 20160705 根据buyerId获取成长值总数
		Long total = scoreDao.findCountGrowthValue(buyerId);
		map.put("count", total);
		/**获取当前成长值*/
		int nowGrowthValue = scoreDao.getRankScoreByBuyerId(buyerId);
		map.put("nowGrowthValue", nowGrowthValue);
		map.put("GrowthValue", growthValueList);
		return map;
	}
}
