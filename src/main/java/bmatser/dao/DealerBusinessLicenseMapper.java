package bmatser.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import bmatser.model.DealerBusinessLicense;
import bmatser.util.FileUpload;
import bmatser.util.LoginInfo;
/**
 * 营业执照
 * @author felx
 * @date 2016.02.23 
 */
public interface DealerBusinessLicenseMapper {
	/**
	 * 新增营业执照
	 * @param DealerBusinessLicense dealerBusinessLicense,FileUpload fileUpload
	 * @return 
	 */
	int insert(DealerBusinessLicense dealerBusinessLicense);
	
	/**
	 * 更新营业执照
	 * @author felx 20160623
	 */
	int update(DealerBusinessLicense dealerBusinessLicense);
	/**
	 * 查询营业执照关联信息
	 * @param String dealerId
	 * @return 
	 */
	List selectByDealerId(@Param(value = "dealerId") Integer dealerId);

}