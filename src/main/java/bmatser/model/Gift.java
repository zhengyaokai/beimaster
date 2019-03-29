package bmatser.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="礼品",description="礼品")
public class Gift {
	private Long id;
	@ApiModelProperty(value="积分类型ID，关联积分类型表（gift_cate）")
    private Integer giftCateId;
	@ApiModelProperty(value="礼品名称")
    private String name;
	@ApiModelProperty(value="小图")
    private String smallPic;
	@ApiModelProperty(value="目标url")
    private String targetUrl;
	@ApiModelProperty(value="详情大图")
    private String bigPic;
	@ApiModelProperty(value="横幅图片")
    private String banner;
	@ApiModelProperty(value="特殊图片")
    private String spePic;
	@ApiModelProperty(value="描述")
    private String brief;
	@ApiModelProperty(value="总数量")
    private Integer totalNum;
	@ApiModelProperty(value="剩余数量")
    private Integer surplusNum;
	@ApiModelProperty(value="市场价")
    private BigDecimal marketPrice;
	@ApiModelProperty(value="所需积分")
    private Integer score;
	@ApiModelProperty(value="每商家限兑换数量")
    private int limitNum;
	@ApiModelProperty(value="开始兑换时间")
    private Timestamp limitStartTime;
	@ApiModelProperty(value="兑换结束时间")
    private Timestamp limitEndTime;
	@ApiModelProperty(value="显示顺序，数值越大越靠前")
    private Integer orderSn;
	@ApiModelProperty(value="添加时间")
    private Timestamp createTime;
	@ApiModelProperty(value="添加人")
    private Integer createUserId;
	@ApiModelProperty(value="状态（1：有效 2：删除）")
    private String status;
	@ApiModelProperty(value="同步码")
    private String synCode;
	@ApiModelProperty(value="商品型号")
    private String model;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getGiftCateId() {
		return giftCateId;
	}
	public void setGiftCateId(Integer giftCateId) {
		this.giftCateId = giftCateId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSmallPic() {
		return smallPic;
	}
	public void setSmallPic(String smallPic) {
		this.smallPic = smallPic;
	}
	public String getTargetUrl() {
		return targetUrl;
	}
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
	public String getBigPic() {
		return bigPic;
	}
	public void setBigPic(String bigPic) {
		this.bigPic = bigPic;
	}
	public String getBanner() {
		return banner;
	}
	public void setBanner(String banner) {
		this.banner = banner;
	}
	public String getSpePic() {
		return spePic;
	}
	public void setSpePic(String spePic) {
		this.spePic = spePic;
	}
	public String getBrief() {
		return brief;
	}
	public void setBrief(String brief) {
		this.brief = brief;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public Integer getSurplusNum() {
		return surplusNum;
	}
	public void setSurplusNum(Integer surplusNum) {
		this.surplusNum = surplusNum;
	}
	public BigDecimal getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public int getLimitNum() {
		return limitNum;
	}
	public void setLimitNum(int limitNum) {
		this.limitNum = limitNum;
	}
	public Timestamp getLimitStartTime() {
		return limitStartTime;
	}
	public void setLimitStartTime(Timestamp limitStartTime) {
		this.limitStartTime = limitStartTime;
	}
	public Timestamp getLimitEndTime() {
		return limitEndTime;
	}
	public void setLimitEndTime(Timestamp limitEndTime) {
		this.limitEndTime = limitEndTime;
	}
	public Integer getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(Integer orderSn) {
		this.orderSn = orderSn;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Integer getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSynCode() {
		return synCode;
	}
	public void setSynCode(String synCode) {
		this.synCode = synCode;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	
	
}
