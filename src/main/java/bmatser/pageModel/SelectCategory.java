package bmatser.pageModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bmatser.model.Category;

public class SelectCategory {

    private Integer id;

    private Integer parentId;

//    private String cateNo;

    private String name;


    private String shortName;

    private String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
    //    private String  shortName;

//    private String hasGoods;

//    private String hasInitialGoods;
//    private String hasMallGoods;

    private List<SelectCategory> childrens = new ArrayList<SelectCategory>();

    private List<Map> brand = new ArrayList<Map>();


    public String getShortName() {

        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

//	public String getCateNo() {
//		return cateNo;
//	}
//
//	public void setCateNo(String cateNo) {
//		this.cateNo = cateNo;
//	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//	public String getShortName() {
//		return shortName;
//	}
//
//	public void setShortName(String shortName) {
//		this.shortName = shortName;
//	}

//	public String getHasGoods() {
//		return hasGoods;
//	}
//
//	public void setHasGoods(String hasGoods) {
//		this.hasGoods = hasGoods;
//	}

//	public String getHasInitialGoods() {
//		return hasInitialGoods;
//	}
//
//	public void setHasInitialGoods(String hasInitialGoods) {
//		this.hasInitialGoods = hasInitialGoods;
//	}


    public List<SelectCategory> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<SelectCategory> childrens) {
        this.childrens = childrens;
    }

    public List<Map> getBrand() {
        return brand;
    }

    public void setBrand(List<Map> brand) {
        this.brand = brand;
    }

//	public String getHasMallGoods() {
//		return hasMallGoods;
//	}
//
//	public void setHasMallGoods(String hasMallGoods) {
//		this.hasMallGoods = hasMallGoods;
//	}


}
