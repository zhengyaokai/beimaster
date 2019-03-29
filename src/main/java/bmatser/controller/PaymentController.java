/*

 * Support: http://www.huqiuhsc.com
 * License: http://www.huqiuhsc.com/license
 */
package bmatser.controller;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.igfay.jfig.JFig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

//import bmatser.service.MongoServiceI;
//import bmatser.service.impl.MQSender;
import bmatser.annotations.Login;
import bmatser.annotations.ResponseDefault;
import bmatser.dao.CrmCustomerMapper;
import bmatser.dao.DealerMapper;
import bmatser.exception.GdbmroException;
import bmatser.model.LoginInfoUtil;
import bmatser.model.OrderInfo;
import bmatser.model.SysOptionsValue;
import bmatser.model.ToPayLogger;
import bmatser.pageModel.PageMode;
import bmatser.pageModel.PaymentBillPage;
import bmatser.pageModel.PageMode.Channel;
import bmatser.plugin.PaymentPlugin;
import bmatser.plugin.weixin.Signature;
import bmatser.plugin.weixin.WeixinPlugin;
import bmatser.service.AddPaymentBillServiceI;
import bmatser.service.OrderInfoI;
import bmatser.service.PaymentServiceI;
import bmatser.service.PluginService;
import bmatser.service.SysOptionsServiceI;
import bmatser.util.Constants;
import bmatser.util.IpUtil;
import bmatser.util.LoginInfo;
import bmatser.util.MQMessage;
import bmatser.util.MemberTools;
import bmatser.util.ParamsSign;
import bmatser.util.ResponseMsg;

/**
 * Controller - 支付
 * 
 * @author felx
 * @version 3.0
 */
@Controller("shopPaymentController")
@RequestMapping("/payment")
@Api(value="payment", description="支付")
public class PaymentController {

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;
	
	@Autowired
	private OrderInfoI orderService; 
	@Autowired
	private DealerMapper dealerMapper;
	@Autowired
	private CrmCustomerMapper crmCustomerMapper;
	
	@Autowired
	private PaymentServiceI paymentI; 
	@Autowired
	private SysOptionsServiceI sysOptionsService;
	@Autowired
	private AddPaymentBillServiceI addPaymentBillService;
	@Autowired
//	private GoldMantisServiceI goldMantisServiceI;
//	@Autowired
//	private MQSenderI mqSender;
//	@Autowired
//	private MongoTemplate MongoDao;
	
	@Value("#{configProperties['pay.repeatPay']}")//重复提交支付跳转页面
	private String repeatPay;
	
	private final boolean beecloud = false;
	
	Logger log=Logger.getLogger(PaymentController.class);

