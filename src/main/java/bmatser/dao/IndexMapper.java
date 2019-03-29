package bmatser.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface IndexMapper {
	
	void updateclickVolume(@Param("modular")String modular,@Param("id")String id,@Param("click")String click);

	List getAllStoreLogo();

}
