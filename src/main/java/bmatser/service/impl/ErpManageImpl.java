package bmatser.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import bmatser.dao.*;
import bmatser.exception.GdbmroException;
import bmatser.model.*;
import bmatser.pageModel.Adress;
import bmatser.pageModel.OrderInfoErpPage;
import bmatser.pageModel.PageMode;
import bmatser.service.ErpManageI;
import bmatser.service.RegionServiceI;
import bmatser.util.DESCoder;
import bmatser.util.ErpConnect;
import bmatser.util.EzhuHttpclient;
import bmatser.util.Status;
import org.apache.commons.lang3.StringUtils;
import org.igfay.jfig.JFig;
import org.igfay.jfig.JFigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

//import bmatser.service.InterfaceLogI;

//import bmatser.service.InterfaceLogI;

@Service("erpManageService")
public class ErpManageImpl implements ErpManageI {
	
	@Autowired
	private  OrderInfoMapper orderInfoMapper;
	@Autowired
	private OrderGoodsMapper orderGoodsMapper;
	@Autowired
	private LogisticsCompanyMapper logisticsCompanyMapper;
	@Autowired
	private StaffMapper staffMapper;
	@Autowired
	private RegionMapper regionMapper;
	@Autowired
	private RegionServiceI regionServiceI;
//	@Autowired
//	private  InterfaceLogI interfaceLogI;
	@Autowired
	private RefundOrderRecordMapper refundOrderRecordDao;
	@Autowired
	private BuyerConsignAddressMapper buyerConsignAddressMapper;
	@Autowired
	private BrandMapper brandDao;
	@Autowired
	private GiftExchangeRecordMapper giftDao;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private CrmCustomerMapper crmCustomerMapper;

	@Autowired
	private OrderInfoMapper orderInfoDao;
	@Autowired
	private LogisticsCompanyMapper logisticsCompanyDao;
	@Autowired
	private OrderExpressMapper orderExpressMapper;
	@Autowired
	private OrderExpressGoodsMapper orderExpressGoodsMapper;



	@Value("${jiqin.erp.guser}")
	private String defaultGuser;
	@Value("${jiqin.erp.buser}")
	private String defaultBuser;
	@Value("${jiqin.erp.gdbuser.suffer}")
	private String GDBCustomerSuffer;//防止和ERP中客户ID重复
	@Value("${jiqin.erp.gdbuser.defaultsale}")
	private boolean useDefaultSale;//是否用默认销售员

	private static final String gdbHtBz = "工电宝平台订单";

	private static final String erpOutSplitStr = "#";

