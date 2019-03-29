package bmatser.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;

public class ModelSelectionCategory implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7767990535510049839L;
	private Integer id;
	private Integer cateId;
	private String cateName;
	private Integer parentId;
	private Integer isRootNode;

	private String  sb;

    public String getSb() {
        return sb;
    }

    public void setSb(String sb) {
        this.sb = sb;
    }

    private List<ModelSelectionCategory> childrens = Lists.newArrayList();
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCateId() {
		return cateId;
	}
	public void setCateId(Integer cateId) {
		this.cateId = cateId;
	}
	
	public String getCateName() {
		return cateName;
	}
	public void setCateName(String cateName) {
		this.cateName = cateName;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getIsRootNode() {
		return isRootNode;
	}
	public void setIsRootNode(Integer isRootNode) {
		this.isRootNode = isRootNode;
	}
	public List<ModelSelectionCategory> getChildrens() {
		return childrens;
	}
	public void setChildrens(List<ModelSelectionCategory> childrens) {
		this.childrens = childrens;
	}
	
}
