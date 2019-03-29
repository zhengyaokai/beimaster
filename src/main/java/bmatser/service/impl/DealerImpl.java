package bmatser.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.DealerBusinessLicenseMapper;
import bmatser.dao.DealerMapper;
import bmatser.model.Dealer;
import bmatser.service.DealerI;
import bmatser.util.LoginInfo;

/**
 * 商家
 * @author felx
 * @date 2016.02.25 
 */
@Service("dealer")
public class DealerImpl implements DealerI {
	
	@Autowired
	private DealerMapper dealerDao;
	/**
	 * 更具商家ID，更新公司名称
	 * @param LoginInfo loginInfo,MemberFavorite memberFavorite
	 * @return 
	 */
	/*@Override
	public int updateById(Integer dealerId, Dealer dealer) throws Exception{
			dealer.setId(dealerId);
			return dealerDao.updateById(dealer);
	}*/
	
}
