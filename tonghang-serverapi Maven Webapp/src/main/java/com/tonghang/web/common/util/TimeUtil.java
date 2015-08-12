package com.tonghang.web.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeUtil {
	public static String getFormatStringTime(String datetime){
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime dt = dtf.parseDateTime(datetime);
		return dt.toString(dtf);
	}
	
	public static Date getFormatDate(String datetime){
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime dt = dtf.parseDateTime(datetime);
		return dt.toDate();
	}

	public static String getFormatString(Date date) {
		 DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 return format.format(date);
	}
	
	public static String getFormatStringDate(Date date) {
		 DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		 return format.format(date);
	}
	
	public static Date plusHour(int hours,Date datetime){
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime dt = dtf.parseDateTime(getFormatString(datetime));
		dt.plusHours(hours);
		return dt.toDate();
	}
}
