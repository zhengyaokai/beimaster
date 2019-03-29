package bmatser.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
public class AmountToChar {
	
    private static final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
	private String point="";
	
	private String thousand="";
	
	private String wan="";
	
	private String yi="";
	
	private boolean symbol = true;
	
	

	public AmountToChar(String amount) throws Exception {
		BigDecimal ads = new BigDecimal(amount);
		if(ads.compareTo(BigDecimal.ZERO)<0){
			symbol = false;
			amount = amount.replace("-", "");
		}
		DecimalFormat df = new DecimalFormat("#0.##");
		amount = df.format(Double.parseDouble(amount));
		if(amount.indexOf(".")!=-1){
			point = amount.substring(amount.indexOf(".")+1, amount.length());
			amount = amount.substring(0, amount.indexOf("."));
		}
		this.thousand = amount.length()>4?amount.substring(amount.length()-4, amount.length()):amount.substring(0, amount.length());
		if(amount.length()>4){
			this.wan = amount.length()>8?amount.substring(amount.length()-8, amount.length()-4):amount.substring(0, amount.length()-4);
		}
		if(amount.length()>8){
			this.yi = amount.length()==12?amount.substring(0, 4) : amount.substring(0, amount.length()-8);
		}
		if(amount.length()>12){
			throw new Exception("金额太大无法转换");
		}
	}

	public String getPoint() {
		String result = "";
		result = point.length()>0?numToChar(point.charAt(0))+"角":"";
		result = result +(point.length()>1?numToChar(point.charAt(1))+"分":"");
		result = result.replace("零角", "零");
		return "".equals(result)?"整":result;
	}
	
	private String numToChar(char charAt) {
		return CN_UPPER_NUMBER[Integer.parseInt(String.valueOf(charAt))];
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getThousand() {
		String result = "";
		result = numToChar(thousand.charAt(thousand.length()-1)) +"元";
		return count(result,thousand);
	}

	private String count(String result, String code) {
		if(code.length()>1){
			result = ("0".equals(String.valueOf(code.charAt(code.length()-2)))?"零":numToChar(code.charAt(code.length()-2))+"拾") +result;
		}
		if(code.length()>2){
			result = ("0".equals(String.valueOf(code.charAt(code.length()-3)))?"零":numToChar(code.charAt(code.length()-3))+"佰") +result;
		}
		if(code.length()>3){
			result = ("0".equals(String.valueOf(code.charAt(code.length()-4)))?"零":numToChar(code.charAt(code.length()-4))+"仟") +result;
		}
		result = result.indexOf("零零零零")!=-1?"":result;
		result = result.indexOf("零零零")==0?result.replace("零零零", "零"):result.replace("零零零", "");
		result = result.indexOf("零零")==3?result.replace("零零", ""):result.replace("零零", "零");
		result = result.replace("壹拾", "拾");
		result = result.replace("零万", "万");
		result = result.replace("零亿", "亿");
		result = result.replace("零元", "元");
		return result;
	}

	public void setThousand(String thousand) {
		this.thousand = thousand;
	}

	public String getWan() {
		String result = "";
		if(wan!=""){
			result = numToChar(wan.charAt(wan.length()-1)) +"万";
			result = count(result,wan);
		}
		return result;
	}

	public void setWan(String wan) {
		this.wan = wan;
	}

	public String getYi() {
		String result = "";
		if(yi!=""){
			result = wan.charAt(yi.charAt(yi.length()-1)) +"亿";
			result = count(result,yi);
		}
		return result;
	}

	public void setYi(String yi) {
		this.yi = yi;
	}

	public String isSymbol() {
		return symbol?"":"负";
	}
	public boolean getIsSymbol() {
		return symbol;
	}

	public void setSymbol(boolean symbol) {
		this.symbol = symbol;
	}
	
	public String getCharAmount(){
		return this.isSymbol()+this.getYi()+this.getWan()+this.getThousand()+this.getPoint();
		
	}
	public static void main(String[] args) throws Exception {
		AmountToChar aa = new AmountToChar("1225645501.01");
		System.out.println(aa.getCharAmount());
	}
}
