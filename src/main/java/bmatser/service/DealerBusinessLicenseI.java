package bmatser.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import bmatser.model.CrmCustomer;
import bmatser.model.Dealer;
import bmatser.model.DealerBusinessLicense;
import bmatser.model.DealerCash;
import bmatser.util.LoginInfo;
/**
 * 营业执照
 * @author felx
 * @date 2016.02.23 
 */
public interface DealerBusinessLicenseI {
	/**
	 * 新增营业执照
	 * @param i 
	 * @param DealerBusinessLicense dealerBusinessLicense
	 * @return 
	 */        
	@Deprecated
	int insert(String pcOrMoblie,String staffNo,Integer dealerId,DealerBusinessLicense dealerBusinessLicense,MultipartFile []files,Dealer dealer,CrmCustomer crmCustomer, int type)throws Exception;
	/**
	 * 查询营业执照关联信息
	 * @param String dealerId
	 * @return 
	 */
	List selectByDealerId(String dealerId);
	/**
	 * 注册审核资料
	 * @param license
	 * @param file 营业执照号
	 * @param fileTwo 税务登记证
	 * @param fileThree 组织机构代码证
	 * @throws Exception 
	 */
	void insert(DealerBusinessLicense license, MultipartFile file,
			MultipartFile fileTwo, MultipartFile fileThree,String type) throws Exception;
	void isRepeatDealerName(String dealerName, String dealerId);
	
	String checkReason(String dealerId);
	
	
	Object checkInfo(String dealerId);
	
	/**
	 * @author felx
	 * @describe 注册渠道
	 * @param dealerId 零售商Id
	 * @return
	 */
	Object getRegChannel(String dealerId);
}
