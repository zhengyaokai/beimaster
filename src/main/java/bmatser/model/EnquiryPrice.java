package bmatser.model;

import java.util.Date;
import java.util.List;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="询价单")
public class EnquiryPrice {
	@ApiModelProperty(value="询价单id")
    private Integer id;
	@ApiModelProperty(value="询价单名称")
    private String name;
	@ApiModelProperty(value="询价单截止日期")
    private Date endDate;
	@ApiModelProperty(value="询价单备注")
    private String remark;
	@ApiModelProperty(value="询价单发起客户")
    private Integer dealerId;
	@ApiModelProperty(value="询价单创建时间")
    private Date createTime;
	@ApiModelProperty(value="询价单状态")
    private String status;
	@ApiModelProperty(value="询价单报价人")
    private Integer quotedStaffId;
	@ApiModelProperty(value="询价单商品")	
	private List<EnquiryPriceGoods> enquiryPriceGoods;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Integer getQuotedStaffId() {
        return quotedStaffId;
    }

    public void setQuotedStaffId(Integer quotedStaffId) {
        this.quotedStaffId = quotedStaffId;
    }

	public List<EnquiryPriceGoods> getEnquiryPriceGoods() {
		return enquiryPriceGoods;
	}

	public void setEnquiryPriceGoods(List<EnquiryPriceGoods> enquiryPriceGoods) {
		this.enquiryPriceGoods = enquiryPriceGoods;
	}
    
    
}