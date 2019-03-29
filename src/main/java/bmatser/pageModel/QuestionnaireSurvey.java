package bmatser.pageModel;

import java.sql.Timestamp;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="问卷调查参数")
public class QuestionnaireSurvey {
	@ApiModelProperty(value="dealerId")
	private String dealerId;
	@ApiModelProperty(value="调查时间")
	private Timestamp time;
	@ApiModelProperty(value="产品类型：1手动工具   2劳保用品   3电气控制    4仪器仪表    5办公用品    6消防安防    7焊接粘合    8机械传动    9气动液压    10清洁消耗品  11暖通照明    12紧固密封    13车间化学品    14金属加工    15仓储物流包装 ")
	private String productType;
	@ApiModelProperty(value="进货渠道 :1品牌厂家（您有授权资质）  2品牌厂家（无授权）   3代理商     4贸易商  5电商网站（例如_）")
	private String  supplyChannels;
	@ApiModelProperty(value="进货渠道中 电商网站的例子")
	private String example;
	@ApiModelProperty(value="进货过程解决的问题：1降低采购成本    2交期    3正品    4采购效率提升   5账期和信用    6产品售后    7其他___")
	private String stockNeedToSolve;
	@ApiModelProperty(value="进货过程解决的问题 其它的例子")
	private String otherFirstExample;
	@ApiModelProperty(value="工电宝您是否可以搜索到想要的品牌和产品  1是    2否")
	private String productsAndBrandsIsWant;
	@ApiModelProperty(value="希望工电宝增加哪些品牌")
	private String needAddBrands;
	@ApiModelProperty(value="希望工电宝增加哪些产品")
	private String needAddProducts;
	@ApiModelProperty(value="网站上的选型功能，能否满足您的需求 1能    2不能")
	private String selection;
	@ApiModelProperty(value="网站上的团购是否去关注 1经常关注    2偶尔看看    3不关注")
	private String focusGroup;
	@ApiModelProperty(value="您还希望增加哪些类别的团购或促销")
	private String addSalesPromotion;
	@ApiModelProperty(value="在积分商城，能兑换到想要的礼品吗？ 1能    2不能（还希望兑换什么礼品_____）")
	private String isNeedGift;
	@ApiModelProperty(value="还想兑换的礼品")
	private String wantGift;
	@ApiModelProperty(value="下单过程还存在哪些问题 : 1操作步骤太多    2订单确认页填写内容太多  3在线支付方式中支持银行太少     4其他（例如__）")
	private String orderProblem;
	@ApiModelProperty(value="下单过程问题的其它 例子")
	private String otherSecondExample;
	@ApiModelProperty(value="对工电宝地推人员的服务态度、产品介绍、促销讲解是否满意  :1非常满意     2满意     3一般     4不满意     5非常不满意")
	private String promoterService;
	@ApiModelProperty(value="商务人员提供的服务包括服务态度、回复速度、问题解答是否满意:1非常满意     2满意     3一般     4不满意     5非常不满意")
	private String businessService;
	@ApiModelProperty(value="在手机APP的使用过程中遇到过哪些问题: 1APP经常出错打不开    2打开内容加载慢    3APP更新太频繁    4没什么问题")
	private String appProblem;
	@ApiModelProperty(value="您公司使用的进销存软件是: 1金蝶ERP 2用友    3管家婆    4速达    5天思    6顺和达   7轻松掌柜   8无    9其他__")
	private String purchaseSaleStock;
	@ApiModelProperty(value="进销存软件的其它 例子")
	private String otherThirdExample;
	@ApiModelProperty(value="您希望通过进销存软件解决哪些问题？:1记录商品真实的库存和销售情况    2价格管理包括定价、报价   3记账功能、简单的财务报表    4员工业绩考核    5统计商品的畅销、滞销情况   6库存缺货报警功能   7其他__")
	private String purchaseSaleStockTosolve;
	@ApiModelProperty(value="用进销存软件解决问题的其它 例子")
	private String otherFourthExample;
	@ApiModelProperty(value="如果工电宝提供一款免费的进销存软件，您愿不愿意使用 1愿意    2不愿意")
	private String freeSoftware;	
	@ApiModelProperty(value="您觉得工电宝网站需要进一步解决的地方是？:1促销力度加大    2品牌种类更多   3下单流程更简便  4优化包装 5售后服务问题     6物流服务质量和及时性问题    7其他（例如__）")
	private String websiteNeedToSolve;
	@ApiModelProperty(value="网站需要进一步解决的其它 例子")
	private String otherFifthExample;
	@ApiModelProperty(value="综合您的网购经历，您对工电宝网站整体服务是否满意 1非常满意     2满意     3一般    4不满意    5非常不满意")
	private String websiteService;
	@ApiModelProperty(value="您对工电宝的建议和意见")
	private String notice;
	
