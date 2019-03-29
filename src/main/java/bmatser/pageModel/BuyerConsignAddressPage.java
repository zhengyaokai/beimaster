package bmatser.pageModel;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
@ApiModel(value="收货地址")
public class BuyerConsignAddressPage implements Serializable {

    private static final long serialVersionUID = 1L;
	private Integer id;
	@ApiModelProperty(value="dealerId")
    private Integer buyerId;
	@ApiModelProperty(value="国家")
    private Integer countryId;
	@ApiModelProperty(value="省")
    private Integer provinceId;
	@ApiModelProperty(value = "省名称")
	private String provinceName;
	@ApiModelProperty(value="市")
    private Integer cityId;
	@ApiModelProperty(value = "市")
	private String cityName;
	@ApiModelProperty(value="辖区")
    private Integer areaId;
	@ApiModelProperty(value = "区域名称")
	private String areaName;
    @ApiModelProperty(value="详细地址信息")
    private String address;
	@ApiModelProperty(value="收货人")
    private String consignee;
	@ApiModelProperty(value="手机号码")
    private String mobile;

    private String mobileSecret;
	@ApiModelProperty(value="是否默认(0：否  1：是)")
    private String isDefault;
	@ApiModelProperty(value="邮编")
    private String postCode;
	@ApiModelProperty(value="固定电话")
    private String telephone;

    private String telephoneSecret;
	@ApiModelProperty(value="电子邮箱")
    private String email;
	@ApiModelProperty(value="是否手工录入(1为是0,null为不是)")
    private String isManual;
	@ApiModelProperty(value="是否京东数据")
    private int isJd;
	@ApiModelProperty(value="乡镇")
    private Integer townId;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }


    public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
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

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

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
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIsManual() {
		return isManual;
	}

	public void setIsManual(String isManual) {
		this.isManual = isManual;
	}
    
	public String getMobileSecret() {
		return mobileSecret;
	}

	public void setMobileSecret(String mobileSecret) {
		this.mobileSecret = mobileSecret;
	}

	public String getTelephoneSecret() {
		return telephoneSecret;
	}

	public void setTelephoneSecret(String telephoneSecret) {
		this.telephoneSecret = telephoneSecret;
	}

	public void checkData() throws Exception{
		if(StringUtils.isBlank(this.getConsignee())){
			throw new Exception("收货人不能为空");
		}
		if(StringUtils.isBlank(this.getAddress())){
			throw new Exception("地址不能为空");
		}
		if(this.getProvinceId()==null || "".equals(this.getProvinceId())){
			throw new Exception("省不能为空");
		}
		if(this.getCityId()==null || "".equals(this.getCityId())){
			throw new Exception("市不能为空");
		}
		if(this.getAreaId()==null || "".equals(this.getAreaId())){
			throw new Exception("地区不能为空");
		}
		if(this.getTownId()==null){
			this.townId = 0;
		}
/*		if(StringUtils.isBlank(this.getPostCode())){
//			throw new Exception("邮编不能为空");
			this.postCode = "000000";
		}*/
	}

	public int getIsJd() {
		return isJd;
	}

	public void setIsJd(int isJd) {
		this.isJd = isJd;
	}

	public Integer getTownId() {
		return townId;
	}

	public void setTownId(Integer townId) {
		this.townId = townId;
	}
}
