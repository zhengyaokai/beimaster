package bmatser.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.model.FreightAmount;

public interface DealerExpressMapper {

	FreightAmount findFreightAmount(String sellerId);
	
	FreightAmount findFreightAmountFirst(@Param("code")int code);

}
