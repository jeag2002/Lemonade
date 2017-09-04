package be.vsko.jss.common.jqg;

import java.util.Date;

import be.vsko.jss.common.AbstractEntity;
import be.vsko.jss.common.DateUtil;
import be.vsko.jss.common.Util;

/**
 * @author lionel
 * 
 * Abstract class to get the sql necessary to filter certain
 * attribute. This is a middle class between the jqgrid filter
 * and the database   
 */
public class AttributeSqlHandler {
	private static String OPERATION_EQUAL = "eq";
	private static String OPERATION_NOT_EQUAL = "ne";
	private static String OPERATION_GREATER_EQUAL = "ge";
	private static String OPERATION_LESS_EQUAL = "le";
	private static String OPERATION_BEGIN_WITH = "bw";
	private static String OPERATION_END_WITH = "ew";
	private static String OPERATION_CONTAINS = "cn";
	
	/**
	 * Depending on the filter data, creates the sql for the where clause.
	 */
	@SuppressWarnings("unchecked")
	public static String getSqlFilter(Class clazz, String field, String data, String operation) {
		String result = "";
		
		data = Util.escape(data);
		
		//get the result type based on the column name
		Class dataType = AbstractEntity.getReturnTypeByDatabaseFieldName(clazz, getFieldWithoutPrefix(field));
		
		//now, based on the data type, create the sql condition
		if(dataType.equals(String.class) || dataType.equals(char.class))
			result = simpleStringCondition(field, data, operation);
		else if(dataType.equals(Long.class) || dataType.equals(Integer.class) || dataType.equals(Double.class))
			result = simpleNumericCondition(field, data, operation);
		else if(dataType.equals(Date.class))
			result = simpleDateCondition(field, data, operation);
		else if(dataType.equals(Boolean.class))
			result = simpleBooleanCondition(field, data, operation);
		else if(dataType.getSuperclass().equals(AbstractEntity.class))
			result = simpleNumericCondition(field, data, operation);
		else if(dataType.isEnum()) 
			result = simpleStringCondition(field, data, operation);
		
		return result;
	}
	
	/**
	 * Gets the field name without the prefix, i.e. df.id -> id
	 */
	protected static String getFieldWithoutPrefix(String field) {
		String result = field;
		
		if(field.contains("."))
			result = field.substring(field.indexOf(".")+1);
		
		return result;
	}
	
	public static boolean isOperationEqual(String operation) {
		return operation.equals(OPERATION_EQUAL);
	}
	
	public static boolean isOperationNotEqual(String operation) {
		return operation.equals(OPERATION_NOT_EQUAL);
	}
	
	public static boolean isOperationGreaterEqual(String operation) {
		return operation.equals(OPERATION_GREATER_EQUAL);
	}
	
	public static boolean isOperationLessEqual(String operation) {
		return operation.equals(OPERATION_LESS_EQUAL);
	}
	
	public static boolean isOperationBeginWith(String operation) {
		return operation.equals(OPERATION_BEGIN_WITH);
	}
	
	public static boolean isOperationEndWith(String operation) {
		return operation.equals(OPERATION_END_WITH);
	}
	
	public static boolean isOperationContains(String operation) {
		return operation.equals(OPERATION_CONTAINS);
	}
	
	public static String simpleStringCondition(String field, String data, String operation) {
		String result = "";
		data = data.toLowerCase();
		field = "lower("+field+")";
		
		if(isOperationEqual(operation))
			result = field + " = '" + data + "'";
		else if(isOperationNotEqual(operation))
			result = field + " <> '" + data + "'";
		else if(isOperationEndWith(operation))
			result = field + " like '%" + data + "'";
		else if(isOperationBeginWith(operation))
			result = field + " like '" + data + "%'";
		else if(isOperationContains(operation))
			result = field + " like '%" + data + "%'";
		
		return result;
	}
	
	public static String simpleNumericCondition(String field, String data, String operation) {
		String result = "";
		
		if(isOperationEqual(operation))
			result = field + " = " + data;
		else if(isOperationNotEqual(operation))
			result = field + " <> " + data;
		else if(isOperationGreaterEqual(operation))
			result = field + " >= " + data;
		else if(isOperationLessEqual(operation))
			result = field + " <= " + data;
		
		return result;
	}
	
	public static String simpleDateCondition(String field, String data, String operation) {
		String result = "";
		
		if(!data.isEmpty()) {
			data = DateUtil.stringDateToSql(data);
			
			if(isOperationEqual(operation))
				result = field + " = '" + data + "'";
			else if(isOperationNotEqual(operation))
				result = field + " <> '" + data + "'";
			else if(isOperationGreaterEqual(operation))
				result = field + " >= '" + data + "'";
			else if(isOperationLessEqual(operation))
				result = field + " <= '" + data + "'";
		}
		else {
			if(isOperationEqual(operation))
				result = field + " is null";
			else if(isOperationNotEqual(operation))
				result = field + " is not null";
		}
		
		return result;
	}
	
	public static String simpleBooleanCondition(String field, String data, String operation) {
		String result = "";
		
		if(isOperationEqual(operation)) {
			if(data.toLowerCase().equals("true") || data.toLowerCase().equals("t") || data.equals("1"))
				result = field + " is true";
			else
				result = field + " is not true";
		}
		
		
		return result;
	}
			
}
