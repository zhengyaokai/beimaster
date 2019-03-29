package bmatser.service;

public interface GbeansServiceI {
	
	/**
	 * 下单获得金豆
	 * @param orderId
	 */
	void toOrderBeans(String orderId);
	/**
	 * 购物车可获得金豆
	 * @param cartId
	 * @return
	 */
	int getCartsBeans(String[] cartId);
	
	/**
	 * 用户有多少金豆
	 * @param dealerId
	 * @return
	 */
	int getUseBeans(String dealerId);
	/**
	 * 使用金豆
	 * @param orderId
	 * @param dealerId
	 */
	void toUserBeans(String orderId,String dealerId);
	
	int getDealerBeanByOrder(String orderId);
	
	int getDealerBeanByOrder(String[] sellerGoodsId, String[] num);
	
	void cancelOrder(String orderId);
	void toNotUserBeans(String orderId, String dealerId);

}
