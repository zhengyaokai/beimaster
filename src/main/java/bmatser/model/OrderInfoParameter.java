package bmatser.model;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="订单查询参数")
public class OrderInfoParameter {
	@ApiModelProperty(value="订单ID")
	private String orderId;
	@ApiModelProperty(value="下单时间（开始）")
	private String startTime;
	@ApiModelProperty(value="下单时间（截止）")
	private String endTime;
	@ApiModelProperty(value="物流单号")	
	private String logisticsCode;
	@ApiModelProperty(value="行数")
	private Integer rows;
	@ApiModelProperty(value="订单状态【1:待付款 2：待发货 3：已发货（待收货） 4：已收货（待结算） 5：对账中（已结算） 6：已完成 7：退货中 8：已取消 9:待确认付款,10:删除】")
	private Integer status;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getLogisticsCode() {
		return logisticsCode;
	}
	public void setLogisticsCode(String logisticsCode) {
		this.logisticsCode = logisticsCode;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
