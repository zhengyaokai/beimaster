package bmatser.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.pageModel.BuyerConsignAddressPage;

public interface BuyerConsignAddressI {

	List<BuyerConsignAddressPage> findByBuyerId(Integer buyerId) throws Exception;
	
	void setDefault(Integer id, Integer buyerId)throws Exception;
	
	Integer add(BuyerConsignAddressPage consignAddressPage)throws Exception ;
	
	int edit(BuyerConsignAddressPage consignAddressPage)  throws Exception ;
	
	int delete(Integer id)  throws Exception;
	
	Map getDefaultByBuyerId(Integer buyerId);
	
	Map getConsignAddressById(Integer id);

	int delete(@Param("id")String id, @Param("buyerId")String buyerId);

	List<BuyerConsignAddressPage> findByBuyerId(Integer dealer, String ids) throws Exception;
}
