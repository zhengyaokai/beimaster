package bmatser.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;

import bmatser.model.MemberFavorite;
import bmatser.model.MemberQuery;
import bmatser.util.LoginInfo;
/**
 * 会员咨询
 * @author felx
 * @date 2016.02.18 
 */
public interface MemberQueryI {
	/**
	 * 新增会员咨询
	 * @param LoginInfo loginInfo,MemberQuery memberQuery
	 * @return 
	 */
	int insert(MemberQuery memberQuery);
	/**
	 * 查询会员咨询关联信息
	 * @param HttpServletRequest request,Map map
	 * @return 
	 */
	List selectById(HttpServletRequest request,Map map);
	/**
	 * 查询会员咨询总记录数
	 * @return 
	 */
	public Long count(Map mapPut);
}
