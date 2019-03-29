package bmatser.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sf.openapi.common.entity.BaseEntity;
import com.sf.openapi.express.sample.order.dto.OrderRespDto;

/**
 * @ProjectName: gdb_serviceApi
 * @Package: bmatser.util
 * @ClassName: SfOrderResponse
 * @Description: 顺丰快递返回参数包装类
 * @Author: zhengyaokai
 * @CreateDate: 2018/8/3 10:51
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SfOrderResponse extends BaseEntity {


    private String orderId;
    private String filterLevel;
    private String orderTriggerCondition;
    private String remarkCode;

    public SfOrderResponse() {
    }
    public SfOrderResponse(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFilterLevel() {
        return this.filterLevel;
    }

    public void setFilterLevel(String filterLevel) {
        this.filterLevel = filterLevel;
    }

    public String getOrderTriggerCondition() {
        return this.orderTriggerCondition;
    }

    public void setOrderTriggerCondition(String orderTriggerCondition) {
        this.orderTriggerCondition = orderTriggerCondition;
    }

    public String getRemarkCode() {
        return remarkCode;
    }

    public void setRemarkCode(String remarkCode) {
        this.remarkCode = remarkCode;
    }
}
