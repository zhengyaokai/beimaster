package bmatser.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.SysOptionsMapper;
import bmatser.model.SysOptionsValue;
import bmatser.service.SysOptionsServiceI;

@Service("sysOptionsService")
public class SysOptionsServiceImpl implements SysOptionsServiceI {
	@Autowired
	private SysOptionsMapper sysOptionsMapper;
	
	
	@Override
	public Map<String, Object> findSysOptions() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<SysOptionsValue> storeList= sysOptionsMapper.findSysOptions(1);
		List<SysOptionsValue> sourceList = sysOptionsMapper.findSysOptions(2);
		List<SysOptionsValue> payList = sysOptionsMapper.findSysOptions(3);
		List<SysOptionsValue> accountList = sysOptionsMapper.findSysOptions(4);
		List<SysOptionsValue> odstatusList = sysOptionsMapper.findSysOptions(5);
		map.put("storeList", storeList);
		map.put("sourceList", sourceList);
		map.put("payList", payList);
		map.put("accountList", accountList);
		map.put("odstatusList", odstatusList);
		return map;
	}
	
	
	public SysOptionsValue getPaySysOption(Integer optId,String remark){
		Map map = sysOptionsMapper.getPaySysOption(optId, remark);
		SysOptionsValue sysOptionsValue = new SysOptionsValue();
		if(null!=map){
			sysOptionsValue.setOptValue(map.get("optValue")==null?null:map.get("optValue").toString());
			sysOptionsValue.setOptText(map.get("optText")==null?null:map.get("optText").toString());
			sysOptionsValue.setRemark(map.get("remark")==null?null:map.get("remark").toString());
		}
		return sysOptionsValue;
	}
	
	/**
	 * 根据id获取系统参数
	 * @author felx
	 * @date 2016-1-4
	 */
	public SysOptionsValue findPaySysOption(Integer OptValue,Integer optId){
		SysOptionsValue sysOptionsValue = sysOptionsMapper.findPaySysOption(OptValue,optId);
		return sysOptionsValue;
	}

}
