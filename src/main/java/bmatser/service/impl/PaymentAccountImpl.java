package bmatser.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import bmatser.dao.PaymentAccountMapper;
import bmatser.pageModel.BuyerConsignAddressPage;
import bmatser.pageModel.PaymentAccountPage;
import bmatser.service.PaymentAccountI;

@Service("paymentAccount")
public class PaymentAccountImpl implements PaymentAccountI{
	@Autowired
	private PaymentAccountMapper paymentAccountMapper;
	/**
	 * 新增付款账号
	 * @param page
	 * @return int
	 * 2016-7-18
	 * @throws Exception 
	 *
	 */
	@Override
	public int add(PaymentAccountPage page) throws Exception {
		if(StringUtils.isBlank(page.getIsDefault())){
			page.setIsDefault("1");
		}
		/**新增收货地址如果默认付款账号，则将原有付款账号默认字段设为不默认*/
		if(StringUtils.equals(page.getIsDefault(), "1")){
			paymentAccountMapper.setNoDefault(page.getBuyerId());
		}
		page.setStatus("1");
		return paymentAccountMapper.add(page);
		
	}
	/**
	 * 修改付款账号
	 * @param page
	 * @return int
	 * 2016-7-18
	 * @throws Exception 
	 *
	 */
	@Override
	public int update(PaymentAccountPage page) throws Exception {
		Integer id = page.getId();
		if(id == null){
			throw new Exception("付款账号不存在");
		}
		return paymentAccountMapper.update(page);
	}
	
	/**
	 * 删除付款账号
	 */
	@Override
	public int delete(String id) throws Exception{
		return paymentAccountMapper.delete(id);
	}
	/**
	 * 查询银行列表
	 */
	public List findBankList() throws Exception{
		return paymentAccountMapper.findBankList();
	}
	/**
	 * 查询银行账号列表
	 */
	public List findPALByBuyerId(Integer buyerId) throws Exception{
		if(buyerId == null){
			throw new Exception("参数不存在");
		}
		return paymentAccountMapper.findPALByBuyerId(buyerId);
	}
	/**
	 * 设置默认付款账号
	 * @param id
	 * @param dealerId
	 * @return int
	 * 2016-7-19
	 * @throws Exception 
	 *
	 */
	@Override
	public void setDefault(Integer id, Integer buyerId) throws Exception {
		if(id == null || buyerId == null){
			throw new Exception("参数不能为空");
		}
		/**先将原有付款账号设为不默认*/
		paymentAccountMapper.setNoDefault(buyerId);
		paymentAccountMapper.setDefault(id);
	}
}
