package bmatser.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import bmatser.pageModel.BuyerInvoicePage;

public interface BuyerInvoiceI {

	List findByBuyerId(Integer buyerId)  throws Exception;
	
	int setDefault(Integer id, Integer buyerId);
	
	int add(BuyerInvoicePage invoicePage, MultipartFile authorImg, MultipartFile licenseFile,MultipartFile invApplicationFile)  throws Exception;
	
	void edit(BuyerInvoicePage invoicePage) throws Exception;
	
	int delete(Integer id);
	
	Map getDefaultByBuyerId(Integer buyerId);
	
	Map<String, Object> findInvoiceById(Integer id);
	
	/**
	 * 商城新增发票信息
	 * @author felx
	 * @date 2016-1-8
	 */
	int addInvoice(BuyerInvoicePage invoicePage) throws Exception;

	int delete(String id, String buyerId);

	Map getMallInvoiceList(Integer buyerId);

	void edit(BuyerInvoicePage invoicePage, MultipartFile authorImg,
			MultipartFile licenseFile,MultipartFile invApplicationFile) throws Exception;

	
	Map findById(String dealerId);

	Map selectIsWhite(Integer buyerId);

	void addApplication(String dealerId, MultipartFile invApplicationFile) throws Exception;

	Map getInvoiveDetialList(Integer dealerId, Long orderId, String fjsNo, int page, int rows);

}
