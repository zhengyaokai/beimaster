package bmatser.service;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.model.SysOptionsValue;

public interface SysOptionsServiceI {

	Map<String, Object> findSysOptions();
	
	
	SysOptionsValue getPaySysOption(Integer optId,String remark);
	
	/**
	 * 根据id获取系统参数
	 * @author felx
	 * @date 2016-1-4
	 */
	public SysOptionsValue findPaySysOption(Integer OptValue,Integer optId);

}
