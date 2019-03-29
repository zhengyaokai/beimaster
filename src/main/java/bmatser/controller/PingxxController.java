package bmatser.controller;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pingplusplus.model.Event;
import com.pingplusplus.model.Webhooks;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import bmatser.model.OrderInfo;
import bmatser.model.SysOptionsValue;
import bmatser.pageModel.PaymentBillPage;
import bmatser.plugin.pingxx.PingxxCharge;
import bmatser.service.AddPaymentBillServiceI;
import bmatser.service.OrderInfoI;
import bmatser.service.PaymentServiceI;
import bmatser.service.SysOptionsServiceI;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;

@Controller
@RequestMapping("pingxx")
@Api(value="pingxx", description="手机支付")
public class PingxxController {
	
	@Autowired
	private PingxxCharge pingCharge;
	
	@Autowired
	private OrderInfoI orderService; 
	
	@Autowired
	private AddPaymentBillServiceI addPaymentBillService;
	
	@Autowired
	private SysOptionsServiceI sysOptionsService;
	
	@Autowired
	private PaymentServiceI paymentI; 
	
	/**
	 * 创建charge
	 * @author felx
	 */
	@RequestMapping(value = "/charge", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "创建charge", 
	notes = "创建charge")
	public ResponseMsg charge(String orderId,String channel,HttpServletRequest request, HttpServletResponse response) {
		ResponseMsg msg = new ResponseMsg();
		paymentI.updateIsBalance(false,orderId);
		try {
			String paymentPluginId = "";
			if(null != channel && "upacp".equals(channel)){
				paymentPluginId = "unionpayPlugin";
			}else if(null != channel && "alipay".equals(channel)){
				paymentPluginId = "alipayDirectPlugin";
			}
			SysOptionsValue sysOptionsValue = sysOptionsService.getPaySysOption(4, paymentPluginId);
			/***********保存付款单start**************/
			if(null!=orderId){
				OrderInfo orderInfo = orderService.getOrderById(Long.valueOf(orderId));
				if(null!=orderInfo){
					
					int r1=(int)(Math.random()*(10));//产生4个0-9的随机数
					int r2=(int)(Math.random()*(10));
					int r3=(int)(Math.random()*(10));
					int r4=(int)(Math.random()*(10));
					long now = System.currentTimeMillis();//一个13位的时间戳
					String paymentID =String.valueOf(r1)+String.valueOf(r2)+String.valueOf(r3)+String.valueOf(r4)+String.valueOf(now);// 订单ID
					PaymentBillPage paymentBillPage = new PaymentBillPage();
					paymentBillPage.setId(paymentID);
					paymentBillPage.setOrderId(Long.valueOf(orderId));
					paymentBillPage.setBuyerId(orderInfo.getBuyerId());
					paymentBillPage.setShouldAmount(orderInfo.getPayShouldAmount()==null?0 : orderInfo.getPayShouldAmount().doubleValue());
					paymentBillPage.setRealAmount(orderInfo.getPayAmount()==null?0 : orderInfo.getPayAmount().doubleValue());
//					paymentBillPage.setScoreDeductionAmout(null==orderInfo.getScoreDeductionAmout()?new BigDecimal(0):orderInfo.getScoreDeductionAmout());
					paymentBillPage.setPaymentScoreAmount(null==orderInfo.getScoreDeductionAmout()?0:orderInfo.getScoreDeductionAmout().doubleValue());
					paymentBillPage.setCompanyAccount(sysOptionsValue.getOptValue());
					paymentBillPage.setPayStatus("0");
					if("upacp".equals(channel)){//银联
						paymentBillPage.setPayType("1");
						orderInfo.setPayType("1");
					}else if("alipay".equals(channel)){//支付宝
						paymentBillPage.setPayType("3");
						orderInfo.setPayType("3");
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
						msg.setCode(-1);
						msg.setMsg(e.getMessage());
						e.printStackTrace();
					}
					
					orderInfo.setCompanyAccount(Integer.valueOf(sysOptionsValue.getOptValue()));
					orderService.editOrder(orderInfo);
				}
			}
			/***********保存付款单end**************/
			
			
			msg.setData(pingCharge.charge(orderId,channel));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.getMessage());
		}
		
		return msg;
	}
	
	@RequestMapping(value = "/notify", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "app通知", 
	notes = "app通知")
	public int notify(HttpServletRequest request, HttpServletResponse response) {
		try{
			request.setCharacterEncoding("UTF8");
	        //获取头部所有信息
	        Enumeration headerNames = request.getHeaderNames();
	        while (headerNames.hasMoreElements()) {
	            String key = (String) headerNames.nextElement();
	            String value = request.getHeader(key);
	            System.out.println(key+" "+value);
	        }
	        // 获得 http body 内容
	        BufferedReader reader = request.getReader();
	        StringBuffer buffer = new StringBuffer();
	        String string;
	        while ((string = reader.readLine()) != null) {
	            buffer.append(string);
	        }
	        reader.close();
	        // 解析异步通知数据
	        Event event = Webhooks.eventParse(buffer.toString());
	        System.out.println("event:"+event);
	        if ("charge.succeeded".equals(event.getType())) {
//	            response.setStatus(200);
	        	Map map = event.getData();
	        	System.out.println("---map:"+map);
	        	if(null!=map){
	        		Map obj = (Map)map.get("object");
	        		System.out.println("---obj:"+obj);
	        		if(null!=obj){
	        			String orderId = obj.get("order_no")==null?null:obj.get("order_no").toString();
	        			if(null!=orderId && !"".equals(orderId)){
	        				System.out.println("---orderId"+orderId);
	        				OrderInfo orderInfo = orderService.getOrderById(Long.valueOf(orderId));
	        				if(null!=orderInfo.getOrderStatus() && "1".equals(orderInfo.getOrderStatus())){//待付款的订单才修改状态
	        					/***********更新付款单start**************/
	        					PaymentBillPage paymentBillPage = new PaymentBillPage();
	        					paymentBillPage.setRealAmount(orderInfo.getPayAmount()==null?0 : orderInfo.getPayAmount().doubleValue());
	        					paymentBillPage.setBankReceiveTime(new Timestamp(System.currentTimeMillis()));
	        					paymentBillPage.setPayStatus("1");
	        					paymentBillPage.setPayTime(new Timestamp(System.currentTimeMillis()));
	        					Map billMap = addPaymentBillService.findPayOrderById(Long.valueOf(orderId));
	        					if(null != billMap && !billMap.isEmpty() && !"0".equals(billMap.get("num").toString())){
	        						paymentBillPage.setId(billMap.get("payment_bill_id").toString());
	        						addPaymentBillService.updatePaymentBill(paymentBillPage);
	        					}
	        					/***********更新付款单end**************/
	        					System.out.println("----orderInfo:"+orderInfo);
	        					orderService.editHandleOrder(orderInfo);
	        				}
	        				return 200;
	        			}
	        		}
	        		return 500;
	        	}else{
	        		return 500;
	        	}
	        } else if ("refund.succeeded".equals(event.getType())) {
//	            response.setStatus(200);
	        	return 200;
	        } else {
//	            response.setStatus(500);
	        	return 500;
	        }
		}catch (Exception e) {
			e.printStackTrace();
		}
		return 500;
	}
	
}
