package bmatser.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import bmatser.model.CrmCustomer;


public interface CrmCustomerMapper {

	int updateById(CrmCustomer crmCustomer);
	
	List<CrmCustomer> find();
	
	void save(CrmCustomer customer);
	
	void update(CrmCustomer customer);
	
	Integer selectId(CrmCustomer customer);
	
	Integer selectMaxId(@Param("dealerId") int dealerId);
	
	int insert(CrmCustomer customer);
	
	//更新客户信息表
	int updateByDealerId(CrmCustomer customer);

	void updateCustomerRank(Integer buyerId);

	CrmCustomer getCustomerByDealerId(String dealerId);

	void updateCrmCustomerCode(@Param("customerNo")Integer customerNo, @Param("dealerId")String dealerId);
}