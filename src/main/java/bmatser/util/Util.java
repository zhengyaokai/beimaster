package bmatser.util;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

import org.apache.http.util.EncodingUtils;

import sun.misc.BASE64Encoder;
public class Util {

	public static String loadFile(String fileName) {
		InputStream fis;
		try {
			fis = new FileInputStream(fileName);
			
			byte[] bs = new byte[fis.available()];
			fis.read(bs);
			String res =EncodingUtils.getString(bs, "gb2312");
			fis.close();
			return res;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String md5EncryptAndBase64(String str) {
		return encodeBase64(md5Encrypt(str));
	}
	private static byte[] md5Encrypt(String encryptStr) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(encryptStr.getBytes("UTF-8"));
			return md5.digest();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	private static String encodeBase64(byte[] b) {
		sun.misc.BASE64Encoder base64Encode = new BASE64Encoder();
		String str = base64Encode.encode(b);
		return str;
	}

	public static void main(String[] args) {
		String xml = loadFile(args[0]);
		String checkword = loadFile(args[1]);
		System.out.println(md5EncryptAndBase64(xml + checkword));
		System.out.println(md5EncryptAndBase64("abc"));
		System.out.println(md5EncryptAndBase64("中"));
	}
}

