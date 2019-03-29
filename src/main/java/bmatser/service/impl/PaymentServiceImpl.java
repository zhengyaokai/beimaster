package bmatser.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.formula.functions.T;
import org.igfay.jfig.JFig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import bmatser.dao.DealerBalanceRecordMapper;
import bmatser.dao.DealerMapper;
import bmatser.dao.OrderInfoMapper;
import bmatser.dao.PaymentBillMapper;
import bmatser.exception.GdbmroException;
import bmatser.model.LoginInfoUtil;
import bmatser.model.OrderInfo;
import bmatser.model.PayInfo;
import bmatser.pageModel.PageMode;
import bmatser.pageModel.PaymentBillPage;
import bmatser.plugin.PaymentPlugin.NotifyMethod;
import bmatser.service.AddPaymentBillServiceI;
import bmatser.service.OrderInfoI;
import bmatser.service.PaymentServiceI;
import bmatser.util.Encrypt;
import bmatser.util.HttpclientUtil;
import bmatser.util.HttpclientUtil.ContentType;
import bmatser.util.HttpclientUtil.Method;
import bmatser.util.IpUtil;
import bmatser.util.LoginInfo;
import bmatser.util.wftUtil.MD5;
import bmatser.util.wftUtil.SignUtils;
import bmatser.util.wftUtil.XmlUtils;

@Service("paymentService")
public class PaymentServiceImpl implements PaymentServiceI {
	
	
	@Autowired
	private DealerMapper<T> dealerMapper;
	@Autowired
	private OrderInfoMapper orderInfoDao;
	@Autowired
	private DealerBalanceRecordMapper balanceRecordMapper;
	@Autowired
	private PaymentBillMapper paymentBillDao;
	@Autowired
	private OrderInfoI orderService; 
	@Autowired
	private AddPaymentBillServiceI addPaymentBillService;
	
	private static String appID = JFig.getInstance().getValue("beeCloud", "appID","242f6237-6264-40c9-9c06-d1837d7c54fe");
	private static String appSecret = JFig.getInstance().getValue("beeCloud", "appSecret","ad15121b-d1d9-43b6-adaa-1d72aa0a9d03");

	//威富通支付商户号mch_id
	private static String mchId = JFig.getInstance().getValue("wftPay", "mch_id","7551000001");
	//威富通支付 key
	private static String key = JFig.getInstance().getValue("wftPay", "key","9d101c97133837e13dde2d32a5054abb");

	
	
	@Override
	public synchronized void updateIsBalance(boolean isbalance, String orderId) {
		//恢复余额
		dealerMapper.returnDealerBalance(orderId);
		//删除已生成记录
		balanceRecordMapper.delRecordByOrderId(orderId);
		paymentBillDao.delBalanceByOrderId(orderId);
		if(isbalance){//判断是否使用余额
			//更新订单使用余额
			orderInfoDao.updateBalanceByType(1,orderId);
			//更新用户余额
			dealerMapper.updateBalanceByOrderId(orderId);
			//新增余额使用记录
			balanceRecordMapper.insertRecordByOrderId(orderId);
		}else{
			//不使用余额
			orderInfoDao.updateBalanceByType(0,orderId);
		}
		
	}

	@Override
	public void saveOrderByBalance(String orderId) {
		orderInfoDao.saveOrderByBalance(orderId);
		paymentBillDao.updatePaymentBillIByBalance(orderId);
		
	}

	
	/**
	 * balance payment
	 * 余额支付
	 */
	@Override
	public int toPayByOffline(PageMode model, LoginInfo login) throws Exception {
		if(model.contains("orderId")){
			String orderId = model.getStringValue("orderId");
			if(model.contains("isBalance")){
				if("true".equals(model.getStringValue("isBalance"))){
					this.updateIsBalance(true,orderId);
				}else{
					this.updateIsBalance(false,orderId);
				}
			}else{
				this.updateIsBalance(false,orderId);
			}
			OrderInfo orderInfo = orderService.getOrderById(Long.valueOf(orderId));
			if(!"1".equals(orderInfo.getOrderStatus())){
				throw new GdbmroException(10001, "订单已支付");
			}
			if(orderInfo.getPayAmount().compareTo(orderInfo.getBalanceDeduction())==0){
				PaymentBillPage paymentBillPage = getPaymentBill(orderInfo);
				Map map = addPaymentBillService.findPayOrderById(Long.valueOf(orderId));
				if(null != map && !map.isEmpty() && !"0".equals(map.get("num").toString())){
					paymentBillPage.setId(map.get("payment_bill_id").toString());
					addPaymentBillService.updatePaymentBill(paymentBillPage);
				}else{
					addPaymentBillService.savePaymentBill(paymentBillPage);
				}
				this.saveOrderByBalance(orderId);
				return 0;
			}else{
				return 1;
			}
		}else{
			throw new Exception("参数错误");
		}
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
		return paymentBillPage;
	}

