package bmatser.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


import bmatser.model.OrderCountModel;
import bmatser.pageModel.MakeOrderPage;
import bmatser.pageModel.ShoppingCartPage;
import bmatser.pageModel.util.MakeOrderPageUtil;

public interface ShoppingCartI {

	int addShoppingCart(ShoppingCartPage shoppingCartPage);
	
	List findShoppingCart(int buyerId);
	
	MakeOrderPageUtil findShoppingCartByIds(int buyerId, String ids, String isCash, String addrId) throws Exception;
	
	/**
	 * @author felx
	 * @describe 获取Saas购物车
	 * @param buyerId 零售商
	 * @param ids 购物车Id
	 * @param isCash 是否使用现金券 默认1
	 * @param addrId 地址Id 
	 * @return
	 * @throws Exception
	 */
	OrderCountModel findSaasShoppingCart(int buyerId, String ids,String isCash,String addrId) throws Exception;
	/**
	 * 购物车添加商品
	 * @param shoppingCart 购物车对象
	 * @return
	 */
	Integer addGoods(ShoppingCartPage shoppingCart)  throws Exception;
	
	/**
	 * 商城购物车添加商品
	 * @param shoppingCart 购物车对象
	 * @return
	 */
	void editOrderCartMall(ShoppingCartPage shoppingCart) throws Exception ;
	
	/**
	 * 获取商城购物车列表
	 * @author felx
	 * @date 2017-12-28
	 */
	List findShoppingCartMall(int buyerId);
	
	/**
	 * 根据id集合获取商城购物车商品列表
	 * @author felx
	 * @date 2017-12-28
	 */
	Map<String,Object> findShoppingCartByIdsMall(int buyerId, String ids,String addressId) throws Exception;
	

	
	/**
	 * 获取会员购物车商品总数
	 * @author felx
	 * @date 2016-1-7
	 */
	Integer getCartNumById(Integer buyerId) throws Exception ;
	
	/**
	 * 删除购物车商品
	 * @author felx
	 * @date 2016-1-7
	 */
	void delCart(Integer id) throws Exception;

	void batchDelcart(String[] strings);
	/**
	 * 批量更新购物车
	 * @param valueOf
	 * @param parameterMap
	 * @throws Exception
	 */
	void batchaAddCart(String cartIds, String nums) throws Exception;

	void delCart(String cartId);
	void deleteCart(String cartId,String dealerId);

	double getCash(Integer buyerId,String cartId);

	/**
	 * @author felx
	 * @describe 获取购物车列表
	 * @param buyerId 零售商Id 
	 * @return
	 */
	List<Map<String, Object>> findShoppingCartList(Integer buyerId);

	/**
	 * @author felx
	 * @describe 套餐加入购物车
	 * @param packageId 套餐Id 
	 * @param dealerId 零售商Id
	 * @param num 加入数量
	 */
	void addPackage(String packageId, String dealerId, int num);

	/**
	 * @author felx
	 * @describe 获取购物车列表
	 * @param dealerId
	 * @return
	 */
	List<Map<String, Object>> findAppShoppingCartList(Integer dealerId);





}
