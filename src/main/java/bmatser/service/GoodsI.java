package bmatser.service;

import java.util.List;
import java.util.Map;

import bmatser.pageModel.PageMode;
import bmatser.pageModel.SearchGoods;

public interface GoodsI {

	List findHotGoods(String payTime, int page, int rows);
	
	List findSellGoodsByKeyword(String keyword, Integer cateId, Integer brandId, String priceOrder, String stockOrder, int page, int rows);
	
	List findSellGoodsCategory(String keyword, Integer brandId, int page, int rows);
	
	List findBaseGoodsByKeyword(String keyword, Integer cateId, Integer brandId, int page, int rows);
	
	List findBaseGoodsCategory(String keyword, Integer brandId, int page, int rows);
	
//	Object getGoodsDetailByApp(String sellerGoodsId, String dealerId, PageMode model) throws Exception;
	
	List getGoodsAttr(long goodsId);

	Map<String, Object> findGoodsById(Long id);
	
	Map<String, Object> searchMallGoods(String keyword, Integer dealerId, Integer brandId,
			Integer categoryId, Integer[] attrValueIds, String sortFields, String sortFlags, int page, int rows, String alias);
	
	Map<String, Object> searchAppMallGoods(String keyword, Integer dealerId, Integer brandId,
			Integer categoryId, Integer[] attrValueIds, String sortFields, String sortFlags, int page, int rows, String alias);

	Map<String, Object> searchMobGoods(String keyword, Integer brandId, Integer categoryId,
			String sortFields, String sortFlags, int page, int rows);

	Map<String, Object> searchMobGoodsPrice(String keyword, Integer brandId,
			Integer categoryId, String sortFields, String sortFlags, int i,
			int j);

	List findGoodsCategory(String keyword, Integer brandId, int i, int rows);

	
	List suggestGoods(String keyword);

	Object getGoodsDetailBySaas(String sellerGoodsId,
			String dealerId)  throws Exception ;

	Object getGoodsDetail(String sellerGoodsId) throws Exception;

	Object getGoodsDetailByMall(String sellerGoodsId) throws Exception;

	Map<String, Object> selectGoodsSalesRecords(PageMode mode,int page, int rows)throws Exception;

	List selectHotGoods(String alias) throws Exception;

	Map<String, Object> searchSaasGoods(SearchGoods search) throws Exception;

	Map<String, Object> searchFaceSaasGoods(SearchGoods search) throws Exception;
	
	Map<String, Object> searchFaceGoods(SearchGoods search) throws Exception;
	
	Map<String, Object> searchModelSelectionGoods(SearchGoods search) throws Exception;

	Map<String, Object> searchPCFaceGoods(SearchGoods search) throws Exception;

	Map<String, Object> getGoodsSeries(String id) throws Exception;

	List getBrandCertificate(String brandId);

	void updateSolrData(String ids) throws Exception;

//	Object getTruscoGoodsDetail(String goodsId) throws Exception;

    List<Map<String,Object>> getSellerGoods(Integer goodsId)throws Exception;


}
