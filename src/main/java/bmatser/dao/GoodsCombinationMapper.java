package bmatser.dao;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


public interface GoodsCombinationMapper {

	List<Map<String,Object>> selectGoodsCombination(@Param("sellerGoodsId") String sellerGoodsId);

	List selectPackagePrice(@Param("idList") List<Map<String, Object>> list);
	
	List selectPackageGoods(@Param("idList") List<Map<String, Object>> list);

	List selectCombination(@Param("idList") List<Map<String, Object>> list);

}
