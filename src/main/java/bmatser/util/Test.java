package bmatser.util;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String,Object> idListMap = Maps.newHashMap();
		idListMap.put("ProID",-1);
		idListMap.put("Gbrand","");
		idListMap.put("Gserial", "");
		idListMap.put("GID","");
		idListMap.put("Gname","");
		idListMap.put("Gindex","");
		idListMap.put("Gunit","个");
		idListMap.put("BaCgCp", "");
		idListMap.put("BaCpMs","");
		idListMap.put("BaHqSm","");
		idListMap.put("BfCpMj", "");
		idListMap.put("BaCd","");
		idListMap.put("BfZl", "");
		idListMap.put("BfQdl", "");
		idListMap.put("BfYjZk", "");
		idListMap.put("BfYjJj", "");
		List<Map<String,Object>> list = Lists.newArrayList();
		list.add(idListMap);
		System.out.println(JSONObject.toJSONString(list));
		
		for(AppkeyScrectEnum app :AppkeyScrectEnum.values()){
			System.out.println(app.getAppKey() + "---" + app.getAppScrect());
		}
	}

}
