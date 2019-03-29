package bmatser.model;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
@ApiModel(value="商家现金券记录",description="商家现金券记录")
public class DealerCash {
    private Long id;
    @ApiModelProperty(value="商家ID")
    private Integer dealerId;
    @ApiModelProperty(value="现金券金额（整数表示充值，负数表示消费）")
    private Integer cash;
    @ApiModelProperty(value="添加时间")
    private Date createTime;
    @ApiModelProperty(value="充值人（0表示系统自动充值）")
    private Integer createUserId;
    @ApiModelProperty(value="有效截止时间，适用充值现金")
    private Date endDate;
    @ApiModelProperty(value="备注")
    private String remark;
    @ApiModelProperty(value="关联表ID")
    private Long relatedId;
    @ApiModelProperty(value="消费类型（1：充值 2：抵货款）")
    private Integer type;
    @ApiModelProperty(value="状态（1：有效 2：删除 3：冻结）")
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public Integer getCash() {
        return cash;
    }

    public void setCash(Integer cash) {
        this.cash = cash;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
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

    public Long getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}