package bmatser.model;

import java.math.BigDecimal;
import java.util.Date;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 贷通支付 操作记录
 * @author 027-20160318
 *
 */
@ApiModel(value="贷通支付 操作记录")
public class JtlHandleRecord {
	@ApiModelProperty(value="客户编码")
	private Integer dealerId;
	@ApiModelProperty(value="申请单号")
	private String applyNo;
	@ApiModelProperty(value="订单号")
	private Integer orderId;
	@ApiModelProperty(value="还款金额")
	private BigDecimal repaymentAmount;
	@ApiModelProperty(value="操作时间")
	private Date createTime;
	@ApiModelProperty(value="备注")
	private String remark;
	public Integer getDealerId() {
		return dealerId;
	}
	public void setDealerId(Integer dealerId) {
		this.dealerId = dealerId;
	}
	public String getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public BigDecimal getRepaymentAmount() {
		return repaymentAmount;
	}
	public void setRepaymentAmount(BigDecimal repaymentAmount) {
		this.repaymentAmount = repaymentAmount;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
