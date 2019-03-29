package bmatser.model;

public class GoodsActivityNoticeBrand {
    private Integer id;

    private Integer activityNoticeId;

    private Integer brandId;

    private String brandImage;

    private String brief;

    private Integer orderSn;

    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivityNoticeId() {
        return activityNoticeId;
    }

    public void setActivityNoticeId(Integer activityNoticeId) {
        this.activityNoticeId = activityNoticeId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandImage() {
        return brandImage;
    }

    public void setBrandImage(String brandImage) {
        this.brandImage = brandImage == null ? null : brandImage.trim();
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief == null ? null : brief.trim();
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
}