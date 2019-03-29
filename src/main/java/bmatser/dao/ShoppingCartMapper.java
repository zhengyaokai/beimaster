package bmatser.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.model.FindShoppingCartByIdsMall;
import bmatser.model.FindShoppingCartByType;
import bmatser.model.GoodsPackage;
import bmatser.model.ShoppingCart;
import bmatser.pageModel.MakeOrderGoodsPage;
import bmatser.pageModel.ShoppingCartPage;
import bmatser.pageModel.ShoppingCartShowPage;
import bmatser.pageModel.util.MakeOrderGoodsPageUtil;

public interface ShoppingCartMapper {

	int addShoppingCart(ShoppingCartPage shoppingCartPage);
	
	List findShoppingCart(@Param("buyerId")int buyerId);
	
	List<MakeOrderGoodsPageUtil> findShoppingCartByIds(@Param("buyerId")int buyerId, @Param("ids")List ids);
	/**
	 * @author felx
	 * @describe 按类型查询购物车
	 * @param buyerId 零售商ID
	 * @param ids 购物车Id 
	 * @param type 类型 0普通 1 团购 3 套餐
	 * @return
	 */
	List<MakeOrderGoodsPageUtil> findCartBytype(@Param("buyerId")int buyerId, @Param("ids")List ids,@Param("type")int type);

	ShoppingCart getShoppingCartByPageId(@Param("buyerId")Integer buyerId, @Param("sellerGoodsId")String sellerGoodsId);
	
	int findShoppingCartFreight(@Param("ids")List ids);
	
	ShoppingCart findShoppingCartById(@Param("id")String id);

	void save(ShoppingCart cart);

	void update(ShoppingCart cart);

	String findRateByGoodsNo(String string);
	
	/**
	 * 同一店铺下购物车金额
	 * @param cartIds
	 * @return
	 */
	List<Map<String, Object>> findStoreCartAmount(@Param("cartIds")String[] cartIds);

	
	/**
	 * 获取商城购物车列表
	 * @author felx
	 * @date 2017-12-28
	 */
	List findShoppingCartMall(@Param("buyerId")int buyerId,@Param("sellerId")int sellerId);
	
	/**
	 * 获取商城会员购物车所含店铺列表
	 * @author felx
	 * @date 2017-12-28
	 */
	List findShoppingCartShop(@Param("buyerId")int buyerId,@Param("cartIds") String[] cartIds);

	
	/**
	 * 获取商城订单购物车列表
	 * @author felx
	 * @date 2017-12-28
	 */
	List<FindShoppingCartByIdsMall> findShoppingCartByIdsMall(@Param("buyerId")int buyerId, @Param("ids")List ids,@Param("sellerId")int sellerId);
	
	/**
	 * 删除购物车
	 * @author felx
	 * @date 2017-12-30
	 */
	void delete(@Param("id")int id);
	
	/**
	 * 根据id获取购物车
	 * @author felx
	 * @date 2017-12-30
	 */
	ShoppingCart getCartById(@Param("cartId")Integer cartId);
	
	/**
	 * 获取会员购物车商品总数
	 * @author felx
	 * @date 2016-1-7
	 */
	Integer getCartNumById(@Param("buyerId")Integer buyerId);

	void batchDelcart(@Param("ids")String[] cartIds);

	ShoppingCart getShoppingCartByPageGroupId(@Param("buyerId")Integer buyerId, @Param("sellerGoodsId")String sellerGoodsId,@Param("grouponActivityId")String grouponActivityId);

	/**
	 * @author felx
	 * @describe 根据类型获取购物车
	 * @param buyerId 零售商Id
	 * @param i 类型 0 普通 1 团购 3套餐
	 * @return
	 */
	List<FindShoppingCartByType> findShoppingCartByType(@Param("buyerId")Integer buyerId,@Param("type")int i);

	int isExistJdGoods(@Param("ids")String[] split);



	/**
	 * @author felx
	 * @describe 套餐加入购物车，存在则更新不存在就加入
	 * @param packageId 套餐Id
	 * @param dealerId 零售商Id
	 * @param num 数量
	 * @param uNum 更新数量
	 */
	void addOrUpdatePackageCart(@Param("packageId")String packageId,
			@Param("dealerId")String dealerId,@Param("num")int num,
			@Param("uNum")Integer uNum);

	/**
	 * @author felx
	 * @describe  加入购物车,存在就更新不存在就加入
	 * @param id 商品Id
	 * @param dealerId 零售商Id
	 * @param num 数量
	 * @param uNum 更新数量
	 * @param activityType 活动类型
	 * @param activityId 活动Id
	 */
	void addOrUpdateCart(@Param("sellerGoodsId")String id,
			@Param("dealerId")String dealerId,@Param("num")int num, 
			@Param("uNum")Integer uNum,@Param("activityType")int activityType,
			@Param("activityId")String activityId);

    /**
     * 新的折扣问题解决
     * @param id
     * @param dealerId
     * @param salePrice
     * @param num
     * @param uNum
     * @param activityType
     * @param activityId
     */
    void addOrUpdateCartByDiscount(@Param("sellerGoodsId")String id,
                                   @Param("dealerId")String dealerId,@Param("salePrice")BigDecimal salePrice, @Param("num")int num,
                                   @Param("uNum")Integer uNum,@Param("activityType")int activityType,
                                   @Param("activityId")String activityId);
	/**
	 * @author felx
	 * @describe 获取套餐信息
	 * @param buyerId 零售商Id
	 * @return
	 */
	List<GoodsPackage> getShoppingCartPackageInfo(@Param("buyerId")Integer buyerId);
	
	/**
	 * @author felx
	 * @describe 获取套餐信息
	 * @param ids 购物车Id集合
	 * @return
	 */
	List<GoodsPackage> getPackageInfoByCartId(@Param("ids")List ids);

	/**
	 * @author felx
	 * @describe 更新购物车，会根据购物车类型来进行更新
	 * @param id 购物车Id
	 * @param dealerId 零售商Id
	 * @param num 数量
	 * @param uNum 更新数量
	 */
	void updateCartByType(@Param("cartId")Integer id,
			@Param("dealerId")String dealerId,
			@Param("num")int num,
			@Param("uNum")Integer uNum);

	/**
	 * @author felx
	 * @describe 获取购物车套餐商品
	 * @param dealerId 零售商Id 
	 * @param packageList 套餐集合
	 * @return
	 */
	List<MakeOrderGoodsPageUtil> findPackageShoppingCartByIds(@Param("dealerId")Integer dealerId,
			@Param("packageList")List<GoodsPackage> packageList);

	void deleteCart(@Param("cartId")String id,@Param("dealerId")String dealerId);

    /**
     * Create by Yu on 2018-09-17
     */
    //新的折扣方式(根据商品)
    BigDecimal getDiscountByGoodsNo(@Param("goodsNo")String goodsNo, @Param("dealerId")String dealerId);

    //按照系列搜索
    BigDecimal getDiscountBySeries(@Param("series")String series,@Param("dealerId")String dealerId);

    //根据商品查系列
    String  getSeries(@Param("goodsNo")String goodsNo);
    //根据dealerName查找dealerId
    String  getDealerId(@Param("dealerName")String dealerName);

}