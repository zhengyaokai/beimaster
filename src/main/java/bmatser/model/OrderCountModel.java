package bmatser.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import bmatser.pageModel.util.MakeOrderGoodsPageUtil;

public class OrderCountModel {
	
	private BigDecimal totalGoodsPrice = BigDecimal.ZERO;//商品总金额
	private BigDecimal totalCash = BigDecimal.ZERO;//总现金券
	private BigDecimal tail = BigDecimal.ZERO;//现金券尾差
	private BigDecimal totalGroupPrice = BigDecimal.ZERO;//总团购价格
	private BigDecimal groupSale = BigDecimal.ZERO;//团购优惠
	//private BigDecimal totalScore;//积分
	//private BigDecimal totalPayPrice;//实付金额（算优惠）
	private BigDecimal totalFreight = BigDecimal.ZERO;//运费
	//private BigDecimal totalOrderPrice;//订单金额（不算优惠）
	private BigDecimal totalFullcut = BigDecimal.ZERO;//满减金额
	private BigDecimal totalPackageSale = BigDecimal.ZERO;//套餐优惠
	private int isCash=1;//是否现金券
	private int bean;//获得工币
	private int useBean;//使用工币
	private int dealerCash;//现金券
	private Map<String, Object> buyerAddr;//默认地址
	private Map<String, Object> buyerInv;//默认发票
	private List<MakeOrderGoodsPageUtil> defaultList = new ArrayList<>();//默认商品
	private List<MakeOrderGoodsPageUtil> groupList = new ArrayList<>();//团购商品
	private List<MakeOrderGoodsPageUtil> packageGoodsList = new ArrayList<>();//套餐商品
	private List<GoodsPackage> packageList = new ArrayList<>();//套餐信息集合
	private List<GoodsPackage> packageInfo;//套餐信息集合
	
	
	public BigDecimal getTotalGoodsPrice() {
		return totalGoodsPrice;
	}
	public void setTotalGoodsPrice(BigDecimal totalGoodsPrice) {
		this.totalGoodsPrice = totalGoodsPrice;
	}
	/**
	 * @author felx
	 * @describe 获取现金券,isCash=1时自动计算现金券(默认1)
	 * @return
	 */
	public BigDecimal getTotalCash() {
		if(this.isCash == 1 && this.totalCash.compareTo(new BigDecimal(this.dealerCash))<=0){
			return this.totalCash;
		}
		return BigDecimal.ZERO;
	}
	public void setTotalCash(BigDecimal totalCash) {
		this.totalCash = totalCash;
	}
	public BigDecimal getTail() {
		return tail;
	}
	public void setTail(BigDecimal tail) {
		this.tail = tail;
	}
	public BigDecimal getTotalGroupPrice() {
		return totalGroupPrice;
	}
	public void setTotalGroupPrice(BigDecimal totalGroupPrice) {
		this.totalGroupPrice = totalGroupPrice;
	}
	public BigDecimal getGroupSale() {
		return groupSale;
	}
	public void setGroupSale(BigDecimal groupSale) {
		this.groupSale = groupSale;
	}
	public BigDecimal getTotalScore() {
		return this.getTotalPayPrice().subtract(this.totalFreight).divide(new BigDecimal(10),0,BigDecimal.ROUND_DOWN);
	}

	/**
	 * @author felx
	 * @describe 获取付款金额
	 * @return
	 */
	public BigDecimal getTotalPayPrice() {
/*		BigDecimal payPrice =  this.totalGoodsPrice.add(this.totalFreight).subtract(this.groupSale).subtract(this.totalFullcut).subtract(getTotalCash());
		if(this.isCash ==1 &&  this.totalCash.compareTo(new BigDecimal(this.dealerCash))<=0 ){
			return payPrice.subtract(this.totalCash);
		}else{
			return payPrice;
		}*/
		return this.totalGoodsPrice.add(this.totalFreight).subtract(this.groupSale).subtract(this.totalFullcut).subtract(getTotalCash());
	}
/*	public void setTotalPayPrice(BigDecimal totalPayPrice) {
		this.totalPayPrice = totalPayPrice;
	}*/
	public BigDecimal getTotalFreight() {
		return totalFreight;
	}
	public void setTotalFreight(BigDecimal totalFreight) {
		this.totalFreight = totalFreight;
	}
	public BigDecimal getTotalOrderPrice() {
		return this.totalGoodsPrice.add(this.totalFreight);
	}

