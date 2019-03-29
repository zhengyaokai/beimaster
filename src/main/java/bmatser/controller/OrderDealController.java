package bmatser.controller;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import bmatser.annotations.Login;
import bmatser.annotations.ResponseDefault;
import bmatser.model.LoginInfoUtil;
import bmatser.pageModel.OrderDealPage;
import bmatser.pageModel.PageMode;
import bmatser.service.OrderInfoI;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;

@RequestMapping("order_detail")
@Controller
@Api(value="order_detail", description="订单处理")
public class OrderDealController {

	@Autowired
	private OrderInfoI orderI;
	
	/**
	 * 订单明细
	 * @param channel  saas pc端 app手机端
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{channel}/detail",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "订单明细")
	public ResponseMsg getOrderDetail(@ApiParam(value="web,mall,app") @PathVariable String channel,HttpServletRequest request){
		ResponseMsg msg= new ResponseMsg();
		String orderId = request.getParameter("orderId");
		if(StringUtils.isBlank(orderId)){
			return msg.setError("参数错误");
		}
		try {
			Map<String, Object> data = new HashMap<String, Object>();
			switch (channel) {
			case "web":
				LoginInfo loginInfo = MemberTools.isSaasLogin(request);
				if(null == loginInfo || null == loginInfo.getDealerId()){
					return msg.setError("请先登录");
				}
				data = orderI.getOrderDetailBySaas(orderId,loginInfo.getDealerId());
				break;
			case "app":
				String dealerId = request.getParameter("dealerId");
				data = orderI.getOrderDetailByApp(orderId,dealerId);
				break;
			default: 
				return msg.setError("数据不存在");
			}
			msg.setData(data);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 确认订单
	 * @param channel   saas pc端 app手机端
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value={"/mall/confirmOrder","/app/confirmOrder"},method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "确认订单")
	@Login(mall="/mall/confirmOrder",app="/app/confirmOrder")
	@ResponseDefault
	public Object confirmOrder(HttpServletRequest request,
			@ApiParam(value="零售商购物车idList") @RequestParam String ids,
			@ApiParam(value="是否使用现金券") @RequestParam(required=false) String isCash,
			@ApiParam(value="是否使用工币") @RequestParam(required=false) String isBean,
			@ApiParam(value="地址Id") @RequestParam(required=false) String addrId,
			LoginInfoUtil loginInfo) throws Exception{
		if(StringUtils.isBlank(ids)){
			throw new RuntimeException("参数错误");
		}
		String dealerId = loginInfo.getDealerId();
		switch (loginInfo.getChannel()) {
		case MALL:
			return orderI.confirmOrderBySaas(ids,dealerId,isCash,addrId);
		case APP:
			ids = URLDecoder.decode(ids, "UTF-8");
			return orderI.confirmOrderByApp(ids,dealerId,isCash,isBean,addrId);
		default:
			throw new RuntimeException("数据不存在");
		}
	}
	/**
	 * 订单列表
	 * @param channel  saas pc端 app手机端
	 * @param page 页数
	 * @param request
	 * @param orderPage 页面参数
	 * @return
	 */
	@RequestMapping(value="/{channel}/list/{page}",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "订单列表")
	public ResponseMsg getOrderLIst(@ApiParam(value="saas,app") @PathVariable String channel,
			@ApiParam(value="页数") @PathVariable int page,HttpServletRequest request,@ModelAttribute OrderDealPage orderPage){
		ResponseMsg msg= new ResponseMsg();
		try {
			Map<String, Object> data = new HashMap<String, Object>();
			switch (channel) {
			case "saas":
				LoginInfo loginInfo = MemberTools.isSaasLogin(request);
				if(null == loginInfo || null == loginInfo.getDealerId()){
					return msg.setError("请先登录");
				}
				data = null;
				break;
			case "app":
				orderPage.setPageParam(page);
				if(StringUtils.isBlank(orderPage.getDealerId())){
					return msg.setError("登陆参数错误");
				}
				data = orderI.getOrderLIstByApp(orderPage);
				break;
			default: 
				return msg.setError("数据不存在");
			}
			msg.setData(data);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * saaspc订单物流信息
	 * @author felx
	 */
	@RequestMapping(value={"/order_logistics","/app/order_logistics"},method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "pcsaas订单物流信息")
	public ResponseMsg getOrderLogistics(@ApiParam(value="订单id") @RequestParam String orderId,HttpServletRequest request){
		ResponseMsg msg= new ResponseMsg();
		PageMode pageMode = new PageMode(request);
		String dealerId;	
		if(StringUtils.isBlank(orderId)){
			return msg.setError("参数错误");
		}
		try {
			switch (pageMode.channel()) {
			case "app":
				msg.setCode(0);
				msg.setData(orderI.getOrderPackageLogistics(orderId));
				//msg.setData(orderI.getOrderLogistics(orderId));
				break;
			default:
				msg.setCode(0);
				msg.setData(orderI.getOrderPackageLogistics(orderId));
				break;
			}
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * saaspc订单发票物流信息
	 * @author felx
	 */
	@RequestMapping(value={"/getInvoiceLogistics","/app/getInvoiceLogistics"},method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "pcsaas订单发票物流信息")
	public ResponseMsg getInvoiceLogistics(@ApiParam(value="订单id") @RequestParam String orderId,HttpServletRequest request){
		ResponseMsg msg= new ResponseMsg();
		PageMode pageMode = new PageMode(request);
		String dealerId;
		if(StringUtils.isBlank(orderId)){
			return msg.setError("参数错误");
		}	
		try {
			switch (pageMode.channel()) {
			case "app":
				dealerId = pageMode.getAppLogin().getDealerId();
				break;
			default:
				dealerId = pageMode.getSaasLogin().getDealerId();
				break;
			}
			msg.setData(orderI.getInvoiceLogistics(orderId));			
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
}
