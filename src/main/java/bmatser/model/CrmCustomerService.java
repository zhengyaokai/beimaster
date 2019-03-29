package bmatser.model;

import java.sql.Timestamp;

public class CrmCustomerService {
    private Integer id;

    private Integer customerId;

    private Integer serviceId;

    private Timestamp createTime;

    private Integer createStaffId;

    private Timestamp endTime;

    private Integer endStaffId;

    private String status;
    
    private String isAutoAssign;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getCreateStaffId() {
        return createStaffId;
    }

    public void setCreateStaffId(Integer createStaffId) {
        this.createStaffId = createStaffId;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Integer getEndStaffId() {
        return endStaffId;
    }

    public void setEndStaffId(Integer endStaffId) {
        this.endStaffId = endStaffId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

	public String getIsAutoAssign() {
		return isAutoAssign;
	}

	public void setIsAutoAssign(String isAutoAssign) {
		this.isAutoAssign = isAutoAssign;
	}
    
}