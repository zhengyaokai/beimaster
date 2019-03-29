package bmatser.model;

import java.sql.Timestamp;
import java.util.Date;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
@ApiModel(value="工币",description="工币")
public class DealerBean {
	  private Integer id;
	  @ApiModelProperty(value="dealerId")
      private Integer dealerId;
	  @ApiModelProperty(value="工币数（获取为正数，使用为负数）")
      private Integer bean;
	  @ApiModelProperty(value="创建时间")
      private Timestamp createTime;
	  @ApiModelProperty(value="修改时间")
      private Timestamp modifyTime;
	  @ApiModelProperty(value="有效截止时间")
      private Date endDate;
	  @ApiModelProperty(value="备注")
      private String remark;
	  @ApiModelProperty(value="关联表ID")
      private Integer relatedId;
	  @ApiModelProperty(value="orderId")
      private  String orderId;
	  @ApiModelProperty(value="记录类型（1：下单收入  2：使用支出 3：退货返还）")
      private int type;
	  @ApiModelProperty(value="状态（1：有效 2：扣除  3：冻结  4：删除）")
      private String status;
	  @ApiModelProperty(value="确认收货时间")
      private Timestamp consignTime;
	  @ApiModelProperty(value="订单状态【1:待付款   2：待发货  3：已发货（待收货）  4：已收货（待结算）  5：对账中（已结算）  6：已完成  7：退货中  8：已取消  9:待确认付款】")
      private String orderStatus;
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
	public Integer getBean() {
		return bean;
	}
	public void setBean(Integer bean) {
		this.bean = bean;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getRelatedId() {
		return relatedId;
	}
	public void setRelatedId(Integer relatedId) {
		this.relatedId = relatedId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Timestamp getConsignTime() {
		return consignTime;
	}
	public void setConsignTime(Timestamp consignTime) {
		this.consignTime = consignTime;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}



}
