package bmatser.dao;

import org.apache.ibatis.annotations.Param;

public interface DownloadMapper {

	String authorizeBydealerId(@Param("dealerId") String dealerId);

}
