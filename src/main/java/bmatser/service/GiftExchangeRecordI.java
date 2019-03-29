package bmatser.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;

import bmatser.model.CrmCustomerCategory;
import bmatser.model.Dealer;
import bmatser.model.DealerBusinessLicense;
import bmatser.model.DealerCash;
import bmatser.model.GiftExchangeRecord;
import bmatser.util.LoginInfo;
/**
 * 礼品兑换记录
 * @author felx
 * @date 2016.03.09 
 */
public interface GiftExchangeRecordI {
	/**
	 *  查询礼品兑换记录
	 */
	List selectCreExchange(Map mp);
	
	/**
	 * 查询礼品兑换总记录
	 */
	Integer count(Map mp);
	/**
	 * 根据礼品所需要的积分对dealer表中的积分字段进行扣减
	 * @param Integer dealerId
	 * @return 
	 */
    Long updateScoreById(HttpServletRequest request,Map mp,GiftExchangeRecord gr) throws Exception;
    //APP查询 礼品兑换记录详情
	Map selectAppGiftInfo(String dealerId, String id);
	
}
