package bmatser.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import bmatser.pageModel.PaymentAccountPage;

public interface PaymentAccountMapper {
	int add(PaymentAccountPage page);
	int setNoDefault(@Param("buyerId")Integer buyerId);
	int setDefault(@Param("id")Integer id);
	int update(PaymentAccountPage page);
	int delete(@Param("id") String id);
	List findBankList();
	List findPALByBuyerId(@Param("buyerId") Integer buyerId);
}
