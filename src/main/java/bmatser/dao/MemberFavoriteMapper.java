package bmatser.dao;

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
public interface MemberFavoriteMapper {
	
	int insert(MemberFavorite memberFavorite);

	int updateById(MemberFavorite memberFavorite);

	int deleteById(@Param(value="sellerGoodsId")String sellerGoodsId,@Param(value="dealerId")Integer dealerId);

	List selectById(Map map);
	
	int count(Map mp);

	int getNumber(@Param(value="sellerGoodsId")Long sellerGoodsId,@Param(value="dealerId")String dealerId);

}