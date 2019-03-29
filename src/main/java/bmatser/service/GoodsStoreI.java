package bmatser.service;

import java.util.List;
import java.util.Map;

public interface GoodsStoreI {
	/**
	 * 通过别名进入店铺
	 * @param alias
	 * @return
	 */
	Map findStoreInfo(String alias);
	/**
	 * 通过别名获得店铺分类
	 * @param alias
	 * @return
	 */
	Map findStoreCate(String alias);
	/**
	 * 通过别名获得店铺使用的模板及模板数据  20160808 tiw
	 * @param alias
	 * @return
	 */
	Map selectTemplatesAndData(String alias);
	/**
	 * 店铺宝贝排行榜
	 * @param alias
	 * @return
	 */
	Map findStoreTop(String alias);
	
	/**
	 * 店铺分类宝贝
	 * @param alias
	 * @return
	 */
	List findStoreCateGoods(String alias);
	
	/**
	 * 店铺特卖宝贝
	 * @param alias
	 * @return
	 */
	List findStoreGoodsActivity(String alias);
	
	/**
	 * 店铺不同分类宝贝
	 * @param alias
	 * @param rows 
	 * @return
	 */
	List findStoreCateGoods(String alias, String cateNo, String page, String row);
	/**
	 * 搜索店铺宝贝
	 * @param alias
	 * @param map
	 * @return
	 */
	Map findStoreSearch(String alias, Map map);
	
	
	List findStoreCate(String alias, String cateId);
	/**
	 * 根据活动id查询出活动商品 20160808 tiw
	 * @return
	 */
	Map findActivityGoods(String id,Integer page,Integer rows);

}
