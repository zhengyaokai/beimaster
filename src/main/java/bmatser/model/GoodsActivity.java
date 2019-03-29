package bmatser.model;

import java.util.Date;

public class GoodsActivity {
    private Integer id;

    private String activityName;

    private String bannerImage;

    private String bigBannerImage;

    private String bigBannerUrl;

    private String logoImage;

    private String brief;

    private String bannerDeductionRate;

    private Date startTime;

    private Date endTime;

    private Integer orderSn;

    private String status;

    private Date createTime;

    private Integer createUserId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName == null ? null : activityName.trim();
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage == null ? null : bannerImage.trim();
    }

    public String getBigBannerImage() {
        return bigBannerImage;
    }

    public void setBigBannerImage(String bigBannerImage) {
        this.bigBannerImage = bigBannerImage == null ? null : bigBannerImage.trim();
    }

    public String getBigBannerUrl() {
        return bigBannerUrl;
    }

    public void setBigBannerUrl(String bigBannerUrl) {
        this.bigBannerUrl = bigBannerUrl == null ? null : bigBannerUrl.trim();
    }

    public String getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(String logoImage) {
        this.logoImage = logoImage == null ? null : logoImage.trim();
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief == null ? null : brief.trim();
    }

    public String getBannerDeductionRate() {
        return bannerDeductionRate;
    }

    public void setBannerDeductionRate(String bannerDeductionRate) {
        this.bannerDeductionRate = bannerDeductionRate == null ? null : bannerDeductionRate.trim();
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