package bmatser.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import bmatser.util.ResponseMsg;

@ApiModel(value="分类",description="分类")
public class Category implements Serializable {

	private static final long serialVersionUID = 1347316889473524604L;

	private Integer id;
	@ApiModelProperty(value="分类编号")
    private String cateNo;
    @ApiModelProperty(value="分类名称")
    private String name;
    @ApiModelProperty(value="上级类型ID")
    private Integer parentId;
    @ApiModelProperty(value="是否末级节点（0：否  1：是）")
    private Boolean isLeaf;
    @ApiModelProperty(value="图标路径")
    private String icon;
    @ApiModelProperty(value="描述")
    private String brief;
    @ApiModelProperty(value="状态（1：有效  2：删除）")
    private String status;
    @ApiModelProperty(value="添加人，关联user用户表")
    private Integer createUserId;
    @ApiModelProperty(value="添加时间")
    private Date createTime;
    @ApiModelProperty(value="最后修改人ID，关联user用户表")
    private Integer modifyUserId;
    @ApiModelProperty(value="最后修改时间")
    private Date modifyTime;
    @ApiModelProperty(value="是否有商品 0：没商品 1:有商品  2:有商城商品")
    private String hasGoods;
    @ApiModelProperty(value="是否有基础数据")
    private String hasInitialGoods;
    @ApiModelProperty(value="分类短名")
    private String  shortName;
    @ApiModelProperty(value="")
    private List<Category> childrens = new ArrayList<Category>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCateNo() {
        return cateNo;
    }

    public void setCateNo(String cateNo) {
        this.cateNo = cateNo == null ? null : cateNo.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief == null ? null : brief.trim();
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

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getHasGoods() {
        return hasGoods;
    }

    public void setHasGoods(String hasGoods) {
        this.hasGoods = hasGoods == null ? null : hasGoods.trim();
    }

    public String getHasInitialGoods() {
        return hasInitialGoods;
    }

    public void setHasInitialGoods(String hasInitialGoods) {
        this.hasInitialGoods = hasInitialGoods == null ? null : hasInitialGoods.trim();
    }
    
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public List<Category> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<Category> childrens) {
		this.childrens = childrens;
	}

}