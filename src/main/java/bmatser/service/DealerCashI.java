package bmatser.service;

import java.util.Map;

import bmatser.model.DealerCash;

public interface DealerCashI {

	Map<String, Object> findByBuyerId(Integer buyerId, Integer page, Integer rows);
	
	void insertDealerCash(DealerCash dealerCash);

}
