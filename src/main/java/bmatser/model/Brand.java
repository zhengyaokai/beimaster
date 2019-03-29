package bmatser.model;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
@ApiModel(value="品牌",description="品牌")
public class Brand {
    private Integer id;
    @ApiModelProperty(value="中文名")
    private String nameCn;
    @ApiModelProperty(value="英文名称")
    private String nameEn;
    @ApiModelProperty(value="品牌LOGO，相对路径")
    private String logo;
    @ApiModelProperty(value="品牌故事(说明)")
    private String mark;
    @ApiModelProperty(value="品牌创始人")
    private String maker;
    @ApiModelProperty(value="品牌创始时间(非本条记录的创建时间)")
    private Date makeDate;
    @ApiModelProperty(value="品牌官方网址")
    private String web;
    @ApiModelProperty(value="品牌类型（1：国际品牌  2：国内品牌）")
    private String type;
    @ApiModelProperty(value="排序(越大越靠前)")
    private Integer orderSn;
    @ApiModelProperty(value="该品牌提示(如3m的劳保)")
    private String tips;
    @ApiModelProperty(value="品牌所属国家id")
    private Integer countryId;
    @ApiModelProperty(value="状态（1：有效  2：删除）")
    private String status;
    @ApiModelProperty(value="添加人，关联user用户表")
    private Integer createUserId;
    @ApiModelProperty(value="添加时间")
    private Date createTime;
    @ApiModelProperty(value="最后修改人ID，关联user用户表")
    private Integer modifyUserId;
    @ApiModelProperty(value="手机")
    private String telphone;
    @ApiModelProperty(value="QQ")
    private String qq;
    @ApiModelProperty(value="最后修改时间")
    private Date modifyTime;
    @ApiModelProperty(value="名称")
    private String name;
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn == null ? null : nameCn.trim();
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn == null ? null : nameEn.trim();
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark == null ? null : mark.trim();
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker == null ? null : maker.trim();
    }

    public Date getMakeDate() {
        return makeDate;
    }

    public void setMakeDate(Date makeDate) {
        this.makeDate = makeDate;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web == null ? null : web.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(Integer orderSn) {
        this.orderSn = orderSn;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips == null ? null : tips.trim();
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(Integer modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone == null ? null : telphone.trim();
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}