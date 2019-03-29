package bmatser.util;

public enum Status {
	
	NO("否", "0"), YES("是", "1"),
	UNDEFAULT("非默认", "0"), DEFAULT("默认", "1"),
	INVALID("无效", "0"), VALID("有效", "1"), DELETE("删除", "2"), EXPIRE("作废", "3"),
	UNCHECKED("待审核", "0"), CHECKED("审核通过", "1"), NOTCHECKED("审核不通过", "2"),
	UNAUTHORIZE("未授权", "0"), AUTHORIZE("已授权", "1"), NOTAUTHORIZE("取消授权", "2"),
	UNPAY("未支付", "0"), PAY("已支付", "1"),COMPLETEPAY("全额付款","2"),
	UNSETTLED("未结算", "0"), SETTLED("已结算", "1"),
	UNSHIPPING("未发货", "0"), SHIPPING("已发货", "1"), COMPLETED("已完成", "2"), CANCELED("已取消", "3"),
	COMMONINVOICE("普通发票","1"),VALUEADDINVOICE("增值税发票","2"),UNACCOUNTCONFIRM("未对账","0"),ACCOUNTCONFIRM("已对账","1"),UNIONPAY("银联支付","1"),
	OFFLINEPAY("线下支付","2"),SHOPFREIGHT("店铺运费","1"),GOODSFREIGHT("单品运费","2"),ORDERAMOUNT("订单金额","1"),REGULARAMOUNT("固定费用","2"),
	HASSHELVES("已上架","1"),NOTSHELVES("未上架","2"),DELETESHELVES("删除","3"),NOFREESHIPPING("不包邮","0"),FREESHIPPING("包邮","1"),
	WAITPAYMENT("未付款","1"),WAITSENDGOODS("待发货","2"),
	//订单状态【1:待付款   2：待发货  3：已发货（待收货）  4：已收货（待结算）  5：对账中（已结算）  6：已完成  7：退货中  8：已取消  9:待确认付款】
	PENDINGPAYMENT("待付款","1"),TOBESHIPPED("待发货","2"),TORECEIVETHEGOODS("已发货（待收货）","3"),
	TOBESETTLED("已收货（待结算）","4"),RECONCILIATION("对账中（已结算）","5"),HASBEENCOMPLETED("已完成","6"),RETURNSBEYOND("退货中","7"),
	HASBEENCANCELED("已取消","8"),TOCONFIRMPAYMENT("待确认付款","9"),
	//订单支付状态（0：待付款(待确认付款)  1：部分付款  2：全额付款）
	CONFIRMPAYMENT("待付款(待确认付款)","0"),PAYMENTINPART("部分付款","1"),FULLPAYMENT("全额付款","2"),
	//订单是否需要开票
	NONEEDINVOICE("不需要开票","0"),NEEDINVOICE("需要开票","1"),
	//订单是否已经开票
	NOBILL("未开票","0"),OPENBILL("已开票","1"),
	//订单审核延期状态（0：无延期申请 1：待确认 2：同意  3：不同意）[order_info.delay_consign_status]
	NODELAY("无延期申请","0"),DELAYWIATCHECK("待确认 ","1"),DELAYCHECKED("同意","2"),DELAYWIATNOCHECK("不同意","3"),
	//退货单的审核状态（0：待审核  1：商家审核不通过  2：商家审核通过(待买家发货)  3：待收货（买家已发货） 4：已完成（确认收货） ）[order_return_bill.check_status]
	RETURNWAITCHECK("待审核","0"),RETURNOCHECKED("商家审核不通过","1"),RETURNCHECKED("商家审核通过(待买家发货)","2"),RETURNWAITGET("待收货（买家已发货）","3"),RETURNDONE("已完成（确认收货）","4"),
	//退款单的退款状态退款状态（0：未退款  1：已退款  2：取消）[order_refund_bill.check_status]
	RETURNBILLUNRETURN("未退款 ","0"),RETURNBILLDONE("已退款","1"),RETURNBILLCANCLE("取消","2"),
	//商家类型-商户类型（1：供应商  2：零售商  3:后台管理）[dealer.dealer_type]
	DEALERSELLER("供应商 ","1"),DEALERBUYER("零售商","2"),DEALEMANGER("后台管理","3"),
	//注册用户-是否登陆（1：登录  0：未登录） [register.is_login]
	ISLOGIN("登录 ","1"),ISNOLOGIN("未登录","0"),
	//是否在门户显示（1：显示  2：不显示）
	ISSHOW("显示","1"),ISNOTSHOW("不显示","2"),
	
	SUPPLY("供货","1"),ASK("求购","2"),
	//发货状态（0：未发货  1：已发货  2：已收货）
	NONDELIVERY("未发货","0"),SHIPPED("已发货","1"),RECEIPTOFGOODS("已收货","2"),
	//0：待审核  1：已人工审核  2：取消
	WAITAPPLY("待审核","0"),HAVEARTIFICIALAUDIT("已人工审核","1"),NOAPPLY("取消","2"),
	//0:待审核  1：报价中 2：已有报价 3：买家比价中 4：买家已锁货 5：交易已锁定 6：交易完成  7：终止交易
	TRADEWAITAPPLY("待审核","0"),QUOTATION("报价中 ","1"),EXISTINGQUOTATION("已有报价","2"),
	BUYERSPRICE("买家比价中","3"),BUYERLOCKGOODS("买家已锁货","4"), TRANSACTIONLOCKED("交易已锁定","5"),
	COMPLETIONTRANSACTION("交易完成","6"),TERMINATIONTRANSACTION("终止交易","7"),
	//积分类型（1：下单收入  2：兑换支出  3：注册收入  4：登陆收入 5：求购信息）
	DSTYPEORDER("下单收入","1"),DSTYPECHANGE("兑换支出","2"),DSTYPEREGISTER("注册收入","3"),
	DSTYPELOGIN("登陆收入","4"),DSTYPESUPPLYDEMAND("求购信息","5"),
	//供应商后台
	ISSHOWPRODUCT("显示","1"),NOSHOWPRODUCT("不显示","0"),
	//上传报价单   状态（0：待受理  1：已受理）
	TOBEACCEPTED("待受理","0"),HAVEBEENACCEPTED("已受理","1"),
	//询价报价单 '状态 1 待报价  2已报价  3.暂存'
	ENQUIRYPREPARE("待报价","1"),ENQUIRYCOMPLETED("已报价","2"),ENQUIRYINPROGRESS("暂存","3")
	;
	private String name;
    private String value;

    private Status(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
     
}
