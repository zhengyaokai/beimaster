package bmatser.service.impl;

import bmatser.util.alibaba.StringUtil;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.igfay.jfig.JFig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import bmatser.dao.AdverImageMapper;
import bmatser.dao.CategoryMapper;
import bmatser.dao.GoodsFloorMapper;
import bmatser.dao.GoodsMapper;
import bmatser.dao.IndexMapper;
import bmatser.dao.OrderInfoMapper;
import bmatser.dao.SysMessageMapper;
import bmatser.model.Category;
import bmatser.service.IndexI;
import bmatser.service.SolrI;

/**
 * 首页
 * @author felx
 * @date 2017-12-16
 */
@Service("indexService")
public class IndexImpl implements IndexI {

	@Autowired
	private GoodsFloorMapper goodsFloorDao;
	@Autowired
	private CategoryMapper categoryDao;
	@Autowired
	private SolrI solrService;
	@Autowired
	private GoodsMapper goodsDao;
	@Autowired
	private AdverImageMapper imageDao;
	@Autowired
	private SysMessageMapper messageDao;
	@Autowired
	private OrderInfoMapper orderDao;
	@Autowired
	private IndexMapper indexDao;
	//0 热门搜索，1 广告，2 公告，3 楼层广告，4 楼层商品，5 每日推荐
	private final String[] modular = {"hot_search","mall_adver","sys_message","page_floor_adver","goods_floor","recommend_goods"};
	//0 热门搜索，1 广告，2 公告，3 楼层广告，4 楼层商品，5 每日推荐
	private final String[] click = {"click_rate","click_rate","click_rate","click_rate","click_rate","click_num"};
	/**
	 * 获取所有分类
	 * @author felx
	 * @date 2017-12-24
	 */
	@Cacheable(value="index",key="'categories'")
	public List<Category> findCategories(){
		//获取大类
		Integer parentId = 0;
		List<Category> result = new ArrayList<Category>();
		List<Category> firstCategories = categoryDao.findCategories(parentId);
		for(Category category : firstCategories){
			List<Category> secondCategories = categoryDao.findCategories(category.getId());
			List<Category> secondCategorie = new ArrayList<Category>();
			int firstNum = 0;
			for(Category category2 : secondCategories){
				List<Category> thirdCategories = categoryDao.findCategories(category2.getId());
				List<Category> thirdCategorie = new ArrayList<Category>();
				int secondNum = 0;
				for(Category cate : thirdCategories){
//					Integer goodsCount = categoryDao.findCategoryGoods(cate.getId(),"2");
//					if(null != goodsCount && goodsCount > 0){
//						cate.setHasGoods(goodsCount.toString());
//						secondNum += goodsCount;
//						thirdCategorie.add(cate);
//					}
					Map mallGoodsMap = solrService.searchMallGoods(null, null, null, cate.getId(), null, null, null, 1, 1,null);
					if(null != mallGoodsMap && null != mallGoodsMap.get("total") && !"0".equals(mallGoodsMap.get("total").toString())){
						cate.setHasGoods("1");
						secondNum += 1;
						thirdCategorie.add(cate);
					}
				}
//				category2.setChildrens(thirdCategories);
				if(secondNum > 0){
					category2.setHasGoods(String.valueOf(secondNum));
					firstNum +=secondNum;
					category2.setChildrens(thirdCategorie);
					secondCategorie.add(category2);
				}
			}
//			category.setChildrens(secondCategories);
			category.setChildrens(secondCategorie);
			category.setHasGoods(String.valueOf(firstNum));
			
			
		}
		result = listSort(firstCategories);
		return result;
	}
	
	private List<Category> listSort(List<Category> categorys){
		List<Category> result = new ArrayList<Category>();
		result.addAll(categorys);
		for(Category category : categorys){
			switch (category.getId()) {
			case 10:
				result.set(2, category);
				break;
			case 20:
				result.set(4, category);
				break;
			case 30:
				result.set(3, category);
				break;
			case 40:
				result.set(5, category);
				break;
			case 50:
				result.set(0, category);
				break;
			case 60:
				result.set(1, category);
				break;
			case 70:
				result.set(7, category);
				break;
			case 80:
				result.set(6, category);
				break;
				
			}
		}
		return result;
	}
	
	/**
	 * 获取楼层商品
	 * @author felx
	 * @date 2017-12-16
	 */
//	@Cacheable(value="index",key="'floors'")
	public List findFloors() {
		return goodsFloorDao.findFloors();
	}

	/**
	 * 获取楼层商品
	 * @author felx
	 * @date 2017-12-16
	 */
//	@Cacheable(value="index",key="'floorGoods'")
	public List findFloorGoods() {
		List<Map<String, Object>> list =  categoryDao.findMallCateFloor();
		for (Map<String, Object> map : list) {
			String floorCode = String.valueOf(map.get("floorCode"));
			map.put("goods", goodsFloorDao.findFloorGoods(floorCode));
		}
		return list;
	}

	/**
	 * 获取楼层分类
	 * @author felx
	 * @date 2017-12-16
	 */
//	@Cacheable(value="index",key="'floorCategorys'")
	public List findFloorCategorys() {
		List<Map<String, Object>> list =  categoryDao.findMallCateFloor();
		for (Map<String, Object> map : list) {
			String id = String.valueOf(map.get("floorCode"));
			map.put("floorCategory",goodsFloorDao.findFloorCategorys(id));
		}
		return list;
		
	}
	
