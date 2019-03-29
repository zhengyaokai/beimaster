package bmatser.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import bmatser.model.*;
import org.apache.ibatis.annotations.Param;

import bmatser.pageModel.OrderDealPage;

public interface OrderInfoMapper {

    /**
     * create by yr on 2018-10-19 获得订单各类订单数量
     */
    List<Map<String ,Object>> getOrderNum(@Param("dealerId")Integer dealerId);

    List<String> getOrderForIos(@Param("dealerId")Integer dealerId);

    String getCheckUser(@Param("dealerId")String dealerId);

	List findCountByStatus(@Param("buyerId")Integer buyerId);
	
	List findOrders(@Param("buyerId")Integer buyerId, @Param("status")Integer status, @Param("page")int page, @Param("rows")int rows);
	
	Long countOrders(@Param("buyerId")Integer buyerId, @Param("status")Integer status);
	
	List getOrderDetail(@Param("orderId")Long orderId,@Param("dealerId")String dealerId);
	
	int cancelOrder(@Param("orderId")Long orderId);
	
	//取消订单 更新dealer 表中的cash字段  20160720
	int updateDC(@Param("orderId")Long orderId);

	//删除订单
    int deleteOrder(@Param("orderId")Long orderId);

	//新增记录到dealer_cash表中  20160720
	int insertDealerCash(OrderInfo order);
	
	int confirmReceipt(@Param("orderId")Long orderId);
	
	Map getOrderById(@Param("orderId")Long orderId);
	Map getOrderInfoByIdValue(@Param("orderId")Long orderId);
	
	int addOrderInfo(OrderInfo orderInfo);

	Trade saveOrderToERP(String id);

	OrderInfo findById(Long id);

	//商城首页显示订单信息   add--20160603
	List findMallOrderMessage();
	
	void update(OrderInfo order);

	OrderInfo findBychildId(@Param("id")String id);
	
	/**
	 * 根据id获取订单详情
	 * @author felx
	 * @date 2017-12-31
	 */
	OrderInfo getOrderInfoById(@Param("orderId")Long orderId);

	/**
	 * 根据第三方id（例如ERP）获取订单详情
	 */
	OrderInfo getOrderInfoByThirdId(@Param("thirdId")String thirdId);

	/**
	 * 根据父订单id获取订单详情
	 * @author felx
	 * @date 2016-1-4
	 */
	List<OrderInfo> getOrderInfoByParentId(@Param("originalOrderId")String originalOrderId);
	/**
	 * 根据零售商ID查询订单列表
	 * @param buyerId
	 * @param status
	 * @param page
	 * @param rows
	 * @return
	 */
	List<Map> getOrderListByDealerId(@Param("buyerId")Integer buyerId, @Param("status")Integer status, @Param("page")int page, @Param("rows")int rows, @Param("query")Map querymap);
	/**
	 * 根据零售商ID查询订单列表数量
	 * @param buyerId
	 * @param status
	 * @param querymap 
	 * @param page
	 * @param rows
	 * @return
	 */
	int getOrderListByDealerIdCount(@Param("buyerId")Integer buyerId, @Param("status")Integer status,@Param("query") Map querymap);
	int getOrderListByDealerIdCountVal(Map querymap);
	
	OrderInfo findByIdAndBuyerId(@Param("orderId")String orderId, @Param("buyerId")String buyerId);

	Map findServiceAndManager(@Param("buyerId")Integer buyerId);
	/**
	 *保存支付页面线下支付信息
	 *@author jxw 
     *@date 2016.03.16 
	 */
	int updateOrderOfflinePay(OrderInfo orderInfo);
	/**
	 *销售合同
	 *@author jxw 
     *@date 2016.03.17 
	 */
	List selectContract(@Param("id") Long id,@Param("IsNull") String IsNull);
	/**
	 * 根据订单号查询商品列表
	 * add 2016 06 29
	 * @param id
	 * @author felx
	 * @return
	 */
	List selectOrderInfoGoodsList(@Param("id") Long id);
	/**
	 *查询crm_customer_linkman表的status是否存在
	 *@author jxw 
     *@date 2016.04.20 
	 */
	String selectCrmCusLikmanStatus(String orderId);
	/**
	 * 根据订单id查询订单信息以及买方供方信息
	 * add 2016 06 29
	 * @param id
	 * @author felx
	 * @return
	 */
	Map<String, Object> findOrderInfoById(@Param("id")Long id);

