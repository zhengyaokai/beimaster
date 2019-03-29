package bmatser.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.model.Dealer;
import bmatser.model.DealerBusinessLicense;
import bmatser.model.DealerCash;
import bmatser.util.LoginInfo;
/**
 * 商家
 * @author felx
 * @date 2016.02.25 
 */
public interface DealerI {
	/**
	 * 由商家id更新营业执照
	 * @param LoginInfo loginInfo,Dealer dealer
	 * @return 
	 */
	//int updateById(Integer dealerId,Dealer dealer)throws Exception;
	
}
