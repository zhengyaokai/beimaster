package bmatser.service;

import java.util.List;
import java.util.Map;

import bmatser.model.Category;
import bmatser.model.ModelSelectionCategory;
import bmatser.pageModel.PageMode;

/**
 * 供应商与商品对应数据
 * @author felx
 * @date 2016.03.10 
 */
public interface CategoryI {
	/**
	 *查询所有存在上架商品的系统分类
	 */
	List selectCategory(String param);

	List getTopCategory();

	List getChildAllCate(String cateId, String brandId);

	List findCateBrand(List map, String cateId);

	List getMallFloorCate();

	List findCateBrand(PageMode model);

	List selectAppCategory(String param);

	List<ModelSelectionCategory> selectModelSelectionCates(Integer brandId);

//

}
