package bmatser.model;

import java.math.BigDecimal;

public class Item {

	private String fskuname;
	private String fouternumber;
	private Integer fqty;
	private BigDecimal fprice;
	private BigDecimal famount;
	public String getFskuname() {
		return fskuname;
	}
	public void setFskuname(String fskuname) {
		this.fskuname = fskuname;
	}
	public String getFouternumber() {
		return fouternumber;
	}
	public void setFouternumber(String fouternumber) {
		this.fouternumber = fouternumber;
	}
	public Integer getFqty() {
		return fqty;
	}
	public void setFqty(Integer fqty) {
		this.fqty = fqty;
	}
	public BigDecimal getFprice() {
		return fprice;
	}
	public void setFprice(BigDecimal fprice) {
		this.fprice = fprice;
	}
	public BigDecimal getFamount() {
		return famount;
	}
	public void setFamount(BigDecimal famount) {
		this.famount = famount;
	}
	
	
}
