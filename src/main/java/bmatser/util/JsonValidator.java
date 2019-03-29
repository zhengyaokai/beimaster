package bmatser.util;

import com.alibaba.fastjson.JSONObject;

public class JsonValidator {

	public static boolean validate(String input){
		try{
		    JSONObject jsonObject = JSONObject.parseObject(input); 
		    return true;
		}catch(Exception e){
		    return false;
		}
	}
}
