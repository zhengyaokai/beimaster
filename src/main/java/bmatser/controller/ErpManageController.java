package bmatser.controller;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import bmatser.annotations.ResponseDefault;
import bmatser.model.OrderToERP;
import bmatser.pageModel.OrderInfoErpPage;
import bmatser.pageModel.PageMode;
import bmatser.service.ErpManageI;
import bmatser.util.ErpConnect;
import bmatser.util.ResponseMsg;
import net.rubyeye.xmemcached.MemcachedClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping(value="/erp")
@Api(value="erp", description="E助")
public class ErpManageController {
	
	@Autowired
	private ErpManageI erpManageI;
	@Autowired
	private MemcachedClient memcachedClient;
//	@Autowired
//	private InterfaceLogI interfaceLogI;
	/**
	 * 同步订单到ERP中
	 * @param id 订单号
	 * @param session 保存ERP KEY
	 * @return 同步ID
	 */
	@RequestMapping(value="/addOrder/{id}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "同步订单到ERP中")
	public Map<String, Object> saveOrderToERP(@ApiParam(value="订单id") @PathVariable("id") String id,HttpSession session,HttpServletResponse resp){
		resp.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			String fid = erpManageI.saveOrderToERP(id);
			if(StringUtils.isNotBlank(fid)){
				map.put("code", 0);
				map.put("msg", fid);
			}else{
				map.put("code", -1);
				map.put("msg", "推送失败!");
			}
		} catch (Exception e) {
			map.put("code", -1);
			map.put("msg", "推送失败!"+e.getMessage());
		}
		return map;
	}
	
	
	@RequestMapping(value="/addGood", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "同步产品到ERP中")
	public Map<String, Object> saveGoodToERP(HttpSession session,HttpServletResponse resp){
		resp.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			erpManageI.saveGoodToERP();
			map.put("code", 0);
			map.put("msg", "success");
		} catch (Exception e) {
			map.put("code", -1);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
	
	@RequestMapping(value="/addCustomer/{dealerId}", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "同步客户到ERP中")
	public Map<String, Object> saveCustomerToERP(@PathVariable("dealerId") String dealerId,HttpSession session,HttpServletResponse resp){
		resp.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			Map<String,Object> customerMap = erpManageI.saveCustomerToERP(dealerId,new OrderToERP());
			if(customerMap.get("gclient")!=null && StringUtils.isNotBlank(customerMap.get("gclient").toString())){
				map.put("code", 0);
				map.put("msg", "success");
			}else{
				map.put("code", -1);
				map.put("msg", "not found crm record");
			}
		} catch (Exception e) {
			map.put("code", -1);
			map.put("msg", e.getMessage());
		}
		return map;
	}

	/**
	 * 查询ERP订单
	 * @param erpOrderNo
	 * @param session
	 * @param resp
	 * @return
	 */
	@RequestMapping(value="/queryOrder/{erpOrderNo}")
	@ResponseBody
	@ApiOperation(value = "查询ERP订单")
	public Map<String, Object> queryERPOrder(@PathVariable("erpOrderNo") String erpOrderNo,HttpSession session,HttpServletResponse resp){
		resp.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> map = new HashMap<String, Object>();
		long t1 = System.currentTimeMillis();
		try{
			String erpOrderInfo = erpManageI.queryERPOrder(erpOrderNo);
			if(StringUtils.isNotBlank(erpOrderInfo)){
				map.put("code", 0);
				map.put("data", erpOrderInfo);
				map.put("msg", "success");
			}else{
				map.put("code", -1);
				map.put("msg", "not found crm record");
			}
		} catch (Exception e) {
			map.put("code", -1);
			map.put("msg", e.getMessage());
		}
		return map;
	}

