package bmatser.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ShopFloorMapper {

	List getShopFloorList(@Param("alias") String alias);

	List getShopFloorGoods(@Param("id") String id);

}
