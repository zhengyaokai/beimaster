package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.model.JnOrderDetails;
import bmatser.model.JnPaid;

public interface KingnorMapper {

	List<Map<String, Object>> getOrderList(@Param("customerCode")String customerCode,
			@Param("orderStartDate")String orderStartDate,@Param("orderEndDate") String orderEndDate);

	int saveCustomerWarning(Map<String, Object> map);
	
	int insertJnPaid(JnPaid jp);
	
	int insertJnOrderDetails(JnOrderDetails jod);

	int checkIsExist(String paidNo);

	void deleteLoanInform(String paidNo);

	void deleteLoanInformDetails(String paidNo);

}
