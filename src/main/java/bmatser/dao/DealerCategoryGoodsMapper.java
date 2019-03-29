package bmatser.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface DealerCategoryGoodsMapper {
	
	List findStoreCateGoods(@Param("cateNo")String id,@Param("alias")String alias);

	List findStoreCateGoodsByCateId(@Param("alias")String alias,@Param("cateNo")String cateId,@Param("page") int page,@Param("rows") int i);

	int findStoreCateGoodsByCateIdCount(@Param("alias")String alias,@Param("cateNo")String cateId);

	String findCateByCateNo(@Param("alias")String alias, @Param("cateNo")String cateNo);
}