	Map createContract(@Param("id")String id);
	/**
	 *查询供应商详情
	 *@author
	 */
	List<Map> selectOrderDealByDealerId(Map querymap);

	void updateDealerCash(@Param("cash")Integer cash,@Param("id")Integer id);
	/**
	 *查询打印的供应商详情 
	 *@author
	 */
	List getSellerOrderDetail(@Param("orderId")Long orderId,@Param("dealerId")String dealerId);

	Map findOrderDetail(@Param("orderId")String orderId,@Param("dealerId")String dealerId);

	List<Map> findGoodsBytype(@Param("orderId")String orderId,@Param("type")int i);

	List<Map> getOrderListByOrderDealPage(OrderDealPage orderPage);
	
	int getOrderListByOrderDealPageCount(OrderDealPage orderPage);
	
	EditOrder getOrderInfoByEdit(@Param("id")String id, @Param("dealerId")String dealerId);
	
	List<EditOrderGoods> getOrderGoodsByEdit(@Param("id")String id);

	List<EditGoods> getGoodsByEdit(@Param("idList")String[] sellerGoodsId,@Param("dealerId")String dealerId);

	BigDecimal getFreightAmount(@Param("idList")List<EditOrderGoods> newGoods);

	BigDecimal getFullCutAmount(@Param("idList")List<EditOrderGoods> newGoods);

	void updateEditOrder(EditOrder o);

	void saveEditOrderGoods(EditOrderGoods editOrderGoods);

	void updateEditOrderGoods(EditOrderGoods editOrderGoods);
	void updateOrderReturnCash(EditOrder o);
	void updateOrderNewCash(EditOrder o);
	void delOrderGoods(EditOrder o);

	int findOrderBeforeCash(@Param("id")String orderId);

	void delCashRecord(@Param("id")String orderId);

	void insertCashRecord(@Param("id")String orderId);

	void updateBalanceByType(@Param("type")int i,@Param("orderId") String orderId);

	double getOrderBalance(@Param("dealerId")String dealerId,@Param("orderId")String orderId);

	void saveOrderByBalance(@Param("orderId")String orderId);

	List selectPaymentAmountAndMethod(@Param("orderId") long orderId);

	List getOrderCountByDealerId(@Param("dealerId") Integer dealerId);
	
	List getOrderCount(@Param("dealerId") Integer dealerId);

	List<Map> getOrderInfoFirst(@Param("buyerId")Integer buyerId,@Param("status")Integer status);

	Map<String, Object> findOrderAddress(String id);

	Trade saveScoreToERP(String id);

	void updateGift(@Param("id")String id, @Param("fid")String fid);

	Map<String, Object> findOrderInterest(@Param("orderId") long orderId);
	
	void updateOrderInterest(@Param("orderId") long orderId);

	void saveOrderInterest(@Param("orderId")String orderId,@Param("amount")String value);

	List<Map<String, Object>> findGoodsInfo(@Param("orderId")String orderId);

	void updateOrderRebate(@Param("orderId")String orderId);

	void cancelEzhuOrder(@Param("orderId")String id);

	Timestamp getOrderTime(@Param("orderId") Long orderId);

	PayInfo getToPayInfo(@Param("orderId")String orderId, @Param("dealerId")String string);

	BigDecimal getRealAmount(Long parseLong);

	Integer getRateType(String originid);

	List<OrderExpressInfo> getOrderLogistics(@Param("orderId")String orderId);


	List<OrderExpressInfo> getOrderPackageLogistics(@Param("orderId")String orderId);

	List<OrderReceiptInfo> getInvoiceLogistics(@Param("orderId") String orderId);
	
	
	void addOrderInfoSave(OrderInfoSave orderinfo);
	
	List<GoodsPackage> getGoodsPackageInfo(String orderId);

	OrderInfo findByOrderId(@Param("orderId")String orderId);

	int findIsFirstWxOrder(@Param("dealerId")String dealerId);

	OrderInfo getAmount(Long oid);

	OrderToERP saveOrderToJQERP(String id);

	ExpressRequestWapper wapperExpressRequestByOrderId(Long oid);


	List<String> getOrdersWithThirdIdAndNoSent();

}