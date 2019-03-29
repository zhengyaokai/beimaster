package bmatser.plugin.weixin;

/**
 * User: rizenguo
 * Date: 2014/10/22
 * Time: 21:29
 */

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import bmatser.plugin.PaymentPlugin.NotifyMethod;

/**
 * 请求被扫支付API需要提交的数据
 */
public class ScanPayReqData {

    //每个字段具体的意思请查看API文档
    private String appid = "wxd8fd26a586e9708a";//公众账号ID,必填
    private String mch_id = "1395903702";//商户号,必填
    private String device_info = "WEB";//设备号,必填
    private String nonce_str = "";//随机字符串,必填
    private String sign = "";//签名,必填
    private String body = "工电宝-支付";//商品描述,必填
    private String out_trade_no = "";//商户订单号,必填
    private int total_fee = 0;//总金额,必填
    private String spbill_create_ip = "";//终端IP,必填
    private String notify_url = "";//通知地址,必填
    private String trade_type = "JSAPI";//交易类型,必填
    private String openid = null;//交易类型,必填
    /*    private String detail = "";//商品详情,
    private String attach = "";//附加数据*/
    /*    private String fee_type = "";//商户订单号,必填*/
    /*    private String time_start = "";//交易起始时间
    private String time_expire = "";//交易结束时间
    private String goods_tag = "";//商品标记*/
/*    private String product_id = "";//商品ID
    private String limit_pay = "";//商品ID
    private String openid = "";//用户标识
*/    //private String auth_code = "";//不存在

    /**
     * @param url 
     * @param authCode 这个是扫码终端设备从用户手机上扫取到的支付授权号，这个号是跟用户用来支付的银行卡绑定的，有效期是1分钟
     * @param body 要支付的商品的描述信息，用户会在支付成功页面里看到这个信息
     * @param attach 支付订单里面可以填的附加数据，API会将提交的这个附加数据原样返回
     * @param outTradeNo 商户系统内部的订单号,32个字符内可包含字母, 确保在商户系统唯一
     * @param totalFee 订单总金额，单位为“分”，只能整数
     * @param deviceInfo 商户自己定义的扫码支付终端设备号，方便追溯这笔交易发生在哪台终端设备上
     * @param spBillCreateIP 订单生成的机器IP
     * @param timeStart 订单生成时间， 格式为yyyyMMddHHmmss，如2009年12 月25 日9 点10 分10 秒表示为20091225091010。时区为GMT+8 beijing。该时间取自商户服务器
     * @param timeExpire 订单失效时间，格式同上
     * @param goodsTag 商品标记，微信平台配置的商品标记，用于优惠券或者满减使用
     * @throws Exception 
     */
    public ScanPayReqData(String orderId,int money,String ip, String url,String openId){

    	Map<String, Object> map = new HashMap<>();
    	String sign = "";
    	
    	setNotify_url(url);
        //商户系统内部的订单号,32个字符内可包含字母, 确保在商户系统唯一
        setOut_trade_no(orderId);

        //订单总金额，单位为“分”，只能整数
        setTotal_fee(money);


        //订单生成的机器IP
        setSpbill_create_ip(ip);


        this.openid=openId;
        //随机字符串，不长于32 位
        setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
    	map.put("appid", this.appid);
    	map.put("mch_id", this.mch_id);
    	map.put("device_info", this.device_info);
    	map.put("body", this.body);
    	map.put("nonce_str", this.nonce_str);
    	map.put("spbill_create_ip", this.spbill_create_ip);
    	map.put("total_fee", String.valueOf(this.total_fee));
    	map.put("notify_url", this.notify_url);
    	map.put("out_trade_no", this.out_trade_no);
    	map.put("trade_type", this.trade_type);
    	map.put("openid", openId);
        //根据API给的签名规则进行签名
        try {
        	sign = Signature.getSign(map);System.out.println(sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
        setSign(sign);//把签名数据设置到Sign这个属性中

    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }



    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }




    public Map<String,String> toMap(){
        Map<String,String> map = new HashMap<String, String>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj;
            try {
                obj = field.get(this);
                if(obj!=null){
                    map.put(field.getName(), obj.toString());
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }


	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}




}
