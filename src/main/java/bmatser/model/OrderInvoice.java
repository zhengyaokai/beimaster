package bmatser.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class OrderInvoice implements Cloneable{
    private Integer id;

    private Integer buyerId;

    private Long orderId;

    private String invType;

    private BigDecimal invAmount;

    private String invTitle;
    
    private String invTitleType;

    private String invContent;

    private String taxpayeRno;

    private String regAddress;

    private String regTelphone;
    private String regTelphoneSecret;

    private String bank;

    private String bankAccount;

    private String recvName;

    private String recvMobile;
    private String recvMobileSecret;

    private Integer recvProvince;

    private Integer recvCity;

    private Integer recvArea;

    private String recvAddress;

    private Date checkTime;

    private Integer checkUserId;

    private Date createTime;

    private String status;

    public String getRegTelphoneSecret() {
		return regTelphoneSecret;
	}

	public void setRegTelphoneSecret(String regTelphoneSecret) {
		this.regTelphoneSecret = regTelphoneSecret;
	}

	public String getRecvMobileSecret() {
		return recvMobileSecret;
	}

	public void setRecvMobileSecret(String recvMobileSecret) {
		this.recvMobileSecret = recvMobileSecret;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getInvType() {
        return invType;
    }

    public void setInvType(String invType) {
        this.invType = invType == null ? null : invType.trim();
    }

    public BigDecimal getInvAmount() {
        return invAmount;
    }

    public void setInvAmount(BigDecimal invAmount) {
        this.invAmount = invAmount;
    }

    public String getInvTitle() {
        return invTitle;
    }

    public void setInvTitle(String invTitle) {
        this.invTitle = invTitle == null ? null : invTitle.trim();
    }

    public String getInvContent() {
        return invContent;
    }

    public void setInvContent(String invContent) {
        this.invContent = invContent == null ? null : invContent.trim();
    }

    public String getTaxpayeRno() {
        return taxpayeRno;
    }

    public void setTaxpayeRno(String taxpayeRno) {
        this.taxpayeRno = taxpayeRno == null ? null : taxpayeRno.trim();
    }

    public String getRegAddress() {
        return regAddress;
    }

    public void setRegAddress(String regAddress) {
        this.regAddress = regAddress == null ? null : regAddress.trim();
    }

    public String getRegTelphone() {
        return regTelphone;
    }

    public void setRegTelphone(String regTelphone) {
        this.regTelphone = regTelphone == null ? null : regTelphone.trim();
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank == null ? null : bank.trim();
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount == null ? null : bankAccount.trim();
    }

    public String getRecvName() {
        return recvName;
    }

    public void setRecvName(String recvName) {
        this.recvName = recvName == null ? null : recvName.trim();
    }

    public String getRecvMobile() {
        return recvMobile;
    }

    public void setRecvMobile(String recvMobile) {
        this.recvMobile = recvMobile == null ? null : recvMobile.trim();
    }

    public Integer getRecvProvince() {
        return recvProvince;
    }

    public void setRecvProvince(Integer recvProvince) {
        this.recvProvince = recvProvince;
    }

    public Integer getRecvCity() {
        return recvCity;
    }

    public void setRecvCity(Integer recvCity) {
        this.recvCity = recvCity;
    }

    public Integer getRecvArea() {
        return recvArea;
    }

    public void setRecvArea(Integer recvArea) {
        this.recvArea = recvArea;
    }

    public String getRecvAddress() {
        return recvAddress;
    }

    public void setRecvAddress(String recvAddress) {
        this.recvAddress = recvAddress == null ? null : recvAddress.trim();
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Integer getCheckUserId() {
        return checkUserId;
    }

    public void setCheckUserId(Integer checkUserId) {
        this.checkUserId = checkUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

	public String getInvTitleType() {
		return invTitleType;
	}

	public void setInvTitleType(String invTitleType) {
		this.invTitleType = invTitleType;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	public void saveInvoiceMap(Map invoiceMap){
		this.setInvType(invoiceMap.get("invType").toString());
		this.setInvTitle(invoiceMap.get("invTitle").toString());
		this.setTaxpayeRno(invoiceMap.get("taxpayeRno")!=null?invoiceMap.get("taxpayeRno").toString():"");
		this.setRegAddress(invoiceMap.get("regAddress")!=null?invoiceMap.get("regAddress").toString():"");
		this.setRegTelphone(invoiceMap.get("regTelphone")!=null?invoiceMap.get("regTelphone").toString():"");
		this.setRegTelphoneSecret(invoiceMap.get("regTelphoneSecret")==null?null:invoiceMap.get("regTelphoneSecret").toString());
		this.setBank(invoiceMap.get("bank")!=null?invoiceMap.get("bank").toString():"");
		this.setBankAccount(invoiceMap.get("bankAccount")!=null?invoiceMap.get("bankAccount").toString():"");
		this.setRecvName(invoiceMap.get("recvName").toString());
		this.setRecvMobile(invoiceMap.get("recvMobile").toString());
		this.setRecvMobileSecret(invoiceMap.get("recvMobileSecret")==null?"":invoiceMap.get("recvMobileSecret").toString());
		this.setRecvProvince(Integer.parseInt(invoiceMap.get("recvProvince").toString()));
		this.setRecvCity(Integer.parseInt(invoiceMap.get("recvCity").toString()));
		this.setRecvArea(Integer.parseInt(invoiceMap.get("recvArea").toString()));
		this.setRecvAddress(invoiceMap.get("recvAddress").toString());
	}
	
}