package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.model.JtlRegisterInfo;
import bmatser.model.RefundOrder;
import bmatser.pageModel.DealerCreditFeedback;
import bmatser.pageModel.GoldOrderApply;

public interface GoldMantisMapper {

	void addCreditFeedback(DealerCreditFeedback back);

	Map<String, Object> getOrderByGoldMantis(@Param("orderId")String orderId,@Param("dealerId")String dealerId);

	void addOrderApply(GoldOrderApply orderApply);

	Map<String, Object> getOrderDeal(@Param("orderId")String orderId,@Param("dealerId")String dealerId);

	/**
	 * 获取用户贷款状态
	 * @param dealerId
	 * @return
	 */
	int getJtlStatus(String dealerId);

	/**
	 *  获取金螳螂用户注册用户信息
	 * @param dealerId
	 * @return
	 */
	JtlRegisterInfo getJtlRegisterInfo(String dealerId);
	/**
	 * 保存贷款申请信息
	 * @param info
	 * @param dealerId
	 */
	void updateJtlRegisterInfo(@Param("o")JtlRegisterInfo info,@Param("dealerId")String dealerId);

	/**
	 * 判断是否支持退货
	 * @param orderId
	 * @param dealerId
	 * @return
	 */
	Map<String, Object> getRefundInfo(@Param("orderId")String orderId, @Param("dealerId")String dealerId);

	/**
	 * 获得退货商品
	 * @param orderId
	 * @return
	 */
	List getRefundGoods(String orderId);

	/**
	 * 新增退款申请
	 * @param refund
	 */
	void addRefundOrder(RefundOrder refund);

	/**
	 * 更新订单状态： 退货
	 * @param orderId
	 */
	void updateOrderToRefund(String orderId);
	/**
	 * 更新订单状态： 退货
	 * @param orderId
	 */
	void updateRefundToDel(String orderId);

	/**
	 * 保存退货图片
	 * @param id
	 * @param s
	 */
	void addRefundImage(@Param("id")String id, @Param("image")String image);

	List<Map<String, Object>> getRefundList(Map<String, Object> query);

	int getRefundListCount(Map<String, Object> query);

	void addJtlRegisterInfo(@Param("o")JtlRegisterInfo info,@Param("dealerId")String dealerId);
	
	int isExistBusinessLicense(@Param("dealerId")String dealerId);

	void updateOrderToComplete(@Param("orderId")String orderId);
	
	void updatePaymentToComplete(@Param("orderId")String orderId);

	void updateDealerIdStatus(@Param("dealerId")String dealerId);
	


}
