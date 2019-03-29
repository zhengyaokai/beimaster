package bmatser.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import bmatser.pageModel.PageMode;
import bmatser.pageModel.PageMode.Channel;
import bmatser.pageModel.util.RegisterPageUtil;
import bmatser.service.GeneralI;
import bmatser.service.GoodsI;
import bmatser.service.PersonalCenterI;
import bmatser.service.RegisterI;
import bmatser.service.SecuritySettingsI;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;

@Controller
@RequestMapping("personalCenter")
@Api(value="personalCenter", description="APP商城个人中心")
public class PersonalCenterController {
	@Autowired
	private PersonalCenterI personalCenterService;
	@Autowired
	private SecuritySettingsI securitySettingsI;
	@Autowired
	private GeneralI generalService;
	@Autowired
	private RegisterI registerService;
	
	/**
	 * saas查询用户二维码
	 * @param request
	 * @return
	 */
//	@RequestMapping(value="/getQrcode", method=RequestMethod.GET)
//	@ResponseBody
//	@ApiOperation(value = "saas查询用户二维码")
//	public ResponseMsg getQrcode(HttpServletRequest request) {
//		ResponseMsg msg = new ResponseMsg();
//		PageMode pageMode = new PageMode(request);
//		try {
//			msg.setData(personalCenterService.getQrcode(pageMode.getDealerId(Channel.SAAS)));
//			msg.setCode(0);
//		} catch (Exception e) {
//			msg.setError(e);
//		}
//		return msg;
//	}
	
	/**
	 * 查询用户邀请记录
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/getInvitedRecord/{page}",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询用户邀请记录")
	public ResponseMsg getInvitedRecord(HttpServletRequest request,@PathVariable int page){
		int rows = 10;
		ResponseMsg msg = new ResponseMsg();
		PageMode pageMode = new PageMode(request);
		String startTime  = pageMode.getStringValue("startTime");//关注开始时间
		String endTime  = pageMode.getStringValue("endTime");//关注结束时间
		if(page<1){
			return msg.setError("程序参数错误");
		}else{
			page = page - 1;
		}
		try {
			msg.setData(personalCenterService.getInvitedRecord(pageMode.getDealerId(Channel.SAAS),page*rows,rows,startTime,endTime));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 账号设置 绑定账号 保存银行账号
	 */
	@RequestMapping(value="/saveBankAccount", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = " 账号设置 绑定账号 保存银行账号")
	public ResponseMsg saveBankAccount(HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		PageMode pageMode = new PageMode(request);
		String accountName = pageMode.getStringValue("accountName");//开户名
		String bankName = pageMode.getStringValue("bankName");//开户行
		String card = pageMode.getStringValue("card");//卡号
		try {
			String dealerName = personalCenterService.getDealerName(pageMode.getDealerId(Channel.SAAS));
			if(!accountName.equals(dealerName)){
				return msg.setError("开户名错误!");
			}
			personalCenterService.saveBankAccount(pageMode.getDealerId(Channel.SAAS),accountName,bankName,card);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 账号设置 绑定账号 查询
	 */
	@RequestMapping(value="/selectBankAccount", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "账号设置 绑定账号 查询")
	public ResponseMsg selectBankAccount(HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		PageMode pageMode = new PageMode(request);
		try {
			msg.setData(personalCenterService.selectBankAccount(pageMode.getDealerId(Channel.SAAS)));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * saas 修改绑定银行账号 查询手机号码
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getMobile", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "saas 修改绑定银行账号 查询手机号码")
	public ResponseMsg getMobile(HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		PageMode pageMode = new PageMode(request);
		try {
			
			msg.setData(securitySettingsI.findMobile(Integer.valueOf(pageMode.getDealerId(Channel.SAAS))));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * saas 修改绑定银行账号 点击下一步 提交
	 * 
	 */
	@RequestMapping(value="/accountChangeValidate", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "saas 修改绑定银行账号 点击下一步 提交")
	public ResponseMsg  accountChangeValidate(RegisterPageUtil registerPage,HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		PageMode pageMode = new PageMode(request);
		String id = pageMode.getStringValue("id");//会员绑定银行卡表 dealer_bank_card 的 id
		String oldCard = pageMode.getStringValue("card");//卡号
		String mobile = "";
		try {
			mobile = generalService.findMobile(Integer.parseInt(pageMode.getDealerId(Channel.SAAS)));
			registerPage.setUserName(mobile);
			registerService.modifyMobileSms(registerPage,request);
			String card = personalCenterService.getCard(id);
			if(!oldCard.equals(card)){
				return msg.setError("原银行账号输入错误!");
			}
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * saas 修改绑定银行账号 第二步 修改绑定银行账户
	 */
	@RequestMapping(value="/modifyBankCard", method=RequestMethod.GET)
	@ResponseBody
	                     
	public ResponseMsg  modifyBankCard(HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		PageMode pageMode = new PageMode(request);
		String id = pageMode.getStringValue("id");//会员绑定银行卡表 dealer_bank_card 的 id
		String accountName = pageMode.getStringValue("accountName");//开户名
		String bankName = pageMode.getStringValue("bankName");//开户行
		String card = pageMode.getStringValue("card");//卡号
		try {
			String dealerName = personalCenterService.getDealerName(pageMode.getDealerId(Channel.SAAS));
			if(!accountName.equals(dealerName)){
				return msg.setError("开户名错误!");
			}
			personalCenterService.modifyBankCard(id,bankName,card,pageMode.getDealerId(Channel.SAAS));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
}
