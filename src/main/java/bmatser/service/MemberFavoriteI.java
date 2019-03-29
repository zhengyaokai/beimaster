package bmatser.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;

import bmatser.model.MemberFavorite;
import bmatser.util.LoginInfo;
/**
 * 会员收藏
 * @author felx
 * @date 2016.02.17 
 */
public interface MemberFavoriteI {
	/**
	 * 新增会员收藏
	 * @param LoginInfo loginInfo,MemberFavorite memberFavorite
	 * @return 
	 */
	int insert(LoginInfo loginInfo,MemberFavorite memberFavorite);
	/**
	 * 修改会员收藏
	 * @param String id,LoginInfo loginInfo,MemberFavorite memberFavorite
	 * @return 
	 */
	int updateById(String id,LoginInfo loginInfo,MemberFavorite memberFavorite);
	/**
	 * 删除会员收藏
	 * @param Integer id
	 * @return 
	 */
	int deleteById(String sellerGoodsIds,Integer dealerId);
	/**
	 * 查询会员收藏关联信息
	 * @param HttpServletRequest request,Map map
	 * @return 
	 */
	List selectById(HttpServletRequest request,Map map);
	/**
	 * 查询会员收藏总记录数
	 */
	public int count(Map mp);
	/**
	 * 查询会员收藏的商品是否已收藏 
	 * @param Integer sellerGoodsId,String dealerId
	 * @return 0没有收藏；1收藏
	 */
	int getNumber(Long sellerGoodsId,String dealerId);
	//新增APP商城会员收藏
	int insertMemberFavorite(MemberFavorite memberFavorite);
}
