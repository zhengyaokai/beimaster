package bmatser.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import bmatser.pageModel.BuyerConsignAddressPage;
import bmatser.pageModel.PaymentAccountPage;
import bmatser.service.PaymentAccountI;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;



@Controller
@RequestMapping("paymentAccount")
@Api(value="paymentAccount", description="付款账户")
public class PaymentAccountController {
	@Autowired
	private PaymentAccountI paymentAccountService;
	
	/**
	 * 查询付款账号列表和银行列表
	 * @param buyerId
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getPaymentAccountList", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询付款账号列表和银行列表")
	private ResponseMsg getPaymentAccountList(HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return msg.setError("请先登录");
		}
		try {
			List list= paymentAccountService.findPALByBuyerId(Integer.parseInt(loginInfo.getDealerId()));
			msg.setData(list);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 查询银行列表   方法暂时无用
	 * @param buyerId
	 * @return
	 */
	@RequestMapping(value="/selectBankList", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询银行列表   方法暂时无用")
	ResponseMsg selectBankList(HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			msg.setMsg("请先登录");
			msg.setCode(-1);
			return msg;
		}
		try {
			List list= paymentAccountService.findBankList();
			msg.setData(list);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 保存付款账号
	 * @param page
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "保存付款账号")
	public ResponseMsg addPaymentAccount(@ModelAttribute PaymentAccountPage page,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		if(page.getBuyerId()==null){
			LoginInfo loginInfo = MemberTools.isSaasLogin(request);
			if(null == loginInfo || null == loginInfo.getDealerId()){
				return msg.setError("请先登录");
			}else{
				page.setBuyerId(Integer.parseInt(loginInfo.getDealerId()));
			}
		}
		try {
			int i = paymentAccountService.add(page);
			if(i>0){
				msg.setCode(0);
			}else{
				msg.setError("添加失败");
			}
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 修改付款账号
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/update", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "修改付款账号")
	public ResponseMsg update(@ModelAttribute PaymentAccountPage page,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		if(page.getBuyerId()==null){
			LoginInfo loginInfo = MemberTools.isSaasLogin(request);
			if(null == loginInfo || null == loginInfo.getDealerId()){
				return msg.setError("请先登录");
			}else{
				page.setBuyerId(Integer.parseInt(loginInfo.getDealerId()));
			}
		}
		try {
			int i = paymentAccountService.update(page);
			if(i>0){
				msg.setCode(0);
			}else{
				msg.setError("修改失败");
			}
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 删除付款账号
	 * @param request 
	 * @param consignAddressPage
	 * @return
	 */
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = " 删除付款账号")
	private ResponseMsg delete(HttpServletRequest request,@ApiParam(value="付款账号ID") @RequestParam String id) {
		/*String id = request.getParameter("id");*/
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return msg.setError("请先登录");
		}
		
		try {
			int i = paymentAccountService.delete(id);
			if(i>0){
				msg.setCode(0);
			}else{
				msg.setError("删除失败");
			}
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 设置默认付款账号
	 * @param request 
	 * @param consignAddressPage
	 * @return
	 */
	@RequestMapping(value="/setdefault", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "设置默认付款账号 ")
	private ResponseMsg setdefault(HttpServletRequest request,@ApiParam(value="付款账号ID") @RequestParam String id) {
		/*String id = request.getParameter("id");*/
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return msg.setError("请先登录");
		}
		try {
			paymentAccountService.setDefault(Integer.parseInt(id), Integer.parseInt(loginInfo.getDealerId()));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}

}
