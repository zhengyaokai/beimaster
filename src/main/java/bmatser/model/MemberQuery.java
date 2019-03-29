package bmatser.model;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
/**
 * 会员咨询
 * @author felx
 * @date 2016.02.18 
 */
@ApiModel(value="会员咨询")
public class MemberQuery {
    private Integer id;
    @ApiModelProperty(value="商品ID")
    private Long sellerGoodsId;
    @ApiModelProperty(value="会员ID")
    private Integer dealerId;
    @ApiModelProperty(value="咨询内容")
    private String queryContent;
    @ApiModelProperty(value="咨询时间")
    private String queryTime;
    @ApiModelProperty(value="是否答复(0/1)")
    private String isReply;
    @ApiModelProperty(value="回复内容")
    private String replyContent;
    @ApiModelProperty(value="回复时间")
    private String replyTime;
    @ApiModelProperty(value="回复人ID")
    private Integer replyUserId;
    @ApiModelProperty(value="状态(1/2)")
    private Integer status;

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

	public Integer getDealerId() {
		return dealerId;
	}

	public void setDealerId(Integer dealerId) {
		this.dealerId = dealerId;
	}

	public String getQueryContent() {
		return queryContent;
	}

	public void setQueryContent(String queryContent) {
		this.queryContent = queryContent;
	}

	public String getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(String queryTime) {
		this.queryTime = queryTime;
	}

	public String getIsReply() {
		return isReply;
	}

	public void setIsReply(String isReply) {
		this.isReply = isReply;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public String getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}

	public Integer getReplyUserId() {
		return replyUserId;
	}

	public void setReplyUserId(Integer replyUserId) {
		this.replyUserId = replyUserId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
    
}