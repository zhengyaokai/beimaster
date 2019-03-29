package bmatser.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;

/**
 * 
 * 团购商品 
 *
 */
public interface GrouponOrderI {

	/**
	 * 
	 * 团购商品列表 
	 *
	 */
	List selectGrouponGoods(Integer dealerId,HttpServletRequest request,Integer page,Integer rows,Map mp) throws Exception;
	
	int count(Map mp);
	/**
	 * 
	 * 活动汇 团购活动列表
	 *
	 */
	List selectGrouponActivitySink(HttpServletRequest request,Integer page,Integer rows,Map mp);
	
	int countActivitySink(Map mp);
	/**
	 * 
	 * 活动汇 团购详情列表
	 *
	 */
	Map selectGrouponActivityDetail(HttpServletRequest request,String dealerId);
	/**
	 * 
	 * 活动汇 团购详情列表矩阵
	 *
	 */
	List selectGrouponActivityDetailList(String id,String dealerId,Integer page,Integer rows);
	
	int countActivityDetailList(Map mp);
}
