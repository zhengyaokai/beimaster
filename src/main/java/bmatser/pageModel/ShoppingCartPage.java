package bmatser.pageModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
@ApiModel(value="购物车参数",description="购物车参数")
public class ShoppingCartPage implements Serializable {
	
	@ApiModelProperty(value="购物车Id")
	private Integer id;
    private Integer buyerId;
	@ApiModelProperty(value="数量")
    private Integer num;
	@ApiModelProperty(value="供应商Id")
    private Integer sellerId;
	@ApiModelProperty(value="商品Id")
    private String sellerGoodsId;
	@ApiModelProperty(value="售价")
    private String salePrice;

    private String activityPrice;

    private String status;
    private Date createTime;
	@ApiModelProperty(value="店铺名称")
    private String shopName;
	@ApiModelProperty(value="更新数量")
    private int uNum;
    private List goodsList;
	@ApiModelProperty(value="别名")
    private String alias;
	@ApiModelProperty(value="1是 0否 团购")
    private int activityType; //1加入团购
	@ApiModelProperty(value="活动Id")
    private String activityId;
	@ApiModelProperty(value="套餐Id")
	private String packageId;
	
	private String[] sellingGoodIds;
	
	private Integer[] addNums;

	public String getActivityPrice() {
		return activityPrice;
	}

	public void setActivityPrice(String activityPrice) {
		this.activityPrice = activityPrice;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

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

	public Integer getNum() {
		if(this.num == null){
			return 1;
		}
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getSellerId() {
		return sellerId;
	}

	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}



	public String getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public List getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List goodsList) {
		this.goodsList = goodsList;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}


	public int getuNum() {
		return uNum;
	}

	public void setuNum(int uNum) {
		this.uNum = uNum;
	}

	public int getActivityType() {
		return activityType;
	}

	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getSellerGoodsId() {
		return sellerGoodsId;
	}

	public void setSellerGoodsId(String sellerGoodsId) {
		this.sellerGoodsId = sellerGoodsId;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String[] getSellingGoodIds() {
		return sellingGoodIds;
	}

	public void setSellingGoodIds(String[] sellingGoodIds) {
		this.sellingGoodIds = sellingGoodIds;
	}

	public Integer[] getAddNums() {
		return addNums;
	}

	public void setAddNums(Integer[] addNums) {
		this.addNums = addNums;
	}



    
}
