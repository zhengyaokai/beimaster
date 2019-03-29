package bmatser.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import bmatser.util.ResponseMsg;
import bmatser.util.SMS;

@Controller
@RequestMapping("sms")
@Api(value="sms", description="短信")
public class SmsController {

	/**
	 * 发货短信提醒
	 * @param mobile 接收人手机号码
	 * @param logisticsName 快递公司名称
	 * @param logisticsCode 快递单号
	 * @return
	 * 2016-3-16
	 * String
	 */
	@RequestMapping(value="sendDeliverWarn", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "发货短信提醒", 
	notes = "发货短信提醒")
	public ResponseMsg sendDeliverWarn(
			@ApiParam(required=false,name="mobile",value="手机号") String mobile, 
			@ApiParam(required=false,name="logisticsName",value="物流公司") String logisticsName,
			@ApiParam(required=false,name="logisticsCode",value="物流号") String logisticsCode){
		ResponseMsg msg = new ResponseMsg();
		try {
			logisticsName = URLDecoder.decode(logisticsName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String result = SMS.sendDeliverWarn(mobile, logisticsName, logisticsCode);
		JSONObject obj = JSONObject.parseObject(result);
		msg.setData(obj);
		return msg;
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
	@RequestMapping(value="sendCheckedWarn", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "注册审核通过短信提醒", 
	notes = "注册审核通过短信提醒")
	public ResponseMsg sendCheckedWarn(
			@ApiParam(required=false,name="mobile",value="手机号") String mobile,
			@ApiParam(required=false,name="loginName",value="登录名")String loginName,
			@ApiParam(required=false,name="amount",value="金额")String amount){
		ResponseMsg msg = new ResponseMsg();
		String result = SMS.sendCheckedWarn(mobile, loginName, amount);
		JSONObject obj = JSONObject.parseObject(result);
		msg.setData(obj);
		return msg;
	}
	
	/**
	 * 注册验证码
	 * @param mobile 接收人手机号码
	 * @param code 验证码 
	 * @return
	 * 2016-3-16
	 * String
	 */
	@RequestMapping(value="sendRegisterCode", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "注册验证码", 
	notes = "注册验证码")
	public ResponseMsg sendRegisterCode(
			@ApiParam(required=false,name="mobile",value="手机号") String mobile,
			@ApiParam(required=false,name="code",value="验证码")String code){
		ResponseMsg msg = new ResponseMsg();
		String result = SMS.sendRegisterCode(mobile, code);
		JSONObject obj = JSONObject.parseObject(result);
		msg.setData(obj);
		return msg;
	}
	
	/**
	 * 重置密码验证码
	 * @param mobile 接收人手机号码
	 * @param code 验证码 
	 * @return
	 * 2016-3-16
	 * String
	 */
	@RequestMapping(value="sendResetPwdCode", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "重置密码验证码", 
	notes = "重置密码验证码")
	public ResponseMsg sendResetPwdCode(
			@ApiParam(required=false,name="mobile",value="手机号") String mobile,
			@ApiParam(required=false,name="mobile",value="验证码")String code){
		ResponseMsg msg = new ResponseMsg();
		String result = SMS.sendResetPwdCode(mobile, code);
		JSONObject obj = JSONObject.parseObject(result);
		msg.setData(obj);
		return msg;
	}
	
	/**
	 * 重置密码通知
	 * @param mobile 接收人手机号码
	 * @param password 密码
	 * @return
	 * 2016-3-16
	 * String
	 */
	@RequestMapping(value="sendResetPwdWarn", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "重置密码通知", 
	notes = "重置密码通知")
	public ResponseMsg sendResetPwdWarn(
			@ApiParam(required=false,name="mobile",value="手机号") String mobile,
			@ApiParam(required=false,name="mobile",value="密码") String password){
		ResponseMsg msg = new ResponseMsg();
		String result = SMS.sendResetPwdWarn(mobile, password);
		JSONObject obj = JSONObject.parseObject(result);
		msg.setData(obj);
		return msg;
	}
}
