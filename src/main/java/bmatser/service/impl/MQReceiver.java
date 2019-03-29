package bmatser.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.igfay.jfig.JFig;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import bmatser.service.StaffI;
import bmatser.util.Constants;
import bmatser.util.Email;
import bmatser.util.MQMessage;
import bmatser.util.mqBean.OrderInfo;

/**
 * 消息接收实现类
 * @author felx
 * 2016-3-18
 */
public class MQReceiver implements MessageListener{
	
	@Autowired
	private StaffI staffDao;
	private static String address = JFig.getInstance().getValue("email_options", "ORDER_PAY_ADDRESS", "cht@gdbmromall.com");

	@Override
	public void onMessage(Message message) {
		if(message instanceof TextMessage){
			TextMessage textMessage = (TextMessage)message;
			try {
				MQMessage mqMessage = JSON.parseObject(textMessage.getText(), MQMessage.class);
				int type = mqMessage.getType();
				/** 订单支付预警 */
				if(Constants.ORDER_PAY == type){
					OrderInfo orderInfo = mqMessage.getOrderInfo();
					Long orderId = orderInfo.getOrderId();
					String dealerName = orderInfo.getDealerName();
					String dateTime = orderInfo.getDateTime();
					Double amount = orderInfo.getAmount();
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("orderId", String.valueOf(orderId));
					data.put("dealerName", dealerName);
					data.put("amount", String.valueOf(amount));
					data.put("dateTime", dateTime);
					Integer serviceId = orderInfo.getServiceId();
					if(serviceId != null && serviceId > 0){
						String email = staffDao.findEmailById(serviceId);
						if(StringUtils.isNoneBlank(email)){
							address = email;
						} else{
							address = "cht@gdbmromall.com";
						}
					} else{
						address = "cht@gdbmromall.com";
					}
					Email.sendHtmlMail(data, address, "已支付--"+dealerName, "orderPayWarn.ftl");
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}
