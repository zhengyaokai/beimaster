package bmatser.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import bmatser.dao.BuyerConsignAddressMapper;
import bmatser.dao.OrderInfoMapper;
import bmatser.dao.RegionMapper;
import bmatser.pageModel.Adress;
import bmatser.service.RegionServiceI;
import bmatser.util.DESCoder;
import bmatser.util.ObjectUtil;

@Service("regionService")
public class RegionServiceImpl implements RegionServiceI {
	
	
	@Autowired
	private RegionMapper regionDao;
	@Autowired
	private OrderInfoMapper orderInfoDao;
	@Autowired
	private BuyerConsignAddressMapper addressDao;

	@Override
	public Adress getRegionbyOrderId(String id) throws Exception {
		Map<String, Object> map = orderInfoDao.findOrderAddress(id);
		String consignAddressInfoSecret = String.valueOf(map.get("consignAddressInfoSecret"));
		String consignAddressId = String.valueOf(map.get("consignAddressId"));
		String isJd = String.valueOf(map.get("isJd"));
		DESCoder.instanceMobile();
		String consignee = null;
		String mobile = null;
		String provinceId = null;
		String cityId = null;
		String areaId = null;
		String address = null;
		String townId = null;
		try {
			JSONObject jo = JSONObject.parseObject(consignAddressInfoSecret);
			consignee = jo.getString("consignee");
			mobile = jo.getString("mobile");
			provinceId = 	 jo.getString("provinceId");
			cityId = 	 jo.getString("cityId");
			areaId = 	 jo.getString("areaId");
			address = 	 jo.getString("address");
			townId = 	 jo.getString("townId");
		} catch (Exception e) {
			Integer adrIntVal = Integer.parseInt(consignAddressId);
			Map<String, Object> addrMap =  addressDao.findJsonSecretAddress(adrIntVal);
			consignee =  addrMap.get("consignee")!=null?String.valueOf(addrMap.get("consignee")):"";
			mobile = 	 addrMap.get("mobile")!=null?String.valueOf(addrMap.get("mobile")):"";
			provinceId = 	 addrMap.get("provinceId")!=null?String.valueOf(addrMap.get("provinceId")):"";
			cityId = 	 addrMap.get("cityId")!=null?String.valueOf(addrMap.get("cityId")):"";
			areaId = 	 addrMap.get("areaId")!=null?String.valueOf(addrMap.get("areaId")):"";
			address = 	 addrMap.get("address")!=null?String.valueOf(addrMap.get("address")):"";
			townId = 	 addrMap.get("townId")!=null?String.valueOf(addrMap.get("townId")):"";
		}
		Adress adress = new Adress();
		adress.setAddress(address);
		adress.setConsignee(consignee);
		adress.setMobile(DESCoder.decoder(mobile));
		if("1".equals(isJd)){
			adress.setProvince(regionDao.selectJDRegionString(provinceId));
			adress.setCity(regionDao.selectJDRegionString(cityId));
			if(!"0".equals(areaId)){
				adress.setArea(regionDao.selectJDRegionString(areaId));
			}else{
				adress.setArea("");
			}
			if(townId != null &&(!"0".equals(townId))){
				adress.setTown(regionDao.selectJDRegionString(townId));
			}else{
				adress.setTown("");
			}
		}else{
			adress.setProvince(regionDao.selectRegionString(provinceId));
			adress.setCity(regionDao.selectRegionString(cityId));
			if(!"0".equals(areaId)){
				adress.setArea(regionDao.selectRegionString(areaId));
			}else{
				adress.setArea("");
			}
			if(townId != null &&(!"0".equals(townId))){
				adress.setTown(regionDao.selectRegionString(townId));
			}else{
				adress.setTown("");
			}
		}
		return adress;
	}

}
