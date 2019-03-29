package bmatser.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.EnquiryPriceGoodsMapper;
import bmatser.dao.EnquiryPriceMapper;
import bmatser.exception.GdbmroException;
import bmatser.model.EnquiryPrice;
import bmatser.model.EnquiryPriceGoods;
import bmatser.service.EnquiryPriceListI;

/**
 * 询价列表
 * @author felx
 * 2018-8-20
 */
@Service
public class EnquiryPriceListImpl implements EnquiryPriceListI {
	@Autowired
	EnquiryPriceMapper enquiryPriceMapper;
	@Autowired
	EnquiryPriceGoodsMapper enquiryPriceGoodsMapper;
	
	@Override
	public List<EnquiryPrice> findEnquiryPriceList(Integer dealerId) {
		try{
			return enquiryPriceMapper.findEnquiryPriceList(dealerId);
		}catch(Exception e){
			throw new GdbmroException("get records failed");
		}
		
		
	}

	@Override
	public Object findEnquiryPriceGoods(Integer enquiryPriceId) {
		try{
			Map<String,Object> enquiryPrice = enquiryPriceMapper.findEnquiryPriceById(enquiryPriceId);
			List<EnquiryPriceGoods> list = enquiryPriceGoodsMapper.findEnquiryPriceGoods(enquiryPriceId);
			enquiryPrice.put("goods",list);
			return enquiryPrice;
		}catch(Exception e){
			throw new GdbmroException("get records failed");
		}
		
	}
	

}
