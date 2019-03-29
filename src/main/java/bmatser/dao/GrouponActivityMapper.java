package bmatser.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface GrouponActivityMapper {

	int isGroupGoods(@Param("grouponBuyerId")String dealerId, @Param("sellerGoodsId")String sellerGoodsId);

	int isGroupActivity(@Param("grouponBuyerId")String dealerId, @Param("groupActivityId")String groupActivityId);

	String findGrouponDesc(@Param("sellerGoodsId")String sellerGoodsId);
	
	String findGrouponActivityId(@Param("grouponBuyerId")String dealerId, @Param("sellerGoodsId")String sellerGoodsId);
	
	int isExistActivity(@Param("id")String id);
	
	void updateGroupClick(@Param("id")String id);
	
	void addGroupBuyer(@Param("groupId")String groupId,@Param("buyerId")String buyerId);
	
	void updateGroupAmount(@Param("id")String id);
	
	int isActivityExistBuyer(@Param("groupId")String groupId,@Param("buyerId")String buyerId);

	String findGrouponId(@Param("sellerGoodsId")String id);

	void updateGroupGoodsStatus(String groupActivityId);
	
	int isGroupGoodsBysellerGoodsId(@Param("sellerGoodsId")String sellerGoodsId);

	Map getGroupInfoByApp(@Param("dealerId")String dealerId,@Param("sellerGoodsId") String sellerGoodsId);
	
	//报名后返回此团购信息
	Map getGroupBuyInfo(@Param("groupId")String groupId,@Param("dealerId")String dealerId);
}
