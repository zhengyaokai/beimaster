package bmatser.service;

import java.util.Map;

public interface KingnorServiceI {

	Map<String, Object> getOrderList(String request);

	Map<String, Object> saveCustomerWarning(String request);
	
	/**
	 * 放款通知
	 * @param request
	 * @return
	 */
	public Map<String, Object> updateLoanInform(String request);
	
}
