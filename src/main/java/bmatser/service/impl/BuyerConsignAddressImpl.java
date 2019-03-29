package bmatser.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.BuyerConsignAddressMapper;
import bmatser.dao.ShoppingCartMapper;
import bmatser.pageModel.BuyerConsignAddressPage;
import bmatser.service.BuyerConsignAddressI;
import bmatser.util.DESCoder;
import bmatser.util.ObjectUtil;

/**
 * 收地地址维护类
 * @author felx
 * 2017-7-24
 */
@Service("consignAddressService")
public class BuyerConsignAddressImpl implements BuyerConsignAddressI {
	
	@Autowired
	private BuyerConsignAddressMapper consignAddressDao;
	@Autowired
	private ShoppingCartMapper cartDao;

	/**
	 * 获取指定用户的收货地址
	 * @param dealerId
	 * @return list
	 * 2017-7-23
	 * @throws Exception 
	 *
	 */
	@Override
	public List<BuyerConsignAddressPage> findByBuyerId(Integer buyerId) throws Exception {
		if(buyerId == null){
			throw new Exception("参数不存在");
		}
		List<BuyerConsignAddressPage> list = consignAddressDao.findByBuyerId(buyerId);
//		DESCoder.instanceMobile();
//		for(BuyerConsignAddressPage page : list){
////			page.put("mobile", DESCoder.decoder(page.get("mobileSecret").toString()));
//            page.setMobile(DESCoder.decoder(page.getMobile().toString()));
//		}
		return list;
	}

	/**
	 * 设置默认收货方式
	 * @param id
	 * @param dealerId
	 * @return int
	 * 2017-7-23
	 * @throws Exception 
	 *
	 */
	@Override
	public void setDefault(Integer id, Integer buyerId) throws Exception {
		if(id == null || buyerId == null){
			throw new Exception("参数不能为空");
		}
		/**先将原有收货地址设为不默认*/
		consignAddressDao.setNoDefault(buyerId);
		consignAddressDao.setDefault(id);
	}

	/**
	 * 新增收货地址
	 * @param consignAddressPage
	 * @return int
	 * 2017-7-23
	 * @throws Exception 
	 *
	 */
	@Override
	public Integer add(BuyerConsignAddressPage consignAddressPage) throws Exception {
		consignAddressPage.checkData();
		/**如果缺省值为null或空，更改为0*/
		if(StringUtils.isBlank(consignAddressPage.getIsDefault())){
			consignAddressPage.setIsDefault("1");
		}
		/**新增收货地址如果默认收货地址，则将原有收货地址默认字段设为不默认*/
		if(StringUtils.equals(consignAddressPage.getIsDefault(), "1")){
			consignAddressDao.setNoDefault(consignAddressPage.getBuyerId());
		}
		Integer countryId = consignAddressPage.getCountryId();
		/**如果国家ID为空，则默认中国*/
		if(countryId == null || countryId == 0){
			consignAddressPage.setCountryId(100);
		}

		DESCoder.instanceMobile();
		if(null!=consignAddressPage.getMobile() && !consignAddressPage.getMobile().contains("*")){
			consignAddressPage.setMobileSecret(DESCoder.encoder(consignAddressPage.getMobile()));//对手机号码加密
			consignAddressPage.setMobile(DESCoder.getNewTel(consignAddressPage.getMobile()));//隐藏手机号码
		}else{
			throw new Exception("手机号码格式不正确");
		}
		if(null!=consignAddressPage.getTelephone() && !consignAddressPage.getTelephone().contains("*")){
			consignAddressPage.setTelephoneSecret(DESCoder.encoder(consignAddressPage.getTelephone()));//对电话加密
			consignAddressPage.setTelephone(DESCoder.getNewTel(consignAddressPage.getTelephone()));//隐藏电话
		}else{
			consignAddressPage.setTelephone(null);
		}
		consignAddressDao.add(consignAddressPage);
		return consignAddressPage.getId();
	}

	/**
	 * 修改收货地址信息
	 * @param consignAddressPage
	 * @return int
	 * 2017-7-24
	 * @throws Exception 
	 *
	 */
	@Override
	public int edit(BuyerConsignAddressPage consignAddressPage) throws Exception {
		Integer id = consignAddressPage.getId();
		if(id == null){
			throw new Exception("收货地址不存在");
		}
		if(StringUtils.equals(consignAddressPage.getIsDefault(), "1")){
			consignAddressDao.setNoDefault(consignAddressPage.getBuyerId());
		}
		consignAddressPage.checkData();
		
		DESCoder.instanceMobile();
		if(null != consignAddressPage.getMobile() && !"".equals(consignAddressPage.getMobile()) && !consignAddressPage.getMobile().contains("*")){
			consignAddressPage.setMobileSecret(DESCoder.encoder(consignAddressPage.getMobile()));//对手机号码加密
			consignAddressPage.setMobile(DESCoder.getNewTel(consignAddressPage.getMobile()));//隐藏手机号码
		}else{
			consignAddressPage.setMobile(null);
		}
		if(null != consignAddressPage.getTelephone() && !"".equals(consignAddressPage.getTelephone()) && !consignAddressPage.getTelephone().contains("*")){
			consignAddressPage.setTelephoneSecret(DESCoder.encoder(consignAddressPage.getTelephone()));//对电话加密
			consignAddressPage.setTelephone(DESCoder.getNewTel(consignAddressPage.getTelephone()));//隐藏电话
		}else{
			consignAddressPage.setTelephone(null);
		}
		
		return consignAddressDao.edit(consignAddressPage);
	}

	/**
	 * 删除指定ID的收货地址
	 * @param id
	 * @return int
	 * 2017-7-24
	 * @throws Exception 
	 *
	 */
	@Override
	public int delete(Integer id) throws Exception {
		if(id == null){
			throw new Exception("地址不存在");
		}
		return consignAddressDao.delete(id);
	}

	/**
	 * 获取默认地址
	 * @param buyerId
	 * @return
	 * 2017-9-24
	 *
	 */
	@Override
	public Map getDefaultByBuyerId(Integer buyerId) {
		Map map = consignAddressDao.getDefaultByBuyerId(buyerId);
		if(map == null || map.isEmpty()){
			map = new HashMap();
		}
		return map;
	}

	/**
	 * 获取地址详情
	 * @param id
	 * @return
	 * 2017-9-25
	 *
	 */
	@Override
	public Map getConsignAddressById(Integer id) {
		return consignAddressDao.getConsignAddressById(id);
	}
	/**
	 * 删除地址
	 */
	@Override
	public int delete(String id, String buyerId) {
		return consignAddressDao.deleteByPage(id,buyerId);
	}

	@Override
	public List<BuyerConsignAddressPage> findByBuyerId(Integer dealer,
			String ids) throws Exception {
		ObjectUtil.isReturn(dealer,"参数不存在");
		int isjd = 0;
		/*ObjectUtil.isReturn(ids,"购物车不能为空");*/
		if(StringUtils.isNotBlank(ids)){
			isjd = cartDao.isExistJdGoods(ids.split(","));
		}		
		return consignAddressDao.findAddrByChannel(dealer,StringUtils.isNotBlank(ids)?(isjd>0?1:0):2);
	}

}
