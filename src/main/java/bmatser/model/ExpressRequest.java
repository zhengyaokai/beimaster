package bmatser.model;

import com.alibaba.fastjson.JSONObject;
import com.sf.openapi.express.sample.order.dto.CargoInfoDto;
import com.sf.openapi.express.sample.order.dto.DeliverConsigneeInfoDto;
import com.sf.openapi.express.sample.order.dto.OrderReqDto;

/**
 * @ProjectName: gdb_serviceApi
 * @Package: bmatser.util
 * @ClassName: ExpressRequest
 * @Description: 快递接口请求参数类
 * @Author: zhengyaokai
 * @CreateDate: 2018/8/2 15:47
 */
public class ExpressRequest {

    private String orderId; //客户订单号(顺丰String(56))
    private String ExpressCompany; //物流/快递公司名称 SF/ZTO/STO
    private Integer parcelQuantity; //包裹数，一个包裹对应一个运单号
    private String remark; //运单备注 顺丰String(100) 最大长度30个汉字
    private Integer sellerId;//发件人ID号
    private String logisticsCode;//物流单号

    private String deliverCompany=""; //发件人公司名称
    private String deliverTel; //发件人公司电话
    private String deliverCountry; //发件人公司所在国家
    private String deliverProvince; //发件人公司所在省份
    private String deliverCity; //发件人公司所在城市
    private String deliverArea; //发件人公司联系地址所在辖区
    private String deliverAddress; //发件公司联系详细地址
    private String deliverLinkMan; //发件公司联系人姓名
    private String deliverLinkTel; //发件公司联系人电话

    private String consignCompany; //收件人公司名称
    private String consignLinkMan; //收件公司联系人姓名
    private String consignLinkTel; //收件公司联系人电话
    private String consignCountry; //收件人公司所在国家
    private String consignProvince;//收件人公司所在国家
    private String consignCity;//收件人公司所在城市
    private String consignArea;//收件人公司所在区
    private String consignAddress;//收件人公司所在街道
    private String consignConsignee;//收件人姓名
    private String consignMobile;//收件人手机号

    /**
     * cargoInfo 顺丰货物信息;
     * 货物名称，如果有多个货物，以英文逗号分隔，如：“手机,IPAD,充电器”(String(4000))
     */
    private String cargo = "工业品电子元器件";


    public ExpressRequest() {
    }

    public ExpressRequest(ExpressRequestWapper requestWapper) {
        this.orderId = requestWapper.getOrderId();
        this.sellerId = requestWapper.getSellerId();
        this.logisticsCode = requestWapper.getLogisticsCode();
        this.deliverCompany = requestWapper.getDeliverCompany();
        this.deliverTel = requestWapper.getDeliverTel();
        this.deliverAddress = requestWapper.getDeliverAddress();
        this.deliverLinkMan = requestWapper.getDeliverLinkMan();
        this.deliverLinkTel = requestWapper.getDeliverLinkTel();
        this.consignCompany = requestWapper.getConsignCompany();
        this.consignLinkMan = requestWapper.getConsignLinkMan();
        this.consignLinkTel = requestWapper.getConsignLinkTel();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getExpressCompany() {
        return ExpressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        ExpressCompany = expressCompany;
    }



    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Integer getParcelQuantity() {
        return parcelQuantity;
    }

    public void setParcelQuantity(Integer parcelQuantity) {
        this.parcelQuantity = parcelQuantity;
    }

    public String getDeliverCompany() {
        return deliverCompany;
    }

    public void setDeliverCompany(String deliverCompany) {
        this.deliverCompany = deliverCompany;
    }

    public String getDeliverTel() {
        return deliverTel;
    }

    public void setDeliverTel(String deliverTel) {
        this.deliverTel = deliverTel;
    }

    public String getDeliverCountry() {
        return deliverCountry;
    }

    public void setDeliverCountry(String deliverCountry) {
        this.deliverCountry = deliverCountry;
    }

    public String getDeliverProvince() {
        return deliverProvince;
    }

    public void setDeliverProvince(String deliverProvince) {
        this.deliverProvince = deliverProvince;
    }

    public String getDeliverCity() {
        return deliverCity;
    }

    public void setDeliverCity(String deliverCity) {
        this.deliverCity = deliverCity;
    }

    public String getDeliverArea() {
        return deliverArea;
    }

    public void setDeliverArea(String deliverArea) {
        this.deliverArea = deliverArea;
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
        return deliverLinkTel;
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
        return consignLinkTel;
    }

    public void setConsignLinkTel(String consignLinkTel) {
        this.consignLinkTel = consignLinkTel;
    }

    public String getConsignCountry() {
        return consignCountry;
    }

    public void setConsignCountry(String consignCountry) {
        this.consignCountry = consignCountry;
    }

    public String getConsignProvince() {
        return consignProvince;
    }

    public void setConsignProvince(String consignProvince) {
        this.consignProvince = consignProvince;
    }

    public String getConsignCity() {
        return consignCity;
    }

    public void setConsignCity(String consignCity) {
        this.consignCity = consignCity;
    }

    public String getConsignArea() {
        return consignArea;
    }

    public void setConsignArea(String consignArea) {
        this.consignArea = consignArea;
    }

    public String getConsignAddress() {
        return consignAddress;
    }

    public void setConsignAddress(String consignAddress) {
        this.consignAddress = consignAddress;
    }

    public String getConsignConsignee() {
        return consignConsignee;
    }

    public void setConsignConsignee(String consignConsignee) {
        this.consignConsignee = consignConsignee;
    }

    public String getConsignMobile() {
        return consignMobile;
    }

    public void setConsignMobile(String consignMobile) {
        this.consignMobile = consignMobile;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
