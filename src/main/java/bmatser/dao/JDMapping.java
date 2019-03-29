package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface JDMapping {

	String findOrderAddrJson(@Param("id")String id);
	
	
	Map<String, String> findOrderAddr(@Param("id")String id);

	Map<String, String> findInv(@Param("id")String id);

	List<Map<String, String>> findGoods(@Param("id")String id);


	void saveJDOrderId(@Param("id")String id, @Param("jdOrderId")String jdOrderId);


	List<Map<String, String>> findArea(@Param("id")String id);


	String findSkuId(@Param("sellerGoodsId")String sellerGoodsId);

}
