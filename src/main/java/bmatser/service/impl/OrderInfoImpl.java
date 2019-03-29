package bmatser.service.impl;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mongodb.util.JSON;
import bmatser.util.alibaba.StringUtil;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Maps;
import bmatser.dao.*;
import bmatser.model.*;
import bmatser.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.igfay.jfig.JFig;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;


import bmatser.pageModel.MakeOrderPage;
import bmatser.pageModel.OrderDealPage;
import bmatser.pageModel.OrderInfoPage;
import bmatser.pageModel.PageMode;
import bmatser.pageModel.PaymentBillPage;
import bmatser.pageModel.ShoppingCartPage;
import bmatser.pageModel.ShoppingCartShowPage;
import bmatser.pageModel.util.MakeOrderGoodsPageUtil;
import bmatser.pageModel.util.MakeOrderPageUtil;
import bmatser.util.AmountToChar;
import bmatser.util.CnUpperCaser;
import bmatser.util.Constants;
import bmatser.util.DESCoder;
import bmatser.util.DateTypeHelper;
import bmatser.util.Delivery;
import bmatser.util.HttpclientUtil;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.NumberToRBM;
import bmatser.util.ObjectUtil;
import bmatser.util.PaymentIDProduce;
import bmatser.util.PropertyUtil;
import bmatser.util.ResponseData;
import bmatser.util.ResponseMsg;
import bmatser.util.HttpclientUtil.Method;


/**
 * 订单操作类
 * @author felx
 * 2017-7-28
 */
@Service("orderService")
public class OrderInfoImpl implements OrderInfoI {
	
	Logger log=Logger.getLogger(OrderInfoImpl.class);
	
//	@Autowired
//	private MongoTemplate mongoTemplate;
	
	@Autowired
	private AmountAlgorithmI amountAlgorithmI;
	
	@Autowired
	private DealerCashMapper dealerCashMapper;
	
	@Autowired
	private ShoppingCartMapper cartDao;
	
	@Autowired
	private OrderInfoMapper orderDao;
	
	@Autowired
	private PaymentBillMapper paymentBillDao;
	@Autowired
	private GoodsActivityI activityService;
	
	@Autowired
	private DealerScoreMapper scoreDao;
	
	@Autowired
	private DealerMapper dealerDao;
	
	@Autowired
	private ShoppingCartI cartService;
	
	@Autowired
	private OrderGoodsMapper orderGoodsDao;
	
	@Autowired
	private BuyerInvoiceI invoiceService;
	
	@Autowired
	private OrderInvoiceMapper orderInvoiceMapper;
	@Autowired
	private BuyerConsignAddressMapper addressMapper;
	@Autowired
	private OrderExpressGoodsMapper orderExpressGoodsMapper;
	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private LogisticsCompanyMapper LogisticsCompanyI;
	@Autowired
	private SellerGoodsMapper sellerGoodsI;
	@Autowired
	private PaymentBillMapper paymentBillMapper;
	@Resource
	private AddPaymentBillServiceI addPaymentBillService;
	@Resource
	private OrderInfoI orderService; 
	@Autowired
	private DealerCashMapper cashDao;
	@Autowired
	private BuyerInvoiceMapper buyerInvoiceI;
	@Autowired
	private DealerBalanceRecordMapper balanceRecordMapper;
	@Autowired
	private GbeansServiceI gbeansServiceI;
	@Autowired
	private ShunfengOperateServiceI sfServiceI;
	
	
	@Value("#{configProperties['logistics']}")//物流信息地址
	private String logistics;
	@Value("#{configProperties['kdId']}")//物流信息地址
	private String kdId;//快递一百身份Key
	
	private final String[] keyArray = {"工电宝","团购"};
/*	@Autowired
	private OrderInvoiceMapper orderInvoiceDao;*/
	
	private String scoreRateJfig = JFig.getInstance().getValue("system_options", "SCORE_RATE", "0.1");
	
	private String baseUrl = JFig.getInstance().getValue("system_options", "BASE_URL", "http://www.bmatser.com/");
	
	private static final String THIRDTRADECODE="thirdTradeCode";
	
	private static final String COMPANYACCOUNT="companyAccount";
	@Autowired
	public GoodsPackageMapper goodsPackageDao;
	/**
	 * 获取指定用户的订单数量（各状态 1:待付款  2：待发货  3：待收货  6：已完成）
	 * @param buyerId
	 * @return list
	 * 2017-7-28
	 * @throws Exception 
	 *
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List findCountByStatus(Integer buyerId) throws Exception {
		if(buyerId == null){
			throw new Exception("请登录");
		}
		return orderDao.findCountByStatus(buyerId);
	}

    @Override
    public String checkUser(String dealerId) {


        return orderDao.getCheckUser(dealerId);
    }

    @Override
    public Map<String,Object> getOrderNumForIos(Integer dealerId) throws Exception {
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> result = Maps.newHashMap();
        result.put("1",0);
        result.put("2",0);
        result.put("3",0);
        result.put("6",0);
        try{
            list =  orderDao.getOrderNum(dealerId);
            for (Map<String,Object> map:list) {
                if(map.get("status").equals("1")){
                    result.put("1",map.get("num"));
                }
                if(map.get("status").equals("2")){
                    result.put("2",map.get("num"));
                }
                if(map.get("status").equals("3")){
                    result.put("3",map.get("num"));
                }
                if(map.get("status").equals("6")){
                    result.put("6",map.get("num"));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

	/**
	 * 获取指定用户的订单列表（各状态）
	 * @param buyerId
	 * @param status
	 * @return map
	 * 2017-7-28
	 * @throws Exception 
	 *
	 */
/*	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Object> findOrders(Integer buyerId, Integer status, Integer page, Integer rows) throws Exception {
		if(buyerId == null){
			throw new Exception("请登录");
		}
		if(status.intValue() == 0){
			status = null;
		}
		Map map = new HashMap();
		List orderList = orderDao.findOrders(buyerId, status, page, rows);
		Map tempMap = new HashMap();
		for(Object obj : orderList){
			Map orderMap = (Map)obj;
			String orderId = String.valueOf(orderMap.get("orderId"));
			String adress = orderMap.get("consignAddressInfo")!=null?orderMap.get("consignAddressInfo").toString() : "";
			try {
				JSONObject jo = JSONObject.parseObject(adress);
				orderMap.put("consignee",	 jo.get("consignee").toString());
				orderMap.put("mobile",	 jo.get("mobile").toString());
			} catch (Exception e) {
				Map consignAddressPage  = addressMapper.findById(Integer.parseInt(orderMap.get("consignAddressId").toString()));
				orderMap.put("consignee",	 consignAddressPage.get("consignee"));
				orderMap.put("mobile",	 consignAddressPage.get("mobile"));
			}
			orderMap.get("consignAddressInfo");
			if(tempMap.containsKey(orderId)){
				*//**如果临时map里存在，则修改*//*
				Map orderInfoMap = (Map)tempMap.get(orderId);
				List orderGoodsList = (List)orderInfoMap.get("orderGoods");
				Map orderGoodsMap = convertInfoGoods(baseUrl, orderMap);
				orderGoodsList.add(orderGoodsMap);
			} else{
				*//**如果临时map里不存在，则新增*//*
				Map orderInfoMap = new HashMap();
				orderInfoMap.put("orderId", orderMap.get("orderId"));
				orderInfoMap.put("amount", orderMap.get("amount"));
				orderInfoMap.put("status", orderMap.get("status"));
				orderInfoMap.put("orderTime", orderMap.get("orderTime"));
				orderInfoMap.put("consignee", orderMap.get("consignee"));
				orderInfoMap.put("mobile", orderMap.get("mobile"));
				Map orderGoodsMap = convertInfoGoods(baseUrl, orderMap);
				List orderGoodsList = new ArrayList();
				orderGoodsList.add(orderGoodsMap);
				orderInfoMap.put("orderGoods", orderGoodsList);
				tempMap.put(orderId, orderInfoMap);
			}
			
		}
		List orderInfos = new ArrayList();
		if(tempMap != null && !tempMap.isEmpty()){
			*//**将订单放到list里*//*
			Iterator iter = tempMap.entrySet().iterator();
			while(iter.hasNext()){
				Map.Entry entry = (Map.Entry)iter.next();
				orderInfos.add(entry.getValue());
			}
		}
		map.put("rows", orderInfos);
		Long count = orderDao.countOrders(buyerId, status);
		map.put("total", count);
		return map;
	}*/

