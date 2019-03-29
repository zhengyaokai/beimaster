package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.pageModel.BuyerInvoicePage;

public interface BuyerInvoiceMapper {

	List findByBuyerId(@Param("buyerId")Integer buyerId);
	
	int setNoDefault(@Param("buyerId")Integer buyerId);
	
	int setDefault(@Param("id")Integer id);
	
	int add(BuyerInvoicePage invoicePage);
	
	int edit(BuyerInvoicePage invoicePage);
	
	int delete(Integer id);
	
	Map getDefaultByBuyerId(@Param("buyerId")Integer buyerId);
	
	Map findInvoiceById(@Param("id")Integer id);

	int deleteByPage(@Param("id")String id, @Param("buyerId")String buyerId);

	//商城首页获取发票列表 
	List getMallInvoiceList(@Param("buyerId") Integer buyerId);

	Map selectIsWhite(@Param("buyerId") Integer buyerId);
    //专票新增上传开票申请(发票的开票申请不在buyer_invoice这张表)
	void addCustomerQualified(@Param("buyerId") Integer buyerId,@Param("qualifications") String qualifications);
	//专票修改上传开票申请(发票的开票申请不在buyer_invoice这张表)
	void updateCustomerQualified(@Param("buyerId") Integer buyerId,@Param("qualifications") String qualifications,
			@Param("isReview") String isReview);

	List getInvoiveDetialList(@Param("dealerId") Integer dealerId,@Param("orderId") Long orderId,
			@Param("fjsNo") String fjsNo,
			@Param("page") int page,@Param("rows") int rows);

	int getInvoiveDetialListCount(@Param("dealerId") Integer dealerId,@Param("orderId") Long orderId,
			@Param("fjsNo") String fjsNo);
}