package bmatser.util;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * 日期时间公共类.
 * 
 * @author felx
 */
public class DateTypeHelper {
	
	public final static String DISTINCT = "DISTINCT";

	public final static String PREFIX_DISTINCT = "DISTINCT ";

	public final static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

	public final static String DEFAULT_TIME_PATTERN = "hh:mm:ss";

	public final static String DEFAULT_TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public final static String VALUE_EMPTY_STRING = "";

	public final static String DELIMITER_DOT = ".";

	public static Integer getInteger(int i) {
		return Integer.valueOf(i);
	}

	public static Long getLong(long l) {
		return Long.valueOf(l);
	}

	public static Float getFloat(float f) {
		return new Float(f);
	}

	public static Double getDouble(double d) {
		return new Double(d);
	}

	public static BigDecimal getDecimal(String str) {
		return new BigDecimal(str);
	}

	/**
	 * 获取当前日期时间（Date类型）
	 * 
	 * @return Date
	 */
	public static Date getCurrentDate() {
		return new Date(System.currentTimeMillis());
	}

	/**
	 * 字符串日期转换成Date格式
	 * 
	 * @param date
	 * @return Date
	 */
	public static Date getDate(String date) {
		return getDate(date, DEFAULT_DATE_PATTERN);
	}

	/**
	 * 获取当前日期时间(字符串类型)
	 * 
	 * @return String
	 */
	public static String getCurrentDateTimeString(){
		String ret = getDateString(new Date(),DEFAULT_TIMESTAMP_PATTERN);
		return ret;
	}
	
	/**
	 * 获取指定日期/时间（根据指定字符串形式日期以及格式）
	 * 
	 * @param date
	 * @param pattern
	 * @return Date
	 */
	public static Date getDate(String date, String pattern) {
		Date d = null;

		if (date == null || VALUE_EMPTY_STRING.equals(date.trim())) {
			return d;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);

		try {
			d = new Date(sdf.parse(date).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return d;
	}

	/**
	 * 将Date型日期转换成字符串日期
	 * 
	 * @param date
	 * @return String
	 */
	public static String getDateString(java.util.Date date) {
		return SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM,
				Locale.CHINA).format(date);
	}
	
	/**
	 * 将Date型日期转换成字符串日期,并进行格式化
	 * 
	 * @param date
	 * @param pattern
	 * @return String
	 */
	public static String getDateString(java.util.Date date,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);
		return sdf.format(date);
	}
	
	/**
	 * 获取当前系统时间
	 * 
	 * @return Time
	 */
	public static Time getCurrentTime() {
		return new Time(System.currentTimeMillis());
	}

	/**
	 * 将字符串类型的时间转换成Time类型的时间
	 * 
	 * @param time
	 * @return Time
	 */
	public static Time getTime(String time) {
		return getTime(time, DEFAULT_TIME_PATTERN);
	}

	/**
	 * 将字符串类型的时间转换成Time类型的时间,并进行格式化
	 * 
	 * @param time
	 * @param pattern
	 * @return Time
	 */
	public static Time getTime(String time, String pattern) {
		Time t = null;

		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);

		try {
			t = new Time(sdf.parse(time).getTime());
		} catch (ParseException e) {
			return null;
		}
		return t;
	}

	
	public static String getTimeString(java.util.Date time) {
		return SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM,
				Locale.CHINA).format(new Date(time.getTime()));
	}

	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static Timestamp getTimestamp(String timestamp) {
		return getTimestamp(timestamp, DEFAULT_TIMESTAMP_PATTERN);
	}

	public static Timestamp getTimestamp(String timestamp, String pattern) {
		Timestamp t = null;

		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);

		try {
			t = new Timestamp(sdf.parse(timestamp).getTime());
		} catch (Exception e) {
			return null;
		}
		return t;
	}

	public static String getTimestampString(java.util.Date ts) {
		return SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.MEDIUM,
				SimpleDateFormat.MEDIUM, Locale.CHINA).format(ts);
	}

	/**
	 * 根据指定日期及加减天数，得到加减后的日期
	 * 
	 * @param date
	 * @param days
	 * @param datePart
	 * @return Date
	 */
	public static Date dateAdd(Date date , int days, String datePart){
		if(datePart==null)
			throw new IllegalArgumentException("datePart=Null");
		if(datePart.length()==0)
			throw new IllegalArgumentException("datePart=\"\"");
		datePart = datePart.toUpperCase();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int field = -1;
		if("D".equals(datePart))
			field = Calendar.DAY_OF_YEAR;
		else if("M".equals(datePart))
			field = Calendar.MONTH;
		else if("Y".equals(datePart))
			field = Calendar.YEAR;
		else if("H".equals(datePart))
			field = Calendar.HOUR;
		else if("N".equals(datePart))
			field = Calendar.MINUTE;
		else if("S".equals(datePart))
			field = Calendar.SECOND;
		if(field==-1)
			throw new IllegalArgumentException("datePart=\""+datePart+"\"");
		cal.add(field, days); 
		return cal.getTime();
	}
	
	public static void main(String[] args){
		System.out.println(dateAdd(new Date(), -10, "D"));
		System.out.print(getDateString(dateAdd(new Date(), -10, "D"), DEFAULT_DATE_PATTERN));
	}
}
