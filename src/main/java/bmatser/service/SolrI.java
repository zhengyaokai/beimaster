package bmatser.service;

import java.util.List;
import java.util.Map;

import bmatser.pageModel.SearchGoods;

public interface SolrI {

	/**
	 * 搜索商品
	 * @param keyword 关键词
	 * @param brandId 品牌ID
	 * @param categoryId 分类ID
	 * @param attrValueIds 属性值ID集合，多个ID通过英文状态下逗号分隔
	 * @param sortFields 排序字段
	 * @param sortFlags 排序方式（true正序  false倒序）
	 * @param page 当前第几页，从1开始
	 * @param rows 每页显示条数
	 * @return map 供应商的商品ID列表、商品总数
	 */
	Map<String, Object> searchFaceGoods(String keyword, Integer brandId, Integer categoryId, Integer[] attrValueIds, int page, int rows);

	/**
	 * 搜索商品
	 * @param keyword 关键词
	 * @param brandId 品牌ID
	 * @param categoryId 分类ID
	 * @param attrValueIds 属性值ID集合，多个ID通过英文状态下逗号分隔
	 * @param page 当前第几页，从1开始
	 * @param rows 每页显示条数
	 * @param alias 
	 * @return map 供应商的商品ID列表、商品总数
	 */
	Map<String, Object> searchMallGoods(String keyword, Integer dealerId, Integer brandId, Integer categoryId, Integer[] attrValueIds, String[] sortFields, Integer[] sortFlags, int page, int rows, String alias);
	/**
	 * 搜索Saas商品
	 * @param keyword 关键词
	 * @param brandId 品牌ID
	 * @param categoryId 分类ID
	 * @param attrValueIds 属性值ID集合，多个ID通过英文状态下逗号分隔
	 * @param sortFields 排序字段
	 * @param sortFlags 排序方式（true正序  false倒序）
	 * @param page 当前第几页，从1开始
	 * @param rows 每页显示条数
	 * @return map 供应商的商品ID列表、商品总数
	 * @author felx
	 * @param advantage 
	 * @date 2017-12-22
	 */
	Map<String, Object> searchSaasGoods(String keyword, Integer dealerId,
			Integer brandId, Integer categoryId, Integer[] attrValueIds,
			String[] sortFields, Integer[] sortFlags, int page, int rows, String advantage);

	Map<String, Object> searchFaceGoods(String keyword, Integer dealerId, Integer brandId,
			Integer categoryId, Integer[] attrValueIds, String[] sortFieldArr,
			Integer[] sortFlagInt, int page, int rows);
	
	/**
	 * saas商品联想
	 * @author felx
	 * @date 2016-3-30
	 */
	List<Map> searchSuggestGoods(String keyword);
	
	/**
	 * 商城店铺查询
	 * @author felx
	 * @date 20160607
	 */
	Map<String, Object> findStoreList(int page,int row,String keyword);

	Map<String, Object> searchSaasGoods(SearchGoods search);
	
	Map<String, Object> searchModelSelectionGoods(SearchGoods search);

	Map<String, Object> searchFaceSaasGoods(SearchGoods search);
	
	Map<String, Object> searchFaceGoods(SearchGoods search);

	Map<String, Object> getGoodsSeries(String spuId) throws Exception;

}

