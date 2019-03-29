package bmatser.dao;

import bmatser.model.CrmCustomerService;

public interface CustomerServiceMapper {

	void insert(CrmCustomerService  customerService);
	
	void insertSelective(CrmCustomerService  customerService);
	
	//更新客户所对应的客服 表
	void updateByCustomerId(CrmCustomerService  customerService);
	
}
