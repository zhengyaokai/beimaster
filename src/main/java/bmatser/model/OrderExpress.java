package bmatser.model;

import java.util.Date;

public class OrderExpress {
    private Integer id;

    private String shopName;//店铺id 46:工电宝品牌分销店  47:工电宝供应链平台  48:工电宝商城

    private Long originalOrderId;

    private String fexpcomname;

    private String fexpNo;

    private String fname;

    private Date fexpprttime;

    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public Long getOriginalOrderId() {
        return originalOrderId;
    }

    public void setOriginalOrderId(Long originalOrderId) {
        this.originalOrderId = originalOrderId;
    }

    public String getFexpcomname() {
        return fexpcomname;
    }

    public void setFexpcomname(String fexpcomname) {
        this.fexpcomname = fexpcomname == null ? null : fexpcomname.trim();
    }

    public String getFexpNo() {
        return fexpNo;
    }

    public void setFexpNo(String fexpNo) {
        this.fexpNo = fexpNo == null ? null : fexpNo.trim();
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname == null ? null : fname.trim();
    }

    public Date getFexpprttime() {
        return fexpprttime;
    }

    public void setFexpprttime(Date fexpprttime) {
        this.fexpprttime = fexpprttime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}