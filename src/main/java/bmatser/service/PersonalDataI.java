package bmatser.service;

import java.util.List;
import java.util.Map;

import bmatser.pageModel.MyInfoPage;
import bmatser.pageModel.PageMode;

public interface PersonalDataI {
	/**
	 * 查看个人资料	
	 * @param dealerId 
	 * @return
	 * @throws Exception
	 */
	Map findRegister(String dealerId) throws Exception ;
	/**
	 * @author felx
	 * create time : 2016/5/19
	 * 查询个人等级，等级积分，距离升级还需多少等级积分以及超过多少用户
	 * @param dealerId
	 * @return
	 * @throws Exception
	 */
	Map findDealerRankAndRankScore(String dealerId) throws Exception;
	/**
	 * 更新个人资料	
	 * @param dealerId 
	 * @return
	 * @throws Exception
	 */
	void updateRegister(MyInfoPage info)  throws Exception;
	
	/**
	 * 查询个人可用余额
	 * @param dealerId
	 * @return
	 */
	Map<String, Object> getDealerCashBack(String dealerId);
	
	/**
	 * 查询个人返现明细
	 * @param dealer
	 * @return
	 */
	Map<String, Object> getDealerCashBackDetail(String dealer, int page, int rows,String startTime,String endTime);
	
	
	void toCashBack(PageMode model) throws Exception;
	
	
	List<Map<String , Object>> getBankCard(String dealer);
	
	
	List<Map<String , Object>> getApplyDetail(String dealer);
	
	/**
	 * @author felx
	 * @describe 年度账单
	 * @param date 时间
	 * @param dealerId
	 * @return
	 */
	List<Map<String , Object>> getAnnualBill(String date, String dealerId);

}
