package bmatser.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;

import bmatser.dao.DealerCategoryGoodsMapper;
import bmatser.dao.DealerCategoryMapper;
import bmatser.dao.DealerDesignMapper;
import bmatser.dao.DealerTemplateMapper;
import bmatser.dao.GoodsActivityMapper;
import bmatser.dao.SellerGoodsMapper;
import bmatser.dao.ShopFloorMapper;
import bmatser.service.GoodsStoreI;
import bmatser.util.Constants;
@Service("GoodsStoreService")
public class GoodsStoreImpl implements GoodsStoreI {

	@Autowired
	private DealerDesignMapper dealerDesignMapper;
	@Autowired
	private DealerCategoryMapper dealerCategoryMapper;
	@Autowired
	private SellerGoodsMapper sellerGoodsMapper;
	@Autowired
	private DealerCategoryGoodsMapper CategoryGoodsMapper;
	@Autowired
	private GoodsActivityMapper activityMapper;
	@Autowired
	private DealerTemplateMapper templateMapper;
	@Autowired
	private ShopFloorMapper shopFloorMapper;
	/**
	 * 通过别名进入店铺
	 */
	@Override
	public Map findStoreInfo(String alias) {
		Map map = dealerDesignMapper.findByDealerAlias(alias);
		if(map.get("jsonBanner")!=null){
				JSONArray jsonArry = JSONArray.parseArray(map.get("jsonBanner").toString());
				List<Map<String, String>> list = new ArrayList<Map<String, String>>();
				for (int i = 0,len = jsonArry.size() ; i < len; i++) {
					Map<String, String> imgMap= new HashMap<String, String>();
					Map<String, String> jo= (Map<String, String>) jsonArry.get(i);
					String img = jo.get("img");
					String link = jo.get("link");
					imgMap.put("img", Constants.IMAGE_URL + img);
					imgMap.put("link", link);
					list.add(imgMap);
				}
				map.put("jsonBanner",list);
		}
		return map;
	}
	/**
	 * 通过别名获得店铺分类
	 */
	@Override
	public Map findStoreCate(String alias) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("patentCate", dealerCategoryMapper.findPatentCateByAlias(alias));
		map.put("allCate", dealerCategoryMapper.findAllCateByAlias(alias));
		return map;
	}
	/**
	 * 通过别名获得店铺使用的模板及模板数据  20160808 tiw
	 * @param alias
	 * @return
	 */
	public Map selectTemplatesAndData(String alias) {
		Map<String, Object> map = new HashMap<String, Object>();
		//通过别名获得店铺使用的模板 20160808 tiw
		List<Map<String,Object>> list = templateMapper.selectTemplates(alias);
		for(Map<String,Object> m:list) {
			//通过店铺模板id查询模板数据  20160808 tiw
			List templateDate =templateMapper.selectTemplateDate(m.get("id").toString());
			m.put("templateDate", templateDate);
		}
		map.put("list", list);
		return map;
	}
	/**
	 * 根据活动id查询出活动商品 20160808 tiw
	 * @return
	 */
	@Override
	public Map findActivityGoods(String id, Integer page, Integer rows) {
		Map<String, Object> map = new HashMap<String, Object>();
		//根据活动id先查询活动
		Map m = sellerGoodsMapper.findActivity(id);
		//根据活动id查询活动商品
		List list = sellerGoodsMapper.findActivityGoods(id,page,rows);
		//查询活动商品总数
		int count = sellerGoodsMapper.findActivityGoodsCount(id);
		map.put("activityName", m.get("activityName"));
		map.put("goodsList", list);
		map.put("count", count);
		return map;
	}
	/**
	 * 店铺宝贝排行榜
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map findStoreTop(String alias) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List goods = sellerGoodsMapper.findStoreOrderCount(alias);
		/*List monGoods = sellerGoodsMapper.findStoreMonthOrderCount(alias);*/
		for (int i = 0,len =goods.size() ; i < len; i++) {
			Map<String, Object> map= (Map<String, Object>) goods.get(i);
			Map goodsInfo = sellerGoodsMapper.findStoreGoodsById(map.get("id").toString());
			map.put("goodsName", goodsInfo.get("goodsName").toString());
			map.put("price", goodsInfo.get("price").toString());
			map.put("model", goodsInfo.get("model").toString());
			map.put("image", goodsInfo.get("image")!=null?goodsInfo.get("image").toString() : "0.jpg");
			goods.set(i, map);
		}
		/*for (int i = 0,len =monGoods.size() ; i < len; i++) {
			Map<String, Object> map= (Map<String, Object>) monGoods.get(i);
			Map goodsInfo = sellerGoodsMapper.findStoreGoodsById(map.get("id").toString());
			map.put("goodsName", goodsInfo.get("goodsName").toString());
			map.put("price", goodsInfo.get("price").toString());
			map.put("image", goodsInfo.get("image")!=null?goodsInfo.get("image").toString() : "0.jpg");
			monGoods.set(i, map);
		}*/
		returnMap.put("topGoods", goods);
		/*returnMap.put("monGoods", monGoods);*/
		return returnMap;
	}
	/**
	 * 店铺分类宝贝
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List findStoreCateGoods(String alias) {
		//modify 20161025 店铺先查楼层商品  没有再查分类商品
		List<Map<String,Object>> shopFloorList = shopFloorMapper.getShopFloorList(alias);
		if(shopFloorList!=null && shopFloorList.size()!=0){
			for(Map m : shopFloorList){
				List list = shopFloorMapper.getShopFloorGoods(m.get("id").toString());
				m.put("goodsList", list);
			}
			return shopFloorList;
		}else{
			List cateList = dealerCategoryMapper.findPatentCateByAlias(alias);
			for (int i = 0,len = cateList.size(); i < len; i++) {
				Map<String, Object> map = (Map) cateList.get(i);
				List list = CategoryGoodsMapper.findStoreCateGoods(map.get("cateNo").toString(),alias);
				map.put("goodsList", list);
				cateList.set(i, map);
			}
			return cateList;
		}
		
	}
	/**
	 * 店铺特卖宝贝
	 */
	@Override
	public List findStoreGoodsActivity(String alias) {
		return activityMapper.findGoodsActivityByAlias(alias);
	}
	/**
	 * 店铺单个分类下的商品
	 */
	@Override
	public List findStoreCateGoods(String alias, String cateNo, String page, String row) {
		List list = new ArrayList();
		int pages = 0;
		int rows = 12;
		if(StringUtils.isNoneBlank(page)){
			if(StringUtils.isNotBlank(row)){
				pages = (Integer.parseInt(page)-1)*40;
				rows = 40;
			}else{
				pages = (Integer.parseInt(page)-1)*12;
			}	
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", StringUtils.equals(cateNo, "0")?"全部商品":CategoryGoodsMapper.findCateByCateNo(alias,cateNo));
		map.put("goodsList", CategoryGoodsMapper.findStoreCateGoodsByCateId(alias,cateNo,pages,rows));
		map.put("count", CategoryGoodsMapper.findStoreCateGoodsByCateIdCount(alias,cateNo));
		list.add(map);
		return list;
	}
	/**
	 * 店铺页面搜索
	 */
	@Override
	public Map findStoreSearch(String alias, Map queryMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		int pages = 0;
		String page = (String) queryMap.get("page");
		if(StringUtils.isNoneBlank(page)){
			pages = (Integer.parseInt(page)-1)*12;
		}
		map.put("goodsList", sellerGoodsMapper.findStoreGoodsByPage(alias,queryMap,pages,12));
		map.put("count", sellerGoodsMapper.findStoreGoodsByPageCount(alias,queryMap));
		return map;
	}
	@Override
	public List findStoreCate(String alias, String cateId) {
		return  (!"0".equals(cateId))?dealerCategoryMapper.findCateByParentId(alias,cateId):null;
	}
	

}
