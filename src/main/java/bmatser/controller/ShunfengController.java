package bmatser.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import bmatser.pageModel.ShunfengOperatePage;
import bmatser.service.ShunfengOperateServiceI;
import bmatser.util.AppkeyScrectEnum;
import bmatser.util.ResponseMsg;
import bmatser.util.Util;

@Controller
@RequestMapping("shunfeng")
@Api(value="shunfeng", description="顺丰接口")
public class ShunfengController {

	@Autowired
	private ShunfengOperateServiceI shunfengOperateI;
	/**
	 * 下单
	 * @param shunfengOperatePage
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "顺丰下单",  
	notes = "顺丰下单")
	public ResponseMsg order(
			@ModelAttribute ShunfengOperatePage shunfengOperatePage){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setMsg(shunfengOperateI.orderService(shunfengOperatePage));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 下单
	 * @param shunfengOperatePage
	 * @return
	 */
	@RequestMapping(value="/notice_delivery",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "和稷顺丰下单",  
	notes = "和稷顺丰下单")
	public ResponseMsg noticeOrder(
			@ModelAttribute ShunfengOperatePage shunfengOperatePage){
		ResponseMsg msg = new ResponseMsg();
		/**判断appkey 对应的sign 是否正确,如果不正确,返回失败*/ 
		String appSecret=null;
		for(AppkeyScrectEnum app : AppkeyScrectEnum.values()){
			if(StringUtils.equals(shunfengOperatePage.getAppKey(), app.getAppKey())){
				appSecret = app.getAppScrect();
				break;
			}
		}
		String sign = Util.md5EncryptAndBase64(shunfengOperatePage.getOrderId()+appSecret);
		if(!StringUtils.equals(sign, shunfengOperatePage.getSign()) || appSecret==null){
			msg.setCode(-1);
			return msg;
		}
		
		try {
			shunfengOperateI.orderService(shunfengOperatePage);
			msg.setMsg("success");
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setError("fail");
		}
		return msg;
	}
	
	/**
	 * 订单单取消
	 * @param shunfengOperatePage
	 * @return
	 */
	@RequestMapping(value="/cancel",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "下单取消", 
	notes = "下单取消")
	public ResponseMsg cancel(@ModelAttribute ShunfengOperatePage shunfengOperatePage){
		ResponseMsg msg = new ResponseMsg();
		try {
			shunfengOperateI.cancelOrder(shunfengOperatePage);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 订单单取消
	 * @param shunfengOperatePage
	 * @return
	 */
	@RequestMapping(value="/order_cancel",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "下单取消", 
	notes = "下单取消")
	public ResponseMsg cancelOrder(@ModelAttribute ShunfengOperatePage shunfengOperatePage){
		ResponseMsg msg = new ResponseMsg();
		try {
			shunfengOperateI.orderConfirmService(shunfengOperatePage);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 下单结果查询
	 * @param shunfengOperatePage
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "下单结果查询", 
	notes = "下单结果查询")
	public ResponseMsg detail(@ModelAttribute ShunfengOperatePage shunfengOperatePage){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(shunfengOperateI.orderSearchResult(shunfengOperatePage));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 顺丰打印电子运单
	 * @param shunfengOperatePage
	 * @return
	 */
	@RequestMapping(value="/print_waybill",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "顺丰面单打印", 
	notes = "顺丰面单打印")
	public ResponseMsg printWaybill(String orderId){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(shunfengOperateI.printWaybill(orderId));
			msg.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			msg.setError("打印失败");
		}
		return msg;
	}
}
