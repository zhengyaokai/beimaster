package bmatser.dao;

import java.util.List;

import bmatser.model.LogisticsCompany;

public interface LogisticsCompanyMapper {
	
	List<LogisticsCompany> findAll();
	
	List<LogisticsCompany> findByPage(LogisticsCompany logCom);
	
	LogisticsCompany fingById(Integer id);

	LogisticsCompany fingByName(String name);
}
