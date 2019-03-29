package bmatser.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.GoodsCombinationMapper;
import bmatser.service.GoodsCombinationI;
@Service("combinationService")
public class GoodsCombinationImpl implements GoodsCombinationI{
	@Autowired
	private GoodsCombinationMapper goodsCombinationDao;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List selectGoodsCombination(String sellerGoodsId) {
		//根据sellerGoodsId查出所有套餐
		List<Map<String,Object>> list = goodsCombinationDao.selectGoodsCombination(sellerGoodsId);
		if(list!=null &&list.size()!=0){
			//根据所有套餐id查询所有该套餐下所有商品价格合计
			List<Map<String,Object>> priceList = goodsCombinationDao.selectPackagePrice(list);
			//根据所有套餐id查询套餐下的所有商品
			List<Map<String,Object>> goodsList = goodsCombinationDao.selectPackageGoods(list);
			//根据所有套餐id查询所有活动
			List<Map<String,Object>> li = goodsCombinationDao.selectCombination(list);
			/*for(Map m : list){
			//根据套餐id查询该套餐下所有商品价格合计
			Map map  = goodsCombinationDao.selectPackagePrice(m.get("packageId").toString());
			if(map!=null){
				BigDecimal salePrice = new BigDecimal(map.get("salePrice").toString());
				BigDecimal packagePrice = new BigDecimal(map.get("packagePrice").toString());
				BigDecimal cutDownPrice = (salePrice.subtract(packagePrice)).setScale(2);
				m.put("packagePrice", packagePrice.setScale(2));
				m.put("cutDownPrice", cutDownPrice);//套餐返回实际价格和节省的价格
			}
			//向下：根据套餐id查询该套餐下所有商品
			m.put("goodsList", goodsCombinationDao.selectPackageGoods(m.get("packageId").toString()));
			//向上：根据套餐id查询该套餐属于哪个活动
			li.add(goodsCombinationDao.selectCombination(m.get("packageId").toString()));
		    }*/
			for(Map taocan : list){//套餐
				String packageId = taocan.get("packageId").toString();
				for(Map price : priceList){//套餐价
					if(packageId.equals(price.get("id").toString())){
						BigDecimal salePrice = new BigDecimal(price.get("salePrice").toString());
						BigDecimal packagePrice = new BigDecimal(price.get("packagePrice").toString());
						BigDecimal cutDownPrice = new BigDecimal((salePrice.subtract(packagePrice)).toString());
						taocan.put("packagePrice", packagePrice.setScale(2,BigDecimal.ROUND_HALF_UP));
						taocan.put("cutDownPrice", cutDownPrice.setScale(2,BigDecimal.ROUND_HALF_UP));//套餐返回实际价格和节省的价格
					}
				}
			}
			for(Map goods : goodsList){//所有商品
				BigDecimal salePrice = new BigDecimal(goods.get("salePrice").toString());
				goods.put("salePrice", salePrice.setScale(2,BigDecimal.ROUND_HALF_UP));
				String packageId = goods.get("packageId").toString();//商品对应的套餐Id
				for(Map taocan : list){//套餐
					if(packageId.equals(taocan.get("packageId").toString())){
						if(!taocan.containsKey("goodsList")){
							List<Map<String,Object>> l = new ArrayList();
							l.add(goods);
							taocan.put("goodsList", l);
						}else{
							List<Map<String,Object>> l= (List<Map<String, Object>>) taocan.get("goodsList");
							l.add(goods);
						}
					}
				}
			}
			
			for(Map taocan : list){//套餐
				String combinationId = taocan.get("combinationId").toString();//套餐对应的活动Id
				for(Map huodong : li){//活动
					if(combinationId.equals(huodong.get("id").toString())){
						if(!huodong.containsKey("combination")){
							List<Map<String,Object>> l = new ArrayList();
							l.add(taocan);
							huodong.put("combination", l);
						}else{
							List<Map<String,Object>> l= (List<Map<String, Object>>) huodong.get("combination");
							l.add(taocan);
						}
					}
				}
			}
			return li;
		}
		
		return null;
	}

}
