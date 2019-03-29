package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.model.CrmCustomer;
/**
 * 客户对应客户经理
 * @author felx
 * @date 2016.03.29
 */
public interface CrmCustomerManagerMapper {
   
	int insert(Map crmCustomerManager);
	
	//更新客户对应客户经理表
	int updateByCustomerId(Map crmCustomerManager);
	
}