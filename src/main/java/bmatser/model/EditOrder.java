package bmatser.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import bmatser.util.DateTypeHelper;

public class EditOrder {
	
	private Long id;
	private Long buyerId;
	private BigDecimal orderAmount;
	private BigDecimal goodsAmount;
	private BigDecimal freightAmount;
	private BigDecimal goodsSaleAmount;
	private BigDecimal freightSaleAmount;
	private BigDecimal taxSaleAmount;
	private BigDecimal scoreDeductionAmout;
	private BigDecimal fullCutAmount;
	private BigDecimal groupOrderSale;
	private Timestamp orderTime;
	private String consignAddressInfo;
	private int isBean;
	private Integer orderStatus;
	protected int bean;//获得金豆
	protected double useBean;//获得金豆
	private int score;
	private List<EditOrderGoods> orderGoods;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}
	public BigDecimal getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}
	public BigDecimal getGoodsAmount() {
		return goodsAmount;
	}
	public void setGoodsAmount(BigDecimal goodsAmount) {
		this.goodsAmount = goodsAmount;
	}
	public BigDecimal getFreightAmount() {
		return freightAmount;
	}
	public void setFreightAmount(BigDecimal freightAmount) {
		this.freightAmount = freightAmount;
	}
	public BigDecimal getGoodsSaleAmount() {
		return goodsSaleAmount;
	}
	public void setGoodsSaleAmount(BigDecimal goodsSaleAmount) {
		this.goodsSaleAmount = goodsSaleAmount;
	}
	public BigDecimal getFreightSaleAmount() {
		return freightSaleAmount;
	}
	public void setFreightSaleAmount(BigDecimal freightSaleAmount) {
		this.freightSaleAmount = freightSaleAmount;
	}
	public BigDecimal getTaxSaleAmount() {
		return taxSaleAmount;
	}
	public void setTaxSaleAmount(BigDecimal taxSaleAmount) {
		this.taxSaleAmount = taxSaleAmount;
	}
	public BigDecimal getScoreDeductionAmout() {
		return scoreDeductionAmout;
	}
	public void setScoreDeductionAmout(BigDecimal scoreDeductionAmout) {
		this.scoreDeductionAmout = scoreDeductionAmout;
	}
	public BigDecimal getFullCutAmount() {
		return fullCutAmount;
	}
	public void setFullCutAmount(BigDecimal fullCutAmount) {
		this.fullCutAmount = fullCutAmount;
	}
	public BigDecimal getGroupOrderSale() {
		return groupOrderSale;
	}
	public void setGroupOrderSale(BigDecimal groupOrderSale) {
		this.groupOrderSale = groupOrderSale;
	}
	public Timestamp getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public List<EditOrderGoods> takeOrderGoods() {
		return orderGoods;
	}
	public List<EditOrderGoods> getOrderGoods() {
		return orderGoods;
	}
	public void setOrderGoods(List<EditOrderGoods> orderGoods) {
		this.orderGoods = orderGoods;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	
	/**
	 * 判断是否为空
	 * @return
	 */
	public boolean isEmpty() {
		return this==null?true : false;
	}
	/**
	 * 只显示需要的字段
	 * @return
	 */
	public Map<String, Object> toPageShow() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderAmount", this.orderAmount);
		map.put("goodsAmount", this.goodsAmount);
		map.put("freightAmount", this.freightAmount);
		map.put("scoreDeductionAmout", this.scoreDeductionAmout);
		map.put("fullCutAmount", this.fullCutAmount);
		map.put("groupOrderSale", this.groupOrderSale);
		map.put("saleAmount", this.countGoodsSale());
		map.put("amount", this.countPayAmount());
		map.put("isBean", 0);
		map.put("score", (this.countPayAmount().subtract(this.freightAmount)).divide(BigDecimal.TEN).setScale(0,BigDecimal.ROUND_DOWN));
		map.put("beanQuantity", this.getBean());
		map.put("beanDeduction", this.getUseBean());
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> groupMap = null;
		Map<String, Object> goodsMap = null;
		List goods = new ArrayList<>();
		List groups = new ArrayList<>();
		for (EditOrderGoods orderGoods : this.orderGoods) {
			if(orderGoods.getGoodsType()==1){
				if(groupMap==null){
					groupMap = new HashMap<String, Object>();
					groupMap.put("name", "团购");
					groupMap.put("list", groups);
					list.add(groupMap);
				}
				groups.add(orderGoods.getShowpage());
			}else if(orderGoods.getGoodsType()==0){
				if(goodsMap==null){
					goodsMap = new HashMap<String, Object>();
					goodsMap.put("name", "工电宝");
					goodsMap.put("list", goods);
					list.add(goodsMap);
				}
				goods.add(orderGoods.getShowpage());
			}
			
		}
		map.put("orderGoods", list);
		return map;
	}
	/**
	 * 计算总现金券
	 */
	public void countScoreDeductionAmout() {
		BigDecimal score = BigDecimal.ZERO;
		for (EditOrderGoods orderGoods : this.orderGoods) {
			score = score.add(orderGoods.getUnitPrice()
					.subtract(orderGoods.getTail())
					.multiply(new BigDecimal(orderGoods.getNum()))
					.setScale(0,BigDecimal.ROUND_HALF_UP));
		}
		this.scoreDeductionAmout = score;
	}
	/**
	 * 计算总商品金额
	 */
	public void countGoodsAmount() {
		BigDecimal goodsAmount = BigDecimal.ZERO;
		for (EditOrderGoods orderGoods : this.orderGoods) {
			goodsAmount = goodsAmount.add(orderGoods.getPrice()
					.multiply(new BigDecimal(orderGoods.getNum())));
		}
		this.goodsAmount = goodsAmount;
	}
	/**
	 * 计算OrderAmount
	 */
	public void countOrderAmount() {
		this.orderAmount = this.goodsAmount.add(this.freightAmount);
	}
	/**
	 * 计算所有金额
	 * @param newGoods 
	 * @param fullCutAmount 
	 * @param freightAmount 
	 */
	public void countAmount(List<EditOrderGoods> newGoods, 
			BigDecimal freightAmount,
			BigDecimal fullCutAmount)
	{
		this.freightAmount = freightAmount!=null?freightAmount : BigDecimal.ZERO;
		this.fullCutAmount = fullCutAmount!=null?fullCutAmount : BigDecimal.ZERO;
		this.orderGoods=newGoods;
		this.countScoreDeductionAmout();
		this.countGoodsAmount();
		this.countOrderAmount();
		this.countScore();
	}
	
	
	/**
	 * 计算积分
	 */
	private void countScore() {
		this.score = countPayAmount().subtract(this.freightAmount)
															.divide(BigDecimal.TEN)
															.intValue();
		
	}
	/**
	 * 计算商品优惠
	 */
	public BigDecimal countGoodsSale() {
		return	this.goodsSaleAmount.add(this.freightSaleAmount)
															.add(this.taxSaleAmount)
															.add(this.fullCutAmount);
	}
	/**
	 * 计算PayAmount
	 */
	public BigDecimal countPayAmount() {
		return	this.goodsAmount.add(this.freightAmount)
													.subtract(this.scoreDeductionAmout)
													.subtract(this.groupOrderSale)
													.subtract(this.countGoodsSale());
	}
	public EditOrder iscash(int cash) {
		if(cash<this.scoreDeductionAmout.intValue()){
			this.scoreDeductionAmout = BigDecimal.ZERO;
			this.countScore();
			for (EditOrderGoods orderGoods : this.orderGoods) {
				orderGoods.setSaleUnitPrice(orderGoods.getUnitPrice());
				orderGoods.setTail(orderGoods.getUnitPrice());
				orderGoods.setSalePrice(orderGoods.getUnitPrice().multiply(orderGoods.getbNum()));
			}
		}
		return this;
	}
	public int getBean() {
		return bean;
	}
	public void setBean(int bean) {
		this.bean = bean;
	}
	public double getUseBean() {
		BigDecimal b = this.countPayAmount();
		if(b.doubleValue()/2<useBean){
			return b.divide(new BigDecimal(2),2,BigDecimal.ROUND_DOWN).doubleValue();
		}
		return useBean;
	}
	public void setUseBean(double useBean) {
		this.useBean = useBean;
	}
	public int getIsBean() {
		return isBean;
	}
	public void setIsBean(int isBean) {
		this.isBean = isBean;
	}
	public String getConsignAddressInfo() {
		return consignAddressInfo;
	}
	public void setConsignAddressInfo(String consignAddressInfo) {
		this.consignAddressInfo = consignAddressInfo;
	}
	public String findProvinceId() {
		JSONObject jo = JSONObject.parseObject(this.consignAddressInfo);
		return jo.getString("provinceId");
	}

	
	
}
