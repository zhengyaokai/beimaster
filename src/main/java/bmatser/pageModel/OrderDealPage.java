package bmatser.pageModel;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="订单列表参数")
public class OrderDealPage {
	@ApiModelProperty(value="订单ID")
	private String orderId;

	private String queryType;
	@ApiModelProperty(value="下单开始时间")
	private String startTime;
	@ApiModelProperty(value="下单结束时间")
	private String endTime;
	@ApiModelProperty(value="物流单号")
	private String logisticsCode;
	@ApiModelProperty(value="会员ID")
	private String dealerId;
	@ApiModelProperty(value="行数")
	private int rows;
	@ApiModelProperty(value="订单状态【1:待付款 2：待发货 3：已发货（待收货） 4：已收货（待结算） 5：对账中（已结算） 6：已完成 7：退货中 8：已取消 9:待确认付款】")
	private int status;
	@ApiModelProperty(value="页数")
	private int page;
	
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
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
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDealerId() {
		return dealerId;
	}
	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}

	public void setPageParam(int page){
		if(this.rows==0){
			this.rows = 8;
		}
		this.page = page*rows;
	}
	
	
}
