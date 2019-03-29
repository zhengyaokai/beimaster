package bmatser.service.impl;

import bmatser.dao.SysMessageMapper;
import bmatser.util.SortAndCategory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import bmatser.dao.BrandMapper;
import bmatser.service.BrandI;

@Service("brandService")
public class BrandImpl implements BrandI {

	@Autowired
	private BrandMapper brandDao;



    /**
	 * 获取已上架品牌
	 * @return
	 * 2017-9-7
	 *
	 */
	@Override
	@Cacheable(value = "brand", key="'brandList'")
	public List findBrands() {
		return brandDao.findBrands();
	}

	/**
	 * 根据标记获取品牌
	 * @param markType
	 * @return
	 * 2017-9-7
	 *
	 */
	@Override
	public List findBrandsByMark(int markType) {
		return brandDao.findBrandsByMark(markType);
	}
	/*
	 * 查询出当前在售的所有品牌
	 */
	@Override
	@Cacheable(value = "brand", key="'salebrand-'+#goodsInitial")
	public Map selectForSaleBrands(int goodsInitial) {
		Map<Object,Object> brandRecommend=new LinkedHashMap<Object,Object>();
		brandRecommend.put("RecommendedBrand",brandDao.selectRecommendBrands());
		switch (goodsInitial) {
		case 1://上架
			brandRecommend.put("AllBrands",brandDao.selectsaleBrands());
			break;
		case 2://面价
			brandRecommend.put("AllBrands",brandDao.selectfaceBrands());
			break;
		default://上架
			brandRecommend.put("AllBrands",brandDao.selectForSaleBrands());
			break;
		}
		return brandRecommend;
	}

	@Override
	//@Cacheable(value = "appBrand", key="'salebrand-'+#goodsInitial")
	public Map selectForAppSaleBrands(int goodsInitial) {
		Map<Object,Object> brandRecommend=new LinkedHashMap<Object,Object>();
		List li = new ArrayList();
		if(goodsInitial==3){
			List l=brandDao.selectappMallBrands();
			for(int i=0;i<15;i++){
				if(i+1<=l.size()){
					li.add(l.get(i));
				}else{
					break;
				}
			}
			brandRecommend.put("RecommendedBrand",li);
		}else{
			brandRecommend.put("RecommendedBrand",brandDao.selectRecommendBrands());
		}
		List list = null;
		switch (goodsInitial) {
		case 1://上架
			list = brandDao.selectsaleBrands();
			break;
		case 2://面价
			list = brandDao.selectfaceBrands();
			break;
		case 3://appmall
			list = brandDao.selectappMallBrands();
			break;	
		default://上架
			list = brandDao.selectForSaleBrands();
			break;
		}
		Map<String, List<Object>> map = new TreeMap<>();
		for (Object o : list) {
			Map<String, Object> brand= (Map<String, Object>) o;
			Object brandName = brand.get("nameCn");
			String firstChar = "";
			if(brandName==null ||StringUtils.isBlank(brandName.toString())){
				firstChar="#";
			}else{
				firstChar = brandName.toString().substring(0, 1).toUpperCase();
				if("0123456789".indexOf(firstChar)>=0){
					firstChar="#";
				}else if(firstChar.getBytes().length>1){
					String s = PinyinHelper.toHanyuPinyinStringArray(firstChar.charAt(0))[0];
					firstChar = s.substring(0, 1).toUpperCase();
				}
			}
			if(map.containsKey(firstChar)){
				map.get(firstChar).add(o);
			}else{
				List<Object> brands = new ArrayList<>();
				brands.add(o);
				map.put(firstChar, brands);
			}
		}
		List<Map<String, Object>> result = new ArrayList<>(map.size());
		for (Entry<String, List<Object>> entity : map.entrySet()) {
			Map<String, Object> brand = new HashMap<>();
			brand.put("key", entity.getKey());
			brand.put("list", entity.getValue());
			result.add(brand);
		}
		brandRecommend.put("AllBrands",result);
		return brandRecommend;
	}

	/**
	 * 查询精选平品牌
	 */
	@Override
	public List findChoiceBrands() {

		return brandDao.findChoiceBrands();
	}


    /**
     * 查询精选平品牌For Ios
     */
    @Override
    public Map<String,Object> findChoiceBrandsForIos() {

         List<Map<String,Object>> list = brandDao.findChoiceBrandsForIos();
         Map<String,Object> map = SortAndCategory.toSortAndCategory(list);
         return map;
    }

    @Override
    public List<Map<String, Object>> findVaildBrandForIos() {


        return brandDao.findValidBrandsForIos();
    }
}
