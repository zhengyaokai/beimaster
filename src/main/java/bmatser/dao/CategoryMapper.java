package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.model.Category;
import bmatser.model.ModelSelectionCategory;
import bmatser.pageModel.SelectCategory;

public interface CategoryMapper {

	List findValidCategories();
	
	List findCategoryAttrs();
	
	List findEightToCategory();
	
	/**
	 * 根据上级id获取分类信息
	 * @author felx
	 */
	List<Category> findCategories(@Param("parentId") Integer parentId);
	
	/**
	 * 获取分类商品数
	 * @author felx
	 */
	Integer findCategoryGoods(@Param("cateId") Integer cateId);
	
	List<SelectCategory> selectCategory(@Param("type")int type, @Param("idList")Integer[] showCate);
	
	List<Category> getFirstCategory();

	List<Map> getChildCate(@Param("cateId")String cateId);

	List<Map> isExistGoods(@Param("idList")List<Map> toplist,@Param("brandId") String brandId);

	List<Map> findCategoryBrand(Integer id);

	int findCategoryIsLeaf(@Param("cateNo")Integer cateNo);

	List<Map> findCateBylist(@Param("idList")List<String> idList);

	List findCateBrandList(@Param("idList")List map,@Param("cateId")String cateId);
	
	List<Map<String, Object>> findMallCateFloor();
	
	List findMallChildCateFloor(@Param("cateId")String cateId);

	List<String> getMoCate(@Param("brandId")String brandId,@Param("cateId")String cateId);

	List<String> getParentCate(@Param("idList")List<String> list, @Param("cateId")String cateId);

	List findBrandList(@Param("cateId")String cateId);
	
	List findFourCate(@Param("idList")List<Integer> list);
	
	List findThreeCate(@Param("idList")List<Integer> list);
	
	Map findCate(@Param("cateNo")String cateNo);

	List<Map> findAppCategoryBrand(@Param("id")Integer id,@Param("row")int i);

	Integer findCategoryNoByCateName(String categoryName);

	List<ModelSelectionCategory> getFirstModelSelectionCategory(Integer brandId);

	List<ModelSelectionCategory> getNoTopModelSelectionCategory();

	//查找该节点是否为末节点
    Map<String,Object> findNodeByCateId(@Param("cateId") Integer modelSelectionCateId);


}