package bmatser.model;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;


public class Produce {

	@JSONField(name = "GoodId")
	private Long GoodId;//GDB商品ID
	@JSONField(name = "Gbrand")
	private String Gbrand;//品牌
	@JSONField(name = "Gserial")
	private String Gserial;//系列
	@JSONField(name = "GID")
	private String GID;//型号
	@JSONField(name = "Gname")
	private String Gname;//品名
	@JSONField(name = "Gindex")
	private String Gindex;//订货号
	@JSONField(name = "GaProID")
	private Integer GaProID;//产品编码
	@JSONField(name = "Gunit")
	private String Gunit;//计量单位
	@JSONField(name = "Bnumb")
	private BigDecimal Bnumb;//合同数量
	@JSONField(name = "BfHtZk")
	private BigDecimal BfHtZk;//合同折扣
	@JSONField(name = "Bprice")
	private BigDecimal Bprice;//合同单价
	@JSONField(name = "BfCalJe")
	private BigDecimal BfCalJe;//合同金额
	@JSONField(name = "BaHtHq")
	private String BaHtHq;//合同货期
	@JSONField(name = "BaBz")
	private String BaBz;//合同备注
	@JSONField(name = "BdYjJhq")
	private String BdYjJhq;//预计交货日期
	@JSONField(name = "BfYgCb")
	private BigDecimal BfYgCb;//预估成本
	@JSONField(name = "BaYfCd")
	private String BaYfCd;//运费承担

	@JSONField(name = "GoodId")
	public Long getGoodId() {
		return GoodId;
	}
	public void setGoodId(Long goodId) {
		GoodId = goodId;
	}
	@JSONField(name = "Gbrand")
	public String getGbrand() {
		return Gbrand;
	}
	public void setGbrand(String gbrand) {
		Gbrand = gbrand;
	}
	@JSONField(name = "Gserial")
	public String getGserial() {
		return Gserial;
	}
	public void setGserial(String gserial) {
		Gserial = gserial;
	}
	@JSONField(name = "GID")
	public String getGID() {
		return GID;
	}
	public void setGID(String gID) {
		GID = gID;
	}
	@JSONField(name = "Gname")
	public String getGname() {
		return Gname;
	}
	public void setGname(String gname) {
		Gname = gname;
	}
	@JSONField(name = "Gindex")
	public String getGindex() {
		return Gindex;
	}
	public void setGindex(String gindex) {
		Gindex = gindex;
	}
	@JSONField(name = "GaProID")
	public Integer getGaProID() {
		return GaProID;
	}
	public void setGaProID(Integer gaProID) {
		GaProID = gaProID;
	}
	@JSONField(name = "Gunit")
	public String getGunit() {
		return Gunit;
	}
	public void setGunit(String gunit) {
		Gunit = gunit;
	}
	@JSONField(name = "Bnumb")
	public BigDecimal getBnumb() {
		return Bnumb;
	}
	public void setBnumb(BigDecimal bnumb) {
		Bnumb = bnumb;
	}
	@JSONField(name = "Bprice")
	public BigDecimal getBprice() {
		return Bprice;
	}
	public void setBprice(BigDecimal bprice) {
		Bprice = bprice;
	}
	@JSONField(name = "BfCalJe")
	public BigDecimal getBfCalJe() {
		return BfCalJe;
	}
	public void setBfCalJe(BigDecimal bfCalJe) {
		BfCalJe = bfCalJe;
	}
	@JSONField(name = "BaHtHq")
	public String getBaHtHq() {
		return BaHtHq;
	}
	public void setBaHtHq(String baHtHq) {
		BaHtHq = baHtHq;
	}
	@JSONField(name = "BaBz")
	public String getBaBz() {
		return BaBz;
	}
	public void setBaBz(String baBz) {
		BaBz = baBz;
	}
	@JSONField(name = "BaYfCd")
	public String getBaYfCd() {
		return BaYfCd;
	}
	public void setBaYfCd(String baYfCd) {
		BaYfCd = baYfCd;
	}
	@JSONField(name = "BdYjJhq")
	public String getBdYjJhq() {
		return BdYjJhq;
	}
	public void setBdYjJhq(String bdYjJhq) {
		BdYjJhq = bdYjJhq;
	}
	@JSONField(name = "BfYgCb")
	public BigDecimal getBfYgCb() {
		return BfYgCb;
	}
	public void setBfYgCb(BigDecimal bfYgCb) {
		BfYgCb = bfYgCb;
	}
	@JSONField(name = "BfHtZk")
	public BigDecimal getBfHtZk() {
		return BfHtZk;
	}
	public void setBfHtZk(BigDecimal bfHtZk) {
		BfHtZk = bfHtZk;
	}
}
