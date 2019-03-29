package bmatser.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import bmatser.util.DESCoder;

public class JDOrder {
	
	private String thirdOrder;
	private List<JSONObject> sku;
	private String name;//收货人
	private String province;//一级地址
	private String city;
	private String county;
	private String town;
	private String address;//详细地址
	private String mobile;
	private String email;//邮箱
	private String invoiceState="2";//开票方式(1为随货开票，0为订单预借，2为集中开票 )
	private String invoiceType="2";//1普通发票2增值税发票;
	//private String selectedInvoiceTitle;//4个人，5单位
	//private String companyName;//发票抬头  (如果selectedInvoiceTitle=5则此字段Y)
	//private String invoiceContent = "1";//1:明细，3：电脑配件，19:耗材，22：办公用品
	private String paymentType="4";//1：货到付款，2：邮局付款，4：在线支付（余额支付），5：公司转账，6：银行转账，7：网银钱包， 101：金采支付
	private String isUseBalance="1";//预存款【即在线支付（余额支付）】下单固定1 使用余额非预存款下单固定0 不使用余额
	private String submitState ="0";//是否预占库存，0是预占库存（需要调用确认订单接口），1是不预占库存
	//private String invoiceName;//增值票收票人姓名 备注：当invoiceType=2 且invoiceState=1时则此字段必填
	//private String invoicePhone;//增值票收票人电话
	//private String invoiceProvice;//增值票收票人所在省(京东地址编码)
	//private String invoiceCity;//增值票收票人所在市(京东地址编码)
	//private String invoiceCounty;//增值票收票人所在区/县(京东地址编码);
	//private String invoiceAddress;//增值票收票人所在地址
	private String remark="";//备注
	
	
	public String getThirdOrder() {
		return thirdOrder;
	}
	public void setThirdOrder(String thirdOrder) {
		this.thirdOrder = thirdOrder;
	}

	public List<JSONObject> getSku() {
		return sku;
	}
	public void setSku(List<JSONObject> sku) {
		this.sku = sku;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getInvoiceState() {
		return invoiceState;
	}
	public void setInvoiceState(String invoiceState) {
		this.invoiceState = invoiceState;
	}

/*	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}*/
	public String getIsUseBalance() {
		return isUseBalance;
	}
	public void setIsUseBalance(String isUseBalance) {
		this.isUseBalance = isUseBalance;
	}
	public String getSubmitState() {
		return submitState;
	}
	public void setSubmitState(String submitState) {
		this.submitState = submitState;
	}

	/**
	 * 保存JSon地址到订单
	 * @param addrJson
	 * @throws Exception 
	 */
	public void setJsonAddr(String addrJson) throws Exception {
		DESCoder.instanceMobile();
		JSONObject jo = JSONObject.parseObject(addrJson);
		this.setName(jo.getString("consignee"));
		this.setProvince(jo.getString("provinceId"));
		this.setCity(jo.getString("cityId"));
		this.setCounty(jo.getString("areaId"));
		this.setTown(jo.getString("townId"));
		//this.setTown("0");
		this.setAddress(jo.getString("address"));
		this.setMobile(DESCoder.decoder(jo.getString("mobile")));
		this.setEmail("liuyj@gdbmromall.com");
	}
	public void setJsonAddr(Map<String, String> addr) throws Exception {
		DESCoder.instanceMobile();
		this.setName(addr.get("consignee"));
		this.setProvince(addr.get("provinceId"));
		this.setCity(addr.get("cityId"));
		this.setCounty(addr.get("areaId"));
		this.setTown(addr.get("townId"));
		this.setAddress(addr.get("address"));
		this.setMobile(DESCoder.decoder(addr.get("mobile")));
		this.setEmail("liuyj@gdbmromall.com");
	}
	public void setSkuList(List<Map<String, String>> goods) {
		if(this.sku == null){
			this.sku = new ArrayList<>();
		}
		for (Map<String, String> map : goods) {
			JSONObject jo = new JSONObject();
			jo.put("skuId", map.get("skuid"));
			jo.put("num", map.get("num"));
			this.sku.add(jo);
		}
		
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	
	
	
	
	

}