	/**
	 * 订单商品转换
	 * @param baseUrl
	 * @param orderMap
	 * @return Map 
	 * 2017-7-29
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map convertInfoGoods(String baseUrl, Map orderMap) {
		Map orderGoodsMap = new HashMap();
		orderGoodsMap.put("goodsId", orderMap.get("goodsId"));
		orderGoodsMap.put("sellerGoodsId", orderMap.get("sellerGoodsId"));
		if(orderMap.get("sellerGoodsId")!=null&&!"".equals(orderMap.get("sellerGoodsId"))&&!"0".equals(orderMap.get("sellerGoodsId"))){
			Map map  = sellerGoodsI.findStoreGoodsById(orderMap.get("sellerGoodsId").toString());
			if(map!=null){
				orderGoodsMap.put("measure", map.get("measure"));
				orderGoodsMap.put("brandName", map.get("brandName"));
				orderGoodsMap.put("shopName", map.get("shopName"));
				orderGoodsMap.put("alias", map.get("alias"));
				orderMap.put("buyNo", map.get("buyNo"));
			}
		}else{
			orderGoodsMap.put("measure", "个");
		}
		String gsId = orderMap.get("gsId").toString();
		if("9407609".equals(gsId)){
			orderMap.put("isJd", 1);
			orderGoodsMap.put("image", "http://img10.360buyimg.com/imgzone/"+orderMap.get("image"));
		}else{
			orderGoodsMap.put("image", baseUrl+"/goodsImg/"+orderMap.get("image"));
		}
		orderGoodsMap.put("sellerId", gsId);
		orderGoodsMap.put("title", orderMap.get("title"));
		orderGoodsMap.put("model", orderMap.get("model"));
		orderGoodsMap.put("num", orderMap.get("num"));
		BigDecimal unitPrice = (BigDecimal)orderMap.get("unitPrice");
		orderGoodsMap.put("unitPrice", orderMap.get("unitPrice"));
		orderGoodsMap.put("buyNo", orderMap.get("buyNo"));
		BigDecimal saleUnitPrice = (BigDecimal)orderMap.get("saleUnitPrice");
		if(saleUnitPrice == null){
			saleUnitPrice = unitPrice;
		}
		orderGoodsMap.put("saleUnitPrice", orderMap.get("unitPrice"));
		orderGoodsMap.put("salePrice", orderMap.get("unitPrice"));
		return orderGoodsMap;
	}

	/**
	 * 订单详情
	 * @param orderId
	 * @return list
	 * 2017-7-29
	 *
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getOrderDetail(Long orderId,String dealerId,String type) {
		if(orderId == null){
			return null;
		}
		String baseUrl=JFig.getInstance().getValue("system_options", "BASE_URL", "http://www.bmatser.com/");
		Map map = new HashMap();
		if(StringUtils.isBlank(type)){
			return null;
		}//1:供应商2：零售商
		List orderList=new ArrayList();
		if(type.equals("2")){
			orderList = orderDao.getOrderDetail(orderId,dealerId);
		}else if(type.equals("1")){
			orderList = orderDao.getSellerOrderDetail(orderId,dealerId);
		}
		
		Map tempMap = new HashMap();
		for(Object obj : orderList){
			Map orderMap = (Map)obj;
			orderMap.put("isJd",0);
			
			if(tempMap.containsKey(orderId)){
				/**如果临时map里存在，则修改*/
				Map orderInfoMap = (Map)tempMap.get(orderId);
				List orderGoodsList = (List)orderInfoMap.get("orderGoods");
				Map orderGoodsMap = convertInfoGoods(baseUrl, orderMap);
				if("1".equals(orderMap.get("isJd").toString())){
					orderInfoMap.put("isJd", 1);
				}
				orderGoodsList.add(orderGoodsMap);
			} else{
				/**如果临时map里不存在，则新增*/
				Map orderInfoMap = new HashMap();
				OrderInvoice orderinv  = orderInvoiceMapper.findByOrderId(Long.parseLong(orderMap.get("orderId").toString()));
				OrderInvoice invoice = orderInvoiceMapper.findByOrderId(Long.parseLong(orderMap.get("orderId").toString()));
				if(invoice!=null){
					orderInfoMap.put("invContent", invoice.getInvContent());
					orderInfoMap.put("taxpayeRno", invoice.getTaxpayeRno());
					orderInfoMap.put("regAddress", invoice.getRegAddress());
					orderInfoMap.put("regTelphone", invoice.getRegTelphone());
					orderInfoMap.put("bank", invoice.getBank());
					orderInfoMap.put("bankAccount", invoice.getBankAccount());
					orderInfoMap.put("recvName", invoice.getRecvName());
					orderInfoMap.put("recvMobile", invoice.getRecvMobile());
					orderInfoMap.put("recvProvince", invoice.getRecvProvince());
					orderInfoMap.put("recvCity", invoice.getRecvCity());
					orderInfoMap.put("recvArea", invoice.getRecvArea());
					orderInfoMap.put("recvAddress", invoice.getRecvAddress());
					orderInfoMap.put("invType", orderinv.getInvType());
				}else{
					orderInfoMap.put("invContent", "");
					orderInfoMap.put("taxpayeRno", "");
					orderInfoMap.put("regAddress", "");
					orderInfoMap.put("regTelphone", "");
					orderInfoMap.put("bank", "");
					orderInfoMap.put("bankAccount", "");
					orderInfoMap.put("recvName", "");
					orderInfoMap.put("recvMobile", "");
					orderInfoMap.put("recvProvince","");
					orderInfoMap.put("recvCity","");
					orderInfoMap.put("recvArea","");
					orderInfoMap.put("recvAddress","");
				}
				if("1".equals(orderMap.get("isJd").toString())){
					orderInfoMap.put("isJd", 1);
				}else{
					orderInfoMap.put("isJd", 0);
				}
				orderInfoMap.put("orderId", orderMap.get("orderId"));
				OrderInfo order = orderDao.findById(Long.parseLong(orderMap.get("orderId").toString()));
				if(order!=null){
					orderInfoMap.put("orderTime", order.getOrderTime());
					orderInfoMap.put("payTime", order.getPayTime());
					orderInfoMap.put("goodsSaleAmount", order.getGoodsSaleAmount());
					orderInfoMap.put("buyerRemark", order.getBuyerRemark());
					orderInfoMap.put("isIssueInvoice", order.getIsIssueInvoice());
					orderInfoMap.put("shippingTime", order.getShippingTime());
					orderInfoMap.put("saleAmount", order.getSaleAmount()==null?0:order.getSaleAmount());
				}
				if(order.getSellerLogisticsId()!=null && !"".equals(order.getSellerLogisticsId())){
					LogisticsCompany  logistics= LogisticsCompanyI.fingById(order.getSellerLogisticsId());
					orderInfoMap.put("logisticsName", "");
					orderInfoMap.put("logisticsCode", "");
					orderInfoMap.put("logisticsRemark", "");
					orderInfoMap.put("logisticsUrl", "");
					if(logistics!=null){
						orderInfoMap.put("logisticsName", logistics.getLogisticsName());
						orderInfoMap.put("logisticsNo", logistics.getLogisticsCode());
						orderInfoMap.put("logisticsCode", order.getLogisticsCode());
						orderInfoMap.put("logisticsRemark", order.getLogisticsRemark());
						try {
							orderInfoMap.put("logisticsUrl", Delivery.search(logistics.getLogisticsCode(),order.getLogisticsCode()));
						} catch (Exception e) {
							orderInfoMap.put("logisticsUrl", "");
						}
					}
				}else{
					orderInfoMap.put("logisticsName", "");
					orderInfoMap.put("logisticsCode", "");
					orderInfoMap.put("logisticsRemark", "");
					orderInfoMap.put("logisticsUrl", "");
				}
				/*Map dealer = dealerDao.findLoginInfo(order.getSellerId().toString());
				if(dealer!=null){
					orderInfoMap.put("shopName", dealer.get("shopName"));
					orderInfoMap.put("sellerProvinceId", dealer.get("provinceId"));
					orderInfoMap.put("sellerCityId", dealer.get("cityId"));
					orderInfoMap.put("sellerAreaId", dealer.get("areaId"));
					orderInfoMap.put("sellerAddress", dealer.get("address"));
					orderInfoMap.put("sellerTelephone", dealer.get("telephone"));
					orderInfoMap.put("sellerEmail", dealer.get("email"));
				}*/
				orderInfoMap.put("status", orderMap.get("status"));
				BigDecimal amount = (BigDecimal)orderMap.get("amount");
				orderInfoMap.put("amount", order.getPayAmount());
				orderInfoMap.put("goodsAmount", orderMap.get("goodsAmount"));
				DecimalFormat df   = new DecimalFormat("######0.00");
				BigDecimal freightAmount = new BigDecimal(orderMap.get("freightAmount")!=null?orderMap.get("freightAmount").toString():"0");
				orderInfoMap.put("freightAmount", df.format(freightAmount.doubleValue()));
				orderInfoMap.put("scoreDeductionAmout", orderMap.get("scoreDeductionAmout"));
				Integer score = (Integer)orderMap.get("score");
				if(score == null || score <= 0){
					BigDecimal scoreRate = new BigDecimal(scoreRateJfig);
					orderInfoMap.put("score", amount.multiply(scoreRate).intValue());
				}
				orderInfoMap.put("isNeedInvoice", orderMap.get("isNeedInvoice"));
				String invTitle = (String)orderMap.get("invTitle");
				String invId = "";
				if(StringUtils.isNoneBlank(invTitle)){
					String[] invs = invTitle.split(":");
					invId = invs[0];
					invTitle = invs[1];
					orderInfoMap.put("invId", invId);
					orderInfoMap.put("invTitle", invTitle);
				}
				orderInfoMap.put("countryId", orderMap.get("countryId"));
				orderInfoMap.put("provinceId", orderMap.get("provinceId"));
				orderInfoMap.put("cityId", orderMap.get("cityId"));
				orderInfoMap.put("areaId", orderMap.get("areaId"));
				orderInfoMap.put("address", orderMap.get("address"));
				orderInfoMap.put("consignee", orderMap.get("consignee"));
				orderInfoMap.put("mobile", orderMap.get("mobile"));
				orderInfoMap.put("townId", 0);
				String consignAddressInfo = (String)orderMap.get("consignAddressInfo");
				/**如果consignAddressInfo为空收货信息区buyer_consign_address表中数据，否则取consignAddressInfo字段*/
				if(StringUtils.isNoneBlank(consignAddressInfo)){
					try {
						JSONObject json = JSONObject.parseObject(consignAddressInfo);
						if(json.get("townId")!=null){
							orderInfoMap.put("townId", json.get("townId"));
						}else{
							orderInfoMap.put("townId", 0);
						}
						orderInfoMap.put("countryId", json.get("countryId"));
						orderInfoMap.put("provinceId", json.get("provinceId"));
						orderInfoMap.put("cityId", json.get("cityId"));
						orderInfoMap.put("areaId", json.get("areaId"));
						orderInfoMap.put("address", json.get("address"));
						orderInfoMap.put("consignee", json.get("consignee"));
						orderInfoMap.put("mobile", json.get("mobile"));
						
					} catch (Exception e) {
					}
				}
				String payType = (String)orderMap.get("payType");
				orderInfoMap.put("payType", payType);
				if(StringUtils.isBlank(payType)){
					orderInfoMap.put("payType", "1");
				}
				Map orderGoodsMap = convertInfoGoods(baseUrl, orderMap);
				if("1".equals(orderMap.get("isJd").toString())){
					orderInfoMap.put("isJd", 1);
				}else{
					orderInfoMap.put("isJd", 0);
				}
				List orderGoodsList = new ArrayList();
				orderGoodsList.add(orderGoodsMap);
				orderInfoMap.put("orderGoods", orderGoodsList);
				tempMap.put(orderId, orderInfoMap);
			}
		}
		List orderInfos = new ArrayList();
		if(tempMap != null && !tempMap.isEmpty()){
			/**将订单放到list里*/
			Iterator iter = tempMap.entrySet().iterator();
			while(iter.hasNext()){
				Map.Entry entry = (Map.Entry)iter.next();
				orderInfos.add(entry.getValue());
			}
		}
		return orderInfos;
	}

	private String getLogistics(String url) throws Exception {
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		UrlEncodedFormEntity uefEntity = null;
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		httpclient = HttpClients.createDefault();
		HttpGet httppost = new HttpGet(url);
		uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
		response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		JSONObject jo = null;
		if (entity != null) {
			jo = JSONObject.parseObject(EntityUtils.toString(entity, "UTF-8"));
			return  jo.getString("data");
		}
		return "";
	}

	/**
	 * 设置指定订单状态
	 * @param orderId
	 * @param status
	 * @return int
	 * 2017-7-29
	 * @throws Exception 
	 *
	 */
	@Override
	public int cancelOrder(Long orderId) throws Exception {
		if(orderId == null){
			throw new Exception("订单号不存在");
		}
		OrderInfo order =  orderDao.findById(orderId);
		if(order == null){
			throw new Exception("订单不存在");
		}
		if(StringUtils.equals("1", order.getOrderStatus()) || StringUtils.equals("9", order.getOrderStatus())){
			return orderDao.cancelOrder(orderId);
		}else{
			throw new Exception("订单不能取消,请联系客服");
		}
	}

    @Override
    public int deleteOrderI(Long orderId) throws Exception {
        if(orderId == null){
            throw new Exception("订单号不存在");
        }
        OrderInfo order =  orderDao.findById(orderId);
        if(order == null){
            throw new Exception("订单不存在");
        }
            return orderDao.deleteOrder(orderId);
    }
	/**
	 * 确认收货
	 * @param orderId
	 * @return int
	 * 2017-7-29
	 * @throws Exception 
	 *
	 */
	@Override
	public int confirmReceipt(Long orderId) throws Exception {
		if(orderId == null){
			throw new Exception("订单号不存在");
		}
		Map map = orderDao.getOrderById(orderId);
		int count = orderDao.confirmReceipt(orderId);
		if(count >0 ){
			/**根据订单ID获取订单信息*/
			if(map != null && !map.isEmpty()){
				BigDecimal rankRate = new BigDecimal(JFig.getInstance().getValue("rank_score", "DEALER_RANK_SCORE","1"));
				String drs = JFig.getInstance().getValue("rank_score", "DRS");
				String[] drsArr = drs.split(",");
				Integer dealerId = (Integer)map.get("buyerId");
				BigDecimal amount = (BigDecimal)map.get("amount");
				BigDecimal scoreAmount = (BigDecimal)map.get("scroeAmount");
				BigDecimal scoreRate = new BigDecimal(scoreRateJfig);
				int score = scoreAmount.multiply(scoreRate).intValue();
				int rankscore = amount.multiply(rankRate).intValue();
				/**添加积分记录*/
				scoreDao.addScore(dealerId, orderId, score);
				/**增加积分*/
				dealerDao.addScoreById(dealerId, score,rankscore);
				
				Map<String, Object> rankScore = scoreDao.findRankScore(dealerId);
				int rank = Integer.parseInt(rankScore.get("rank").toString());
				int rScore = Integer.parseInt(rankScore.get("rankScore").toString());
				for (int i = 0,len = drsArr.length; i < len ; i++) {
					int rankLevel = Integer.parseInt(drsArr[i]);
					if(rankLevel>rScore){
						if(i+1 != rank){
							dealerDao.updateRank(dealerId,i+1);
						}
						break;
					}else if(len-1==i && rankLevel<=rScore){  
						dealerDao.updateRank(dealerId,i+1);
					}
				}
				int i = dealerDao.insertDealerRankScore(rankscore, orderId, dealerId);
			}	
		}
		return count;
	}

	/**
	 * 生成订单
	 * @param orderInfoPage
	 * @return
	 * 2017-9-30
	 *
	 */
	@Override
	public synchronized Map addOrderInfo(OrderInfoPage orderInfoPage) throws Exception {
		Integer dealerId = orderInfoPage.getBuyerId();
		String ids = orderInfoPage.getCartIds();
		String isCash = orderInfoPage.getIsCash();
		Integer addrId = orderInfoPage.getConsignAddressId();
		OrderCountModel order = cartService.findSaasShoppingCart(dealerId, ids, isCash, addrId.toString());
		OrderInfoSave orderInfo = new OrderInfoSave(dealerId.toString());
		Map returnMap = new HashMap<String, Object>();
		
		Map<String, Object> consignAddressMap = addressMapper.findJsonAddress(addrId);
		Map<String, Object> consignAddressSecretMap = addressMapper.findJsonSecretAddress(addrId);
		orderInfo.setConsignAddressInfo(new JSONObject(consignAddressMap).toJSONString());
		orderInfo.setConsignAddressInfoSecret(new JSONObject(consignAddressSecretMap).toJSONString());
		orderInfo.setConsignAddressId(addrId.toString());
		orderInfo.setBuyerRemark(orderInfoPage.getBuyerRemark());
		Integer invoiceId = orderInfoPage.getInvoiceId();
		Map service = orderDao.findServiceAndManager(dealerId);
		if(service!=null&&!service.isEmpty()){
			orderInfo.setCustomerManagerId(service.get("managerId")!=null?service.get("managerId").toString() : null);
			orderInfo.setCustomerServiceId(service.get("serviceId")!=null?service.get("serviceId").toString() : null);
		}
		OrderInvoice orderInvoice  = null;
		if(invoiceId != null && invoiceId!=0){
			orderInfo.setIsNeedInvoice(1);
			orderInvoice = new OrderInvoice();
			Map invoiceMap = invoiceService.findInvoiceById(invoiceId);
			orderInvoice.saveInvoiceMap(invoiceMap);
			orderInvoice.setBuyerId(dealerId);
			orderInvoice.setOrderId(orderInfo.getOrderId());
			orderInvoice.setInvAmount(order.getTotalPayPrice());
			orderInvoiceMapper.save(orderInvoice);
		}
		orderInfo.setOrderChannel(Integer.parseInt(StringUtils.isNotBlank(orderInfoPage.getOrderChannel())?orderInfoPage.getOrderChannel():"5"));
		orderInfo.saveOrderCountModel(order);
		if(order.getPackageList() !=null && order.getPackageList().size()>0){
			orderInfo.setPackageInfo(JSONArray.toJSONString(order.getPackageList()));
		}
		orderDao.addOrderInfoSave(orderInfo);
		if(orderInfoPage.getIsBean()==1){
			gbeansServiceI.toUserBeans(orderInfo.getOrderId().toString(), dealerId.toString());
		}
		if(order.getTotalCash()!=null && order.getTotalCash().intValue()>0){
			DealerCash dealerCash = new DealerCash();
			dealerCash.setCash(order.getTotalCash().intValue()*-1);
			dealerCash.setCreateTime(new Date());
			dealerCash.setCreateUserId(0);
			dealerCash.setDealerId(dealerId);
			dealerCash.setStatus("1");
			dealerCash.setType(2);
			dealerCash.setRelatedId(orderInfo.getOrderId());
			orderDao.updateDealerCash(order.getTotalCash().intValue(),dealerId);
			cashDao.insertDealerCash(dealerCash);
		}
		for(MakeOrderGoodsPageUtil goodsMap : order.getDefaultList()){
			OrderGoods orderGoods = new OrderGoods();
			orderGoods.setOrderId(orderInfo.getOrderId());
			orderGoods.setGoodsType(0);
			orderGoods.saveMakeOrderGoods(goodsMap, order.getTotalCash().intValue());
			orderGoodsDao.insertSelective(orderGoods);
		}
		for(MakeOrderGoodsPageUtil goodsMap : order.getGroupList()){
			OrderGoods orderGoods = new OrderGoods();
			orderGoods.setOrderId(orderInfo.getOrderId());
			orderGoods.setGoodsType(1);
			orderGoods.saveMakeOrderGoods(goodsMap, order.getTotalCash().intValue());
			orderGoodsDao.insertSelective(orderGoods);
		}
		for(MakeOrderGoodsPageUtil goodsMap : order.getPackageGoodsList()){
			OrderGoods orderGoods = new OrderGoods();
			orderGoods.setOrderId(orderInfo.getOrderId());
			orderGoods.setGoodsType(3);
			orderGoods.saveMakeOrderGoods(goodsMap, order.getTotalCash().intValue());
			orderGoodsDao.insertSelective(orderGoods);
		}
		cartService.deleteCart(ids, dealerId.toString());
		gbeansServiceI.toOrderBeans(orderInfo.getOrderId().toString());
		Map o = orderDao.getOrderById(orderInfo.getOrderId());
		returnMap.put("orderId", orderInfo.getOrderId());
		returnMap.put("orderAmount", o.get("amount"));
		return returnMap;
	}
	/*@Override
	public Map addOrderInfo(OrderInfoPage orderInfoPage) throws Exception {
		String cartIds = orderInfoPage.getCartIds();
		Integer buyerId = orderInfoPage.getBuyerId();
		Integer sellerId = orderInfoPage.getSellerId();
		Map returnMap = new HashMap<String, Object>();
		if(sellerId == null){
			sellerId = 939817;
		}
		Integer consignAddressId = orderInfoPage.getConsignAddressId();
		if(consignAddressId==null){
			throw new Exception("收货地址不能为空");
		}
		Long orderId = PaymentIDProduce.getPaymentID();
		MakeOrderPage page = cartService.findShoppingCartByIds(buyerId, cartIds,orderInfoPage.getIsCash(),String.valueOf(consignAddressId));
		if(page!=null){
			cartService.batchDelcart(cartIds.split(","));
		}
		Double orderAmount = Double.parseDouble(page.getTotalOrderPrice());
		Double totalPayPrice = Double.parseDouble(page.getTotalPayPrice());
		Double goodsAmount = Double.parseDouble(page.getTotalGoodsPrice());
		Double totalFullcut = Double.parseDouble(page.getTotalFullcut());
		Double groupSale = Double.parseDouble(page.getGroupSale());
		Integer freightAmount = new BigDecimal(page.getTotalFreight()!=null?page.getTotalFreight() : "0").intValue();
		Integer cash = new BigDecimal(page.getTotalCash()).intValue();
		Integer score = new BigDecimal(page.getTotalScore()).intValue();
		
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setId(orderId);
		orderInfo.setBuyerRemark(orderInfoPage.getBuyerRemark());
		orderInfo.setBuyerId(buyerId);
		orderInfo.setSellerId(sellerId);
		BigDecimal orderAmountBd = new BigDecimal(orderAmount);
		orderInfo.setOrderAmount(orderAmountBd);
		orderInfo.setFullCutAmount(totalFullcut);
		BigDecimal goodsAmountBd = new BigDecimal(goodsAmount);
		orderInfo.setGoodsAmount(goodsAmountBd);
		BigDecimal freightAmountBd = new BigDecimal(freightAmount);
		orderInfo.setFreightAmount(freightAmountBd);
		orderInfo.setConsignAddressId(consignAddressId);
		Map<String, Object> consignAddressMap = addressMapper.findJsonAddress(consignAddressId);
		Map<String, Object> consignAddressSecretMap = addressMapper.findJsonSecretAddress(consignAddressId);
		orderInfo.setConsignAddressInfo(new JSONObject(consignAddressMap).toJSONString());
		orderInfo.setConsignAddressInfoSecret(new JSONObject(consignAddressSecretMap).toJSONString());
		Integer invoiceId = orderInfoPage.getInvoiceId();
		String isNeedInvoice = "0";
		OrderInvoice orderInvoice  = null;
		if(invoiceId != null && invoiceId !=0){
			orderInvoice = new OrderInvoice();
			isNeedInvoice = "1";
			Map invoiceMap = invoiceService.findInvoiceById(invoiceId);
			orderInvoice.setBuyerId(buyerId);
			orderInvoice.setOrderId(orderId);
			orderInvoice.setInvType(invoiceMap.get("invType").toString());
			orderInvoice.setInvAmount(new BigDecimal(totalPayPrice));
			orderInvoice.setInvTitle(invoiceMap.get("invTitle").toString());
			orderInvoice.setTaxpayeRno(invoiceMap.get("taxpayeRno")!=null?invoiceMap.get("taxpayeRno").toString():"");
			orderInvoice.setRegAddress(invoiceMap.get("regAddress")!=null?invoiceMap.get("regAddress").toString():"");
			orderInvoice.setRegTelphone(invoiceMap.get("regTelphone")!=null?invoiceMap.get("regTelphone").toString():"");
			orderInvoice.setRegTelphoneSecret(invoiceMap.get("regTelphoneSecret")==null?null:invoiceMap.get("regTelphoneSecret").toString());
			orderInvoice.setBank(invoiceMap.get("bank")!=null?invoiceMap.get("bank").toString():"");
			orderInvoice.setBankAccount(invoiceMap.get("bankAccount")!=null?invoiceMap.get("bankAccount").toString():"");
			orderInvoice.setRecvName(invoiceMap.get("recvName").toString());
			orderInvoice.setRecvMobile(invoiceMap.get("recvMobile").toString());
			orderInvoice.setRecvMobileSecret(invoiceMap.get("recvMobileSecret")==null?"":invoiceMap.get("recvMobileSecret").toString());
			orderInvoice.setRecvProvince(Integer.parseInt(invoiceMap.get("recvProvince").toString()));
			orderInvoice.setRecvCity(Integer.parseInt(invoiceMap.get("recvCity").toString()));
			orderInvoice.setRecvArea(Integer.parseInt(invoiceMap.get("recvArea").toString()));
			orderInvoice.setRecvAddress(invoiceMap.get("recvAddress").toString());
			orderInvoiceMapper.save(orderInvoice);
		}
		orderInfo.setIsNeedInvoice(isNeedInvoice);
		orderInfo.setScore(score);
		BigDecimal scoreDeductionAmout = new BigDecimal(cash);
		orderInfo.setScoreDeductionAmout(scoreDeductionAmout);
		orderInfo.setOrderChannel(StringUtils.isNotBlank(orderInfoPage.getOrderChannel())?orderInfoPage.getOrderChannel() : "5");
		Map service = orderDao.findServiceAndManager(buyerId);
		if(service!=null&&!service.isEmpty()){
			orderInfo.setCustomerManagerId(service.get("managerId")!=null?Integer.parseInt(service.get("managerId").toString()) : null);
			orderInfo.setCustomerServiceId(service.get("serviceId")!=null?Integer.parseInt(service.get("serviceId").toString()) : null);
		}
		orderInfo.setOrderStore("2");
		orderInfo.setTail(page.getTail()!=null&&StringUtils.isNotBlank(page.getTail())?Double.parseDouble(page.getTail()):0);
		BigDecimal totalGroupPrice = new BigDecimal(page.getTotalGroupPrice()!=null?page.getTotalGroupPrice() : "0");
		if(groupSale>0){
			orderInfo.setOrderType(1);
		}else{
			orderInfo.setOrderType(0);
		}
		orderInfo.setGroupOrderSale(groupSale);
		orderDao.addOrderInfo(orderInfo);
		if(orderInfoPage.getIsBean()==1){
			System.out.println(orderInfo.getId()+"======================使用工币");
			gbeansServiceI.toUserBeans(orderId.toString(), buyerId.toString());
		}
		if(cash!=null && cash>0){
			DealerCash dealerCash = new DealerCash();
			dealerCash.setCash(cash*-1);
			dealerCash.setCreateTime(new Date());
			dealerCash.setCreateUserId(0);
			dealerCash.setDealerId(buyerId);
			dealerCash.setStatus("1");
			dealerCash.setType(2);
			dealerCash.setRelatedId(orderInfo.getId());
			orderDao.updateDealerCash(cash,buyerId);
			cashDao.insertDealerCash(dealerCash);
		}
		List<MakeOrderGoodsPageUtil> goodsList = page.getGoods();
		for(MakeOrderGoodsPageUtil goodsMap : goodsList){
			String activityType = String.valueOf(goodsMap.getActivityType());
			String activityId = String.valueOf(goodsMap.getActivityId());
			OrderGoods orderGoods = new OrderGoods();
			orderGoods.setOrderId(orderId);
			orderGoods.setGoodsId(goodsMap.getGoodsId());
			orderGoods.setSellerGoodsId(goodsMap.getSellerGoodsId());
			int num = goodsMap.getNum();
			orderGoods.setNum(num);
			BigDecimal unitPriceBd = goodsMap.getSalePrice();
			BigDecimal costPrice = goodsMap.getCostPrice();
			orderGoods.setUnitPrice(unitPriceBd);
			Double saleUnitPrice = 0D;
			if(cash!=null && cash>0){
				saleUnitPrice = goodsMap.getSaleUnitPrice().doubleValue();
				orderGoods.setTail(goodsMap.getTail());
			}else{
				saleUnitPrice = goodsMap.getSalePrice().doubleValue();
			}
			if("1".equals(activityType)){
				orderGoods.setGoodsType(1);
				orderGoods.setActivityId(activityId);
			}
			BigDecimal saleUnitPriceBd = new BigDecimal(saleUnitPrice);
			orderGoods.setSaleUnitPrice(saleUnitPriceBd);
			BigDecimal salePriceBd = new BigDecimal(saleUnitPrice*num).setScale(2, BigDecimal.ROUND_HALF_UP);    
			orderGoods.setSalePrice(salePriceBd);
			orderGoods.setPrice(costPrice);
			orderGoodsDao.insertSelective(orderGoods);
			
			//更新购物车
//			ShoppingCartPage shoppingCart = new ShoppingCartPage();
//			shoppingCart.setBuyerId(buyerId);
//			shoppingCart.setSellerGoodsId(Long.valueOf(goodsMap.get("sellerGoodsId").toString()));
//			shoppingCart.setNum(num);
//			cartService.editOrderCartMall(shoppingCart);
		}
		gbeansServiceI.toOrderBeans(orderId.toString());
		Map o = orderDao.getOrderById(orderId);
		returnMap.put("orderId", orderId);
		returnMap.put("orderAmount", o.get("amount"));
		return returnMap;
	}*/


	/**
	 * 保存商城订单
	 * @author felx
	 * @date 2017-12-30
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Map addMallOrderInfo(OrderInfoPage orderInfoPage)throws Exception{
		//结果Map
		Map<String, Object> returnMap = new HashMap<>();
		//买家Id
		Integer buyerId = orderInfoPage.getBuyerId();
		//发票Id
		Integer invoiceId =orderInfoPage.getInvoiceId();
		//使用现金券店铺
		String dealerDeductionIds = orderInfoPage.getDealerDeductionIds();
		//购物车Id
		String cartIds = ObjectUtil.isReturn(
				orderInfoPage.getCartIds(),
				"参数错误,请联系客服"
				);
		//收货地址
		Integer consignAddressId = ObjectUtil.isReturn(
				orderInfoPage.getConsignAddressId(),
				"收货地址不能为空"
				);
		//购物车列表
		Map<String,Object> map = ObjectUtil.isReturn(
				cartService.findShoppingCartByIdsMall(buyerId, cartIds,consignAddressId.toString()),
				"购物车不能为空"
				);                       
		OrderInfo orderInfo = new OrderInfo();
		//买家Id
		orderInfo.setBuyerId(buyerId);
		orderInfo.setConsignAddressId(consignAddressId);
		
		Map<String, Object> invoiceMap = null;
		if(invoiceId != null){
			invoiceMap = ObjectUtil.isReturn(
					invoiceService.findInvoiceById(invoiceId),
					"发票不存在"
					);
		}
		Map<String, Object> consignAddressMap = ObjectUtil.isReturn(
				addressMapper.findJsonAddress(consignAddressId),
				"地址不存在"
				);
		Map<String, Object> consignAddressSecretMap = ObjectUtil.isReturn(
				addressMapper.findJsonSecretAddress(consignAddressId),
				"地址不存在"
				);
		orderInfo.addOrderInfoByMall(map,dealerDeductionIds);
		OrderInvoice invoice = orderInfo.addOrderInvoiceByMall(invoiceMap);
		List<OrderGoods> goods = orderInfo.addOrderGoodsByMall();
		orderInfo.setConsignAddressInfo(new JSONObject(consignAddressMap).toJSONString());
		orderInfo.setConsignAddressInfoSecret(new JSONObject(consignAddressSecretMap).toJSONString());
		
		orderDao.addOrderInfo(orderInfo);
		cartService.batchDelcart(cartIds.split(","));
		if(invoice != null){
			orderInvoiceMapper.save(invoice);
		}
		for (OrderGoods orderGoods : goods) {
			orderGoodsDao.insertSelective(orderGoods);
		}
//		try {
//			jdInterfaceI.toJDOrder(orderInfo.getId().toString());
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("============"+e.getMessage());
//			throw new RuntimeException(e.getMessage());
//		}
		int isRebate = dealerDao.isRebate(buyerId.toString());
		if(isRebate>0){
			orderDao.updateOrderRebate(orderInfo.getId().toString());
			orderGoodsDao.updateGoodsRebate(orderInfo.getId().toString());
		}
		List<OrderInfo> orderList = new ArrayList<>();
		orderList.add(orderInfo);
		returnMap.put("orders", orderList);
		returnMap.put("totalAmount", orderInfo.toPayAmount());
		returnMap.put("parentOrderId", orderInfo.getId());
		return returnMap;
	}
	/**
	 * 保存商城订单
	 * @author felx
	 * @date 2017-12-30
	 */
	@Deprecated
	public Map addMallOrderInfo(OrderInfoPage orderInfoPage,int i) throws Exception {
		String cartIds = orderInfoPage.getCartIds();
		Integer buyerId = orderInfoPage.getBuyerId();
//		Integer sellerId = orderInfoPage.getSellerId();
		Integer invoiceId = orderInfoPage.getInvoiceId();
		String dealerDeductionIds = orderInfoPage.getDealerDeductionIds();//商城使用现金券的店铺id
		
		
		Map returnMap = new HashMap<String, Object>();
//		if(sellerId == null){
//			sellerId = 939817;
//		}
		List orderList = new ArrayList();
		Integer consignAddressId = orderInfoPage.getConsignAddressId();
		Map map = cartService.findShoppingCartByIdsMall(buyerId, cartIds,consignAddressId.toString());
		List shopList = (List)map.get("shops");
		Long originalOrderId = null;
		if(!shopList.isEmpty()){
			BigDecimal totalScoreDeductionAmout = new BigDecimal(0);//总现金券使用金额
			if(null != dealerDeductionIds && !"".equals(dealerDeductionIds)){
				//有使用现金券抵扣的
				for(Object obj : shopList){
					Long orderId = PaymentIDProduce.getPaymentID();
					Map shopMap = (Map)obj;
					Integer sellerId = Integer.valueOf(shopMap.get("sellerId").toString());
					Integer cash = Integer.parseInt(shopMap.get("totalCash").toString());
					boolean isUse = dealerDeductionIds.contains(sellerId.toString());
					if(isUse){
						totalScoreDeductionAmout = totalScoreDeductionAmout.add(new BigDecimal(cash));
					}
					
				}
			}
			
			if(shopList.size()>1){//有多个店铺的，需要拆单，先生成一个父订单
				originalOrderId = PaymentIDProduce.getPaymentID();
				Double orderAmount = Double.valueOf(map.get("orderTotalOrderPrice").toString());
				Double totalPayPrice = Double.valueOf(map.get("orderTotalPayPrice").toString());
				Double goodsAmount = Double.valueOf(map.get("orderTotalGoodsPrice").toString());
				Double freightAmount = Double.valueOf(map.get("orderTotalFreight").toString());
				Integer cash = Integer.parseInt(map.get("orderTotalCash").toString());
				Integer score = Integer.parseInt(map.get("orderTotalScore").toString());
				OrderInfo orderInfo = new OrderInfo();
				orderInfo.setId(originalOrderId);
				orderInfo.setBuyerId(buyerId);
				orderInfo.setSellerId(0);
				BigDecimal orderAmountBd = new BigDecimal(orderAmount);
				orderInfo.setOrderAmount(orderAmountBd);
				BigDecimal goodsAmountBd = new BigDecimal(goodsAmount);
				orderInfo.setGoodsAmount(goodsAmountBd);
				BigDecimal freightAmountBd = new BigDecimal(freightAmount);
				orderInfo.setFreightAmount(freightAmountBd);
				orderInfo.setConsignAddressId(consignAddressId);
				orderInfo.setHasChild("1");
				
				String isNeedInvoice = "0";
				if(invoiceId != null){
					isNeedInvoice = "1";
					/*Map invoiceMap = invoiceService.findInvoiceById(invoiceId);
					OrderInvoice orderInvoice = new OrderInvoice();
					orderInvoice.setBuyerId(buyerId);
					orderInvoice.setOrderId(originalOrderId);
					orderInvoice.setInvType(invoiceMap.get("invType").toString());
					orderInvoice.setInvAmount(new BigDecimal(totalPayPrice));
					orderInvoice.setInvTitle(invoiceMap.get("invTitle")==null?null:invoiceMap.get("invTitle").toString());
					orderInvoice.setTaxpayeRno(invoiceMap.get("taxpayeRno")==null?null:invoiceMap.get("taxpayeRno").toString());
					orderInvoice.setRegAddress(invoiceMap.get("regAddress")==null?null:invoiceMap.get("regAddress").toString());
					orderInvoice.setRegTelphone(invoiceMap.get("regTelphone")==null?null:invoiceMap.get("regTelphone").toString());
					orderInvoice.setBank(invoiceMap.get("bank")==null?null:invoiceMap.get("bank").toString());
					orderInvoice.setBankAccount(invoiceMap.get("bankAccount")==null?null:invoiceMap.get("bankAccount").toString());
					orderInvoice.setRecvName(invoiceMap.get("recvName")==null?"":invoiceMap.get("recvName").toString());
					orderInvoice.setRecvMobile(invoiceMap.get("recvMobile")==null?"":invoiceMap.get("recvMobile").toString());
					orderInvoice.setRecvProvince(invoiceMap.get("recvProvince")==null?0:Integer.parseInt(invoiceMap.get("recvProvince").toString()));
					orderInvoice.setRecvCity(invoiceMap.get("recvCity")==null?0:Integer.parseInt(invoiceMap.get("recvCity").toString()));
					orderInvoice.setRecvArea(invoiceMap.get("recvArea")==null?1:Integer.parseInt(invoiceMap.get("recvArea").toString()));
					orderInvoice.setRecvAddress(invoiceMap.get("regAddress")==null?"":invoiceMap.get("regAddress").toString());
					orderInvoice.setCreateTime(new Date());
					orderInvoiceMapper.save(orderInvoice);*/
				}
				orderInfo.setIsNeedInvoice(isNeedInvoice);
				orderInfo.setScore(score);
//				BigDecimal scoreDeductionAmout = new BigDecimal(cash);
//				orderInfo.setScoreDeductionAmout(scoreDeductionAmout);
				orderInfo.setScoreDeductionAmout(totalScoreDeductionAmout);
				orderInfo.setOrderSource("5");//订单来源:其他
				orderInfo.setOrderStore("1");//订单店铺:商城
				orderInfo.setOrderChannel("2");//商城订单
				orderDao.addOrderInfo(orderInfo);
			}
			for(Object obj : shopList){
				Long orderId = PaymentIDProduce.getPaymentID();
				Map shopMap = (Map)obj;
				Double orderAmount = Double.valueOf(shopMap.get("totalOrderPrice").toString());
				Double totalPayPrice = Double.valueOf(shopMap.get("totalPayPrice").toString());
				Double goodsAmount = Double.valueOf(shopMap.get("totalGoodsPrice").toString());
				Double freightAmount = Double.valueOf(shopMap.get("totalFreight").toString());
				Integer cash = Integer.parseInt(shopMap.get("totalCash").toString());
				Integer score = Integer.parseInt(shopMap.get("totalScore").toString());
				Integer sellerId = Integer.valueOf(shopMap.get("sellerId").toString());
				OrderInfo orderInfo = new OrderInfo();
				orderInfo.setId(orderId);
				orderInfo.setBuyerId(buyerId);
				orderInfo.setSellerId(sellerId);
				BigDecimal orderAmountBd = new BigDecimal(orderAmount);
				orderInfo.setOrderAmount(orderAmountBd);
				BigDecimal goodsAmountBd = new BigDecimal(goodsAmount);
				orderInfo.setGoodsAmount(goodsAmountBd);
				BigDecimal freightAmountBd = new BigDecimal(freightAmount);
				orderInfo.setFreightAmount(freightAmountBd);
				orderInfo.setConsignAddressId(consignAddressId);
				if(null!=originalOrderId){
					orderInfo.setOriginalOrderId(originalOrderId.toString());
				}else{
					originalOrderId = orderId;
				}
				
				String isNeedInvoice = "0";
				if(invoiceId != null){
					isNeedInvoice = "1";
					Map invoiceMap = invoiceService.findInvoiceById(invoiceId);
					OrderInvoice orderInvoice = new OrderInvoice();
					orderInvoice.setBuyerId(buyerId);
					orderInvoice.setOrderId(orderId);
					orderInvoice.setInvType(invoiceMap.get("invType").toString());
					orderInvoice.setInvAmount(new BigDecimal(totalPayPrice));
					orderInvoice.setInvTitle(invoiceMap.get("invTitle")==null?null:invoiceMap.get("invTitle").toString());
					orderInvoice.setInvTitleType(invoiceMap.get("invTitleType")==null?null:invoiceMap.get("invTitleType").toString());
					orderInvoice.setInvContent(invoiceMap.get("invContent")==null?null:invoiceMap.get("invContent").toString());
					orderInvoice.setTaxpayeRno(invoiceMap.get("taxpayeRno")==null?null:invoiceMap.get("taxpayeRno").toString());
					orderInvoice.setRegAddress(invoiceMap.get("regAddress")==null?null:invoiceMap.get("regAddress").toString());
					orderInvoice.setRegTelphone(invoiceMap.get("regTelphone")==null?null:invoiceMap.get("regTelphone").toString());
					orderInvoice.setRegTelphoneSecret(invoiceMap.get("regTelphoneSecret")==null?null:invoiceMap.get("regTelphoneSecret").toString());
					orderInvoice.setBank(invoiceMap.get("bank")==null?null:invoiceMap.get("bank").toString());
					orderInvoice.setBankAccount(invoiceMap.get("bankAccount")==null?null:invoiceMap.get("bankAccount").toString());
					orderInvoice.setRecvName(invoiceMap.get("recvName")==null?"":invoiceMap.get("recvName").toString());
					orderInvoice.setRecvMobile(invoiceMap.get("recvMobile")==null?"":invoiceMap.get("recvMobile").toString());
					orderInvoice.setRecvMobileSecret(invoiceMap.get("recvMobileSecret")==null?"":invoiceMap.get("recvMobileSecret").toString());
					orderInvoice.setRecvProvince(invoiceMap.get("recvProvince")==null?0:Integer.parseInt(invoiceMap.get("recvProvince").toString()));
					orderInvoice.setRecvCity(invoiceMap.get("recvCity")==null?0:Integer.parseInt(invoiceMap.get("recvCity").toString()));
					orderInvoice.setRecvArea(invoiceMap.get("recvArea")==null?1:Integer.parseInt(invoiceMap.get("recvArea").toString()));
					orderInvoice.setRecvAddress(invoiceMap.get("regAddress")==null?"":invoiceMap.get("regAddress").toString());
					orderInvoice.setCreateTime(new Date());
					orderInvoiceMapper.save(orderInvoice);
				}
				orderInfo.setIsNeedInvoice(isNeedInvoice);
				orderInfo.setScore(score);
				if(null != dealerDeductionIds && !"".equals(dealerDeductionIds)){
					//使用现金券
					boolean isUse = dealerDeductionIds.contains(sellerId.toString());
					if(isUse){
						BigDecimal scoreDeductionAmout = new BigDecimal(cash);
						orderInfo.setScoreDeductionAmout(scoreDeductionAmout);
					}
				}
				orderInfo.setOrderSource("5");//订单来源:其他
				orderInfo.setOrderStore("1");//订单店铺:商城
				orderInfo.setOrderChannel("2");//商城订单 
				orderDao.addOrderInfo(orderInfo);
				
				List goodsList = (List)shopMap.get("goods");
				for(Object o : goodsList){
					Map goodsMap = (Map)o;
					OrderGoods orderGoods = new OrderGoods();
					orderGoods.setOrderId(orderId);
					orderGoods.setGoodsId(Long.valueOf(goodsMap.get("goodsId").toString()));
					orderGoods.setSellerGoodsId(Long.valueOf(goodsMap.get("sellerGoodsId").toString()));
					int num = Integer.parseInt(goodsMap.get("num").toString());
					orderGoods.setNum(num);
					BigDecimal unitPriceBd = new BigDecimal(Double.valueOf(goodsMap.get("price").toString()));
					orderGoods.setUnitPrice(unitPriceBd);
					Double saleUnitPrice = Double.valueOf(goodsMap.get("saleUnitPrice").toString());
					BigDecimal saleUnitPriceBd = new BigDecimal(saleUnitPrice);
					orderGoods.setSaleUnitPrice(saleUnitPriceBd);
					BigDecimal salePriceBd = new BigDecimal(saleUnitPrice*num);
					orderGoods.setSalePrice(salePriceBd);
					orderGoodsDao.insertSelective(orderGoods);
					
					//更新购物车
					ShoppingCartPage shoppingCart = new ShoppingCartPage();
//					shoppingCart.setBuyerId(buyerId);
//					shoppingCart.setSellerGoodsId(Long.valueOf(goodsMap.get("sellerGoodsId").toString()));
					shoppingCart.setNum(num*(-1));
					shoppingCart.setId(Integer.valueOf(goodsMap.get("cartId").toString()));
					cartService.editOrderCartMall(shoppingCart);
				}
				Map orderMap = new HashMap();
				orderMap.put("orderId", orderId);
				orderMap.put("orderAmount", orderAmount);
				orderList.add(orderMap);
			}
		}
		
		Double orderTotalAmount = Double.valueOf(map.get("orderTotalOrderPrice").toString());
		returnMap.put("orders", orderList);
		returnMap.put("totalAmount", orderTotalAmount);
		if(null!=originalOrderId){
			returnMap.put("parentOrderId", originalOrderId);
		}
		
		return returnMap;
	}
	
	/**
	 * 根据id获取订单详情
	 * @author felx
	 * @date 2017-12-31
	 */
	public OrderInfo getOrderById(Long orderId){
		OrderInfo orderInfo = (OrderInfo)orderDao.getOrderInfoById(orderId);
		return orderInfo;
	}
	
	/**
	 * 根据订单id获取订单应付金额
	 */
	public OrderInfo getOrderInfoByIdValue(Long orderId){
		OrderInfo orderInfo = (OrderInfo)orderDao.getOrderInfoByIdValue(orderId);
		return orderInfo;
	}
	
	/**
	 * 修改订单
	 * @author felx
	 * @date 2017-12-31
	 */
	public void editOrder(OrderInfo orderInfo){
		orderDao.update(orderInfo);
	}
	
	/**
	 * 支付完成后修改订单
	 * @author felx
	 * @date 2016-1-4
	 */
	public void editHandleOrder(OrderInfo orderInfo){
		String orderStatus ="2";
		if(orderInfo.getJdOrderId()!=null &&orderInfo.getJdOrderId().length()>0){
			log.info("this is jd order, set orderStatus to 3");
			orderStatus="3";
		}
		List<OrderInfo> orders = orderDao.getOrderInfoByParentId(orderInfo.getId().toString());
		if(null != orders && orders.size()>0){
			for(OrderInfo order : orders){
				order.setPayTime(new Date());
//				order.setPayType("2");//全额付款
				order.setOrderStatus("2");//待发货
				order.setPayStatus("2");//全额付款
				orderDao.update(order);
			}
		}
		log.info("pay success, change orderStatus is:"+orderStatus);
		//修改父订单
		orderInfo.setPayTime(new Date());
//		orderInfo.setPayType("2");//全额付款
		orderInfo.setOrderStatus(orderStatus);//待发货
		orderInfo.setPayStatus("2");//全额付款
		orderDao.update(orderInfo);
		
	}

	@Override
	public Map<String, Object> findOrders(Integer buyerId, Integer status,
			Integer page, Integer rows,String orderId,String queryType, String startTime, String endTime, String logisticsCode,int type) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> querymap = new HashMap<String, String>();
		if(buyerId == null){
			throw new Exception("请登录");
		}
		if(status.intValue() == 0){
			status = null;
		}
		querymap.put("orderId", StringUtils.isNotBlank(orderId)?orderId : null);
		querymap.put("queryType", StringUtils.isNotBlank(queryType)?queryType : null);
		querymap.put("startTime", StringUtils.isNotBlank(startTime)?startTime : null);
		querymap.put("endTime", StringUtils.isNotBlank(endTime)?endTime : null);
		querymap.put("logisticsCode", StringUtils.isNotBlank(logisticsCode)?logisticsCode : null);
		querymap.put("type", String.valueOf(type));
		List<Map> orderList = orderDao.getOrderListByDealerId(buyerId, status, page, rows,querymap);
		for (int i = 0,len = orderList.size(); i < len; i++) {
			Map order = orderList.get(i);
			if(order.get("freightAmount")!=null){
				if(!"".equals(order.get("freightAmount").toString())){
					DecimalFormat fmt = new DecimalFormat("0.00");
					order.put("freightAmount", fmt.format(Double.parseDouble(order.get("freightAmount").toString())));
				}
			};
			List<Map> goods = orderGoodsDao.getGoodsByOrderId(order.get("orderId").toString());
			order.put("orderGoods", goods);
			String adress = order.get("consignAddressInfo")!=null?order.get("consignAddressInfo").toString() : "";
			try {
				JSONObject jo = JSONObject.parseObject(adress);
				order.put("consignee",	 jo.get("consignee")!=null?jo.get("consignee").toString() : "");
				order.put("mobile",	 jo.get("mobile")!=null?jo.get("mobile").toString() : "");
			} catch (Exception e) {
				Map consignAddressPage  = addressMapper.findById(Integer.parseInt(order.get("consignAddressId").toString()));
				order.put("consignee",	 consignAddressPage.get("consignee")!=null?consignAddressPage.get("consignee"):"");
				order.put("mobile",	 consignAddressPage.get("mobile")!=null?consignAddressPage.get("mobile"):"");
			}
			try {
				LogisticsCompany  logistics= LogisticsCompanyI.fingById(Integer.parseInt(order.get("sellerLogisticsId").toString()));
				order.put("logisticsUrl", Delivery.search(logistics.getLogisticsCode(),order.get("logisticsCode").toString()));
			} catch (Exception e) {
				order.put("logisticsUrl", "");
			}
			orderList.set(i, order);
		}
		
		map.put("rows", orderList);
		map.put("total", orderDao.getOrderListByDealerIdCount(buyerId, status,querymap));
		return map;
	}
	
	@Override
	public Map<String, Object> findMallOrders(Integer buyerId, Integer status,
			Integer page, Integer rows,String orderId, String startTime, String endTime, String logisticsCode,int type,String buyNo,String goodsName) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> querymap = new HashMap<String, String>();
		if(buyerId == null){
			throw new Exception("请登录");
		}
		if(status.intValue() == 0){
			status = null;
		}
		querymap.put("orderId", StringUtils.isNotBlank(orderId)?orderId : null);
		querymap.put("startTime", StringUtils.isNotBlank(startTime)?startTime : null);
		querymap.put("endTime", StringUtils.isNotBlank(endTime)?endTime : null);
		querymap.put("logisticsCode", StringUtils.isNotBlank(logisticsCode)?logisticsCode : null);
		querymap.put("type", String.valueOf(type));
		querymap.put("buyNo", StringUtils.isNotBlank(buyNo)?buyNo : null);
		querymap.put("goodsName", StringUtils.isNotBlank(goodsName)?goodsName : null);
		/*if(StringUtils.isNotBlank(buyNo)||StringUtils.isNotBlank(goodsName)){
			querymap.put("buyNo", StringUtils.isNotBlank(buyNo)?buyNo : null);
			querymap.put("goodsName", StringUtils.isNotBlank(goodsName)?goodsName : null);
			List<Map> goods = orderGoodsDao.getGoods(buyerId,status, page, rows,querymap);
			for (int i = 0,len = orderList.size(); i < len; i++) {
			
		}else{
			
		}*/
		
		
		
		
		
		
		
		List<Map> orderList = orderDao.getOrderListByDealerId(buyerId, status, page, rows,querymap);
		for (int i = 0,len = orderList.size(); i < len; i++) {
			Map order = orderList.get(i);
			if(order.get("freightAmount")!=null){
				if(!"".equals(order.get("freightAmount").toString())){
					DecimalFormat fmt = new DecimalFormat("0.00");
					order.put("freightAmount", fmt.format(Double.parseDouble(order.get("freightAmount").toString())));
				}
			};
			List<Map> goods = orderGoodsDao.getGoodsByOrderId(order.get("orderId").toString());
			order.put("orderGoods", goods);
			String adress = order.get("consignAddressInfo")!=null?order.get("consignAddressInfo").toString() : "";
			try {
				JSONObject jo = JSONObject.parseObject(adress);
				order.put("consignee",	 jo.get("consignee")!=null?jo.get("consignee").toString() : "");
				order.put("mobile",	 jo.get("mobile")!=null?jo.get("mobile").toString() : "");
			} catch (Exception e) {
				Map consignAddressPage  = addressMapper.findById(Integer.parseInt(order.get("consignAddressId").toString()));
				order.put("consignee",	 consignAddressPage.get("consignee")!=null?consignAddressPage.get("consignee"):"");
				order.put("mobile",	 consignAddressPage.get("mobile")!=null?consignAddressPage.get("mobile"):"");
			}
			try {
				LogisticsCompany  logistics= LogisticsCompanyI.fingById(Integer.parseInt(order.get("sellerLogisticsId").toString()));
				order.put("logisticsUrl", Delivery.search(logistics.getLogisticsCode(),order.get("logisticsCode").toString()));
			} catch (Exception e) {
				order.put("logisticsUrl", "");
			}
			orderList.set(i, order);
		}
		
		map.put("rows", orderList);
		map.put("total", orderDao.getOrderListByDealerIdCount(buyerId, status,querymap));
		return map;
	}

	@Override
	public void updateOrder(OrderInfoPage orderInfoPage) {
		orderDao.update(orderInfoPage);
		
	}

	@Override
	public int cancelOrder(String orderId, String buyerId) throws Exception {
		if(orderId == null){
			throw new Exception("订单号不存在");
		}
		OrderInfo order =  orderDao.findByIdAndBuyerId(orderId,buyerId);
		if(order == null){
			throw new Exception("订单不存在");
		}
		
		if(StringUtils.equals("1", order.getOrderStatus())){
			//恢复余额
			dealerDao.returnDealerBalance(orderId);
			//删除已生成记录
			balanceRecordMapper.delRecordByOrderId(orderId);
			//不使用余额
			orderDao.updateBalanceByType(0,orderId);
			if(order.getScoreDeductionAmout()!=null && order.getScoreDeductionAmout().compareTo(new BigDecimal(0))>0){
				orderDao.updateDC(order.getId());//更新dealer 表中的cash字段  20160720
				orderDao.insertDealerCash(order);//新增记录到dealer_cash表中 20160720
			}
			gbeansServiceI.cancelOrder(orderId);
			return orderDao.cancelOrder(order.getId());
		}else{
			throw new Exception("订单不能取消,请联系客服");
		}
		
	}
	/**
	 *保存支付页面线下支付信息
	 *@author jxw 
     *@date 2016.03.16 
	 */
	@Override
	public int updateOrderOfflinePay(HttpServletRequest request,LoginInfo loginInfo) throws Exception{
		String thirdTradeCodeCompanyAccount="companyAccount";
		return offlinePayAlipay(request,loginInfo,thirdTradeCodeCompanyAccount);
	}
	
	/**
	 *保存支付页面支付宝转账信息
	 *@author jxw 
     *@date 2016.03.16 
	 */
	@Override
	public int updateOrderAlipay(HttpServletRequest request,LoginInfo loginInfo) throws Exception{
		String thirdTradeCodeCompanyAccount="thirdTradeCode";
		return offlinePayAlipay(request,loginInfo,thirdTradeCodeCompanyAccount);
	}
	
	/**
	 * 保存支付页面支付宝转账信息；保存支付页面线下支付信息
	 * @param request
	 * @param loginInfo
	 * @param thirdTradeCodeCompanyAccount
	 * @return
	 * @throws Exception
	 */
	public int offlinePayAlipay(HttpServletRequest request,LoginInfo loginInfo,String thirdTradeCodeCompanyAccount) throws Exception{
		if(StringUtils.isBlank(request.getParameter("id"))){
			throw new RuntimeException("无订单信息");
		}
		/**
		 * 根据单号查询付款单id  from payment_bill_order
		 */
		Map map = addPaymentBillService.findPayOrderById(Long.valueOf(request.getParameter("id")));
		//Integer alipayType = JFig.getInstance().getIntegerValue("system_options", "ALIPAY_TYPE");//支付宝
		if(!map.isEmpty() && !"0".equals(map.get("num").toString())){
			//int x=0;
			int m=0;
			 //from payment_bill_order 根据订单id查询付款单id
			String selectPaymentBillId=paymentBillMapper.selectPaymentBillOrderId(request.getParameter("id"));
			
			if(StringUtils.isNotBlank(selectPaymentBillId)){
				//根据id删除 payment_bill （付款单对应订单关系表） 
				paymentBillMapper.delete(selectPaymentBillId);
				//根据对账单id 删除 payment_bill_order （付款单对应订单关系表）
				paymentBillMapper.delete1(selectPaymentBillId);
			}
			/*PaymentBill paymentBill=new PaymentBill();
			paymentBill.setId(selectPaymentBillId);
			String param="";
			if(thirdTradeCodeCompanyAccount.equals(THIRDTRADECODE)){
				param=StringUtils.isBlank(request.getParameter(THIRDTRADECODE))?null:request.getParameter(THIRDTRADECODE).toString();
				paymentBill.setThirdTradeCode(param);
				paymentBill.setCompanyAccount("3");
				paymentBill.setPayType("2");
			}else if(thirdTradeCodeCompanyAccount.equals(COMPANYACCOUNT)){
				param=StringUtils.isBlank(request.getParameter(COMPANYACCOUNT))?null:request.getParameter(COMPANYACCOUNT).toString();
				paymentBill.setCompanyAccount(param);
				paymentBill.setPayType("2");
			}
			x=paymentBillMapper.updateInt(paymentBill);update payment_bill  
			if(x==0){
				throw new RuntimeException("更新付款单失败");
			}	*/
			if(thirdTradeCodeCompanyAccount.equals(COMPANYACCOUNT)
			&&StringUtils.isNotBlank(request.getParameter(COMPANYACCOUNT))){
				OrderInfo orderMessage=new OrderInfo();
				orderMessage.setCompanyAccount(Integer.parseInt(request.getParameter(COMPANYACCOUNT)));
				orderMessage.setId(Long.valueOf(request.getParameter("id")));
				if(request.getParameter("paymentAccountId")!=null||request.getParameter("paymentAccountId")==""){
					orderMessage.setPaymentAccount(Integer.valueOf(request.getParameter("paymentAccountId")));
				}
				orderMessage.setOrderStatus("9");
				orderMessage.setPayStatus("0");
				orderMessage.setPayType("2");
				m=orderDao.updateOrderOfflinePay(orderMessage);
				if(m==0){
					throw new RuntimeException("更新付款单和订单失败");
				}		
			}else if(thirdTradeCodeCompanyAccount.equals(THIRDTRADECODE)
						&&StringUtils.isNotBlank(request.getParameter(THIRDTRADECODE))){
					OrderInfo orderMessage=new OrderInfo();
					orderMessage.setCompanyAccount(3);//支付宝
					orderMessage.setId(Long.valueOf(request.getParameter("id")));
					if(request.getParameter("paymentAccountId")!=null||request.getParameter("paymentAccountId")==""){
						orderMessage.setPaymentAccount(Integer.valueOf(request.getParameter("paymentAccountId")));
					}
					orderMessage.setOrderStatus("9");
					orderMessage.setPayStatus("0");
					orderMessage.setPayType("2");
					int n=orderDao.updateOrderOfflinePay(orderMessage);
					if(n==0){
						throw new RuntimeException("更新订单失败");
					}
				}
		}else{
			//from order_info 根据id查询订单
		    OrderInfo orderInfo = orderService.getOrderById(Long.valueOf(request.getParameter("id")));
		    if(null!=orderInfo){
		    	int z=0;
		    	/*int y=0;
		    	int z=0;
				*//***********保存付款单start**************//*
				int r1=(int)(Math.random()*(10));//产生4个0-9的随机数
				int r2=(int)(Math.random()*(10));
				int r3=(int)(Math.random()*(10));
				int r4=(int)(Math.random()*(10));
				long now = System.currentTimeMillis();//一个13位的时间戳
				String paymentID =String.valueOf(r1)+String.valueOf(r2)+String.valueOf(r3)+String.valueOf(r4)+String.valueOf(now);// 订单ID
				PaymentBillPage paymentBillPage = new PaymentBillPage();
				paymentBillPage.setId(paymentID);
				paymentBillPage.setOrderId(Long.valueOf(request.getParameter("id")));
				paymentBillPage.setBuyerId(Integer.valueOf(loginInfo.getDealerId()));
				paymentBillPage.setPaymentScoreAmount(orderInfo.getScoreDeductionAmout()==null?0 : orderInfo.getScoreDeductionAmout().doubleValue());//现金券 scoreDeductionAmout
				paymentBillPage.setShouldAmount(orderInfo.getPayShouldAmount()==null?0 : orderInfo.getPayShouldAmount().doubleValue());
				paymentBillPage.setRealAmount(orderInfo.getPayAmount()==null?0 : orderInfo.getPayAmount().doubleValue());
				paymentBillPage.setPayStatus("0");
				paymentBillPage.setPayType("2");
				paymentBillPage.setStatus("1");
				paymentBillPage.setPayTime(new Timestamp(System.currentTimeMillis()));*/
				if(thirdTradeCodeCompanyAccount.equals(COMPANYACCOUNT)){
					/*paymentBillPage.setCompanyAccount(request.getParameter(COMPANYACCOUNT));*/
					if(StringUtils.isNotBlank(request.getParameter(COMPANYACCOUNT))){
						orderInfo.setCompanyAccount(Integer.parseInt(request.getParameter(COMPANYACCOUNT)));
					}
					orderInfo.setId(Long.valueOf(request.getParameter("id")));
					if(request.getParameter("paymentAccountId")!=null||request.getParameter("paymentAccountId")==""){
						orderInfo.setPaymentAccount(Integer.valueOf(request.getParameter("paymentAccountId")));
					}
					orderInfo.setOrderStatus("9");
					orderInfo.setPayStatus("0");
					orderInfo.setPayType("2");
					z=orderDao.updateOrderOfflinePay(orderInfo);
					if(z==0){
						throw new RuntimeException("更新订单失败");
					}
				}else if(thirdTradeCodeCompanyAccount.equals(THIRDTRADECODE)){
					/*paymentBillPage.setThirdTradeCode(request.getParameter(THIRDTRADECODE));
					paymentBillPage.setCompanyAccount("3");*/
					OrderInfo orderMessage=new OrderInfo();
					orderMessage.setCompanyAccount(3);//支付宝
					orderMessage.setId(Long.valueOf(request.getParameter("id")));
					if(request.getParameter("paymentAccountId")!=null||request.getParameter("paymentAccountId")==""){
						orderMessage.setPaymentAccount(Integer.valueOf(request.getParameter("paymentAccountId")));
					}				
					orderMessage.setOrderStatus("9");
					orderMessage.setPayStatus("0");
					orderMessage.setPayType("2");
					int nn=orderDao.updateOrderOfflinePay(orderMessage);
					if(nn==0){
						throw new RuntimeException("更新订单失败");
					}
				}
				/*y=addPaymentBillService.savePaymentBillParam(paymentBillPage);//保存付款单和订单关系
			    if(y==0){ payment_bill_order
					throw new RuntimeException("提交失败");
				}*/
			}else{
			     throw new RuntimeException("无订单信息");
		    }	
		}
		return 1;
	}
	
	/**
	 * 合同
	 * @param 
	 * @throws Exception 
	 *
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map selectContract(Long orderId) throws Exception {
		if(null==orderId){
			throw new RuntimeException("没有合同订单！");
		}
		/**
		 * 根据订单id查询订单信息以及买方供方信息
		 * @param id
		 * @author felx
		 * @return
		 */
		Map map =orderDao.findOrderInfoById(orderId);
		String amountVal="";
		if(null!=map.get("amount")&&""!=map.get("amount")){
          //金额转中文大写
		  amountVal=NumberToRBM.number2CNMontrayUnit(new BigDecimal(map.get("amount").toString()));
		}
		map.put("amountVal", amountVal);
		/**
		 * 根据订单号查询商品列表
		 * add 2016 06 29
		 * @param id
		 * @author felx
		 * @return
		 */
		List goodsList = orderDao.selectOrderInfoGoodsList(orderId);		
		map.put("goodsList", goodsList);
		
		
		/*String isHave=orderDao.selectCrmCusLikmanStatus(String.valueOf(orderId));
		String paramValue="";
		if(StringUtils.isNotBlank(isHave)&&isHave.equals("1")){
			paramValue=" AND cc.status !='2' AND clm.status='1' AND clm.is_default='1' ";
		}
		List orderList = orderDao.selectContract(orderId,paramValue);
		Map<String,Object> allMap = new LinkedHashMap<String,Object>();
		List<Map> lt=new LinkedList<Map>();
		Map<String,Object> paramMap=null;
		int m=1;
		for(Object obj : orderList){
			Map firstMap = (Map)obj;
			if(m==1){
				allMap.put("dealerName", null==firstMap.get("dealerName")?"":firstMap.get("dealerName").toString());
				allMap.put("telephone", null==firstMap.get("telephone")?"":firstMap.get("telephone").toString());
				allMap.put("orderId", null==firstMap.get("orderId")?"":firstMap.get("orderId").toString());
				allMap.put("buyerName", null==firstMap.get("buyerName")?"":firstMap.get("buyerName").toString());
				allMap.put("buyerLinkman", null==firstMap.get("buyerLinkman")?"":firstMap.get("buyerLinkman"));
				allMap.put("buyerTelephone", null==firstMap.get("buyerTelephone")?"":firstMap.get("buyerTelephone").toString());
				allMap.put("buyerFax", null==firstMap.get("buyerFax")?"":firstMap.get("buyerFax").toString());
				
				allMap.put("orderTime", firstMap.get("orderTime"));
				allMap.put("sellerName", null==firstMap.get("sellerName")?"":firstMap.get("sellerName").toString());
				allMap.put("sellerLinkman", null==firstMap.get("sellerLinkman")?"":firstMap.get("sellerLinkman").toString());
				allMap.put("sellerTelephone", null==firstMap.get("sellerTelephone")?"":firstMap.get("sellerTelephone").toString());
				allMap.put("sellerFax", null==firstMap.get("sellerFax")?"":firstMap.get("sellerFax").toString());
				String amountVal="";
				if(null!=firstMap.get("amount")&&""!=firstMap.get("amount")){
				//amountVal=new CnUpperCaser(firstMap.get("amount").toString()).getCnString();
				  amountVal=new AmountToChar(firstMap.get("amount").toString()).getCharAmount();
				}
				allMap.put("amountLittle", null==firstMap.get("amount")?"":firstMap.get("amount").toString());
				allMap.put("amount", amountVal);
				allMap.put("address", null==firstMap.get("address")?"":firstMap.get("address").toString());
				StringBuffer saleDiscount = new StringBuffer();
				BigDecimal decimal = new BigDecimal(firstMap.get("otherDiscount").toString());
				BigDecimal decimal1 = new BigDecimal(firstMap.get("fullCutDiscount").toString());
				BigDecimal decimal2 = new BigDecimal(firstMap.get("grouponDiscount").toString());
				if(decimal.compareTo(BigDecimal.ZERO)!=0){
					saleDiscount.append("其他优惠:"+firstMap.get("otherDiscount").toString()+"<br>");
				}
				if(decimal1.compareTo(BigDecimal.ZERO)!=0){
					saleDiscount.append("满减优惠:"+firstMap.get("fullCutDiscount").toString()+"<br>");
				}
				if(decimal2.compareTo(BigDecimal.ZERO)!=0){
					saleDiscount.append("团购优惠:"+firstMap.get("grouponDiscount").toString()+"<br>");
				}
				allMap.put("discountTotal", saleDiscount);
				allMap.put("otherDiscount", firstMap.get("otherDiscount").toString());//其他优惠
				allMap.put("fullCutDiscount", firstMap.get("fullCutDiscount").toString());//满减优惠
				allMap.put("grouponDiscount", firstMap.get("grouponDiscount").toString());//团购优惠
				allMap.put("freightTotalAmount", null==firstMap.get("freightTotalAmount")?"":firstMap.get("freightTotalAmount").toString());
			}
			paramMap=new LinkedHashMap<String,Object>();
			paramMap.put("buyNo", null==firstMap.get("buyNo")?"":firstMap.get("buyNo").toString());
			paramMap.put("title", null==firstMap.get("title")?"":firstMap.get("title").toString());
			paramMap.put("measure", null==firstMap.get("measure")?"":firstMap.get("measure").toString());
			paramMap.put("num", null==firstMap.get("num")?"":firstMap.get("num").toString());
			paramMap.put("model", null==firstMap.get("model")?"":firstMap.get("model").toString());
			paramMap.put("nameCn", null==firstMap.get("nameCn")?"":firstMap.get("nameCn").toString());
			DecimalFormat de=new DecimalFormat("######0.00");
			DecimalFormat d=new DecimalFormat("######0.0000");
			BigDecimal g = null;
			BigDecimal g2 = null;
			if(null!=firstMap.get("saleUnitPrice")&&""!=firstMap.get("saleUnitPrice")){
				g=new BigDecimal(firstMap.get("saleUnitPrice").toString());
				paramMap.put("saleUnitPrice", d.format(g));
			}else{
				paramMap.put("saleUnitPrice", "");
			}
			if(null!=firstMap.get("num")&&""!=firstMap.get("num")){
				g2=new BigDecimal(firstMap.get("num").toString());
			}
			if(null!=g&&null!=g2){
				paramMap.put("everyAmount",de.format(g.multiply(g2)));
			}else{
				paramMap.put("everyAmount","");
			}
			paramMap.put("delivery","");
			lt.add(paramMap);
			m=2;
		}
		allMap.put("allBodyMap", lt);*/
		return map;
	}

	@Override
	public Map<String, Object> getContractById(String id) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = orderDao.createContract(id);
		String[] keys = {"groupOrderSale","fullCutAmount","beanDeduction","otherAmount"};
		String[] text = {"聚划算优惠:","满减优惠:","工币抵扣:","订单优惠:"};
		StringBuffer sale = new StringBuffer("");
		DecimalFormat de=new DecimalFormat("######0.00");
		for (int i = 0,len=keys.length; i < len; i++) {
			Object o = data.get(keys[i]);
			BigDecimal m = new BigDecimal(o.toString());
			if(m.compareTo(BigDecimal.ZERO)>0){
				if(sale.length()==0){
					sale.append(text[i]+de.format(m.doubleValue()));
				}else{
					sale.append("<br/>"+text[i]+de.format(m.doubleValue()));
				}
			}
		}
		DESCoder.instanceMobile();
		data.put("buyerTel", data.get("buyerTel")!=null&&StringUtils.isNotBlank(data.get("buyerTel").toString())?DESCoder.decoder(data.get("buyerTel").toString()):"");
		BigDecimal pay = new BigDecimal(data.get("payAmount").toString()).subtract(new BigDecimal(data.get("balanceDeduction").toString()));
		data.put("charAmount", NumberToRBM.number2CNMontrayUnit(pay));
		map.put("orderInfo", data);
		map.put("saleChar", sale);
		map.put("orderGoods", orderGoodsDao.getGoodsByOrderId(id));
		return map;
	}
	
	/**
	 *Saas供应商订单列表 
	 *@author jxw 
	 */
	@Override
	public Map<String, Object> selectSellerOrderList(Integer dealerId,Integer page,HttpServletRequest request) throws Exception{
		String orderId = request.getParameter("orderId");
		String dealerName = request.getParameter("dealerName");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String payType = request.getParameter("payType");
		Integer rows = StringUtils.isNotBlank(request.getParameter("rows"))?Integer.parseInt(request.getParameter("rows")) : 8;
		Integer status=0;
		if(StringUtils.isBlank(request.getParameter("status"))){
			throw new RuntimeException("订单状态出错");
		}
		status=Integer.parseInt(request.getParameter("status"));
		if((page=page-1)<0){
			throw new RuntimeException("页数出错");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> querymap = new HashMap<String, Object>();
		querymap.put("orderId", StringUtils.isNotBlank(orderId)?orderId : null);
		querymap.put("dealerName", StringUtils.isNotBlank(dealerName)?dealerName : null);
		querymap.put("startTime", StringUtils.isNotBlank(startTime)?startTime : null);
		querymap.put("endTime", StringUtils.isNotBlank(endTime)?endTime : null);
		querymap.put("payType", StringUtils.isNotBlank(payType)?payType : null);
		querymap.put("dealerId", dealerId);
		querymap.put("status", status);
		querymap.put("page", page);
		querymap.put("rows", rows);
		List<Map> orderList = orderDao.selectOrderDealByDealerId(querymap);
		for (int i = 0,len = orderList.size(); i < len; i++) {
			Map order = orderList.get(i);
			if(order.get("freightAmount")!=null){
				if(!"".equals(order.get("freightAmount").toString())){
					DecimalFormat fmt = new DecimalFormat("0.00");
					order.put("freightAmount", fmt.format(Double.parseDouble(order.get("freightAmount").toString())));
				}
			};
			List<Map> goods = orderGoodsDao.selectSellerGoodsByOrderId(order.get("orderId").toString());
			order.put("orderGoods", goods);
			String adress = order.get("consignAddressInfo")!=null?order.get("consignAddressInfo").toString() : "";
			try {
				JSONObject jo = JSONObject.parseObject(adress);
				order.put("consignee",	 jo.get("consignee")!=null?jo.get("consignee").toString() : "");
				order.put("mobile",	 jo.get("mobile")!=null?jo.get("mobile").toString() : "");
				if(null==order.get("sellerLogisticsId")||null==order.get("logisticsCode")){
					throw new RuntimeException("获取物流公司数据出错");
				}
				LogisticsCompany  logistics= LogisticsCompanyI.fingById(Integer.parseInt(order.get("sellerLogisticsId").toString()));
				order.put("logisticsUrl", Delivery.search(logistics.getLogisticsCode(),order.get("logisticsCode").toString()));
			} catch (Exception e) {
				if(null==order.get("consignAddressId")){
					throw new RuntimeException("数据出错");
				}
				Map consignAddressPage  = addressMapper.findById(Integer.parseInt(order.get("consignAddressId").toString()));
				order.put("consignee",	 consignAddressPage.get("consignee")!=null?consignAddressPage.get("consignee"):"");
				order.put("mobile",	 consignAddressPage.get("mobile")!=null?consignAddressPage.get("mobile"):"");
				order.put("logisticsUrl", "");
			}
			orderList.set(i, order);
		}
		map.put("rows", orderList);
		map.put("total", orderDao.getOrderListByDealerIdCountVal(querymap));
		return map;
	        }

	@Override
	public Map<String, Object> getOrderDetailBySaas(String orderId,
			String dealerId) {
		Map detail = orderDao.findOrderDetail(orderId,dealerId);
		detail.put("goodsSaleAmount" ,detail.get("saleAmount"));
		detail.put("userBean" ,gbeansServiceI.getDealerBeanByOrder(orderId));
		detail.put("activityInfo" ,"");
		String adress = detail.get("consignAddressInfo")!=null?detail.get("consignAddressInfo").toString() : "";
        /**
         *create by yr on 2018-10-19
         * 增加行政区划汉字
         */
        Map<String, Object> cityMap = new HashMap<>();
        Gson gson = new Gson();
        cityMap = gson.fromJson(adress, cityMap.getClass());
        String areaId = cityMap.get("areaId")!=null?cityMap.get("areaId").toString().substring(0,cityMap.get("areaId").toString().indexOf(".")):"";
        String cityId = cityMap.get("cityId")!=null?cityMap.get("cityId").toString().substring(0,cityMap.get("cityId").toString().indexOf(".")):"";
        String provinceId =  cityMap.get("provinceId")!=null?cityMap.get("provinceId").toString().substring(0,cityMap.get("provinceId").toString().indexOf(".")):"";
        String areaName = orderGoodsDao.findNameById(areaId);
        String cityName = orderGoodsDao.findNameById(cityId);
        String provinceName = orderGoodsDao.findNameById(provinceId);
        cityMap.put("areaName",areaName);
        cityMap.put("cityName",cityName);
        cityMap.put("provinceName",provinceName);
        cityMap.put("areaId",areaId);
        cityMap.put("cityId",cityId);
        cityMap.put("provinceId",provinceId);


		if("8".equals(detail.get("orderChannel").toString()) && "2".equals(detail.get("status").toString())){
			if(orderDao.findIsFirstWxOrder(dealerId)==1){
				String info= PropertyUtil.getPropertyValue("properties/info.properties", "fristWxOrderInfo");
				detail.put("activityInfo" ,info);
			}
		}
		/*detail.put("logisticsUrl", "");
		if(detail.get("sellerLogisticsId")!=null && !"".equals(detail.get("sellerLogisticsId").toString())){
			LogisticsCompany  logistics= LogisticsCompanyI.fingById(Integer.parseInt(detail.get("sellerLogisticsId").toString()));
			try {
				detail.put("logisticsUrl", Delivery.search(logistics.getLogisticsCode(),detail.get("logisticsCode").toString()));
			} catch (Exception e) {
				detail.put("logisticsUrl", "");
			}
		}*/ 
		List<Map> defaultList = orderDao.findGoodsBytype(orderId,0);
		List<Map> groupList = orderDao.findGoodsBytype(orderId,1);
		List<Map> packageGoodsList = orderDao.findGoodsBytype(orderId,3);
		List<GoodsPackage> packageList = new ArrayList<>();
		if(packageGoodsList.size()>0){
			packageList = JSONArray.parseArray(detail.get("packageInfo").toString(),GoodsPackage.class);
			for (Map cart : packageGoodsList) {
				for (GoodsPackage p : packageList) {
					String packageId = p.getPackageId();
					if(packageId.equals(cart.get("activityId").toString())){
						if(p.getGoodsList()!=null){
							p.getGoodsList().add(cart);
						}else{
							p.setGoodsList(new ArrayList<Object>());
							p.getGoodsList().add(cart);
						}
					}
				}
			}
		}
		List<Map<String, Object>> list = new ArrayList<>();
		if(defaultList!=null && defaultList.size()>0){
			Map<String, Object> map = new HashMap<>();
			map.put("name", "工电宝");
			map.put("list", defaultList);
			map.put("type", 0);
			list.add(map);
		}
		if(packageList!=null && packageList.size()>0){
			Map<String, Object> map = new HashMap<>();
			map.put("name", "套餐");
			map.put("list", packageList);
			map.put("type", 3);
			list.add(map);
		}
		if(groupList!=null && groupList.size()>0){
			Map<String, Object> map = new HashMap<>();
			map.put("name", "团购");
			map.put("list", groupList);
			map.put("type", 1);
			list.add(map);
		}
/*		ResponseData data = new ResponseData(keyArray);
		for (int i = 0,len=keyArray.length; i < len; i++) {
			data.addResponseList(orderDao.findGoodsBytype(orderId,i));
		}*/
		detail.put("orderGoods", list);
        detail.put("cityMap",cityMap);
		return detail;
	}




	@Override
	public Map<String, Object> confirmOrderBySaas(String ids, String dealerId,
			String isCash, String addrId) throws Exception {
		OrderCountModel page = cartService.findSaasShoppingCart(Integer.parseInt(dealerId), ids, isCash, addrId);
		OrderOut out = new OrderOut(page);
		return out.getConfirmOrderBySaas();
	}

	@Override
	public Map<String, Object> confirmOrderByApp(String ids, String dealerId,
			String isCash,
			String isBean, 
			String addrId) throws Exception {
		OrderCountModel page = cartService.findSaasShoppingCart(Integer.parseInt(dealerId), ids, isCash, addrId);
		OrderOut out = new OrderOut(page);
		return out.getConfirmOrderByApp(isBean);
	}

	@Override
	public Map<String, Object> getOrderDetailByApp(String orderId,
			String dealerId) {
		String[] attrValue={"商品总金额","抵扣券抵扣","满减金额","团购总优惠","商品优惠","工币可抵扣金额","可获积分","可获得工币"};
		DecimalFormat df   = new DecimalFormat("######0.00");
		Map detail = orderDao.findOrderDetail(orderId,dealerId);
		detail.put("goodsSaleAmount" ,detail.get("saleAmount"));
		double freightAmount= new BigDecimal(detail.get("freightAmount").toString()).subtract(new BigDecimal(detail.get("freightSaleAmount").toString())).doubleValue();
		detail.put("freightAmount" ,df.format(freightAmount));
		String adress = detail.get("consignAddressInfo")!=null?detail.get("consignAddressInfo").toString() : "";
		detail.put("logisticsUrl", "");
		if(detail.get("sellerLogisticsId")!=null && !"".equals(detail.get("sellerLogisticsId").toString())){
			LogisticsCompany  logistics= LogisticsCompanyI.fingById(Integer.parseInt(detail.get("sellerLogisticsId").toString()));
			try {
				detail.put("logisticsUrl", Delivery.search(logistics.getLogisticsCode(),detail.get("logisticsCode").toString()));
			} catch (Exception e) {
				detail.put("logisticsUrl", "");
			}
		}
		List<Map> defaultList = orderDao.findGoodsBytype(orderId,0);
		List<Map> groupList = orderDao.findGoodsBytype(orderId,1);
		List<Map> packageGoodsList = orderDao.findGoodsBytype(orderId,3);
		List<GoodsPackage> packageList = new ArrayList<>();
		if(packageGoodsList.size()>0){
			packageList = JSONArray.parseArray(detail.get("packageInfo").toString(),GoodsPackage.class);
			for (Map cart : packageGoodsList) {
				for (GoodsPackage p : packageList) {
					String packageId = p.getPackageId();
					if(packageId.equals(cart.get("activityId").toString())){
						if(p.getGoodsList()!=null){
							p.getGoodsList().add(cart);
						}else{
							p.setGoodsList(new ArrayList<Object>());
							p.getGoodsList().add(cart);
						}
					}
				}
			}
		}
		List<Map<String, Object>> list = new ArrayList<>();
		List<Object> appList = new ArrayList<Object>();
		if(defaultList!=null){
			appList.addAll(defaultList);
		}
		if(packageList!=null){
			appList.addAll(packageList);
		}
		if(appList!=null && appList.size()>0){
			Map<String, Object> map = new HashMap<>();
			map.put("name", "工电宝");
			map.put("list", appList);
			map.put("type", 0);
			list.add(map);
		}
		if(groupList!=null && groupList.size()>0){
			Map<String, Object> map = new HashMap<>();
			map.put("name", "团购");
			map.put("list", groupList);
			map.put("type", 1);
			list.add(map);
		}

		detail.put("orderGoods", list);
		List<Map<String, Object>> attrList = new ArrayList<Map<String,Object>>();
		for (int i = 0,len = attrValue.length; i < len; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", attrValue[i]);
			switch (i) {
				case 0: map.put("value", "￥" + detail.get("goodsAmount").toString());
							 attrList.add(map);
				break;
				case 1: map.put("value", "- ￥" + detail.get("scoreDeductionAmout").toString());
					if(!(Double.parseDouble(detail.get("scoreDeductionAmout").toString())==0)){
						attrList.add(map);
					}
				break;
				case 2: map.put("value", "- ￥" + detail.get("fullCutAmount").toString());
					if(!(Double.parseDouble(detail.get("fullCutAmount").toString())==0)){
						attrList.add(map);
					}
				break;
				case 3: map.put("value", "- ￥" + detail.get("groupOrderSale").toString());
				if(!(Double.parseDouble(detail.get("groupOrderSale").toString())==0)){
					attrList.add(map);
				}
				break;
				case 4: map.put("value", "- ￥" + detail.get("goodsSale").toString());
				if(!(Double.parseDouble(detail.get("goodsSale").toString())==0)){
					attrList.add(map);
				}
				break;
				case 5: map.put("value", "- ￥" + detail.get("beanDeduction").toString());
				break;
				case 6: map.put("value", detail.get("score").toString()+"分");
				break;
				case 7: map.put("value", detail.get("beanQuantity").toString()+"个");
				break;
			}
			
		}
		List<List<Map<String, Object>>> attrArrary = new ArrayList<>();
		List<Map<String, Object>> list1 = new ArrayList<>();
		List<Map<String, Object>> list2 = new ArrayList<>();
		List<Map<String, Object>> list3 = new ArrayList<>();
		String[] attrValue1={"商品总金额","运费"};
		String[] attrValue2={"抵扣券抵扣","余额抵扣","满减金额","团购总优惠","商品优惠","工币抵扣"};
		String[] attrValue3={"可获积分","可获得工币"};
		
		for (int i = 0,len = attrValue1.length; i < len; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", attrValue1[i]);
			switch (i) {
			case 0:map.put("value", "￥" + detail.get("goodsAmount").toString());
				list1.add(map);
				break;
			case 1:map.put("value", "￥" + detail.get("freightAmount").toString());
				list1.add(map);
				break;
			}
		}
		for (int i = 0,len = attrValue2.length; i < len; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", attrValue2[i]);
			switch (i) {
			case 0:map.put("value", "- ￥" + detail.get("scoreDeductionAmout").toString());
						if(!(Double.parseDouble(detail.get("scoreDeductionAmout").toString())==0)){
							list2.add(map);
						}
				break;
			case 1:map.put("value", "- ￥" + detail.get("balanceDeduction").toString());
			if(!(Double.parseDouble(detail.get("balanceDeduction").toString())==0)){
				list2.add(map);
			}
			break;
			case 2:map.put("value", "- ￥" + detail.get("fullCutAmount").toString());
						if(!(Double.parseDouble(detail.get("fullCutAmount").toString())==0)){
							list2.add(map);
						}
				break;
			case 3:map.put("value", "- ￥" +  detail.get("groupOrderSale").toString());
						if(!(Double.parseDouble(detail.get("groupOrderSale").toString())==0)){
							list2.add(map);
						}
			break;
			case 4:map.put("value", "- ￥" +  detail.get("goodsSale").toString());
			if(!(Double.parseDouble(detail.get("goodsSale").toString())==0)){
				list2.add(map);
			}
			break;
			case 5:map.put("value", "- ￥" +  detail.get("beanDeduction").toString());
				if(!(Double.parseDouble(detail.get("beanDeduction").toString())==0)){
					list2.add(map);
				}
			break;
			}
		}
		for (int i = 0,len = attrValue3.length; i < len; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", attrValue3[i]);
			switch (i) {
			case 0:map.put("value", detail.get("score").toString() + "分");
				if(!"0".equals(detail.get("score").toString())){
					list3.add(map);
				}
				break;
			case 1:map.put("value", detail.get("beanQuantity").toString() + "个");
						if(!"0".equals(detail.get("beanQuantity").toString())){
							list3.add(map);
						}
				break;
			}
		}
		if(list1.size()>0){
			attrArrary.add(list1);
		}
		if(list2.size()>0){
			attrArrary.add(list2);
		}
		if(list3.size()>0){
			attrArrary.add(list3);
		}
		detail.put("attrs", attrList);
		detail.put("attrsList", attrArrary);
		return detail;
	}

	@Override
	public Map<String, Object> getOrderLIstByApp(OrderDealPage orderPage) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> orderList = orderDao.getOrderListByOrderDealPage(orderPage);
		for (int i = 0,len = orderList.size(); i < len; i++) {
			Map order = orderList.get(i);
			ResponseData data = new ResponseData(keyArray);
			List<Map> goodsList = orderGoodsDao.getGoodsByOrderId(order.get("orderId").toString());
			List<Map> goodsArray = new ArrayList<Map>();
			List<Map> groupArray = new ArrayList<Map>();
			for (Map goods : goodsList) {
				switch (goods.get("goodsType").toString()) {
				case "0":case "3":
					goodsArray.add(goods);
					break;
				case "1":
					groupArray.add(goods);
					break;
				}
			}
			data.addResponseList(goodsArray);
			data.addResponseList(groupArray);
			String adress = order.get("consignAddressInfo")!=null?order.get("consignAddressInfo").toString() : "";
			try {
				JSONObject jo = JSONObject.parseObject(adress);
				order.put("consignee",	 jo.get("consignee")!=null?jo.get("consignee").toString() : "");
				order.put("mobile",	 jo.get("mobile")!=null?jo.get("mobile").toString() : "");
			} catch (Exception e) {
				Map consignAddressPage  = addressMapper.findById(Integer.parseInt(order.get("consignAddressId").toString()));
				order.put("consignee",	 consignAddressPage.get("consignee")!=null?consignAddressPage.get("consignee"):"");
				order.put("mobile",	 consignAddressPage.get("mobile")!=null?consignAddressPage.get("mobile"):"");
			}
			try {
				LogisticsCompany  logistics= LogisticsCompanyI.fingById(Integer.parseInt(order.get("sellerLogisticsId").toString()));
				order.put("logisticsUrl", Delivery.search(logistics.getLogisticsCode(),order.get("logisticsCode").toString()));
			} catch (Exception e) {
				order.put("logisticsUrl", "");
			}
			order.put("orderGoods", data.getReturnData());
			orderList.set(i, order);
		}
		map.put("rows", orderList);
		map.put("total", orderDao.getOrderListByOrderDealPageCount(orderPage));
		return map;
	}

	@Override
	public EditOrder editOrderGoodsBySaas(PageMode page) throws Exception {
		if(!page.contains("orderId","sellerGoodsId","num","orderGoodsId")){
			throw new Exception("参数错误");
		}
		String[] sellerGoodsId = page.split("sellerGoodsId", ",");
		String[] orderGoodsId = page.split("orderGoodsId", ",");
		String[] num = page.split("num", ",");
		String orderId = page.getStringValue("orderId");
		String dealerId = page.getSaasLogin().getDealerId();
		if(sellerGoodsId.length!=0 && sellerGoodsId.length==num.length){
			EditOrder info = orderDao.getOrderInfoByEdit(orderId,dealerId);
			List<EditOrderGoods> orderGoods = orderDao.getOrderGoodsByEdit(orderId);
			List<EditGoods> goods = orderDao.getGoodsByEdit(sellerGoodsId,dealerId);
			if(info==null){
				throw new Exception("订单修改失败");
			}
			Map<String, EditOrderGoods> OrderGoodsMap= toMapByOrderGoods(orderGoods);
			Map<String, EditGoods> goodsMap= toMapByGoods(goods);
			List<EditOrderGoods>  newGoods= new ArrayList<EditOrderGoods>();
			for (int i = 0; i < sellerGoodsId.length; i++) {
				EditOrderGoods og = OrderGoodsMap.get(orderGoodsId[i]);
				if(!OrderGoodsMap.containsKey(orderGoodsId[i])){
					og = new EditOrderGoods();
					og.setOrderId(info.getId());
				}
				if(og.getGoodsType()==0){
					goodsMap.get(sellerGoodsId[i]).setNum(Integer.parseInt(num[i]));
					og.conversionSellerGoods(goodsMap.get(sellerGoodsId[i]));
				}
				newGoods.add(og);
			}
			newGoods = replaceAllGoods(newGoods);
			BigDecimal freightAmount = amountAlgorithmI.getEditFreightAmount(newGoods, info.findProvinceId());
			BigDecimal fullCutAmount = orderDao.getFullCutAmount(newGoods);
			int cash = orderDao.findOrderBeforeCash(orderId);
			info.countAmount(newGoods,freightAmount,fullCutAmount);
			info.setBean(gbeansServiceI.getDealerBeanByOrder(sellerGoodsId,num));
			info.setUseBean(new BigDecimal(gbeansServiceI.getDealerBeanByOrder(orderId)).divide(new BigDecimal(100)).doubleValue());
			return info.iscash(cash);
		}else{
			throw new Exception("参数错误");
		}
	}
	/**
	 * 去掉重复的商品
	 * @param newGoods
	 * @return
	 * @throws Exception 
	 */
	private List<EditOrderGoods> replaceAllGoods(List<EditOrderGoods> newGoods) throws Exception {
		Map<Long ,Object> map = new HashMap<Long, Object>();
		for (EditOrderGoods editOrderGoods : newGoods) {
			if(!map.containsKey(editOrderGoods.getSellerGoodsId())){
				map.put(editOrderGoods.getSellerGoodsId(), editOrderGoods);
			}else{
				throw new Exception("商品已经存在");
			}
		}
		List<EditOrderGoods> g = new ArrayList<EditOrderGoods>();
		for (Entry<Long, Object> entity : map.entrySet()) {
			g.add((EditOrderGoods) entity.getValue());
		}
		return g;
	}

	/**
	 * 保存历史订单信息到mongoliandb
	 * @param info 订单信息
	 * @param orderGoods 商品集合
	 * @param o 
	 */
	private void editOrderGoodsBySaasSaveMongoDb(EditOrder info,
			List<EditOrderGoods> orderGoods, EditOrder o) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("order", JSONObject.toJSONString(info));
		map.put("orderId", info.getId());
		map.put("orderDetail", JSONArray.toJSONString(orderGoods));
		map.put("updatetime", DateTypeHelper.getCurrentDateTimeString());
		map.put("newOrder", JSONObject.toJSONString(o));
