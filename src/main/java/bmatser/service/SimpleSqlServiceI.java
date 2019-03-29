package bmatser.service;

import java.util.List;
import java.util.Map;

import bmatser.model.Select;

public interface SimpleSqlServiceI {
	
	Map<String, Object> selectOne(Select select);
	
	String selectString(Select select);
	
	List<Map<String, Object>> select(Select select);
	

}
