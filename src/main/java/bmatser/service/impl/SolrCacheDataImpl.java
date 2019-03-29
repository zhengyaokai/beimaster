package bmatser.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import bmatser.dao.BrandMapper;
import bmatser.dao.CategoryMapper;
import bmatser.service.SolrCacheDataI;

@Service("cacheDataService")
public class SolrCacheDataImpl implements SolrCacheDataI {
	
	@Autowired
	private BrandMapper brandDao;
	
	@Autowired
	private CategoryMapper categoryDao;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Cacheable(value = "solr", key = "'attrCache'")
	public Map findCategoryAttr() {
		List list = categoryDao.findCategoryAttrs();
		Map map = new HashMap();
		for(Object obj : list){
			Map objMap = (Map)obj;
			Long attrValueId = (Long)objMap.get("attrValueId");
			map.put(attrValueId, objMap);
		}
		return map;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Cacheable(value = "solr", key = "'brandCache'")
	public Map findBrand() {
		List list = brandDao.findValidBrands();
		Map map = new HashMap();
		for(Object obj : list){
			Map objMap = (Map)obj;
			Integer attrValueId = (Integer)objMap.get("id");
			map.put(attrValueId, objMap.get("name"));
		}
		return map;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Cacheable(value = "solr", key = "'categoryCache'")
	public Map findCategory() {
		List list = categoryDao.findValidCategories();
		Map map = new HashMap();
		for(Object obj : list){
			Map objMap = (Map)obj;
			Integer attrValueId = Integer.valueOf(objMap.get("id").toString());
			map.put(attrValueId, objMap.get("name"));
		}
		return map;
	}

}