	private static final Logger LOGGER = LoggerFactory.getLogger(ErpManageImpl.class);

//	@Override
//	public String saveOrderToERP(String id, String sessionKey) throws Exception {
//		String  tradeStr = null;
//		List<Item> items =new ArrayList<Item>();
//		String fid="";
//		try {
//			if(StringUtils.isNotBlank(id)){
//				Trade trade = orderInfoMapper.saveOrderToERP(id);
//				if(trade==null){
//					throw new Exception("请检查订单号,订单不存在");
//				}
//				String originid = trade.getFbillno();
//				if(originid.contains("-")){
//					originid = originid.substring(0, originid.indexOf("-"));
//				}
//				
//				Adress addr = regionServiceI.getRegionbyOrderId(id);
////				String adress = order.getConsignAddressInfo()!=null?order.getConsignAddressInfo() : "";
//				//String adress = order.getConsignAddressInfoSecret()!=null?order.getConsignAddressInfoSecret() : "";
//				trade.setFreceiverstate(addr.getProvince());
//				trade.setFreceivercity(addr.getCity());
//				trade.setFreceiverdistrict(addr.getArea());
//				trade.setFreceiver(addr.getConsignee());
//				trade.setFreceivermobile(addr.getMobile());
//				trade.setFreceiveraddress(addr.getAddress());
//				DESCoder.instanceMobile();
//				try {
//	/*				JSONObject jo = JSONObject.parseObject(adress);
//					String consignee =  jo.get("consignee")!=null?jo.get("consignee").toString() : "";
//					String mobile = 	 jo.get("mobile")!=null?jo.get("mobile").toString() : "";
//					String areaId = 	 jo.get("areaId")!=null?jo.get("areaId").toString() : "";
//					String cityId = 	 jo.get("cityId")!=null?jo.get("cityId").toString() : "";
//					String provinceId = 	 jo.get("provinceId")!=null?jo.get("provinceId").toString() : "";
//					String address = 	 jo.get("address")!=null?jo.get("address").toString() : "";
//					String area = "";
//					if(!"0".equals(areaId)){
//						area = regionMapper.selectByPrimaryKey(Integer.parseInt(areaId)).getRegionName();
//					}
//					String city = regionMapper.selectByPrimaryKey(Integer.parseInt(cityId)).getRegionName();
//					String province = regionMapper.selectByPrimaryKey(Integer.parseInt(provinceId)).getRegionName();*/
//
//				} catch (Exception e) {
//				}
//				Map<String, String> inv = new HashMap<String, String>();
//				trade.setInvoiceaddress(inv);
//				try {
//					if( (!StringUtils.equals("0", trade.getFinvreceiverstate())) && (!StringUtils.equals("0", trade.getFinvreceivercity())) ){
//						inv.put("finvreceiver", trade.getFinvreceiver());
//						inv.put("finvreceiveraddress", trade.getFinvreceiveraddress());
//						inv.put("finvreceiverstate", regionMapper.selectByPrimaryKey(Integer.parseInt(trade.getFinvreceiverstate())).getRegionName());
//						inv.put("finvreceivercity", regionMapper.selectByPrimaryKey(Integer.parseInt(trade.getFinvreceivercity())).getRegionName());
//						inv.put("finvreceiverdistrict", regionMapper.selectByPrimaryKey(Integer.parseInt(trade.getFinvreceiverdistrict())).getRegionName());
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				//trade.setFreceivermobile(StringUtils.isNotBlank(trade.getFreceivermobile())?DESCoder.decoder(trade.getFreceivermobile()):"");
//				trade.setFreceiverphone(StringUtils.isNotBlank(trade.getFreceiverphone())?DESCoder.decoder(trade.getFreceiverphone()):"");
//				trade.setFinvoiceregtel(StringUtils.isNotBlank(trade.getFinvoiceregtel())?DESCoder.decoder(trade.getFinvoiceregtel()):"");
//				inv.put("finvreceivermobile", StringUtils.isNotBlank(trade.getFinvreceivermobile())?DESCoder.decoder(trade.getFinvreceivermobile()):"");
//				OrderInfo order = orderInfoMapper.findByOrderId(id);
//				if(StringUtils.isNotBlank(order.getOriginalOrderId())&&order.getOriginalOrderId().contains(",")){
//					List<Long> ids = new ArrayList<Long>();
//					List<Long> originalId = getOriginalId(ids, order.getId());
//					trade.setFshoptradestatus(2);
//					for (Long oid : originalId) {
//						OrderInfo o = orderInfoMapper.getAmount(oid);
//						if(o.getShouldAmount().compareTo(o.getPayAmount())==1&&o.getRateType()!=3){
//							trade.setFshoptradestatus(1);
//							break;
//						}
//					}
//				}else{
//					OrderInfo co = orderInfoMapper.findByOrderId(originid);
//					if(StringUtils.isNotBlank(co.getOriginalOrderId())&&co.getOriginalOrderId().contains(",")){
//						List<Long> ids = new ArrayList<Long>();
//						List<Long> originalId = getOriginalId(ids, order.getId());
//						trade.setFshoptradestatus(2);
//						for (Long oid : originalId) {
//							OrderInfo o = orderInfoMapper.getAmount(oid);
//							if(o.getShouldAmount().compareTo(o.getPayAmount())==1&&o.getRateType()!=3){
//								trade.setFshoptradestatus(1);
//								break;
//							}
//						}
//					}else{
//						Integer rateType = orderInfoMapper.getRateType(originid);
//						OrderInfo o = orderInfoMapper.getAmount(Long.valueOf(originid));
//						if(8==trade.getFshoptradestatus()){
//							trade.setFshoptradestatus(-1);
//						}else if(o.getShouldAmount().compareTo(o.getPayAmount()==null?BigDecimal.ZERO:o.getPayAmount())==1&&rateType==null){
//							trade.setFshoptradestatus(1);
//						}else{
//							if(rateType!=null&&rateType.intValue()==3){
//								trade.setFiscod(1);
//								trade.setFexpcomnum("SHSM");
//							}
//							trade.setFshoptradestatus(2);
//						}
//					}
//				}
//	/*			switch (trade.getFshoptradestatus()) {
//				case 1:trade.setFshoptradestatus(2);
//					break;
//				case 2:trade.setFshoptradestatus(2);
//					break;
//				case 3:trade.setFshoptradestatus(2);
//					break;
//				case 4:trade.setFshoptradestatus(2);
//					break;
//				case 5:trade.setFshoptradestatus(2);
//				break;
//				case 6:trade.setFshoptradestatus(2);
//				break;
//				case 7:trade.setFshoptradestatus(2);
//				break;
//				case 8:trade.setFshoptradestatus(-1);
//				break;
//				default: trade.setFshoptradestatus(2);
//					break;
//			}*/
//				/** 供应链平台 47 商城46*/
//				switch (trade.getFshopid()){
//					case 1:trade.setFshopid(48);
//						break;
//					case 2:trade.setFshopid(47);
//						break;
//					case 3:trade.setFshopid(46);
//						break;
//					default: trade.setFshopid(47);
//					break;	
//				}
//				if(trade.getFempnumber()!=null && !"".equals(trade.getFempnumber())){
//					Staff staff = staffMapper.selectByPrimaryKey(trade.getFempnumber());
//					trade.setFempname(staff.getStaffName());
//				}
//				List<OrderGoods> goodsList = orderGoodsMapper.selectGoodsByOrderId(id);
//				for(OrderGoods goods : goodsList){
//					BigDecimal fprice = goods.getSaleUnitPrice()==null?goods.getUnitPrice() : goods.getSaleUnitPrice();
//					fprice	= goods.getTail()!=null && StringUtils.isNotBlank(goods.getTail().toString()) ? goods.getTail() : fprice;
//					Item item = new Item();
//					item.setFskuname(goods.getTitle());
//					item.setFouternumber(goods.getBuyNo());
//					item.setFqty(goods.getNum());
//					item.setFprice(fprice);
//					item.setFamount(fprice.multiply(new BigDecimal(goods.getNum())));
//					items.add(item);
//				}
//				trade.setItems(items);
//				trade.setFexpcomnum("SF");
//				tradeStr = JSONObject.toJSONString(trade);
//			}else{
//				throw new Exception("订单ID不能为空");
//			}
//			fid = ErpConnect.postFrom(sessionKey,"AddTrade",tradeStr,"trade").getString("fid");
//			OrderInfo order= new OrderInfo();
//			order.setId(Long.parseLong(id));
//			order.setThirdId(fid);
//			orderInfoMapper.update(order);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new Exception(e.getMessage());
//		}
//		return fid;
//	}
	
	
	
	
	@Override
	public String saveOrderToERP(String id) throws Exception {
		
		String fid="";
		try {
			OrderToERP trade = null;
			if(StringUtils.isNotBlank(id)){
				trade = orderInfoMapper.saveOrderToJQERP(id);
				/**拿到buyerId 调用erp新增客户*/
				
				if(trade==null){
					throw new Exception("请检查订单号,订单不存在");
				}
				trade.setBaDjBz("");
				trade.setGaKhBID("");
				trade.setSumCkSl("");
				trade.setSumKpSl("");
				trade.setBaDjBz(gdbHtBz+" "+id);
				trade.setDeliveryMan(trade.getExactDeliveryMan());//准确的收货人地址
				trade.setDeliveryAddress(getFullDeliveryAddess(trade.getConsignAddress()));
				DESCoder.instanceMobile();
				trade.setInvoiceTelphone(StringUtils.isNotBlank(trade.getInvoiceTelphone())?DESCoder.decoder(trade.getInvoiceTelphone()):"");
				trade.setDeliveryTelphone(StringUtils.isNotBlank(trade.getDeliveryTelphone())?DESCoder.decoder(trade.getDeliveryTelphone()):"");
				trade.setInvoiceDeliveryTelphone(StringUtils.isNotBlank(trade.getInvoiceDeliveryTelphone())?DESCoder.decoder(trade.getInvoiceDeliveryTelphone()):"");

				Map<String,Object> customerMap = this.saveCustomerToERP(trade.getBuyerId().toString(),trade);
			    if(customerMap.get("gclient")==null || StringUtils.isBlank(customerMap.get("gclient").toString())){ //客户编码
					throw new Exception("ERP中新增客户失败!");
				}
				trade.setGclient(customerMap.get("gclient").toString());
				if(customerMap.get("guser")==null || StringUtils.isBlank(customerMap.get("guser").toString())){//销售员
					trade.setGuser(defaultGuser);
				}else {
					trade.setGuser(customerMap.get("guser").toString());
				}
				if(customerMap.get("buser")==null || StringUtils.isBlank(customerMap.get("buser").toString())){//销售商务
					trade.setBuser(defaultBuser);
				}else {
					trade.setBuser(customerMap.get("buser").toString());
				}
				//如果订单中商品没同步，先同步商品
				List<String> noSynoCodeGoodsList = orderGoodsMapper.selectNoSyncGoodsByOrderIdForErp(id);
				if(noSynoCodeGoodsList!=null && !noSynoCodeGoodsList.isEmpty()){
					if(!syncGoodsToErp( String.join(",",noSynoCodeGoodsList))){
						throw new RuntimeException("订单推送失败![订单中商品同步到ERP失败]");
					}
				}
				List<Produce> goodsList = orderGoodsMapper.selectGoodsByOrderIdForErp(id);
				for(Produce produce : goodsList){
					produce.setBdYjJhq("");
					produce.setBaYfCd("");
					OrderGoods og = orderGoodsMapper.selectByOrderIdAndGoodId(Long.valueOf(id),produce.getGoodId());
					produce.setBfYgCb(og!=null&&og.getCostPrice()!=null?og.getCostPrice(): BigDecimal.valueOf(0));//商品的采购成本(预估成本)（用作ERP 采购备货等）取Table:order_goods.cost_price
					trade.setContractDiscount(null);//为了下一行得到折扣
					produce.setBfHtZk(trade.getContractDiscount());//合同总折扣（和商品折扣一起体现)
					produce.setBprice(produce.getBprice().multiply(produce.getBfHtZk()).setScale(2, BigDecimal.ROUND_HALF_UP));
				}
				trade.setProduceList(goodsList);
				
			}else{
				throw new Exception("订单ID不能为空");
			}
			String body = restTemplateObjectInvoke(trade,"RestfulCreateBargainInOrderEx");
			if(StringUtils.isNotBlank(body)){
				Integer result = (Integer)JSONObject.parseObject(body).get("Result");
				if(result==1){
					fid = (String)JSONObject.parseObject(body).get("BID");
					OrderInfo order= new OrderInfo();
					order.setId(Long.parseLong(id));
					order.setThirdId(fid);
					orderInfoMapper.update(order);
				}else{
					String errorMsg = String.valueOf(JSONObject.parseObject(body).get("Message"));
					throw new Exception(errorMsg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			throw new Exception(e.getMessage());
		}
		return fid;
	}
	
	
	@Override
	public Map<String,Object> saveCustomerToERP(String dealerId,OrderToERP trade) throws Exception{
		/**新在erp里先新增组织机构，再新增部门，最后新增商务和销售*/
		/**新增客户,先根据dealer查询客户信息*/
		Map<String,Object> customerMap = new HashMap<String,Object>();
		CrmCustomer customer = crmCustomerMapper.getCustomerByDealerId(dealerId);
		//customer.setName("工电宝测试客户"+System.currentTimeMillis());
		if(customer==null){
			return customerMap;
		}
		if(StringUtils.isNotBlank(customer.getErpCustomerCode())){
			/**2 新增客户收货信息(根据订单号查询收获地址)*/
			if(trade!=null){
				Map<String,Object> receiveMap = Maps.newHashMap();
				receiveMap.put("Gclient", customer.getErpCustomerCode());
				receiveMap.put("Name", trade.getExactDeliveryMan());//吉勤ERP 相同收货人名字，不同地址，不会更新
				receiveMap.put("Telphone", trade.getDeliveryTelphone());
				receiveMap.put("Address", trade.getDeliveryAddress());
				restTemplateObjectInvoke(receiveMap,"RestfulCreateCustomerDeliveryMan");
			}
			customerMap.put("gclient",customer.getErpCustomerCode());
			customerMap.put("guser",(customer.getCustomerManagerId()==null || useDefaultSale)?defaultGuser:GDBCustomerSuffer+customer.getCustomerManagerId());
			customerMap.put("buser",(customer.getCustomerServiceId()==null || useDefaultSale)?defaultGuser:GDBCustomerSuffer+customer.getCustomerServiceId());
			return customerMap;
		}
		Map<String,Object> map = transObjectToMap(customer,trade);
		String createCustomerBody = restTemplateObjectInvoke(map,"RestfulCreateCustomerEx_ZP");
		if(StringUtils.isNotBlank(createCustomerBody)){
			Integer result = (Integer)JSONObject.parseObject(createCustomerBody).get("Result");
			if(result!=0 ){
				Integer customerNo = (Integer)JSONObject.parseObject(createCustomerBody).get("ID");
				/**根据返回的erp客户编码，更新到crm_customer表的erp_customer_code*/
				crmCustomerMapper.updateCrmCustomerCode(customerNo,dealerId);

				/**< </>新增客户商务*/
				Map<String,Object> map2 = Maps.newHashMap();
				map2.put("Gclient", customerNo.toString());
				map2.put("Buser", (customer.getCustomerServiceId()==null || useDefaultSale)?defaultBuser:GDBCustomerSuffer+customer.getCustomerServiceId());
				String bUserBody = restTemplateObjectInvoke(map2,"RestfulCreateCustomerBUser");
				/**2 新增客户收货信息(根据订单号查询收获地址)*/
				if(trade!=null){
					Map<String,Object> receiveMap = Maps.newHashMap();
					receiveMap.put("Gclient", customerNo.toString());
					receiveMap.put("Name", trade.getExactDeliveryMan());
					receiveMap.put("Telphone", trade.getDeliveryTelphone());
					receiveMap.put("Address", trade.getDeliveryAddress());
					String cDeliveryManBody = restTemplateObjectInvoke(receiveMap,"RestfulCreateCustomerDeliveryMan");
				}
				customerMap.put("gclient",customerNo.toString());
				customerMap.put("guser",(customer.getCustomerManagerId()==null || useDefaultSale)?defaultGuser:GDBCustomerSuffer+customer.getCustomerManagerId());
				customerMap.put("buser",map2.get("Buser"));
				return customerMap;
			}
		}
		return customerMap;
	}

	private Map<String,Object> transObjectToMap(CrmCustomer customer,OrderToERP trade) throws Exception{
		Map<String,Object> map = Maps.newHashMap();
		map.put("ID", "-1");
		map.put("Name", customer.getName());
		map.put("Name2", customer.getName());
		map.put("Man", customer.getLinkMan());
		DESCoder.instanceMobile();
		String mobile = DESCoder.decoder(customer.getMobileSecret());
		map.put("Tel", mobile);
		map.put("Phone", mobile);
		map.put("Fax", customer.getFax()!=null?customer.getFax():"");
		map.put("QQ", customer.getQq()!=null?customer.getQq():"");
		map.put("Email", customer.getEmail()!=null?customer.getEmail():"");
		map.put("Kind", "全部客户");
		map.put("Trade", "工控行业");
		map.put("Address",customer.getDetailAddress()!=null?customer.getDetailAddress():"");
		map.put("Area",customer.getPlaceId()!=null&&customer.getPlaceId()>0 ? regionMapper.selectRegionString(customer.getPlaceId().toString()) :"");
		map.put("Province", customer.getProvinceId()!=null&&customer.getProvinceId()>0 ? regionMapper.selectRegionString(customer.getProvinceId().toString()) :"");
		map.put("City", customer.getCityId()!=null&&customer.getCityId()>0 ? regionMapper.selectRegionString(customer.getCityId().toString()) :"");
		map.put("Post", "");
		map.put("Importance", "重要");
		map.put("Account", trade.getAccount()!=null?trade.getAccount():"");
		map.put("Tax", trade.getTax()!=null?trade.getTax():"");
		map.put("BankAddress", trade.getBankAddress()!=null?trade.getBankAddress():"");
		map.put("InvoiceAddress", trade.getInvoiceAddress()!=null?trade.getInvoiceAddress():"");
		map.put("InvoiceTelphone", trade.getInvoiceTelphone()!=null?trade.getInvoiceTelphone():"");
		if(customer.getCustomerManagerId()==null || useDefaultSale){
			map.put("Guser", defaultGuser);
			map.put("Gdept", "root.001.GLB");
		}else{
			map.put("Guser", GDBCustomerSuffer+customer.getCustomerManagerId());
			map.put("Gdept", "root.006.XSB");
		}
		if(customer.getCustomerServiceId()==null || useDefaultSale){
			map.put("Buser", defaultBuser);
			map.put("Bdept", "root.001.GLB");
		}else{
			map.put("Buser", GDBCustomerSuffer+customer.getCustomerServiceId());
			map.put("Bdept", "root.006.XSB");
		}
		return map;
	}
	
	@Override
	public void saveGoodToERP() {
		/**先查询没有syn_code的商品，然后循环调用接口新增到erp*/
		List<Goods> list = goodsMapper.selectNoSynCodeGoods(0,1000);
			try {
				List<Map<String,Object>> param= transObjectForList(list);
				restTemplateInvoke(param,"RestfulCreateProduceEx");
			} catch (JFigException e) {
				e.printStackTrace();
			}
	}





	private List<Map<String,Object>> transObjectForList(List<Goods> param){
		List<Map<String,Object>> list = Lists.newArrayList();
		for(Goods good : param){
			Map<String,Object> idListMap = Maps.newHashMap();
			idListMap.put("ID", good.getId());
			idListMap.put("ProID",-1);
			idListMap.put("Gbrand",good.getBrandName());
			idListMap.put("Gserial", good.getSeries());
			idListMap.put("GID",good.getModel().replace("%", ""));
			idListMap.put("Gname",good.getTitle().replace("%", ""));
			idListMap.put("Gindex",good.getBuyNo().replace("%", ""));
			idListMap.put("Gunit",good.getMeasure()==null?"个":good.getMeasure());
			idListMap.put("BaCgCp", "");
			idListMap.put("BaCpMs","");
			idListMap.put("BaHqSm","");
			idListMap.put("BfCpMj", good.getMarketPrice());
			idListMap.put("BaCd","");
			idListMap.put("BfZl", good.getWeight());
			idListMap.put("BfQdl", 0);
			idListMap.put("BfYjZk", "");
			idListMap.put("BfYjJj", 0);
			list.add(idListMap);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	private void restTemplateInvoke(List<Map<String,Object>> list,String method) throws JFigException{
		String url = JFig.getInstance().getValue("erp_options", "URL")+method;
		RestTemplate  restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		try{
			ResponseEntity<String> response=restTemplate.postForEntity(url, list, String.class);
			/**处理返回body,循环更新goods表的syn_code*/
			String result = response.getBody();
			LOGGER.info(response.getBody());
			System.out.println(response.getBody());
			List<Map<String,Object>> resultList = (List<Map<String, Object>>) JSONObject.parseObject(result).get("IDList");
			if(resultList!=null && resultList.size()>0){
				for(Map<String,Object> mapValue : resultList){
					goodsMapper.updateSynCodeById(mapValue.get("ID").toString(),(Integer)mapValue.get("ProID"));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}


	@Override
	public String queryERPOrder(String erpOrderNo) throws Exception {
			if(StringUtils.isEmpty(erpOrderNo)){
				return null;
			}
			Map<String,Object> map = Maps.newHashMap();
			map.put("BID",erpOrderNo);
			String produceInfoStr = restTemplateObjectInvoke(map,"RestfulRefreshBargainInOrderEx");
			if(StringUtils.isNotBlank(produceInfoStr)){
				JSONObject resultObj = JSONObject.parseObject(produceInfoStr);
				Integer result = (Integer)resultObj.get("Result");
				if(result!=0){
					return  produceInfoStr;
				}
			}
			return null;
	}


	private String restTemplateObjectInvoke(Object request, String method) {
		try{
			String url = JFig.getInstance().getValue("erp_options", "URL")+method;
			RestTemplate  restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
			LOGGER.info(JSONObject.toJSONString(request));
			System.out.println("=========="+JSONObject.toJSONString(request));
			ResponseEntity<String> response=restTemplate.postForEntity(url, JSONObject.toJSONString(request), String.class);
			/**处理返回body,循环更新goods表的syn_code*/
			LOGGER.info(response.getBody());
			System.out.println("=========="+response.getBody());
			return response.getBody();
		}catch(Exception e){
			LOGGER.info(e.getMessage());
		}
		return null;
	}

	@Override
	public void updateOrder(OrderInfoErpPage orderPage) throws Exception {
		if(StringUtils.isNotBlank(orderPage.getId())){
			OrderInfo order= orderInfoMapper.findBychildId(orderPage.getId());
			if(order==null){
				order=orderInfoMapper.findById(Long.parseLong(orderPage.getId()));
			}
			if(order!=null){
				if((!"2".equals(order.getOrderStatus()))&&StringUtils.isNotBlank(orderPage.getOrderStatus())){
					return;
				}
				if(StringUtils.isNotBlank(orderPage.getOrderStatus())){
					order.setOrderStatus(orderPage.getOrderStatus());
				}else{
					order.setOrderStatus("3");
				}
				if("3".equals(orderPage.getOrderStatus())){
					if(StringUtils.isBlank(orderPage.getLogisticsId())){
						throw new RuntimeException("物流公司Id不能为空");
					}
					if(StringUtils.isBlank(orderPage.getLogisticsTime())){
						throw new RuntimeException("物流时间不能为空");
					}
					if(StringUtils.isBlank(orderPage.getLogisticsNo())){
						throw new RuntimeException("物流单号不能为空");
					}
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");                
					Date date = sdf.parse(orderPage.getLogisticsTime());
					LogisticsCompany logistics = logisticsCompanyMapper.fingById(Integer.parseInt(orderPage.getLogisticsId()));
					if(logistics==null){
						throw new Exception("找不到对应物流公司");
					}
					order.setSellerLogisticsId(Integer.parseInt(orderPage.getLogisticsId()));
					order.setSellerRemark(orderPage.getSellerRemark());
					order.setShippingTime(date);
					order.setLogisticsCode(orderPage.getLogisticsNo());
				}
				order.setShippingStatus("1");
				orderInfoMapper.update(order);
			}else{
				int giftStatus = giftDao.getExistEZhuGiftStatus(orderPage.getId());
				if(giftStatus==0){
					throw new Exception("该订单号不存在");
				}else{
					giftDao.updateEzhuInfo(orderPage.getId(),orderPage.getLogisticsId(),orderPage.getLogisticsNo());
				}
			}
		}else{
			throw new Exception("订单号不能为空");
		}
	}

	@Override
	public int getGoodsStock(String id, String sessionKey) throws Exception {
		int num =0;
		try {
			JSONArray  jo= ErpConnect.postFrom(sessionKey,"Gph_GetInventory",id,"outnumber").getJSONArray("iteminvs");
			String brandId = brandDao.selectERPbrand(id);
			for (int i = 0; i < jo.size(); i++) {
				JSONObject o=  jo.getJSONObject(i);
				String stockid = o.getString("fstockid");
				int fcansellqty = o.getIntValue("fcansellqty");
				if("194".equals(brandId) || "104".equals(brandId)){
					if("39".equals(stockid)){
						num=o.getIntValue("fcansellqty");
						break;
					}
				}else{
					if("32".equals(stockid) || "38".equals(stockid)){
						num=num+fcansellqty;
					}
				}
			}
		} catch (Exception e) {
			num =0;
		}
		return num;
	}

	@Override
	public void saveOrderToERP(Long id, String sessionKey) throws Exception {

		String  tradeStr = null;
		List<Item> items =new ArrayList<Item>();
		if(id != null && !"".equals(id)){
			Trade trade = orderInfoMapper.saveOrderToERP(id.toString());
			if(trade==null){
				throw new Exception("请检查订单号,订单不存在");
			}
			switch (trade.getFshoptradestatus()) {
			case 1:trade.setFshoptradestatus(4);
				break;
			case 2:trade.setFshoptradestatus(2);
				break;
			case 3:trade.setFshoptradestatus(4);
				break;
			case 4:trade.setFshoptradestatus(5);
				break;
			case 5:trade.setFshoptradestatus(5);
			break;
			case 6:trade.setFshoptradestatus(6);
			break;
			case 7:trade.setFshoptradestatus(5);
			break;
			case 8:trade.setFshoptradestatus(-1);
			break;
			default: trade.setFshoptradestatus(4);
			break;
		}
			/** 供应链平台 47 商城46*/
			switch (trade.getFshopid()){
				case 1:trade.setFshopid(48);
					break;
				case 2:trade.setFshopid(47);
					break;
				case 3:trade.setFshopid(46);
					break;
				default: trade.setFshopid(47);
				break;	
			}
			if(trade.getFempnumber()!=null && !"".equals(trade.getFempnumber())){
				Staff staff = staffMapper.selectByPrimaryKey(trade.getFempnumber());
				trade.setFempname(staff.getStaffName());
			}
			List<OrderGoods> goodsList = orderGoodsMapper.selectGoodsByOrderId(id.toString());
			for(OrderGoods goods : goodsList){
				BigDecimal fprice = goods.getSaleUnitPrice()==null?goods.getUnitPrice() : goods.getSaleUnitPrice();
				Item item = new Item();
				item.setFskuname(goods.getTitle());
				item.setFouternumber(goods.getBuyNo());
				item.setFqty(goods.getNum());
				item.setFprice(fprice);
				item.setFamount(fprice.multiply(new BigDecimal(goods.getNum())));
				items.add(item);
			}
			trade.setItems(items);
			tradeStr = JSONObject.toJSONString(trade);
		}else{
			throw new Exception("订单ID不能为空");
		}
		String fid = ErpConnect.postFrom(sessionKey,"AddTrade",tradeStr,"trade").getString("fid");
		OrderInfo order= new OrderInfo();
		order.setId(id);
		order.setThirdId(fid);
		orderInfoMapper.update(order);
	}

	@Override
	public String getBatchGoodsStock(PageMode model, String sessionKey) throws Exception {
		EzhuHttpclient eZhu  = null;
		try {
			if(model.contains("id")){
				eZhu = new EzhuHttpclient("Gph_GetInventory2", sessionKey);
				eZhu.put("outnumber", model.getStringValue("id"));
				String body = eZhu.getResponseBody();
				JSONObject jo = JSONObject.parseObject(body);
				String errmsg = jo.getString("errmsg");
				if(StringUtils.isBlank(errmsg)){
					return jo.getString("Inventorys");
				}else{
					throw new Exception(body);
				}
			}else{
				return "";
			}
		} finally{
			/*saveMongodb(eZhu); */
		}
	}

	private void saveMongodb(EzhuHttpclient eZhu) {
//		interfaceLogI.saveLogToMongoDB(eZhu.getDescribe(), //接口描述
//																	eZhu.getUrl(), //接口访问地址
//																	eZhu.getMethod(), 
//																	eZhu.getParamString(), 
//																	"gdbmro", 
//																	"erp", 
//																	"0", 
//																	eZhu.getMsg());
	}

	@Override
	public void updateRefundRecord(String channel, String orderId,String refundNo) throws Exception {
		int i =0;
		String id = null;
		if(StringUtils.isNotBlank(orderId) && StringUtils.isNotBlank(refundNo)){
			id = refundOrderRecordDao.findIdByOrderId(orderId);
		}else{
			throw new Exception("参数不能为空");
		}
		switch (channel) {
		case "backware"://退库
			if(StringUtils.isBlank(id)){
				i = refundOrderRecordDao.addBackware(id,refundNo);
			}else{
				throw new Exception("重复提交退货");
			}
			break;
		case "putware"://入库
			if(StringUtils.isNotBlank(id)){
				i = refundOrderRecordDao.updatePutware(id,refundNo);
			}else{
				throw new Exception("请先退货,再提交入库");
			}
			break;
		}
		if(i == 0){
			throw new Exception("更新失败");
		}
	}

	@Override
	public String saveScoreToERP(String id, String sessionKey) throws Exception {
		String  tradeStr = null;
		List<Item> items =new ArrayList<Item>();
		if(StringUtils.isNotBlank(id)){
			Trade trade = orderInfoMapper.saveScoreToERP(id);
			if(trade==null){
				throw new Exception("请检查订单号,订单不存在");
			}
			trade.setFsalermessage("积分兑换礼品");
			if(StringUtils.isNotBlank(trade.getFreceiveraddress())){
				DESCoder.instanceMobile();
				JSONObject json=JSONObject.parseObject(trade.getFreceiveraddress().toString());
				if(json.get("province_id_name")!=null){
					trade.setFreceiverstate(json.get("province_id_name").toString());
				}
				if(json.get("city_id_name")!=null){
					trade.setFreceivercity(json.get("city_id_name").toString());
				}
				if(json.get("area_id_name")!=null){
					trade.setFreceiverdistrict(json.get("area_id_name").toString());
				}
				if(json.get("consignee")!=null){
					trade.setFreceiver(json.get("consignee").toString());
				}
				if(json.get("mobile")!=null){
					Map m = buyerConsignAddressMapper.findById(Integer.valueOf(json.get("id").toString()));
					if(m!=null&&m.get("mobileSecret")!=null){
						trade.setFreceivermobile(DESCoder.decoder(m.get("mobileSecret").toString()));
					}
				}
				if(json.get("post_code")!=null){
					trade.setFreceiverzip(json.get("post_code").toString());
				}
				if(json.get("address")!=null){
					trade.setFreceiveraddress(json.get("address").toString());
				}
			}
			trade.setFshopid(47);
			if(trade.getFempnumber()!=null && !"".equals(trade.getFempnumber())){
				Staff staff = staffMapper.selectByPrimaryKey(trade.getFempnumber());
				trade.setFempname(staff.getStaffName());
			}
			Item item = new Item();
			item.setFskuname(trade.getName());
			item.setFouternumber(trade.getSynCode());
			item.setFqty(trade.getNum());
			item.setFprice(BigDecimal.ZERO);
			item.setFamount(BigDecimal.ZERO);
			items.add(item);
			trade.setItems(items);
			tradeStr = JSONObject.toJSONString(trade);
		}else{
			throw new Exception("订单ID不能为空");
		}
		String fid = ErpConnect.postFrom(sessionKey,"AddTrade",tradeStr,"trade").getString("fid");
		orderInfoMapper.updateGift(id,fid);
		return fid;
	}

	@Override
	public void cancelOrder(String id) {
		orderInfoMapper.cancelEzhuOrder(id);
	}

	@Override
	public void delThirdOrder(String orderId, String sessionKey) throws Exception {
		EzhuHttpclient eZhu  = null;
		OrderInfo order= orderInfoMapper.findByOrderId(orderId);
		String code = "47";
		switch (order.getOrderStore()){
		case "1":code="48";
			break;
		case "2":code="47";
			break;
		case "3":code="46";
			break;
		default: code="47";
		break;	
	}
		try {
			if(StringUtils.isNotBlank(orderId)){
				eZhu = new EzhuHttpclient("DelTrade", sessionKey);
				eZhu.put("fbillno", StringUtils.isNotBlank(order.getChildId())?order.getChildId():orderId);
				eZhu.put("fshopid", code);
				String body = eZhu.getResponseBody();
				JSONObject jo = JSONObject.parseObject(body);
				String errmsg = jo.getString("errmsg");
				if(StringUtils.isBlank(errmsg)){
					orderInfoMapper.cancelEzhuOrder(orderId);
				}else{
					throw new GdbmroException(-1, errmsg);
				}
			}else{
				throw new GdbmroException(-1,"订单号不能为空");
			}
		} finally{
			saveMongodb(eZhu.setDescribe("删除E助订单"));
		}
	}

	@Override
	public String getInvoice(String startTime,String endTime, String sessionKey){
		EzhuHttpclient eZhu  = null;
		try {
			if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
				eZhu = new EzhuHttpclient("Gph_GetALLJsNo", sessionKey);
				eZhu.put("fbegintime", startTime);
				eZhu.put("fendtime", endTime);
				String body = eZhu.getResponseBody();
				JSONObject jo = JSONObject.parseObject(body);
				String errmsg = jo.getString("errmsg");
				if(StringUtils.isBlank(errmsg)){
					return jo.toString();
				}else{
					throw new GdbmroException(-1, errmsg);
				}
			}else{
				throw new GdbmroException(-1,"时间段不能为空");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			saveMongodb(eZhu.setDescribe("查询E助发票"));
		}
		return "{\"code\":\"500\"}";
	}

	@Override
	public String getLogics(String startTime,String endTime, String sessionKey){
		EzhuHttpclient eZhu  = null;
		try {
			if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
				eZhu = new EzhuHttpclient("Gph_GetALLExpNumber", sessionKey);
				eZhu.put("fbegintime", startTime);
				eZhu.put("fendtime", endTime);
				String body = eZhu.getResponseBody();
				JSONObject jo = JSONObject.parseObject(body);
				String errmsg = jo.getString("errmsg");
				if(StringUtils.isBlank(errmsg)){
					return jo.toString();
				}else{
					throw new GdbmroException(-1, errmsg);
				}
			}else{
				throw new GdbmroException(-1,"时间段不能为空");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			saveMongodb(eZhu.setDescribe("查询E助快递"));
		}
		return "{\"code\":\"500\"}";
	}

	@Override
	public String financesaveOrderToERP(String id, String sessionKey) throws Exception {
		String  tradeStr = null;
		List<Item> items =new ArrayList<Item>();
		if(StringUtils.isNotBlank(id)){
			Trade trade = orderInfoMapper.saveOrderToERP(id);
			if(trade==null){
				throw new Exception("请检查订单号,订单不存在");
			}
			String originid = trade.getFbillno();
			if(originid.contains("-")){
				originid = originid.substring(0, originid.indexOf("-"));
			}
			Adress addr = regionServiceI.getRegionbyOrderId(id);
			trade.setFreceiverstate(addr.getProvince());
			trade.setFreceivercity(addr.getCity());
			trade.setFreceiverdistrict(addr.getArea());
			trade.setFreceiver(addr.getConsignee());
			trade.setFreceivermobile(addr.getMobile());
			trade.setFreceiveraddress(addr.getAddress());
			DESCoder.instanceMobile();
			Map<String, String> inv = new HashMap<String, String>();
			trade.setInvoiceaddress(inv);
			try {
				if( (!StringUtils.equals("0", trade.getFinvreceiverstate())) && (!StringUtils.equals("0", trade.getFinvreceivercity())) ){
					inv.put("finvreceiver", trade.getFinvreceiver());
					inv.put("finvreceiveraddress", trade.getFinvreceiveraddress());
					inv.put("finvreceiverstate", regionMapper.selectByPrimaryKey(Integer.parseInt(trade.getFinvreceiverstate())).getRegionName());
					inv.put("finvreceivercity", regionMapper.selectByPrimaryKey(Integer.parseInt(trade.getFinvreceivercity())).getRegionName());
					inv.put("finvreceiverdistrict", regionMapper.selectByPrimaryKey(Integer.parseInt(trade.getFinvreceiverdistrict())).getRegionName());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			trade.setFreceiverphone(StringUtils.isNotBlank(trade.getFreceiverphone())?DESCoder.decoder(trade.getFreceiverphone()):"");
			trade.setFinvoiceregtel(StringUtils.isNotBlank(trade.getFinvoiceregtel())?DESCoder.decoder(trade.getFinvoiceregtel()):"");
			inv.put("finvreceivermobile", StringUtils.isNotBlank(trade.getFinvreceivermobile())?DESCoder.decoder(trade.getFinvreceivermobile()):"");
			trade.setFshoptradestatus(2);
			/** 供应链平台 47 商城46*/
			switch (trade.getFshopid()){
				case 1:trade.setFshopid(48);
					break;
				case 2:trade.setFshopid(47);
					break;
				case 3:trade.setFshopid(46);
					break;
				default: trade.setFshopid(47);
				break;	
			}
			if(trade.getFempnumber()!=null && !"".equals(trade.getFempnumber())){
				Staff staff = staffMapper.selectByPrimaryKey(trade.getFempnumber());
				trade.setFempname(staff.getStaffName());
			}
			List<OrderGoods> goodsList = orderGoodsMapper.selectGoodsByOrderId(id);
			for(OrderGoods goods : goodsList){
				BigDecimal fprice = goods.getSaleUnitPrice()==null?goods.getUnitPrice() : goods.getSaleUnitPrice();
				fprice	= goods.getTail()!=null && StringUtils.isNotBlank(goods.getTail().toString()) ? goods.getTail() : fprice;
				Item item = new Item();
				item.setFskuname(goods.getTitle());
				item.setFouternumber(goods.getBuyNo());
				item.setFqty(goods.getNum());
				item.setFprice(fprice);
				item.setFamount(fprice.multiply(new BigDecimal(goods.getNum())));
				items.add(item);
			}
			trade.setItems(items);
			trade.setFexpcomnum("SF");
			tradeStr = JSONObject.toJSONString(trade);
		}else{
			throw new Exception("订单ID不能为空");
		}
		String fid = ErpConnect.postFrom(sessionKey,"AddTrade",tradeStr,"trade").getString("fid");
		OrderInfo order= new OrderInfo();
		order.setId(Long.parseLong(id));
		order.setThirdId(fid);
		orderInfoMapper.update(order);
		return fid;
	}
	
	private List<Long> getOriginalId(List<Long> ids,Long id) {
		OrderInfo orderInfo = orderInfoMapper.findById(id);
		String originalOrderId = orderInfo.getOriginalOrderId();
		if(StringUtils.isNotBlank(originalOrderId)){
			if(originalOrderId.contains(",")){
				String[] originalIds = originalOrderId.split(",");
				for (String originalId : originalIds) {
					if(originalId.contains("-")){
						originalId = originalId.substring(0, originalId.indexOf("-"));
					}
					getOriginalId(ids, Long.parseLong(originalId));
				}
			}else {
				if(originalOrderId.contains("-")){
					originalOrderId = originalOrderId.substring(0, originalOrderId.indexOf("-"));
				}
				getOriginalId(ids, Long.parseLong(originalOrderId));
			}
		}else {
			ids.add(id);
		}
		return ids;
	}



	/**
	 * 根据吉勤ERP返回的出库状态 来更新GDB的订单
	 */
	@Override
	public Map<String, Object> updateOrderLogis(String erpOrderNo) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", -1);
		map.put("msg", "not found right ERP stockOut info!");
		Map<String,Object> logicMap  = queryERPOrderStockOut(erpOrderNo);
		if(!logicMap.isEmpty() && logicMap.get("logistics_code")!=null ){
			//更新GDB订单发货信息
			OrderInfo gdbOrder = orderInfoDao.getOrderInfoByThirdId(erpOrderNo);
			if(gdbOrder.getLogisticsCode()==null && gdbOrder.getLogisticsMultiCode()==null || StringUtils.isBlank(gdbOrder.getLogisticsCode()+gdbOrder.getLogisticsMultiCode()) ){
				gdbOrder.setOrderStatus(Status.TORECEIVETHEGOODS.getValue());
				gdbOrder.setShippingStatus(Status.SHIPPED.getValue());
				if(logicMap.get("logistics_code")==null || StringUtils.isBlank(logicMap.get("logistics_code").toString()) ) {
					return map;
				}
				if(logicMap.get("logistics_code")!=null) {
					if (logicMap.get("logistics_code").toString().contains(",")) {
						gdbOrder.setLogisticsMultiCode(logicMap.get("logistics_code").toString());
					}else{
						gdbOrder.setLogisticsCode(logicMap.get("logistics_code").toString());
					}
				}
				LogisticsCompany logisCompany = logisticsCompanyDao.fingByName(logicMap.get("seller_logistics_name").toString());
				if(logisCompany==null){
					return map;
				}
				gdbOrder.setSellerLogisticsId(logisCompany.getId());
				if(logicMap.get("shipping_time")!=null){
					String dateStr = logicMap.get("shipping_time").toString();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					gdbOrder.setShippingTime(sdf.parse(dateStr));
				}else{
					gdbOrder.setShippingTime(new Date(System.currentTimeMillis()));
				}
				orderInfoDao.update(gdbOrder);
				map.put("code",0);
				map.put("msg","success");
			}
		}
		return map;
	}



	/**
	 * 根据吉勤ERP返回的出库状态 来更新GDB的订单(Job 用,更新到order_info)
	 */
/*	@Override
	public void updateOrdersLogisJobBak() {
		List<String> gdbOrderIds = new ArrayList<String>();
		try{
			LOGGER.info("Jiqing Erp Job[update order logsic info] star at "+new Date()+"\n");
			gdbOrderIds = orderInfoDao.getOrdersWithThirdIdAndNoSent();
			if(gdbOrderIds==null || gdbOrderIds.isEmpty()){
				LOGGER.info("there no orders need to update!");
				return;
			}
		} catch (Exception e) {
			LOGGER.info("Error happen when Jiqing Erp Job:"+e.getMessage());
		}
		for(String erpOrderNo:gdbOrderIds) {
			try {
				Map<String, Object> logicMap = queryERPOrderStockOut(erpOrderNo);
				if (!logicMap.isEmpty() && logicMap.get("logistics_code") != null) {
					//更新GDB订单发货信息
					OrderInfo gdbOrder = orderInfoDao.getOrderInfoByThirdId(erpOrderNo);
					if (gdbOrder.getLogisticsCode() == null && gdbOrder.getLogisticsMultiCode() == null || StringUtils.isBlank(gdbOrder.getLogisticsCode() + gdbOrder.getLogisticsMultiCode())) {
						gdbOrder.setOrderStatus(Status.TORECEIVETHEGOODS.getValue());
						gdbOrder.setShippingStatus(Status.SHIPPED.getValue());
						if (logicMap.get("logistics_code") == null || StringUtils.isBlank(logicMap.get("logistics_code").toString())) {
							LOGGER.info("ERP not reponse parameter [logistics_code]!");
							continue;
						}
						if (logicMap.get("logistics_code") != null) {
							if (logicMap.get("logistics_code").toString().contains(",")) {
								gdbOrder.setLogisticsMultiCode(logicMap.get("logistics_code").toString());
							} else {
								gdbOrder.setLogisticsCode(logicMap.get("logistics_code").toString());
							}
						}
						LogisticsCompany logisCompany = logisticsCompanyDao.fingByName(logicMap.get("seller_logistics_name").toString());
						if (logisCompany == null) {
							LOGGER.info("not found right logiscCompany in GDB!");
							continue;
						}
						gdbOrder.setSellerLogisticsId(logisCompany.getId());
						if (logicMap.get("shipping_time") != null) {
							String dateStr = logicMap.get("shipping_time").toString();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							gdbOrder.setShippingTime(sdf.parse(dateStr));
						} else {
							gdbOrder.setShippingTime(new Date(System.currentTimeMillis()));
						}
						orderInfoDao.update(gdbOrder);
						LOGGER.info("[" + erpOrderNo + "]Jiqing Erp Job[update order logsic info] end at " + new Date() + "\n");
					}
				}
			} catch (Exception e) {
				LOGGER.info("Error happen when Jiqing Erp Job:" + e.getMessage());
				continue;
			}
		}
	}*/



	/**
	 * 根据吉勤ERP返回的出库状态 来更新GDB的订单(Job 用,适用于部分出库)
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateOrdersLogisJob() throws Exception{
			List<String> gdbOrderIds = new ArrayList<String>();
			LOGGER.info("Jiqing Erp Job[update order logsic info] star at "+new Date()+"\n");
			gdbOrderIds = orderInfoDao.getOrdersWithThirdIdAndNoSent();
			if(gdbOrderIds==null || gdbOrderIds.isEmpty()){
				LOGGER.info("There no orders need to update!");
				return;
			}
		   for(String erpOrderNo:gdbOrderIds) {
			   List<Map> logicLists = queryERPOrderStockOutInfo(erpOrderNo);
			   if (!logicLists.isEmpty() && logicLists != null) {
				   OrderInfo gdbOrder = orderInfoDao.getOrderInfoByThirdId(erpOrderNo);
				   List<OrderExpress> oExpressList = orderExpressMapper.findByOriginalOrderId(gdbOrder.getId());

				   if ( (oExpressList == null || oExpressList.isEmpty()) || filterDiffLogisCode(logicLists)>oExpressList.size()) {//防止Job重复更新(1，分步后一次性更新到库的 2，分布出库并分布更新到库的)
					   //更新GDB订单发货信息(表 order_express，order_express_goods )
					   for (Map<String, Object> outInfo : logicLists) {
						   if (outInfo.get("BaFhDh") == null || StringUtils.isEmpty(outInfo.get("BaFhDh").toString())) {
							   continue;
						   }
						   OrderExpress oExp = orderExpressMapper.findByOriginalOrderIdAndFexpNoAndFexpcomname(gdbOrder.getId(),outInfo.get("BaFhDh").toString(),outInfo.get("BaFhFs").toString());
						   if(oExp==null || oExp.getId()==null){
							   oExp = new OrderExpress();
							   oExp.setShopName("工电宝商城");//店铺id 46:工电宝品牌分销店  47:工电宝供应链平台  48:工电宝商城
							   oExp.setOriginalOrderId(gdbOrder.getId());
							   LogisticsCompany lc = logisticsCompanyMapper.fingByName(outInfo.get("BaFhFs").toString());
							   oExp.setFexpcomname(lc!=null?lc.getLogisticsCode():outInfo.get("BaFhFs").toString());
							   oExp.setFexpNo(outInfo.get("BaFhDh").toString());
							   oExp.setFname(logicLists.size() == 1 ? "主面单" : "子面单");
							   oExp.setFexpprttime(new Date());
							   oExp.setStatus("1");
							   orderExpressMapper.insert(oExp);
						   }
						   OrderExpressGoods oeGoods = orderExpressGoodsMapper.findByOrderExpressIdAndSyncCode(String.valueOf(gdbOrder.getId()),outInfo.get("GaProID").toString());
						   int erpNum = (int) filterDiffLogisNum(logicLists).get(oExp.getFexpNo()+erpOutSplitStr+outInfo.get("GaProID").toString());
						   if(oeGoods==null){
							   OrderExpressGoods expressGood = new OrderExpressGoods();
							   expressGood.setOrderExpressId(oExp.getId());
							   expressGood.setSyncCode(outInfo.get("GaProID").toString());
							   expressGood.setNum(Integer.valueOf(outInfo.get("Bnumb").toString()));
							   orderExpressGoodsMapper.insert(expressGood);
						   }else if(oeGoods!=null && oeGoods.getNum()<erpNum){//同一型号，同一物流单号，分两次处理发货
							   oeGoods.setNum(erpNum);
							   orderExpressGoodsMapper.updateByPrimaryKey(oeGoods);
						   }
					   }
					   gdbOrder.setOrderStatus(Status.TORECEIVETHEGOODS.getValue());
					   gdbOrder.setShippingStatus(Status.SHIPPED.getValue());
					   gdbOrder.setShippingTime(new Date());
					   orderInfoDao.update(gdbOrder);
				   }
			   }
		   }
			LOGGER.info("Jiqing Erp Job[update order logsic info] end at " + new Date() + "\n");
	}


	/**
	 * 根据吉勤ERP返回的出库信息，筛选出不同的物流单号
	 * @param logicLists
	 * @return
	 */
	public int filterDiffLogisCode(List<Map> logicLists){
		Set<String> logiscSet = new HashSet<String>();
		for( Map Litem:logicLists ){
			logiscSet.add(Litem.get("BaFhDh").toString());
		}
		return logiscSet.size();
	}

	/**
	 * 根据吉勤ERP返回的出库信息，筛选出不同的物流单号+商品号，个数
	 * @param logicLists
	 * @return
	 */
	public Map<String,Object> filterDiffLogisNum(List<Map> logicLists){
		Map<String,Object> map = new HashMap<String,Object>();
		for( Map Litem:logicLists ){
			String itemKey = Litem.get("BaFhDh").toString()+erpOutSplitStr+Litem.get("GaProID").toString();
			if(map.get(itemKey)!=null){
				map.put(itemKey,Integer.parseInt(map.get(itemKey).toString())+Integer.parseInt(Litem.get("Bnumb").toString()));
			}else{
				map.put(itemKey,Litem.get("Bnumb").toString());
			}
		}
		return map;
	}



	/**
	 * 同步商品SKU 到 吉勤ERP(Job 用)
	 */
	@Override
	public void updateErpGoodSku() {
		/**先查询没有syn_code的商品，然后循环调用接口新增到erp*/
		LOGGER.info("Update Sku To Erp job star at "+new Date(System.currentTimeMillis())+"\n");
		Long preSyncGoodsTotal = goodsMapper.selectNoSynCodeGoodsTotal();
		LOGGER.info("Left "+preSyncGoodsTotal+" Good Records Need To Update!");
		List<Goods> list = new ArrayList<Goods>();
		list = goodsMapper.selectNoSynCodeGoods(0,1000);
		try {
			List<Map<String,Object>> param= transObjectForList(list);
			restTemplateInvoke(param,"RestfulCreateProduceEx");
			LOGGER.info("Update Sku To Erp job end at"+new Date(System.currentTimeMillis())+"\n");
		} catch (JFigException e) {
			LOGGER.info("Error Happen When Update Sku To Erp job "+e.getMessage());
		}
	}



	public List<Map> queryERPOrderStockOutInfo(String erpOrderNo) throws Exception {
		List<Map> produceList = new ArrayList<Map>();
		Map<String,Object> map = new HashMap<String,Object>();
		if(StringUtils.isEmpty(erpOrderNo)){
			return Collections.emptyList();
		}
		map.put("BID",erpOrderNo);
		String produceListStr = restTemplateObjectInvoke(map,"RestfulRefreshBargainInStockOut");
		if(StringUtils.isNotBlank(produceListStr)){
			LOGGER.info("查询ERP订单["+erpOrderNo+"]出库信息:\n"+produceListStr);
			JSONObject resultObj = JSONObject.parseObject(produceListStr);
			Integer result = (Integer)resultObj.get("Result");
			if(result!=0) {
				produceList = JSONObject.parseArray(resultObj.getString("ProduceList"), Map.class);
			}
		}
		return produceList;
	}



	@Override
	public Map<String,Object> queryERPOrderStockOut(String erpOrderNo) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		if(StringUtils.isEmpty(erpOrderNo)){
			return map;
		}
		map.put("BID",erpOrderNo);
		String produceListStr = restTemplateObjectInvoke(map,"RestfulRefreshBargainInStockOut");
		if(StringUtils.isNotBlank(produceListStr)){
			LOGGER.info("查询ERP订单["+erpOrderNo+"]出库信息:\n"+produceListStr);
			JSONObject resultObj = JSONObject.parseObject(produceListStr);
			Integer result = (Integer)resultObj.get("Result");
			if(result!=0) {
				List<Map> produceList = JSONObject.parseArray(resultObj.getString("ProduceList"), Map.class);
				if (produceList != null && !produceList.isEmpty()) {
					for(Map<String,Object> item : produceList){
						filterMap(item,map);
					}
				}
			}
		}
		return map;
	}

	private void filterMap(Map<String,Object> targetMap,Map<String,Object> resultMap){
		if( targetMap.get("BaFhFs")!=null ) {  //ERP(发货方式) - GDB（物流公司）
			if(resultMap.get("seller_logistics_name")==null || targetMap.get("BaFhFs").equals(resultMap.get("seller_logistics_name"))) {//同一物流公司
				resultMap.put("seller_logistics_name", targetMap.get("BaFhFs"));
				if (targetMap.get("BaFhDh")!=null && !targetMap.get("BaFhDh").equals(resultMap.get("logistics_code"))) {  //ERP（物流单号）- GDB（物流单号）
					if (resultMap.get("logistics_code") == null) {
						resultMap.put("logistics_code", targetMap.get("BaFhDh"));
					} else {
						resultMap.put("logistics_code", resultMap.get("logistics_code") + "," + targetMap.get("BaFhDh"));
					}
				}
				if (targetMap.get("shipping_time")==null){
					resultMap.put("shipping_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				}else{
					resultMap.put("shipping_time",targetMap.get("Bdate"));//ERP（发货时间）- GDB（发货时间）
				}
			}
		}
	}

	/**
	 *订单中的商品没有同步到ERP，先同步商品
	 * @return
	 */
	private boolean syncGoodsToErp(String goodIdsList){
		List<Goods> list = goodsMapper.selectTheNoSynCodeGoods(goodIdsList);
		try {
			List<Map<String,Object>> param= transObjectForList(list);
			restTemplateInvoke(param,"RestfulCreateProduceEx");
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return false;
		}
	}


	/**
	 * 获得收货地址的省市区具体名称
	 */
	@SuppressWarnings("rawtypes")
	public String getFullDeliveryAddess(String consignAddress){
		StringBuffer sbf = new StringBuffer();
		
		if(StringUtils.isNotEmpty(consignAddress)){
			Map addObj;
			try{
				addObj = JSONObject.parseObject(consignAddress,Map.class);
			}catch(Exception e){
				return consignAddress;
			}
			
			if(addObj.get("provinceId")!=null && StringUtils.isNotEmpty(addObj.get("provinceId").toString())){
				sbf.append(regionMapper.selectRegionString(addObj.get("provinceId").toString()) );
			}
			if(addObj.get("cityId")!=null && StringUtils.isNotEmpty(addObj.get("cityId").toString())){
				sbf.append(regionMapper.selectRegionString(addObj.get("cityId").toString()) );
			}
			if(addObj.get("areaId")!=null && StringUtils.isNotEmpty(addObj.get("areaId").toString())){
				sbf.append(regionMapper.selectRegionString(addObj.get("areaId").toString()) );
			}
			if(addObj.get("townId")!=null && (int) addObj.get("townId")>0){
				sbf.append(regionMapper.selectRegionString(addObj.get("townId").toString()) );
			}
			if(addObj.get("address")!=null && StringUtils.isNotEmpty(addObj.get("address").toString())){
				sbf.append(addObj.get("address").toString());
			}
		}
		return sbf.toString();
	}



}
