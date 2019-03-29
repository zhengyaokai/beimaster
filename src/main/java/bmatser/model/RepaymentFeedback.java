package bmatser.model;
/**
 * 还款处理结果反馈 参数实体
 */
import java.util.Date;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
@ApiModel(value="还款处理结果反馈")
public class RepaymentFeedback {
	@ApiModelProperty(value="客户编号")
	private String customerCode;//客户编号
	@ApiModelProperty(value="客户名称")
	private String customerName;//客户名称
	@ApiModelProperty(value="出账单号")
	private String accountNo;//出账单号
	@ApiModelProperty(value="还款单号（金诺系统产生的唯一编码）")
	private String repaymentNo;//还款单号（金诺系统产生的唯一编码）
	@ApiModelProperty(value="-1:表示还款失败，0：表示还款成功")
	private int status;//-1:表示还款失败，0：表示还款成功
	@ApiModelProperty(value="如果还款失败，财务人员填写失败的原因，供客户参考")
	private String note;//如果还款失败，财务人员填写失败的原因，供客户参考；
	@ApiModelProperty(value="系统更新时间")
	private String updateTime;//系统更新时间
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getRepaymentNo() {
		return repaymentNo;
	}
	public void setRepaymentNo(String repaymentNo) {
		this.repaymentNo = repaymentNo;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	

}
