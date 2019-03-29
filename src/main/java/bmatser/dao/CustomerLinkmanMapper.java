package bmatser.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import bmatser.model.CustomerLinkman;

public interface CustomerLinkmanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerLinkman record);

    int insertSelective(CustomerLinkman record);
    
    //更新客户信息表
    int updateByCustomerId(CustomerLinkman record);

    CustomerLinkman selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerLinkman record);

    int updateByPrimaryKey(CustomerLinkman record);
    
	List<CustomerLinkman> selectLinkmanById(@Param("customerId")int customerId);
	
	void deleteByCustomerId(@Param("customerId")int customerId);
}