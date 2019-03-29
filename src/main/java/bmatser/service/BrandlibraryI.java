package bmatser.service;

import java.util.List;
import java.util.Map;

public interface BrandlibraryI {
	/**
	 * 查询所有有效品牌
	 * @return
	 */
	List findBrandlibrary();
	/**
	 * 查询八大类
	 * @return
	 */
	List findBrandlibraryCate();
	/**
	 * 查询品牌下的商品
	 * @param page 
	 * @param brandId 
	 * @return
	 */
	Map findBrandlibraryGoods(String brandId, int page);
	
	/**
	 * 查看该品牌明细
	 * @param brandId
	 * @return
	 */
	Map findBrandlibraryInfo(String brandId);

}
