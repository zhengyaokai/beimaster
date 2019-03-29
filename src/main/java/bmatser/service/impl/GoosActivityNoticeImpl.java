package bmatser.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import bmatser.dao.GoodsActivityNoticeMapper;
import bmatser.service.GoosActivityNoticeI;

@Service("activityNoticeService")
public class GoosActivityNoticeImpl implements GoosActivityNoticeI {
	
	@Autowired
	private GoodsActivityNoticeMapper activityNoticeDao;

	/**
	 * 获取特卖预告
	 * @return list
	 *
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
//	@Cacheable(value = "goodsActivity", key="'notice'")
	public List findGoodsActivityNotices() {
		List activityNotices = new ArrayList();
		List activityNoticeList = activityNoticeDao.findGoodsActivityNotices();
		if(activityNoticeList != null && !activityNoticeList.isEmpty()){
			Map tempMap = new HashMap();
			for(Object object : activityNoticeList){
				Map activityNoticeMap = (Map)object;
				Integer id = (Integer)activityNoticeMap.get("id");
				if(tempMap.containsKey(id)){
					Map existMap = (Map)tempMap.get(id);
					List noticeList = (List)existMap.get("brands");
					Map noticeMap = new HashMap();
					noticeMap.put("brandImage", activityNoticeMap.get("brandImage"));
					noticeMap.put("brandId", activityNoticeMap.get("brandId"));
					noticeList.add(noticeMap);
				} else {
					List noticeList = new ArrayList();
					Map noticeMap = new HashMap();
					noticeMap.put("brandImage", activityNoticeMap.get("brandImage"));
					noticeMap.put("brandId", activityNoticeMap.get("brandId"));
					noticeList.add(noticeMap);
					Map tempActivityNotices = new HashMap();
					tempActivityNotices.put("id", activityNoticeMap.get("id"));
					tempActivityNotices.put("startDate", activityNoticeMap.get("startDate"));
					tempActivityNotices.put("endDate", activityNoticeMap.get("endDate"));
					tempActivityNotices.put("title", activityNoticeMap.get("title"));
					tempActivityNotices.put("brands", noticeList);
					tempMap.put(id, tempActivityNotices);
				}
			}
			if(tempMap != null && !tempMap.isEmpty()){
				Iterator iter = tempMap.entrySet().iterator();
				while(iter.hasNext()){
					Map.Entry enter = (Map.Entry)iter.next();
					activityNotices.add(enter.getValue());
				}
			}
		}
		return activityNotices;
	}
	
	/**
	 *
	 * 下期预告
	 */
	@Override
	public Map selectUnderlineGoodsActivity() {
		Map saveMap=new LinkedHashMap();
		Map setMap=null;
		List ltTwo=new LinkedList();
		List activity=activityNoticeDao.selectUnderlineGoodsActivity();
		for(int i=0;i<activity.size();i++){
			Map mp=(Map) activity.get(i);
			if(null!=mp&&mp.size()>0){
				setMap=new HashMap();
				String startDate= mp.get("startDate").toString();
				String endDate= mp.get("endDate").toString();
				setMap.put("startDate", startDate);
				setMap.put("endDate", endDate);
				saveMap.put("time", setMap);
				break;
			}else{
				setMap=new HashMap();
				setMap.put("startDate", "");
				setMap.put("endDate", "");
				saveMap.put("time", setMap);
				break;
			}
		}
		for(int i=0;i<activity.size();i++){
			Map mp=(Map) activity.get(i);
			if(null!=mp&&mp.size()>0){
				setMap=new HashMap();
				String id= mp.get("id").toString();
				String nameEn= mp.get("nameEn").toString();
				String nameCn= mp.get("nameCn").toString();
				String brandImage=(String) mp.get("brandImage");
				setMap.put("id", id);
				setMap.put("nameEn", nameEn);
				setMap.put("nameCn", nameCn);
				setMap.put("brandImage", brandImage);
				ltTwo.add(setMap);
			}else{
				break;
			}
		}
		saveMap.put("dataValue", ltTwo);
		return saveMap;
	}

}
