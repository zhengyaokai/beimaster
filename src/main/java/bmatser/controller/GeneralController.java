package bmatser.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import bmatser.dao.RegisterMapper;
import bmatser.pageModel.PageMode;
import bmatser.pageModel.PageMode.Channel;
import bmatser.service.GeneralI;
import bmatser.util.DESCoder;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;

@Controller
public class GeneralController {
	
	@Autowired
	private GeneralI generalService;
	@Autowired
	private RegisterMapper registerDao;
	/**
	 * 商城发送验证短信
	 * @param mobile
	 * @param session
	 * @return
	 */
	@RequestMapping(value="sms/register/verify", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城发送验证短信")
	public ResponseMsg sendSMSVerifyCode(@ApiParam(value="图片验证码") @RequestParam String vcode,
			@ApiParam(value="手机号码") @RequestParam String mobile,HttpServletRequest request, HttpSession session){
		ResponseMsg msg = new ResponseMsg();
		/*String vcode = request.getParameter("vcode");
		String mobile = request.getParameter("mobile");*/
		try {
			generalService.sendSMSVerifyCode(mobile,vcode,request, session,"4");
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	/**
	 * 商城app注册发送验证短信
	 * @param mobile
	 * @param session
	 * @return
	 */
	@RequestMapping(value="sms/appRegister/verify", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城app注册发送验证短信")
	public ResponseMsg sendSMSVerifyCode1(@ApiParam(value="手机号码") @RequestParam String mobile,HttpServletRequest request, HttpSession session){
		ResponseMsg msg = new ResponseMsg();
		/*String mobile = request.getParameter("mobile");*/
		try {
			generalService.sendSMSVerifyCode1(mobile,request, session,"4");
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	
	/**
	 * 发送验证短信
	 * @param mobile
	 * @param session
	 * @return
	 */
	@RequestMapping(value="sms/saas/verify", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "saas注册发送验证短信")
	public ResponseMsg sendSaasSMSVerifyCode(@ApiParam(value="图片验证码") @RequestParam String vcode,
			@ApiParam(value="手机号码") @RequestParam String mobile,HttpServletRequest request, HttpSession session){
		ResponseMsg msg = new ResponseMsg();
		/*String vcode = request.getParameter("vcode");
		String mobile = request.getParameter("mobile");*/
		try {
			generalService.sendSMSVerifyCode(mobile,vcode,request, session,"2");
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	/**
	 * 发送验证短信
	 * @param mobile
	 * @param session
	 * @return
	 */
	@RequestMapping(value="sms/mall/getPassword", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城忘记密码发送验证短信")
	public ResponseMsg sendSMSVerifyCodeGetPassword(@ApiParam(value="图片验证码") @RequestParam String vcode,
			@ApiParam(value="手机号码") @RequestParam String mobile,HttpServletRequest request, HttpSession session){
		ResponseMsg msg = new ResponseMsg();
		/*String vcode = request.getParameter("vcode");
		String mobile = request.getParameter("mobile");*/
		try {
//			generalService.sendSMSVerifyCode(mobile,vcode,request, session);
			generalService.sendSMSGetPassword(mobile,"4",vcode,request, session);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * app商城 忘记密码 发送验证短信
	 * @param mobile
	 * @param session
	 * @return
	 */
	@RequestMapping(value="sms/appMall/getPassword", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "app商城 忘记密码 发送验证短信")
	public ResponseMsg sendSMSVerifyCodeGetPassword1(@ApiParam(value="手机号码") @RequestParam String mobile,HttpServletRequest request, HttpSession session){
		ResponseMsg msg = new ResponseMsg();
		/*String mobile = request.getParameter("mobile");*/
		try {
//			generalService.sendSMSVerifyCode(mobile,vcode,request, session);
			generalService.sendSMSGetPassword1(mobile,"4",request, session);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	/**
	 * 找回密码
	 * @param mobile
	 * @param session
	 * @return
	 */
	@RequestMapping(value="sms/getPassword", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "saas 忘记密码 发送验证短信")
	public ResponseMsg sendSMSGetPassword(@ApiParam(value="手机号码") @RequestParam String mobile,HttpServletRequest request, HttpSession session){
		ResponseMsg msg = new ResponseMsg();
		/*String mobile = request.getParameter("mobile");*/
		try {
			generalService.sendSMSGetPassword(mobile,"2",null,request, session);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	/**
	 * 发送验证短信
	 * @param mobile
	 * @param session
	 * @return
	 */
	@RequestMapping(value="sms/register", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "saas app 注册 发送验证短信")
	public ResponseMsg sendSMSCode(@ApiParam(value="手机号码") @RequestParam String mobile,HttpServletRequest request, HttpSession session){
		ResponseMsg msg = new ResponseMsg();
		/*String mobile = request.getParameter("mobile");*/
		try {
			generalService.sendSMSVerifyCode(mobile,request, session);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	/**
	 * 发送验证邮件
	 * @param mobile
	 * @param session
	 * @return
	 */
	@RequestMapping(value="email/register/verify", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城  发送验证邮件")
	public ResponseMsg sendEmailVerifyCode(@ApiParam(value="邮箱") @RequestParam String email,
			@ApiParam(value="图片验证码") @RequestParam String vcode,HttpServletRequest request, HttpSession session){
		ResponseMsg msg = new ResponseMsg();
		/*String email = request.getParameter("email");
		String vcode = request.getParameter("vcode");*/
		try {
			generalService.sendCode2Email(email,vcode,request);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * 商城 修改手机号 验证身份 发送验证短信
	 * @param mobile
	 * @param session
	 * @return
	 */
	@RequestMapping(value="sms/modifyMobile", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城 修改手机号 验证身份 发送验证短信")
	public ResponseMsg modifyMobile(@ApiParam(value="新手机号") @RequestParam String newMobile,
			@ApiParam(value="图片验证码") @RequestParam String vcode,HttpServletRequest request, HttpSession session){
		ResponseMsg msg = new ResponseMsg();
		/*String vcode = request.getParameter("vcode");
		String newMobile = request.getParameter("newMobile");*/
		String mobile = "";
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return msg.setError("请先登录");
		}
		try {
			if(StringUtils.isNotBlank(newMobile)){
				DESCoder.instanceMobile();
				String newMobileSecret = DESCoder.encoder(newMobile).trim();//对手机号码加密
				int count = registerDao.existMallMobile(newMobileSecret);
				if(count > 0){
					throw new Exception("该手机已经注册!");
				}
				mobile = newMobile;
			}else{
				mobile = generalService.findMobile(Integer.parseInt(loginInfo.getDealerId()));
			}
			generalService.sendMobileSMSVerifyCode(mobile,vcode,request, session,"4");
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * saas 修改绑定银行卡号 发送验证短信
	 * @param mobile
	 * @param session
	 * @return
	 */
	@RequestMapping(value="sms/modifyCard", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "saas 修改绑定银行卡号 发送验证短信")
	public ResponseMsg modifyCard(@ApiParam(value="图片验证码") @RequestParam String vcode,HttpServletRequest request, HttpSession session){
		ResponseMsg msg = new ResponseMsg();
		PageMode pageMode = new PageMode(request);
		/*String vcode = pageMode.getStringValue("vcode");*/
		String mobile = "";
		try {
			mobile = generalService.findMobile(Integer.valueOf(pageMode.getDealerId(Channel.SAAS)));
			generalService.sendCardSMSVerifyCode(mobile,vcode,request, session,"2");
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
}
