package bmatser.service;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.pageModel.PaymentBillPage;

public interface AddPaymentBillServiceI {

	void savePaymentBill(PaymentBillPage paymentBillPage) ;
	
	Map<String, Object> findPayOrderById(Long id);
	
	void updatePaymentBill(PaymentBillPage paymentBillPage);
	
	int savePaymentBillParam(PaymentBillPage paymentBillPage) throws Exception;

}
