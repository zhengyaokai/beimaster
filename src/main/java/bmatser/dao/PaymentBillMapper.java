package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.model.PaymentBill;
import bmatser.model.PaymentBillOrder;
import bmatser.pageModel.PaymentBillPage;

public interface PaymentBillMapper {
	
	PaymentBill findById(String Id);
	
	void save(PaymentBill paymentBill);
	int saveParam(PaymentBill paymentBill);

	void savePaymentBillOrder(PaymentBillOrder paymentBillOrder);
	int savePaymentBillOrderParam(PaymentBillOrder paymentBillOrder);
	
	void updatePayStatus(@Param("id")String id,@Param("check")int check);

	void update(PaymentBill paymentBill);
	int updateInt(PaymentBill paymentBill);
	/**
	 * 根据订单id查询付款单id   
	 * @param orderId
	 * @return
	 */
	String selectPaymentBillOrderId(String orderId);
	//删除PaymentBill表 
	void delete(@Param("selectPaymentBillId")String selectPaymentBillId);

	//根据对账单id 删除 payment_bill_order （付款单对应订单关系表）
	void delete1(@Param("selectPaymentBillId")String selectPaymentBillId);
	
	List<PaymentBillPage> getPayByPage(PaymentBillPage paymentBillPage);
	
	int countPayByPage(PaymentBillPage paymentBillPage);
	
	void deleteById(@Param("id")String id);
	
	Map<String, Object> findPayOrderById(@Param("id")Long id);

	void updateDiscountAmount(PaymentBill paymentBill);

	Long findOrderIdById(@Param("id")String id);

	void updateOrderAmount(PaymentBill paymentBill);
	
	void deletePayOrderByOrderId(@Param("id")Long id);

	List<Map<String, Object>> findOpxVal(@Param("i") int i);
	
	String getsaleAmountById(@Param("id")Long id);
	
	Map<String, Object> findOrderById(@Param("id")Long id);
	
	/**
	 *保存支付宝转账信息
	 *@author jxw 
     *@date 2016.03.16 
	 */
	int updateOrderAlipay(PaymentBill paymentBill);

	String findIdByOrderId(@Param("orderId")String orderId);

	void savePaymentBillIByOrderId();

	void updatePaymentBillIByBalance(@Param("orderId")String orderId);

	void delBalanceByOrderId(@Param("orderId")String orderId);
}
