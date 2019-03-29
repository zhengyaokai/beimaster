package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.model.BillInfo;
import bmatser.model.RepaymentFeedback;

public interface AuthorizationInfoMapper {

	
	/*int getChuZhangDetails(@Param("accountNo") String accountNo);*/

	/*int getReimbursementInfo(@Param("customerCode") String customerCode);*/

	void insert(BillInfo billInfo);

	void update(BillInfo billInfo);

	List getJtlDealerBillDetails
	(@Param("dealerId")String dealerId,@Param("status") String status,@Param("expiryStartTime") String expiryStartTime,
			@Param("expiryEndTime") String expiryEndTime,@Param("payStartTime") String payStartTime,
			@Param("payEndTime") String payEndTime, @Param("page") int page,@Param("rows") int rows);

	int getJtlDealerBillDetailsCount(@Param("dealerId")String dealerId,@Param("status") String status,
			@Param("expiryStartTime") String expiryStartTime,
			@Param("expiryEndTime") String expiryEndTime,@Param("payStartTime") String payStartTime,
			@Param("payEndTime") String payEndTime);

	Map getJtlDealerBillInfo(@Param("accountNo") String accountNo);

	void updateJtlDealerReimbursement(RepaymentFeedback repaymentFeedback);

	void saveJtlDealerReimbursement(Map<String, Object> m);

	List getStatusCount(@Param("dealerId") String dealerId);

	void insertJtlHandleRecord(Map<String, Object> m);

	List getJtlHandleRecord(@Param("dealerId") Integer dealerId,@Param("startTime")  String startTime,@Param("endTime") String endTime,
			@Param("page") int page,@Param("rows") int rows);

	int getJtlHandleRecordCount(@Param("dealerId") Integer dealerId,@Param("startTime")  String startTime,@Param("endTime") String endTime);
	
	//查询待还款总金额和逾期总金额
	List<Map<String, Object>> getPayTotal(@Param("dealerId") String dealerId);
}
