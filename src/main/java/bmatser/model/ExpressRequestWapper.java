package bmatser.model;

import bmatser.util.DESCoder;

/**
 * @ProjectName: gdb_serviceApi
 * @Package: bmatser.util
 * @ClassName: ExpressRequestWapper
 * @Description: 快递接口请求参数包装类
 * @Author: zhengyaokai
 * @CreateDate: 2018/8/2 15:47
 */
public class ExpressRequestWapper {

    private String orderId; //客户订单号(顺丰String(56))
    private Integer sellerId;//供应商ID，快递发件人Id
    private String logisticsCode;//物流单号

    private String deliverCompany; //发件人公司名称
    private String deliverTel; //发件人公司电话
    private Integer deliverCountryId; //发件人公司所在国家
    private Integer deliverProvinceId; //发件人公司所在省份
    private Integer deliverCityId; //发件人公司所在城市
    private Integer deliverAreaId; //发件人公司联系地址所在辖区
    private String deliverAddress; //发件公司联系详细地址
    private String deliverLinkMan; //发件公司联系人姓名
    private String deliverLinkTel; //发件公司联系人电话
    private String consignCompany; //收件人公司名称
    private String consignLinkMan; //收件公司联系人姓名
    private String consignLinkTel; //收件公司联系人电话
    private String consignAddress; //收件人地址信息 json

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getLogisticsCode() {
        return logisticsCode;
    }

    public void setLogisticsCode(String logisticsCode) {
        this.logisticsCode = logisticsCode;
    }

    public String getDeliverCompany() {
        return deliverCompany;
    }

    public void setDeliverCompany(String deliverCompany) {
        this.deliverCompany = deliverCompany;
    }

    public String getDeliverTel() {
        try {
            return DESCoder.decoder(deliverTel);
        } catch (Exception e) {
            return deliverTel;
        }
    }

    public void setDeliverTel(String deliverTel) {
        this.deliverTel = deliverTel;
    }



    public String getDeliverAddress() {
        return deliverAddress;
    }

    public void setDeliverAddress(String deliverAddress) {
        this.deliverAddress = deliverAddress;
    }

    public String getDeliverLinkMan() {
        return deliverLinkMan;
    }

    public void setDeliverLinkMan(String deliverLinkMan) {
        this.deliverLinkMan = deliverLinkMan;
    }

    public String getDeliverLinkTel() {
        try {
            return DESCoder.decoder(deliverLinkTel);
        } catch (Exception e) {
            return deliverLinkTel;
        }
    }

    public void setDeliverLinkTel(String deliverLinkTel) {
        this.deliverLinkTel = deliverLinkTel;
    }

    public String getConsignCompany() {
        return consignCompany;
    }

    public void setConsignCompany(String consignCompany) {
        this.consignCompany = consignCompany;
    }

    public String getConsignLinkMan() {
        return consignLinkMan;
    }

    public void setConsignLinkMan(String consignLinkMan) {
        this.consignLinkMan = consignLinkMan;
    }

    public String getConsignLinkTel() {
        try {
            return DESCoder.decoder(consignLinkTel);
        } catch (Exception e) {
            return consignLinkTel;
        }
    }

    public void setConsignLinkTel(String consignLinkTel) {
        this.consignLinkTel = consignLinkTel;
    }

    public String getConsignAddress() {
        return consignAddress;
    }

    public void setConsignAddress(String consignAddress) {
        this.consignAddress = consignAddress;
    }

    public Integer getDeliverCountryId() {
        return deliverCountryId;
    }

    public void setDeliverCountryId(Integer deliverCountryId) {
        this.deliverCountryId = deliverCountryId;
    }

    public Integer getDeliverProvinceId() {
        return deliverProvinceId;
    }

    public void setDeliverProvinceId(Integer deliverProvinceId) {
        this.deliverProvinceId = deliverProvinceId;
    }

    public Integer getDeliverCityId() {
        return deliverCityId;
    }

    public void setDeliverCityId(Integer deliverCityId) {
        this.deliverCityId = deliverCityId;
    }

    public Integer getDeliverAreaId() {
        return deliverAreaId;
    }

    public void setDeliverAreaId(Integer deliverAreaId) {
        this.deliverAreaId = deliverAreaId;
    }
}
