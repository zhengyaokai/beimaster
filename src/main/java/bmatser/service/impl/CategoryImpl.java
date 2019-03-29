package bmatser.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;

import bmatser.dao.CategoryMapper;
import bmatser.model.Category;
import bmatser.model.ModelSelectionCategory;
import bmatser.pageModel.PageMode;
import bmatser.pageModel.SelectCategory;
import bmatser.service.CategoryI;


/**
 * 客户经营类别
 * @author felx
 * @date 2016.03.10 
 */
@Service("category")
public class CategoryImpl implements CategoryI {
	@Autowired
	private CategoryMapper categoryDao;
	
/*	@Autowired
	private BrandMapper brandDao;*/
/*	@Autowired
	private SolrI solrService;*/
/*	private static List<String> cateSort=new LinkedList<String>(){{
		add("电气工程及工业自动化");add("个人防护、安防、标识");add("生产加工、磨具磨料");
		add("工具及工具耗材");add("测量、仪表、仪器设备");add("机械部件及辅件");
		add("物流保管、包材");add("办公住宅、清洁用品");
	}};*/
/*	private static List<String> cateSecondSort=new LinkedList<String>(){{
		add("配电及控制电器");add("工业控制元件");
	}};*/
	
	private  Integer[] showCate = {50,40,80};
	private  Integer[] mallCate = {50,60,10,20,30,40,70,80,90}; 
	
	/**
	 *查询所有存在上架商品的系统分类
	 */
	@Override
	//@Cacheable(value = "category", key="'allOn'")
	public List selectCategory(String param) {
		if(StringUtils.isBlank(param)){
			return new ArrayList<>();
		}
		if("3".equals(param)){
			showCate = mallCate;
		}
		List<SelectCategory> categoryTotal = categoryDao.selectCategory(Integer.parseInt(param),showCate);
		List<SelectCategory> oneCategory = getFirstCate(categoryTotal);
		List<SelectCategory> twoCategory = getSecondCate(categoryTotal);
		//归类
		//把第三级放置第二级
		toClassified(twoCategory,categoryTotal);
		//把第二级放置第一级
		toClassified(oneCategory,twoCategory);
		for (SelectCategory category : oneCategory) {
			category.setBrand(categoryDao.findCategoryBrand(category.getId()));
		}
		return oneCategory;
	}
	/**
	 * 分类归类
	 * @param topCategory 父级分类
	 * @param categoryTotal 所有分了
	 */
	private void toClassified(List<SelectCategory> topCategory,
			List<SelectCategory> categoryTotal) {
		for (SelectCategory category : topCategory) {
			for (SelectCategory cate : categoryTotal) {
				if(cate.getParentId().equals(category.getId())){
//					cate.setId(null);
					category.getChildrens().add(cate);
				}
			}
		}
	}
	/**
	 * 获取第二级分类
	 * @param categoryTotal
	 * @return
	 */
	private List<SelectCategory> getSecondCate(List<SelectCategory> categoryTotal) {
		List<SelectCategory> list = new ArrayList<SelectCategory>();
		List<Integer> exist = Arrays.asList(showCate);
		for (SelectCategory category : categoryTotal) {
			if(exist.contains(category.getParentId())){
				list.add(category);
			}
		}
		return list;
	}
	/**
	 * 获取第一级分类
	 * @param categoryTotal
	 * @return
	 */
	private List<SelectCategory> getFirstCate(List<SelectCategory> categoryTotal) {
		List<SelectCategory> list = new ArrayList<SelectCategory>();
		List<Integer> exist = Arrays.asList(showCate);
		for (SelectCategory category : categoryTotal) {
			if(exist.contains(category.getId())){
				list.add(category);
			}
		}
		return list;
	}
	
	
	@Override
	public List getTopCategory() {
		List<Category> list = categoryDao.getFirstCategory();
		return list;
	}

