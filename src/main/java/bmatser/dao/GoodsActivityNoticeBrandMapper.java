package bmatser.dao;

import bmatser.model.GoodsActivityNoticeBrand;

public interface GoodsActivityNoticeBrandMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsActivityNoticeBrand record);

    int insertSelective(GoodsActivityNoticeBrand record);

    GoodsActivityNoticeBrand selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsActivityNoticeBrand record);

    int updateByPrimaryKey(GoodsActivityNoticeBrand record);
}