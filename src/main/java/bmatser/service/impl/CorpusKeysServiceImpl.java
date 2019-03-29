package bmatser.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import bmatser.dao.CorpusKeysDao;
import bmatser.service.CorpusKeysServiceI;
import bmatser.service.GetMessageFromRobot;
import net.rubyeye.xmemcached.MemcachedClient;

@Service
public class CorpusKeysServiceImpl implements CorpusKeysServiceI{
	@Autowired
	MemcachedClient memcachedClient;
	
	@Autowired
	private CorpusKeysDao corpusKeysDao;
	
	@Autowired
	GetMessageFromRobot getMessageFromRobot;
	
	@Override
	public String getQAs(String word) {
		try{
			List<String> result = getKeys();
			if(!CollectionUtils.isEmpty(result)){
				List<String> keys = result.stream().distinct().collect(Collectors.toList());
				for(String key :keys){
					if(word.contains(key)){
						Map<String,Object> map = getMessageFromRobot.getMessageFromRobot(key);
						if(map.get("answer")!=null && !map.get("answer").equals("")){
							String returnResult = map.get("answer").toString();
 								return JSONObject.parseObject(returnResult).getString("text");
						}
					}
				}
			}
			return null;
		}catch(Exception e){
			return null;
		}
		
	}
	
	private List<String> getKeys() throws Exception{
		List<String> list = memcachedClient.get("tuling_keys");
		if(!CollectionUtils.isEmpty(list)){
			return list;
		}else{
			list = corpusKeysDao.findTulingKeys();
			memcachedClient.add("tuling_keys", 0, list);
		}
		return list;
		
	}

}
