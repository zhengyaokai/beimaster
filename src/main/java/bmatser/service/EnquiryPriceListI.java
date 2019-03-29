package bmatser.service;

import java.util.List;

import bmatser.model.EnquiryPrice;

public interface EnquiryPriceListI {

	List<EnquiryPrice> findEnquiryPriceList(Integer dealerId);
	
	Object findEnquiryPriceGoods(Integer enquiryPriceId);

	
}
