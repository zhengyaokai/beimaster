package bmatser.util;

import java.io.Serializable;

import bmatser.util.mqBean.OrderInfo;

public class MQMessage implements Serializable{
	
	private int type;
	
	private OrderInfo orderInfo;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public OrderInfo getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}
	
}
