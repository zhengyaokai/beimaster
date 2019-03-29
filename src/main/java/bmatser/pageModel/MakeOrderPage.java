/**
 * 生成确认订单
 */
package bmatser.pageModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import bmatser.pageModel.util.MakeOrderGoodsPageUtil;

public class MakeOrderPage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8320175783625607091L;
	protected String totalGoodsPrice;//商品总金额
	protected String totalCash;//总现金券
	protected String tail;//现金券尾差
	protected String totalGroupPrice;//总团购价格
	protected String groupSale;//团购优惠
	protected String totalScore;//积分
	protected String totalPayPrice;//实付金额（算优惠）
	protected String totalFreight;//运费
	protected String totalOrderPrice;//订单金额（不算优惠）
	protected String totalFullcut;//满减金额
	protected int isCash;//满减金额
	protected int bean;//获得金豆
	protected int useBean;//获得金豆
	protected int dealerCash;//满减金额
	protected Map<String, Object> buyerAddr;//默认地址
	protected Map<String, Object> buyerInv;//默认发票
	protected List<MakeOrderGoodsPageUtil> goods;
	
	
	public String getTotalGoodsPrice() {
		return totalGoodsPrice;
	}
	public void setTotalGoodsPrice(String totalGoodsPrice) {
		this.totalGoodsPrice = totalGoodsPrice;
	}
	public String getTotalCash() {
		return totalCash;
	}
	public void setTotalCash(String totalCash) {
		this.totalCash = totalCash;
	}
	public String getTail() {
		return tail;
	}
	public void setTail(String tail) {
		this.tail = tail;
	}
	public String getTotalGroupPrice() {
		return totalGroupPrice;
	}
	public void setTotalGroupPrice(String totalGroupPrice) {
		this.totalGroupPrice = totalGroupPrice;
	}
	public String getGroupSale() {
		return groupSale;
	}
	public void setGroupSale(String groupSale) {
		this.groupSale = groupSale;
	}
	public String getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}
	public String getTotalPayPrice() {
		return totalPayPrice;
	}
	public void setTotalPayPrice(String totalPayPrice) {
		this.totalPayPrice = totalPayPrice;
	}
	public String getTotalFreight() {
		return totalFreight;
	}
	public void setTotalFreight(String totalFreight) {
		this.totalFreight = totalFreight;
	}
	public String getTotalOrderPrice() {
		return totalOrderPrice;
	}
	public void setTotalOrderPrice(String totalOrderPrice) {
		this.totalOrderPrice = totalOrderPrice;
	}
	public String getTotalFullcut() {
		return totalFullcut;
	}
	public void setTotalFullcut(String totalFullcut) {
		this.totalFullcut = totalFullcut;
	}
	public List<MakeOrderGoodsPageUtil> getGoods() {
		return goods;
	}
	public void setGoods(List<MakeOrderGoodsPageUtil> goods) {
		this.goods = goods;
	}
	public Map<String, Object> getBuyerAddr() {
		return buyerAddr;
	}
	public void setBuyerAddr(Map<String, Object> buyerAddr) {
		this.buyerAddr = buyerAddr;
	}
	public Map<String, Object> getBuyerInv() {
		return buyerInv;
	}
	public void setBuyerInv(Map<String, Object> buyerInv) {
		this.buyerInv = buyerInv;
	}
	public int getIsCash() {
		return isCash;
	}
	public void setIsCash(int isCash) {
		this.isCash = isCash;
	}
	public int getDealerCash() {
		return dealerCash;
	}
	public void setDealerCash(int dealerCash) {
		this.dealerCash = dealerCash;
	}
	public int getBean() {
		return bean;
	}
	public void setBean(int bean) {
		this.bean = bean;
	}
	public String getUseBean() {
		if(new BigDecimal(this.totalPayPrice).divide(new BigDecimal(2)).compareTo(new BigDecimal(this.useBean).divide(new BigDecimal(100)))<=0){
			return new BigDecimal(this.totalPayPrice).divide(new BigDecimal(2),2,BigDecimal.ROUND_DOWN).toString();
		}
		return new BigDecimal(this.useBean).divide(new BigDecimal(100)).toString();
	}
	public void setUseBean(int useBean) {
		this.useBean = useBean;
	}

	
	
	
}