	/**
	 * 获取楼层品牌
	 * @author felx
	 * @date 2017-12-22
	 */
//	@Cacheable(value="index",key="'floorBrands'")
	public List findFloorBrands(){
		return goodsFloorDao.findFloorBrands();
	}
	
	//商城首页热门搜索   ---add 20160603
	public List findHotSearch(){
		return goodsDao.findHotSearch();
	}
	
	//商城首页每日推荐    ---add  20160606
	public List getDailyRecommendation(){
		return goodsDao.getDailyRecommendation();
	}
	
	/**
	 * 商城首页楼层的横幅广告和分类促销广告
	 * add   20160606
	 * @param channel
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List getfloorAdvertisement(String positionId){
		/*if(StringUtils.isBlank(positionId)){
			throw new Exception("参数不能为空!");
		}*/
		return imageDao.getfloorAdvertisement(positionId);
	}
	
	//查询商城首页通知公告  add --20160603
    public List findMallNotice(){
    	return messageDao.findMallNotice();
    }
    
    //商城首页显示订单信息   add--20160603
  	public	List findMallOrderMessage(){
  		List<HashMap> list = orderDao.findMallOrderMessage();
  		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
  		List li =new ArrayList(); 		
  		if(list != null && list.size()!= 0){	
			for(int i=0;i<list.size();i++){
				Map<String,Object> m = new HashMap<String,Object>();
				Timestamp t =(Timestamp) list.get(i).get("orderTime");
				String orderTime = fmt.format(t);
				StringBuffer ss = new StringBuffer(list.get(i).get("name").toString());
					switch (ss.length()) {
					case 0:
						break;
					case 1:
						ss.append("***");
						break;
					case 2:
						ss.replace(1, 2, "***");
						break;
					case 3:
						ss.replace(1, 2, "***");
						break;
					default:
						ss.replace(1, ss.length()-1, "***");
						break;
					}
					m.put("orderTime",orderTime);//下单时间
					m.put("money",list.get(i).get("money"));
					m.put("name",ss);//加密后的姓名
					li.add(i, m);
			}
			
		}
  		return li;
  	}
  	
  //更新商城首页模块：0 热门搜索，1 广告，2 公告，3 楼层广告，4 楼层商品，5 每日推荐 点击量
  	public void updateclickVolume(int code,String id){
  		if(code<modular.length){
  			indexDao.updateclickVolume(modular[code],id,click[code]);
  		}
  		
  	}
  	
    //微信商城查询所有店铺logo和别名
	@Override
	public List getAllStoreLogo() {
		// TODO Auto-generated method stub
		return indexDao.getAllStoreLogo();
	}

	@Override
	public List findPageFloorDetail(String floorId) {
		String path="";
		List<Map<String,Object>> resultMap = null;
		try {
			path = JFig.getInstance().getValue("UPLOAD_PATH", "SHOW_IMAGE");
		} catch (Exception e) {
		}
		return goodsFloorDao.findPageFloorDetail(floorId,path);
	}
//Ios系统使用接口
    @Override
    public List<Map<String,Object>> findPageFloorDetailForIos(String floorId) {
        String path="";
        List<Map<String,Object>>  listAll = new ArrayList<>();
        try {
            path = JFig.getInstance().getValue("UPLOAD_PATH", "SHOW_IMAGE");
            List<Map<String,Object>> list = goodsFloorDao.findPageFloorDetailForIos(floorId,path);
            listAll =  getListAll(list);
        } catch (Exception e) {

        }
        return listAll;
    }

    private List<Map<String,Object>> getListAll(List<Map<String,Object>> list){
        for(Map<String,Object> map:list) {
         map.put("categoryNames",map.get("categoryNames").toString().split(","));
         map.put("categoryIds",map.get("categoryIds").toString().split(","));
         map.put("sellerGoods",findFloorGoodsDetail(map.get("sellerGoodIds").toString()));
         String[] brandIdAndNamesSon = map.get("brandIdAndNames").toString().split(",");
         List<Map<String,Object>> brandList = new ArrayList<>();
         for(int i=0;i<brandIdAndNamesSon.length;i++){
             Map<String,Object> map1 = new HashMap<>();
             map1.put("brandId",brandIdAndNamesSon[i].split("#")[0]);
             map1.put("brandName",brandIdAndNamesSon[i].split("#")[1]);
             map1.put("brandImage",brandIdAndNamesSon[i].split("#")[2]);
             brandList.add(map1);
            }
            map.put("brandIdAndNames",brandList);
        }
        return list;
    }

	@Override
	public List findFloorGoodsDetail(String sellerGoodIds) {
  		String path="";
  		List<Map<String,Object>> resultMap = null;
		try {
			resultMap = goodsFloorDao.findFloorGoodsDetail(sellerGoodIds);
			path = JFig.getInstance().getValue("UPLOAD_PATH", "GOODS_SHOW_IMAGE");
			if( StringUtils.isNotBlank(path) ){
				for (Map<String,Object> hotGood : resultMap) {
					hotGood.put("image",path+hotGood.get("image"));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("获取商品详情失败!"+e.getMessage());
		}
		return resultMap;
	}



	@Override
	public String getHotSaleGoodsInfo() {
		return goodsFloorDao.getHotSaleGoodsInfo();
	}
}
