package bmatser.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.DealerCashMapper;

/**
 * 
 *	@create_date 2017-12-23
 *
 */
@Service("myCashService")
public class MyCashImpl implements MyCashI {
	@Autowired
	private DealerCashMapper dealerCashMapper;
	/**
	 * 查询现金券
	 */
	@Override
	public Map findMyCash(String dealerId, int page) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cash", dealerCashMapper.findCash(Integer.parseInt(dealerId)));
		map.put("list", dealerCashMapper.findByBuyerId(Integer.parseInt(dealerId), page*10, 10));
		return map;
	}

}
