package bmatser.dao;

import bmatser.model.OrderInvoice;

public interface OrderInvoiceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderInvoice record);

    int insertSelective(OrderInvoice record);

    OrderInvoice selectByPrimaryKey(Integer id);
 
    int updateByPrimaryKeySelective(OrderInvoice record);

    int updateByPrimaryKey(OrderInvoice record);
    
	void save(OrderInvoice orderInvoice);
	
	OrderInvoice findById(Integer id);
	
	OrderInvoice findByOrderId(Long id);
}