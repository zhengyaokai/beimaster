package bmatser.util;

import java.util.UUID;


public class PaymentIDProduce {
	public static Long getPaymentID(){
		int r1=(int)(Math.random()*(10));//产生2个0-9的随机数
		int r2=(int)(Math.random()*(10));
		long now = System.currentTimeMillis();//一个13位的时间戳
		String paymentID =String.valueOf(r1)+String.valueOf(r2)+String.valueOf(now);// 订单ID
		return  Long.valueOf(paymentID);
	} 
	
	
	public static String getUUID(){
		 UUID uuid = UUID.randomUUID();
		 return uuid.toString().replace("-", "");
	}
	
	public static String getOrderContractID(){
		int r1=(int)(Math.random()*(10));//产生2个0-9的随机数
		int r2=(int)(Math.random()*(10));
		long now = System.currentTimeMillis();//一个13位的时间戳
		String paymentID =String.valueOf(r1)+String.valueOf(r2)+String.valueOf(now);// 订单ID
		return paymentID;
	} 
}
