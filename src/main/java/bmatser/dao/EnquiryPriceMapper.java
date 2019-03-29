package bmatser.dao;

import java.util.List;
import java.util.Map;

import bmatser.model.EnquiryPrice;

public interface EnquiryPriceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EnquiryPrice record);

    int insertSelective(EnquiryPrice record);

    EnquiryPrice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EnquiryPrice record);

    int updateByPrimaryKey(EnquiryPrice record);

	List<EnquiryPrice> findEnquiryPriceList(Integer dealerId);

	Map<String,Object> findEnquiryPriceById(Integer enquiryPriceId);
}