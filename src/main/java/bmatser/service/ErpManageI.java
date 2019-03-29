package bmatser.service;

import bmatser.model.OrderToERP;
import bmatser.pageModel.OrderInfoErpPage;
import bmatser.pageModel.PageMode;

import java.util.Map;

public interface ErpManageI {

	/**
	 * 增加订单同步到ERP中
	 * @param id 订单Id
	 * @return
	 */
	String saveOrderToERP(String id)  throws Exception;
	
	/**
	 * 更新订单状态
	 * @param order
	 * @return 
	 */
	void updateOrder(OrderInfoErpPage order)  throws Exception;
	
	/**
	 * 查询商品库存
	 * @param id
	 * @param sessionKey
	 * @return
	 */
	int getGoodsStock(String id, String sessionKey) throws Exception ;
	/**
	 * 反馈订单同步到ERP中
	 * @param id 订单Id
	 * @param sessionKey ERP的KEY
	 * @return 
	 */
	void saveOrderToERP(Long id, String sessionKey)  throws Exception;
	

	String getBatchGoodsStock(PageMode model, String sessionKey) throws Exception;

	void updateRefundRecord(String channel, String orderId, String refundNo) throws Exception;

	String saveScoreToERP(String id, String sessionKey) throws Exception;

	void cancelOrder(String id);

	void delThirdOrder(String orderId, String sessionKey) throws Exception;

	String getLogics(String startTime, String endTime, String sessionKey);

	String getInvoice(String startTime, String endTime, String sessionKey);

	String financesaveOrderToERP(String id, String sessionKey) throws Exception;

	void saveGoodToERP();

	Map<String,Object> saveCustomerToERP(String dealerId,OrderToERP trade) throws Exception;

	Map<String,Object> queryERPOrderStockOut(String erpOrderNo) throws Exception;

	String queryERPOrder(String erpOrderNo) throws Exception;

	Map<String, Object> updateOrderLogis(String gdb0rderId) throws Exception;

	void updateOrdersLogisJob() throws Exception;

	void updateErpGoodSku();
}
