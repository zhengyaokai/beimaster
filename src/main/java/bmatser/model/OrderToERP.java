package bmatser.model;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;


public class OrderToERP {

	public OrderToERP() {
	}
	//构造函数，传必填参数
	public OrderToERP( String baDjBz, String baNbXsBh, String guser, String buser, String sumHtSl, String sumCkSl, String sumKpSl ) {
		BaDjBz = baDjBz;
		BaNbXsBh = baNbXsBh;
		Guser = guser;
		Buser = buser;
		SumHtSl = sumHtSl;
		SumCkSl = sumCkSl;
		SumKpSl = sumKpSl;
	}

	@JSONField(name = "NID")
	private String NID;//订单编号
	private Integer buyerId;
	@JSONField(name = "Gclient")
	private String Gclient;//客户编码
	@JSONField(name = "GclientDeliveryID")
	private String GclientDeliveryID;//收货人ID
	@JSONField(name = "BaXsFpLx")
	private String BaXsFpLx;//发票类型
	@JSONField(name = "BaFkFs")
	private String BaFkFs;//付款方式
	@JSONField(name = "BaFhFs")
	private String BaFhFs;//发货方式
	@JSONField(name = "BaYfCd")
	private String BaYfCd;//运费承担
	@JSONField(name = "Account")
	private String Account;//账号
	@JSONField(name = "Tax")
	private String Tax;//税号
	@JSONField(name = "BankAddress")
	private String BankAddress;//开户银行
	@JSONField(name = "InvoiceAddress")
	private String InvoiceAddress;//开票地址
	@JSONField(name = "InvoiceTelphone")
	private String InvoiceTelphone;//开票电话
	@JSONField(name = "DeliveryMan")
	private String DeliveryMan;//收货人
	@JSONField(name = "ExactDeliveryMan")
	private String ExactDeliveryMan;//精确收货人
	@JSONField(name = "DeliveryAddress")
	private String DeliveryAddress;//收货地址
	@JSONField(name = "DeliveryTelphone")
	private String DeliveryTelphone;//收货电话
	@JSONField(name = "InvoiceDeliveryMan")
	private String InvoiceDeliveryMan;//收票人
	@JSONField(name = "InvoiceDeliveryAddress")
	private String InvoiceDeliveryAddress;//收票地址
	@JSONField(name = "InvoiceDeliveryTelphone")
	private String InvoiceDeliveryTelphone;//收票电话
	@JSONField(name = "BaDjBz")
	private String BaDjBz;//单据备注
	@JSONField(name = "BaNbXsBh")
	private String BaNbXsBh;//销售编号
	@JSONField(name = "Guser")
	private String Guser;//销售员
	@JSONField(name = "Buser")
	private String Buser;//销售商务
	@JSONField(name = "SumHtSl")
	private String SumHtSl;//总合同数量
	@JSONField(name = "SumCkSl")
	private String SumCkSl;//总出库数量
	@JSONField(name = "SumKpSl")
	private String SumKpSl;//总开票数量
	@JSONField(name = "GaKhBID")
	private String GaKhBID;//客户合同
	@JSONField(name = "ProduceList")
	private List<Produce> ProduceList;//产品列表
	@JSONField(name = "goodsAmount")
	private BigDecimal goodsAmount;//订单商品金额总计
	@JSONField(name = "realAmount")
	private BigDecimal realAmount;//订单实际付款金额
	@JSONField(name = "contractDiscount")
	private BigDecimal contractDiscount;//订单总折扣（所有优惠抵扣都算折扣）
	@JSONField(name = "consignAddress")
	private String consignAddress;//收货地址JSON


	@JSONField(name = "NID")
	public String getNID() {
		return NID;
	}
	public void setNID(String nID) {
		NID = nID;
	}

	public Integer getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(Integer buyerId) {
		this.buyerId = buyerId;
	}
	@JSONField(name = "Gclient")
	public String getGclient() {
		return Gclient;
	}
	public void setGclient(String gclient) {
		Gclient = gclient;
	}
	@JSONField(name = "GclientDeliveryID")
	public String getGclientDeliveryID() {
		return GclientDeliveryID;
	}
	public void setGclientDeliveryID(String gclientDeliveryID) {
		GclientDeliveryID = gclientDeliveryID;
	}
	@JSONField(name = "BaXsFpLx")
	public String getBaXsFpLx() {
		return BaXsFpLx;
	}
	public void setBaXsFpLx(String baXsFpLx) {
		BaXsFpLx = baXsFpLx;
	}
	@JSONField(name = "BaFkFs")
	public String getBaFkFs() {
		return BaFkFs;
	}
	public void setBaFkFs(String baFkFs) {
		BaFkFs = baFkFs;
	}
	@JSONField(name = "BaFhFs")
	public String getBaFhFs() {
		return BaFhFs;
	}
	public void setBaFhFs(String baFhFs) {
		BaFhFs = baFhFs;
	}
	@JSONField(name = "BaYfCd")
	public String getBaYfCd() {
		return BaYfCd;
	}
	public void setBaYfCd(String baYfCd) {
		BaYfCd = baYfCd;
	}
	@JSONField(name = "Account")
	public String getAccount() {
		return Account;
	}
	public void setAccount(String account) {
		Account = account;
	}
	@JSONField(name = "Tax")
	public String getTax() {
		return Tax;
	}
	public void setTax(String tax) {
		Tax = tax;
	}
	@JSONField(name = "BankAddress")
	public String getBankAddress() {
		return BankAddress;
	}
	public void setBankAddress(String bankAddress) {
		BankAddress = bankAddress;
	}
	@JSONField(name = "InvoiceAddress")
	public String getInvoiceAddress() {
		return InvoiceAddress;
	}
	public void setInvoiceAddress(String invoiceAddress) {
		InvoiceAddress = invoiceAddress;
	}
	@JSONField(name = "InvoiceTelphone")
	public String getInvoiceTelphone() {
		return InvoiceTelphone;
	}
	public void setInvoiceTelphone(String invoiceTelphone) {
		InvoiceTelphone = invoiceTelphone;
	}
	@JSONField(name = "DeliveryMan")
	public String getDeliveryMan() {
		return DeliveryMan;
	}
	public void setDeliveryMan(String deliveryMan) {
		DeliveryMan = deliveryMan;
	}
	@JSONField(name = "ExactDeliveryMan")
	public String getExactDeliveryMan() {
		return ExactDeliveryMan;
	}

