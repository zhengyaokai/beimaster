package bmatser.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.GiftMapper;
import bmatser.service.GiftStoreI;
import bmatser.util.Constants;
import bmatser.util.LogoException;
@Service("giftStoreService")
public class GiftStoreImpl implements GiftStoreI {
	
	@Autowired
	private GiftMapper GiftDao;

	@Override
	public Map<String, Object> findGiftList(String isRecommend,String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("giftList", GiftDao.findGiftList(isRecommend,type));
		return map;
	}

	@Override
	public Map<String, Object> findAppGiftList(String isRecommend,String type,int page,int row) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("giftList", GiftDao.findAppGiftList(isRecommend,type,page,row));
		map.put("count",GiftDao.findAppGiftListCount(isRecommend,type));
		return map;
	}
	
	@Override
	public Map<String, Object> findGiftById(String id) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(id)){
			Map gift = GiftDao.findGiftById(id);
			if(gift!=null){
				if(gift.get("smallPic") != null){
					gift.put("smallPic",Constants.IMAGE_URL + "giftImg/" + gift.get("smallPic").toString());
				}
				if(gift.get("bigPic") != null){
					gift.put("bigPic",Constants.IMAGE_URL + "giftImg/" + gift.get("bigPic").toString());
				}
				if(gift.get("banner") != null){
					gift.put("banner",Constants.IMAGE_URL + "giftImg/" + gift.get("banner").toString());
				}
				if(gift.get("spePic") != null){
					gift.put("spePic",Constants.IMAGE_URL + "giftImg/" + gift.get("spePic").toString());
				}
				map.put("gift", gift);
			}
		}else{
			throw new LogoException("商品不存在");
		}
		return map;
	}

}
