package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface SimpleSqlMapping {


	List<Map<String, Object>> select(@Param("sql")String sql);

}
