package bmatser.dao;

import org.apache.ibatis.annotations.Param;

public interface DealerBalanceRecordMapper {

	void delRecordByOrderId(@Param("orderId")String orderId);

	void insertRecordByOrderId(@Param("orderId")String orderId);

}