	public String getDealerId() {
		return dealerId;
	}
	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getSupplyChannels() {
		return supplyChannels;
	}
	public void setSupplyChannels(String supplyChannels) {
		this.supplyChannels = supplyChannels;
	}
	public String getExample() {
		return example;
	}
	public void setExample(String example) {
		this.example = example;
	}
	public String getStockNeedToSolve() {
		return stockNeedToSolve;
	}
	public void setStockNeedToSolve(String stockNeedToSolve) {
		this.stockNeedToSolve = stockNeedToSolve;
	}
	public String getOtherFirstExample() {
		return otherFirstExample;
	}
	public void setOtherFirstExample(String otherFirstExample) {
		this.otherFirstExample = otherFirstExample;
	}
	public String getProductsAndBrandsIsWant() {
		return productsAndBrandsIsWant;
	}
	public void setProductsAndBrandsIsWant(String productsAndBrandsIsWant) {
		this.productsAndBrandsIsWant = productsAndBrandsIsWant;
	}
	public String getNeedAddBrands() {
		return needAddBrands;
	}
	public void setNeedAddBrands(String needAddBrands) {
		this.needAddBrands = needAddBrands;
	}
	public String getNeedAddProducts() {
		return needAddProducts;
	}
	public void setNeedAddProducts(String needAddProducts) {
		this.needAddProducts = needAddProducts;
	}
	public String getSelection() {
		return selection;
	}
	public void setSelection(String selection) {
		this.selection = selection;
	}
	public String getFocusGroup() {
		return focusGroup;
	}
	public void setFocusGroup(String focusGroup) {
		this.focusGroup = focusGroup;
	}
	public String getAddSalesPromotion() {
		return addSalesPromotion;
	}
	public void setAddSalesPromotion(String addSalesPromotion) {
		this.addSalesPromotion = addSalesPromotion;
	}
	public String getIsNeedGift() {
		return isNeedGift;
	}
	public void setIsNeedGift(String isNeedGift) {
		this.isNeedGift = isNeedGift;
	}
	public String getWantGift() {
		return wantGift;
	}
	public void setWantGift(String wantGift) {
		this.wantGift = wantGift;
	}
	public String getOrderProblem() {
		return orderProblem;
	}
	public void setOrderProblem(String orderProblem) {
		this.orderProblem = orderProblem;
	}
	public String getOtherSecondExample() {
		return otherSecondExample;
	}
	public void setOtherSecondExample(String otherSecondExample) {
		this.otherSecondExample = otherSecondExample;
	}
	public String getPromoterService() {
		return promoterService;
	}
	public void setPromoterService(String promoterService) {
		this.promoterService = promoterService;
	}
	public String getBusinessService() {
		return businessService;
	}
	public void setBusinessService(String businessService) {
		this.businessService = businessService;
	}
	public String getAppProblem() {
		return appProblem;
	}
	public void setAppProblem(String appProblem) {
		this.appProblem = appProblem;
	}
	public String getPurchaseSaleStock() {
		return purchaseSaleStock;
	}
	public void setPurchaseSaleStock(String purchaseSaleStock) {
		this.purchaseSaleStock = purchaseSaleStock;
	}
	public String getOtherThirdExample() {
		return otherThirdExample;
	}
	public void setOtherThirdExample(String otherThirdExample) {
		this.otherThirdExample = otherThirdExample;
	}
	public String getPurchaseSaleStockTosolve() {
		return purchaseSaleStockTosolve;
	}
	public void setPurchaseSaleStockTosolve(String purchaseSaleStockTosolve) {
		this.purchaseSaleStockTosolve = purchaseSaleStockTosolve;
	}
	public String getOtherFourthExample() {
		return otherFourthExample;
	}
	public void setOtherFourthExample(String otherFourthExample) {
		this.otherFourthExample = otherFourthExample;
	}
	public String getFreeSoftware() {
		return freeSoftware;
	}
	public void setFreeSoftware(String freeSoftware) {
		this.freeSoftware = freeSoftware;
	}
	public String getWebsiteNeedToSolve() {
		return websiteNeedToSolve;
	}
	public void setWebsiteNeedToSolve(String websiteNeedToSolve) {
		this.websiteNeedToSolve = websiteNeedToSolve;
	}
	public String getOtherFifthExample() {
		return otherFifthExample;
	}
	public void setOtherFifthExample(String otherFifthExample) {
		this.otherFifthExample = otherFifthExample;
	}
	public String getWebsiteService() {
		return websiteService;
	}
	public void setWebsiteService(String websiteService) {
		this.websiteService = websiteService;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
	
	
}
