package bmatser.model;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
@ApiModel(value="图片广告")
public class AdverImage {
	@ApiModelProperty(value="图片广告id")
    private Integer id;
	@ApiModelProperty(value="标题")
    private String title;
	@ApiModelProperty(value="简要描述")
    private String brief;
	@ApiModelProperty(value="图片")
    private String image;
	@ApiModelProperty(value="手机端图片")
    private String mImage;
	@ApiModelProperty(value="目标URL")
    private String targetUrl;
	@ApiModelProperty(value="开始时间")
    private Date startTime;
	@ApiModelProperty(value="截止时间")
    private Date endTime;
	@ApiModelProperty(value="排序")
    private Integer orderSn;
	@ApiModelProperty(value="终端类型（1：PC和移动端 2：PC端 3：移动端）")
    private Short terminalType;
	@ApiModelProperty(value="放置位置（1：特价区banner 2：厂家直发区banner 3：列表页banner 4：商城首页banner 5：活动汇banner）")
    private Integer positionId;
	@ApiModelProperty(value="品牌ID")
    private Integer brandId;
	@ApiModelProperty(value="状态（1：有效 2：删除）")
    private String status;
	@ApiModelProperty(value="添加时间")
    private Date createTime;
	@ApiModelProperty(value="添加人")
    private Integer createUserId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief == null ? null : brief.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage == null ? null : mImage.trim();
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl == null ? null : targetUrl.trim();
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

    public Short getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(Short terminalType) {
        this.terminalType = terminalType;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
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