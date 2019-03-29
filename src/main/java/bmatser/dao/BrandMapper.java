package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BrandMapper {

	List findBrands();
	
	List findBrandsByMark(@Param("markType")int markType);
	
	List findValidBrands();
	
	List findAllBrands();
	
	Map findBrandsById(String brandId);
	
	List selectForSaleBrands();
	
	List selectRecommendBrands();
	
	List selectfaceBrands();

	List selectsaleBrands();
	
	String selectERPbrand(String id);

	List selectappMallBrands();

	List findChoiceBrands();

    /**
     * 精选品牌ForOIos
     *
     * @return
     */
    List<Map<String,Object>> findChoiceBrandsForIos();

    //获得所有品牌
    List<Map<String,Object>> findValidBrandsForIos();
	
}