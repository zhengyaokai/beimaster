package bmatser.service;

import java.util.List;
import java.util.Map;

public interface BrandI {

	List findBrands();
	
	List findBrandsByMark(int markType);
	/*
	 * 查询出当前在售的所有品牌
	 */
	Map selectForSaleBrands(int goodsInitial);

	Map selectForAppSaleBrands(int code);

	/**
	 * @author felx
	 * @describe 查询精选品牌
	 * @return
	 */
	List findChoiceBrands();

    /**
     * @author Yr
     * @describe 查询精选品牌for Ios
     * @return
     */
    Map<String,Object> findChoiceBrandsForIos();

    //获得ios所有品牌 create by yr on 2018-11-20
    List<Map<String,Object>> findVaildBrandForIos();

}
