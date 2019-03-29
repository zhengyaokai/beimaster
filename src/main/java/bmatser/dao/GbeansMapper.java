package bmatser.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface GbeansMapper {

	List<Map<String, Object>> getOrderGoodsInfo(String orderId);
	
	void updateOrderBeans(@Param("orderId")String orderId,@Param("amount")BigDecimal amount);
	
	void delDealerBeansToAdd(@Param("orderId")String orderId);

	void addDealerBeansToAdd(@Param("orderId")String orderId,@Param("amount")BigDecimal amount);

	List<Map<String, Object>> getCartsInfo(@Param("carts")String[] carts);

	int getDealerBeans(@Param("orderId")String orderId);

	double getOrderAmount(@Param("orderId")String orderId);

	void updateOrderToComplete(String orderId);

	void addOrderPay(@Param("orderId")String orderId,@Param("paymentBillId")String paymentBillId);
	
	void addOrderPayOrder(@Param("orderId")String orderId,@Param("paymentBillId")String paymentBillId);

	void updateOrderBeansDeduction(@Param("orderId")String orderId,@Param("amount")double beans);

	void updateDealerBeans(@Param("dealerId")String dealerId,@Param("amount")double beans);

	void addDealerBeansToUse(@Param("orderId")String orderId,@Param("amount")double beans);

	int getUseBeans(String dealerId);

	void delDealerBeansToUse(@Param("orderId")String orderId);


	List<Map<String, Object>> getOrderSellerGoods(@Param("idList")List<Map<String, Object>> data);

	void cancelOrderUpdateBean(@Param("orderId")String orderId);

	void updateOrderGoodsBeans(@Param("orderGoodsId")String orderGoodsId);
	
	void returnDealerBeans(@Param("dealerId")String dealerId,@Param("amount")int beans);
	

}
