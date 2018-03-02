package com.framework.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @author 刘佳佳
 * 
 */
public class DateUtil {

	public static void main(String[] args) {

	}
	private static  SimpleDateFormat  shortSdf;
	private final static SimpleDateFormat sdfTime = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 日期转字符串
	 * 
	 * @param date
	 *            日期
	 * 
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static String dateToString(Date date, String pattern) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(date);
		}
		return "";
	}

	/**
	 * 日期转字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		return dateToString(date, "yyyy-MM-dd hh:mm:ss");
	}
	
	/**
	 * 日期转字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToSimpleString(Date date) {
		return dateToString(date, "yyyy-MM-dd");
	}
	
	/**
	 * 当前日期 指定天数之前的日期
	 * @param days
	 * @return
	 */
	public static String laterDate(int days) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date temp_date = null;
		try {
			Date d =new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			c.add(Calendar.DATE, -days);
			temp_date = c.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return format.format(temp_date);
	}

	public static String nextDate(String date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date temp_date = null;
		try {
			Date d = format.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			c.add(Calendar.DATE, +1);
			temp_date = c.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return format.format(temp_date);
	}

	public static Date string2Date(String date) {
		Date temp_date = null;
		if (date != null) {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				temp_date = format.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {

		}
		return temp_date;
	}

	
	public static Date string2SimpleDate(String date) {
		Date temp_date = null;
		if (date != null) {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				temp_date = format.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {

		}
		return temp_date;
	}


	/**
	 * 获取YYYY-MM-DD hh:mm:ss格式
	 *
	 * @return
	 */
	public static String getTime() {
		return sdfTime.format(new Date());
	}





	/**
	 * 获得本周的第一天，周一
	 *
	 * @return
	 */
	public static  Date getCurrentWeekDayStartTime() {
		shortSdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			int weekday = c.get(Calendar.DAY_OF_WEEK) - 2;
			c.add(Calendar.DATE, -weekday);
			c.setTime(shortSdf.parse(shortSdf.format(c.getTime())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c.getTime();
	}

	/**
	 * 获得本周的最后一天，周日
	 *
	 * @return
	 */
	public static Date getCurrentWeekDayEndTime() {
		shortSdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			int weekday = c.get(Calendar.DAY_OF_WEEK);
			c.add(Calendar.DATE, 8 - weekday);
			c.setTime(shortSdf.parse(shortSdf.format(c.getTime())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c.getTime();
	}
	/**
	 * 获得本月的开始时间，即2012-01-01
	 *
	 * @return
	 */
	public  static Date getCurrentMonthStartTime() {
		shortSdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Date now = null;
		try {
			c.set(Calendar.DATE, 1);
			now = shortSdf.parse(shortSdf.format(c.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 当前月的结束时间，即2012-01-31
	 *
	 * @return
	 */
	public static  Date getCurrentMonthEndTime() {
		shortSdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Date now = null;
		try {
			c.set(Calendar.DATE, 1);
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -1);
			now = shortSdf.parse(shortSdf.format(c.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 当前季度的开始时间，即2012-01-1
	 *
	 * @return
	 */
	public static  Date getCurrentQuarterStartTime() {
		shortSdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		int currentMonth = c.get(Calendar.MONTH) + 1;
		Date now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 3)
				c.set(Calendar.MONTH, 0);
			else if (currentMonth >= 4 && currentMonth <= 6)
				c.set(Calendar.MONTH, 3);
			else if (currentMonth >= 7 && currentMonth <= 9)
				c.set(Calendar.MONTH, 4);
			else if (currentMonth >= 10 && currentMonth <= 12)
				c.set(Calendar.MONTH, 9);
			c.set(Calendar.DATE, 1);
			now = shortSdf.parse(shortSdf.format(c.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 当前季度的结束时间，即2012-03-31
	 *
	 * @return
	 */
	public static  Date getCurrentQuarterEndTime() {
		shortSdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		int currentMonth = c.get(Calendar.MONTH) + 1;
		Date now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 3) {
				c.set(Calendar.MONTH, 2);
				c.set(Calendar.DATE, 31);
			} else if (currentMonth >= 4 && currentMonth <= 6) {
				c.set(Calendar.MONTH, 5);
				c.set(Calendar.DATE, 30);
			} else if (currentMonth >= 7 && currentMonth <= 9) {
				c.set(Calendar.MONTH, 8);
				c.set(Calendar.DATE, 30);
			} else if (currentMonth >= 10 && currentMonth <= 12) {
				c.set(Calendar.MONTH, 11);
				c.set(Calendar.DATE, 31);
			}
			now = shortSdf.parse(shortSdf.format(c.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

}
