package bmatser.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.AmountAlgorithmMapper;
import bmatser.model.EditOrderGoods;
import bmatser.model.FreightAmountInfo;
import bmatser.service.AmountAlgorithmI;

/**
 * @author felx
 *	@Date 2016-12-13
 *	@description 金额计算业务类
 */
@Service("amountAlgorithmService")
public class AmountAlgorithmImpl implements AmountAlgorithmI {
	
	@Autowired
	private AmountAlgorithmMapper dao;

	/**
	 *	@author jxw
	 *	@Date 2016-12-13
	 *	@description saas获取订单运费
	 * @param cartIds 购物车Id
	 * @param provinceId 省Id
	 * @return	运费金额
	 */
	@Override
	public BigDecimal getFreightAmount(List<String> cartIds, String provinceId) {
		List<FreightAmountInfo> freightInfoArray = null;
		
		if(cartIds==null || cartIds.size()==0){
			throw new RuntimeException("购物车Id不能为空房");
		}
		
		if(StringUtils.isBlank(provinceId)){
			throw new RuntimeException("省Id不能为空");
		}
		//运费模板内容集合
		freightInfoArray = dao.getFreightAmountInfo(cartIds,provinceId);
		
		return countFreightAmount(freightInfoArray,provinceId);
	}

	@Override
	public BigDecimal getEditFreightAmount(List<EditOrderGoods> newGoods,
			String provinceId) {
		List<FreightAmountInfo> freightInfoArray = null;		
		if(newGoods==null || newGoods.size()==0){
			return BigDecimal.ZERO;
		}
		
		if(StringUtils.isBlank(provinceId)){
			throw new RuntimeException("省Id不能为空");
		}
		//运费模板内容集合
		freightInfoArray = dao.getEditFreightAmountInfo(newGoods,provinceId);
		
		return countFreightAmount(freightInfoArray,provinceId);

	}

	/**
	 * 
	 *	@author jxw
	 *	@Date 2016-12-14
	 *	@description  修改订单和保存订单通用算法
	 * @param freightInfoArray 模板金额
	 * @param provinceId 省Id
	 * @return 运费
	 */
	private BigDecimal countFreightAmount(List<FreightAmountInfo> freightInfoArray, String provinceId) {
		BigDecimal bigDecimal = BigDecimal.ZERO;
		//筛选去重
		Map<String, FreightAmountInfo>  map = new HashMap<>();
		
		for (FreightAmountInfo freightInfo : freightInfoArray) {
			//判断是否存在运费模板
			if(map.containsKey(freightInfo.getFareTempId())){
				//优先匹配省相同的运费模板
				if(provinceId.equals(freightInfo.getProvinceId())){
					map.put(freightInfo.getFareTempId(), freightInfo);
				}
			}else{
				map.put(freightInfo.getFareTempId(), freightInfo);
			}
		}
		//遍历运费求和
		for (Entry<String, FreightAmountInfo> freightEntity : map.entrySet()) {
			FreightAmountInfo freightEntityVal= freightEntity.getValue();
			bigDecimal =bigDecimal.add(freightEntityVal.toCountFreight());
		}
		return bigDecimal.setScale(2, BigDecimal.ROUND_UP);
	}


}
