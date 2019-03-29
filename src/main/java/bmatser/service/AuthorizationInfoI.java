package bmatser.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import bmatser.model.BillInfo;
import bmatser.model.RepaymentFeedback;
import bmatser.pageModel.PageMode;

public interface AuthorizationInfoI {
    //信贷业务查询:授信信息查询
	Map getAuthorizationInfo(String dealerId) throws Exception;
	//信贷业务查询:出账明细查询
	void updateChuZhangDetails(String str);
	//信贷业务查询:还款信息查询
	/*void getReimbursementInfo(String repaymentNo);*/
	
	Map<String, Object> getJtlDealerBillDetails(PageMode pageMode, int page, int rows) throws Exception;
	
	void toReimbursement(String accountNo, String bankSerialNo) throws Exception ;
	
	void repaymentFeedback(RepaymentFeedback repaymentFeedback);
	
	Map getJtlHandleRecord(Integer dealerId, String startTime,String endTime, int page, int rows);

}
