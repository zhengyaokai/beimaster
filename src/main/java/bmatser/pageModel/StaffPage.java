package bmatser.pageModel;

import java.io.Serializable;

import bmatser.model.Staff;

public class StaffPage extends Staff implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4083275426946290799L;

	private Integer roleId;
	
	private int page = 0;
	
	private int rows = 10;
	
	private int roleNo;
	
	
	private String createStaffNo;
	
	private String createStaffName;

	private String createStaffMobile;
	
	private String roleName;
	
	private String depatName;
	

	public String getDepatName() {
		return depatName;
	}

	public void setDepatName(String depatName) {
		this.depatName = depatName;
	}

	public int getRoleNo() {
		return roleNo;
	}

	public void setRoleNo(int roleNo) {
		this.roleNo = roleNo;
	}

	public String getCreateStaffNo() {
		return createStaffNo;
	}

	public void setCreateStaffNo(String createStaffNo) {
		this.createStaffNo = createStaffNo;
	}

	public String getCreateStaffName() {
		return createStaffName;
	}

	public void setCreateStaffName(String createStaffName) {
		this.createStaffName = createStaffName;
	}

	public String getCreateStaffMobile() {
		return createStaffMobile;
	}

	public void setCreateStaffMobile(String createStaffMobile) {
		this.createStaffMobile = createStaffMobile;
	}
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	


	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
}
