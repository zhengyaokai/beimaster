package bmatser.model;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
/**
 * 会员收藏
 * @author felx
 * @date 2016.02.17 
 */
@ApiModel(value="会员收藏")
public class MemberFavorite {
    private Integer id;
    @ApiModelProperty(value="会员ID")
    private Integer dealerId;
    @ApiModelProperty(value="商品ID")
    private Long sellerGoodsId;
    @ApiModelProperty(value="收藏日期")
    private String favDate;
    @ApiModelProperty(value="状态(1/2)")
    private String status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDealerId() {
		return dealerId;
	}

	public void setDealerId(Integer dealerId) {
		this.dealerId = dealerId;
	}

	public Long getSellerGoodsId() {
		return sellerGoodsId;
	}

	public void setSellerGoodsId(Long sellerGoodsId) {
		this.sellerGoodsId = sellerGoodsId;
	}

	public String getFavDate() {
		return favDate;
	}

	public void setFavDate(String favDate) {
		this.favDate = favDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
}