	@Override
	//@Cacheable(value = "category", key="'face-'+#cateId+'-'+#brandId")
	public List getChildAllCate(String cateId, String brandId) {
		List<String> categoryArray = new ArrayList<String>();
		
		//Map faceGoodsMap = solrService.searchFaceGoods(null, null, Integer.parseInt(brandId), Integer.parseInt(cateId), null, null, null, 1, 1);
		//List<Map> list = (List<Map>) faceGoodsMap.get("categorys");
		List<String> list = categoryDao.getMoCate(brandId,cateId);
        List<String> idArray = Lists.newArrayList();
		if(list.size()>0 && list != null){
             idArray = categoryDao.getParentCate(list,cateId);
            boolean is = true;
            while (is) {
                list.addAll(idArray);
                idArray = categoryDao.getParentCate(idArray,cateId);
                if(idArray.size()<1){
                    is = false;
                }
            }
            categoryArray.addAll(list);
        }
        List<Map> cateList = Lists.newArrayList();
        if(categoryArray.size()>0 && categoryArray != null){
            cateList = categoryDao.findCateBylist(categoryArray);
        }
		return cateList;
	}
	@Override
	public List findCateBrand(List map,String cateId) {
		List list = new ArrayList<>();
		if(map != null){
			list = categoryDao.findCateBrandList(map,cateId);
		}
		return list;
	}
	@Override
	public List getMallFloorCate() {
		List<Map<String, Object>> list =  categoryDao.findMallCateFloor();
		for (Map<String, Object> map : list) {
			String id = map.get("id").toString();
			map.put("list", categoryDao.findMallChildCateFloor(id));
		}
		return list;
	}
	@Override
	public List findCateBrand(PageMode model) {
		if(model.contains("cateId")){
			String cateId = model.getStringValue("cateId");
			return categoryDao.findBrandList(cateId);
		}else{
			return new ArrayList<>();		
		}
	}
	@Override
	public List selectAppCategory(String param) {
		
		if(StringUtils.isBlank(param)){
			return new ArrayList<>();
		}
		if("3".equals(param)){
			showCate = mallCate;
		}
		List<SelectCategory> categoryTotal = categoryDao.selectCategory(Integer.parseInt(param),showCate);
		List<SelectCategory> oneCategory = getFirstCate(categoryTotal);
		List<SelectCategory> twoCategory = getSecondCate(categoryTotal);
		for (SelectCategory selectCategory : categoryTotal) {
//			selectCategory.setId(Integer.parseInt(selectCategory.getCateNo()));
		}
		//归类
		//把第三级放置第二级
		toClassified(twoCategory,categoryTotal);
		//把第二级放置第一级
		toClassified(oneCategory,twoCategory); 
		for (SelectCategory category : oneCategory) {
			category.setBrand(categoryDao.findAppCategoryBrand(category.getId(),6));
		}
		
		return oneCategory;  
	
	}
	@Override
//	@Cacheable(value = "selection_category", key="#brandId")
	public List<ModelSelectionCategory> selectModelSelectionCates(Integer brandId) {
		/**根据brandid查询选型顶级分类*/
		List<ModelSelectionCategory> firstCategoryList =  categoryDao.getFirstModelSelectionCategory(brandId);
		/**查询非顶级的所有选型分类*/
		List<ModelSelectionCategory> allCategoryList = categoryDao.getNoTopModelSelectionCategory();
		allCategoryList.addAll(firstCategoryList);
		List<ModelSelectionCategory> childrenList = Lists.newArrayList();
		if(allCategoryList!=null && allCategoryList.size()>0){
			for(ModelSelectionCategory modelSelectionCategory : allCategoryList){
				if(modelSelectionCategory.getParentId()==0){
					ModelSelectionCategory category = this.getChildren(modelSelectionCategory,allCategoryList);
					childrenList.add(category);
				}
				
			}
			
		}
//		System.out.println(JSONObject.toJSONString(childrenList));
		return childrenList;
	}
	
	private ModelSelectionCategory getChildren(ModelSelectionCategory modelSelectionCategory,List<ModelSelectionCategory> categoryList){
		if(categoryList!=null && categoryList.size()>0){
			for(ModelSelectionCategory item:categoryList){
				if(item.getParentId().equals(modelSelectionCategory.getCateId())){
					if(modelSelectionCategory.getCateId()==920){
						System.out.println(item.getCateId());
					}
					modelSelectionCategory.getChildrens().add(getChildren(item,categoryList));
				}
			}
			return modelSelectionCategory;
		}
		return null;
	}

	//查询当前categoryId的所有父节点



}
