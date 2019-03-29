package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 首页楼层数据处理
 * @author felx
 * @date 2017-12-16
 */
public interface GoodsFloorMapper {
	
	List findFloors();
	
	List<Map<String, Object>> findFloorGoods(@Param("code")String floorCode);
	
	List findFloorCategorys(@Param("id")String id);
	
	List findFloorBrands();

	List findPageFloorDetail(@Param("floorId")String floorId,@Param("imagePrefix")String imagePrefix);

    List<Map<String, Object>> findPageFloorDetailForIos(@Param("floorId")String floorId,@Param("imagePrefix")String imagePrefix);

	List findFloorGoodsDetail(@Param("sellerGoodIds")String sellerGoodIds);

	String getHotSaleGoodsInfo();
}
