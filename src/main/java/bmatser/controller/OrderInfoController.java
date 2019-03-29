package bmatser.controller;

import bmatser.util.alibaba.StringUtil;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import freemarker.template.Template;
import bmatser.annotations.Login;
import bmatser.annotations.ResponseDefault;
import bmatser.model.EditOrder;
import bmatser.model.LoginInfoUtil;
import bmatser.model.OrderInfo;
import bmatser.model.OrderInfoParameter;
import bmatser.model.SysOptionsValue;
import bmatser.pageModel.OrderInfoPage;
import bmatser.pageModel.PageMode;
import bmatser.pageModel.PageMode.Channel;
import bmatser.service.AmountAlgorithmI;
import bmatser.service.OrderInfoI;
import bmatser.service.RegisterI;
import bmatser.service.SysOptionsServiceI;
import bmatser.util.Delivery;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.PropertyUtil;
import bmatser.util.ResponseMsg;

@Controller
@RequestMapping("order")
@Api(value="order", description="订单")
public class OrderInfoController {

	@Autowired
	private OrderInfoI orderService;
	@Autowired
	private SysOptionsServiceI sysOptionsService;
	@Autowired
	private RegisterI registerService;
	
	@Autowired
	private AmountAlgorithmI amountAlgorithmI;
	
	@Autowired
	private  FreeMarkerConfigurer freeMarkerConfigurer;
	
	/**
	 *用户各订单状态的数量(Saas手机端)
	 * @param buyerId
	 * @return
	 */
//	@RequestMapping(value="/count/status/{buyerId}", method=RequestMethod.GET)
//	@ResponseBody
//	@ApiOperation(value = "用户各订单状态的数量(Saas手机端)")
//	public ResponseMsg findCountByStatus(@ApiParam(value="dealerId") @PathVariable Integer buyerId,HttpServletRequest request){
//		return getOrderStatusCount(buyerId,request);
//	}
	/**
	 *用户各订单状态的数量(Saas端)
	 * @param buyerId
	 * @return
	 */
	@RequestMapping(value="/count/status", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "用户各订单状态的数量(Saas端)")
	public ResponseMsg findSaasCountByStatus(HttpServletRequest request){
		return getOrderStatusCount(null,request);
	}
	
	/**
	 * 查看订单(Saas手机端)
	 * @param buyerId
	 * @param status
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/list/{buyerId}/{status}/{page}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查看订单(Saas手机端)")
	public ResponseMsg findOrders(@ApiParam(value="dealerId") @PathVariable Integer buyerId,
			@ApiParam(value="状态")  @PathVariable Integer status,
			@ApiParam(value="页数")  @PathVariable Integer page,
			@ModelAttribute OrderInfoParameter orderInfoParameter,HttpServletRequest request) {
		return getOrderList(buyerId,status,page,orderInfoParameter,request);
	}
	/**
	 * 查看订单(Saas)
	 * @param buyerId
	 * @param status
	 * @param page(页数从1开始)
	 * @return
	 */
	@RequestMapping(value="/list/{page}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查看订单(Saas)")
	public ResponseMsg findSaasOrders(@ApiParam(value="页数") @PathVariable Integer page,
			@ModelAttribute OrderInfoParameter orderInfoParameter,HttpServletRequest request) {
		return getOrderList(null,null,page-1,orderInfoParameter,request);
	}

