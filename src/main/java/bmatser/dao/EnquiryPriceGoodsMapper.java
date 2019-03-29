package bmatser.dao;

import java.util.List;

import bmatser.model.EnquiryPriceGoods;
import org.apache.ibatis.annotations.Param;


public interface EnquiryPriceGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EnquiryPriceGoods record);

    int insertSelective(EnquiryPriceGoods record);

    int insertGoodsList(@Param("list") List<EnquiryPriceGoods> list,@Param("batchEpId") Integer batchEpId);

    EnquiryPriceGoods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EnquiryPriceGoods record);

    int updateByPrimaryKey(EnquiryPriceGoods record);

	List<EnquiryPriceGoods> findEnquiryPriceGoods(Integer enquiryPriceId);
}