package bmatser.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class BuyerShoppingCart {
	
	private Integer id;
	private Integer buyerId;
	private Integer num;
	private Integer sellerId;
	private Long sellerGoodsId;
	private Timestamp createTime;
	private BigDecimal salePrice;
	private String status;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(Integer buyerId) {
		this.buyerId = buyerId;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getSellerId() {
		return sellerId;
	}
	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}
	public Long getSellerGoodsId() {
		return sellerGoodsId;
	}
	public void setSellerGoodsId(Long sellerGoodsId) {
		this.sellerGoodsId = sellerGoodsId;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public BigDecimal getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
