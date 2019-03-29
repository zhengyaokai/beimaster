package bmatser.service.impl;
//package bmatser.service.impl;
//
//import javax.jms.Destination;
//import javax.jms.JMSException;
//import javax.jms.Message;
//import javax.jms.Session;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.jms.core.MessageCreator;
//import org.springframework.stereotype.Service;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//
//import bmatser.service.MQSenderI;
//
///**
// * MQ消息推送
// * @author felx
// * 2016-2-18
// */
//@Service("mqService")
//public class MQSender implements MQSenderI{
//
//	@Autowired
//	private JmsTemplate pushJmsTemplate;
//	
//	/**
//	 * 通过WEB方式调用
//	 * @param mqMessage
//	 * 2016-2-18
//	 * void
//	 */
//	public void send(final JSONObject json){
//		pushJmsTemplate.send(new MessageCreator() {
//			@Override
//			public Message createMessage(Session session) throws JMSException {
//				return session.createTextMessage(JSON.toJSONString(json));
//			}
//		});
//	}
//	
//	/**
//	 * 通过读取配置文件方式调用
//	 * @param mqMessage
//	 * 2016-2-18
//	 * void
//	 */
//	public static void sendByClassPath(final JSONObject json){
//		 ApplicationContext context = new ClassPathXmlApplicationContext("spring-activemq.xml");
//	     JmsTemplate template = (JmsTemplate) context.getBean("audioJmsTemplate");
//	     Destination destination = (Destination) context.getBean("audioDestination");
//	     template.send(destination, new MessageCreator() {
//	            public Message createMessage(Session session) throws JMSException {
//	                return session
//	                        .createTextMessage(JSON.toJSONString(json));
//	            }
//	      });
//	}
//	
//	/**
//	 * 测试
//	 * @param args
//	 * 2016-2-18
//	 * void
//	 */
//	 public static void main(String[] args) {  
//	  
//	 }  
//}
