package bmatser.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface AdverImageMapper {
	/**
	 * 获取banner图片
	 * @param positionId 位置
	 * @return
	 */
	List findAdverImages(@Param("positionId")int positionId);
	
	/**
	 * 获取所有banner图片
	 * @param positionId 
	 * @return
	 */
	List findAllAdverImages(@Param("positionId")int positionId);
	/**
	 * 商城活动图片
	 * @param positionId
	 * @return
	 */
	List findMallAdverImages(@Param("positionId")int positionId);
	
	/**
	 * 商城首页楼层的横幅广告和分类促销广告
	 * @param channel
	 * @return
	 */
	List getfloorAdvertisement(@Param("positionId")String positionId);

	List findAppMallAdverImages(@Param("positionId")int positionId);
	
}