package bmatser.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;

import bmatser.model.MemberQuery;

/**
 * 会员咨询
 * @author felx
 * @date 2016.02.18 
 */
public interface MemberQueryMapper {
	
	int insert(MemberQuery memberQuery);

	List selectById(Map map);
	
	Long count(Map mapPut);
}