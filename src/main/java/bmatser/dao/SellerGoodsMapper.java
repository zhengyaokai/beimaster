package bmatser.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.model.SellerGoods;

public interface SellerGoodsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SellerGoods record);

    int insertSelective(SellerGoods record);

    SellerGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SellerGoods record);

    int updateByPrimaryKey(SellerGoods record);

	SellerGoods findGoodsPrice(String sellerGoodsId);
	
	List findGoodsByBrand(@Param("page")int page,@Param("rows")int rows,@Param("brandId")String brandId);
	
	int  findGoodsByBrandCount(@Param("brandId")String brandId);
	
	List findStoreOrderCount(String alias);
	
	List findStoreMonthOrderCount(String alias);
	
	Map findStoreGoodsById(String Id);

	List findStoreGoodsByPage(@Param("alias")String alias, @Param("query")Map queryMap, @Param("page")int pages, @Param("rows")int rows);

	int findStoreGoodsByPageCount(@Param("alias")String alias,@Param("query") Map queryMap);

	List findStoreGoodsByNoExist(@Param("alias")String shopAlias,@Param("page")int i,@Param("noExist")String noExist);

	Map getSellerGoodsByGoodsId(String id);
	//根据活动id先查询活动
	Map findActivity(@Param("id")String id);
	// 根据活动id查询出活动商品 20160808 tiw
	List findActivityGoods(@Param("id")String id, @Param("page")Integer page, @Param("rows")Integer rows);
    //查询活动商品总数
	int findActivityGoodsCount(@Param("id") String id);

    //新的折扣方式(根据商品)
    BigDecimal getDiscountByGoodsNo(@Param("goodsNo")String goodsNo, @Param("dealerId")String dealerId);

    //按照系列搜索
    BigDecimal getDiscountBySeries(@Param("series")String series,@Param("dealerId")String dealerId);

    //获取series
    String getSeries(@Param("goodsNo")String goodsNo);

    //获取面价
    BigDecimal  getMarketprice(@Param("sellerGoodsId")String sellerGoodsId);

}