package bmatser.model;

import java.util.Date;

public class GoodsActivityBrand {
    private Long id;

    private Integer goodsActivityId;

    private Integer brandId;

    private String deductionRate;

    private Integer stock;

    private Integer perLimtNum;

    private Date startTime;

    private Date endTime;

    private String status;

    private Date createTime;

    private Integer createUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGoodsActivityId() {
        return goodsActivityId;
    }

    public void setGoodsActivityId(Integer goodsActivityId) {
        this.goodsActivityId = goodsActivityId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getDeductionRate() {
        return deductionRate;
    }

    public void setDeductionRate(String deductionRate) {
        this.deductionRate = deductionRate == null ? null : deductionRate.trim();
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getPerLimtNum() {
        return perLimtNum;
    }

    public void setPerLimtNum(Integer perLimtNum) {
        this.perLimtNum = perLimtNum;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
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
}