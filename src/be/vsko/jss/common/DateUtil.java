package be.vsko.jss.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import be.vsko.jss.exception.DataException;

public class DateUtil {
	
	private static SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat timestampDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	

	/**
	 * Converts a date to its sql representation
	 */
	public static String dateToSql(Date date) {
		return sqlDateFormat.format(date);
	}
	
	public static String stringDateToSql(String str) {
		Date date = stringToDate(str);
		return dateToSql(date);
	}

	public static String dateToStringWithTime(Date date) {	
		return timestampDateFormat.format(date);
	}
	
	public static String dateToString(Date date) {
		String result = "";
		if(date != null)
			result = dateFormat.format(date);
		
		return result;
	}
	
	public static Date sqlStringToDate(String str) {
		Date result = null;
		if(str != null && !str.isEmpty()) {
			try {
				result = sqlDateFormat.parse(str);
			}
			catch(ParseException e) {
				throw new DataException(e);
			}
		}
		
		return result;
	}
	
	public static Date stringToDate(String str) {
		Date result = null;
		if(str != null && !str.isEmpty()) {
			try {
				result = dateFormat.parse(str);
			}
			catch(ParseException e) {
				throw new DataException(e);
			}
		}
		
		return result;
	}
	
	
}