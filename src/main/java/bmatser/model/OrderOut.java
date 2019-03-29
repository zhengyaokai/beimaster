package bmatser.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import bmatser.pageModel.util.MakeOrderGoodsPageUtil;

public class OrderOut {
	
	private OrderCountModel  orderInfo;

	public OrderOut(OrderCountModel orderInfo) {
		this.orderInfo = orderInfo;
	}
	
	
	public Map<String, Object> getConfirmOrderBySaas(){
		Map<String, Object> map = new HashMap<String, Object>();
//        map.put("totalGoodsPrice","2000");
		map.put("totalGoodsPrice", b2s(orderInfo.getTotalGoodsPrice()));
		map.put("totalCash", b2s(orderInfo.getTotalCash()));
		map.put("totalGroupPrice", b2s(orderInfo.getTotalGroupPrice()));
		map.put("groupSale", b2s(orderInfo.getGroupSale()));
//		map.put("totalScore", b2s(orderInfo.getTotalScore(),0));
		map.put("totalPayPrice", b2s(orderInfo.getTotalPayPrice()));
		map.put("totalFreight", b2s(orderInfo.getTotalFreight()));
		map.put("totalOrderPrice", b2s(orderInfo.getTotalOrderPrice()));
//		map.put("totalFullcut", b2s(orderInfo.getTotalFullcut()));
		map.put("totalCoin", orderInfo.getBean());
		map.put("userCoin", orderInfo.getUseBean());
		map.put("buyerAddr", orderInfo.getBuyerAddr());
		map.put("buyerInv",orderInfo.getBuyerInv());
		map.put("goods", this.getSaasGoodsList());
		return map;
		
	}
	
	
	/**
	 * 输出app端确认订单
	 * @param isBean 
	 */
	public Map<String, Object> getConfirmOrderByApp(String isBean) {
		Map<String, Object> map = new HashMap<String, Object>();
		String useBean = orderInfo.getUseBean();
		map.put("totalGroupPrice", b2s(orderInfo.getTotalGroupPrice()));
		if(isBean != null && "1".equals(isBean)){
			map.put("totalPayPrice", b2s(orderInfo.getTotalPayPrice().subtract(new BigDecimal(useBean))));
		}else{
			map.put("totalPayPrice",b2s(orderInfo.getTotalPayPrice()));
		}
		map.put("totalFreight", b2s(orderInfo.getTotalFreight()));
		map.put("totalOrderPrice", b2s(orderInfo.getTotalOrderPrice()));
		map.put("buyerAddr", orderInfo.getBuyerAddr());
		map.put("buyerInv", orderInfo.getBuyerInv());
		map.put("userBean", useBean);
		map.put("userBeanStr", new BigDecimal(useBean).multiply(new BigDecimal(100)).intValue());
		map.put("goods", this.getAppGoodsList());
		map.put("attrs", this.getAppAttrs());
		map.put("attrsList", this.getAppAttrsLIst(isBean));
		return map;
	}
	
	
	/**
	 * 获得saas商品显示
	 * @return
	 */
	public List<Map<String,Object>> getSaasGoodsList(){
		List<Map<String, Object>> list = new ArrayList<>();
		if(orderInfo.getDefaultList()!=null && orderInfo.getDefaultList().size()>0){
			Map<String, Object> map = new HashMap<>();
			map.put("name", "工电宝");
			map.put("list", orderInfo.getDefaultList());
			map.put("type", 0);
			list.add(map);
		}
		if(orderInfo.getGroupList()!=null && orderInfo.getGroupList().size()>0){
			Map<String, Object> map = new HashMap<>();
			for (MakeOrderGoodsPageUtil goods : orderInfo.getGroupList()) {
				goods.setSalePrice(goods.getCostPrice());
				goods.setPayPrice(goods.getGoodsPrice());
			}
			map.put("name", "团购");
			map.put("list", orderInfo.getGroupList());
			map.put("type", 1);
			list.add(map);
		}
		if(orderInfo.getPackage()!=null && orderInfo.getPackage().size()>0){
			Map<String, Object> map = new HashMap<>();
			map.put("name", "套餐");
			map.put("list", orderInfo.getPackage());
			map.put("type", 3);
			list.add(map);
		}
		return list;

	}
	
