package bmatser.model;

import java.math.BigDecimal;
import java.util.List;

public class GoodsPackage {
	
	private Long id;
	private String packageId;
	private String packageName;
	private BigDecimal price;
	private int num;
	private BigDecimal packageSale;
	private int activityType=3;
	private List<Object> goodsList;
	
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public BigDecimal getPackageSale() {
		return packageSale;
	}
	public void setPackageSale(BigDecimal packageSale) {
		this.packageSale = packageSale;
	}
	public List<Object> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<Object> goodsList) {
		this.goodsList = goodsList;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getActivityType() {
		return activityType;
	}
	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}
	
	

}
