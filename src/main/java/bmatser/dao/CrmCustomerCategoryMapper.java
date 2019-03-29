package bmatser.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import bmatser.model.CrmCustomer;
import bmatser.model.CrmCustomerCategory;

/**
 * 客户经营类别
 * @author felx
 */
public interface CrmCustomerCategoryMapper {
	/**
	 * 新增客户经营类别
	 * @param CrmCustomerCategory crmCustomerCategory
	 * @return 
	 */
	int insert(CrmCustomerCategory crmCustomerCategory);
	
	/**
	 * 更新客户经营类别 
	 * @param crmCustomerCategory
	 * @return
	 */
	int updateByCustomerId(CrmCustomerCategory crmCustomerCategory);
}