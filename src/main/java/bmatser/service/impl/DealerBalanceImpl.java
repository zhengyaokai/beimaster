package bmatser.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.DealerBalanceMapper;
import bmatser.dao.OrderInfoMapper;
import bmatser.model.DealerBalanceRecord;
import bmatser.service.DealerBalanceI;
@Service("balanceService")
public class DealerBalanceImpl implements DealerBalanceI{
	@Autowired
	private  DealerBalanceMapper dealerBalanceMapper;
	/**
	 * 获取用户余额及使用记录
	 * @param page
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> getDealerBalanceByBuyerId(Integer buyerId, Integer page,
			Integer rows) {
		Map map = new HashMap();
		/**获取用户余额使用记录*/
		List<DealerBalanceRecord> list = dealerBalanceMapper.getDealerBalanceByBuyerId(buyerId, page, rows);
		if(list!=null&&list.size()!=0){
			for(DealerBalanceRecord dbr : list){
				if("1".equals(dbr.getType())&&dbr.getBalanceApplyId()!=null){
					dbr.setSubtitle("提现不成功，余额返还");
				}
				if("2".equals(dbr.getType())||"3".equals(dbr.getType())){
					dbr.setBalance(dbr.getBalance().multiply(new BigDecimal(-1)));
				}
				if("3".equals(dbr.getType())){
					if("1".equals(dbr.getBalanceApplyStatus())||
					   "2".equals(dbr.getBalanceApplyStatus())||
					   "3".equals(dbr.getBalanceApplyStatus())){
						dbr.setShowStatus("处理中");
					}else if("4".equals(dbr.getBalanceApplyStatus())){
						dbr.setShowStatus("处理成功");
					}else if("5".equals(dbr.getBalanceApplyStatus())){
						dbr.setShowStatus("已关闭");
					}
					if(dbr.getBalanceApplyId()!=null&&"5".equals(dbr.getBalanceApplyStatus())){
						dbr.setSubtitle("提现不成功，交易关闭");
					}
				}else{
					dbr.setShowStatus("处理成功");
				}
				
			}
		}
		
		//获取用户余额
		BigDecimal decimal = dealerBalanceMapper.getDealerBalance(buyerId);
		//获取总数
		Long total = dealerBalanceMapper.getDealerBalanceCount(buyerId);
		map.put("balance", decimal);
		map.put("count", total);
		map.put("list", list);
		return map;
	}

}
