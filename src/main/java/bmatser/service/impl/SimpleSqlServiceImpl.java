package bmatser.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.SimpleSqlMapping;
import bmatser.model.Select;
import bmatser.service.SimpleSqlServiceI;

@Service("simpleSqlService")
public class SimpleSqlServiceImpl implements SimpleSqlServiceI{

	@Autowired
	private SimpleSqlMapping SimpleSqlDao;
	
	@Override
	public Map<String, Object> selectOne(Select select) {
		select.setRow(1);
		List<Map<String, Object>> list= SimpleSqlDao.select(select.sql());
		if(list!=null){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public String selectString(Select select) {
		select.setRow(1);
		List<Map<String, Object>> list= SimpleSqlDao.select(select.sql());
		if(list!=null && list.size()>0){
			Map<String, Object> map = list.get(0);
			for (Entry<String, Object> data : map.entrySet()) {
				if(data.getValue() != null){
					return data.getValue().toString();
				}else{
					return "";
				}
			}
		}else{
			return null;
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> select(Select select) {
		System.out.println(select.sql());
		return SimpleSqlDao.select(select.sql());
	}

}