	@Override
	public String toBeecloudPay(String orderId, BigDecimal amount, String paymentPluginId,Map<String, Object> param) throws Exception {
		orderId = orderId + getRandom();
		String channel= null;
		switch (paymentPluginId) {
		case "unionpayPlugin":
			channel = "BC_EXPRESS";//BC版快捷支付
			break;
		case "alipayDirectPlugin":
			channel = "BC_ALI_QRCODE";//BC版支付宝线下扫码支付
			break;
		case "weixinPlugin":
			channel = "BC_NATIVE";//微信二维码支付
			break;
		case "bcWXPlugin":
			channel = "BC_WX_JSAPI";//BC版微信公众号支付
			break;
		}
		HttpclientUtil httpClient = new HttpclientUtil("https://api.beecloud.cn/2/rest/bill", Method.POST);
		Map<String , Object> data = new HashMap<>();
		data.put("app_id", appID);
		long date = System.currentTimeMillis();
		data.put("timestamp", date);
		//Encrypt.md5("242f6237-6264-40c9-9c06-d1837d7c54fe"+date+"ad15121b-d1d9-43b6-adaa-1d72aa0a9d03")
		data.put("app_sign", Encrypt.md5(appID+date+appSecret));
		data.put("channel", channel);
		data.put("total_fee",amount.intValue());
		data.put("bill_no", orderId);
		data.put("title", "订单支付");
		data.put("return_url", getNotifyUrl(orderId, NotifyMethod.sync));
		data.put("notify_url", getNotifyUrl(orderId, NotifyMethod.async));
		if("bcWXPlugin".equals(paymentPluginId)){
			data.put("openid", param.get("openid"));
		}
		httpClient.setContentType(ContentType.APP_JSON);
		System.out.println("beecloud request:"+data);
		String msg = httpClient.getResponseBody(data);
		System.out.println("beecloud response msg:"+msg);
		JSONObject jo =JSONObject.parseObject(msg);
		
		if(jo.get("resultCode")!=null && jo.getIntValue("resultCode")==0){
			switch (paymentPluginId) {
			case "unionpayPlugin":
				return jo.getString("html");
			case "bcWXPlugin":
				JSONObject res = new JSONObject();
				res.put("appId", jo.getString("app_id"));
				res.put("package", jo.getString("package"));
				res.put("nonceStr", jo.getString("nonce_str"));
				res.put("timeStamp", jo.getString("timestamp"));
				res.put("paySign", jo.getString("pay_sign"));
				res.put("signType", jo.getString("sign_type"));
				return res.toString();
			default:
				sun.misc.BASE64Encoder encoder1 = new sun.misc.BASE64Encoder();
				BufferedImage s1 = createImage(jo.getString("code_url"));
				ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
				ImageIO.write(s1, "jpg", bos1);
				byte[] b1 = bos1.toByteArray();
				String encoderStr1 = "data:image/jpg;base64,"+encoder1.encodeBuffer(b1);
				return encoderStr1;
			}
		}else{
			throw new GdbmroException(10001,"请勿重复支付");
		}
	}
	
