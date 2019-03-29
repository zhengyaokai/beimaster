package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.pageModel.BuyerConsignAddressPage;

public interface BuyerConsignAddressMapper {
   
	List<BuyerConsignAddressPage> findByBuyerId(@Param("buyerId")Integer buyerId);
	
	int setDefault(@Param("id")Integer id);
	
	int setNoDefault(@Param("buyerId")Integer buyerId);
	
	int add(BuyerConsignAddressPage consignAddressPage);
	
	int edit(BuyerConsignAddressPage consignAddressPage);
	
	int delete(Integer id);
	
	Map getDefaultByBuyerId(@Param("buyerId")Integer buyerId);
	Map getDefaultJDByBuyerId(@Param("buyerId")Integer buyerId);
	
	Map getConsignAddressById(@Param("id")Integer id);

	Map findById(Integer parseInt);

	int deleteByPage(@Param("id")String id,@Param("buyerId")String buyerId);
	
	Map selectAddress(Integer consignAddressId);

	Map<String, Object> findJsonAddress(Integer consignAddressId);

	Map<String, Object> findJsonSecretAddress(Integer consignAddressId);

	List<BuyerConsignAddressPage> findAddrByChannel(@Param("buyerId")Integer dealer,@Param("type")int i);
}