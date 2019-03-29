package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.model.GoodsActivity;

public interface GoodsActivityMapper {

	List findGoodsActivity(@Param("activityChannel")Integer activityChannel);
	
	List findGoodsActivitySales(@Param("goodsActivityId")int goodsActivityId, @Param("cateId")Integer cateId, @Param("priceOrder")String priceOrder, @Param("rateOrder")String rateOrder, @Param("page")int page, @Param("rows")int rows,@Param("activityChannel")String activityChannel);
	
	Long countGoodsActivitySales(@Param("goodsActivityId")int goodsActivityId, @Param("cateId")Integer cateId,@Param("activityChannel")String activityChannel);
	
	List findGoodsActivityCategory(@Param("goodsActivityId")int goodsActivityId);
	
	List findGoodsActivityFullcut(@Param("sellerGoodsIds")List sellerGoodsIds);
	
	Map<String, Object> findGoodsActivityTime(@Param("id")Integer id);
	
	List findMallActivityGoods(@Param("activityChannel")String activityChannel);

	List findGoodsActivityByAlias(String alias);

	Map findFullCut(String id);

	String findFullgiveDesc(@Param("sellerGoodsId")String id);

	List getFloor();

	List findFloorActivity(@Param("floorId")String floorId);
	
}