	private int getRandom(){
		int i=(int)(Math.random()*900)+100;
		return i;
	}
	
	
	/**
	 * 威富通支付宝，微信扫码支付
	 * add 20170209
	 */
	public String toWFTPay(String orderId, BigDecimal amount, String paymentPluginId,HttpServletRequest request) throws Exception {
		String service= null;
		switch (paymentPluginId) {
		case "alipayDirectPlugin":
			service = "pay.alipay.nativev2";//BC版支付宝线下扫码支付
			break;
		case "weixinPlugin":
			service = "pay.weixin.native";//微信二维码支付
			break;
		}
		HttpclientUtil httpClient = new HttpclientUtil("https://pay.swiftpass.cn/pay/gateway", Method.POST);
		Map<String , String> data = new HashMap();
		data.put("service", service);//接口类型
		data.put("mch_id", mchId);//商户号
		data.put("out_trade_no", orderId);//订单号
		data.put("body", "购买商品");//商品描述
		data.put("total_fee",String.valueOf(amount.intValue()));//总金额
		data.put("mch_create_ip",IpUtil.getIpAddr(request));//终端ip
		data.put("notify_url", getNotifyUrl(orderId, NotifyMethod.async));//通知地址
		data.put("nonce_str", String.valueOf(new Date().getTime()));//随机字符串
		
		Map<String,String> params = SignUtils.paraFilter(data);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "&key="+key, "utf-8");
        data.put("sign", sign);
        SortedMap<String,String> map = new TreeMap(data);//对map根据首字母排序
        System.out.println("reqParams:" + XmlUtils.parseXML(map));
        CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
        String res = null;
        try {
            HttpPost httpPost = new HttpPost("https://pay.swiftpass.cn/pay/gateway");
            StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map),"utf-8");
            httpPost.setEntity(entityParams);
            httpPost.setHeader("Content-Type", "text/xml;utf-8");
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            if(response != null && response.getEntity() != null){
                Map<String,String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
                res = XmlUtils.toXml(resultMap);
                System.out.println("请求结果：" + res);
                
                if(resultMap.containsKey("sign")){
                    if(!SignUtils.checkParam(resultMap, key)){
                        res = "验证签名不通过";
                    }else{
                        if("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))){                     	
            				String encoderStr1 = resultMap.get("code_img_url");
            				return encoderStr1;
                        }else{
                        	return res.toString();
                        }
                    }
                } 
            }else{
                res = "操作失败";
            }
        }finally {
            if(response != null){
                response.close();
            }
            if(client != null){
                client.close();
            }
        }
        return res.toString();
	}
	
	/**
	 * 获取通知URL
	 * 
	 * @param sn
	 *            编号
	 * @param notifyMethod
	 *            通知方法
	 * @return 通知URL
	 */
	protected String getNotifyUrl(String orderId, NotifyMethod notifyMethod) {
		
		String notifyUrl = JFig.getInstance().getValue("pay_options", "NOTIFY_URL", "https://www.bmatser.com/gdbmro_serviceApi/payment/notify/");
		notifyUrl = notifyUrl + orderId;
		String returnUrl = JFig.getInstance().getValue("pay_options", "RETURN_URL", "http://www.bmatser.com/customer/center/orderList");
		if (notifyMethod == null) {
			return notifyUrl ;
		}else if(notifyMethod.equals(NotifyMethod.sync)){//同步
			return returnUrl;         
		}else if(notifyMethod.equals(NotifyMethod.async)){//异步
			return notifyUrl;
		}else{
			return returnUrl; 
		}
		
		/*if (notifyMethod == null) {
			return Constants.BASE_URL_NET + "gdbmro_serviceApi/payment/notify/" + orderId ;
		}else if(notifyMethod.equals(NotifyMethod.sync)){//同步
//			return Constants.BASE_URL_COM + "payment/payok/" + orderId;  //com
			return Constants.BASE_URL_NET + "buyer/order/list";         //net
//			return "http://mall.bmatser.com/payment/payok/" + orderId;
		}else if(notifyMethod.equals(NotifyMethod.async)){//异步
			return Constants.BASE_URL_NET + "gdbmro_serviceApi/payment/notify/" + orderId;  //net
//			return Constants.BASE_URL_COM + "gdbmro_serviceApi/payment/notify/" + orderId;    //com
		}else{
//			return Constants.BASE_URL_COM + "payment/payok/" + orderId;  //com
			return Constants.BASE_URL_NET + "buyer/order/list";         //net
//			return "http://mall.bmatser.com/payment/payok/" + orderId;
		}*/
	}

	
	private static BufferedImage createImage(String content) throws Exception {  
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();  
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");  
        hints.put(EncodeHintType.MARGIN, 1);  
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,  
                BarcodeFormat.QR_CODE, 220, 220, hints);  
        int width = bitMatrix.getWidth();  
        int height = bitMatrix.getHeight();  
        BufferedImage image = new BufferedImage(width, height,  
                BufferedImage.TYPE_INT_RGB);  
        for (int x = 0; x < width; x++) {  
            for (int y = 0; y < height; y++) {  
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000  
                        : 0xFFFFFFFF);  
            }  
        }  
/*        if (imgPath == null || "".equals(imgPath)) {  
            return image;  
        }  */
        /*// 插入图片  
        QRCodeUtil.insertImage(image, imgPath, needCompress);  */
        return image;  
    }  
	public static void main(String[] args) throws Exception {
		sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
		BufferedImage s = createImage("");
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	ImageIO.write(s, "jpg", bos);
    	byte[] b = bos.toByteArray();
    	String encoderStr = "data:image/jpg;base64,"+encoder.encodeBuffer(b);
    	System.out.println("<img src='"+encoderStr+"'>");
	}

	@Override
	public PayInfo toAppPay(String orderId,
			LoginInfoUtil loginInfo, String isUse) {
		if(isUse==null || "".equals(isUse)){
			isUse = "-1";
		}else if(!"1".equals(isUse) && !"0".equals(isUse)){
			isUse="0";
		}
		
		PayInfo payInfo = orderInfoDao.getToPayInfo(orderId,loginInfo.getDealerId());
		payInfo.toUseBalance(Integer.parseInt(isUse));
		return payInfo;
	}

	@Override
	public BigDecimal toAppPayByBalance(String orderId, LoginInfoUtil loginInfo,
			String isUse) {
		this.updateIsBalance("1".equals(isUse),orderId);
		OrderInfo orderInfo = orderService.getOrderById(Long.valueOf(orderId));
		if(orderInfo != null){
			if(!"1".equals(orderInfo.getOrderStatus())){
				return BigDecimal.ZERO;
			}
			if(orderInfo.getPayAmount().compareTo(orderInfo.getBalanceDeduction())==0){
				PaymentBillPage paymentBillPage = getPaymentBill(orderInfo);
				paymentBillPage.setPayType("4");
				Map map = addPaymentBillService.findPayOrderById(Long.valueOf(orderId));
				if(null == map || map.isEmpty() || "0".equals(map.get("num").toString())){
					addPaymentBillService.savePaymentBill(paymentBillPage);
				}
				this.saveOrderByBalance(orderId);
				return BigDecimal.ZERO;
			}else{
				return orderInfo.getPayAmount().subtract(orderInfo.getBalanceDeduction());
			}
		}else{
			throw new RuntimeException("订单不存在");
		}
	}
}
