package bmatser.util;

import java.io.Serializable;

public class SessionInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3647646092214274725L;

	private Integer staffId;
	
	private String staffName;
	
	private Integer roleId;
	
	private Integer roleNo;

	public Integer getStaffId() {
		return staffId;
	}

	public void setStaffId(Integer staffId) {
		this.staffId = staffId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getRoleNo() {
		return roleNo;
	}

	public void setRoleNo(Integer roleNo) {
		this.roleNo = roleNo;
	}
	
}
