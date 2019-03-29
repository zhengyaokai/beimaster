package bmatser.service.impl;

import bmatser.dao.OrderInfoMapper;
import bmatser.dao.PaymentBillMapper;
import bmatser.model.PaymentBillOrder;
import bmatser.pageModel.PaymentBillPage;
import bmatser.service.AddPaymentBillServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service("addPaymentBillService")
public class AddPaymentBillServiceImpl implements AddPaymentBillServiceI {
	
	@Autowired
	private OrderInfoMapper orderInfoMapper;
	@Autowired
	private PaymentBillMapper paymentBillMapper;

	@Transactional(rollbackFor=Exception.class)
	@Override
	public void savePaymentBill(PaymentBillPage paymentBillPage) {
//		OrderInfo order= orderInfoMapper.getOrderInfoById(paymentBillPage.getOrderId());
//		if(order != null && order.getId() != null){
//			paymentBillPage.setShouldAmount(order.getOrderAmount()==null?0 : order.getOrderAmount().doubleValue());
//			paymentBillPage.setPayStatus("0");
//			paymentBillPage.setPayType("2");
//			paymentBillPage.setStatus("1");
//			paymentBillPage.setPayTime(new Timestamp(System.currentTimeMillis()));
			paymentBillMapper.save(paymentBillPage);
			PaymentBillOrder paymentBillOrder = new PaymentBillOrder();
			paymentBillOrder.setOrderId(paymentBillPage.getOrderId());
			paymentBillOrder.setPaymentBillId(paymentBillPage.getId());
			paymentBillOrder.setStatus("1");
			paymentBillMapper.savePaymentBillOrder(paymentBillOrder);
//		}else{
//			throw new Exception("订单参数错误");
//		}
	}
	
	@Override
	public int savePaymentBillParam(PaymentBillPage paymentBillPage) throws Exception {
			int k=paymentBillMapper.saveParam(paymentBillPage);
			PaymentBillOrder paymentBillOrder = new PaymentBillOrder();
			paymentBillOrder.setOrderId(paymentBillPage.getOrderId());
			paymentBillOrder.setPaymentBillId(paymentBillPage.getId());
			paymentBillOrder.setStatus("1");
			int m=paymentBillMapper.savePaymentBillOrderParam(paymentBillOrder);
			if(k==0||m==0){
				throw new RuntimeException("保存付款单和订单关系失败");
			}
			return 1;
			
	}
	
	public void updatePaymentBill(PaymentBillPage paymentBillPage){
		paymentBillMapper.update(paymentBillPage);
	}

	
	public Map<String, Object> findPayOrderById(Long id){
		return paymentBillMapper.findPayOrderById(id);
	}
}
