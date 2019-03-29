package bmatser.service.impl;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.ws.spi.http.HttpContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.igfay.jfig.JFig;
import org.igfay.jfig.JFigException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.sf.dto.WaybillDto;

//import bmatser.service.InterfaceLogI;
import bmatser.dao.BuyerConsignAddressMapper;
import bmatser.dao.DealerMapper;
import bmatser.dao.OrderInfoMapper;
import bmatser.dao.RegionMapper;
import bmatser.model.BuyerConsignAddress;
import bmatser.model.Dealer;
import bmatser.model.OrderInfo;
import bmatser.model.Region;
import bmatser.pageModel.ShunfengOperatePage;
import bmatser.service.ShunfengOperateServiceI;
import bmatser.util.DESCoder;
import bmatser.util.Status;
import bmatser.util.Util;


@SuppressWarnings("deprecation")
@Service("shunfengOperateService")
public  class ShunfengOperateServiceImpl implements ShunfengOperateServiceI{

	@Autowired
	private OrderInfoMapper orderInfoDao;
	@Autowired
	private DealerMapper dealerDao;
	@Autowired
	private BuyerConsignAddressMapper buyerAddressDao;
	@Autowired
	private RegionMapper regionDao;
//	@Autowired
//	private InterfaceLogI interfaceLogI;

	private String xmldata;
	private String sfurl;
	private String message;

