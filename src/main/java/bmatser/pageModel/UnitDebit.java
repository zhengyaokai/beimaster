package bmatser.pageModel;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;


@ApiModel(value="单位结算卡",description="单位结算卡")
public class UnitDebit {

	@ApiModelProperty(value="结算卡卡号")
	private String debitCard;
	@ApiModelProperty(value="账单金额")
	private String amount;
	@ApiModelProperty(value="收款日期")
	private String payTime;
	@ApiModelProperty(value="款项用途")
	private String useType;
	@ApiModelProperty(value="银行流水号")
	private String serialNo;
	public String getDebitCard() {
		return debitCard;
	}
	public void setDebitCard(String debitCard) {
		this.debitCard = debitCard;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getUseType() {
		return useType;
	}
	public void setUseType(String useType) {
		this.useType = useType;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	

}
