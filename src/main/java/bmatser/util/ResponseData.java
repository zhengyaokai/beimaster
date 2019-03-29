package bmatser.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseData {
	
	private List<List> list;
	
	private String[] key;
	
	public ResponseData(String[] key) {
		if(this.list==null){
			this.list = new ArrayList<List>();
		}
		this.key = key;
	}

	public List<List> getList() {
		return list;
	}

	public void setList(List<List> list) {
		this.list = list;
	}

	public String[] getKey() {
		return key;
	}

	public void setKey(String[] key) {
		this.key = key;
	}

	public void addResponseList(List o){
		this.list.add(o);	
	}
	public List<Map<String,Object>> getReturnData(){
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		for (int i = 0,len=this.list.size(); i < len; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			if(list.get(i).size()>0){
				map.put("name", this.key[i]);
				map.put("list", list.get(i));
				data.add(map);
			}
		}
		return data;
	}
}