	/**
	 * 网站提交
	 */
	@RequestMapping(value = "/payment_submit", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "pc微信支付提交")
	public ResponseMsg wxSubmit(
			HttpServletRequest request,
			@ApiParam(value="订单id") @RequestParam String orderId,
			@ApiParam(value="是否使用余额 1是 0否") @RequestParam boolean isbalance,
			@ApiParam(value="支付类型") @RequestParam String paymentType,
			@ApiParam(value="支付渠道") @RequestParam(required=false) String payType){
		ResponseMsg msg = new ResponseMsg();
		try {
			paymentI.updateIsBalance(isbalance,orderId);
			OrderInfo orderInfo = orderService.getOrderById(Long.valueOf(orderId));
			if(orderInfo != null){
				if(!"1".equals(orderInfo.getOrderStatus())){
					throw new GdbmroException(10001, "订单已支付");
				}
				PaymentBillPage paymentBillPage = getPaymentBill(orderInfo);
				if("weixinPlugin".equals(paymentType)||"bcWXPlugin".equals(paymentType)){
					paymentBillPage.setPayType("9");
					orderInfo.setPayType("9");
					orderInfo.setCompanyAccount(7);
				}else if("alipayDirectPlugin".equals(paymentType)){
					paymentBillPage.setPayType("3");
					orderInfo.setPayType("3");
					orderInfo.setCompanyAccount(3);
				}else{
					throw new RuntimeException("支付方式不存在");
				}
				Map map = addPaymentBillService.findPayOrderById(Long.valueOf(orderId));
				if(null != map && !map.isEmpty() && !"0".equals(map.get("num").toString())){
					paymentBillPage.setId(map.get("payment_bill_id").toString());
					addPaymentBillService.updatePaymentBill(paymentBillPage);
				}else{
					addPaymentBillService.savePaymentBill(paymentBillPage);
				}
				Map<String, String> res = new HashMap<String, String>(2); 
				if(orderInfo.getPayAmount().compareTo(orderInfo.getBalanceDeduction())==0){
					paymentI.saveOrderByBalance(orderId);
					res.put("imgSrc", null);
					msg.setData(res);
					return msg;
				}else if("bcWXPlugin".equals(paymentType)){
					BigDecimal amount = (orderInfo.getPayAmount().subtract(orderInfo.getBalanceDeduction())).multiply(new BigDecimal(100));
					Map<String, Object> param = new HashMap<>();
					String openId = dealerMapper.getSaasOpenId(orderId);
					param.put("openid", openId);
					JSONObject result = JSONObject.parseObject(paymentI.toBeecloudPay(orderId,amount,paymentType,param));
					msg.setData(result);
				}else{
					
					String url = (String) JFig.getInstance().getSection("pay_options").get("WXRETURN_URL");
					url = url.replace("${key}", orderId);
					if("wftPay".equals(payType)){
						BigDecimal amount = (orderInfo.getPayAmount().subtract(orderInfo.getBalanceDeduction()));
						res.put("imgSrc", paymentI.toWFTPay(orderId,amount,paymentType,request));
					}else{
						BigDecimal amount = (orderInfo.getPayAmount().subtract(orderInfo.getBalanceDeduction())).multiply(new BigDecimal(100));
						res.put("imgSrc", paymentI.toBeecloudPay(orderId,amount,paymentType,null));
					}					
//					res.put("url", url);
					msg.setData(res);
				}
				orderService.editOrder(orderInfo);
			}else{
				throw new RuntimeException("订单不存在");
			}
		} catch (Exception e) {
			msg.setError(e);
		}finally{
			ToPayLogger toPayLogger = new ToPayLogger(orderId,JSONObject.toJSONString(msg),paymentType,isbalance,"/beeCloudSubmit");
//			MongoDao.save(toPayLogger, "toPayLogger");
		}
		return msg;
	}
	/**
	 * 网站提交
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
//	@ResponseBody
	@ApiOperation(value = "网站提交")
	public String submit(String paymentPluginId, String orderId,String isSaas, HttpServletRequest request, HttpServletResponse response,
			ModelMap model
			,boolean isbalance
			,String type,//0 订单支付 1 利息支付
			String bankCode
			) {
		ResponseMsg res = new ResponseMsg();
		if(null != type && "1".equals(type)){
			try {
				return toPayInterest(paymentPluginId,orderId,request,response,model);
			} catch (Exception e) {
				res.setError(e);
				return "/page/error";
			}
		}
		
		
		paymentI.updateIsBalance(isbalance,orderId);
/*		LoginInfo loginInfo = null;
		if(null != isSaas && "1".equals(isSaas)){
			loginInfo = MemberTools.isSaasLogin(request);
		}else{
			loginInfo = MemberTools.isLogin(request);
		}*/
/*		if (loginInfo == null) {
			res.setMsg("请先登录");
			res.setCode(-1);
			return "/page/error";
		}*/
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
		if (paymentPlugin == null) {
			res.setMsg("请选择支付方式");
			res.setCode(-1);
			return "/page/error";
		}
		
