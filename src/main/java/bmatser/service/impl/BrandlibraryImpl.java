/**
 * 
 */
package bmatser.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.BrandMapper;
import bmatser.dao.CategoryMapper;
import bmatser.dao.SellerGoodsMapper;
import bmatser.service.BrandlibraryI;

/**
 *	@create_date 2017-12-23
 * 
 */
@Service("BrandlibraryService")
public class BrandlibraryImpl implements BrandlibraryI {
	
	@Autowired
	private BrandMapper brandMapper;
	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private SellerGoodsMapper goodsMapper;
	/**
	 * 查询所有有效品牌
	 */
	@Override
	public List findBrandlibrary() {
		return brandMapper.findAllBrands();
	}
	/**
	 * 查询八大类
	 */
	@Override
	public List findBrandlibraryCate() {
		return categoryMapper.findEightToCategory();
	}
	/**
	 * 查询该品牌下商品
	 */
	@Override
	public Map findBrandlibraryGoods(String brandId, int page) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("goodsList", goodsMapper.findGoodsByBrand(page*10, 10, brandId));
		map.put("count", goodsMapper.findGoodsByBrandCount(brandId));
		return map;
	}
	/**
	 * 查看该品牌明细
	 */
	@Override
	public Map findBrandlibraryInfo(String brandId) {
		return brandMapper.findBrandsById(brandId);
	}

}
