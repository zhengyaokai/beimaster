package bmatser.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import bmatser.model.Dealer;
import bmatser.pageModel.RegisterPage;

public interface DealerMapper<T> {
	
	int add(RegisterPage registerPage);
	
	int addScoreById(@Param("buyerId")Integer buyerId, @Param("score")Integer score,@Param("rankScore")Integer rankscore);

	Map findLoginInfo(String dealerId);
	
	//查询个人等级，等级积分以及距离升级还需多少等级积分
	Map findDealerRankAndRankScore(String dealerId);
	//查询dealer表总人数
	int countDealer();

	int update(T t);
	
	int updatePerson(T t);
	
	int updateById(T t);
	
	int updateScoreById(Map mp);
	
	Integer selectScoreRemain(Integer dealerId);
	
	Map findById(String dealerId);
	
	int updateValue(Dealer de);


	int IsExistName(String dealerName);

	void updateRank(@Param("dealerId")Integer dealerId,@Param("rank") int i);

	int IsExistCheck(@Param("dealerId")Integer dealerId);

	int delCrmCustomer(@Param("dealerId")Integer dealerId);
	
	int insertDealerRankScore(@Param("score")Integer score,@Param("orderId")Long orderId,@Param("dealerId")Integer dealerId);


	void returnDealerBalance(@Param("orderId")String orderId);

	void updateBalanceByOrderId(@Param("orderId")String orderId);

	List<Map<String, Object>> getDealerCashBackDetail(@Param("dealerId") String dealerId,@Param("page") int page,@Param("rows") int rows,@Param("startTime") String startTime,@Param("endTime") String endTime);
	
	int getDealerCashBackDetailCount(@Param("dealerId") String dealerId,@Param("startTime") String startTime,@Param("endTime") String endTime);
	
	Map<String, Object> getDealerCashBack(@Param("dealerId")String dealerId);

	int isExistBankCard(@Param("dealerId")String dealerId,@Param("cardId")String cardId);
	
	void addWithdrawals(@Param("dealerId")String dealerId,@Param("cardId")String cardId,@Param("amount")BigDecimal amount);

	List<Map<String, Object>> getBankCard(@Param("dealerId")String dealerId);

	List<Map<String, Object>> getApplyDetail(@Param("dealerId")String dealerId);

	void delDealerName(@Param("dealerId")Integer dealerId);

	String getOpenId(@Param("orderId")String orderId);
	String getSaasOpenId(@Param("orderId")String orderId);

	void delCrmCustomerInfo(@Param("dealerId")Integer dealerId);

	void delDealerLicense(@Param("dealerId")Integer dealerId);

	int IsExistDealerName(@Param("dealerName")String dealerName,@Param("dealerId") String dealerId);

	String checkReason(@Param("dealerId")String dealerId);


	int isRebate(@Param("dealerId")String dealerId);

    //saas个人信息增加查询专属商务经理信息
	Map getBusinessManagerInfo(@Param("dealerId")String dealerId);

	Map checkInfo(@Param("dealerId")String dealerId);

	void updateSaasOpenId(@Param("openId") String openId,@Param("dealerId") String dealerId);

	/**
	 * @author felx
	 * @describe 年度账单
	 * @param date 时间
	 * @param dealerId 用户ID
	 * @return
	 */
	List<Map<String, Object>> getAnnualBill(@Param("date")String date,@Param("dealerId")String dealerId);

	int getRegChannel(@Param("dealerId")String dealerId);

}