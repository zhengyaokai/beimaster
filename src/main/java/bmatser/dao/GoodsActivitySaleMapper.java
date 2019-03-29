package bmatser.dao;

import bmatser.model.GoodsActivitySale;

public interface GoodsActivitySaleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsActivitySale record);

    int insertSelective(GoodsActivitySale record);

    GoodsActivitySale selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsActivitySale record);

    int updateByPrimaryKey(GoodsActivitySale record);
}