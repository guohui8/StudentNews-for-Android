package com.guohui.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	private static final DateFormat FMT_DATE = new SimpleDateFormat("MM-dd");
	private static final DateFormat FMT_DATE2 = new SimpleDateFormat("yy-MM-dd");
	@SuppressWarnings("unused")
	private static final DateFormat FMT_DATE_TIME = new SimpleDateFormat("MM-dd HH:mm");
	@SuppressWarnings("unused")
	private static final DateFormat FMT_DATE_TIME2 = new SimpleDateFormat("yy-MM-dd HH:mm");
	private static final DateFormat FMT_TIME = new SimpleDateFormat("HH:mm");
	public static final long RELEASE_ZERO_TIMESTAMP = 1298908800000L;
	private static long TIME_CALIBRATOR = 0L;
	public static final long ZERO_TIMESTAMP = 1044028800000L;

	public static long currentTimeMillis() {
		return System.currentTimeMillis() + TIME_CALIBRATOR;
	}

	public static String format(Date paramDate) {
		String s;
		if (paramDate == null || paramDate.getTime() < 0xf314f73c00L) {
			s = "";
		} else {
			Date date1 = new Date();
			if (date1.getYear() == paramDate.getYear()
					&& date1.getMonth() == paramDate.getMonth()
					&& date1.getDate() == paramDate.getDate())
				s = FMT_TIME.format(paramDate);
			else if (date1.getYear() == paramDate.getYear())
				s = FMT_DATE.format(paramDate);
			else
				s = FMT_DATE2.format(paramDate);
		}
		return s;
	}


	public static long getNextDayTimeMillis() {
		Calendar localCalendar = Calendar.getInstance();
		localCalendar.set(11, 24);
		localCalendar.set(12, 0);
		localCalendar.set(13, 0);
		localCalendar.set(14, 0);
		return localCalendar.getTimeInMillis();
	}

	public static void setHttpResponseDate(String paramString) {
		try {
			Date localDate = new SimpleDateFormat(
					"EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US)
					.parse(paramString.trim());
			if (localDate.getTime() >= 1298908800000L)
				TIME_CALIBRATOR = localDate.getTime()
						- System.currentTimeMillis();
		} catch (Exception localException) {
		} catch (Error localError) {
		}
	}
	public static long timeCalibrator() {
		return TIME_CALIBRATOR;
	}
	
	public static String getStandardTime(long timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		Date date = new Date(timestamp * 1000);
		sdf.format(date);
		return sdf.format(date);
	}

	public static String getDate(long timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
		Date date = new Date(timestamp * 1000);
		sdf.format(date);
		return sdf.format(date);
	}

	public static String getDatetime(long timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(timestamp * 1000);
		sdf.format(date);
		return sdf.format(date);
	}
	//当前当天时间
	public static String getDayTime(){
		SimpleDateFormat sm=new SimpleDateFormat("MM-dd"+"日");
		return sm.format(new Date());
	}
	// 转换时间
	public static String converTime(long timestamp) {
		long currentSeconds = System.currentTimeMillis() / 1000;
		long timeGap = currentSeconds - timestamp;// 与现在时间相差秒数
		String timeStr = null;
		if (timeGap > 24 * 60 * 60) {// 1天以上
			//timeStr = timeGap / (24 * 60 * 60) + "天前";
			SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
			Date date = new Date(timestamp * 1000);
			timeStr=sdf.format(date);
		} else if (timeGap > 60 * 60) {// 1小时-24小时
			timeStr = timeGap / (60 * 60) + "小时前";
		} else if (timeGap > 60) {// 1分钟-59分钟
			timeStr = timeGap / 60 + "分钟前";
		} else {// 1秒钟-59秒钟
			timeStr = "刚刚";
		}
		return timeStr;
	}

	/**
     * 根据 timestamp 生成各类时间状态串
     * 
     * @param timestamp 距1970 00:00:00 GMT的秒数
     * @return 时间状态串(如：刚刚5分钟前)
     */
    public static String getTimeState(String timestamp) {
        if (timestamp == null || "".equals(timestamp)) {
            return "";
        }

        try {
        	long _timestamp = Long.parseLong(timestamp);
        	_timestamp = (timestamp.length() == 13) ? _timestamp : _timestamp * 1000;
            if (System.currentTimeMillis() - _timestamp < 1 * 60 * 1000) {
                return "刚刚";
            } else if (System.currentTimeMillis() - _timestamp < 30 * 60 * 1000) {
                return ((System.currentTimeMillis() - _timestamp) / 1000 / 60)
                        + "分钟前";
            } else {
                Calendar now = Calendar.getInstance();
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(_timestamp);
                if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                        && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                        && c.get(Calendar.DATE) == now.get(Calendar.DATE)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
                    return sdf.format(c.getTime());
                }
                if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                        && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                        && c.get(Calendar.DATE) == now.get(Calendar.DATE) - 1) {
                    SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
                    return sdf.format(c.getTime());
                } else if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("M月d日 HH:mm:ss");
                    return sdf.format(c.getTime());
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            "yyyy年M月d日 HH:mm:ss");
                    return sdf.format(c.getTime());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}