//		mongoTemplate.save(map, "editOrderLogs");
	}
	/**
	 * List OrderGoods转换Map
	 * @param orderGoods
	 * @return
	 */
	private Map<String, EditOrderGoods> toMapByOrderGoods(
			List<EditOrderGoods> orderGoods) {
		Map<String, EditOrderGoods> map = new HashMap<String, EditOrderGoods>();
		for (EditOrderGoods editOrderGoods : orderGoods) {
			map.put(editOrderGoods.getId().toString(), editOrderGoods);
		}
		return map;
	}
	/**
	 * List Goods转换Map
	 * @param goods
	 * @return
	 */
	private Map<String, EditGoods> toMapByGoods(List<EditGoods> goods) {
		Map<String, EditGoods> map = new HashMap<String, EditGoods>();
		for (EditGoods g : goods) {
			map.put(g.getId().toString(), g);
		}
		return map;
	}
	/**
	 * 保存修改的订单
	 */
	@Override
	public void saveEditOrder(PageMode page)  throws Exception{
		String orderId = page.getStringValue("orderId");
		String dealerId = page.getSaasLogin().getDealerId();
		EditOrder info = orderDao.getOrderInfoByEdit(orderId,dealerId);
		List<EditOrderGoods> orderGoods = orderDao.getOrderGoodsByEdit(orderId);
		EditOrder o = editOrderGoodsBySaas(page);
		editOrderGoodsBySaasSaveMongoDb(info,orderGoods,o);
		orderDao.updateOrderReturnCash(o);
		orderDao.updateEditOrder(o);
		orderDao.updateOrderNewCash(o);
		orderDao.delOrderGoods(o);
		orderDao.delCashRecord(orderId);
		orderDao.insertCashRecord(orderId);
		if(page.contains("isBean")&&"1".equals(page.getStringValue("isBean"))){
			gbeansServiceI.toUserBeans(orderId, dealerId);
		}else{
			gbeansServiceI.toNotUserBeans(orderId, dealerId);
		}
		for (EditOrderGoods editOrderGoods : o.takeOrderGoods()) {
				orderDao.saveEditOrderGoods(editOrderGoods);
		}
		gbeansServiceI.toOrderBeans(orderId);
	}

	/**
	 * 根据订单号查询支付金额及支付方式 
	 * @param orderId
	 * @return
	 */
	@Override
	public List selectPaymentAmountAndMethod(long orderId) throws Exception{
		
		return orderDao.selectPaymentAmountAndMethod(orderId);
	}
	

	public static final Object toBean(Class<?> type, Map<String, ? extends Object> map)   
            throws Exception {  
        BeanInfo beanInfo = Introspector.getBeanInfo(type);  
        Object obj = type.newInstance();  
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
        for (int i = 0; i< propertyDescriptors.length; i++) {  
            PropertyDescriptor descriptor = propertyDescriptors[i];  
            String propertyName = descriptor.getName();  
            if (map.containsKey(propertyName)) {  
                Object value = map.get(propertyName);  
                if(value != null){
                	Object[] args = new Object[1];  
                	args[0] = Long.parseLong(String.valueOf(value));  
                	descriptor.getWriteMethod().invoke(obj, args);  
                }
            }  
        }  
        return obj;  
    }
	
	public static Object toClass(Object type, Map<String, ? extends Object> map){
		return map;
	}

	//商城首页查询订单 待付款，待发货，待收货 总数
	@Override
	public List getOrderCountByDealerId(Integer dealerId) {
    	return orderDao.getOrderCountByDealerId(dealerId);
	}
	
	//SaaS首页查询订单 待付款，待发货，待收货 总数
	@Override
	public List getOrderCount(Integer dealerId) {
    	return orderDao.getOrderCount(dealerId);
	}

	@Override
	public Map<String, Object> getOrderInfoFirst(Integer buyerId,Integer status) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> querymap = new HashMap<String, String>();
		if(buyerId == null){
			throw new Exception("请登录");
		}
		List<Map> orderList = orderDao.getOrderInfoFirst(buyerId,status);
		for (int i = 0,len = orderList.size(); i < len; i++) {
			Map order = orderList.get(i);
			if(order.get("freightAmount")!=null){
				if(!"".equals(order.get("freightAmount").toString())){
					DecimalFormat fmt = new DecimalFormat("0.00");
					order.put("freightAmount", fmt.format(Double.parseDouble(order.get("freightAmount").toString())));
				}
			};
			List<Map> goods = orderGoodsDao.getGoodsByOrderId(order.get("orderId").toString());
			order.put("orderGoods", goods);
			String adress = order.get("consignAddressInfo")!=null?order.get("consignAddressInfo").toString() : "";
			try {
				JSONObject jo = JSONObject.parseObject(adress);
				order.put("consignee",	 jo.get("consignee")!=null?jo.get("consignee").toString() : "");
				order.put("mobile",	 jo.get("mobile")!=null?jo.get("mobile").toString() : "");
			} catch (Exception e) {
				Map consignAddressPage  = addressMapper.findById(Integer.parseInt(order.get("consignAddressId").toString()));
				order.put("consignee",	 consignAddressPage.get("consignee")!=null?consignAddressPage.get("consignee"):"");
				order.put("mobile",	 consignAddressPage.get("mobile")!=null?consignAddressPage.get("mobile"):"");
			}
			try {
				LogisticsCompany  logistics= LogisticsCompanyI.fingById(Integer.parseInt(order.get("sellerLogisticsId").toString()));
				order.put("logisticsUrl", Delivery.search(logistics.getLogisticsCode(),order.get("logisticsCode").toString()));
			} catch (Exception e) {
				order.put("logisticsUrl", "");
			}
			orderList.set(i, order);
		}
		
		map.put("rows", orderList);
		return map;
	}

	/**
	 * 微信商城订单详情查看发票详情
	 * @param invId
	 * @return
	 */
	@Override
	public OrderInvoice getOrderInvoiceDetils(Integer invId) {
		
		return orderInvoiceMapper.selectByPrimaryKey(invId);
	}

	@Override
	public Map<String, Object> findOrderInterest(Long orderId) {
		// TODO Auto-generated method stub
		return orderDao.findOrderInterest(orderId);
	}

	@Override
	public void updateOrderInterest(Long orderId) {
		orderDao.updateOrderInterest(orderId);
		
	}

	@Override
	public void saveOrderInterest(Map<String, Object> interest, String orderId) {
		String value = interest.get("returnInterest").toString();
		orderDao.saveOrderInterest(orderId,value);
	}

	@Override
	public synchronized void addAgainToBuy(String orderId, String dealerId) throws Exception {
		List<Map<String, Object>> list =orderDao.findGoodsInfo(orderId);
		Set<String> set = new HashSet<String>();
		for (Map<String, Object> map : list) {
			String sellerGoodsId = map.get("sellerGoodsId").toString();
			String goodsType = map.get("goodsType").toString();
			String activityId = map.get("activityId")!=null?map.get("activityId").toString():"0";
			String num = map.get("num").toString();
			ShoppingCartPage shoppingCart = new ShoppingCartPage();
			shoppingCart.setBuyerId(Integer.parseInt(dealerId));
			shoppingCart.setSellerGoodsId(sellerGoodsId);
			shoppingCart.setNum(Integer.parseInt(num));
			shoppingCart.setActivityType(Integer.parseInt(goodsType));
			if("3".equals(goodsType)){
				shoppingCart.setPackageId(activityId);
				if(set.contains(activityId)){
					continue;
				}else{
					set.add(activityId);
				};
			}
			try {
				cartService.addGoods(shoppingCart);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}


	@Override
	public int tobuyJudge(String orderId, String dealerId) {
		List<Map<String, Object>> list =orderDao.findGoodsInfo(orderId);
		int code  = 0;
		Set<String> set =  new HashSet<>();
		for (Map<String, Object> map : list) {
			String sellerGoodsId = map.get("sellerGoodsId").toString();
			String goodsType = map.get("goodsType").toString();
			String activityId = map.get("activityId").toString();
			SellerGoods goods = sellerGoodsI.findGoodsPrice(sellerGoodsId);
			if("3".equals(goodsType)){
				Map<String, Object> info = goodsPackageDao.getPackageInfo(activityId);
				if(info==null || goods==null){
					code = code+1;
				}
			}else if(goods==null){
				code = code+1;
			}
		}
		if(code==0){
			return 0;
		}else if(code == list.size()){
			return 1;
		}else{
			return 2;
		}
		
	}

	@Override
	public Timestamp getOrderTime(Long orderId) {
		return orderDao.getOrderTime(orderId);
	}

	@Override
	public Map getOrderLogistics(String orderId) throws Exception {
		//查询订单物流信息
		List<OrderExpressInfo> list = orderDao.getOrderLogistics(orderId);
		Map map = new HashMap();
		List li =new ArrayList();
		for(OrderExpressInfo orderExpress : list){
			String str =null;
			JSONObject jo = null;
			if( "顺丰速运".equals(orderExpress.getLogisticsName()) ){//顺丰不加入快递100
				Map m = new HashMap();
				m.put("com", orderExpress.getLogisticsName());
				m.put("nu", orderExpress.getFexpNo());
				m.put("data", sfServiceI.orderRouteSearch(orderExpress.getFexpNo()));
				li.add(m);
			}else{
				Map m = new HashMap();
				m.put("id", kdId);
				m.put("com", orderExpress.getFexpcomname());
				m.put("nu", orderExpress.getFexpNo());
				HttpclientUtil h =new HttpclientUtil(logistics, Method.GET);
				//调用第三方接口
				str = h.getResponseBody(m);
				jo = JSONObject.parseObject(str);
				if("1".equals(jo.get("status"))){
					if(StringUtils.isNotBlank(jo.get("com").toString())){
						jo.put("com",orderExpress.getLogisticsName());
					}
					li.add(jo);
				}
			}
		}

		map.put("list", li);
		return map;
	}

	@Override
	public Map getOrderPackageLogistics(String orderId) throws Exception {
		//查询订单物流信息
		Map map = new HashMap();
		map.put("GdbOrderId", orderId);
		List<OrderExpressInfo> list = orderDao.getOrderPackageLogistics(orderId);
		if(list!=null && !list.isEmpty()){
			map.put("type","PL");//PL - 多包裹物流
		}else{
			map.put("type","OL");//OL - 订单物流
			list = orderDao.getOrderLogistics(orderId);
		}
		List li =new ArrayList();
		String comStr = null;
		String nuStr = null;
		for(OrderExpressInfo orderExpress : list){
			String str =null;
			JSONObject jo = null;
			comStr = orderExpress.getLogisticsName();
			nuStr = orderExpress.getFexpNo();

			if( "顺丰速运".equals(orderExpress.getLogisticsName()) ){//顺丰不加入快递100
				Map m = new HashMap();
				m.put("com", orderExpress.getLogisticsName());
				m.put("nu", orderExpress.getFexpNo());
				List<Map<String,Object>> dataMap = sfServiceI.orderRouteSearch(orderExpress.getFexpNo());
				if(dataMap!=null && !dataMap.isEmpty()){
					Map<String,Object> logisDetail = Maps.newHashMap();
					logisDetail.put("data",dataMap);
					m.put("logisticsData", logisDetail);
				}
				if("PL".equals(map.get("type").toString())){
					m.put("logisGoodsData",getSellerGoodsByOrderExpress(orderExpress.getId()));
				}
				li.add(m);
			}else{
				Map m = new HashMap();
				m.put("id", kdId);
				m.put("com", orderExpress.getFexpcomname());
				m.put("nu", orderExpress.getFexpNo());
				HttpclientUtil h =new HttpclientUtil(logistics, Method.GET);
				//调用第三方接口
				str = h.getResponseBody(m);
				jo = JSONObject.parseObject(str);
				if("1".equals(jo.get("status"))){
					if(StringUtils.isNotBlank(jo.get("com").toString())){
						jo.put("com",orderExpress.getLogisticsName());
					}
				}
				m.remove("id");
				m.put("com", orderExpress.getLogisticsName());
				m.put("logisticsData", jo);
				if("PL".equals(map.get("type").toString())){
					m.put("logisGoodsData",getSellerGoodsByOrderExpress(orderExpress.getId()));
				}
				li.add(m);
			}
		}
		map.put("list", li);
		return map;
	}


    @Override
	public Map getInvoiceLogistics(String orderId) throws Exception {
		//查询订单发票物流信息
		List<OrderReceiptInfo> list = orderDao.getInvoiceLogistics(orderId);
		Map map = new HashMap();
		List<Map<String,Object>> li =new ArrayList();
		for(OrderReceiptInfo orderReceipt : list){
			Map m = new HashMap();
			m.put("id", kdId);
			m.put("com", orderReceipt.getFexpcomname());
			m.put("nu", orderReceipt.getFexpNo());
			HttpclientUtil h =new HttpclientUtil(logistics, Method.GET);
			//调用第三方接口
			String str =null;
			JSONObject jo = null;
			str = h.getResponseBody(m);
			jo = JSONObject.parseObject(str);
			if("1".equals(jo.get("status"))){
				if(StringUtils.isNotBlank(jo.get("com").toString())){
					jo.put("com",orderReceipt.getLogisticsName());
				}
				jo.put("invoiceId", orderReceipt.getFjsNo());//发票号
				li.add(jo);
			}
			
		}
		map.put("list", li);
		return map;
	}

	/**
	 * 根据订单的子物流单号获得商品信息
	 * @param orderExpressId
	 * @return
	 */
	public List<Map<String,Object>> getSellerGoodsByOrderExpress(int orderExpressId){
		List<Map<String,Object>> goodsInfoInPackage = new ArrayList<Map<String,Object>>();
		try{
			List<OrderExpressGoods> oExpGoodsList = orderExpressGoodsMapper.findByOrderExpressId(String.valueOf(orderExpressId));
			if(oExpGoodsList!=null && !oExpGoodsList.isEmpty()){
				for(OrderExpressGoods oExp:oExpGoodsList){
					Map<String,Object> goodItemInfo = goodsMapper.getGoodsDetailInPackage(oExp.getSyncCode());
					goodItemInfo.put("outedNum",oExp.getNum());
					goodsInfoInPackage.add(goodItemInfo);
				}
			}
		}catch (Exception e){
			log.error("查询包裹商品出错：\n"+e.getMessage());
		}
		return goodsInfoInPackage;
	}


}