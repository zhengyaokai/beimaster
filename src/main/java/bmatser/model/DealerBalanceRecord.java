package bmatser.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
@ApiModel(value="余额记录",description="余额记录")
public class DealerBalanceRecord {
	private Integer id;
	@ApiModelProperty(value="dealerId")
	private Integer dealerId;
	@ApiModelProperty(value="余额")
	private BigDecimal balance;
	@ApiModelProperty(value="关联的付款单id或订单id")
	private String relatedId;
	@ApiModelProperty(value="时间")
	private Date createTime;
	@ApiModelProperty(value="状态 1 有效 2删除")
	private String status;
	@ApiModelProperty(value="1:余额转入  2:消费余额  3:余额提现转出")
	private String type;
	@ApiModelProperty(value="提现转出申请id")
	private Long balanceApplyId;
	@ApiModelProperty(value="状态  1 待主管审核  2待财务审核  3 需重新提交 4 已完成  5 关闭")
	private String balanceApplyStatus;
	@ApiModelProperty(value="余额转入  消费余额  余额提现转出")
	private String name;
	@ApiModelProperty(value="副标题")
	private String subtitle;
	@ApiModelProperty(value="前台显示状态 处理中 已关闭 处理成功")
	private String showStatus;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDealerId() {
		return dealerId;
	}
	public void setDealerId(Integer dealerId) {
		this.dealerId = dealerId;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getRelatedId() {
		return relatedId;
	}
	public void setRelatedId(String relatedId) {
		this.relatedId = relatedId;
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
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getBalanceApplyId() {
		return balanceApplyId;
	}
	public void setBalanceApplyId(Long balanceApplyId) {
		this.balanceApplyId = balanceApplyId;
	}
	public String getBalanceApplyStatus() {
		return balanceApplyStatus;
	}
	public void setBalanceApplyStatus(String balanceApplyStatus) {
		this.balanceApplyStatus = balanceApplyStatus;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public String getShowStatus() {
		return showStatus;
	}
	public void setShowStatus(String showStatus) {
		this.showStatus = showStatus;
	}

}