	/**
	 * 查询ERP订单出库情况
	 * @param erpOrderNo
	 * @param session
	 * @param resp
	 * @return
	 */
	@RequestMapping(value="/queryOrderOut/{erpOrderNo}")
	@ResponseBody
	@ApiOperation(value = "查询ERP订单，出库数量等，并更新GDB订单")
	public Map<String, Object> queryERPOrderOut(@PathVariable("erpOrderNo") String erpOrderNo,HttpSession session,HttpServletResponse resp){
		resp.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map = erpManageI.updateOrderLogis(erpOrderNo);
		} catch (Exception e) {
			map.put("code", -1);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
	
	/**
	 * 获取sessionKey
	 * @return
	 * @throws Exception
	 */
	private String getSessionKey() throws Exception {
		String sessionKey =memcachedClient.get("EZHU_KEY");
		if(StringUtils.isBlank(sessionKey)){
			sessionKey = ErpConnect.getSession();
			memcachedClient.set("EZHU_KEY", 0, sessionKey);
		}
		if(StringUtils.isBlank(sessionKey)){
			throw new Exception("sessionKey不存在");
		}
		return sessionKey;
	}

	/**
	 * 修改订单状态
	 * @param order 订单对象
	 * @return 
	 */
	@RequestMapping(value="/morderedit", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "同修改订单状态")
	public Map<String, Object> updateOrder(@ModelAttribute OrderInfoErpPage order,HttpSession session){
		Map<String, Object> map = new HashMap<String, Object>();
		String sessionKey = null;
		try {
			sessionKey = getSessionKey();
			erpManageI.updateOrder(order);
			map.put("code", 0);
			map.put("msg", "推送成功");
			//erpManageI.saveOrderToERP(id,sessionKey);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", -1);
			map.put("msg", "推送失败:"+e.toString());
		}
//		//接口调用时保存日志到mongodb的InterfaceLog表
//		interfaceLogI.saveLogToMongoDB("修改订单状态",
//				Constants.BASE_URL_NET+"/erp/morderedit",
//				"updateOrder",JSONObject.toJSON(order).toString(),
//				"erp","vipmro",map.get("code").toString(),map.get("msg").toString());
		return map;
	}
	/**
	 * 取消E助订单
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/cancelOrder", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "取消E助推送标志")
	public ResponseMsg cancelOrder(String id){
		ResponseMsg msg = new ResponseMsg();
		try {
			erpManageI.cancelOrder(id);
		} catch (Exception e) {
			msg.setError(e);
		}
//		interfaceLogI.saveLogToMongoDB("E助关闭订单",
//				Constants.BASE_URL_NET+"/erp/cancelOrder",
//				"cancelOrder",id,
//				"erp","vipmro",String.valueOf(msg.getCode()),msg.getMsg());
		return msg;
	}
	
	/**
	 * 获取库存(单个)
	 * @param id 订货号
	 * @return
	 */
	@RequestMapping(value="/goodstock/{id}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询单个商品库存")
	@Deprecated
	public Map<String, Object> getGoodsStock(@ApiParam(value="同步码或订货号")@PathVariable("id") String id){
		Map<String, Object> map = new HashMap<String, Object>();
		String sessionKey = null;
		try{
			sessionKey = getSessionKey();
			int num = erpManageI.getGoodsStock(id,sessionKey);
			map.put("code", 0);
			map.put("msg", num);
		} catch (Exception e) {
			map.put("code", -1);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	/**
	 * 获取库存(单个) post 请求
	 * @param id 订货号
	 * @return
	 */
	@RequestMapping(value="/goodstock", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "查询商品库存")
	public Map<String, Object> getEzhuGoodsStock(@ApiParam(value="同步码或订货号") @RequestParam String id){
		Map<String, Object> map = new HashMap<String, Object>();
		String sessionKey = null;
		try{
			sessionKey = getSessionKey();
			int num = erpManageI.getGoodsStock(id,sessionKey);
			map.put("code", 0);
			map.put("msg", num);
		} catch (Exception e) {
			map.put("code", -1);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	@RequestMapping(value="/test", method=RequestMethod.GET)
	@ResponseBody
	public ResponseMsg getTest(){
		ResponseMsg msg = new ResponseMsg();
		String sessionKey ="";
		try {
			sessionKey = ErpConnect.getSession();
			memcachedClient.set("EZHU_KEY", 0, sessionKey);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	} 
	
	/**
	 * 
	 * @author felx
	 * @describe 批量查询库存
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/goodBatchStock",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "查询商品批量库存")
	public ResponseMsg getBatchGoodsStock(HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		PageMode model = new PageMode(request);
		String sessionKey;
		try {
			sessionKey = getSessionKey();
			msg.setData(erpManageI.getBatchGoodsStock(model,sessionKey));
		} catch (Exception e) {
			e.printStackTrace();
			msg.setData("");
		}
		return msg;
	}
	
	
	/**
	 * 退货，入库记录更新(不使用 )
	 * @param channel backware 退货  putware 入库
	 * @param orderId 订单Id
	 * @param refundNo 第三方号
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{channel}/refund",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "退货，入库记录更新")
	@Deprecated
	public ResponseMsg updateRefundRecord(
			@ApiParam(value="backware 退货  putware 入库")@PathVariable("channel")String channel,
			@ApiParam(value="订单ID") @RequestParam String orderId,@ApiParam(value="第三方号") @RequestParam String refundNo,
			HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		StringBuffer url = request.getRequestURL();
		try {
			erpManageI.updateRefundRecord(channel,orderId,refundNo);
		} catch (Exception e) {
			msg.setError(e);
		}finally{
//			interfaceLogI.saveLogToMongoDB("退货,入库记录更新",
//					url.toString(), 
//					"updateRefundRecord", 
//					channel+orderId, 
//					"vipmro", 
//					"erp", 
//					msg.getMsg().length()>0?"-1":"0", 
//					msg.getMsg());
		}
		return msg;
	}
	
	/**
	 * 同步订单到ERP中
	 * @param id 订单号
	 * @param session 保存ERP KEY
	 * @return 同步ID
	 */
	@RequestMapping(value="/addScoreOrder/{id}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "同步订单到ERP中")
	public Map<String, Object> pushScoreToERP(@ApiParam(value="订单ID") @PathVariable("id") String id,HttpSession session,HttpServletResponse resp){
		resp.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> map = new HashMap<String, Object>();
		String sessionKey = null;
		try{
			sessionKey = getSessionKey();
			String fid = erpManageI.saveScoreToERP(id,sessionKey);
			map.put("code", 0);
			map.put("msg", fid);
		} catch (Exception e) {
			map.put("code", -1);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
	
	/**
	 *	@author jxw
	 * @param orderId 订单Id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/delOrder",method=RequestMethod.POST)
	@ResponseBody
	@ResponseDefault
	public Object delThirdOrder(
			@ApiParam(value="订单ID") @RequestParam String orderId
			) throws Exception{
		String sessionKey = getSessionKey();
		erpManageI.delThirdOrder(orderId,sessionKey);
		return null;
	}
	
	/**
	 *	@author jxw
	 *	@description 获取发票号
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getInvoice",method=RequestMethod.GET,produces="application/json; charset=utf-8")
	@ResponseBody
	public String getInvoice(@ApiParam(value="开始时间") @RequestParam("startTime") String startTime,
			@ApiParam(value="结束时间") @RequestParam("endTime") String endTime) throws Exception{
		String sessionKey = getSessionKey();
		return erpManageI.getInvoice(startTime,endTime,sessionKey);
	}
	
	/**
	 *	@author jxw
	 *	@description 获取快递号
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getLogics",method=RequestMethod.GET,produces="application/json; charset=utf-8")
	@ResponseBody
	public String getLogics(@ApiParam(value="开始时间") @RequestParam("startTime") String startTime,
			@ApiParam(value="结束时间") @RequestParam("endTime") String endTime) throws Exception{
		String sessionKey = getSessionKey();
		return erpManageI.getLogics(startTime,endTime,sessionKey);
	}
	
	/**
	 * 同步订单到ERP中
	 * @param id 订单号
	 * @param session 保存ERP KEY
	 * @return 同步ID
	 */
	@RequestMapping(value="/finalceaddOrder/{id}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "同步订单到ERP中")
	public Map<String, Object> financesaveOrderToERP(@ApiParam(value="订单id") @PathVariable("id") String id,HttpSession session,HttpServletResponse resp){
		resp.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> map = new HashMap<String, Object>();
		String sessionKey = null;
		long t1 = System.currentTimeMillis();
		try{
			sessionKey = getSessionKey();
			String fid = erpManageI.financesaveOrderToERP(id,sessionKey);
			map.put("code", 0);
			map.put("msg", fid);
			System.out.println("Cost:"+(System.currentTimeMillis()-t1));
		} catch (Exception e) {
			System.out.println("Cost:"+(System.currentTimeMillis()-t1));
			map.put("code", -1);
			map.put("msg", e.getMessage());
		}
		return map;
	}
}
