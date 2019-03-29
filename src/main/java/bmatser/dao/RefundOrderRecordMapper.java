package bmatser.dao;

import org.apache.ibatis.annotations.Param;

public interface RefundOrderRecordMapper {

	String findIdByOrderId(@Param("orderId")String orderId);

	int addBackware(@Param("id")String id, @Param("refundNo")String refundNo);

	int updatePutware(@Param("id")String id, @Param("refundNo")String refundNo);

}
