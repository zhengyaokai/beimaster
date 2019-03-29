package bmatser.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.igfay.jfig.JFig;

/**
 * 快递服务接口
 * @author felx
 * @version 1.0
 * 
 */
public class Delivery {
	
	/** 快递接口地址 */
	private static String KUAIDI_HTML_URL = JFig.getInstance().getValue("system_options", "KUAIDI_URL", "http://www.kuaidi100.com/applyurl?key=51ec6d652d9ae7e0&amp;com=%s&amp;nu=%s");

	/**
	 * 通过HtmlAPI获取物流信息
	 * @param comNo 快递公司编码。例如： yuantong（圆通），yunda（韵达）， zhongtong（中通），shunfeng（顺丰），shentong（申通）等
	 * @param num 快递单号
	 * @return 结果url地址(用iframe页调用该url)
	 */
	public static String search(String comNo, String num){
		 URLConnection con = null;
		 InputStream urlStream = null;
		 StringBuffer content = new StringBuffer("");
	     try {
	    	 URL url = new URL(String.format(KUAIDI_HTML_URL, comNo, num).replaceAll("&amp;", "&"));
	         con = url.openConnection();
	         con.setAllowUserInteraction(false);
	         urlStream = url.openStream();
	         byte b[] = new byte[10000];
	         int numRead = urlStream.read(b);
	         content.append(new String(b, 0, numRead));
	         while (numRead != -1) {
	            numRead = urlStream.read(b);
	            if (numRead != -1) {
	               String newContent = new String(b, 0, numRead, "UTF-8");
	               content.append(newContent);
	            }
	         }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally{
	    	if(urlStream != null){
	    		 try {
					urlStream.close();
				} catch (IOException e) {
				}
	    	}
	    }
	    return content.toString();
	}
	
	public static void main(String[] agrs){
		System.out.print(search("huitongkuaidi", "310016189941"));
	}
}
