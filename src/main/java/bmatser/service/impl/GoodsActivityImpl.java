package bmatser.service.impl;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import bmatser.dao.GoodsActivityMapper;
import bmatser.service.GoodsActivityI;

@Service("activityService")
public class GoodsActivityImpl implements GoodsActivityI {
	
	@Autowired
	private GoodsActivityMapper activityDao;

	/**
	 * 获取进行中的特卖活动
	 * @return list
	 * 2017-8-10
	 *
	 */
	@SuppressWarnings("rawtypes")
	@Override
//	@Cacheable(value="activity", key="'goodsActivity'")
//	@CacheEvict(value="activity", key="'goodsActivity'")
	public List findGoodsActivity(Integer activityChannel) {
		return activityDao.findGoodsActivity(activityChannel);
	}

	/**
	 * saas获取楼层活动
	 * @author felx
	 * @date 20160922
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List getFloorGoodsActivity() {
	    List<Map<String,Object>> list = activityDao.getFloor();//先查楼层
	    List<Map<String,Object>> l =new ArrayList();
	    List li = new ArrayList();
	    for (Map<String, Object> map : list) {
	    	li = activityDao.findFloorActivity(map.get("id").toString());//根据楼层id 关联 floor_activity-->goods_activity
	    	//只返回有楼层活动的楼层
	    	if(li!=null&&li.size()!=0){
	    		map.put("list",li);
	    		l.add(map);
	    	}
	    	
		}
  		return l;
	}
	
	/**
	 * 获取特卖产品
	 * @param goodsActivityId
	 * @param page
	 * @param rows
	 * @return map
	 * 2017-8-10
	 *
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Object> findGoodsActivitySales(int goodsActivityId, Integer cateId, String priceOrder, String rateOrder, int page, int rows,String activityChannel) {
		Map<String, Object> map = new HashMap();
		/**当前页产品列表数据*/
		map.put("rows", activityDao.findGoodsActivitySales(goodsActivityId, cateId, priceOrder, rateOrder, page, rows,activityChannel));
		/**总数量*/
		map.put("total", activityDao.countGoodsActivitySales(goodsActivityId, cateId,activityChannel));
		Map<String, Object> activityBrand = activityDao.findGoodsActivityTime(goodsActivityId);
		map.put("endTime", activityBrand.get("endTime"));
		map.put("startTime", activityBrand.get("startTime"));
		map.put("overTime", activityBrand.get("overTime"));
		map.put("activityName", activityBrand.get("activityName"));
		map.put("rate", activityBrand.get("rate"));
		map.put("price", activityBrand.get("price"));
		map.put("bigBannerImage", activityBrand.get("bigBannerImage"));
		map.put("bigBannerUrl", activityBrand.get("bigBannerUrl"));
		return map;
	}
	
	/**
	 * 获取商城特卖商品
	 * @author felx
	 * @date 2017-12-23
	 */
	@Override
//	@Cacheable(value="activity",key="'mallActivityGoods'+#activityChannel")
	public List findMallActivityGoods(String activityChannel) {
		Map<String, Object> map = new HashMap();
		/**当前页产品列表数据*/
		return activityDao.findMallActivityGoods(activityChannel);
	}

	/**
	 * 获取特卖商品的分类
	 * @param goodsActivityId
	 * @return list
	 * 2017-8-31
	 *
	 */
	@Override
	public List findGoodsActivityCategory(int goodsActivityId) {
		return activityDao.findGoodsActivityCategory(goodsActivityId);
	}

	/**
	 * 获取满减活动
	 * @param sellerGoodsIds
	 * @return
	 * 2017-10-8
	 *
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public double findGoodsActivityFullcut(Map sellerGoodsIds) {
		Iterator iter = sellerGoodsIds.entrySet().iterator();
		List sellerGoodsIdList = new ArrayList();
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry)iter.next();
			String key = entry.getKey().toString();
			sellerGoodsIdList.add(key);
		}
		Map fullcutMap = new HashMap();
		List fullcutList = activityDao.findGoodsActivityFullcut(sellerGoodsIdList);
		for(Object o : fullcutList){
			Map fullcuts = (Map)o;
			String activityId = fullcuts.get("activityId").toString();
			List list = null;
			if(fullcutMap.containsKey(activityId)){
				list = (List)fullcutMap.get(activityId);
				list.add(fullcuts);
			} else{
				list = new ArrayList();
				list.add(fullcuts);
				fullcutMap.put(activityId, list);
			}
		}
		Iterator fullcutIter = fullcutMap.entrySet().iterator();
		double totalFullcut = 0;
		while(fullcutIter.hasNext()){
			Map.Entry entry = (Map.Entry)fullcutIter.next();
			List list = (List)entry.getValue();
			double fullAmount = 0;
			double sutExt = 0;
			double totalPrice = 0;
			for(Object o : list){
				Map fullcuts = (Map)o;
				if(fullAmount == 0){
					fullAmount = Double.valueOf(fullcuts.get("fullAmount").toString());
				}
				if(sutExt == 0){
					sutExt = Double.valueOf(fullcuts.get("sutExt").toString());
				}
				Long sellerGoodsId = Long.valueOf(fullcuts.get("sellerGoodsId").toString());
				if(totalPrice ==0){
					sellerGoodsIds.get(sellerGoodsId);
					totalPrice = Double.valueOf(sellerGoodsIds.get(sellerGoodsId).toString());
				} else{
					totalPrice += Double.valueOf(sellerGoodsIds.get(sellerGoodsId).toString());
				}
			}
			if(totalPrice >= fullAmount){
				int multiple = (int)(totalPrice/fullAmount);
				totalFullcut += multiple*sutExt;
			}
		}
		return totalFullcut;
	}



}
