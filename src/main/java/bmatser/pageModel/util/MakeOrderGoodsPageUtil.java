package bmatser.pageModel.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import bmatser.pageModel.MakeOrderGoodsPage;

public class MakeOrderGoodsPageUtil extends MakeOrderGoodsPage {

	private static final long serialVersionUID = 7249356368905450849L;

	public void init() {
		BigDecimal eachCash = this.getCash().divide(this.getbNum(),6,BigDecimal.ROUND_HALF_UP);
		this.setSaleUnitPrice(this.salePrice.subtract(eachCash).setScale(2,BigDecimal.ROUND_DOWN));
		this.setTail(this.salePrice.subtract(eachCash));
		this.setActivityPrice(this.costPrice);
		this.setPayPrice(this.salePrice.multiply(this.getbNum()));
		
	}
	/**
	 * 获得商品金额
	 * @return
	 */
	public BigDecimal getGoodsPrice(){
		return this.getCostPrice().multiply(this.getbNum());
	}
	/**
	 * 获得团购优惠
	 * @return
	 */
	public BigDecimal getGroupSale() {
		return (this.costPrice.subtract(this.salePrice)).multiply(this.getbNum());
	}
	/**
	 * 获得计算用的BigDecimal 数量
	 * @return
	 */
	public BigDecimal getbNum(){
		return new BigDecimal(this.getNum());
	}
	/**
	 * 获得现金券
	 * @return
	 */
	public BigDecimal getCash() {
		if(this.isRate==1){
			return this.getCartPrice().multiply(this.rate)
														   .multiply(this.getbNum())
														   .setScale(0,BigDecimal.ROUND_DOWN);
		}
		return BigDecimal.ZERO;
	}
	/**
	 * 包含团购商品价格
	 * @return
	 */
	public BigDecimal getGroupPrice() {
		return this.getSalePrice().multiply(this.getbNum());
	}
	/**
	 * 包含团购商品价格
	 * @return
	 */
	public BigDecimal getTailPrice() {
		BigDecimal eachCash = this.getCash().divide(this.getbNum(),6,BigDecimal.ROUND_HALF_UP);
		return this.salePrice.subtract(eachCash);
	}
	/**
	 * 转换成Map
	 * @return
	 */
	public Map toMap() {
	   Map map = new HashMap<String, Object>();
	   this.setSalePrice(this.costPrice);
	   this.setPayPrice(this.getGoodsPrice());
       JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(this));  
       Iterator it = jsonObject.keySet().iterator();  
       // 遍历jsonObject数据，添加到Map对象  
       while (it.hasNext())  
       {  
           String key = String.valueOf(it.next());  
           Object value = jsonObject.get(key);  
           map.put(key, value);  
       }  
		return map;
	}

}
