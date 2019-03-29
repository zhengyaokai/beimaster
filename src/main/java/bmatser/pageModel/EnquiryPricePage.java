package bmatser.pageModel;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import bmatser.model.EnquiryPriceGoods;

import java.util.Date;
import java.util.List;

@ApiModel(value="询价单")
public class EnquiryPricePage{
	@ApiModelProperty(value="询价名称")
	private String name;
	@ApiModelProperty(value="询价截止日期")
	private String endDate;
	@ApiModelProperty(value="备注")
	private String remark;
	@ApiModelProperty(value="询价客户")
	private Integer dealerId;
	@ApiModelProperty(value="询价货品的json数组")
	private List<EnquiryPriceGoods> goodsList;



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getDealerId() {
		return dealerId;
	}

	public void setDealerId(Integer dealerId) {
		this.dealerId = dealerId;
	}

	public List<EnquiryPriceGoods> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<EnquiryPriceGoods> goodsList) {
		this.goodsList = goodsList;
	}
}