	/**
	 * 订单明细(手机)
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/detail/{orderId}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "订单明细(手机)")
	public ResponseMsg getOrderDetail(@ApiParam(value="订单ID") @PathVariable Long orderId,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(orderService.getOrderDetail(orderId,null,"2"));//1:供应商2：零售商
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 订单明细(PC商城)
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/detailMall", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "订单明细(PC商城)")
	public ResponseMsg getOrderDetailMall(@ApiParam(value="订单ID") @RequestParam String orderId,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
/*		String orderId = request.getParameter("orderId");*/
		if(StringUtils.isBlank(orderId)){
			return msg.setError("程序参数丢失") ;
		}
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return msg.setError("请先登录");
		}
		try {
			msg.setData(orderService.getOrderDetail(Long.parseLong(orderId),loginInfo.getDealerId(),"2"));//1:供应商2：零售商
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 订单明细(APP商城)
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/detailAppMall", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "订单明细(APP商城)")
	public ResponseMsg getOrderDetailAppMall(@ApiParam(value="订单ID") @RequestParam String orderId,HttpServletRequest request,String dealerId){
		ResponseMsg msg = new ResponseMsg();
		/*String orderId = request.getParameter("orderId");*/
		if(StringUtils.isBlank(orderId)){
			return msg.setError("程序参数丢失") ;
		}
		if(StringUtils.isBlank(dealerId)){
			return msg.setError("请先登录");
		}
		try {
			msg.setData(orderService.getOrderDetail(Long.parseLong(orderId),dealerId,"2"));//1:供应商2：零售商
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 订单明细(Saas)
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/detail", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "订单明细(Saas)")
	public ResponseMsg getOrderDetail(@ApiParam(value="订单ID") @RequestParam String orderId,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		/*String orderId = request.getParameter("orderId");*/
		if(StringUtils.isBlank(orderId)){
			return msg.setError("程序参数丢失") ;
		}
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return msg.setError("请先登录");
		}
		try {
			msg.setData(orderService.getOrderDetail(Long.parseLong(orderId),loginInfo.getDealerId(),"2"));//1:供应商2：零售商
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg ;
	}
	/**
	 * 取消订单
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/cancel/{orderId}", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "取消订单")
	public  ResponseMsg cancelOrder(@ApiParam(value="订单ID") @PathVariable Long orderId){
		ResponseMsg msg = new ResponseMsg();
		try {
			orderService.cancelOrder(orderId);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}

    /**
     * 删除订单
     * create by yr on 2018-10-29
     */

    @RequestMapping(value="delete", method=RequestMethod.GET)
    @ResponseBody
    public  ResponseMsg deleteOrder( Long orderId){
        ResponseMsg msg = new ResponseMsg();
        try {
            orderService.deleteOrderI(orderId);
            msg.setCode(0);
        } catch (Exception e) {
            msg.setError(e);
        }
        return msg;
    }

	/**
	 * 取消订单(手机Saas)
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/mobile/cancel", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "取消订单(手机Saas)")
	public  ResponseMsg cancelOrder(HttpServletRequest request,HttpServletResponse response,
			@ApiParam(value="订单ID") @RequestParam String orderId,@ApiParam(value="dealerId") @RequestParam String buyerId){
		return cancelOrderById(request,response,orderId,buyerId);
	}
	/**
	 * 取消订单(Saas)
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/cancel", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "取消订单(Saas)")
	public  ResponseMsg cancelSaasOrder(HttpServletRequest request,HttpServletResponse response,
			@ApiParam(value="订单ID") @RequestParam String orderId,@ApiParam(value="dealerId") @RequestParam(required=false) String buyerId)
	{
		return cancelOrderById(request,response,orderId,buyerId);
	}

	/**
	 * 确认收货
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/receipt/{orderId}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "确认收货")
	public ResponseMsg confirmReceipt(@ApiParam(value="订单ID") @PathVariable Long orderId){
		ResponseMsg msg = new ResponseMsg();
		try {
			orderService.confirmReceipt(orderId);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 确认收货
	 * @param orderId 订单Id
	 * @param passWord 密码
	 * @param dealerId app端需要用户Id
	 * @return
	 */
	@RequestMapping(value={"/receipt","/app/receipt"}, method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "确认收货")
	public ResponseMsg Receipt(HttpServletRequest request,@ApiParam(value="订单Id") @RequestParam Long orderId)
	{
		PageMode pm = new PageMode(request);
/*		String orderId = request.getParameter("orderId");
		String passWord = request.getParameter("passWord");*/
		ResponseMsg msg = new ResponseMsg();
/*		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return msg.setError("请先登录");
		}*/
		/*if(!pm.contains("orderId","passWord")){
			return msg.setError("参数错误");
		}*/
		if(orderId==null){
			return msg.setError("参数错误");
		}
		try {
			switch (pm.channel()) {
			case "app":
				/*orderService.confirmReceipt(pm.getBigDecimalValue("orderId").longValue());*/
				orderService.confirmReceipt(orderId);
				break;
			default:
				//registerService.login(request, pm.getSaasLogin().getUsername(),pm.getMd5AndSha("passWord") , "2");
				/*orderService.confirmReceipt(pm.getBigDecimalValue("orderId").longValue());*/
				orderService.confirmReceipt(orderId);
				registerService.loadUserInfo(request);
				break;
			}
/*			registerService.login(request,loginInfo.getUsername(), Encrypt.md5AndSha(passWord),"2");
			orderService.confirmReceipt(Long.parseLong(orderId));
			registerService.loadUserInfo(request);*/
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 保存订单(通用)
	 * @param orderInfoPage
	 * @return	
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "保村订单(通用)")
	public ResponseMsg  addOrderInfo(@ModelAttribute OrderInfoPage orderInfoPage,HttpServletRequest request,HttpServletResponse response){
		ResponseMsg msg = new ResponseMsg();
		Integer buyerId = orderInfoPage.getBuyerId();
		if(buyerId == null){
			LoginInfo loginInfo = MemberTools.isSaasLogin(request);
			if(null == loginInfo || null == loginInfo.getDealerId()){
				return msg.setError("请先登录");
			}else{
				buyerId = Integer.parseInt(loginInfo.getDealerId());
				orderInfoPage.setBuyerId(buyerId);
				if(StringUtils.isBlank(orderInfoPage.getOrderChannel())){
					orderInfoPage.setOrderChannel(StringUtils.isNotBlank(orderInfoPage.getOrderChannel())?orderInfoPage.getOrderChannel():"1");
				}
			}
		}
		try {
			msg.setData(orderService.addOrderInfo(orderInfoPage));
			registerService.loadUserInfo(request);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 修改订单(通用)
	 * @param orderInfoPage
	 * @return
	 */
	@RequestMapping(method=RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = " 修改订单(通用)")
	public ResponseMsg  editOrderInfo(@ModelAttribute OrderInfoPage orderInfoPage,HttpServletRequest request,HttpServletResponse response){
		ResponseMsg msg = new ResponseMsg();
		Integer buyerId = orderInfoPage.getBuyerId();                                                                                                                                                                                                                                                                                                                                                                  
		if(buyerId == null){
			LoginInfo loginInfo = MemberTools.isLogin(request);
			if(null == loginInfo || null == loginInfo.getDealerId()){
				return msg.setError("请先登录");
			}else{
				buyerId = Integer.parseInt(loginInfo.getDealerId());
			}
		}
		try {
			orderService.updateOrder(orderInfoPage);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 修改订单
	 * @param orderInfoPage
	 * @return
	 */
	@RequestMapping(value="/mobile/edit",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "修改订单")
	public ResponseMsg  editOrderInfoBymobile(@ModelAttribute OrderInfoPage orderInfoPage){
		ResponseMsg msg = new ResponseMsg();
		try {
			orderService.updateOrder(orderInfoPage);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 商城保存订单
	 * @param orderInfoPage
	 * @return
	 */
	@RequestMapping(value={"/mallOrder","/app/mallOrder"}, method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "商城保存订单")
	public ResponseMsg  addOrderInfoMall(@ModelAttribute OrderInfoPage orderInfoPage,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		PageMode model = new PageMode(request);
		Integer dealerId = null;
		StringBuffer url = request.getRequestURL();
		try {
			if(url.indexOf("/app/")<0){
				dealerId = model.getDealerIdDecimalVal(Channel.MALL).intValue();
				orderInfoPage.setBuyerId(dealerId);
			}
			msg.setData(orderService.addMallOrderInfo(orderInfoPage));
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 商城获取订单列表
	 * @param buyerId
	 * @param status
	 * @param page
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/list/{status}/{page}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城获取订单列表")
	public ResponseMsg findMallOrders(@ApiParam(value="状态") @PathVariable Integer status,
			@ApiParam(value="页数") @PathVariable Integer page,
			@ApiParam(value="订单ID") @RequestParam(required=false) String orderId,
			@ApiParam(value="订货号") @RequestParam(required=false) String buyNo,
			@ApiParam(value="商品名称") @RequestParam(required=false) String goodsName,
			@ApiParam(value="下单开始时间") @RequestParam(required=false) String startTime,
			@ApiParam(value="下单截止时间") @RequestParam(required=false) String endTime,
			HttpServletRequest request) throws UnsupportedEncodingException {
		Integer rows = 8;
		ResponseMsg msg = new ResponseMsg();
		if(page<1){
			return msg.setError("程序参数错误");
		}else{
			page = page - 1;
		}
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			msg.setMsg("请先登录");
			msg.setCode(-1);
			return msg;
		}
		/*String orderId = request.getParameter("orderId");
		String buyNo = request.getParameter("buyNo");
		String goodsName = null;
		if(request.getParameter("goodsName")!=null &&request.getParameter("goodsName")!=""){
			goodsName = URLDecoder.decode(request.getParameter("goodsName"),"UTF-8");
		}
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");*/
		if(StringUtils.isNotBlank(goodsName)){
			goodsName = URLDecoder.decode(goodsName,"UTF-8");
		}
		try {
			msg.setData(orderService.findMallOrders(Integer.valueOf(loginInfo.getDealerId()), status, page*rows, rows,orderId,startTime,endTime,null,1,buyNo,goodsName));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * APP商城获取订单列表
	 * @param buyerId
	 * @param status
	 * @param page
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/AppMallList/{status}/{page}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "App商城获取订单列表")
	public ResponseMsg findAppMallOrders(@ApiParam(value="状态") @PathVariable Integer status,
			@ApiParam(value="页数") @PathVariable Integer page,HttpServletRequest request,
			@ApiParam(value="dealerId") @RequestParam String dealerId,
			@ApiParam(value="订单ID") @RequestParam(required=false) String orderId,
			@ApiParam(value="订货号") @RequestParam(required=false) String buyNo,
			@ApiParam(value="商品名称") @RequestParam(required=false) String goodsName,
			@ApiParam(value="下单开始时间") @RequestParam(required=false) String startTime,
			@ApiParam(value="下单截止时间") @RequestParam(required=false) String endTime) throws UnsupportedEncodingException {
		Integer rows = 8;
		ResponseMsg msg = new ResponseMsg();
		if(page<1){
			return msg.setError("程序参数错误");
		}else{
			page = page - 1;
		}
		if(StringUtils.isBlank(dealerId)){
			msg.setMsg("请先登录");
			msg.setCode(-1);
			return msg;
		}
		/*String orderId = request.getParameter("orderId");
		String buyNo = request.getParameter("buyNo");
		String goodsName = null;
		if(request.getParameter("goodsName")!=null &&request.getParameter("goodsName")!=""){
			goodsName = URLDecoder.decode(request.getParameter("goodsName"),"UTF-8");
		}
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");*/
		if(StringUtils.isNotBlank(goodsName)){
			goodsName = URLDecoder.decode(goodsName,"UTF-8");
		}
		try {
			msg.setData(orderService.findMallOrders(Integer.valueOf(dealerId), status, page*rows, rows,orderId,startTime,endTime,null,1,buyNo,goodsName));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * 获取物流信息
	 * @param buyerId
	 * @param status
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/deliveryInfo", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = " 获取物流信息")
	public ResponseMsg deliveryInfo(@ApiParam(value="快递公司编码") @RequestParam String comNo,
			@ApiParam(value="快递单号") @RequestParam String num,HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(Delivery.search(comNo, num));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * 获取订单支付后信息
	 * @param buyerId
	 * @param status
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/orderPayInfo", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取订单支付后信息")
	public ResponseMsg orderPaymentInfo(@ApiParam(value="订单Id") @RequestParam Long orderId,HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		try {
			OrderInfo orderInfo = orderService.getOrderById(Long.valueOf(orderId));
			Map map = new HashMap();
			if(null != orderInfo.getCompanyAccount()){
				SysOptionsValue SysOptionsValue = sysOptionsService.findPaySysOption(orderInfo.getCompanyAccount(), 4);
				map.put("orderId", orderInfo.getId());
				map.put("payAmount", orderInfo.getPayAmount());
				map.put("payName", SysOptionsValue.getRemark());
			}
			msg.setData(map);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}


	/**
	 * 用户各订单状态的数量(通用)
	 * @param buyerId
	 * @param request
	 * @return
	 */
	private ResponseMsg getOrderStatusCount(Integer buyerId, HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		if(buyerId==null){
			LoginInfo loginInfo = MemberTools.isSaasLogin(request);
			if(null == loginInfo || null == loginInfo.getDealerId()){
				return msg.setError("请先登录");
			}else{
				buyerId = Integer.parseInt(loginInfo.getDealerId());
			}
		}
		try {
			msg.setData(orderService.findCountByStatus(buyerId));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * Saas查看订单(通用)
	 * @param buyerId
	 * @param status
	 * @param page
	 * @param request
	 * @return
	 */
	private ResponseMsg getOrderList(Integer buyerId, Integer status,
			Integer page, OrderInfoParameter orderInfoParameter,HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		String orderId = orderInfoParameter.getOrderId();
		String startTime = orderInfoParameter.getStartTime();
		String endTime = orderInfoParameter.getEndTime();
		String logisticsCode = orderInfoParameter.getLogisticsCode();
		String queryType = request.getParameter("queryType");
		/*String orderId = request.getParameter("orderId");
		String queryType = request.getParameter("queryType");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String logisticsCode = request.getParameter("logisticsCode");
		Integer rows = StringUtils.isNotBlank(request.getParameter("rows"))?Integer.parseInt(request.getParameter("rows")) : 8;
		status = StringUtils.isNotBlank(request.getParameter("status"))?Integer.parseInt(request.getParameter("status")) : status;*/
		Integer rows = null;
		if(orderInfoParameter.getRows()==null){
			rows = 8;
		}else{
			rows = orderInfoParameter.getRows();
		}
		if(orderInfoParameter.getStatus()!=null){
			status = orderInfoParameter.getStatus();
		}
		/*Integer rows = StringUtils.isNotBlank(orderInfoParameter.getRows().toString())?orderInfoParameter.getRows() : 8;
		status = StringUtils.isNotBlank(orderInfoParameter.getStatus().toString())?orderInfoParameter.getStatus() : status;*/
		if(status==null || "".equals(status)){
			return msg.setError("程序参数丢失");
		}
		if(page<0){
			return msg.setError("程序页数不存在");
		}
		if(buyerId==null){
			LoginInfo loginInfo = MemberTools.isSaasLogin(request);
			if(null == loginInfo || null == loginInfo.getDealerId()){
				return msg.setError("请先登录");
			}else{
				buyerId = Integer.parseInt(loginInfo.getDealerId());
			}
		}
		try {
			msg.setData(orderService.findOrders(buyerId, status, page*rows, rows,orderId,queryType,startTime,endTime,logisticsCode,0));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 取消订单(通用)
	 * @param orderId
	 * @return
	 */
	private ResponseMsg cancelOrderById(HttpServletRequest request,HttpServletResponse response,String orderId,String buyerId) {
		ResponseMsg msg = new ResponseMsg();
		/*String orderId = request.getParameter("orderId");
		String buyerId = request.getParameter("buyerId");*/
		if(StringUtils.isBlank(buyerId)){
			LoginInfo loginInfo = MemberTools.isSaasLogin(request);
			if(null == loginInfo || null == loginInfo.getDealerId()){
				return msg.setError("请先登录");
			}else{
				buyerId = loginInfo.getDealerId();
			}
		}
		try {
			orderService.cancelOrder(orderId,buyerId);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 保存支付页面线下支付信息
	 * @author felx
	 * @param 
	 * @return
	 */
	@RequestMapping(value={"/updateOrderOfflinePay","/mall/updateOrderOfflinePay"}, method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "保存支付页面线下支付信息")
	public ResponseMsg updateOrderOfflinePay(HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		PageMode model = new PageMode(request);
		LoginInfo loginInfo = null;
		try {
			switch (model.channel()) {
			case "mall":
				loginInfo = model.getMallLogin();
				break;
				
			default:
				loginInfo = model.getSaasLogin();
				break;
			}
			int m = orderService.updateOrderOfflinePay(request,loginInfo);
			if(m>0){
				msg.setCode(0);
				msg.setMsg("提交成功");
			}else{
				msg.setCode(-1);
				msg.setMsg("提交失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * 获取订单合同
	 * @author felx
	 */
	@RequestMapping(value="/selectContract", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取订单合同")
	public ResponseMsg selectContract(@ApiParam(value="订单Id") @RequestParam Long orderId,HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			msg.setMsg("请先登录");
			msg.setCode(-1);
			return msg;
		}
		try {
			Map mp = orderService.selectContract(orderId);
			msg.setData(mp);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * SAAS,商城根据订单id获取订单应付金额
	 *
	 */
	@RequestMapping(value={"/selectOrderInfoById","/mall/selectOrderInfoById"}, method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据订单id获取订单应付金额")
	public ResponseMsg selectOrderInfoById(@ApiParam(value="订单Id") @RequestParam Long orderId,HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		PageMode pageMode = new PageMode(request);
		Integer dealerId;
		try {
			switch (pageMode.channel()) {
			case "mall":
				dealerId = Integer.parseInt(pageMode.getMallLogin().getDealerId());
				break;
			default:
				dealerId = Integer.parseInt(pageMode.getSaasLogin().getDealerId());
				break;
			}
		    OrderInfo mp = orderService.getOrderById(orderId);
			msg.setData(mp);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}

/*	@RequestMapping(value="/contract", method=RequestMethod.GET)
	@ApiOperation(value = "订单合同")
	public ModelAndView selectOrderInfoById(@ApiParam(value="订单Id") @RequestParam String id,HttpServletRequest request,HttpServletResponse response) {
		String id = request.getParameter("id");
		String referer = request.getHeader("Referer");
		ModelAndView mav = null;
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			if(referer==null || referer.indexOf("/customer/order/contract/")<0){
				return new ModelAndView("register");
			}
		}
		try {
			mav = new ModelAndView("dowload/contract");
			mav.addObject("map", orderService.getContractById(id));
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView("register");
		}
		
	}*/
	/**
	 * pdf合同
	 * @author felx
	 * @describe 
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/contract", method=RequestMethod.GET)
	@ApiOperation(value = "订单合同")
	public ResponseEntity<byte[]> getContractPDF(
			@ApiParam(value="订单Id") @RequestParam String id,
			HttpServletRequest request,
			HttpServletResponse response) {
		HttpHeaders headers = new HttpHeaders(); 
        try {
        	Template template = freeMarkerConfigurer.getConfiguration().getTemplate(
        			"contract.ftl");
        	StringWriter stringWriter = new StringWriter();
        	template.process(orderService.getContractById(id), stringWriter);
        	headers.setContentDispositionFormData("attachment", "contract.pdf");   
        	headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        	ITextRenderer renderer = new ITextRenderer(); 
        	renderer.setDocumentFromString(stringWriter.toString());
        	String font = PropertyUtil.getPropertyValue("properties/info.properties", "pdtFont");
        	renderer.getFontResolver().addFont(font,BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        	// 解决图片的相对路径问题,图片路径必须以file开头  
        	renderer.layout();  
			renderer.createPDF(baos);
			return new ResponseEntity<byte[]>(baos.toByteArray(),    
					headers, HttpStatus.CREATED); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
	}
	
	
	
	/**
	 * Saas供应商订单列表  
	 * @param buyerId
	 * @param status
	 * @param page(页数从1开始)
	 * @author felx
	 * @return
	 */
	@RequestMapping(value="/selectSellerOrderList/{page}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Saas供应商订单列表  ")
	public ResponseMsg selectSellerOrderList(@ApiParam(value="页数") @PathVariable Integer page,HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return msg.setError("请先登录");
		}
		try {
			Map<String, Object> mp=orderService.selectSellerOrderList(Integer.parseInt(loginInfo.getDealerId()),page, request);
			msg.setData(mp);
			msg.setCode(0);
			msg.setMsg("查询供应商订单列表成功");
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg("查询供应商订单列表失败");
			msg.setData(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * 打印 订单明细(Saas) 供应商 
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/selectSellerDetail", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "打印 订单明细(Saas) 供应商 ")
	public ResponseMsg selectSellerDetail(HttpServletRequest request,@ApiParam(value="订单Id") @RequestParam String orderId){
		ResponseMsg msg = new ResponseMsg();
		/*String orderId = request.getParameter("orderId");*/
		if(StringUtils.isBlank(orderId)){
			return msg.setError("程序参数丢失") ;
		}
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return msg.setError("请先登录");
		}
		try {
			msg.setData(orderService.getOrderDetail(Long.parseLong(orderId),loginInfo.getDealerId(),"1"));//1:供应商2：零售商
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg ;
	}
	/**
	 * 修改订单商品
	 * @param request
	 * @param channel 请求来源
	 * @param orderId 订单Id
	 * @param orderGoodsId[] 已购买商品Id 
	 * @param sellerGoodsId[]	商品Id
	 * @param num[] 数量集合
	 * @return
	 */
	@RequestMapping(value="/{channel}/modifyOrder",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "修改订单商品")
	public ResponseMsg editOrderGoods(
			HttpServletRequest request,
			@ApiParam(value="channel") @PathVariable("channel")String channel
			){
		ResponseMsg msg = new ResponseMsg();
		PageMode page = new PageMode(request);
		try {
			switch (channel) {
			case "saas":
				EditOrder o = orderService.editOrderGoodsBySaas(page);
				orderService.saveEditOrder(page);
				break;
			default:
				break;
			}
			registerService.loadUserInfo(request);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 *	显示修改后的优惠和商品
	 * @param request
	 * @param channel
	 * @return
	 */
	@RequestMapping(value="/{channel}/showOrder",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "显示修改后的优惠和商品")
	public ResponseMsg showOrderGoods(
			HttpServletRequest request,
			@ApiParam(value="channel") @PathVariable("channel")String channel
			){
		ResponseMsg msg = new ResponseMsg();
		PageMode page = new PageMode(request);
		try {
			Object o = null;
			switch (channel) {
			case "saas":
				o = orderService.editOrderGoodsBySaas(page).toPageShow();
				break;
			default:
				break;
			}
			msg.setData(o);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 根据订单号查询支付金额及支付方式
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/selectPaymentAmountAndMethod", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据订单号查询支付金额及支付方式")
	public ResponseMsg selectPaymentAmountAndMethod(@ApiParam(value="订单Id") @RequestParam String orderId,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		/*String orderId = request.getParameter("orderId");*/
		if(StringUtils.isBlank(orderId)){
			return msg.setError("程序参数丢失") ;
		}
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return msg.setError("请先登录");
		}
		try {
			msg.setData(orderService.selectPaymentAmountAndMethod(Long.parseLong(orderId)));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg ;
	}
	
	/**
	 * 商城个人中心首页查询订单 待付款，待发货，待收货 总数
	 */
//	@RequestMapping(value="/getOrderCountByDealerId", method=RequestMethod.GET)
//	@ResponseBody
//	@ApiOperation(value = "商城个人中心首页查询订单 待付款，待发货，待收货 总数")
//	public ResponseMsg getOrderCountByDealerId(HttpServletRequest request){
//		ResponseMsg msg = new ResponseMsg();
//		PageMode pageMode = new PageMode(request);
//		try {
//			Integer dealerId = Integer.parseInt(pageMode.getDealerId(Channel.MALL));
//			msg.setData(orderService.getOrderCountByDealerId(dealerId));
//			msg.setCode(0);
//		} catch (Exception e) {
//			msg.setError(e);
//		}
//		return msg;
//	}
	
	/**
	 * SaaS首页查询订单 待付款，待发货，待收货 总数
	 */
	@RequestMapping(value="/order_details", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "SaaS首页查询订单 待付款，待发货，待收货 总数")
	public ResponseMsg getOrderCount(HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		PageMode pageMode = new PageMode(request);
		try {
			Integer dealerId = Integer.parseInt(pageMode.getDealerId(Channel.SAAS));
			msg.setData(orderService.getOrderCount(dealerId));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 商城个人中心首页获取三条订单信息（待付款，待发货，待收货）
	 * @param buyerId
	 * @param status
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/getOrderInfoFirst/{status}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城个人中心首页获取三条订单信息（待付款，待发货，待收货）")
	public ResponseMsg getOrderInfoFirst(@ApiParam(value="状态") @PathVariable Integer status,HttpServletRequest request) {
		Integer rows = 8;
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			msg.setMsg("请先登录");
			msg.setCode(-1);
			return msg;
		}
		try {
			msg.setData(orderService.getOrderInfoFirst(Integer.valueOf(loginInfo.getDealerId()),status));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * 微信商城订单详情查看发票详情
	 * @param invId
	 * @return
	 */
	@RequestMapping(value="/getOrderInvoiceDetils", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "微信商城订单详情查看发票详情")
	public ResponseMsg getOrderInvoiceDetils(@ApiParam(value="发票Id") @RequestParam String invId) {
		ResponseMsg msg = new ResponseMsg();
		if(StringUtils.isBlank(invId)){
			throw new RuntimeException("参数错误!");
		}
		try {
			msg.setData(orderService.getOrderInvoiceDetils(Integer.valueOf(invId)));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	
	@RequestMapping(value={"/againToBuy","/app/againToBuy"},method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "再次购买",  notes = "再次购买")
	@ApiParam(required=true,name="orderId",value="订单号")
	@Login(saas="/againToBuy",app="/app/againToBuy")
	@ResponseDefault
	public Object addAgainToBuy(@ApiParam(value="订单Id") @RequestParam String orderId,HttpServletRequest request,LoginInfoUtil login) throws Exception{
		String dealerId = login.getDealerId();
		orderService.addAgainToBuy(orderId,dealerId);
		return null;
	}
	/**
	 * 判断订单能否再次购买
	 * @param orderId 订单号
	 * @param request
	 * @param login 自动获取
	 * @return
	 */
	@RequestMapping(value={"/buyJudge","/app/buyJudge"},method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "判断订单能否再次购买",  notes = "判断订单能否再次购买")
	@ApiParam(required=true,name="orderId",value="订单号")
	@Login(saas="/buyJudge",app="/app/buyJudge")
	public ResponseMsg tobuyJudge(@ApiParam(value="订单Id") @RequestParam String orderId,HttpServletRequest request,LoginInfoUtil login){
		ResponseMsg msg = new ResponseMsg();
		try {
			String dealerId = login.getDealerId();
			int code = orderService.tobuyJudge(orderId,dealerId);
			if(code == 1){
				msg.setMsg("该订单下所有商品或套餐已下架");
				msg.setCode(1);
			}else if(code == 2){
				msg.setMsg("您的订单中有已下架商品或商品，这部分商品将不再添加到购物车中，确认再次购买吗？");
				msg.setCode(2);
			}
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
		
	}
	
	@RequestMapping(value="/freightAmount",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "获得saas运费",  notes = "获得saas运费")
	@Login(saas="/freightAmount")
	@ResponseDefault
	public Object getFreightAmount(
			@ApiParam(value="省Id") @RequestParam String provinceId,
			@ApiParam(value="购物车Id") @RequestParam String ids
			){
			List<String> cartIds = new ArrayList<>();
			String[] cartIdArrary = ids.split(",");
			Collections.addAll(cartIds, cartIdArrary);
			return amountAlgorithmI.getFreightAmount(cartIds, provinceId);
		}

    /**
     * create by yr on 2018-11-2
     */

    @RequestMapping(value="/getOrderNum",method = RequestMethod.GET)
    @ResponseBody
    public ResponseMsg getOrderNumForIos(@RequestParam("id") Integer id){
        ResponseMsg msg = new ResponseMsg();
        Map<String,Object> result = new HashMap<>();
        try {
            if(id==null){
                msg.setCode(-1);
                msg.setData("请先登陆!");
                return msg;
            }
            result = orderService.getOrderNumForIos(id);
            msg.setData(result);
            msg.setCode(0);
        }catch (Exception e){
            msg.setCode(-1);
            msg.setData(e.getMessage());
        }
        return msg;
    }


    @RequestMapping(value = "/checkUser",method = RequestMethod.GET)
    @ResponseBody
    @Login(saas = "/checkUser")
    @ResponseDefault
    public ResponseMsg CheckUser(LoginInfoUtil login){
        ResponseMsg msg = new ResponseMsg();
        try{
            String dealerId = login.getDealerId();
            if(StringUtils.isNotBlank(dealerId)){
                String checkStatus = orderService.checkUser(dealerId);
                msg.setData(checkStatus);
                msg.setCode(0);
            }else {
                msg.setCode(-1);
                msg.setData("请先登陆");
                return msg;
            }
        }catch (Exception e){
            msg.setData(e.getMessage());
            msg.setCode(-1);
        }
        return msg;
    }



}
