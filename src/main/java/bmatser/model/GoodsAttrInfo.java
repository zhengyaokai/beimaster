package bmatser.model;

import org.apache.solr.client.solrj.beans.Field;

public class GoodsAttrInfo {
	
	@Field
	private String id;
/*	@Field
	private Long attrId;*/
	@Field
	private Long spuId;
/*	@Field
	private Long attrValueId;*/
/*	@Field
	private Long goodsId;*/
	@Field
	private int orderSn;
	@Field
	private String attrValue;
	@Field
	private String attrName;
	@Field
	private Long sellerGoodsId;

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public Long getSpuId() {
		return spuId;
	}
	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public String getAttrValue() {
		return attrValue;
	}
	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}
	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	public int getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(int orderSn) {
		this.orderSn = orderSn;
	}
	public Long getSellerGoodsId() {
		return sellerGoodsId;
	}
	public void setSellerGoodsId(Long sellerGoodsId) {
		this.sellerGoodsId = sellerGoodsId;
	}

	
	
	
	
	
	

}