		Map returnMap = new HashMap();
		

//		returnMap.put("requestUrl", paymentPlugin.getRequestUrl());
//		returnMap.put("requestMethod", paymentPlugin.getRequestMethod());
//		returnMap.put("requestCharset", paymentPlugin.getRequestCharset());
		
		
//		Map<String, String> map = paymentPlugin.getParameterMap(orderId, amount, request);
//		// SDKUtil.sign(map, "UTF-8");
//		model.addAttribute("parameterMap", map);
////		returnMap.put("parameterMap", map);
		
		
		SysOptionsValue sysOptionsValue = sysOptionsService.getPaySysOption(4, paymentPluginId);
		if(null!=orderId){
			OrderInfo orderInfo = orderService.getOrderById(Long.valueOf(orderId));
			if(null!=orderInfo){
				try{
					if(!"1".equals(orderInfo.getOrderStatus())){
						res.setMsg("订单已支付");
						res.setCode(10001);
						return "redirect:"+repeatPay;
					}
					/***********保存付款单start**************/
					PaymentBillPage paymentBillPage = getPaymentBill(orderInfo);
					if("unionpayPlugin".equals(sysOptionsValue.getRemark())){
						paymentBillPage.setPayType("1");
						orderInfo.setPayType("1");
					}else if("alipayDirectPlugin".equals(sysOptionsValue.getRemark())){
						paymentBillPage.setPayType("3");
						orderInfo.setPayType("3");
					}else if("businessPlugin".equals(sysOptionsValue.getRemark())){
						paymentBillPage.setPayType("5");
						orderInfo.setPayType("5");
					}
					Map map = addPaymentBillService.findPayOrderById(Long.valueOf(orderId));
					if(null != map && !map.isEmpty() && !"0".equals(map.get("num").toString())){
						paymentBillPage.setId(map.get("payment_bill_id").toString());
						addPaymentBillService.updatePaymentBill(paymentBillPage);
					}else{
						addPaymentBillService.savePaymentBill(paymentBillPage);
					}
				}catch (Exception e) {
					res.setMsg(e.getMessage());
					res.setCode(-1);
					e.printStackTrace();
					return "/page/error";
				}
				/***********保存付款单end**************/
				if(orderInfo.getPayAmount().compareTo(orderInfo.getBalanceDeduction())==0){
					paymentI.saveOrderByBalance(orderId);
					String urlUpload = JFig.getInstance().getValue("system_options", "URL_UPLOAD","");
					return "redirect:"+urlUpload+"/pay/success/"+orderId;
				}else if("businessPlugin".equals(paymentPluginId)){
					model.addAttribute("requestUrl", paymentPlugin.getRequestUrl());
					model.addAttribute("requestMethod", paymentPlugin.getRequestMethod());
					model.addAttribute("requestCharset", paymentPlugin.getRequestCharset());
					BigDecimal pay = orderInfo.getPayAmount().subtract(orderInfo.getBalanceDeduction());
					BigDecimal hundred = new BigDecimal(100);
					Map<String, String> map = paymentPlugin.getParameterMap(orderId, pay.multiply(hundred).setScale(0, BigDecimal.ROUND_HALF_DOWN), request,null,null);
					model.addAttribute("parameterMap", map);
				}else if(("unionpayPlugin".equals(paymentPluginId) || "alipayDirectPlugin".equals(paymentPluginId))&&beecloud){
					try {
						orderInfo.setCompanyAccount(Integer.valueOf(sysOptionsValue.getOptValue()));
						orderService.editOrder(orderInfo);
						BigDecimal amount = (orderInfo.getPayAmount().subtract(orderInfo.getBalanceDeduction())).multiply(new BigDecimal(100));
						response.setContentType("text/html;charset=utf-8"); 
						PrintWriter out=response.getWriter();
						out.println(paymentI.toBeecloudPay(orderId,amount,paymentPluginId,null));
						return null;
					} catch (Exception e) {
						e.printStackTrace();
						return "/page/payment/submit";
					}
				}else{
					model.addAttribute("requestUrl", paymentPlugin.getRequestUrl());
					model.addAttribute("requestMethod", paymentPlugin.getRequestMethod());
					model.addAttribute("requestCharset", paymentPlugin.getRequestCharset());
					Map<String, String> otherMap = new HashMap<String, String>();
					if("CMBCPlugin".equals(paymentPluginId) && StringUtils.isNotBlank(bankCode)){
						otherMap.put("bankCode", bankCode);
					}
					Map<String, String> map = paymentPlugin.getParameterMap(orderId, orderInfo.getPayAmount().subtract(orderInfo.getBalanceDeduction()), request,null,otherMap);
					model.addAttribute("parameterMap", map);
				}
				orderInfo.setCompanyAccount(Integer.valueOf(sysOptionsValue.getOptValue()));
				orderService.editOrder(orderInfo);
			}
		}
		try{
			ToPayLogger toPayLogger = new ToPayLogger(orderId,JSONObject.toJSONString(model.get("parameterMap")),paymentPluginId,isbalance,"/beeCloudSubmit");
//			MongoDao.save(toPayLogger, "toPayLogger");
		}catch (Exception e) {
			e.printStackTrace();
		}
		if (StringUtils.isNotEmpty(paymentPlugin.getRequestCharset())) {
			response.setContentType("text/html; charset=" + paymentPlugin.getRequestCharset());
		}
		return "/page/payment/submit";
//		res.setCode(0);
//		res.setData(returnMap);
//		return res;
	}


	private String toPayInterest(String paymentPluginId, String orderId,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
		Map<String, Object> data = orderService.findOrderInterest(Long.parseLong(orderId));
		if (paymentPlugin == null) {
			throw new Exception("支付方式不正确");
		}
		if(data==null){
			throw new Exception("利息结算订单不存在");
		}
		model.addAttribute("requestUrl", paymentPlugin.getRequestUrl());
		model.addAttribute("requestMethod", paymentPlugin.getRequestMethod());
		model.addAttribute("requestCharset", paymentPlugin.getRequestCharset());
		Map<String, String> otherMap =new HashMap<>();
		otherMap.put("notify_url", Constants.BASE_URL_NET + "gdbmro_serviceApi/payment/interest/notify/" + orderId);
//		Map<String, Object>  interest= goldMantisServiceI.getOrdernterest(orderId, data.get("dealerId").toString());
//		orderService.saveOrderInterest(interest,orderId);
//		BigDecimal returnInterest = new BigDecimal(interest.get("returnInterest").toString());
//		if(returnInterest.compareTo(BigDecimal.ZERO)<=0){
//			String saas =JFig.getInstance().getSection("system_options").get("netReferer");
//			goldMantisServiceI.toOrderDeal(orderId, data.get("dealerId").toString());
//			orderService.updateOrderInterest(Long.parseLong(orderId));
//			return "redirect:"+ saas+"/buyer/goldMantis/index";
//		}
//		Map<String, String> map = paymentPlugin.getParameterMap(orderId,returnInterest, request,null,otherMap);//new BigDecimal(data.get("interest").toString())
//		model.addAttribute("parameterMap", map);
		return "/page/payment/submit";
	}


	/**
	 * 利息通知
	 * @throws Exception 
	 */
	@RequestMapping("/interest/notify/{orderId}")
	@ApiOperation(value = "利息通知")
