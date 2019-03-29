package bmatser.model;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;

public class OrderReceiptInfo {
	private int id;
	private int shopId;//店铺id  46:工电宝品牌分销店  47:工电宝供应链平台  48:工电宝商城 
	private String shopName;//店铺名称
	private Long originalOrderId;//原始订单号
	private String fjsNo;//发票号
	private BigDecimal ftradeamt;//订单在发票中的金额
	private BigDecimal famt;//订单在发票中的金额
	private Timestamp fcheckTime;//发票时间
	private String fexpcomname;//快递公司名称
	private String fexpNo;//快递单号
	private String status;//状态 1有效 2删除
	private String logisticsName;//快递公司(中文)
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
	public String getFjsNo() {
		return fjsNo;
	}
	public void setFjsNo(String fjsNo) {
		this.fjsNo = fjsNo;
	}
	public BigDecimal getFtradeamt() {
		return ftradeamt;
	}
	public void setFtradeamt(BigDecimal ftradeamt) {
		this.ftradeamt = ftradeamt;
	}
	public BigDecimal getFamt() {
		return famt;
	}
	public void setFamt(BigDecimal famt) {
		this.famt = famt;
	}
	public Timestamp getFcheckTime() {
		return fcheckTime;
	}
	public void setFcheckTime(Timestamp fcheckTime) {
		this.fcheckTime = fcheckTime;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLogisticsName() {
		return logisticsName;
	}
	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}
	
}
