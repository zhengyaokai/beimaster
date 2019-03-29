package bmatser.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {

	public static String getPropertyValue(String fileName, String propertyKey) {
		if(null == fileName){
			return null;
		}
		if(fileName.indexOf(".properties")<0){
			fileName = fileName + ".properties";
		}
		InputStream in = null;
		String result = null;
		try {
			in = PropertyUtil.class.getResourceAsStream("/"+fileName);
			Properties pro = new Properties();
			pro.load(in);
			result = pro.getProperty(propertyKey);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException ex1) {
				ex1.printStackTrace();
			}
		}
		return result;
	}
	
	public static String getPropertyValue(String fileName, String propertyKey,String defaultValue) {
		if(null == fileName){
			return null;
		}
		if(fileName.indexOf(".properties")<0){
			fileName = fileName + ".properties";
		}
		InputStream in = null;
		String result = defaultValue;
		try {
			in = PropertyUtil.class.getResourceAsStream("/"+fileName);
			Properties pro = new Properties();
			pro.load(in);
			result = pro.getProperty(propertyKey);
			if(null == result){
				result = defaultValue;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result = defaultValue;
		} finally {
			try {
				in.close();
			} catch (IOException ex1) {
				ex1.printStackTrace();
			}
		}
		return result;
	}
}
