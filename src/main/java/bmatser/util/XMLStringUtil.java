package bmatser.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

public class XMLStringUtil {
	
	/**
	 * @author felx
	 * @describe Map转XMLString
	 * @param map
	 * @return	
	 */
	public static String toXmlString(Map<String, ?> map){
		StringBuffer buff = new StringBuffer("<xml>");
		for (Entry<String, ?> param : map.entrySet()) {
			String key = param.getKey();
			String value = param.getValue()!=null?param.getValue().toString():"";
			buff.append("<"+key+">");
			buff.append("<![CDATA["+value+"]]>");
			buff.append("</"+key+">");
		}
		buff.append("</xml>");
		return buff.toString();
	}
	
	/**
	 * 
	 * @author felx
	 * @describe 取出XML中key对应的Value值
	 * @param xml
	 * @param key
	 * @return
	 */
	public static String parsingXmlString(String xml,String key){
		String startStr = "<"+key+">";
		String endStr = "</"+key+">";
		int start = xml.indexOf(startStr);
		int end = xml.indexOf(endStr);
		if(start != -1 && end != -1){
			String param = xml.substring(start+startStr.length(), end);
			if(param.indexOf("<![CDATA[")!=-1){
				int x = param.indexOf("<![CDATA[")+9;
				int y = param.indexOf("]]>");
				param = param.substring(x, y);
			}
			return param;
		}else{
			throw new RuntimeException(key+"值不存在或未结束");
		}
	}

	/**
	 * @author felx
	 * @describe 将XML中的值存入Map中
	 * @param xml
	 * @return
	 */
	public static Map<String, String> parsingXmlToMap(String xml) {
		String str = xml;
		Map<String, String> map = new HashMap<String, String>();
		while(str.indexOf("</")>=0){
			str = str.substring(str.indexOf("</")+2, str.length());
			String key = str.substring(0, str.indexOf(">")>0?str.indexOf(">"):0);
			if(StringUtils.isNotBlank(key)){
				map.put(key, parsingXmlString(xml,key));
			}
		}
		return map;
	}
}
