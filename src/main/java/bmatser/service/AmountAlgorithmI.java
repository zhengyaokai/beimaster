package bmatser.service;

import java.math.BigDecimal;
import java.util.List;

import bmatser.model.EditOrderGoods;

/**
 * @author felx
 *	@Date 2016-12-13
 *	@description 金额计算接口类
 */
public interface AmountAlgorithmI {
	
	/**
	 *	@author jxw
	 *	@Date 2016-12-13
	 *	@description saas获取订单运费
	 * @param cartIds 购物车Id
	 * @param provinceId 省Id
	 * @return	运费金额
	 */
	BigDecimal getFreightAmount(List<String> cartIds, String provinceId);
	/**
	 *	@author jxw
	 *	@Date 2016-12-13
	 *	@description saas获取修改得订单运费
	 * @param cartIds 购物车Id
	 * @param provinceId 省Id
	 * @return	运费金额
	 */
	BigDecimal getEditFreightAmount(List<EditOrderGoods> newGoods,
			String findProvinceId);

}
