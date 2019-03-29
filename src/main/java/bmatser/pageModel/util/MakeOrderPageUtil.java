package bmatser.pageModel.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import bmatser.pageModel.MakeOrderPage;
import bmatser.util.ResponseData;

public class MakeOrderPageUtil extends MakeOrderPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6053628641489234892L;
	
	/**
	 * 获得商品Id集合
	 * @return
	 */
	public Map<Long, String> getSellerGoodsIdArray(){
		Map<Long, String> fullcutMap = new HashMap<Long, String>();
		for (MakeOrderGoodsPageUtil orderGoods : this.goods) {
			if(orderGoods.getActivityType()==0 && StringUtils.isBlank(orderGoods.getPackageId())){
				Long sellerGoodsId = orderGoods.getSellerGoodsId();
				double gPrice = orderGoods.getCostPrice()
																 .multiply(orderGoods.getbNum())
																 .doubleValue();
                if(fullcutMap.containsKey(orderGoods.getSellerGoodsId())){    
                	double price = Double.valueOf(fullcutMap.get(sellerGoodsId));
                    fullcutMap.put(sellerGoodsId , String.valueOf(gPrice+price));
                } else{
                    fullcutMap.put(sellerGoodsId, String.valueOf(gPrice));
                }
			}
		}
		return fullcutMap;
	}
	/**
	 * 生成保存的订单
	 * @return
	 */
	public MakeOrderPageUtil toCreateOrder() {
		BigDecimal cash = BigDecimal.ZERO ;
		BigDecimal groupSale = BigDecimal.ZERO ;
		BigDecimal goodsPrice = BigDecimal.ZERO ;
		BigDecimal groupPrice = BigDecimal.ZERO ;
		DecimalFormat df   = new DecimalFormat("######0.00");
		DecimalFormat df2   = new DecimalFormat("######0.#");
		for (MakeOrderGoodsPageUtil orderGoods : this.goods) {
			orderGoods.init();
			cash = cash.add(orderGoods.getCash());
			groupSale = groupSale.add(orderGoods.getGroupSale());
			goodsPrice = goodsPrice.add(orderGoods.getGoodsPrice());
			groupPrice = groupPrice.add(orderGoods.getGroupPrice());
		}
		if(this.isCash==0 ||  cash.compareTo(this.getbDealerCash()) > 0){
			this.setTotalCash(df.format(0));
		}else{
			this.setTotalCash(df.format(cash));
		}
		this.setTotalGroupPrice(df.format(groupPrice));
		this.setTotalGoodsPrice(df.format(goodsPrice));
		this.setGroupSale(df.format(groupSale));
		this.setTotalOrderPrice(df.format(this.getOrderPrice()));
		this.setTotalPayPrice(df.format(this.getPayPrice()));
		this.setTotalScore(df2.format(this.getScore()));
		return this;
	}
	
	/**
	 * 获得积分( 订单金额 - 运费 )
	 * @return
	 */
	private BigDecimal getScore() {
		return (this.getPayPrice().subtract(this.getFreight()))
													.divide(new BigDecimal(10),0,BigDecimal.ROUND_DOWN);
	}
	/**
	 * 获得订单金额( 商品金额+运费 )
	 * @return
	 */
	public BigDecimal getOrderPrice(){
		return this.getGoodsPrice().add(this.getFreight());
	}
	/**
	 * 获得付款金额(订单金额 - 满减优惠 - 现金券抵扣 - 团购优惠)
	 * @return
	 */
	public BigDecimal getPayPrice(){
		return this.getOrderPrice().subtract(this.getFullcut())
													  .subtract(this.getCash())
													  .subtract(this.getbGroupSale());
	}
	/**
	 * BigDecimal 商品价格
	 * @return
	 */
	public BigDecimal getGoodsPrice(){
		return new BigDecimal(this.totalGoodsPrice);
	}
	/**
	 * BigDecimal 运费
	 * @return
	 */
	public BigDecimal getFreight(){
		return new BigDecimal(this.totalFreight);
	}
	/**
	 * BigDecimal 团购优惠
	 * @return
	 */
	public BigDecimal getbGroupSale(){
		return new BigDecimal(this.groupSale);
	}
	/**
	 * BigDecimal 满减优惠
	 * @return
	 */
	public BigDecimal getFullcut(){
		return new BigDecimal(this.totalFullcut);
	}
	/**
	 * BigDecimal 现金券抵扣
	 * @return
	 */
	public BigDecimal getCash(){
		return new BigDecimal(this.totalCash);
	}
	/**
	 * BigDecimal 现金券抵扣
	 * @return
	 */
	public BigDecimal getbDealerCash(){
		return new BigDecimal(this.dealerCash);
	}
	/**
	 * 输出saas端确认订单
	 */
	public Map<String, Object> getConfirmOrderBySaas() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalGoodsPrice", this.totalGoodsPrice);
		map.put("totalCash", this.totalCash);
		map.put("totalGroupPrice", this.totalGroupPrice);
		map.put("groupSale", this.groupSale);
		map.put("totalScore", this.totalScore);
		map.put("totalPayPrice", this.totalPayPrice);
		map.put("totalFreight", this.totalFreight);
		map.put("totalOrderPrice", this.totalOrderPrice);
		map.put("totalFullcut", this.totalFullcut);
		map.put("totalBean", this.bean);
		map.put("userBean", this.getUseBean());
		map.put("buyerAddr", this.buyerAddr);
		map.put("buyerInv", this.buyerInv);
		map.put("goods", this.getSaasGoodsList());
		return map;
	}
	/**
	 * 输出app端确认订单
	 * @param isBean 
	 */
	public Map<String, Object> getConfirmOrderByApp(String isBean) {
		Map<String, Object> map = new HashMap<String, Object>();
		String useBean = this.getUseBean();
		map.put("totalGroupPrice", this.totalGroupPrice);
		if(isBean != null && "1".equals(isBean)){
			map.put("totalPayPrice", new BigDecimal(this.totalPayPrice).subtract(new BigDecimal(this.getUseBean())));
		}else{
			map.put("totalPayPrice", this.totalPayPrice);
		}
		map.put("totalFreight", this.totalFreight);
		map.put("totalOrderPrice", this.totalOrderPrice);
		map.put("buyerAddr", this.buyerAddr);
		map.put("buyerInv", this.buyerInv);
		map.put("userBean", useBean);
		map.put("userBeanStr", new BigDecimal(useBean).multiply(new BigDecimal(100)).intValue());
		map.put("goods", this.getSaasGoodsList());
		map.put("attrs", this.getAppAttrs());
		map.put("attrsList", this.getAppAttrsLIst(isBean));
		return map;
	}
	private Object getAppAttrsLIst(String isBean) {
		List<List<Map<String, Object>>> attrList = new ArrayList<>();
		List<Map<String, Object>> list1 = new ArrayList<>();
		List<Map<String, Object>> list2 = new ArrayList<>();
		List<Map<String, Object>> list3 = new ArrayList<>();
		String[] attrValue1={"商品总金额","运费"};
		String[] attrValue2={"抵扣券抵扣","满减金额","团购总优惠"};
		String[] attrValue3={"可获积分","可获得工币"};
		for (int i = 0,len = attrValue1.length; i < len; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", attrValue1[i]);
			switch (i) {
			case 0:map.put("value", "￥" + this.totalGoodsPrice);
				list1.add(map);
				break;
			case 1:map.put("value", "￥" + this.totalFreight);
				list1.add(map);
				break;
			}
		}
		for (int i = 0,len = attrValue2.length; i < len; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", attrValue2[i]);
			switch (i) {
			case 0:map.put("value", "- ￥" + this.totalCash);
						if(!StringUtils.equals("0.00", this.totalCash)){
							list2.add(map);
						}
				break;
			case 1:map.put("value", "- ￥" + this.totalFullcut);
						if(!StringUtils.equals("0.00", this.totalFullcut)){
							list2.add(map);
						}
						break;		
			case 2:map.put("value", "- ￥" +  this.groupSale);
						if(!StringUtils.equals("0.00", this.groupSale)){
							list2.add(map);
						}
			break;
			}
		}
		for (int i = 0,len = attrValue3.length; i < len; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", attrValue3[i]);
			switch (i) {
			case 0:
				if(isBean != null && "1".equals(isBean)){
					BigDecimal b=(new BigDecimal(this.totalPayPrice).subtract(new BigDecimal(this.getUseBean()))
																					.subtract(new BigDecimal(this.totalFreight)))
																					.divide(BigDecimal.TEN,0,BigDecimal.ROUND_DOWN);
					if(b.compareTo(BigDecimal.ZERO)>0){
						map.put("value", b + "分");
					}else{
						map.put("value", 0 + "分");
					}
					list3.add(map);
				}else{
					map.put("value", this.totalScore + "分");
					list3.add(map);
				}
				break;
			case 1:map.put("value", this.bean + "个");
						if(this.bean != 0){
							list3.add(map);
						}
				break;
			}
		}
		if(list1.size()>0){
			attrList.add(list1);
		}
		if(list2.size()>0){
			attrList.add(list2);
		}
		if(list3.size()>0){
			attrList.add(list3);
		}
		return attrList;
	}
	/**
	 * 获得手机端属性
	 * @return
	 */
	private List<Map<String, Object>> getAppAttrs() {
		String[] attrValue={"商品总金额","抵扣券抵扣","满减金额","团购总优惠","工币可抵扣金额","可获积分","可获得工币"};
		DecimalFormat df   = new DecimalFormat("######0.00");
		List<Map<String, Object>> attrList = new ArrayList<Map<String,Object>>();
		for (int i = 0,len = attrValue.length; i < len; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", attrValue[i]);
			switch (i) {
				case 0: map.put("value", "￥" + this.totalGoodsPrice);
					attrList.add(map);
				break;
				case 1: map.put("value", "- ￥" + this.totalCash);
					if(!StringUtils.equals("0.00", this.totalCash)){
						attrList.add(map);
					}
				break;
				case 2: map.put("value", "- ￥" + this.totalFullcut);
					if(!StringUtils.equals("0.00", this.totalFullcut)){
						attrList.add(map);
					}
				break;
				case 3: map.put("value", "- ￥" +  this.groupSale);
					if(!StringUtils.equals("0.00", this.groupSale)){
						attrList.add(map);
					}
				break;
				case 4: String price =this.getUseBean();
					map.put("value", "- ￥" + price);
					if(new BigDecimal(price).compareTo(BigDecimal.ZERO)>0){
						attrList.add(map);
					}
				break;
				case 5: map.put("value", this.totalScore + "分");
				attrList.add(map);
				break;
				case 6: map.put("value", this.bean + "个");
					if(this.bean != 0){
						attrList.add(map);
					}
				break;
			}
		}
		return attrList;
	}
	/**
	 * 获得saas商品显示
	 * @return
	 */
	public List<Map<String,Object>> getSaasGoodsList(){
		String[]  keyArray= {"工电宝","团购"}; 
		ResponseData data = new ResponseData(keyArray);
		List<Map> goodsList = new ArrayList<Map>();
		List<Map> groupList = new ArrayList<Map>();
		for (MakeOrderGoodsPageUtil g : this.goods) {
			if(g.getActivityType()==1){
				groupList.add(g.toMap());
			}else{
				goodsList.add(g.toMap());
			}
		}
		data.addResponseList(goodsList);
		data.addResponseList(groupList);
		return data.getReturnData();
	}
}
