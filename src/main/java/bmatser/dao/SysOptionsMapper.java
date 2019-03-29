package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.model.SysOptionsValue;

public interface SysOptionsMapper {

	List<SysOptionsValue> findSysOptions(@Param("id") Integer id);
	
	Map getPaySysOption(@Param("optId") Integer optId,@Param("remark") String remark);
	
	SysOptionsValue findPaySysOption(@Param("optValue") Integer optValue,@Param("optId") Integer optId);
	
}
