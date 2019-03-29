package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.model.OrderGoods;
import bmatser.model.Produce;

public interface OrderGoodsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderGoods record);

    int insertSelective(OrderGoods record);

    OrderGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderGoods record);

    int updateByPrimaryKey(OrderGoods record);

	List<OrderGoods> selectGoodsByOrderId(String id);
	
	List<Produce> selectGoodsByOrderIdForErp(String id);
	/**
	 * 订单列表商品显示
	 * @param id
	 * @return
	 */
	List<Map> getGoodsByOrderId(String id);
	
	/**
	 * 订单商品
	 */
	List<Map> getGoods(@Param("buyerId")Integer buyerId,@Param("status")Integer status, @Param("page")int page, @Param("rows")int rows, @Param("query")Map querymap);

	
	/**
	 * 商城 订单列表商品显示 2016.04.26
	 * @param id
	 * @return
	 */
	List<Map> selectSellerGoodsByOrderId(String id);

	void insertOrderGoods(@Param("list")List<OrderGoods> goods);

	void updateGoodsRebate(@Param("orderId")String orderId);

	List<Produce> selectAllGoodsByOrderIdForErp(String id);

	List<String> selectNoSyncGoodsByOrderIdForErp(@Param("oid") String id);


	OrderGoods selectByOrderIdAndGoodId(@Param("orderId") Long orderId,@Param("goodId") Long goodId);

    /**
     * create by yr on 2018-10-19
     * 增加行政区划名称字段
     */
    String findNameById(String id);
}