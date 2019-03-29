package bmatser.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileUtils {

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^(1[3,5,7,8])\\d{9}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
}
