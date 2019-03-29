package bmatser.model;

import java.math.BigDecimal;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
@ApiModel(value="询价单")
public class EnquiryPriceGoods {
	@ApiModelProperty(value="id")
    private Integer id;
	@ApiModelProperty(value="询价单id")
    private Integer enquiryPriceId;
	@ApiModelProperty(value="品牌名称")
    private String brandName;
	@ApiModelProperty(value="型号")
    private String model;
	@ApiModelProperty(value="数量")
    private Integer num;
	@ApiModelProperty(value="商品id")
    private Integer goodsId;
	@ApiModelProperty(value="价格")
    private BigDecimal price;
	@ApiModelProperty(value="货期")
    private String deliveryTime;
	@ApiModelProperty(value="备注")
    private String remarks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEnquiryPriceId() {
        return enquiryPriceId;
    }

    public void setEnquiryPriceId(Integer enquiryPriceId) {
        this.enquiryPriceId = enquiryPriceId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName == null ? null : brandName.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime == null ? null : deliveryTime.trim();
    }

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
    
}