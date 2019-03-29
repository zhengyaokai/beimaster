package bmatser.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

public class ObjectUtil {
	
	/**
	 * 判断对象属性是否有值
	 * @param param
	 * @param arg
	 * @throws Exception
	 */
	public static void isEmpty(Object param,String ...arg) throws Exception{
		Class<?> cls = param.getClass();
		for (String s : arg) {
			String str = "get" + s.substring(0, 1).toUpperCase() + s.substring(1);
			System.out.println(str);
			Method method = cls.getDeclaredMethod(str, null);
			Object o = method.invoke(param, null);
			if(o==null || "".equals(o.toString())){
				throw new RuntimeException(s+"不能为空!");
			}
		}
	}
	
	/**
	 * 判断多个参数是否为空
	 * @param param
	 * @return
	 */
	public static boolean isEmpty(String ... param){
		for (String value : param) {
			if(StringUtils.isBlank(value)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 判断Map对象是否为空
	 * @param param
	 * @return
	 */
	public static boolean isEmpty(Map param){
		if(param==null || param.isEmpty()){
			return true;
		}
		return false;
	}
	/**
	 * 判断是否为空，若为空返回对应异常
	 * @param param
	 * @param exception  自定义异常信息
	 * @return
	 * @throws Exception
	 */
	public static String isReturn(String param,String exception)throws Exception{
		if(StringUtils.isBlank(param)){
			throw new Exception(exception);
		}
		return param;
	}
	
	/**
	 * 判断是否为空，若为空返回对应异常
	 * @param param
	 * @param exception  自定义异常信息
	 * @return
	 * @throws Exception
	 */
	public static Integer isReturn(Integer param,String exception)throws Exception{
		if(param == null){
			throw new Exception(exception);
		}
		return param;
	}
	
	public static  <K, V>Map<K, V> isReturn(Map<K, V> param,String exception) throws Exception{
		if(param==null || param.isEmpty()){
			throw new Exception(exception);
		}
		return param;
	}
	/**
	 * java对象转Map(建议使用toMapBuild)
	 * @param o
	 * @return
	 */
	@Deprecated
    public static Map<String,Object> toMap(Object o){
    	String jsonString = JSONObject.toJSONString(o);
    	JSONObject jo = JSONObject.parseObject(jsonString);
        Map<String,Object> map = new HashMap<>();
        for (Entry<String, Object> data : jo.entrySet()) {
        	map.put(data.getKey(), data.getValue());
		}
        return map;
    }
	/**
	 * 新java对象转Map(建议使用toMapBuild)
	 * @param o
	 * @return
	 */
    public static Map<String, Object> toMapBuild(Object o){
    	Map<String,Object> map = new HashMap<>();
    	Class c = o.getClass();
    	Method[] methods = c.getDeclaredMethods();
    	for (Method method : methods) {
    		String  name = method.getName();
    		if(name.indexOf("get")==0){
    			String field = name.substring(3);
    			field = field.substring(0, 1).toLowerCase()+field.substring(1);
    			try {
    				map.put(field, method.invoke(o, null));
				} catch (Exception e) {
					e.printStackTrace();
				}
    		}
		}
		return map;
    }
    /**
     * 新java对象转Map(建议使用toMapBuild)
     * @param o
     * @return
     */
    public static Map<String, String> toMapStringBuild(Object o){
    	Map<String,String> map = new HashMap<>();
    	Class c = o.getClass();
    	Method[] methods = c.getDeclaredMethods();
    	for (Method method : methods) {
    		String  name = method.getName();
    		if(name.indexOf("get")==0){
    			String field = name.substring(3);
    			field = field.substring(0, 1).toLowerCase()+field.substring(1);
    			try {
    				Object value = method.invoke(o, null);
    				map.put(field, value!=null?String.valueOf(value):null);
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
    	}
    	return map;
    }

    /**
     * Object转BigDecimal
     * @param o
     * @return
     */
	public static BigDecimal toBigDecimal(Object o) {
		if(o!=null){
			return new BigDecimal(o.toString());
		}
		return null;
	}
	/**
	 * Object转Int
	 * @param o
	 * @return
	 */
	public static int tointValue(Object o) {
		if(o != null){
			if(o instanceof Integer){
				return (Integer)o;
			}
			if(o instanceof Number){
				return ((Number)o).intValue();
			}
			return new BigDecimal(o.toString()).intValue();
		}
		return 0;
	}
}
