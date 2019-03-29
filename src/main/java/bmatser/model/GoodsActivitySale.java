package bmatser.model;

import java.math.BigDecimal;
import java.util.Date;

public class GoodsActivitySale {
    private Long id;

    private Long sellerGoodsId;

    private String goodsNo;

    private Long activityBrandId;

    private Integer stock;

    private Integer surplusStock;

    private Integer perLimtNum;

    private String deductionRate;

    private BigDecimal salePrice;

    private Integer orderSn;

    private String status;

    private Date createTime;

    private Integer createUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSellerGoodsId() {
        return sellerGoodsId;
    }

    public void setSellerGoodsId(Long sellerGoodsId) {
        this.sellerGoodsId = sellerGoodsId;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo == null ? null : goodsNo.trim();
    }

    public Long getActivityBrandId() {
        return activityBrandId;
    }

    public void setActivityBrandId(Long activityBrandId) {
        this.activityBrandId = activityBrandId;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSurplusStock() {
        return surplusStock;
    }

    public void setSurplusStock(Integer surplusStock) {
        this.surplusStock = surplusStock;
    }

    public Integer getPerLimtNum() {
        return perLimtNum;
    }

    public void setPerLimtNum(Integer perLimtNum) {
        this.perLimtNum = perLimtNum;
    }

    public String getDeductionRate() {
        return deductionRate;
    }

    public void setDeductionRate(String deductionRate) {
        this.deductionRate = deductionRate == null ? null : deductionRate.trim();
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(Integer orderSn) {
        this.orderSn = orderSn;
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