	public BigDecimal getTotalFullcut() {
		return totalFullcut;
	}
	public void setTotalFullcut(BigDecimal totalFullcut) {
		this.totalFullcut = totalFullcut;
	}
	public int getIsCash() {
		return isCash;
	}
	public void setIsCash(int isCash) {
		this.isCash = isCash;
	}
	public int getBean() {
		return bean;
	}
	public void setBean(int bean) {
		this.bean = bean;
	}
	public String getUseBean() {
		if(getTotalPayPrice().divide(new BigDecimal(2)).compareTo(new BigDecimal(this.useBean).divide(new BigDecimal(100)))<=0){
			return getTotalPayPrice().divide(new BigDecimal(2),2,BigDecimal.ROUND_DOWN).toString();
		}
		return new BigDecimal(this.useBean).divide(new BigDecimal(100)).toString();
	}
	public void setUseBean(int useBean) {
		this.useBean = useBean;
	}
	public int getDealerCash() {
		return dealerCash;
	}
	public void setDealerCash(int dealerCash) {
		this.dealerCash = dealerCash;
	}
	public Map<String, Object> getBuyerAddr() {
		return buyerAddr;
	}
	public void setBuyerAddr(Map<String, Object> buyerAddr) {
		this.buyerAddr = buyerAddr;
	}
	public Map<String, Object> getBuyerInv() {
		return buyerInv;
	}
	public void setBuyerInv(Map<String, Object> buyerInv) {
		this.buyerInv = buyerInv;
	}
	public List<MakeOrderGoodsPageUtil> getDefaultList() {
		return defaultList;
	}
	/**
	 * 普通商品计算
	 * @author felx
	 * @describe 
	 * @param defaultList
	 */
	public void setDefaultList(List<MakeOrderGoodsPageUtil> defaultList) {
		if(defaultList!=null && defaultList.size()>0){
			for (MakeOrderGoodsPageUtil goods : defaultList) {
				goods.init();


				/**
                 * create by yr on 2018-09-18
                 */
                System.out.println(goods.getCostPrice()+"==costprice="+goods.getGoodsPrice()+"=goodsprice"+goods.getCartPrice()+"==cartprice");

                this.totalGoodsPrice = this.totalGoodsPrice.add(goods.getCostPrice().multiply(goods.getbNum()));
				this.totalCash = this.totalCash.add(goods.getCash());
				this.tail = this.tail.add(goods.getTail());
			}
		}
		this.defaultList = defaultList;
	}
	
	
	public List<MakeOrderGoodsPageUtil> getGroupList() {
		return groupList;
	}
	
	/**
	 * @author felx
	 * @describe 设置团购集合
	 * @param groupList
	 */
	public void setGroupList(List<MakeOrderGoodsPageUtil> groupList) {
		if(groupList!=null && groupList.size()>0){
			for (MakeOrderGoodsPageUtil goods : groupList) {
				goods.init();
				this.totalCash = this.totalCash.add(goods.getCash());
				this.totalGoodsPrice = this.totalGoodsPrice.add(goods.getGoodsPrice());
				this.groupSale = this.groupSale.add(goods.getGroupSale());
				this.totalGroupPrice =  this.totalGroupPrice.add(goods.getGroupPrice());
			}
		}
		this.groupList = groupList;
	}
	public List<MakeOrderGoodsPageUtil> getPackageGoodsList() {
		return packageGoodsList;
	}
	/**
	 * @author felx
	 * @describe 
	 * @param packageGoodsList
	 */
	public void setPackageGoodsList(List<MakeOrderGoodsPageUtil> packageGoodsList) {
		if(packageGoodsList!=null && packageGoodsList.size()>0){
			for (MakeOrderGoodsPageUtil goods : packageGoodsList) {
				goods.init();
			}
		}
		this.packageGoodsList = packageGoodsList;
	}
	public List<GoodsPackage> getPackageList() {
		return packageList;
	}
	/**
	 * @author felx
	 * @describe 加入套餐活动
	 * @param packageList
	 */
	public void setPackageList(List<GoodsPackage> packageList) {
		if(packageList!=null && packageList.size()>0){
			for (GoodsPackage goodsPackage : packageList) {
				this.totalGoodsPrice = this.totalGoodsPrice.add(goodsPackage.getPrice().multiply(new BigDecimal(goodsPackage.getNum())));
				this.totalPackageSale = this.totalPackageSale.add(goodsPackage.getPackageSale().multiply(new BigDecimal(goodsPackage.getNum())));
			}
		}
		this.packageList = packageList;
	}
	public BigDecimal getTotalPackageSale() {
		return totalPackageSale;
	}
	public void setTotalPackageSale(BigDecimal totalPackageSale) {
		this.totalPackageSale = totalPackageSale;
	}
	
	
	
	/**
	 * 获得商品Id集合
	 * @return
	 */
	public Map<Long, String> getSellerGoodsIdArray(){
		Map<Long, String> fullcutMap = new HashMap<Long, String>();
		for (MakeOrderGoodsPageUtil orderGoods : this.defaultList) {
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
	 * @author felx
	 * @describe 获取套餐组合
	 * @return
	 */
	public List<GoodsPackage> getPackage(){
		if(this.packageList.size()==0 || this.packageGoodsList.size() == 0){
			return null;
		}
		if(packageInfo!=null){
			return packageInfo;
		}
		List<MakeOrderGoodsPageUtil> packageList = this.packageGoodsList;
		List<GoodsPackage> list = this.packageList;
		for (MakeOrderGoodsPageUtil cart : packageList) {
			for (GoodsPackage p : list) {
				String packageId = p.getPackageId();
				if(packageId.equals(cart.getActivityId())){
					if(p.getGoodsList()!=null){
						p.getGoodsList().add(cart);
					}else{
						p.setGoodsList(new ArrayList<Object>());
						p.getGoodsList().add(cart);
					}
				}
			}
		}
		packageInfo=list;
		return packageInfo;
	}
}
