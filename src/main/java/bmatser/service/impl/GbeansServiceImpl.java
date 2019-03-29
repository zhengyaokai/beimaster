package bmatser.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.GbeansMapper;
import bmatser.service.GbeansServiceI;
import bmatser.util.ObjectUtil;
@Service("gbeansService")
public class GbeansServiceImpl implements GbeansServiceI {
	
	@Autowired
	private GbeansMapper gbeansDao;

	/**
	 * 下单获得金豆
	 * @param orderId
	 */
	@Override
	public void toOrderBeans(String orderId) {
		List<Map<String, Object>> goodsList = gbeansDao.getOrderGoodsInfo(orderId);
		BigDecimal gBeans = BigDecimal.ZERO;
		StringBuffer buffer = new StringBuffer("");
		for (Map<String, Object> goods : goodsList) {
			int isBean=ObjectUtil.tointValue(goods.get("isBean"));
			//判断是否满足工币
			if(isBean==1){
				BigDecimal amount = ObjectUtil.toBigDecimal(goods.get("amount"));
				BigDecimal beanRate = ObjectUtil.toBigDecimal(goods.get("beanRate"));
				BigDecimal num = ObjectUtil.toBigDecimal(goods.get("num"));
				//上架价格*数量*100
				gBeans = gBeans.add(
							amount.multiply(beanRate).multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).multiply(num)
							);
				buffer.append(goods.get("id").toString()+",");
			}
		}
		gbeansDao.delDealerBeansToAdd(orderId);
		if(gBeans.compareTo(BigDecimal.ZERO)>0){
			String orderGoodsId  = buffer.substring(0, buffer.length()-1);
			gbeansDao.updateOrderGoodsBeans(orderGoodsId);
			gbeansDao.addDealerBeansToAdd(orderId,gBeans);
		}
		gbeansDao.updateOrderBeans(orderId,gBeans);
	}
	/**
	 * 获得付款单id
	 * @return
	 */
	private String getPaymentID() {
		int r1=(int)(Math.random()*(10));//产生4个0-9的随机数
		int r2=(int)(Math.random()*(10));
		int r3=(int)(Math.random()*(10));
		int r4=(int)(Math.random()*(10));
		long now = System.currentTimeMillis();//一个13位的时间戳
		String paymentID =String.valueOf(r1)+String.valueOf(r2)+String.valueOf(r3)+String.valueOf(r4)+String.valueOf(now);// 订单ID
		return paymentID;
	}
	/**
	 * 购物车可获得金豆
	 * @param cartId
	 * @return
	 */
	@Override
	public int getCartsBeans(String[] cartId) {
		List<Map<String, Object>> goodsList = gbeansDao.getCartsInfo(cartId);
		BigDecimal gBeans = BigDecimal.ZERO;
		for (Map<String, Object> goods : goodsList) {
			int isBean=ObjectUtil.tointValue(goods.get("isBean"));
			//判断是否满足工币
			if(isBean==1){
				BigDecimal amount = ObjectUtil.toBigDecimal(goods.get("amount"));
				BigDecimal num = ObjectUtil.toBigDecimal(goods.get("num"));
				BigDecimal beanRate = ObjectUtil.toBigDecimal(goods.get("beanRate"));
				//上架价格*数量*100
				gBeans = gBeans.add(
							amount.multiply(beanRate).multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).multiply(num)
							);
			}
		}
		return gBeans.intValue();
	}
	/**
	 * 使用金豆
	 * @param orderId
	 * @param dealerId
	 */
	@Override
	public void toUserBeans(String orderId,String dealerId) {
		int beans = gbeansDao.getDealerBeans(orderId);
		double amount = gbeansDao.getOrderAmount(orderId);
		gbeansDao.returnDealerBeans(dealerId, getDealerBeanByOrder(orderId));
		if(beans<=0){
			return;
		}else if(beans>0 && amount/2*100>beans){
			usedealerBean(beans,orderId,dealerId);
		}else if(beans>=amount/2*100){
			BigDecimal b = (new BigDecimal(String.valueOf(amount)).multiply(new BigDecimal(100))).multiply(new BigDecimal(0.5));
			int userBean = b.intValue();
			usedealerBean(userBean,orderId,dealerId);
/*			gbeansDao.updateOrderToComplete(orderId);
			String payId= getPaymentID();
			gbeansDao.addOrderPay(orderId,payId);
			gbeansDao.addOrderPayOrder(orderId, payId);*/

		}
	}

	private void usedealerBean(double beans, String orderId,String dealerId) {
		gbeansDao.updateOrderBeansDeduction(orderId,beans/100);
		gbeansDao.updateDealerBeans(dealerId,beans);
		gbeansDao.delDealerBeansToUse(orderId);
		gbeansDao.addDealerBeansToUse(orderId,beans);
	}

	/**
	 * 用户有多少金豆
	 * @param dealerId
	 * @return
	 */
	@Override
	public int getUseBeans(String dealerId) {
		return gbeansDao.getUseBeans(dealerId);
	}
	@Override
	public int getDealerBeanByOrder(String orderId) {
		int beans = gbeansDao.getDealerBeans(orderId);
		return beans;
	}
	@Override
	public int getDealerBeanByOrder(String[] sellerGoodsId, String[] nums) {
		List<Map<String, Object>> data = new ArrayList<>();
		for (int i = 0; i < sellerGoodsId.length; i++) {
			Map<String, Object> goods =new HashMap<>();
			goods.put("sellerGoodsId", sellerGoodsId[i]);
			goods.put("num", nums[i]);
			data.add(goods);
		}
		List<Map<String, Object>> goodsList = gbeansDao.getOrderSellerGoods(data);
		BigDecimal gBeans = BigDecimal.ZERO;
		for (Map<String, Object> goods : goodsList) {
			int isBean=ObjectUtil.tointValue(goods.get("isBean"));
			//判断是否满足工币
			if(isBean==1){
				BigDecimal amount = ObjectUtil.toBigDecimal(goods.get("amount"));
				BigDecimal num = ObjectUtil.toBigDecimal(goods.get("num"));
				BigDecimal beanRate = ObjectUtil.toBigDecimal(goods.get("beanRate"));
				//上架价格*数量*100
				gBeans = gBeans.add(
							amount.multiply(beanRate).multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).multiply(num)
							);
			}
		}
		return gBeans.intValue();
	}
	@Override
	public void cancelOrder(String orderId) {
		gbeansDao.cancelOrderUpdateBean(orderId);
		
	}
	@Override
	public void toNotUserBeans(String orderId, String dealerId) {
		gbeansDao.returnDealerBeans(dealerId, getDealerBeanByOrder(orderId));
		gbeansDao.delDealerBeansToUse(orderId);
		gbeansDao.updateOrderBeansDeduction(orderId,0);
	}
	
	

}
