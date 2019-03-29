package bmatser.service;

import java.util.List;

import bmatser.model.Category;
import java.util.Map;

/**
 * 首页
 * @author felx
 * @date 2017-12-16
 */
public interface IndexI {
	
	List<Category> findCategories();
	
	List findFloors();
	
	List findFloorGoods();
	
	List findFloorCategorys();
	
	List findFloorBrands();
	
	//商城首页热门搜索   ---add 20160603
	List findHotSearch();
	
	//商城首页每日推荐    ---add  20160606
	List getDailyRecommendation();
	
	/**
	 * 商城首页楼层的横幅广告和分类促销广告
	 * add   20160606
	 * @param channel
	 * @return
	 * @throws Exception 
	 */
	List getfloorAdvertisement(String positionId);
	
	//查询商城首页通知公告  add --20160603
	List findMallNotice();
	
	//商城首页显示订单信息   add--20160603
	List findMallOrderMessage();
	//更新商城首页模块：0 热门搜索，1 广告，2 公告，3 楼层广告，4 楼层商品，5 每日推荐 点击量
	void updateclickVolume(int code,String id);
	
	//微信商城查询所有店铺logo和别名
	List getAllStoreLogo();

	//商城楼层基本信息（楼层名称，分类，品牌）(
	List findPageFloorDetail(String floorId);

    //商城楼层基本信息（楼层名称，分类，品牌IOS）(
    List<Map<String,Object>> findPageFloorDetailForIos(String floorId);

	//商城楼层产品信息
	List findFloorGoodsDetail(String sellerGoodIds);


	String getHotSaleGoodsInfo();
}
