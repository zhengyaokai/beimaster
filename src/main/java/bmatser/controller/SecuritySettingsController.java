package bmatser.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import bmatser.pageModel.util.RegisterPageUtil;
import bmatser.service.GeneralI;
import bmatser.service.RegisterI;
import bmatser.service.SecuritySettingsI;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;

/**
 * 
 * @author felx
 *
 */
@Controller
@RequestMapping("security")
@Api(value="security", description="商城设置")
public class SecuritySettingsController {
	@Autowired
	private SecuritySettingsI securitySettingsI;
	@Autowired
	private GeneralI generalService;
	@Autowired
	private RegisterI registerService;
	/**
	 * 商城安全设置 修改手机 验证身份 查询 手机号
	 */
	@RequestMapping(value="/findMobile", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城安全设置 修改手机 验证身份 查询 手机号", 
	notes = "商城安全设置 修改手机 验证身份 查询 手机号")
	public ResponseMsg findMobile(HttpServletRequest request) {
		Integer rows = 8;
		ResponseMsg msg = new ResponseMsg();
		
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			msg.setMsg("请先登录");
			msg.setCode(-1);
			return msg;
		}
		try {
			
			msg.setData(securitySettingsI.findMobile(Integer.valueOf(loginInfo.getDealerId())));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}


	/**
	 * 商城安全设置 修改手机 验证身份 提交
	 */
	@RequestMapping(value="/authenticationSub", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城安全设置 修改手机 验证身份 提交", 
	notes = "商城安全设置 修改手机 验证身份 提交")
	public ResponseMsg  authenticationSubmit(@ModelAttribute RegisterPageUtil registerPage,HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		String mobile = "";
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			msg.setMsg("请先登录");
			msg.setCode(-1);
			return msg;
		}
		try {
			mobile = generalService.findMobile(Integer.parseInt(loginInfo.getDealerId()));
			registerPage.setUserName(mobile);
			registerService.modifyMobileSms(registerPage,request);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * 商城安全设置 d第二步 修改手机号
	 */
	@RequestMapping(value="/modifyMobile", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城安全设置 d第二步 修改手机号", 
	notes = "商城安全设置 d第二步 修改手机号")
	public ResponseMsg  modifyMobile(HttpServletRequest request,
			@ApiParam(required=false,name="newMobile",value="新手机")  String newMobile,
			@ApiParam(required=false,name="verifyCode",value="验证码")  String verifyCode
			) {
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			msg.setMsg("请先登录");
			msg.setCode(-1);
			return msg;
		}
		try {
			securitySettingsI.modifyMobile(verifyCode,newMobile,loginInfo.getDealerId(),request);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
}
