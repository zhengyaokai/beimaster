package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.model.MemberComment;

public interface GoodsCommentMapper {

	List getGoodsCommentByPage(Map<String, Object> queryMap);

	int getGoodsCommentByPageColunt(Map<String, Object> queryMap);

	void add(MemberComment comment);

	int isExist(MemberComment comment);
	
	int isExistGoods(MemberComment comment);

	int isCheck(Integer id);

	void check(@Param("id")Integer id,@Param("dealerId")String dealerId);

	void del(Integer id);
	
	Map isExistGetOrder(@Param("dealerId")Integer dealerId,@Param("sellerGoodsId")Long sellerGoodsId);
	
	void updateOrderGoods(@Param("orderId")Long id,@Param("sellerGoodsId")Long sellerGoodsId);
	
}