	/**
	 *  顺丰下单
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String orderService(ShunfengOperatePage shunfengOperatePage) throws Exception {
		if(shunfengOperatePage.getNum()==null){
			shunfengOperatePage.setNum(1);
		}
		String code = "-1";
		String mailno="";
		//调用下单接口
		try {
			HttpResponse response = getHttpResponse(shunfengOperatePage);

			if (response.getStatusLine().getStatusCode() == 200){
				String str1=EntityUtils.toString(response.getEntity());
				Document dom=DocumentHelper.parseText(str1);
				Element root=dom.getRootElement();
//				System.out.println("----"+root.element("Head").getText()+"-----"+root.elements("Body"));
				if(!root.element("Head").getText().equals("OK")){//如果下单失败就抛出异常
					System.out.println(str1+"============="+shunfengOperatePage.getOrderId());
					throw new Exception();
				}
				message = str1;
				List  skills =root.elements("Body");

				for (Iterator<?> it = skills.iterator(); it.hasNext();) {
					Element e = (Element) it.next();
					Attribute a = e.element("OrderResponse").attribute("mailno");
					mailno=a.getText();
					OrderInfo oi = orderInfoDao.findById(Long.parseLong(shunfengOperatePage.getOrderId()));
					String[] mailnos = mailno.split(",");
					oi.setSellerLogisticsId(1);
					if(mailnos.length==1){
						oi.setLogisticsCode(mailnos[0]);
					}else if(mailnos.length>0){
						oi.setLogisticsCode(mailnos[0]);
						String mutlis="";
						for(int i=1;i<mailnos.length;i++){
							mutlis=mutlis+mailnos[i]+",";
						}
						oi.setLogisticsMultiCode(mutlis.substring(0,mutlis.length()-1));
					}
					//更新orderinfo 的运单号码和子单号码

					oi.setShippingStatus(Status.SHIPPED.getValue());//更新发货状态和发货时间
					oi.setOrderStatus(Status.TORECEIVETHEGOODS.getValue());
					Timestamp time = new Timestamp(System.currentTimeMillis());
					oi.setShippingTime(time);

					orderInfoDao.update(oi);
					code = "0";
				}
			} else {
				EntityUtils.consume(response.getEntity());
				throw new Exception("response status error: " + response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			message = e.toString();
			throw e;
		}finally{
//			//接口调用时保存日志到mongodb的InterfaceLog表
//			interfaceLogI.saveLogToMongoDB("顺风下单",
//					sfurl,
//					"orderService",xmldata,
//					"gdbmro","shunfeng",code,message);
		}

		//判断数量是否大于1，如果是，就调用子母单申请接口，如果不是，就不调用
//		if(shunfengOperatePage.getNum()>1){
//			OrderZDService(shunfengOperatePage);
//		}
		return mailno;
	}

	private HttpResponse getHttpResponse(ShunfengOperatePage shunfengOperatePage) throws Exception{
		String url = JFig.getInstance().getValue("shunfeng", "URL","http://bsp-oisp.sf-express.com/bsp-oisp/sfexpressService");
		sfurl = url;
		String checkword = JFig.getInstance().getValue("shunfeng", "CHECKWORD","95lAp6M4wNDXSnE829KpYvFuda3qNPnQ");
		int port = 80;
		Properties dbProps = new Properties();
		InputStream in = ShunfengOperateServiceImpl.class.getResourceAsStream("/resource_zh_CN.properties");
		dbProps.load(in);
		String xml=dbProps.getProperty("ORDER_SERVICE");
		try{
			OrderInfo oi = getOrderInfo(shunfengOperatePage.getOrderId());
			Map buyer = dealerDao.findById(oi.getBuyerId().toString());
			Map seller = dealerDao.findById(oi.getSellerId().toString());
			Object[] param = this.getParam(oi, buyer, seller,shunfengOperatePage.getNum());
			xml=MessageFormat.format(xml, param);
			xmldata = xml;
			System.out.println("xml=================="+xml);
		}catch(Exception e){
			throw new  Exception();
		}
		String verifyCode=Util.md5EncryptAndBase64(xml+checkword);//效验码 是 xml报文和checkword的拼集

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();//参数集
		nvps.add(new BasicNameValuePair("xml", xml));
		nvps.add(new BasicNameValuePair("verifyCode", verifyCode));

		HttpClient httpclient=getHttpClient(port);
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		HttpResponse response = httpclient.execute(httpPost);
		return response;
	}

    @Override
    public List<Map<String, Object>> orderRouteSearch(String sfOrderId) throws Exception {
		String url = "http://bsp-oisp.sf-express.com/bsp-oisp/sfexpressService";//开发地址
		int port = 80;
		String checkword = "95lAp6M4wNDXSnE829KpYvFuda3qNPnQ";
		Properties dbProps = new Properties();
		InputStream in = ShunfengOperateServiceImpl.class.getResourceAsStream("/resource_zh_CN.properties");
		dbProps.load(in);
		String xml=dbProps.getProperty("ORDER_ROUTE_SERVICE");
		Object[] param = new String[1];
		param[0] = sfOrderId;
		xml=MessageFormat.format(xml, param);
		String verifyCode=Util.md5EncryptAndBase64(xml+checkword);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("xml", xml));
		nvps.add(new BasicNameValuePair("verifyCode", verifyCode));

		HttpClient httpclient=getHttpClient(port);
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		HttpResponse response = httpclient.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200){
			String str1=EntityUtils.toString(response.getEntity());

			Document dom=DocumentHelper.parseText(str1);
			Element root=dom.getRootElement();
			if(!root.element("Head").getText().equals("OK")){
				throw new Exception();
			}else{
				List<Map<String,Object>> mapLi = Lists.newArrayList();
				Element bodyElement = root.element("Body").element("RouteResponse");
				if(bodyElement==null){
			/*		Map<String,Object> map = Maps.newHashMap();
					map.put("context","不是正确运单号或未查到物流信息!");
					mapLi.add(map);*/
					return Collections.emptyList();
				}
				for(Object route : root.element("Body").element("RouteResponse").elements("Route")){
					Element item = (Element) route;
					Map<String,Object> map = Maps.newHashMap();
					map.put("location",item.attributeValue("accept_address"));
					map.put("context",item.attributeValue("remark"));
					map.put("time",item.attributeValue("accept_time"));
					mapLi.add(map);
				}
				return mapLi;
			}
		} else {
			EntityUtils.consume(response.getEntity());
			throw new RuntimeException("response status error: " + response.getStatusLine().getStatusCode());
		}
    }
	private static HttpClient getHttpClient(int port){
		PoolingClientConnectionManager pcm = new PoolingClientConnectionManager();
		SSLContext ctx=null;
		try{
			ctx = SSLContext.getInstance("TLS");
			X509TrustManager x509=new X509TrustManager(){
				public void checkClientTrusted(X509Certificate[] xcs, String string)
					throws CertificateException {
				}
				public void checkServerTrusted(X509Certificate[] xcs, String string)
					throws CertificateException {
				}
				public X509Certificate[] getAcceptedIssuers(){
					return null;
				}
			};
			ctx.init(null, new TrustManager[]{x509}, null);
		}catch(Exception e){
			e.printStackTrace();
		}

		SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		Scheme sch = new Scheme("https", port, ssf);
		pcm.getSchemeRegistry().register(sch);
		return new DefaultHttpClient(pcm);
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	private void OrderZDService(ShunfengOperatePage shunfengOperatePage) throws IOException, DocumentException{
		String url = "http://bsp-oisp.test.sf-express.com:6080/bsp-oisp/sfexpressService";//外网地址
		int port = 6080;
		String checkword = "j8DzkIFgmlomPt0aLuwU";
		Properties dbProps = new Properties();
		InputStream in = ShunfengOperateServiceImpl.class.getResourceAsStream("/resource_zh_CN.properties");
		dbProps.load(in);
		String xml=dbProps.getProperty("ORDER_CHILD_SERVICE");
		Object[] param = new String[2];
		param[0] =shunfengOperatePage.getOrderId();
		param[1] =shunfengOperatePage.getNum();
		xml=MessageFormat.format(xml, param);
		String verifyCode=Util.md5EncryptAndBase64(xml+checkword);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("xml", xml));
		nvps.add(new BasicNameValuePair("verifyCode", verifyCode));

		HttpClient httpclient=getHttpClient(port);
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		HttpResponse response = httpclient.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200){
			String str1=EntityUtils.toString(response.getEntity());
			Document dom=DocumentHelper.parseText(str1);
			Element root=dom.getRootElement();
			List  skills =root.elements("Body");
			for (Iterator<?> it = skills.iterator(); it.hasNext();) {
				Element e = (Element) it.next();
				Attribute mailno_zd = e.element("OrderZDResponse").element("OrderZDResponse").attribute("mailno_zd");
				//更新orderinfo的子母单字段
				OrderInfo oi = getOrderInfo(shunfengOperatePage.getOrderId());
				String exsitMailMutli=oi.getLogisticsMultiCode();
				oi.setLogisticsMultiCode(exsitMailMutli+","+mailno_zd.getText());
				orderInfoDao.update(oi);
			}

		} else {
			EntityUtils.consume(response.getEntity());
			throw new RuntimeException("response status error: " + response.getStatusLine().getStatusCode());
		}

	}

	private OrderInfo getOrderInfo(String orderId){
		return orderInfoDao.findById(Long.parseLong(orderId));
	}

	/**
	 * 参数拼装
	 * **/
	private Object[] getParam(OrderInfo oi,Map buyer,Map seller,Integer num) throws Exception{
		Object[] param = new String[20];
		Map bca= new HashMap<>();;
		try{
			if(StringUtils.isNoneBlank(oi.getConsignAddressInfoSecret())){
				try {
					JSONObject json = JSONObject.parseObject(oi.getConsignAddressInfoSecret());
					bca.put("provinceId", json.get("provinceId"));
					bca.put("cityId", json.get("cityId"));
					bca.put("areaId", json.get("areaId"));
					bca.put("address", json.get("address"));
					bca.put("consignee", json.get("consignee"));
					bca.put("mobileSecret", json.get("mobile"));
				} catch (Exception e) {
					bca=buyerAddressDao.findById(oi.getConsignAddressId());
				}
			}else{
				bca=buyerAddressDao.findById(oi.getConsignAddressId());
			}
			DESCoder.instanceMobile();
			String mobile = bca.get("mobileSecret")!=null?DESCoder.decoder(bca.get("mobileSecret").toString()):"";
			System.out.println("+++++++++++++++++++++="+bca.get("mobileSecret"));
			param[0]=oi.getId().toString();
			param[1]="江苏工电宝信息科技有限公司";
			param[2]=seller.get("linkman").toString();
			param[3]=DESCoder.decoder(seller.get("telephone_secret").toString());
			param[4]=DESCoder.decoder(seller.get("link_tel_secret").toString());
			Region sellerProvince=regionDao.selectByPrimaryKey(Integer.parseInt(seller.get("province_id").toString()));
			param[5] = sellerProvince.getRegionName();
			Region sellerCity=regionDao.selectByPrimaryKey(Integer.parseInt(seller.get("city_id").toString()));
			param[6] = sellerCity.getRegionName();
			Region sellerArea=regionDao.selectByPrimaryKey(Integer.parseInt(seller.get("area_id").toString()));
			param[7]=sellerArea.getRegionName();
			param[8]=seller.get("address").toString();
			param[9]=buyer.get("dealerName").toString();
			param[10]=bca.get("consignee").toString();
			param[11]=mobile;
			param[12]=mobile;
			Region r1=regionDao.selectByPrimaryKey(Integer.parseInt(bca.get("provinceId").toString()));
			param[13]=r1==null?"":r1.getRegionName();
			Region r2=regionDao.selectByPrimaryKey(Integer.parseInt(bca.get("cityId").toString()));
			param[14]=r2==null?"":r2.getRegionName();
			Region r3=regionDao.selectByPrimaryKey(Integer.parseInt(bca.get("areaId").toString()));
			param[15]=r3==null?"":r3.getRegionName();
			param[16]=bca.get("address").toString();
			param[17]=seller.get("sf_cust_id").toString();
			param[18]=num.toString();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
			param[19] =sdf.format(new Date());
		}catch(Exception e){
			throw e;
		}
		return param;
	}

	/**
	 * 顺丰子单申请
	 * **/
	@SuppressWarnings("rawtypes")
	@Override
	public String orderZDService(ShunfengOperatePage shunfengOperatePage)
			throws Exception {
		String url = "http://bsp-oisp.sf-express.com/bsp-oisp/sfexpressService";//开发地址
		int port = 80;
		String checkword = "n7xKKL4mNjRYnAUBQAe9aQw8SNObyrdb";
		Properties dbProps = new Properties();
		InputStream in = ShunfengOperateServiceImpl.class.getResourceAsStream("/resource_zh_CN.properties");
		dbProps.load(in);
		String xml=dbProps.getProperty("ORDER_CHILD_SERVICE");
		Object[] param = new String[2];
		param[0] =shunfengOperatePage.getOrderId();
		param[1] =shunfengOperatePage.getNum().toString();
		xml=MessageFormat.format(xml, param);
		String verifyCode=Util.md5EncryptAndBase64(xml+checkword);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("xml", xml));
		nvps.add(new BasicNameValuePair("verifyCode", verifyCode));

		HttpClient httpclient=getHttpClient(port);
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		HttpResponse response = httpclient.execute(httpPost);
		String mailno_Zd="";
		if (response.getStatusLine().getStatusCode() == 200){
			String str1=EntityUtils.toString(response.getEntity());

			Document dom=DocumentHelper.parseText(str1);
			Element root=dom.getRootElement();
			if(!root.element("Head").getText().equals("OK")){
				throw new Exception();
			}
			List  skills =root.elements("Body");
			for (Iterator<?> it = skills.iterator(); it.hasNext();) {
				Element e = (Element) it.next();
				/**
				 * dom4j解析xml字符
				 * **/
				Attribute mailno_zd = e.element("OrderZDResponse").element("OrderZDResponse").attribute("mailno_zd");
				//更新orderinfo的子母单字段
				mailno_Zd=mailno_zd.getText();
				OrderInfo oi = getOrderInfo(shunfengOperatePage.getOrderId());
				String exsitMailMutli=oi.getLogisticsMultiCode();
				oi.setLogisticsMultiCode(exsitMailMutli+","+mailno_zd.getText());
				orderInfoDao.update(oi);
			}

		} else {
			EntityUtils.consume(response.getEntity());
			throw new RuntimeException("response status error: " + response.getStatusLine().getStatusCode());
		}
		return mailno_Zd;
	}

	/**
	 * 顺丰下单取消
	 * **/
	@Override
	public void orderConfirmService(ShunfengOperatePage shunfengOperatePage)
			throws Exception {
		String url = "http://bsp-oisp.sf-express.com/bsp-oisp/sfexpressService";//开发地址
		int port = 80;
		String checkword = "95lAp6M4wNDXSnE829KpYvFuda3qNPnQ";
		Properties dbProps = new Properties();
		InputStream in = ShunfengOperateServiceImpl.class.getResourceAsStream("/resource_zh_CN.properties");
		dbProps.load(in);
		String xml=dbProps.getProperty("ORDER_CONFIRM_SERVICE");
		Object[] param = new String[1];
		param[0] =shunfengOperatePage.getOrderId();
		xml=MessageFormat.format(xml, param);
		String verifyCode=Util.md5EncryptAndBase64(xml+checkword);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("xml", xml));
		nvps.add(new BasicNameValuePair("verifyCode", verifyCode));

		HttpClient httpclient=getHttpClient(port);
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		HttpResponse response = httpclient.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200){
			String str1=EntityUtils.toString(response.getEntity());

			Document dom=DocumentHelper.parseText(str1);
			Element root=dom.getRootElement();
			if(!root.element("Head").getText().equals("OK")){
				throw new Exception();
			}
			/**
			 * 取消顺丰下单后，更新订单表的订单状态为待发货
			 * **/
			OrderInfo oi = orderInfoDao.findById(Long.parseLong(shunfengOperatePage.getOrderId()));
			oi.setOrderStatus(Status.TOBESHIPPED.getValue());
		} else {
			EntityUtils.consume(response.getEntity());
			throw new RuntimeException("response status error: " + response.getStatusLine().getStatusCode());
		}
	}

	/**
	 * 顺丰下单结果查询
	 * **/
	@Override
	public void orderSearchService(ShunfengOperatePage shunfengOperatePage)
			throws Exception {
		String url = "http://bsp-oisp.sf-express.com/bsp-oisp/sfexpressService";//开发地址
		int port = 80;
		String checkword = "n7xKKL4mNjRYnAUBQAe9aQw8SNObyrdb";
		Properties dbProps = new Properties();
		InputStream in = ShunfengOperateServiceImpl.class.getResourceAsStream("/resource_zh_CN.properties");
		dbProps.load(in);
		String xml=dbProps.getProperty("ORDER_SEARCH_SERVICE");
		Object[] param = new String[1];
		param[0] =shunfengOperatePage.getOrderId();
		xml=MessageFormat.format(xml, param);
		String verifyCode=Util.md5EncryptAndBase64(xml+checkword);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("xml", xml));
		nvps.add(new BasicNameValuePair("verifyCode", verifyCode));

		HttpClient httpclient=getHttpClient(port);
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		HttpResponse response = httpclient.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200){
			String str1=EntityUtils.toString(response.getEntity());

			Document dom=DocumentHelper.parseText(str1);
			Element root=dom.getRootElement();
			if(!root.element("Head").getText().equals("OK")){
				throw new Exception();
			}

		} else {
			EntityUtils.consume(response.getEntity());
			throw new RuntimeException("response status error: " + response.getStatusLine().getStatusCode());
		}
	}
	
	
	public static void main(String args[]){
		ShunfengOperateServiceImpl service = new ShunfengOperateServiceImpl();
		ShunfengOperatePage shunfengOperatePage = new ShunfengOperatePage();
		shunfengOperatePage.setOrderId("901467855927466");
		try {
			service.orderSearchService(shunfengOperatePage);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}

	/**
	 * 订单取消
	 * **/
	@Override
	public void cancelOrder(ShunfengOperatePage shunfengOperatePage) {
		OrderInfo oi = orderInfoDao.findById(Long.parseLong(shunfengOperatePage.getOrderId()));
		oi.setSellerId(939817);
		orderInfoDao.update(oi);
	}

	@Override
	public String orderSearchResult(ShunfengOperatePage shunfengOperatePage) {
		OrderInfo oi = orderInfoDao.findById(Long.parseLong(shunfengOperatePage.getOrderId()));
		if(oi.getSellerLogisticsId()!=null&&StringUtils.isNotBlank(oi.getLogisticsCode())){
			return "下单成功";
		}else{
			return "下单失败";
		}
	}

	@Override
	public String printWaybill(String orderId) throws Exception {
		// 根据订单id获取相关参数
		// 根据业务需求确定请求地址
		String reqURL = JFig.getInstance().getValue("shunfeng", "PRINTURL",
				"http://localhost:4040/sf/waybill/print?type=V2.0.FM_poster_100mm150mm&output=image");
		// 电子面单顶部是否需要logo
		boolean topLogo = true;// true 需要logo false 不需要logo
		if (reqURL.contains("V2.0") && topLogo) {
			reqURL = reqURL.replace("V2.0", "V2.1");
		}
		System.out.println(reqURL);
		/** 注意 需要使用对应业务场景的url **/
		URL myURL = new URL(reqURL);

		HttpURLConnection httpConn = (HttpURLConnection) myURL.openConnection();
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		httpConn.setUseCaches(false);
		httpConn.setRequestMethod("POST");
		httpConn.setRequestProperty("Content-Type", "text/plain;charset=utf-8");
		httpConn.setConnectTimeout(50000);
		httpConn.setReadTimeout(2 * 50000);

		List<WaybillDto> waybillDtoList = new ArrayList<WaybillDto>();
		WaybillDto dto = new WaybillDto();

		

		// dto.setMailNo("755123456788,001000000002");//子母单方式

		OrderInfo oi = getOrderInfo(orderId);
		Map buyer = dealerDao.findById(oi.getBuyerId().toString());
		Map seller = dealerDao.findById(oi.getSellerId().toString());

		String strImg = convertParam( oi, buyer, seller);
		
		return strImg;
	}
	
	@SuppressWarnings("unchecked")
	private String convertParam(OrderInfo oi, Map<String, Object> buyer, Map<String, Object> seller) throws Exception {

		String reqURL = JFig.getInstance().getValue("shunfeng", "PRINTURL",
				"http://localhost:4040/sf/waybill/print?type=V2.0.FM_poster_100mm150mm&output=image");

		//电子面单顶部是否需要logo 
				boolean topLogo=true;//true 需要logo  false 不需要logo
				if(reqURL.contains("V2.0")&&topLogo){
					reqURL=reqURL.replace("V2.0", "V2.1");
				}
//				
//				if(reqURL.contains("V3.0")&&topLogo){
//					reqURL=reqURL.replace("V3.0", "V3.1");
//				}
			    
				System.out.println(reqURL);
				
		        /**注意 需要使用对应业务场景的url  **/
				URL myURL = new URL(reqURL);
			     
				 //其中127.0.0.1:4040为打印服务部署的地址（端口如未指定，默认为4040），
				 //type为模板类型（支持两联、三联，尺寸为100mm*150mm和100mm*210mm，type为poster_100mm150mm和poster_100mm210mm）
				 //A5 poster_100mm150mm   A5 poster_100mm210mm
				 //output为输出类型,值为print或image，如不传，
				 //默认为print（print 表示直接打印，image表示获取图片的BASE64编码字符串）
				 //V2.0/V3.0模板顶部是带logo的  V2.1/V3.1顶部不带logo
				
//				HttpURLConnection httpConn = (HttpURLConnection) myURL.openConnection();
//				httpConn.setDoOutput(true);
//				httpConn.setDoInput(true);
//				httpConn.setUseCaches(false);
//				httpConn.setRequestMethod("POST");
//				//httpConn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
//				httpConn.setRequestProperty("Content-Type", "text/plain;charset=utf-8");
//				httpConn.setConnectTimeout(5000);
//				httpConn.setReadTimeout(2 * 5000);
				
				
				RestTemplate restTemplate = new RestTemplate();
				HttpEntity<String> entity =null;
				MediaType type = MediaType.parseMediaType("text/plain;charset=utf-8");
				HttpHeaders headers = new HttpHeaders();
				
				headers.setContentType(type);
				
				List<WaybillDto> waybillDtoList = new ArrayList<WaybillDto>();
				WaybillDto dto = new WaybillDto();
				
				
				
			    //这个必填 
				dto.setAppId("GDBXXKJ");// 对应clientCode
				String checkword = JFig.getInstance().getValue("shunfeng", "CHECKWORD", "95lAp6M4wNDXSnE829KpYvFuda3qNPnQ");
				dto.setAppKey(checkword);// 对应checkWord
				dto.setMailNo(oi.getLogisticsCode());
				Map<String,Object> bca= Maps.newHashMap();
					if(StringUtils.isNoneBlank(oi.getConsignAddressInfoSecret())){
						try {
							JSONObject json = JSONObject.parseObject(oi.getConsignAddressInfoSecret());
							bca.put("provinceId", json.get("provinceId"));
							bca.put("cityId", json.get("cityId"));
							bca.put("areaId", json.get("areaId"));
							bca.put("address", json.get("address"));
							bca.put("consignee", json.get("consignee"));
							bca.put("mobileSecret", json.get("mobile"));
						} catch (Exception e) {
							bca=buyerAddressDao.findById(oi.getConsignAddressId());
						}
					}else{
						bca=buyerAddressDao.findById(oi.getConsignAddressId());
					}
					DESCoder.instanceMobile();
					String mobile = bca.get("mobileSecret")!=null?DESCoder.decoder(bca.get("mobileSecret").toString()):"";
					System.out.println("+++++++++++++++++++++="+bca.get("mobileSecret"));
					Region sellerProvince=regionDao.selectByPrimaryKey(Integer.parseInt(seller.get("province_id").toString()));
					Region sellerCity=regionDao.selectByPrimaryKey(Integer.parseInt(seller.get("city_id").toString()));
					Region sellerArea=regionDao.selectByPrimaryKey(Integer.parseInt(seller.get("area_id").toString()));
					Region r1=regionDao.selectByPrimaryKey(Integer.parseInt(bca.get("provinceId").toString()));
					Region r2=regionDao.selectByPrimaryKey(Integer.parseInt(bca.get("cityId").toString()));
					Region r3=regionDao.selectByPrimaryKey(Integer.parseInt(bca.get("areaId").toString()));
				
				//收件人信息  
				dto.setConsignerProvince(r1.getRegionName());
				dto.setConsignerCity(r2.getRegionName());
				dto.setConsignerCounty(r3.getRegionName());
				dto.setConsignerAddress(bca.get("address").toString()); //详细地址建议最多30个字  字段过长影响打印效果
//				dto.setConsignerCompany(buyer.get("dealerName").toString());
				dto.setConsignerMobile(mobile);
				dto.setConsignerName(bca.get("consignee").toString());
//				dto.setConsignerShipperCode("518052");
//				dto.setConsignerTel("0755-33123456");
				
				
				//寄件人信息
				dto.setDeliverProvince(sellerProvince.getRegionName());
				dto.setDeliverCity(sellerCity.getRegionName());
				dto.setDeliverCounty(sellerArea.getRegionName());
				dto.setDeliverCompany("江苏工电宝信息科技有限公司");
				dto.setDeliverAddress(seller.get("address").toString());//详细地址建议最多30个字  字段过长影响打印效果
				dto.setDeliverName(seller.get("linkman").toString());
				dto.setDeliverMobile("0512-65099638");
				dto.setDeliverShipperCode("215000");
//				dto.setDeliverTel("0512-65099638");
				
				
//				dto.setDestCode("755");//目的地代码 参考顺丰地区编号
//				dto.setZipCode("571");//原寄地代码 参考顺丰地区编号
				
				//签回单号  签单返回服务 会打印两份快单 其中第二份作为返寄的单
				//如客户使用签单返还业务则需打印“POD”字段，用以提醒收派员此件为签单返还快件。	
				// dto.setReturnTrackingNo("755123457778");
				
				//陆运E标示
				//业务类型为“电商特惠、顺丰特惠、电商专配、陆运件”则必须打印E标识，用以提示中转场分拣为陆运	
				dto.setElectric("E");
				
				
				//快递类型	
				//1 ：标准快递   2.顺丰特惠   3： 电商特惠   5：顺丰次晨  6：顺丰即日  7.电商速配   15：生鲜速配		
				dto.setExpressType(1);
						
				//COD代收货款金额,只需填金额, 单位元- 此项和月结卡号绑定的增值服务相关
//				dto.setCodValue("999.9");
				
//				dto.setInsureValue("501");//声明货物价值的保价金额,只需填金额,单位元
				dto.setMonthAccount(seller.get("sf_cust_id").toString());//月结卡号  
				dto.setPayMethod(3);//
				
				
				/**丰密运单相关-如非使用丰密运单模板 不需要设置以下值**/
//				dto.setDestRouteLabel("755WE-571A3");
//				dto.setPrintIcon("1111");
//				dto.setProCode("T6");
//				dto.setAbFlag("A");
//				dto.setXbFlag("XB");
//				dto.setCodingMapping("F33");
//				dto.setCodingMappingOut("1A");
//				dto.setDestTeamCode("012345678");
//				dto.setSourceTransferCode("021WTF");
//				//对应下订单设置路由标签返回字段twoDimensionCode 该参数是丰密面单的二维码图
//				dto.setQRCode("MMM={'k1':'755WE','k2':'755BF','k3':'','k4':'T6','k5':'755123456789','k6':'A'}");
//				客户个性化Logo 必须是个可以访问的图片本地路径地址或者互联网图片地址
//			    dto.setCustLogo("D:\\qiao.jpg");
			    
				
				//加密项
				dto.setEncryptCustName(true);//加密寄件人及收件人名称
				dto.setEncryptMobile(true);//加密寄件人及收件人联系手机	
				
				
				
//				CargoInfoDto cargo = new CargoInfoDto();
//				cargo.setCargo("苹果7S");
//				cargo.setCargoCount(1);
//				cargo.setCargoUnit("件");
//				cargo.setSku("00015645");
//				cargo.setRemark("手机贵重物品 小心轻放");
//				
//				CargoInfoDto cargo2 = new CargoInfoDto();
//				cargo2.setCargo("苹果macbook pro");
//				cargo2.setCargoCount(1);
//				cargo2.setCargoUnit("件");
//				cargo2.setSku("00015646");
//				cargo2.setRemark("笔记本贵重物品 小心轻放");
//				
//				List<CargoInfoDto> cargoInfoList = new ArrayList<CargoInfoDto>();
//				cargoInfoList.add(cargo2);
//				cargoInfoList.add(cargo);
				
//				dto.setCargoInfoDtoList(cargoInfoList);
				waybillDtoList.add(dto);
				
				ObjectMapper objectMapper = new ObjectMapper();
				StringWriter stringWriter = new StringWriter();
				objectMapper.writeValue(stringWriter,waybillDtoList);
				
				
				System.out.println("请求参数： "+stringWriter.toString());
				
				entity = new HttpEntity<String>(stringWriter.toString(),headers);
		        String str = restTemplate.postForObject(reqURL,entity,String.class);
		        String imgStr = JSONObject.parseObject(str).getJSONArray("result").get(0).toString();
		        System.out.println(imgStr);
				
//				httpConn.getOutputStream().flush();
//				httpConn.getOutputStream().close();
//				InputStream in = httpConn.getInputStream();
//						
//				BufferedReader in2=new BufferedReader(new InputStreamReader(in));
//
//				String y="";
//				
//			    
//				String strImg="";
//				while((y=in2.readLine())!=null){
//					 
//						strImg=y.substring(y.indexOf("[")+1,y.length()-"]".length()-1);
//						if(strImg.startsWith("\"")){
//							strImg=strImg.substring(1,strImg.length());
//						}
//						if(strImg.endsWith("\"")){
//							strImg=strImg.substring(0,strImg.length()-1);
//						}
//						  
//					   }
//			    
//			        //将换行全部替换成空    
//				strImg=strImg.replace("\\n", ""); 	    
//				System.out.println(strImg); 
		return imgStr;
	}
	
	
	
}
