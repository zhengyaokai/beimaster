package bmatser.controller;

import com.alibaba.fastjson.JSONObject;
import com.sf.openapi.common.entity.MessageReq;
import com.sf.openapi.express.sample.order.dto.OrderNotifyReqDto;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import bmatser.dao.OrderInfoMapper;
import bmatser.dao.RegionMapper;
import bmatser.model.*;
import bmatser.pageModel.ShunfengOperatePage;
import bmatser.plugin.unionpay.sdk.SDKConstants;
import bmatser.service.ExpressI;
import bmatser.service.ShunfengOperateServiceI;
import bmatser.util.DESCoder;
import bmatser.util.ResponseMsg;
import bmatser.util.Status;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.*;


@Controller
@RequestMapping("express")
@Api(value="express", description="快递物流")
public class ExpressController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExpressController.class);

	@Autowired
	private ExpressI sfExpressesService;
	
	@Autowired
	private ShunfengOperateServiceI shunfengOperateI;

	@Autowired
	private ExpressI ztoExpressesService;

	@Autowired
	private OrderInfoMapper orderInfoDao;

	@Autowired
	private RegionMapper regionDao;


	/**
	 *
	 * @param gdbOrderId
	 * @param expressCompany
	 * @param request
	 * @return response
	 * @throws Exception
	 * @desc 根据物流公司下单并返回运单号
	 */
	@RequestMapping(value="/order",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "物流下单")
	public ExpressResponse addExpressOrder(@ApiParam(value="平台订单号") @RequestParam(value = "gdbOrderId",required = true) String gdbOrderId,
										   @ApiParam(value="物流公司名称拼音") @RequestParam(value = "expressCompany",required = true) String expressCompany,
										   @ApiParam(value="包裹数量") @RequestParam(value = "parcelQuantity",required = true) int parcelQuantity,
										   @ApiParam(value="物流备注") @RequestParam(value = "remark",required = false) String expressRemark, HttpServletRequest request){
		ExpressResponse response= new ExpressResponse();
		try{
			if(ExpressCompany.SF.name.equals(expressCompany)){
				ShunfengOperatePage shunfengOperatePage = new ShunfengOperatePage();
				shunfengOperatePage.setNum(parcelQuantity);
				shunfengOperatePage.setOrderId(gdbOrderId);
				try{
					String mailNo = shunfengOperateI.orderService(shunfengOperatePage);//里面已经更新了order_info表
					response.setExpressOrderId(mailNo);
					response.setStatus(SDKConstants.SUCCESS);
					response.setOrderId(gdbOrderId);
					response.setMsg("物流下单成功!");
				}catch(Exception e){
					response.setStatus(SDKConstants.FAIL);
					response.setOrderId(gdbOrderId);
					response.setMsg("物流下单失败!");
				}
				return response;
			}else {
				ExpressRequest expRequest = getExpressRequestByGdbOrderId(gdbOrderId);
				if(expRequest==null && expRequest.getOrderId()==null){
					response.setStatus(SDKConstants.FAIL);
					response.setMsg("未查到此订单或订单号不正确!");
					return response;
				}
				expRequest.setOrderId(gdbOrderId);//赋值正确的订单号
				expRequest.setExpressCompany(expressCompany);//快递物流公司的名称缩写 SF/ZTO/STO
				expRequest.setParcelQuantity(parcelQuantity);//包裹数
				if(StringUtils.isNotEmpty(expressRemark)){
					expRequest.setRemark(URLDecoder.decode(expressRemark, "utf-8"));//备注，顺丰备注最大支持30个汉字
				}else{
					expRequest.setRemark("GDB平台订单,请勿摔货,尽快处理!");
				}
				if(ExpressCompany.ZTO.name.equals(expressCompany)){
					response = ztoExpressesService.gdbExpressOrder(expRequest);
				}
			}
			if(SDKConstants.SUCCESS.equals(response.getStatus())  && response.getExpressOrderId()!=null) {
				boolean updateOrderResult = updateExpressToOrder(response);//更新order_info 表
				if (updateOrderResult) {
					response.setMsg("物流下单成功!");
					return response;
				} else {
					response.setMsg("物流下单成功,订单更新失败!");
				}
			}
		}catch (Exception e){
			LOGGER.error("GDB订单:"+gdbOrderId+"物流下单异常"+e.getMessage());
		}
		response.setStatus(SDKConstants.FAIL);
		return response;
	}

	/**
	 *
	 * @param gdbOrderId
	 * @param request
	 * @return map
	 * @throws Exception
	 * @desc 查询订单对应的物流公司和运单号
	 */
	@RequestMapping(value="/showOrderInfo",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "查询物流运单号")
	public Map<String,Object> showOrderInfo(@ApiParam(value ="平台订单号") @RequestParam(value = "gdbOrderId",required = true) String gdbOrderId, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("expressCompany","");//物流公司
		map.put("expressOrderId","");//对应的运单号(主单号)
		map.put("expressChildOrderId","");//对应的子单号
		try{
			if(StringUtils.isNumeric(gdbOrderId)){
				OrderInfo oInfo = orderInfoDao.findById(Long.valueOf(gdbOrderId.trim()));
				if(oInfo!=null && oInfo.getSellerLogisticsId()!=null){
					int logisticsId = oInfo.getSellerLogisticsId().intValue();
					String logisticsCode = oInfo.getLogisticsCode();
					map.put("expressCompany", ExpressCompany.getName(logisticsId));
					if(ExpressCompany.SF.index==logisticsId){ //顺丰下单后直接返回运单号了
						if(StringUtils.isNotEmpty(oInfo.getLogisticsCode())){
							map.put("expressOrderId", oInfo.getLogisticsCode());
							if(StringUtils.isNotEmpty(oInfo.getLogisticsMultiCode())){//有子母单号
								map.put("expressChildOrderId", oInfo.getLogisticsMultiCode());
							}
						}
					}else if(ExpressCompany.ZTO.index ==logisticsId){
						if(StringUtils.isNotEmpty(logisticsCode) && logisticsCode.length() > 15){ //中通下单后，返回的是中通订单号（18位）;快递收件，产生运单后,根据中通订单号查询运单号（运单号最少12位，最大15位）
							ExpressRequest eRequest = new ExpressRequest();
							eRequest.setOrderId(gdbOrderId);
							eRequest.setExpressCompany(ExpressCompany.ZTO.name);
							eRequest.setLogisticsCode(logisticsCode);
							eRequest.setSellerId(oInfo.getSellerId());
							ExpressResponse ep = ztoExpressesService.expressQuery(eRequest);
							if(SDKConstants.SUCCESS.equals(ep.getStatus()) && StringUtils.isNotEmpty(ep.getExpressOrderId()) ){
								String epressOrderIdStr = ep.getExpressOrderId();
								if(epressOrderIdStr.indexOf(",") > -1){ //多个包裹， 主单号和子单号用 ，号分割
									String[] orderIdArray = epressOrderIdStr.split(",");
									map.put("expressOrderId", orderIdArray[0]);
									map.put("expressChildOrderId", epressOrderIdStr.substring(orderIdArray[0].length()+1));
									oInfo.setLogisticsCode(orderIdArray[0]); //第一个为主单号
									oInfo.setLogisticsMultiCode(epressOrderIdStr.substring(orderIdArray[0].length()+1));
								}else{
									map.put("expressOrderId", epressOrderIdStr);
									oInfo.setLogisticsCode(epressOrderIdStr);
								}
								orderInfoDao.update(oInfo);
								LOGGER.info("GDB订单:"+gdbOrderId+"根据中通订单号查询到运单号并更新订单成功！(运单号:"+ep.getExpressOrderId()+")成功!");
							}
						}else{
							map.put("expressOrderId", oInfo.getLogisticsCode());
							if(StringUtils.isNotEmpty(oInfo.getLogisticsMultiCode())){//有子母单号
								map.put("expressChildOrderId", oInfo.getLogisticsMultiCode());
							}
						}
					}
				}
			}
		}catch (Exception e){
			 LOGGER.error("GDB订单:"+gdbOrderId+"根据中通订单号查询物流运单号异常"+e.getMessage());
		}
		return map;
	}





	/**
	 *
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/cancel",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "取消物流订单")
	public ResponseMsg cancelExpressOrder(@ApiParam(value ="平台订单号") String orderId){
		ResponseMsg msg = new ResponseMsg();
		try {
			if(StringUtils.isNumeric(orderId)) {
				OrderInfo oInfo = orderInfoDao.findById(Long.valueOf(orderId.trim()));
				if (oInfo != null && oInfo.getSellerLogisticsId() != null) {
					int logisticsId = oInfo.getSellerLogisticsId().intValue();
					if (ExpressCompany.SF.index == logisticsId) { //顺丰下单后直接返回运单号了
						ShunfengOperatePage shunfengOperatePage = new ShunfengOperatePage();
						shunfengOperatePage.setOrderId(orderId);
						//shunfengOperateI.cancelOrder(shunfengOperatePage)
						msg.setMsg("取消物流订单成功!");
						msg.setCode(0);
						return msg;
					} else if(ExpressCompany.ZTO.index == logisticsId) {
						Map<String,Object> map = new LinkedHashMap<String,Object>();
						if(StringUtils.isNotEmpty(oInfo.getLogisticsCode()) && oInfo.getLogisticsCode().toString().length()>15 ){
							ExpressRequest eRequest = new ExpressRequest();
							eRequest.setOrderId(orderId);
							eRequest.setLogisticsCode(oInfo.getLogisticsCode());
							eRequest.setExpressCompany(ExpressCompany.ZTO.name);
							ExpressResponse ep = ztoExpressesService.cancelExpressOrder(eRequest);
							msg.setMsg(ep.getMsg());
							msg.setCode(0);
							return msg;
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("error happen when run method printWaybill:"+e.getMessage());
		}
		msg.setMsg("取消物流订单失败!");
		msg.setCode(-1);
		return msg;
	}


	/**
	 * 打印电子运单/中通打印二维码
	 * @param
	 * @return
	 */
	@RequestMapping(value="/print_waybill",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "电子面单打印", notes = "面单打印")
	public ResponseMsg printWaybill(@ApiParam(value ="平台订单号") String orderId){
		ResponseMsg msg = new ResponseMsg();
		try {
			if(StringUtils.isNumeric(orderId)) {
				OrderInfo oInfo = orderInfoDao.findById(Long.valueOf(orderId.trim()));
				if (oInfo != null && oInfo.getSellerLogisticsId() != null) {
					int logisticsId = oInfo.getSellerLogisticsId().intValue();
					if (ExpressCompany.SF.index == logisticsId) { //顺丰下单后直接返回运单号了
						msg.setData(shunfengOperateI.printWaybill(orderId));
						msg.setCode(0);
					} else if(ExpressCompany.ZTO.index == logisticsId) {
						Map<String,Object> map = new LinkedHashMap<String,Object>();
						if(StringUtils.isNotEmpty(oInfo.getLogisticsCode()) && oInfo.getLogisticsCode().toString().length()<=15 ){
							map.put("req",getExpressRequestByGdbOrderId(orderId));
						}else{
							msg.setData("");
							msg.setCode(-1);
						}
						msg.setData(ztoExpressesService.printWaybill(orderId,map));
						msg.setCode(0);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("error happen when run method printWaybill:"+e.getMessage());
			msg.setError("打印失败");
		}
		return msg;
	}




	/**
	 * 成功下单后更新order_info 表的相关操作
	 * @return
	 */
	public boolean updateExpressToOrder(ExpressResponse respone){
		try{
			if(respone!=null && StringUtils.isNumeric(respone.getOrderId())){
				OrderInfo oInfo = orderInfoDao.findById(Long.valueOf(respone.getOrderId().trim()));
				if(oInfo!=null && StringUtils.isEmpty(oInfo.getLogisticsCode()) ){
					oInfo.setOrderStatus(Status.TORECEIVETHEGOODS.getValue());//3：已发货（待收货）
					oInfo.setShippingStatus(Status.SHIPPED.getValue()); //发货状态（0：未发货 1：已发货 2：已收货）
					Timestamp time = new Timestamp(System.currentTimeMillis());
					oInfo.setShippingTime(time); //发货时间
					oInfo.setSellerLogisticsId(ExpressCompany.getIndex(respone.getExpressCompany()));
					oInfo.setLogisticsRemark(respone.getLogisticsRemark());
					String epressOrderIdStr = respone.getExpressOrderId();
					if(epressOrderIdStr.indexOf(",") > -1){ //多个包裹， 主单号和子单号用 ，号分割
						String[] orderIdArray = epressOrderIdStr.split(",");
						oInfo.setLogisticsCode(orderIdArray[0]); //第一个为主单号
						oInfo.setLogisticsMultiCode(epressOrderIdStr.substring(orderIdArray[0].length()+1));
					}else{
						oInfo.setLogisticsCode(epressOrderIdStr);
					}
					orderInfoDao.update(oInfo);
					LOGGER.info("GDB订单:"+respone.getOrderId()+"更新物流信息(运单号 "+respone.getExpressOrderId()+")成功!");
					return true;
				}
			}
		}catch (Exception e){
			LOGGER.error("GDB订单:"+respone.getOrderId()+"更新物流信息(运单号 "+respone.getExpressOrderId()+")失败;异常原因:"+e.getMessage());
		}
		return false;
	}

	/**
	 * 根据GDB订单号获取发货人和收件人的相关信息()
	 * @param gdbOrderId
	 * @return
	 */
	public ExpressRequest getExpressRequestByGdbOrderId(String gdbOrderId)  {
		ExpressRequest expRequest = new ExpressRequest();
		try {
			Long oId = Long.valueOf(gdbOrderId);
			ExpressRequestWapper expressRequestWapper = orderInfoDao.wapperExpressRequestByOrderId(oId);
			Set<Integer> regionIdsSet = new HashSet<Integer>();
			DESCoder.instanceMobile();
			if (expressRequestWapper != null) {
				regionIdsSet.add(expressRequestWapper.getDeliverProvinceId());
				regionIdsSet.add(expressRequestWapper.getDeliverCityId());
				regionIdsSet.add(expressRequestWapper.getDeliverAreaId());
				expRequest = new ExpressRequest(expressRequestWapper);
				ExpressConsignAddressInfo expConAddres = JSONObject.parseObject(expressRequestWapper.getConsignAddress(), ExpressConsignAddressInfo.class);
				if (expConAddres != null) {
					expRequest.setConsignAddress(expConAddres.getAddress());
					expRequest.setConsignConsignee(expConAddres.getConsignee());
					expRequest.setConsignMobile(expConAddres.getMobile());
					regionIdsSet.add(expConAddres.getProvinceId());
					regionIdsSet.add(expConAddres.getCityId());
					regionIdsSet.add(expConAddres.getAreaId());
				}
				List<Region> regionList = getExpressRequestByGdbOrderId(regionIdsSet);
				if (regionList != null && regionList.size() > 0) {
					expRequest.setDeliverProvince(getRegionNameById(regionList, expressRequestWapper.getDeliverProvinceId()));
					expRequest.setDeliverCity(getRegionNameById(regionList, expressRequestWapper.getDeliverCityId()));
					expRequest.setDeliverArea(getRegionNameById(regionList, expressRequestWapper.getDeliverAreaId()));
					if (expConAddres != null) {
						expRequest.setConsignProvince(getRegionNameById(regionList, expConAddres.getProvinceId()));
						expRequest.setConsignCity(getRegionNameById(regionList, expConAddres.getCityId()));
						expRequest.setConsignArea(getRegionNameById(regionList, expConAddres.getAreaId()));
					}
				}
			}
			LOGGER.info("GDB订单:"+expRequest.getOrderId()+":快递请求包装类成功!");
		}catch(Exception e){
			LOGGER.error("GDB订单:"+expRequest.getOrderId()+":快递请求包装类异常！"+e.getMessage());
		}
		return expRequest;


	}

	/**
	 * 根据地区ID的set集合查询出对应的 Map<id,name>
	 * @param regionSet
	 * @return
	 */
	public List<Region> getExpressRequestByGdbOrderId(Set<Integer> regionSet){
		List<Region> idNamesList = regionDao.findNamesInIds(regionSet);
		if(idNamesList==null || idNamesList.isEmpty()){
			return Collections.emptyList();
		}
		return idNamesList;
	}

	/**
	 *从集合里根据Id查询地区名称
	 */
	public String getRegionNameById(List<Region> results,Integer regionId){
		for(Region region:results){
			if(regionId.intValue()==region.getId().intValue()){
				return region.getRegionName();
			}
		}
		return "";
	}

	/***
	 * @param req
	 * @param request
	 * @return ExpressResponses
	 * @throws Exception
	 * @desc 顺丰速运订单结果通知 在完成下单后，系统会处理这笔订单，系统处理完后会在调用商户在系统预留的订单结果回调的地址，推送订单结果信息。
	 */
	@RequestMapping(value="/sfOrder/notify",method=RequestMethod.POST)
	@ResponseBody
	 public ExpressResponse sfOrderAndQueryTest(@RequestBody MessageReq<OrderNotifyReqDto> req, HttpServletRequest request) throws Exception {
		ExpressResponse response= new ExpressResponse();
		response.setExpressCompany(ExpressCompany.SF.name);
		if(req.getBody()!=null){
			OrderNotifyReqDto notifyReqDto = req.getBody();
			if(StringUtils.isNotEmpty(notifyReqDto.getOrderId()) && StringUtils.isNotEmpty(notifyReqDto.getMailNo()) ){
				response.setOrderId(notifyReqDto.getOrderId());
				response.setExpressOrderId(notifyReqDto.getMailNo());
				boolean updateOrderResult = updateExpressToOrder(response);//更新order_info 表
				if(updateOrderResult){
					response.setStatus(SDKConstants.SUCCESS);
					response.setMsg("运单已生成，订单物流信息已更新;物流反馈信息如下:"+response.getMsg());
					return response;
				}else{
					response.setMsg("运单已生成，订单物流信息更新失败;物流反馈信息如下:"+response.getMsg());
				}
			}

		}
		response.setStatus(SDKConstants.FAIL);
		return response;

	 }


		private enum ExpressCompany{

			SF("shunfeng",1),
			STO("shentong",10),
			ZTO("zhongtong",21);

			private String name;
			private int index;

			ExpressCompany(String name, int index) {
				this.name = name;
				this.index = index;
			}

			public static int getIndex(String name) {
				for (ExpressCompany en : ExpressCompany.values()) {
					if (name.equals(en.getName()) ) {
						return en.index;
					}
				}
				return 0;
			}

			public static String getName(int index) {
				for (ExpressCompany en : ExpressCompany.values()) {
					if (index == en.getIndex() ) {
						return en.name;
					}
				}
				return "";
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public int getIndex() {
				return index;
			}

			public void setIndex(int index) {
				this.index = index;
			}
		}


		class pageRequest{

		}


	public static void main(String[] args) {
		try {
			DESCoder.instanceMobile();
			System.out.println(DESCoder.encoder("18929342412"));
		} catch (Exception e) {
			e.printStackTrace();
		}


	}


}
