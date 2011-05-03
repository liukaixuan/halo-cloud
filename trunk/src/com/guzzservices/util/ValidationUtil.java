package com.guzzservices.util;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;


/**
 * 用于检测字符串，如果检测不通过，就显示相应错误信息
 */
public class ValidationUtil extends ValidationUtils {
	/**
	 * 检测，如果没有包含某个字符，那么显示出错信息
	 * @param errors
	 * @param field
	 * @param errorCode
	 * @param defaultMessage 
	 * @param containedChar 必须包含的字符
	 */
	public static void rejectIfNotContain(Errors errors, String field,
			String errorCode, String defaultMessage, char containedChar) {
		Object value = errors.getFieldValue(field);
		if (value != null) {
			String string = (String) value;
			if (string.indexOf(containedChar) == -1) {
				errors.rejectValue(field, errorCode, null, defaultMessage);
			}
		}
	}
	
	public static void reject(Errors errors, String field, String errorCode, String defaultMessage) {
		errors.rejectValue(field, errorCode, null, defaultMessage);
	}
	
	/**
	 * 检测输入字符串是否和指定字符串相等
	 * @param errors
	 * @param field
	 * @param errorCode
	 * @param defaultMessage 
	 * @param match 必须包含的字符
	 */
	public static void rejectIfNotMatch(Errors errors, String field,
			String errorCode, String defaultMessage, String match) {
		Object value = errors.getFieldValue(field);
		if (value != null) {
			String string = (String) value;
			if (!string.equalsIgnoreCase(match)) {
				errors.rejectValue(field, errorCode, null, defaultMessage);
			}
		}
	}
	/**
	 * 检测boolean
	 * @param errors
	 * @param field
	 * @param errorCode
	 * @param defaultMessage 
	 * @param match 必须包含的字符
	 */
	public static void rejectIfFalse(Errors errors, String field,
			String errorCode, String defaultMessage) {
		Object value = errors.getFieldValue(field);
		if (value != null) {
			Boolean string = (Boolean) value;
			if (!string.booleanValue()) {
				errors.rejectValue("category.name.exists", "category_name_exists", null, defaultMessage);
			}
		}
	}

	/**
	 * 检测，是否包含空白
	 * @param errors
	 * @param field
	 * @param errorCode
	 * @param defaultMessage
	 */
	public static void rejectIfContainWhitespace(Errors errors, String field,
			String errorCode, String defaultMessage) {
		Object value = errors.getFieldValue(field);
		if (value != null) {
			String string = (String) value;
			if ((string.trim()).indexOf(" ") != -1) {
				errors.rejectValue(field, errorCode, null, defaultMessage);
			}
		}
	}

	/**
	 * 检测是否包含非数字字符
	 * @param errors
	 * @param field
	 * @param errorCode
	 * @param defaultMessage
	 */
	public static void rejectIfContainString(Errors errors, String field,
			String errorCode, String defaultMessage) {
		Object value = errors.getFieldValue(field);
		if (value != null) {
			String string = ((String) value).trim();
			for (int i = 0; i < string.length(); i++) {
				int temp = string.charAt(i);
				if (temp < '0' || temp > '9') {
					errors.rejectValue(field, errorCode, null, defaultMessage);
				}
			}

		}
	}

	/**
	 * 检测字符串长度是否长于某个值
	 * @param errors
	 * @param field
	 * @param errorCode
	 * @param defaultMessage
	 * @param length
	 */
	public static void rejectIfLongThan(Errors errors, String field,
			String errorCode, String defaultMessage, int length) {
		Object value = errors.getFieldValue(field);
		if (value != null) {
			String string = (String) value;
			if ((string.trim()).length() > length) {
				errors.rejectValue(field, errorCode, null, defaultMessage);
			}
		}
	}

	/**
	 * 检测字符串长度是否小于某个值
	 * @param errors
	 * @param field
	 * @param errorCode
	 * @param defaultMessage
	 * @param length
	 */
	public static void rejectIfShortThan(Errors errors, String field,
			String errorCode, String defaultMessage, int length) {
		Object value = errors.getFieldValue(field);
		if (value != null) {
			String string = (String) value;
			if ((string.trim()).length() < length) {
				errors.rejectValue(field, errorCode, null, defaultMessage);
			}
		}
	}

	/**
	 * 检测字符串长度是否在某个范围内
	 * @param errors
	 * @param field
	 * @param errorCode
	 * @param defaultMessage
	 * @param minLength
	 * @param maxLength
	 */
	public static void rejectIfLengthBeyond(Errors errors, String field,
			String errorCode, String defaultMessage, int minLength,
			int maxLength) {
		Object value = errors.getFieldValue(field);
		if (value != null) {
			String string = (String) value;
			if ((string.trim()).length() < minLength) {
				errors.rejectValue(field, errorCode, null, defaultMessage);
			}
			if ((string.trim()).length() > maxLength) {
				errors.rejectValue(field, errorCode, null, defaultMessage);
			}
		}
	}
	
	/**
	 * 检测字符串长度是否是一个合法的email地址
	 * @param errors
	 * @param field
	 * @param errorCode
	 * @param defaultMessage
	 */
	public static void rejectIfNotEmail(Errors errors, String field, String errorCode, String defaultMessage) {
		Object value = errors.getFieldValue(field);
		
		if(value == null){
			errors.rejectValue(field, errorCode, null, defaultMessage);
			return ;
		}
		
		String mail = (String) value;
		if(mail.length() < 5){
			errors.rejectValue(field, errorCode, null, defaultMessage);
			return ;
		}
		
		if(mail.indexOf('@') < 1 || mail.indexOf('.') < 1){
			errors.rejectValue(field, errorCode, null, defaultMessage);
			return ;
		}
		
	}
	
}
