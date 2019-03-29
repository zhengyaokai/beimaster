package bmatser.job;

import bmatser.service.ErpManageI;
import org.springframework.beans.factory.annotation.Autowired;

public class JiQingErpOrderJob {
	
	@Autowired
	private ErpManageI erpManageService;
	
	/**
	 * 更新订单出库状态
	 * @author zyk
	 */
	public void updateOrdersLogisJob() throws  Exception{
		erpManageService.updateOrdersLogisJob();
	}

	/**
	 * 同步sku到ERP
	 */
	public void updateErpGoodSku() {
		erpManageService.updateErpGoodSku();
	}


}
