package bmatser.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import bmatser.pageModel.util.MakeOrderGoodsPageUtil;

public class OrderGoods {
    private Long id;

    private Long orderId;

    private Long goodsId;

    private Long sellerGoodsId;

    private Integer num;

    private BigDecimal unitPrice;

    private String goodsDesc;

    private BigDecimal salePrice;

    private BigDecimal saleUnitPrice;

    private BigDecimal costPrice;
    
    private String title;
    
    private String goodsNo;
    private String buyNo;
    
    private BigDecimal tail;

    private  int goodsType;
    	
    private String activityId;
    private BigDecimal price;
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getSellerGoodsId() {
        return sellerGoodsId;
    }

    public void setSellerGoodsId(Long sellerGoodsId) {
        this.sellerGoodsId = sellerGoodsId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc == null ? null : goodsDesc.trim();
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getSaleUnitPrice() {
        return saleUnitPrice;
    }

    public void setSaleUnitPrice(BigDecimal saleUnitPrice) {
        this.saleUnitPrice = saleUnitPrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getBuyNo() {
		return buyNo;
	}

	public void setBuyNo(String buyNo) {
		this.buyNo = buyNo;
	}

	public BigDecimal getTail() {
		return tail;
	}

	public void setTail(BigDecimal tail) {
		this.tail = tail;
	}

	public int getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	
	public void saveMakeOrderGoods(MakeOrderGoodsPageUtil goodsMap,int cash){
		String activityId = String.valueOf(goodsMap.getActivityId());
		this.setGoodsId(goodsMap.getGoodsId());
		this.setSellerGoodsId(goodsMap.getSellerGoodsId());
		this.setNum(goodsMap.getNum());
		BigDecimal unitPriceBd = goodsMap.getSalePrice();
		BigDecimal costPrice = goodsMap.getCostPrice();
		this.setUnitPrice(unitPriceBd);
		Double saleUnitPrice = 0D;
		this.setActivityId(StringUtils.isNotBlank(activityId)?activityId:"0");
		if(cash>0){
			saleUnitPrice = goodsMap.getSaleUnitPrice().doubleValue();
			this.setTail(goodsMap.getTail());
		}else{
			saleUnitPrice = goodsMap.getSalePrice().doubleValue();
		}
		BigDecimal saleUnitPriceBd = new BigDecimal(saleUnitPrice);
		this.setSaleUnitPrice(saleUnitPriceBd);
		BigDecimal salePriceBd = new BigDecimal(saleUnitPrice*num).setScale(2, BigDecimal.ROUND_HALF_UP);    
		this.setSalePrice(salePriceBd);
		this.setPrice(costPrice);
	}

}