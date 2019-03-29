package bmatser.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface GoodsPackageMapper {

	/**
	 * @author felx
	 * @describe 获取套餐信息
	 * @param packageId 套餐Id
	 * @return
	 */
	Map<String, Object> getPackageInfo(@Param("packageId")String packageId);
	
	/**
	 * @author felx
	 * @describe 判断套餐是否存在
	 * @param packageId
	 * @return
	 */
	int isExistPackage(@Param("packageId")String packageId);

}
