/**
 * 查询订单运费金额
 */
package bmatser.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import bmatser.dao.BuyerConsignAddressMapper;
import bmatser.dao.DealerExpressMapper;
import bmatser.dao.ShoppingCartMapper;
import bmatser.model.FreightAmount;
import bmatser.service.FreightAmountI;

/**
 *	@create_date 2017-12-28
 * 
 */
@Service("FreightAmountService")
public class FreightAmountImpl implements FreightAmountI{

	@Autowired
	private ShoppingCartMapper cartMapper;
	@Autowired
	private DealerExpressMapper expressMapper;
	@Autowired
	private BuyerConsignAddressMapper addressMapper;

	@Override
	public Map getFreightAmount(String cartIds, String buyerAddrId) throws Exception{
		int code = 0;//0:普通地址 1:京东地址
		int isjd = cartMapper.isExistJdGoods(cartIds.split(","));
		if(isjd>0){
			code = 1;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isBlank(cartIds)){
			throw new Exception("参数不全");
		}
		List<Map<String, Object>> carts = cartMapper.findStoreCartAmount(cartIds.split(","));
		BigDecimal amount = BigDecimal.ZERO;
		double freightAmount = 0;
		String provinceId =  "";
		if(carts == null	||	carts.isEmpty()){
			throw new Exception("参数不正确");
		}else if(StringUtils.isNotBlank(buyerAddrId)){
			Map address = addressMapper.findById(Integer.parseInt(buyerAddrId));
			provinceId = address.get("provinceId").toString();
		}
		for (Map<String, Object> cart : carts) {
			if(cart.get("price") != null){
				amount = amount.add(new BigDecimal(cart.get("price").toString()));
			}
		}
		FreightAmount express = expressMapper.findFreightAmountFirst(code);
		if(express != null){
			Map byintervallocation = JSONObject.parseObject(express.getByintervallocation());
			if(StringUtils.isNotBlank(buyerAddrId)){
				Object provinceInfo = byintervallocation.get(provinceId);
				if(provinceInfo==null){
					freightAmount = express.getDefaultExpressPrice();
				}else{
					Map<String, String>  buyerAddr= (Map<String, String>) JSONObject.parse(provinceInfo.toString());
					TreeMap<String, String> treeMap = new TreeMap(buyerAddr);
					String  maxPrice = treeMap.firstKey();
					/**没有免邮*/
					if(StringUtils.isBlank(maxPrice)){
						freightAmount = express.getDefaultExpressPrice();
					}else if (StringUtils.isNotBlank(maxPrice) && amount.doubleValue() < Double.parseDouble(maxPrice)) {
						if(StringUtils.isNotBlank(treeMap.get(maxPrice))){
							/**没有免邮,邮费不为空*/
							freightAmount = Double.parseDouble(treeMap.get(maxPrice));
						}else{
							/**没有免邮,邮费为空*/
							freightAmount = express.getDefaultExpressPrice();
						}
					}else if(StringUtils.isNotBlank(maxPrice) && amount.doubleValue() > Double.parseDouble(maxPrice)){
						freightAmount = 0;
					}
				}
			}else{
				freightAmount = express.getDefaultExpressPrice();
			}
		}else{
			freightAmount = 0;
		}
		int divide = 0;//等分
		int reste = 0;//余数
		if(freightAmount > 0){
			divide = (int) (freightAmount/carts.size());
			reste = (int) (freightAmount - (divide*carts.size()));
		}
		for (int i = 0; i < carts.size(); i++) {
			if(i+1 == carts.size()){
				carts.get(i).put("cartFreightAmount", divide + reste);
			}else{
				carts.get(i).put("cartFreightAmount", divide);
			}
		}
		map.put("list", carts);
		map.put("freightAmount", freightAmount);
		return map;
		
	}
	
	/*@Override
	public Map getFreightAmount(String cartIds, String buyerAddrId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isBlank(cartIds)){
			throw new Exception("参数不全");
		}
		List carts = cartMapper.findStoreCartAmount(cartIds.split(","));
		double freightAmount = 0;
		String provinceId =  "";
		if(carts == null	||	carts.isEmpty()){
			throw new Exception("参数不正确");
		}else if(StringUtils.isNotBlank(buyerAddrId)){
			Map address = addressMapper.findById(Integer.parseInt(buyerAddrId));
			provinceId = address.get("provinceId").toString();
		}
		for (int i = 0,len = carts.size(); i < len; i++) {
			Map cart = (Map) carts.get(i);
			String sellerId =  cart.get("sellerId").toString();
			double price = Double.parseDouble(cart.get("price").toString());
			FreightAmount express = expressMapper.findFreightAmount(sellerId);
			if(express!=null){
				Map byintervallocation = JSONObject.parseObject(express.getByintervallocation());
				if(StringUtils.isNotBlank(buyerAddrId)){
					Map<String, String>  buyerAddr= (Map<String, String>) JSONObject.parse(byintervallocation.get(provinceId).toString());
					TreeMap<String, String> treeMap = new TreeMap(buyerAddr);
					String  maxPrice = treeMap.firstKey();
					*//**没有免邮*//*
					if(StringUtils.isBlank(maxPrice)){
						freightAmount+= express.getDefaultExpressPrice();
						cart.put("cartFreightAmount", express.getDefaultExpressPrice());
					}else if (StringUtils.isNotBlank(maxPrice) && price < Double.parseDouble(maxPrice)) {
						if(StringUtils.isNotBlank(treeMap.get(maxPrice))){
							*//**没有免邮,邮费不为空*//*
							freightAmount+=Double.parseDouble(treeMap.get(maxPrice));
							cart.put("cartFreightAmount", treeMap.get(maxPrice));
						}else{
							*//**没有免邮,邮费为空*//*
							freightAmount+=express.getDefaultExpressPrice();
							cart.put("cartFreightAmount", express.getDefaultExpressPrice());
						}
					}else if(StringUtils.isNotBlank(maxPrice) && price > Double.parseDouble(maxPrice)){
						freightAmount+=0;
						cart.put("cartFreightAmount", 0.00);
					}
				}else{
					freightAmount+=express.getDefaultExpressPrice();
					cart.put("cartFreightAmount", express.getDefaultExpressPrice());
				}
			}else{
				cart.put("cartFreightAmount", 0);
			}
			carts.set(i, cart);
		}
		map.put("list", carts);
		map.put("freightAmount", freightAmount);
		return map;
	}*/

}
