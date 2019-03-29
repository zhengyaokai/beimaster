package bmatser.service.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.ApplicationAppMapper;
import bmatser.service.AppVersionServiceI;
@Service("appVersionService")
public class AppVersionServiceImpl implements AppVersionServiceI {
    @Autowired
	private ApplicationAppMapper AppMapper;

	@Override
	public Map getAppInfo(String type, String version,String channel, String build) {
		if(StringUtils.isNotBlank(type)){
			if(StringUtils.equals("1", type)){
				type = "android";
			}else if(StringUtils.equals("2", type)){
				type = "ios";
			}
		}
		Map map = AppMapper.findByPage(type,version,channel,build,0);
		if(map!=null&&map.get("build")!=null){
			String code = AppMapper.findIsUpdate(type, version, channel, build, 0);
			map.put("isUpdate", code);
		}else{
			map =null;
		}
		return map;
	}

	@Override
	public Map getMappAppInfo(String type, String version, String channel, String build) {
		if(StringUtils.isNotBlank(type)){
			if(StringUtils.equals("1", type)){
				type = "android";
			}else if(StringUtils.equals("2", type)){
				type = "ios";
			}
		}
		Map map = AppMapper.findByPage(type,version,channel,build,1);
		if(map!=null&&map.get("build")!=null){
			String code = AppMapper.findIsUpdate(type, version, channel, build, 1);
			map.put("isUpdate", code);
		}else{
			map =null;
		}
		return map;
	}

}
