package bmatser.model;

import bmatser.util.DESCoder;

/**
 * @ProjectName: gdb_serviceApi
 * @Package: bmatser.model
 * @ClassName: ExpressConsignAddressInfo
 * @Description: 用于order_info 表consignAddress 字段的json转换
 * @Author: zhengyaokai
 * @CreateDate: 2018/8/6 13:35
 */
public class ExpressConsignAddressInfo {
    private Integer provinceId;
    private Integer cityId;
    private Integer areaId;
    private String address;
    private String consignee;
    private String mobile;

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getMobile() {
        try {
            return DESCoder.decoder(mobile);
        } catch (Exception e) {
            return mobile;
        }
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
