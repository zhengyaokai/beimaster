package bmatser.pageModel;

public class Adress {
	
	private String province;
	private String city;
	private String area;
	private String town;
	private String consignee;
	private String address;
	private String mobile;
	
	
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@Override
	public String toString() {
		return "Adress [province=" + province + ", city=" + city + ", area="
				+ area + ", town=" + town + ", consignee=" + consignee
				+ ", address=" + address + ", mobile=" + mobile + "]";
	}
	
	
	
	
}
