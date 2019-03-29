package bmatser.service;

import java.util.List;

import bmatser.pageModel.BuyerConsignAddressPage;
import bmatser.pageModel.PaymentAccountPage;

public interface PaymentAccountI {
	int add(PaymentAccountPage page) throws Exception ;
	int update(PaymentAccountPage page)  throws Exception ;
	int delete(String id)  throws Exception ;
	List findBankList() throws Exception ;
	List findPALByBuyerId(Integer buyerId) throws Exception ;
	void setDefault(Integer id, Integer buyerId)throws Exception;
	
}
