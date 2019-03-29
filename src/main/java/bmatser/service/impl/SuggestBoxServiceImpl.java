package bmatser.service.impl;

import java.sql.Timestamp;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.ctc.wstx.util.StringUtil;

import bmatser.pageModel.FeedBack;
import bmatser.service.SuggestBoxServiceI;
import bmatser.util.LoginInfo;

@Service("suggestBoxService")
public class SuggestBoxServiceImpl implements SuggestBoxServiceI {
	
	private static final String FEED_BACK = "feedBack";

//	@Autowired
//	private MongoTemplate mongoDao;
	
	@Override
	public void addSuggest(FeedBack feedBack, LoginInfo loginInfo) throws Exception {
		if(StringUtils.isNotBlank(loginInfo.getLoginId())){
			feedBack.setLoginId(loginInfo.getLoginId());
		}
		if(StringUtils.isNotBlank(loginInfo.getDealerId())){
			feedBack.setDealerId(loginInfo.getDealerId());
		}
		if(StringUtils.isBlank(feedBack.getContent())){
			throw new Exception("描述内容不能为空");
		}
		if(StringUtils.isBlank(feedBack.getName())){
			throw new Exception("姓名不能为空");
		}
		if(StringUtils.isBlank(feedBack.getCompany())){
			throw new Exception("公司名称不能为空");
		}
		feedBack.setCreateTime(new Timestamp(System.currentTimeMillis()));
//		mongoDao.save(feedBack,FEED_BACK);
	}

}
