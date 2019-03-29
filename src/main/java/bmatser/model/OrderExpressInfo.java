package bmatser.model;
/**
 * 订单物流信息
 * @author felx
 * 2016-12-27
 */
public class OrderExpressInfo {
	private int id;
	private int shopId;//店铺id 46:工电宝品牌分销店  47:工电宝供应链平台  48:工电宝商城 
	private String shopName;//店铺名称
	private Long originalOrderId;//原始订单号
	private String fexpcomname;//快递公司
	private String fexpNo;//快递单号
	private String fname;//主面单还是子面单
	private String status;//状态 1有效 2删除
	private String logisticsName;//快递公司(中文)
	
	public String getLogisticsName() {
		return logisticsName;
	}
	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getShopId() {
		return shopId;
	}
	public void setShopId(int shopId) {
		this.shopId = shopId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Long getOriginalOrderId() {
		return originalOrderId;
	}
	public void setOriginalOrderId(Long originalOrderId) {
		this.originalOrderId = originalOrderId;
	}
	public String getFexpcomname() {
		return fexpcomname;
	}
	public void setFexpcomname(String fexpcomname) {
		this.fexpcomname = fexpcomname;
	}
	public String getFexpNo() {
		return fexpNo;
	}
	public void setFexpNo(String fexpNo) {
		this.fexpNo = fexpNo;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
