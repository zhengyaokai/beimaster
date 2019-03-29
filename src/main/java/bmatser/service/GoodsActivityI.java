package bmatser.service;

import java.util.List;
import java.util.Map;

public interface GoodsActivityI {

	List findGoodsActivity(Integer activityChannel);
	
	Map<String, Object> findGoodsActivitySales(int goodsActivityId, Integer cateId, String priceOrder, String rateOrder, int page, int rows,String activityChannel);
	
	List findGoodsActivityCategory(int goodsActivityId);
	
	double findGoodsActivityFullcut(Map sellerGoodsIds);
	
	/**
	 * 获取商城特卖商品
	 * @author felx
	 * @date 2017-12-23
	 */
	List findMallActivityGoods(String activityChannel);

	List getFloorGoodsActivity();
}