	/**
	 * 获得saas商品显示
	 * @return
	 */
	public List<Map<String,Object>> getAppGoodsList(){
		List<Map<String, Object>> list = new ArrayList<>();
		List<Object> appList = new ArrayList<Object>();
		if(orderInfo.getDefaultList()!=null){
			appList.addAll(orderInfo.getDefaultList());
		}
		if(orderInfo.getPackage()!=null){
			appList.addAll(orderInfo.getPackage());
		}
		if(appList!=null && appList.size()>0){
			Map<String, Object> map = new HashMap<>();
			//appList.addAll(orderInfo.getDefaultList());
			map.put("name", "工电宝");
			map.put("list", appList);
			map.put("type", 0);
			list.add(map);
		}
		if(orderInfo.getGroupList()!=null && orderInfo.getGroupList().size()>0){
			Map<String, Object> map = new HashMap<>();
			map.put("name", "团购");
			for (MakeOrderGoodsPageUtil goods : orderInfo.getGroupList()) {
				goods.setSalePrice(goods.getCostPrice());
				goods.setPayPrice(goods.getGoodsPrice());
			}
			map.put("list", orderInfo.getGroupList());
			map.put("type", 1);
			list.add(map);
		}
		return list;

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
			case 0:map.put("value", "￥" + b2s(orderInfo.getTotalGoodsPrice()));
				list1.add(map);
				break;
			case 1:map.put("value", "￥" + b2s(orderInfo.getTotalFreight()));
				list1.add(map);
				break;
			}
		}
		for (int i = 0,len = attrValue2.length; i < len; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", attrValue2[i]);
			switch (i) {
			case 0:map.put("value", "- ￥" + b2s(orderInfo.getTotalCash()));
						if(!StringUtils.equals("0.00", b2s(orderInfo.getTotalCash()))){
							list2.add(map);
						}
				break;
			case 1:map.put("value", "- ￥" + b2s(orderInfo.getTotalFullcut()));
						if(!StringUtils.equals("0.00", b2s(orderInfo.getTotalFullcut()))){
							list2.add(map);
						}
						break;		
			case 2:map.put("value", "- ￥" +  b2s(orderInfo.getGroupSale()));
						if(!StringUtils.equals("0.00", b2s(orderInfo.getGroupSale()))){
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
					BigDecimal b=(orderInfo.getTotalPayPrice().subtract(new BigDecimal(orderInfo.getUseBean()))
																					.subtract(orderInfo.getTotalFreight())
																					.divide(BigDecimal.TEN,0,BigDecimal.ROUND_DOWN));
					if(b.compareTo(BigDecimal.ZERO)>0){
						map.put("value", b + "分");
					}else{
						map.put("value", 0 + "分");
					}
					list3.add(map);
				}else{
					map.put("value", b2s(orderInfo.getTotalScore(),0) + "分");
					list3.add(map);
				}
				break;
			case 1:map.put("value", orderInfo.getBean() + "个");
						if(orderInfo.getBean() != 0){
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
				case 0: map.put("value", "￥" + b2s(orderInfo.getTotalGoodsPrice()));
					attrList.add(map);
				break;
				case 1: map.put("value", "- ￥" + b2s(orderInfo.getTotalCash()));
					if(!StringUtils.equals("0.00", b2s(orderInfo.getTotalCash()))){
						attrList.add(map);
					}
				break;
				case 2: map.put("value", "- ￥" + b2s(orderInfo.getTotalFullcut()));
					if(!StringUtils.equals("0.00", b2s(orderInfo.getTotalFullcut()))){
						attrList.add(map);
					}
				break;
				case 3: map.put("value", "- ￥" +  b2s(orderInfo.getGroupSale()));
					if(!StringUtils.equals("0.00", b2s(orderInfo.getGroupSale()))){
						attrList.add(map);
					}
				break;
				case 4: String price =orderInfo.getUseBean();
					map.put("value", "- ￥" + price);
					if(new BigDecimal(price).compareTo(BigDecimal.ZERO)>0){
						attrList.add(map);
					}
				break;
				case 5: map.put("value", b2s(orderInfo.getTotalScore(),0) + "分");
				attrList.add(map);
				break;
				case 6: map.put("value", orderInfo.getBean() + "个");
					if(orderInfo.getBean() != 0){
						attrList.add(map);
					}
				break;
			}
		}
		return attrList;
	}

	/**
	 * @author felx
	 * @describe BigDecimal保留两位小数
	 * @param m 数字
	 * @return
	 */
	public String b2s(BigDecimal m){
		DecimalFormat df = new DecimalFormat("0.00") ;
		return df.format(m.doubleValue());
	}
	
	/**
	 * @author felx
	 * @describe BigDecimal保留几位小数
	 * @param m 数字
	 * @param num 位数
	 * @return
	 */
	public String b2s(BigDecimal m,int num){
		DecimalFormat df = null;
		if(num<=0){
			df = new DecimalFormat("0") ;
		}else{
			StringBuffer buffer=new StringBuffer("0.");
			for (int j = 0; j < num; j++) {
				buffer = buffer.append("0");
			}
			df = new DecimalFormat(buffer.toString()) ;
		}
		return df.format(m.doubleValue());
	}
	
}
