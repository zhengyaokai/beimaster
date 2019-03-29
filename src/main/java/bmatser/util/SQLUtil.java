package bmatser.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SQLUtil {
	
	public final static String PAY_AMOUNT = "(order_amount"+//订单金额
			"-ifnull(goods_sale_amount,0)"+//商品优惠
			"-ifnull(freight_sale_amount,0)"+//运费优惠
			"-ifnull(full_cut_amount,0)"+//满减优惠
			"-ifnull(score_deduction_amout,0)" +//现金券抵扣
			"-ifnull(tax_sale_amount,0)" +//现金券抵扣
			"-ifnull(bean_deduction,0)" +//工币抵扣
			"- IFNULL(group_order_sale,0))";//团购优惠
	public final static String PAY_AMOUNT_OI = "(oi.order_amount"+
			"-ifnull(oi.goods_sale_amount,0)"+
			"-ifnull(oi.freight_sale_amount,0)"+
			"-ifnull(oi.full_cut_amount,0)"+
			"-ifnull(oi.score_deduction_amout,0)" +
			"-ifnull(oi.tax_sale_amount,0)" +
			"-ifnull(oi.bean_deduction,0)" +		
			"- IFNULL(oi.group_order_sale,0))";
	//排除工币
	public final static String PAY_AMOUNT_BEAN = "(oi.order_amount"+
			"-ifnull(oi.goods_sale_amount,0)"+
			"-ifnull(oi.freight_sale_amount,0)"+
			"-ifnull(oi.full_cut_amount,0)"+
			"-ifnull(oi.score_deduction_amout,0)" +
			"-ifnull(oi.tax_sale_amount,0)" +
			"- IFNULL(oi.group_order_sale,0))";
	
	public final static String GOODS_IMG = "CONCAT(if(" +
			"sg.seller_id = 9407609 and sg.seller_id is not null," +
			"'http://img10.360buyimg.com/imgzone/'," +
			"'"+Constants.IMAGE_URL+"goodsImg/')," +
			"ifnull(g.image,'0.jpg'))";
	public final static String BEAN = "if((sg.is_groupon is not null and sg.is_groupon = 1) or (sg.is_fullcut is not null and sg.is_fullcut = 1),0,ROUND(sg.price*ifnull(sg.bean_rate,0)*100))";
	
	public final static String IS_BEAN = "if((sg.is_groupon is not null and sg.is_groupon = 1) or (sg.is_fullcut is not null and sg.is_fullcut = 1),0,ifnull(sg.is_bean,0))";
	
	public final static String IS_RATE = "if(sg.is_sale is null or sg.is_sale = 0 or sg.is_sale = '',sg.is_rate,0)";
	
	public final static String CASHBACK_TYPE = "(CASE cashback_type when 1 then '返现' when 2 then '提现' END )";
	
	public final static String CASHBACK_STATUS = "(CASE cashback_status when 1 then '即将返现'  when 2 then '已返现' END)";
	
	public final static String WITHDRAWALS_STATUS = "(case status when 0 then '处理中' when 1 then '处理完成' when 2 then '驳回' end)";
	
	public final static String GER_STATUS = "(case ger.status when 1 then '待发货' when 2 then '已发货' when 3 then '已取消' when 4 then '已推送' end)";//gift_exchange_record status(状态)（1：待发货  2：已发货  3：已取消 4 : 已推送）
	
	public final static String GROUP_STATUS  = "(select gas.seller_goods_id id from groupon_goods gas, groupon_activity ga where  gas.`status`=1  and ga.groupon_status!=5  and gas.groupon_id = ga.id and ga.end_time >= now() and ga.start_time <= now())groupStatus";
	
	public final static String IS_REAL_BEAN = "if((groupStatus.id is not null) or (sg.is_fullcut is not null and sg.is_fullcut = 1),0,ifnull(sg.is_bean,0))";
	
	public final static String REAL_BEAN = "if((groupStatus.id is not null) or (sg.is_fullcut is not null and sg.is_fullcut = 1),0,ROUND(sg.price*ifnull(sg.bean_rate,0)*100))";
	
	public static String getSaasDealerId(){
    	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    	String dealerId = request.getParameter("dealerId");
    	String buyerId = request.getParameter("buyerId");
    	if(StringUtils.isNotBlank(dealerId)||StringUtils.isNotBlank(buyerId)){
    		return StringUtils.isNotBlank(buyerId)?buyerId:dealerId;
    	}
    	LoginInfo loginInfo = MemberTools.isSaasLogin(request);
    	if(loginInfo!=null){
    		return loginInfo.getDealerId();
    	}else{
    		return "";
    	}
    }
}