//	@ResponseBody
	public String toInterestNotify(@PathVariable Long orderId, HttpServletRequest request) throws Exception {
		System.out.println("in payment:"+orderId);
		String tradeNo = request.getParameter("trade_no");
		System.out.println("trade_no:"+tradeNo);
		Map<String, Object> data = orderService.findOrderInterest(orderId);
		if(data!=null){
//			goldMantisServiceI.toOrderDeal(orderId.toString(), data.get("dealerId").toString());
			orderService.updateOrderInterest(orderId);
			request.setAttribute("notifyMessage", "success");
		}
		return "/page/payment/notify";
//		return paymentPlugin.getNotifyMessage(request);
	}
	/**
	 * 通知
	 */
	@RequestMapping(value="/notify/{orderId}",method = RequestMethod.POST)
	@ApiOperation(value = "支付通知")
	@ResponseBody
	public String notify(@PathVariable Long orderId, HttpServletRequest request) {
		
 		log.info("in payment:"+orderId);
		String strOrderId = orderId.toString();
		Long afterSubOrderId = Long.parseLong(strOrderId.substring(0, strOrderId.length()-3));
		String ip = IpUtil.getIpAddr(request);
		log.info("request ip:"+ip);
		if(!StringUtils.equals("123.57.146.46", ip) && !StringUtils.equals("182.92.114.175", ip)){
			return "faild";
		}
		String tradeNo = request.getParameter("trade_no");
		log.info("trade_no:"+tradeNo);
		OrderInfo orderInfo = orderService.getOrderById(afterSubOrderId);
		if (orderInfo == null) {
			return "success";
		}
		log.info("orderStatus:"+orderInfo.getOrderStatus());
		
		SysOptionsValue sysOptionsValue = sysOptionsService.findPaySysOption(orderInfo.getCompanyAccount(),4);
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(sysOptionsValue.getRemark());
//		try {
//			ToPayLogger toPayLogger = new ToPayLogger(
//						String.valueOf(orderId),
//						JSONObject.toJSONString(request.getParameterMap()),
//						sysOptionsValue.getRemark(),
//						false,
//						"/notify"
//					).setType(1);
////			MongoDao.save(toPayLogger, "toPayLogger");
//		} catch (Exception e) { 
//			e.printStackTrace();
//		}
		if (paymentPlugin != null) {
			log.info("in paymentPlugin:"+orderId);
			if(null!=orderInfo.getOrderStatus() && "1".equals(orderInfo.getOrderStatus())){//待付款的订单才修改状态
				if (paymentPlugin.verifyNotify(orderId.toString(), request)) {
					log.info("in verifyNotify:"+orderId);
//				orderInfo.setPayTime(new Date());
//				orderInfo.setPayType("2");//全额付款
//				orderInfo.setOrderStatus("2");//待发货
//				orderInfo.setPayStatus("2");//全额付款
//				orderService.editOrder(orderInfo);
					
					
					/***********更新付款单start**************/
					PaymentBillPage paymentBillPage = new PaymentBillPage();
					paymentBillPage.setRealAmount(orderInfo.getPayAmount()==null?0 : orderInfo.getPayAmount().doubleValue());
					paymentBillPage.setPayStatus("1");
					paymentBillPage.setBankReceiveTime(new Timestamp(System.currentTimeMillis()));
					paymentBillPage.setPayTime(new Timestamp(System.currentTimeMillis()));
					paymentBillPage.setTradeNo(tradeNo);
					Map map = addPaymentBillService.findPayOrderById(Long.valueOf(afterSubOrderId));
					if(null != map && !map.isEmpty() && !"0".equals(map.get("num").toString())){
						paymentBillPage.setId(map.get("payment_bill_id").toString());
						addPaymentBillService.updatePaymentBill(paymentBillPage);
					}
					/***********更新付款单end**************/
					
					orderService.editHandleOrder(orderInfo);
					if("9".equals(orderInfo.getPayType())|| "3".equals(orderInfo.getPayType())){
						JSONObject json = new JSONObject(2);
						json.put("key", afterSubOrderId.toString());
						json.put("value", "success");
						json.put("isHold", "0");
//						mqSender.send(json);
					}
				}
				
				/**更新客户等级*/
				crmCustomerMapper.updateCustomerRank(orderInfo.getBuyerId());
			}
			System.out.println("notifyMessage:"+paymentPlugin.getNotifyMessage(request));
			request.setAttribute("notifyMessage", paymentPlugin.getNotifyMessage(request));
		}

//		return "/page/payment/notify";
		return paymentPlugin.getNotifyMessage(request);
	}
	
	
	/**
	 * APP提交
	 */
	@RequestMapping(value = "/appSubmit", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "APP提交")
	public ResponseMsg appSubmit(String paymentPluginId, String orderId, HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		ResponseMsg res = new ResponseMsg();
		paymentI.updateIsBalance(false,orderId);
//		LoginInfo loginInfo = MemberTools.isLogin(request);
//		if (loginInfo == null) {
//			res.setMsg("请先登录");
//			res.setCode(-1);
//			return res;
//		}
		/*PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
		if (paymentPlugin == null) {
			res.setMsg("请选择支付方式");
			res.setCode(-1);
			return res;
		}
		
		
		SysOptionsValue sysOptionsValue = sysOptionsService.getPaySysOption(4, paymentPluginId);
		if(null!=orderId){
			OrderInfo orderInfo = orderService.getOrderById(Long.valueOf(orderId));
			if(null!=orderInfo){
				*//***********保存付款单start**************//*
				int r1=(int)(Math.random()*(10));//产生4个0-9的随机数
				int r2=(int)(Math.random()*(10));
				int r3=(int)(Math.random()*(10));
				int r4=(int)(Math.random()*(10));
				long now = System.currentTimeMillis();//一个13位的时间戳
				String paymentID =String.valueOf(r1)+String.valueOf(r2)+String.valueOf(r3)+String.valueOf(r4)+String.valueOf(now);// 订单ID
				PaymentBillPage paymentBillPage = new PaymentBillPage();
				paymentBillPage.setId(paymentID);
				paymentBillPage.setOrderId(Long.valueOf(orderId));
				paymentBillPage.setBuyerId(Integer.valueOf(orderInfo.getBuyerId()));
				paymentBillPage.setShouldAmount(orderInfo.getPayShouldAmount()==null?0 : orderInfo.getPayShouldAmount().doubleValue());
				paymentBillPage.setRealAmount(orderInfo.getPayAmount()==null?0 : orderInfo.getPayAmount().doubleValue());
				paymentBillPage.setPaymentScoreAmount(null==orderInfo.getScoreDeductionAmout()?0:orderInfo.getScoreDeductionAmout().doubleValue());
				paymentBillPage.setPayStatus("0");
				if("unionpayPlugin".equals(sysOptionsValue.getRemark())){
					paymentBillPage.setPayType("1");
				}else if("alipayDirectPlugin".equals(sysOptionsValue.getRemark())){
					paymentBillPage.setPayType("3");
				}
				paymentBillPage.setStatus("1");
				paymentBillPage.setPayTime(new Timestamp(System.currentTimeMillis()));
				
				try{
					Map map = addPaymentBillService.findPayOrderById(Long.valueOf(orderId));
					if(null != map && !map.isEmpty() && !"0".equals(map.get("num").toString())){
						paymentBillPage.setId(map.get("payment_bill_id").toString());
						addPaymentBillService.updatePaymentBill(paymentBillPage);
					}else{
						addPaymentBillService.savePaymentBill(paymentBillPage);
					}
				}catch (Exception e) {
					res.setMsg(e.getMessage());
					res.setCode(-1);
					e.printStackTrace();
					return res;
				}
				*//***********保存付款单end**************//*
				Map<String, String> map = paymentPlugin.getParameterMap(orderId, orderInfo.getPayAmount().subtract(orderInfo.getBalanceDeduction()), request);
				// SDKUtil.sign(map, "UTF-8");
				model.addAttribute("parameterMap", map);
				
				
				orderInfo.setCompanyAccount(Integer.valueOf(sysOptionsValue.getOptValue()));
				orderService.editOrder(orderInfo);
				res.setCode(0);
				return res;
			}
		}*/
		
		
		
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
		if (paymentPlugin == null) {
			res.setMsg("请选择支付方式");
			res.setCode(-1);
			return res;
		}
		
		
		SysOptionsValue sysOptionsValue = sysOptionsService.getPaySysOption(4, paymentPluginId);
		if(null!=orderId){
			OrderInfo orderInfo = orderService.getOrderById(Long.valueOf(orderId));
			if(null!=orderInfo){
				/***********保存付款单start**************/
				PaymentBillPage paymentBillPage = getPaymentBill(orderInfo);
				if("unionpayPlugin".equals(sysOptionsValue.getRemark())){
					paymentBillPage.setPayType("1");
				}else if("alipayDirectPlugin".equals(sysOptionsValue.getRemark())){
					paymentBillPage.setPayType("3");
				}

				
				try{
					Map map = addPaymentBillService.findPayOrderById(Long.valueOf(orderId));
					if(null != map && !map.isEmpty() && !"0".equals(map.get("num").toString())){
						paymentBillPage.setId(map.get("payment_bill_id").toString());
						addPaymentBillService.updatePaymentBill(paymentBillPage);
					}else{
						addPaymentBillService.savePaymentBill(paymentBillPage);
					}
				}catch (Exception e) {
					res.setMsg(e.getMessage());
					res.setCode(-1);
					e.printStackTrace();
					return res;
				}
				/***********保存付款单end**************/
				
				Map<String, String> map = paymentPlugin.getParameterMap(orderId, orderInfo.getPayAmount(), request,null,null);
				// SDKUtil.sign(map, "UTF-8");
				System.out.println("APPmap:"+map);
				orderInfo.setCompanyAccount(Integer.valueOf(sysOptionsValue.getOptValue()));
				orderService.editOrder(orderInfo);
			}
		}
        res.setCode(0);
		return res;
	}
	
	/**
	 * 保存支付宝转账信息
	 * @author felx
	 * @param 
	 * @return
	 */
	@RequestMapping(value="/updateOrderAlipay", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "保存支付宝转账信息")
	public ResponseMsg updateOrderAlipay(HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			msg.setMsg("请先登录");
			msg.setCode(-1);
			return msg;
		}
		try {
			int m = orderService.updateOrderAlipay(request,loginInfo);
			if(m>0){
				msg.setCode(0);
				msg.setMsg("提交成功");
			}else{
				msg.setCode(-1);
				msg.setMsg("提交失败");
			}
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value="/pay",method=RequestMethod.POST)
	@ApiOperation(value = "利息支付")
	public String pay(String paymentPluginId, String orderId,String isSaas, HttpServletRequest request, HttpServletResponse response,
		ModelMap model
		,boolean isbalance
		,int type//0 订单支付 1 利息支付
		){
		ResponseMsg res = new ResponseMsg();
		if(type==1){
			try {
				return toPayInterest(paymentPluginId,orderId,request,response,model);
			} catch (Exception e) {
				res.setError(e);
				return "/page/error";
			}
		}
		if (paymentPluginId == null || StringUtils.isBlank(orderId)) {
			res.setMsg("参数不能为空");
			res.setCode(-1);
			return "/page/error";
		}
		Map returnMap = new HashMap();
		SysOptionsValue sysOptionsValue = sysOptionsService.getPaySysOption(4, paymentPluginId);
		OrderInfo orderInfo = orderService.getOrderById(Long.valueOf(orderId));
		if(orderInfo==null){
			return "/page/error";
		}
		/***********保存付款单start**************/
		PaymentBillPage paymentBillPage = getPaymentBill(orderInfo);
		if("unionpayPlugin".equals(sysOptionsValue.getRemark())){
			paymentBillPage.setPayType("1");
			orderInfo.setPayType("1");
		}else if("alipayDirectPlugin".equals(sysOptionsValue.getRemark())){
			paymentBillPage.setPayType("3");
			orderInfo.setPayType("3");
		}else if("businessPlugin".equals(sysOptionsValue.getRemark())){
			paymentBillPage.setPayType("5");
			orderInfo.setPayType("5");
		}
		try{
			Map map = addPaymentBillService.findPayOrderById(Long.valueOf(orderId));
			if(null != map && !map.isEmpty() && !"0".equals(map.get("num").toString())){
				paymentBillPage.setId(map.get("payment_bill_id").toString());
				addPaymentBillService.updatePaymentBill(paymentBillPage);
			}else{
				addPaymentBillService.savePaymentBill(paymentBillPage);
			}
		}catch (Exception e) {
			res.setMsg(e.getMessage());
			res.setCode(-1);
			e.printStackTrace();
			return "/page/error";
		}
		/***********保存付款单end**************/
		orderInfo.setCompanyAccount(Integer.valueOf(sysOptionsValue.getOptValue()));
		orderService.editOrder(orderInfo);
		if(orderInfo.getPayAmount().compareTo(orderInfo.getBalanceDeduction())==0){
			paymentI.saveOrderByBalance(orderId);
			String urlUpload = JFig.getInstance().getValue("system_options", "URL_UPLOAD","");
			return "redirect:"+urlUpload+"/pay/success/"+orderId;
		}
		try {
			return paymentI.toBeecloudPay(orderId,new BigDecimal(100),paymentPluginId,null);
		} catch (Exception e) {
			res.setError(e);
			return "/page/error";
		}
		}
	

	@RequestMapping(value="/jtl",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "金螳螂支付")
	public ResponseMsg toPayJTL(String orderId,HttpServletRequest request, HttpServletResponse response,
			ModelMap model
			,boolean isbalance,
			String accountPeriod
			){
		PageMode mode = new PageMode(request);
		ResponseMsg msg = new ResponseMsg();
		try {
			paymentI.updateIsBalance(false,orderId);
			OrderInfo orderInfo = orderService.getOrderById(Long.valueOf(orderId));
			if(null!=orderInfo){
				/***********保存付款单start**************/
				PaymentBillPage paymentBillPage = getPaymentBill(orderInfo);
				paymentBillPage.setPayType("7");
					Map map = addPaymentBillService.findPayOrderById(Long.valueOf(orderId));
					if(null != map && !map.isEmpty() && !"0".equals(map.get("num").toString())){
						paymentBillPage.setId(map.get("payment_bill_id").toString());
						addPaymentBillService.updatePaymentBill(paymentBillPage);
					}else{
						addPaymentBillService.savePaymentBill(paymentBillPage);
					}
					String dealerId =mode.getDealerId(Channel.SAAS);
//					goldMantisServiceI.toOrderApply(orderId, dealerId,accountPeriod);
				}else{
					throw new Exception("订单不存在");
				}
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	@RequestMapping(value="/weixin",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "微信支付")
	public ResponseMsg toPayWeixin(String orderId,HttpServletRequest request, HttpServletResponse response,
			ModelMap model
			,boolean isbalance
			){
		ResponseMsg msg = new ResponseMsg();
		final String PAY_TYPE = "6";
		try {
			paymentI.updateIsBalance(isbalance,orderId);
			PaymentPlugin paymentPlugin = new WeixinPlugin();
			if(null!=orderId){
				OrderInfo orderInfo = orderService.getOrderById(Long.valueOf(orderId));
				if(null!=orderInfo){
					/***********保存付款单start**************/
					PaymentBillPage paymentBillPage = getPaymentBill(orderInfo);
					paymentBillPage.setPayType(PAY_TYPE);
					orderInfo.setPayType(PAY_TYPE);
					Map map = addPaymentBillService.findPayOrderById(Long.valueOf(orderId));
					if(null != map && !map.isEmpty() && !"0".equals(map.get("num").toString())){
						paymentBillPage.setId(map.get("payment_bill_id").toString());
						addPaymentBillService.updatePaymentBill(paymentBillPage);
					}else{
						addPaymentBillService.savePaymentBill(paymentBillPage);
					}
					/***********保存付款单end**************/
					BigDecimal pay = orderInfo.getPayAmount().subtract(orderInfo.getBalanceDeduction());
					BigDecimal hundred = new BigDecimal(100);
					String openId = dealerMapper.getOpenId(orderId);
					Map<String, String> data = paymentPlugin.getParameterMap(orderId, pay.multiply(hundred).setScale(0, BigDecimal.ROUND_HALF_DOWN), request,openId,null);
					orderInfo.setCompanyAccount(7);
					orderService.editOrder(orderInfo);
					if(data!=null){
						Map<String, Object> wxData  = new HashMap<>();
						wxData.put("appId", data.get("appid"));
						wxData.put("timeStamp", new Date().getTime()/1000);
						wxData.put("nonceStr", data.get("nonce_str"));
						wxData.put("package", "prepay_id="+data.get("prepay_id"));
						wxData.put("signType", "MD5");
						String sign = Signature.getSign(wxData);
						wxData.put("paySign", sign);
						msg.setData(wxData);
					}
				}else{
					throw new Exception("订单支付失败");
				}
			}
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}

	/**
	 * 获取请求参数中所有的信息
	 * 
	 * @param request
	 * @param encoding 
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, String> getAllRequestParam(final HttpServletRequest request, String encoding) throws Exception {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = new String(request.getParameter(en).getBytes(encoding), encoding);
				res.put(en, value);
				//在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				//System.out.println("ServletUtil类247行  temp数据的键=="+en+"     值==="+value);
				if (null == res.get(en) || "".equals(res.get(en))) {
					res.remove(en);
				}
			}
		}
		return res;
	}

	@RequestMapping(value="/saas/offline",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "saas线下")
	public ResponseMsg toPayByBalance(HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		PageMode model = new PageMode(request);
		try {
			msg.setData(paymentI.toPayByOffline(model,model.getLogin()));
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 *	@author jxw
	 *	@description 获取支付使用信息
	 * @param orderId 订单号
	 * @param isUse 是否使用余额
	 * @param loginInfo 登录信息
	 * @return
	 */
	@RequestMapping(value="/app/toPayInfo",method=RequestMethod.GET)
	@ResponseBody
	@ResponseDefault
	@ApiOperation(value = "支付详情")
	@Login(app="/app/toPayInfo")
	public Object toPayByBalance(
			@ApiParam(value="订单号")@RequestParam(required=false) String orderId,
			@ApiParam(value="是否使用余额")@RequestParam(required=false) String isUse,
			LoginInfoUtil loginInfo){		
		return paymentI.toAppPay(orderId,loginInfo,isUse);
	}
	/**
	 *	@author jxw
	 *	@description 获取支付使用信息
	 * @param orderId 订单号
	 * @param isUse 是否使用余额
	 * @param loginInfo 登录信息
	 * @return
	 */
	@RequestMapping(value="/app/pay",method=RequestMethod.POST)
	@ResponseBody
	@ResponseDefault
	@ApiOperation(value = "App支付获取信息（余额支付）")
	@Login(app="/app/pay")
	public ResponseMsg toAppPayByBalance(
			@ApiParam(value="订单号")@RequestParam(required=false) String orderId,
			@ApiParam(value="是否使用余额")@RequestParam(required=false) String isUse,
			LoginInfoUtil loginInfo){		
		ResponseMsg msg = new ResponseMsg();
		try {
			BigDecimal amount = paymentI.toAppPayByBalance(orderId,loginInfo,isUse);
			if(BigDecimal.ZERO.compareTo(amount)==0){
				msg.setData(0);
				msg.setCode(10001);
			}else{
				msg.setData(amount);
			}
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 通过Order获得PaymentBill
	 * @param orderInfo
	 * @return
	 */
	private PaymentBillPage getPaymentBill(OrderInfo orderInfo) {
		int r1=(int)(Math.random()*(10));//产生4个0-9的随机数
		int r2=(int)(Math.random()*(10));
		int r3=(int)(Math.random()*(10));
		int r4=(int)(Math.random()*(10));
		long now = System.currentTimeMillis();//一个13位的时间戳
		String paymentID =String.valueOf(r1)+String.valueOf(r2)+String.valueOf(r3)+String.valueOf(r4)+String.valueOf(now);// 订单ID
		PaymentBillPage paymentBillPage = new PaymentBillPage();
		paymentBillPage.setId(paymentID);
		paymentBillPage.setOrderId(orderInfo.getId());
		paymentBillPage.setBuyerId(Integer.valueOf(orderInfo.getBuyerId()));
		paymentBillPage.setShouldAmount(orderInfo.getPayShouldAmount()==null?0 : orderInfo.getPayShouldAmount().doubleValue());
		paymentBillPage.setRealAmount(orderInfo.getPayAmount()==null?0 : orderInfo.getPayAmount().doubleValue());
		paymentBillPage.setPaymentScoreAmount(null==orderInfo.getScoreDeductionAmout()?0:orderInfo.getScoreDeductionAmout().doubleValue());
		paymentBillPage.setPayStatus("0");
		paymentBillPage.setStatus("1");
		paymentBillPage.setPayTime(new Timestamp(System.currentTimeMillis()));
		paymentBillPage.setBalanceDeduction(orderInfo.getBalanceDeduction());
		return paymentBillPage;
	}


	public static void main(String[] var){
		/*String appSecret = "a5f8fe5k59eb0c6534787b6d1a739192";
		TreeMap<String, String> paramsMap = new TreeMap<String, String>();
		paramsMap.put("bName", "lihong1");
		paramsMap.put("aName", "lihong2");
		paramsMap.put("fName", "lihong3");
		paramsMap.put("gName", "lihong0");
		paramsMap.put("dName", "lihong4");
		paramsMap.put("cName", "lihong5");

		// 调用SDK方法，获得参数签名值
		String signValue = ParamsSign.value(paramsMap,appSecret);
		System.out.println("vlue of sign result is : "+signValue);*/
		
		System.out.println(System.currentTimeMillis());
	}
}