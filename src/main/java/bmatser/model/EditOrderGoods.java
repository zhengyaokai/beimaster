package bmatser.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class EditOrderGoods {

	private Long id;
	private Long orderId;
	private Long goodsId;
	private Long sellerGoodsId;
	private int num;
	private BigDecimal unitPrice;
	private BigDecimal salePrice;
	private BigDecimal saleUnitPrice;
	private BigDecimal costPrice;
	private BigDecimal tail;
	private int goodsType;
	private Long activityId;
	private BigDecimal price;
	private String buyNo;
	private String model;
	private String brandName;
	private int batchQuantity;
	private int orderQuantity;
	private String image;
	private String title;
	private String packageId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public Long getSellerGoodsId() {
		return sellerGoodsId;
	}
	public void setSellerGoodsId(Long sellerGoodsId) {
		this.sellerGoodsId = sellerGoodsId;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public BigDecimal getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}
	public BigDecimal getSaleUnitPrice() {
		return saleUnitPrice;
	}
	public void setSaleUnitPrice(BigDecimal saleUnitPrice) {
		this.saleUnitPrice = saleUnitPrice;
	}
	public BigDecimal getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}
	public BigDecimal getTail() {
		return tail;
	}
	public void setTail(BigDecimal tail) {
		this.tail = tail;
	}
	public int getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}
	public Long getActivityId() {
		return activityId;
	}
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	
	
	public String getBuyNo() {
		return buyNo;
	}
	public void setBuyNo(String buyNo) {
		this.buyNo = buyNo;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public int getBatchQuantity() {
		return batchQuantity;
	}
	public void setBatchQuantity(int batchQuantity) {
		this.batchQuantity = batchQuantity;
	}
	public int getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(int orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	public String getImage() {
		return image;
	}
	public BigDecimal getbNum() {
		return new BigDecimal(this.num);
	}
	public void setImage(String image) {
		this.image = image;
	}
	/**
	 * sellerGoods 转换成orderGoods
	 * @param editGoods
	 */
	public void conversionSellerGoods(EditGoods editGoods) {
		this.setNum(editGoods.getNum());
		if(editGoods.getIsRate() == 1){
			BigDecimal cash = editGoods.getSalePrice().multiply(editGoods.getBNum()).multiply(editGoods.getRate()).setScale(0,BigDecimal.ROUND_DOWN);
			BigDecimal eachCash = cash.divide(editGoods.getBNum(),6,BigDecimal.ROUND_DOWN);
			this.setTail(editGoods.getSalePrice().subtract(eachCash));
			this.setSaleUnitPrice(editGoods.getSalePrice().subtract(eachCash).setScale(2,BigDecimal.ROUND_DOWN));
			this.setPrice(editGoods.getSalePrice());
			this.setUnitPrice(editGoods.getSalePrice());
			this.setSalePrice(editGoods.getSalePrice().multiply(editGoods.getBNum()).subtract(cash));
		}else{
			this.setTail(editGoods.getSalePrice());
			this.setSaleUnitPrice(editGoods.getSalePrice());
			this.setPrice(editGoods.getSalePrice());
			this.setUnitPrice(editGoods.getSalePrice());
			this.setSalePrice(editGoods.getSalePrice().multiply(editGoods.getBNum()));
		}
		this.setGoodsId(editGoods.getGoodsId());
		this.setSellerGoodsId(editGoods.getId());
		this.setBuyNo(editGoods.getBuyNo());
		this.setModel(editGoods.getModel());
		this.setBrandName(editGoods.getBrandName());
		this.setBatchQuantity(editGoods.getBatchQuantity());
		this.setOrderQuantity(editGoods.getOrderQuantity());
		this.setImage(editGoods.getImage());
		this.setTitle(editGoods.getTitle());
		this.packageId =editGoods.getPackageId();
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Map<String, Object> getShowpage() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderGoodsId", this.id);
		map.put("sellerGoodsId", this.sellerGoodsId);
		map.put("salePrice", this.unitPrice);
		map.put("num", this.num);
		map.put("title", this.title);
		map.put("buyNo", this.buyNo);
		map.put("model", this.model);
		map.put("brandName", this.brandName);
		map.put("batchQuantity", this.batchQuantity);
		map.put("orderQuantity", this.orderQuantity);
		map.put("image", this.image);
		map.put("title", this.title);
		return map;
	}
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	
	
	
	
}
