package bmatser.util;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.igfay.jfig.JFig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.context.ContextLoader;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

import bmatser.model.InterfaceLog;
import bmatser.model.SMSMessage;

/**
 * 短信服务接口
 * @author felx
 * @version 1.0
 */
public class SMS {
	
//	private static MongoTemplate mongoTemplate;
	
//	static{
//		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
//		mongoTemplate = context.getBean("mongoTemplate", MongoTemplate.class);
//	}
	
	/**
	 * 阿里云短信发送
	 * @param mobile 短信接收号码。支持单个或多个手机号码，传入号码为11位手机号码，不能加0或+86。群发短信需传入多个号码，以英文逗号分隔，一次调用最多传入200个号码。示例：18600000000,13911111111,13322222222
	 * @param smsParam 短信模板变量，传参规则{"key":"value"}，key的名字须和申请模板中的变量名一致，多个变量之间以逗号隔开。示例：针对模板“验证码${code}，您正在进行${product}身份验证，打死不要告诉别人哦！”，传参时需传入{"code":"1234","product":"alidayu"}
	 * @param smsTempCode 短信模板ID，传入的模板必须是在阿里大鱼“管理中心-短信模板管理”中的可用模板。示例：SMS_585014
	 */
	private static String sendByDY(String mobile, String smsParam, String smsTempCode){	
		SendSmsResponse rsp =null;
		try {
			rsp = SmsUtil.sendSms(mobile, smsParam, smsTempCode);
			System.out.println(rsp.getCode());
		} catch (ClientException e) {
			e.printStackTrace();
		}
		
		//保存发送验证码信息到momgodb
//		try {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			Calendar cal = Calendar.getInstance();
//			cal.setTime(new Date());
//			//+8小时
//			cal.add(Calendar.HOUR_OF_DAY, 8);
//			Timestamp t = Timestamp.valueOf(sdf.format(cal.getTime()));
//			SMSMessage m = new SMSMessage();
//			m.setMobile(mobile);
//			m.setSmsParam(smsParam);
//			m.setSmsTempCode(smsTempCode);
//			m.setUseTime(t);
//			mongoTemplate.save(m);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		return rsp.getCode();
	}
	
	/**
	 * 发货短信提醒
	 * @param mobile 接收人手机号码
	 * @param logisticsName 快递公司名称
	 * @param logisticsCode 快递单号
	 * @return
	 * 2016-3-16
	 * String
	 */
	public static String sendDeliverWarn(String mobile, String logisticsName, String logisticsCode){
		JSONObject json = new JSONObject();
		json.put("logisticsName", logisticsName);
		json.put("logisticsCode", logisticsCode);
		return sendByDY(mobile, json.toJSONString(), "SMS_5710141");
	}
	
	/**
	 * 注册审核通过短信提醒
	 * @param mobile 接收人手机号码
	 * @param loginName 登录名 
	 * @param amount 赠送现金券
	 * @return
	 * 2016-3-16
	 * String
	 */
	public static String sendCheckedWarn(String mobile, String loginName, String amount){
		JSONObject json = new JSONObject();
		json.put("loginName", loginName);
		json.put("amount", amount);
		return sendByDY(mobile, json.toJSONString(), "SMS_5670237");
	}
	
	/**
	 * 注册验证码
	 * @param mobile 接收人手机号码
	 * @param loginName 登录名 
	 * @param amount 赠送现金券
	 * @return
	 * 2016-3-16
	 * String
	 */
	public static String sendRegisterCode(String mobile, String code){
		JSONObject json = new JSONObject();
		json.put("code", code);
//		json.put("product", "工电宝");
		return sendByDY(mobile, json.toJSONString(), "SMS_126425082");
	}
	
	/**
	 * 修改手机发送验证码
	 * @param mobile 接收人手机号码
	 * @param loginName 登录名 
	 * @param amount 赠送现金券
	 * @return
	 * 2016-3-16
	 * String
	 */
	public static String sendModifyMobileCode(String mobile, String code){
		JSONObject json = new JSONObject();
		json.put("code", code);
		json.put("product", "工电宝");
		return sendByDY(mobile, json.toJSONString(), "SMS_14691222");
	}
	
	/**
	 * 修改绑定银行卡发送验证码
	 * @param mobile 接收人手机号码
	 * @param loginName 登录名 
	 * @param amount 赠送现金券
	 * @return
	 * 2016-3-16
	 * String
	 */
	public static String sendModifyCardCode(String mobile, String code){
		JSONObject json = new JSONObject();
		json.put("code", code);
		json.put("product", "工电宝");
		return sendByDY(mobile, json.toJSONString(), "SMS_14746550");
	}
	
	/**
	 * 重置密码验证码
	 * @param mobile
	 * @param code
	 * @return
	 * 2016-3-31
	 * String
	 */
	public static String sendResetPwdCode(String mobile, String code){
		JSONObject json = new JSONObject();
		json.put("code", code);
//		json.put("product", "工电宝");
		return sendByDY(mobile, json.toJSONString(), "SMS_133045030");
	}
	
	/**
	 * 重置密码通知
	 * @param mobile
	 * @param password
	 * @return
	 * 2016-3-31
	 * String
	 */
	public static String sendResetPwdWarn(String mobile, String password){
		JSONObject json = new JSONObject();
		json.put("password", password);
		return sendByDY(mobile, json.toJSONString(), "SMS_7230004");
	}
	
	public static void main(String[] args){
		System.out.print(sendRegisterCode("18914080809","456879"));
//		sendByDY();
	}
}
