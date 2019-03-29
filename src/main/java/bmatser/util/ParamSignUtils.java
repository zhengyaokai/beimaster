package bmatser.util;

import java.io.IOException;  
import java.io.UnsupportedEncodingException;  
import java.security.GeneralSecurityException;  
import java.security.MessageDigest;  
import java.util.ArrayList;  
import java.util.Collections;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
  
/** 
 * 请求参数签名工具类 
 * 
 */  
public class ParamSignUtils {  
      
    public static void main(String[] args)  
    {  
        HashMap<String, String> signMap = new HashMap<String, String>();  
        signMap.put("a","01");  
        signMap.put("c","02");  
        signMap.put("b","03");  
        String secret="test";  
        String SignHashMap=ParamSignUtils.sign(signMap, secret);  
        System.out.println("SignHashMap:"+SignHashMap);  
        List<String> ignoreParamNames=new ArrayList<String>();  
        ignoreParamNames.add("a");  
        String SignHashMap2=ParamSignUtils.sign(signMap,ignoreParamNames, secret);  
        System.out.println("SignHashMap2:"+SignHashMap2);  
    }  
  
    public static String sign(Map<String, String> paramValues,  
            String secret) {  
        return sign(paramValues, null, secret);  
    }  
  
    /** 
     * @param paramValues 
     * @param ignoreParamNames 
     * @param secret 
     * @return 
     */  
    public static String sign(Map<String, String> paramValues,  
            List<String> ignoreParamNames, String secret) {  
        try {  
            HashMap<String, String> signMap = new HashMap<String, String>();  
            StringBuilder sb = new StringBuilder();  
            List<String> paramNames = new ArrayList<String>(paramValues.size());  
            paramNames.addAll(paramValues.keySet());  
            if (ignoreParamNames != null && ignoreParamNames.size() > 0) {  
                for (String ignoreParamName : ignoreParamNames) {  
                    paramNames.remove(ignoreParamName);  
                }  
            }  
            Collections.sort(paramNames);  
            sb.append(secret);  
            for (String paramName : paramNames) {  
                sb.append(paramName).append(paramValues.get(paramName));  
            }  
            sb.append(secret);  
            //System.out.println(sb.toString());
            byte[] md5Digest = getMD5Digest(sb.toString());  
            String sign = byte2hex(md5Digest);  
            signMap.put("appParam", sb.toString());  
//            System.out.println("appParam:"+sb.toString());
            signMap.put("appSign", sign);  
            return sign;  
        } catch (IOException e) {  
            throw new RuntimeException("加密签名计算错误", e);  
        }  
          
    }  
  
    public static String utf8Encoding(String value, String sourceCharsetName) {  
        try {  
            return new String(value.getBytes(sourceCharsetName), "UTF-8");  
        } catch (UnsupportedEncodingException e) {  
            throw new IllegalArgumentException(e);  
        }  
    }  
  
    private static byte[] getSHA1Digest(String data) throws IOException {  
        byte[] bytes = null;  
        try {  
            MessageDigest md = MessageDigest.getInstance("SHA-1");  
            bytes = md.digest(data.getBytes("UTF-8"));  
        } catch (GeneralSecurityException gse) {  
            throw new IOException(gse);  
        }  
        return bytes;  
    }  
  
    private static byte[] getMD5Digest(String data) throws IOException {  
        byte[] bytes = null;  
        try {  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            bytes = md.digest(data.getBytes("UTF-8"));  
        } catch (GeneralSecurityException gse) {  
            throw new IOException(gse);  
        }  
        return bytes;  
    }  
  
  
    private static String byte2hex(byte[] bytes) {  
        StringBuilder sign = new StringBuilder();  
        for (int i = 0; i < bytes.length; i++) {  
            String hex = Integer.toHexString(bytes[i] & 0xFF);  
            if (hex.length() == 1) {  
                sign.append("0");  
            }  
            sign.append(hex.toUpperCase());  
        }  
        return sign.toString();  
    }  
  
} 
