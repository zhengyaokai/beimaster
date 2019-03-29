package bmatser.model;

import java.util.Date;

public class Invoice {
    private Integer id;

    private Integer buyerId;

    private String invType;

    private String invTitle;

    private String invContent;

    private String taxpayeRno;

    private String regAddress;

    private String regTelphone;

    private String bank;

    private String bankAccount;

    private String recvName;

    private String recvMobile;

    private Integer recvProvince;

    private Integer recvCity;

    private Integer recvArea;

    private String recvAddress;

    private String isDefault;

    private Date createTime;

    private String status;

    private String isManual;

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

    public String getInvType() {
        return invType;
    }

    public void setInvType(String invType) {
        this.invType = invType == null ? null : invType.trim();
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

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault == null ? null : isDefault.trim();
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

    public String getIsManual() {
        return isManual;
    }

    public void setIsManual(String isManual) {
        this.isManual = isManual == null ? null : isManual.trim();
    }
}