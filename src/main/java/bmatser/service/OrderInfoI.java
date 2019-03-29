package bmatser.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import bmatser.model.EditOrder;
import bmatser.model.OrderInfo;
import bmatser.model.OrderInvoice;
import bmatser.pageModel.OrderDealPage;
import bmatser.pageModel.OrderInfoPage;
import bmatser.pageModel.PageMode;
import bmatser.util.LoginInfo;

public interface OrderInfoI {

    /**
     * for Ios 获取订单类型的数量
     */
    Map<String,Object> getOrderNumForIos(Integer dealerId) throws Exception;

    /**
     *
     * @param buyerId
     * @return
     * @throws Exception
     */
    String checkUser(String dealerId);

	List findCountByStatus(Integer buyerId) throws Exception;
	
	Map<String, Object> findOrders(Integer buyerId, Integer status, Integer page, Integer rows, String orderId,String queryType, String startTime, String endTime, String logisticsCode, int i)  throws Exception;
	
	Map<String, Object> findMallOrders(Integer buyerId, Integer status, Integer page, Integer rows, String orderId, String startTime, String endTime, String logisticsCode, int i,String buyNo,String goodsName)  throws Exception;
	
	List getOrderDetail(Long orderId,String dealerId,String type);
	
	int cancelOrder(Long orderId) throws Exception ;
	
	int confirmReceipt(Long orderId)throws Exception;
	
	Map addOrderInfo(OrderInfoPage orderInfoPage) throws Exception;

    /**
     * 删除订单 2018-10-24 by yr
     */

    int deleteOrderI(Long orderId) throws Exception ;
	
	/**
	 * 保存商城订单
	 * @author felx
	 * @date 2017-12-30
	 */
	Map addMallOrderInfo(OrderInfoPage orderInfoPage) throws Exception;
	
	/**
	 * 根据id获取订单详情
	 * @author felx
	 */
	OrderInfo getOrderById(Long orderId);
	/**
	 * 根据订单id获取订单应付金额
	 * @author
	 */
	OrderInfo getOrderInfoByIdValue(Long orderId);
	/**
	 * 修改订单
	 * @author felx
	 */
	void editOrder(OrderInfo orderInfo);
	
	/**
	 * 支付完成后修改订单
	 * @author felx
	 */
	void editHandleOrder(OrderInfo orderInfo);

	void updateOrder(OrderInfoPage orderInfoPage);

	int cancelOrder(String orderId, String buyerId) throws Exception;
	/**
	 *保存支付页面线下支付信息
	 *@author jxw 
	 */
	int updateOrderOfflinePay(HttpServletRequest request,LoginInfo loginInfo) throws Exception;
	/**
	 *保存支付页面支付宝转账信息
	 *@author jxw 
	 */
	int updateOrderAlipay(HttpServletRequest request,LoginInfo loginInfo) throws Exception;
	/**
	 *销售合同
	 *@author jxw 
	 */
	Map selectContract(Long orderId) throws Exception;

	Map getContractById(String id)  throws Exception;
	/**
	 *Saas供应商订单列表 
	 *@author jxw 
	 */
	Map<String, Object> selectSellerOrderList(Integer dealerId,Integer page,HttpServletRequest request) throws Exception;

	Map<String, Object> getOrderDetailBySaas(String orderId, String dealerId);

	Map<String, Object> confirmOrderBySaas(String ids, String dealerId, String isCash, String addrId)  throws Exception;

	Map<String, Object> confirmOrderByApp(String ids, String dealerId, String isCash, String isBean, String addrId)  throws Exception;

	Map<String, Object> getOrderDetailByApp(String orderId, String dealerId);

	Map<String, Object> getOrderLIstByApp(OrderDealPage orderPage);

	EditOrder editOrderGoodsBySaas(PageMode page) throws Exception;

	void saveEditOrder(PageMode page) throws Exception;

	/**
	 * 根据订单号查询支付金额及支付方式 
	 * @param orderId
	 * @return
	 */
	List selectPaymentAmountAndMethod(long orderId) throws Exception;

	List getOrderCountByDealerId(Integer dealerId);
	
	List getOrderCount(Integer dealerId);

	Map<String, Object> getOrderInfoFirst(Integer buyerId,Integer stusus) throws Exception;

	OrderInvoice getOrderInvoiceDetils(Integer invId);

	Map<String, Object> findOrderInterest(Long orderId);

	void updateOrderInterest(Long orderId);

	void saveOrderInterest(Map<String, Object> interest, String orderId);

	void addAgainToBuy(String orderId, String dealerId) throws Exception;

	int tobuyJudge(String orderId, String dealerId);

	Timestamp getOrderTime(Long orderId);

	Map getOrderLogistics (String orderId) throws Exception;

	Map getInvoiceLogistics(String orderId) throws Exception;

	Map getOrderPackageLogistics(String orderId) throws Exception;
}
