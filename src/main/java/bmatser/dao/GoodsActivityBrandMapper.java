package bmatser.dao;

import bmatser.model.GoodsActivityBrand;

public interface GoodsActivityBrandMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsActivityBrand record);

    int insertSelective(GoodsActivityBrand record);

    GoodsActivityBrand selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsActivityBrand record);

    int updateByPrimaryKey(GoodsActivityBrand record);
}