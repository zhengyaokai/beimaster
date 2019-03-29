package bmatser.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import bmatser.model.DealerCash;
import bmatser.pageModel.util.RegisterPageUtil;

public interface DealerCashMapper {
    
	List findByBuyerId(@Param("buyerId")Integer buyerId, @Param("page")int page, @Param("rows")int rows);
	
	Long countByBuyerId(@Param("buyerId")Integer buyerId);

	int findCash(Integer dealerId);
	
	void insertDealerCash(DealerCash dealerCash);

	void addByMallRegister(RegisterPageUtil registerPage);
}