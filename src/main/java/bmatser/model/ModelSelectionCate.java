package bmatser.model;

public class ModelSelectionCate {
	private Integer id;
	private Integer cateId;
	private String cateName;
	private Integer parentId;
	private Integer isRootNode;
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
}
