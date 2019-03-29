package bmatser.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;

/**
 * 
 * 团购商品 
 *
 */
public interface GrouponOrderMapper {
	/**
	 * 
	 * 团购商品列表 
	 *
	 */
	List selectGrouponGoods(@Param("dealerId")Integer dealerId,@Param("allActivityParam")String allActivityParam,
			@Param("grouponName")String grouponName,@Param("page") int page,@Param("rows") int rows);
	
	int count(Map mp);
	/**
	 * 
	 * 活动汇 团购活动列表 
	 *
	 */
	List selectGrouponActivitySink(@Param("chooseActivityModule")String chooseActivityModule,
			@Param("page") int page,@Param("rows") int rows,@Param("dealerId") String dealerId);
	
	int countActivitySink(Map mp);
	/**
	 * 
	 * 活动汇 团购详情列表 
	 *
	 */
	Map selectGrouponActivityDetail(@Param("id")String id,@Param("dealerId") String dealerId);
	
	/**
	 * 
	 * 活动汇 团购详情列表矩阵
	 *
	 */
	List selectGrouponActivityDetailList(@Param("id")String id,@Param("dealerId")String dealerId,@Param("page") int page,@Param("rows") int rows);
	
	int countActivityDetailList(Map mp);
}