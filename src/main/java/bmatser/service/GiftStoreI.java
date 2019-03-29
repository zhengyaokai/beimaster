package bmatser.service;

import java.util.Map;

public interface GiftStoreI {

	Map<String, Object> findGiftList(String isRecommend,String type);

	Map<String, Object> findAppGiftList(String isRecommend,String type,int page,int rows);	
	
	Map<String, Object> findGiftById(String id) throws Exception;

}
