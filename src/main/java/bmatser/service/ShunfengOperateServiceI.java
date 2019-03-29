package bmatser.service;

import bmatser.pageModel.ShunfengOperatePage;

import java.util.List;
import java.util.Map;


public interface ShunfengOperateServiceI {
	
	/**下单**/
	public String orderService(ShunfengOperatePage shunfengOperatePage) throws Exception;
	
	/**子单申请**/
	public String orderZDService(ShunfengOperatePage shunfengOperatePage) throws Exception;
	
	/**顺丰下单取消**/
	public void orderConfirmService(ShunfengOperatePage shunfengOperatePage) throws Exception;
	
	/**顺丰下单结果查询**/
	public void orderSearchService(ShunfengOperatePage shunfengOperatePage) throws Exception;
	
	/**订单取消**/
	public void cancelOrder(ShunfengOperatePage shunfengOperatePage);
	/**结果查询**/
	public String orderSearchResult(ShunfengOperatePage shunfengOperatePage);
	/**顺丰快递路由查询**/
	public List<Map<String,Object>> orderRouteSearch(String orderId) throws Exception;

	public String printWaybill(String orderId) throws Exception;
	
}
