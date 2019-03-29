package bmatser.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.CrmCustomerMapper;
import bmatser.model.CrmCustomer;
import bmatser.model.Dealer;
import bmatser.service.CrmCustomerI;

/**
 * 客户信息
 * @author felx
 * @date 2016.02.25 
 */
@Service("crmCustomer")
public class CrmCustomerImpl implements CrmCustomerI {
	
	@Autowired
	private CrmCustomerMapper crmCustomerDao;
	/**
	 * 更具商家ID，更新公司名称
	 * @param CrmCustomer crmCustomer
	 * @return 
	 */
	/*@Override
	public int updateById(CrmCustomer crmCustomer)throws Exception {
			return crmCustomerDao.updateById(crmCustomer);
	}*/
	
}
