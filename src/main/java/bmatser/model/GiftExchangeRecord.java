package bmatser.model;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;
/**
 * 礼品兑换记录
 * @author felx
 * @date 2016.03.09 
 */
public class GiftExchangeRecord implements Serializable{
	private static final long serialVersionUID = 5746182872029019949L;

	private Long id;

    private Integer giftId;

    private Integer num;

    private Integer score;

    private Integer scoreRemain;

    private Timestamp exchangeTime;

    private Integer dealerId;

    private Integer userId;

    private String mobile;

    private Integer consignAddressId;

    private String consignAddressInfo;

    private Integer sellerLogisticsId;

    private String logisticsCode;

    private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getGiftId() {
		return giftId;
	}

	public void setGiftId(Integer giftId) {
		this.giftId = giftId;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getScoreRemain() {
		return scoreRemain;
	}

	public void setScoreRemain(Integer scoreRemain) {
		this.scoreRemain = scoreRemain;
	}

	public Timestamp getExchangeTime() {
		return exchangeTime;
	}

	public void setExchangeTime(Timestamp exchangeTime) {
		this.exchangeTime = exchangeTime;
	}

	public Integer getDealerId() {
		return dealerId;
	}

	public void setDealerId(Integer dealerId) {
		this.dealerId = dealerId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getConsignAddressId() {
		return consignAddressId;
	}

	public void setConsignAddressId(Integer consignAddressId) {
		this.consignAddressId = consignAddressId;
	}

	public String getConsignAddressInfo() {
		if(StringUtils.isNotBlank(consignAddressInfo)){
			consignAddressInfo=consignAddressInfo.replace("\\\\","\\");
		}
		return consignAddressInfo;
	}

	public void setConsignAddressInfo(String consignAddressInfo) {
		this.consignAddressInfo = consignAddressInfo;
	}

	public Integer getSellerLogisticsId() {
		return sellerLogisticsId;
	}

	public void setSellerLogisticsId(Integer sellerLogisticsId) {
		this.sellerLogisticsId = sellerLogisticsId;
	}

	public String getLogisticsCode() {
		return logisticsCode;
	}

	public void setLogisticsCode(String logisticsCode) {
		this.logisticsCode = logisticsCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

    
   
    
}