package bmatser.service;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.DealerDesignMapper;
import bmatser.dao.SellerGoodsMapper;
import bmatser.pageModel.GoodsInfo;
import bmatser.util.Constants;

@Service("storeListService")
public class StoreListImpl implements StoreListI {

	@Autowired
	private DealerDesignMapper dealerDesignMapper;
	@Autowired
	private SellerGoodsMapper sellerGoodsMapper;
	@Autowired
	private SolrI solrService;
	
	@Override
	public Map findStoreList(int page, int row,String keyword) {
		Map<String, Object> resultData= new HashMap<String, Object>();
			
		/**
		 * 店铺搜索功能开发，solr里面增加一个core5放店铺的信息。搜索时从core5中获取店铺信息
		 */
		List<Map> shopList =dealerDesignMapper.getStoreList(page,row,keyword.toLowerCase());
		/*注销从数据库查询店铺信息的方法 从solr中查询
		 * List<Map> shopList= dealerDesignMapper.findStoreList(page,row,operateScope);*/
		
		for (int i = 0,len = shopList.size(); i < len; i++) {
			Map shop = shopList.get(i);
			String shopAlias= shop.get("alias")!=null?shop.get("alias").toString() : "";
			String logo= shop.get("logo")!=null?shop.get("logo").toString() : "";
			if(StringUtils.isNotBlank(shopAlias)){
				List goods = sellerGoodsMapper.findStoreOrderCount(shopAlias);
				StringBuffer buff = new StringBuffer();
				for (int j = 0,size =goods.size() ; j < size; j++) {
					Map<String, Object> map= (Map<String, Object>) goods.get(j);
					Map goodsInfo = sellerGoodsMapper.findStoreGoodsById(map.get("id").toString());
					buff.append(map.get("id").toString()+",");
					map.put("goodsName", goodsInfo.get("goodsName").toString());
					map.put("price", goodsInfo.get("price").toString());
					map.put("image", goodsInfo.get("image")!=null?goodsInfo.get("image").toString() : "0.jpg");
					goods.set(j, map);
				}
				if (goods.size()<6) {
					List goodsArr= sellerGoodsMapper.findStoreGoodsByNoExist(shopAlias,6-goods.size(),buff.append(0).toString());
					if(goodsArr!=null){
						for (int j = 0; j < goodsArr.size(); j++) {
							goods.add(goodsArr.get(j));
						}
					}
				}
				shop.put("goodsList", goods);
				shopList.set(i, shop);
			}
		}
		resultData.put("shopList", shopList);
		resultData.put("rows", dealerDesignMapper.getStoreListCount(keyword));
		return resultData;
	}
    

}
