package bmatser.model;


import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="店铺页面搜索传入参数",description="店铺页面搜索传入参数")
public class StoreSearchParam {
	@ApiModelProperty(value="商品名称")
	private String goodsName;
	@ApiModelProperty(value="最低单价")
	private String lowPrice;
	@ApiModelProperty(value="最高单价")
	private String hightPrice;
	@ApiModelProperty(value="页数")
	private String page;
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getLowPrice() {
		return lowPrice;
	}
	public void setLowPrice(String lowPrice) {
		this.lowPrice = lowPrice;
	}
	public String getHightPrice() {
		return hightPrice;
	}
	public void setHightPrice(String hightPrice) {
		this.hightPrice = hightPrice;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	
}
