package bmatser.model;

import java.math.BigDecimal;
import java.util.Date;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 授权信息查询返回参数
 * @author 027-20160318
 *
 */
@ApiModel(value="授权信息")
public class AuthorizationInfo {
	@ApiModelProperty(value="状态码：-1:失败,1:成功")
	private char status;//状态码：-1:失败,1:成功
	@ApiModelProperty(value="授信单号")
	private String creditNo;//授信单号
	@ApiModelProperty(value="客户编码")
	private String customerCode;//客户编码
	@ApiModelProperty(value="客户名称")
	private String customerName;//客户名称
	@ApiModelProperty(value="授信状态")
	private String creditStatus;//授信状态
	//private String creditContractNo;//授信合同号
	@ApiModelProperty(value="授信额度")
	private BigDecimal creditLimit;//授信额度
	@ApiModelProperty(value="利率类型：1：日化；2：月化；3：年化")
	private long rateType;//利率类型：1：日化；2：月化；3：年化
	@ApiModelProperty(value="授信利率，例1%")
	private long rate;//授信利率，例1%
	@ApiModelProperty(value="已贷款金额，累计所有的贷款金额")
	private BigDecimal amount;//已贷款金额，累计所有的贷款金额
	@ApiModelProperty(value="已用额度，客户未还款金额")
	private BigDecimal repaymentAmount;//已用额度，客户未还款金额
	@ApiModelProperty(value="可用额度=授信金额-已用额度")
	private BigDecimal availableAmount;//可用额度=授信金额-已用额度
	@ApiModelProperty(value="当前未还逾期总金额")
	private BigDecimal overdueAmount;//当前未还逾期总金额
	@ApiModelProperty(value="授信开始时间")
	private Date startDate;//授信开始时间
	@ApiModelProperty(value="授信结束时间")
	private Date endDate;//授信结束时间
	@ApiModelProperty(value="服务器时间")
	private Date responseTime;//服务器时间
	@ApiModelProperty(value="失败信息")
	private String msg;//失败信息
	@ApiModelProperty(value="还款方式：1：一次性还本付息")
	private String repaymentType;//还款方式：1：一次性还本付息
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public String getCreditNo() {
		return creditNo;
	}
	public void setCreditNo(String creditNo) {
		this.creditNo = creditNo;
	}
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
	public String getCreditStatus() {
		return creditStatus;
	}
	public void setCreditStatus(String creditStatus) {
		this.creditStatus = creditStatus;
	}
	public BigDecimal getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}
	public long getRateType() {
		return rateType;
	}
	public void setRateType(long rateType) {
		this.rateType = rateType;
	}
	public long getRate() {
		return rate;
	}
	public void setRate(long rate) {
		this.rate = rate;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getRepaymentAmount() {
		return repaymentAmount;
	}
	public void setRepaymentAmount(BigDecimal repaymentAmount) {
		this.repaymentAmount = repaymentAmount;
	}
	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}
	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}
	public BigDecimal getOverdueAmount() {
		return overdueAmount;
	}
	public void setOverdueAmount(BigDecimal overdueAmount) {
		this.overdueAmount = overdueAmount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getRepaymentType() {
		return repaymentType;
	}
	public void setRepaymentType(String repaymentType) {
		this.repaymentType = repaymentType;
	}
	

}
