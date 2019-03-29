package bmatser.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ApplicationAppMapper {

	Map findByPage(
			@Param("type")String type,
			@Param("version")String version,
			@Param("channel") String channel,
			@Param("build") String build,
			@Param("platform") int i
			);

	String findIsUpdate(
			@Param("type")String type,
			@Param("version")String version,
			@Param("channel") String channel,
			@Param("build") String build,
			@Param("platform") int i
			);


}
