package bmatser.model;

import org.apache.http.HttpStatus;

/**
 * @ProjectName: gdb_serviceApi
 * @Package: bmatser.util
 * @ClassName: ExpressResponse
 * @Description: 快递接口返回参数包装类
 * @Author: zhengyaokai
 * @CreateDate: 2018/8/2 15:50
 */
public class ExpressResponse  {
    private String msg;
    private String orderId;
    private String expressCompany;
    private String expressOrderId;
    private String status;
    private String logisticsRemark;


    public ExpressResponse() {
    }

    public ExpressResponse(String expressCompany, String orderId, String logisticsRemark) {
        this.expressCompany = expressCompany;
        this.orderId = orderId;
        this.logisticsRemark = logisticsRemark;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    public String getExpressOrderId() {
        return expressOrderId;
    }

    public void setExpressOrderId(String expressOrderId) {
        this.expressOrderId = expressOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogisticsRemark() {
        return logisticsRemark;
    }

    public void setLogisticsRemark(String logisticsRemark) {
        this.logisticsRemark = logisticsRemark;
    }
}
