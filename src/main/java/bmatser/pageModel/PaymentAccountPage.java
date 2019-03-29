package bmatser.pageModel;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
@ApiModel(value="买家付款账号")
public class PaymentAccountPage {
	@ApiModelProperty(value="id")
	private Integer id;
	@ApiModelProperty(value="零售商ID")
	private Integer buyerId;
	@ApiModelProperty(value="买家付款账号")
	private String  paymentAccount;
	@ApiModelProperty(value="银行ID")
	private Integer bankId;
	@ApiModelProperty(value="开户银行")
	private String  bankDetail;
	@ApiModelProperty(value="是否默认(0：否  1：是)")
	private String  isDefault;
	@ApiModelProperty(value="添加时间")
	private Date    createTime;
	@ApiModelProperty(value="修改时间")
	private Date    modifyTime; 
	@ApiModelProperty(value="状态（1：有效  2：删除）")
	private String  status;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(Integer buyerId) {
		this.buyerId = buyerId;
	}
	public String getPaymentAccount() {
		return paymentAccount;
	}
	public void setPaymentAccount(String paymentAccount) {
		this.paymentAccount = paymentAccount;
	}
	public Integer getBankId() {
		return bankId;
	}
	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}
	public String getBankDetail() {
		return bankDetail;
	}
	public void setBankDetail(String bankDetail) {
		this.bankDetail = bankDetail;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}	
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
