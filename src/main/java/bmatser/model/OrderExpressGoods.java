package bmatser.model;

public class OrderExpressGoods {
    private Long id;

    private Integer orderExpressId;

    private String syncCode;

    private Integer num;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrderExpressId() {
        return orderExpressId;
    }

    public void setOrderExpressId(Integer orderExpressId) {
        this.orderExpressId = orderExpressId;
    }

    public String getSyncCode() {
        return syncCode;
    }

    public void setSyncCode(String syncCode) {
        this.syncCode = syncCode == null ? null : syncCode.trim();
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}