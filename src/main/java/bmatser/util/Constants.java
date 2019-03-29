package bmatser.util;

import org.igfay.jfig.JFig;

public class Constants {

	public final static String BASE_URL_NET = JFig.getInstance().getValue("system_options", "BASE_URL_NET", "http://www.bmatser.com/");
	
	public final static String BASE_URL_COM = JFig.getInstance().getValue("system_options", "BASE_URL_COM", "http://www.gdbmromall.com/");
	
	public final static String IMAGE_URL = JFig.getInstance().getValue("system_options", "IMAGE_URL", "http://images.bmatser.com/");
	
	public final static Integer DEFAULT_SELLER_ID = 939817; 
	
	public final static Double SCORE_RATE = 0.1;
	
	public final static String AUTH = "U2FsdGVkX1/IDFxw1gfZo7SzP/yt3cdt";
	
	public final static String loginCookieName = "cookieId";
	public final static String saasLoginCookieName = "cookieId";
	public final static String wxloginCookieName = "wxlg_";
	
	public final static String CHECK_SEARCH = "user_";
	public final static String loginCountKey = "lgCou";
	
	public final static Integer NginxIP = 0;
	
	public final static String checkCodeCookieName = "vc_";
	
	public final static String shopCartCookieName = "shopcart";
	
	public final static String regCodeKey = "regCode";
	public final static String regMobileKey = "regMobile";
	public final static String regEmailKey = "regEmail";
	public final static String editPasswordKey = "editPassword";
	public final static String editMobileKey = "editMobile";
	
	public final static String modifyMobileKey = "modifyMobile";
	public final static String modifyMobCodeKey = "modifyMobCode";
	
	public final static String cartNumCookieName = "cartNum";
	
	public final static int ORDER_PAY = 1;//订单付款
	public final static int GROUP_GOODS = 3;//团购商品
	
	
	//三证审核  手机端
	public final static String mobileIs = "MOBILEIS";
	
	//三证审核  PC端
    public final static String PcIs = "PCIS";
	
}
