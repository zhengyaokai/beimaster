/** 
 *	@author jxw
 * @description 
 */
package bmatser.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import bmatser.model.EditOrderGoods;
import bmatser.model.FreightAmountInfo;

/**
 * @author felx
 *	@description 金额计算Dao层
 */
public interface AmountAlgorithmMapper {

	List<FreightAmountInfo> getFreightAmountInfo(
			@Param("cartIdParam") List<String> cartIds, 
			@Param("provinceId") String provinceId);
	
	
	List<FreightAmountInfo> getEditFreightAmountInfo(
			@Param("goodsParam") List<EditOrderGoods> newGoods, 
			@Param("provinceId") String provinceId);

}
