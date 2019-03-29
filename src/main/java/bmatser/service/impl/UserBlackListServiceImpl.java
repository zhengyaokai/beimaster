/**
 * 
 */
package bmatser.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.lang3.StringUtils;
import org.igfay.jfig.JFig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import bmatser.exception.GdbmroException;
import bmatser.model.UserBlackList;
import bmatser.pageModel.PageMode;
import bmatser.pageModel.PageMode.Channel;
import bmatser.service.UserBlackListServiceI;
import bmatser.util.Constants;
import bmatser.util.DateTypeHelper;

/**
 * @author felx
 *
 */
@Service("userBlackListService")
public class UserBlackListServiceImpl implements UserBlackListServiceI {
	
//	@Autowired
//	private MongoTemplate MongoDao;
	@Autowired
	private MemcachedClient memcachedClient;
	
	/** 访问次数 */
	private final long ACCESS_COUNT = JFig.getInstance().getIntegerValue("safeAccess", "ACCESS_COUNT","10");
	/** 访问间隔时间阀值,单位：毫秒 */
	private final long ACCESS_BETWEEN_TIME= JFig.getInstance().getIntegerValue("safeAccess", "ACCESS_BETWEEN_TIME","100000");
	/** 安全访问时间,单位：毫秒 */
	private final long ACCESS_SAFE_TIME= JFig.getInstance().getIntegerValue("safeAccess", "ACCESS_SAFE_TIME","100000");
	
	/** 黑名单访问次数阀值 */
	private final long ACCESS_SAFE_COUNT= JFig.getInstance().getIntegerValue("safeAccess", "ACCESS_SAFE_COUNT","3");
	

	@Override
	public void checkUserAccess(PageMode pageMode) throws Exception,GdbmroException {
//		String dealerId = "";
//		String ip = pageMode.getIp();
//		Long ipCode = pageMode.getIp2Long();
//		Long nowTime  = new Date().getTime();
//		Long beforeTime  = new Date().getTime()-ACCESS_BETWEEN_TIME;
//		String datefmt = DateTypeHelper.getCurrentDateTimeString();
//		//Long lastVerifyTime =new Date().getTime();
//		
//		/** 判断是否登陆 */
//		if(pageMode.isLogin(Channel.SAAS)){
//			dealerId = pageMode.getDealerId(Channel.SAAS);
//		}
//		/** 查询用户记录 */
//		Query userBlackQuery = new Query(
//				Criteria.where("ipCode").is(ipCode)
//				);
//		UserBlackList user= MongoDao.findOne(userBlackQuery, UserBlackList.class, "userBlackList");
//		if(user != null){
//			beforeTime = getBeforeTime(user.getLastVerifyTime());//获得时间区间
//		}
//		/** 查询次数 */
//		Query queryCount = new Query(
//				Criteria.where("ipCode").is(ipCode)
//				.and("searchTime").gte(beforeTime)
//				.lte(nowTime)
//				);
//		long count = MongoDao.count(queryCount, "userSearchList");
//		
//		if(user != null && user.isWhite()){ // 判断白名单
//			return ;
//		}else if(count < ACCESS_COUNT && user == null){ //查询次数小于阀值,直接通过
//			return ;
//		}else if(count >= ACCESS_COUNT && user == null){// 第一次超过访问次数，新增用户记录
//			user = new UserBlackList();
//			user.InsertForFirst(ip, dealerId,ipCode);
//			MongoDao.insert(user, "userBlackList");
//			throw new GdbmroException(32003, "");//2 生成验证码
//		}else if(user != null && user.isBlack()){//判断黑名单
//			System.out.println(ip + "已列入黑名单");
//			throw new GdbmroException(32004, "");//黑名单
//		}else if(user != null){// 查询次数大于阀值,已有记录
//			
//			if(user.getVerifyCode()==1){//需要填写验证码
//				String userAuth = pageMode.getStringValue("auth").toLowerCase();
//				String auth = memcachedClient.get(Constants.CHECK_SEARCH + ip);
//				
//				if(StringUtils.isBlank(auth)){//验证码过期，验证码错误，验证码为空返回新的验证码
//					throw new GdbmroException(32003, "");
//				}else if(StringUtils.isBlank(userAuth)){
//					throw new GdbmroException(32003, "请填写验证码");
//				}else if(!auth.toLowerCase().equals(userAuth)){
//					throw new GdbmroException(32003, "验证码填写不正确");
//				}else if(auth.toLowerCase().equals(userAuth)){//验证码通过,并更新状态
//					MongoDao.updateMulti(userBlackQuery, 
//							new Update().set("verifyCode", 0)
//								.set("searchCount", user.getSearchCount()+1)
//								.set("lastVerifyTime", nowTime)
//								.set("lastVDateFormat", datefmt)
//							, "userBlackList");
//					return ;
//				}
//				
//			}else if(count >= ACCESS_COUNT && user.getVerifyCode() == 0){//在时间内不再验证
//				
//				if(toCheakTime(user.getLastVerifyTime())){//在时间内不再重复验证
//					return ;
//					
//				}else{//超过时间,重新需要验证码
//					if(user.getSearchCount() >= ACCESS_SAFE_COUNT){//超过验证次数加入黑名单
//						MongoDao.updateMulti(userBlackQuery,
//								new Update().set("loginStatus", 1)
//									.set("lastVerifyTime", nowTime)
//									.set("lastVDateFormat", datefmt)
//								, "userBlackList");
//						throw new GdbmroException(32004, "");//黑名单
//					}else{//再次进入验证
//						MongoDao.updateMulti(userBlackQuery,new Update()
//							.set("verifyCode", 1)
//							.set("lastVerifyTime", nowTime)
//							.set("lastVDateFormat", datefmt)
//							, "userBlackList");
//						throw new GdbmroException(32003, "");
//					}
//				}
//			}else if(count < ACCESS_COUNT && user.getVerifyCode() == 0){//未超过访问限制
//				return;
//			}
//		}
	}
	/**
	 * 查询时间区间
	 * @param lastVerifyTime
	 * @return
	 * @throws Exception
	 */
	private Long getBeforeTime(Long lastVerifyDate) throws Exception {
		/*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lastVerifyDate = df.parse(lastVerifyTime).getTime();*/
		long nowDate = new Date().getTime();
		if(lastVerifyDate + ACCESS_SAFE_TIME + ACCESS_BETWEEN_TIME > nowDate){
			return lastVerifyDate + ACCESS_SAFE_TIME;
		}else{
			return new Date().getTime()-ACCESS_BETWEEN_TIME;
		}
	}
	/**
	 * 比较时间
	 * @param lastTime
	 * @return
	 * @throws Exception 
	 */
	private boolean toCheakTime(Long lastDate) throws Exception {
/*		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lastDate = df.parse(lastTime).getTime();*/
		long nowDate = new Date().getTime();
		if(lastDate + ACCESS_SAFE_TIME > nowDate){
			return true;
		}else{
			return false;
		}
		
	}

	/**
	 * 时间转换
	 * @param time
	 * @return
	 */
	private String getTime(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = df.format(new Date(time));
		return date;
	}
	@Override
	public void saveSearchCount(PageMode pageModel) {
		Map<String, Object> map = new HashMap<String, Object>();
		String datefmt = DateTypeHelper.getCurrentDateTimeString();
		map.put("ip", pageModel.getIp());
		map.put("ipCode", pageModel.getIp2Long());
		map.put("searchTime", new Date().getTime());
		map.put("searchDateFormat", datefmt);
//		MongoDao.insert(map, "userSearchList");
	}
	
}