	public void setExactDeliveryMan(String exactDeliveryMan) {
		ExactDeliveryMan = exactDeliveryMan;
	}
	@JSONField(name = "DeliveryAddress")
	public String getDeliveryAddress() {
		return DeliveryAddress;
	}
	public void setDeliveryAddress(String deliveryAddress) {
		DeliveryAddress = deliveryAddress;
	}
	@JSONField(name = "DeliveryTelphone")
	public String getDeliveryTelphone() {
		return DeliveryTelphone;
	}
	public void setDeliveryTelphone(String deliveryTelphone) {
		DeliveryTelphone = deliveryTelphone;
	}
	@JSONField(name = "InvoiceDeliveryMan")
	public String getInvoiceDeliveryMan() {
		return InvoiceDeliveryMan;
	}
	public void setInvoiceDeliveryMan(String invoiceDeliveryMan) {
		InvoiceDeliveryMan = invoiceDeliveryMan;
	}
	@JSONField(name = "InvoiceDeliveryAddress")
	public String getInvoiceDeliveryAddress() {
		return InvoiceDeliveryAddress;
	}
	public void setInvoiceDeliveryAddress(String invoiceDeliveryAddress) {
		InvoiceDeliveryAddress = invoiceDeliveryAddress;
	}
	@JSONField(name = "InvoiceDeliveryTelphone")
	public String getInvoiceDeliveryTelphone() {
		return InvoiceDeliveryTelphone;
	}
	public void setInvoiceDeliveryTelphone(String invoiceDeliveryTelphone) {
		InvoiceDeliveryTelphone = invoiceDeliveryTelphone;
	}
	@JSONField(name = "BaDjBz")
	public String getBaDjBz() {
		return BaDjBz;
	}
	public void setBaDjBz(String baDjBz) {
		BaDjBz = baDjBz;
	}
	@JSONField(name = "BaNbXsBh")
	public String getBaNbXsBh() {
		return BaNbXsBh;
	}
	public void setBaNbXsBh(String baNbXsBh) {
		BaNbXsBh = baNbXsBh;
	}
	@JSONField(name = "Guser")
	public String getGuser() {
		return Guser;
	}
	public void setGuser(String guser) {
		Guser = guser;
	}
	@JSONField(name = "Buser")
	public String getBuser() {
		return Buser;
	}
	public void setBuser(String buser) {
		Buser = buser;
	}
	@JSONField(name = "SumHtSl")
	public String getSumHtSl() {
		return SumHtSl;
	}
	public void setSumHtSl(String sumHtSl) {
		SumHtSl = sumHtSl;
	}
	@JSONField(name = "SumCkSl")
	public String getSumCkSl() {
		return SumCkSl;
	}
	public void setSumCkSl(String sumCkSl) {
		SumCkSl = sumCkSl;
	}
	@JSONField(name = "SumKpSl")
	public String getSumKpSl() {
		return SumKpSl;
	}
	public void setSumKpSl(String sumKpSl) {
		SumKpSl = sumKpSl;
	}
	@JSONField(name = "ProduceList")
	public List<Produce> getProduceList() {
		return ProduceList;
	}
	public void setProduceList(List<Produce> produceList) {
		ProduceList = produceList;
	}
	@JSONField(name = "GaKhBID")
	public String getGaKhBID() {
		return GaKhBID;
	}
	public void setGaKhBID(String gaKhBID) {
		GaKhBID = gaKhBID;
	}
	@JSONField(name = "goodsAmount")
	public BigDecimal getGoodsAmount() {
		return goodsAmount;
	}
	public void setGoodsAmount(BigDecimal goodsAmount) {
		this.goodsAmount = goodsAmount;
	}
	@JSONField(name = "realAmount")
	public BigDecimal getRealAmount() {
		return realAmount;
	}
	public void setRealAmount(BigDecimal realAmount) {
		this.realAmount = realAmount;
	}
	@JSONField(name = "contractDiscount")
	public BigDecimal getContractDiscount() {
		return contractDiscount;
	}
	public void setContractDiscount(BigDecimal contractDiscount) {
		if(contractDiscount!=null && contractDiscount.compareTo(BigDecimal.ZERO) > 0){
			this.contractDiscount = contractDiscount;
		}else{
			this.contractDiscount =  realAmount.divide(goodsAmount,4,BigDecimal.ROUND_HALF_UP);
		}
	}
	@JSONField(name = "consignAddress")
	public String getConsignAddress() {
		return consignAddress;
	}
	public void setConsignAddress(String consignAddress) {
		this.consignAddress = consignAddress;
	}
}
