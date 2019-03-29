package bmatser.model;

import java.sql.Timestamp;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
@ApiModel(value="商品评价",description="商品评价")
public class MemberComment {
	
	private Integer id;
	@ApiModelProperty(value="商品ID")
	private Long sellerGoodsId;
	@ApiModelProperty(value="订单ID")
	private Long orderId;
	@ApiModelProperty(value="会员ID")
	private Integer dealerId;
	@ApiModelProperty(value="购买日期")
	private Timestamp buyTime;
	@ApiModelProperty(value="评价日期")
	private Timestamp commentTime;
	@ApiModelProperty(value="评分")
	private Integer commentScore;
	@ApiModelProperty(value="评价内容")
	private String commentNotes;
	@ApiModelProperty(value="优点")
	private String advantage;
	@ApiModelProperty(value="不足")
	private String fault;
	@ApiModelProperty(value="使用心得")
	private String useExperience;
	@ApiModelProperty(value="有用数量")
	private Integer usefullAmount;
	@ApiModelProperty(value="无用数量")
	private Integer uselessAmount;
	@ApiModelProperty(value="是否审核(0/1)")
	private String isAudit;
	@ApiModelProperty(value="审核人")
	private Integer auditUserId;
	@ApiModelProperty(value="审核时间")
	private String auditTime;
	@ApiModelProperty(value="状态(1/2)")
	private String status;
	@ApiModelProperty(value="包装评分")
	private Integer packageScore;  
	@ApiModelProperty(value="物流发货速度评分")
	private Integer logisticsScore;  
	@ApiModelProperty(value="服务满意度")
	private Integer serviceScore;  
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getSellerGoodsId() {
		return sellerGoodsId;
	}
	public void setSellerGoodsId(Long sellerGoodsId) {
		this.sellerGoodsId = sellerGoodsId;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Integer getDealerId() {
		return dealerId;
	}
	public void setDealerId(Integer dealerId) {
		this.dealerId = dealerId;
	}
	public Timestamp getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(Timestamp buyTime) {
		this.buyTime = buyTime;
	}
	public Timestamp getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(Timestamp commentTime) {
		this.commentTime = commentTime;
	}
	public Integer getCommentScore() {
		return commentScore;
	}
	public void setCommentScore(Integer commentScore) {
		this.commentScore = commentScore;
	}
	public String getCommentNotes() {
		return commentNotes;
	}
	public void setCommentNotes(String commentNotes) {
		this.commentNotes = commentNotes;
	}
	public String getAdvantage() {
		return advantage;
	}
	public void setAdvantage(String advantage) {
		this.advantage = advantage;
	}
	public String getFault() {
		return fault;
	}
	public void setFault(String fault) {
		this.fault = fault;
	}
	public String getUseExperience() {
		return useExperience;
	}
	public void setUseExperience(String useExperience) {
		this.useExperience = useExperience;
	}
	public Integer getUsefullAmount() {
		return usefullAmount;
	}
	public void setUsefullAmount(Integer usefullAmount) {
		this.usefullAmount = usefullAmount;
	}
	public Integer getUselessAmount() {
		return uselessAmount;
	}
	public void setUselessAmount(Integer uselessAmount) {
		this.uselessAmount = uselessAmount;
	}
	public String getIsAudit() {
		return isAudit;
	}
	public void setIsAudit(String isAudit) {
		this.isAudit = isAudit;
	}
	public Integer getAuditUserId() {
		return auditUserId;
	}
	public void setAuditUserId(Integer auditUserId) {
		this.auditUserId = auditUserId;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getPackageScore() {
		return packageScore;
	}
	public void setPackageScore(Integer packageScore) {
		this.packageScore = packageScore;
	}
	public Integer getLogisticsScore() {
		return logisticsScore;
	}
	public void setLogisticsScore(Integer logisticsScore) {
		this.logisticsScore = logisticsScore;
	}
	public Integer getServiceScore() {
		return serviceScore;
	}
	public void setServiceScore(Integer serviceScore) {
		this.serviceScore = serviceScore;
	}
	
}
