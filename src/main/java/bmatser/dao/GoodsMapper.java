package bmatser.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import bmatser.model.Goods;
import bmatser.model.SellerGoods;
import bmatser.pageModel.GoodsDetail;
import bmatser.pageModel.GoodsPage;
import bmatser.pageModel.MallGoodsInfo;

public interface GoodsMapper {

	List findHotGoods(@Param("payTime")String payTime, @Param("page")int page, @Param("rows")int rows);
	
	//商城首页热门搜索   ---add 20160603
    List findHotSearch();
    
    //商城首页每日推荐    ---add  20160606
  	List getDailyRecommendation();
  	
	List findSellGoodsByKeyword(@Param("keywords")List<String> keywords, @Param("cateId")Integer cateId, @Param("brandId")Integer brandId, @Param("priceOrder")String priceOrder, @Param("stockOrder")String rateOrder, @Param("page")int page, @Param("rows")int rows);
	
	List findSellGoodsCategory(@Param("keywords")List<String> keywords, @Param("brandId")Integer brandId, @Param("page")int page, @Param("rows")int rows);
	
	List findBaseGoodsByKeyword(@Param("keywords")List<String> keywords, @Param("cateId")Integer cateId, @Param("brandId")Integer brandId, @Param("page")int page, @Param("rows")int rows);

	List findBaseGoodsCategory(@Param("keywords")List<String> keywords, @Param("brandId")Integer brandId, @Param("page")int page, @Param("rows")int rows);

	GoodsDetail getGoodsDetail(@Param("sellerGoodsId") String sellerGoodsId, @Param("channel")int i);
	
	List getGoodsAttr(@Param("goodsId") long goodsId);
	
	
	String getProxyCertificate(@Param("brandId") int brandId, @Param("sellerId")String sellerId);

	List<SellerGoods> getShelvesGoods(@Param("goods")GoodsPage goodsPage,@Param("page") int i,@Param("rows")int rows);

	SellerGoods getShelvesGoodsById(Long id);

	List findByMobPage(@Param("keyword")String keyword,@Param("brandId")Integer brandId,@Param("cateId")Integer categoryId,
			@Param("sort")String sort,@Param("page")int page,@Param("rows")int rows);

	int findByMobPageCount(@Param("keyword")String keyword,@Param("brandId")Integer brandId,@Param("cateId")Integer categoryId,
			@Param("sort")String sort);

	List findByMobPricePage(@Param("keyword")String keyword,@Param("brandId")Integer brandId,
			@Param("cateId")Integer categoryId,@Param("sort") String string, @Param("page")int page, @Param("rows")int rows);

	int findByMobPricePageCount(@Param("keyword")String keyword,@Param("brandId")Integer brandId,@Param("cateId")Integer categoryId,
			@Param("sort")String sort);

	List findGoodsCategory(@Param("keywords")List<String> keywords, @Param("brandId")Integer brandId, @Param("page")int page, @Param("rows")int rows);

	String getGoodsActivityDec(@Param("sellerGoodsId")String sellerGoodsId,@Param("activityType")int i);

	String getGoodsRateDec(@Param("sellerGoodsId")String sellerGoodsId);


	//查询商品销售记录 
	List selectGoodsSalesRecords(@Param("goodsId") String goodsId,@Param("page")int page, @Param("rows")int rows);

	//查询商品销售记录总数
	int getGoodsSalesRecordsCount(@Param("goodsId") String goodsId);

	//查询不同购买人累计购买商品总数
	int getGoodsSumNumByBuyerId(@Param("goodsId") String goodsId, @Param("buyerId") String buyerId);

	//先查有销量的
	List selectHotGoods(@Param("alias")String alias);
    //关联店铺查没有销量的推荐商品
	List selectHotGoods1(@Param("rows")int rows);


	List getRecommendGoods(@Param("sellerGoodsId")Long sellerGoodsId);

	String getProxyCertificateByMall(@Param("sellerGoodsId")String sellerGoodsId);

	String findSpuIdById(String id);

	//商城商品详情 增加返回字段 活动描述List add 20161013
	List<Map<String,Object>> getActivityDescList(String sellerGoodsId);

	List getBrandCertificate(@Param("brandId")String brandId);

	List<MallGoodsInfo> getGoodsView(@Param("ids")String ids);

	List<Integer> getGoodsViewAttr(@Param("goodsId")String goodsId);

	List<Integer> getGoodsViewAttrValue(@Param("goodsId")String goodsId);

	String getMallGoodsActivityDec(@Param("sellerGoodsId")String sellerGoodsId,@Param("activityType")int i);

	GoodsDetail getTruscoGoodsDetail(@Param("goodsId")String goodsId);

	List<Goods> selectNoSynCodeGoods(@Param("page")int page, @Param("rows")int rows);

	List<Goods> selectTheNoSynCodeGoods(@Param("goodsIdList") String goodsIdList);


	void updateSynCodeById(@Param("id")String id, @Param("proID")Integer proID);

    //新的折扣方式(根据商品)
    BigDecimal getDiscountByGoodsNo(@Param("goodsNo")String goodsNo,@Param("dealerId")String dealerId);

	//按照系列搜索
    BigDecimal getDiscountBySeries(@Param("series")String series,@Param("dealerId")String dealerId);

    //create by yr on 2018-11-05
    List<Map<String,Object>> getSellerGoods(@Param("goodsId")Integer goodsId);

	Long selectNoSynCodeGoodsTotal();

	Map<String,Object> getGoodsDetailInPackage(@Param("goodSynCode") String goodSynCode);



}