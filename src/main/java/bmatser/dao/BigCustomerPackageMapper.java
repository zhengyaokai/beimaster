package bmatser.dao;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BigCustomerPackageMapper {
	//查询当前用户是否是大客户返回套餐id
	Integer isExist(@Param("dealerId") Integer dealerId);
	//查询商品是否是套餐商品返回折扣
	BigDecimal goodsIsPackage(@Param("id") String id,@Param("bigCustomerPackageId") Integer bigCustomerPackageId);

}
