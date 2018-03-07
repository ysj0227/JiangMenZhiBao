package com.winsafe.jiangmenzhibao.utils;

import android.annotation.SuppressLint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;

public class StringHelper {
	private StringHelper() {}

	/**
	 * 替代字符串
	 * @param originalString 原始字符串
	 * @param length 指定长度
	 * @return 返回替代源字符串的字符串
	 */
	public static String subString(String originalString, int length) {
		String result = "";

		if (originalString != null && !originalString.equalsIgnoreCase("")) {
			if (length <= 0 || length >= originalString.length())
				result = originalString;
			else
				result = originalString.substring(0, length) + "...";
		}

		return result;
	}

	/**
	 * 判断字符串对象是否为null或者为空
	 * @param originalString 初始字符串
	 * @return true-空或者为null、false-非空或者不为null
	 */
	public static boolean isNullOrEmpty(String originalString) {
		if (originalString == null) return true;
		String str = originalString.trim();
		return str == null || str.length() == 0;
	}
	
	
	/**
	 * 判断字符串对象是否为null或者为空
	 *  初始字符串
	 * @return true-空或者为null、false-非空或者不为null
	 */
	public static boolean isEmpty(String strOriginal) {
		if (strOriginal == null) return true;
		String str = strOriginal.trim();
		return str == null || str.length() == 0;
	}
	

	/**
	 * 判断是否是一个中文汉字
	 * @param c 字符
	 * @return true-表示是中文汉字，false-表示是英文字母
	 */
	public static boolean isChineseChar(char c) {
		boolean result = false;

		try {
			// 如果字节数大于1是汉字，以这种方式区别英文字母和中文汉字并不是十分严谨
			result = String.valueOf(c).getBytes(ConfigHelper.TextCode.GBK).length > 1;
		}
		catch (UnsupportedEncodingException e) {
			LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()),
					"UnsupportedEncodingException:" + e.getMessage(), true);
		}

		return result;
	}

	/**
	 * 按字节截取字符串
	 * @param orignal 原始字符串
	 * @param textCode 字符串编码
	 * @param start 开始位置
	 * @param length 截取位数
	 * @return 截取后的字符串
	 */
	public static String substring(String orignal, String textCode, int start, int length) {
		String result = "";

		try {
			if (orignal != null && !"".equals(orignal)) {
				byte[] bytes = orignal.getBytes(textCode);

				if (length > 0 && length < bytes.length) {
					byte[] temps = new byte[length];
					for (int i = start; i < start + length; i++) {
						temps[i - start] = bytes[i];
					}

					result = new String(temps, textCode);
				}
			}
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * 如果对象值为null则返回null
	 * @param obj
	 * @return
	 */
	public static String valueOf(Object obj) {
		if (obj == null) return null;
		return String.valueOf(obj);
	}
	
	public static String toString(InputStream in, String encoding) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int count = -1;
        while ((count = in.read(buffer)) != -1) {
            outStream.write(buffer, 0, count);
        }
        return new String(outStream.toByteArray(), encoding);
	}
	
	public static String paddingLeft(String s, String paddingS, int count) {
		if (count <= 0) return s;
		if (isNullOrEmpty(paddingS)) return s;

		if (s == null) s = "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			sb.append(paddingS);
		}
		sb.append(s);
		
		return sb.toString();
	}
	
	public static String paddingRight(String s, String paddingS, int count) {
		if (count <= 0) return s;
		if (isNullOrEmpty(paddingS)) return s;

		if (s == null) s = "";
		StringBuilder sb = new StringBuilder(s);
		for (int i = 0; i < count; i++) {
			sb.append(paddingS);
		}
		
		return sb.toString();
	}
	
	public static String toString(Collection<String> lst, String split) {
		return toString(lst, null, null, split);
	}
	
	public static String toString(Collection<String> lst, String itemPrev, String itemPost, String split) {
		if (lst == null || lst.size() == 0) return "";
		StringBuilder sb = new StringBuilder();
		int i = -1;
		int lastIndex = lst.size() - 1;
		if (itemPrev == null) itemPrev = "";
		if (itemPost == null) itemPost = "";
		for (String s : lst) {
			i++;
			sb.append(itemPrev);
			sb.append(s);
			sb.append(itemPost);
			if (!isNullOrEmpty(split) && i < lastIndex) {
				sb.append(split);
			}
		}
		return sb.toString();
	}
	
	public static String startString(String s, int count) {
		if (isNullOrEmpty(s)) return "";
		if (s.length() <= count) return s;
		return s.substring(0, count);
	}
	
	public static String endString(String s, int count) {
		if (isNullOrEmpty(s)) return "";
		int beginIndex = s.length() - count;
		int endIndex = beginIndex + count;
		return s.substring(beginIndex, endIndex);
	}
	
	/**
	 * 以字节数来截取字符串
	 * @param s
	 * @param startIndex
	 * @param toIndex
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressLint("NewApi")
	public static String subStringInByte(String s, int startIndex, int toIndex) throws UnsupportedEncodingException {
		byte[] bytes = s.getBytes("GBK");
		byte[] sub = Arrays.copyOfRange(bytes, startIndex, toIndex);
		return new String(sub, "GBK");
	}
	
	/**
	 * 以字节数来截取字符串
	 * @param s
	 * @param startIndex
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressLint("NewApi")
	public static String subStringInByte(String s, int startIndex) throws UnsupportedEncodingException {
		byte[] bytes = s.getBytes("GBK");
		byte[] sub = Arrays.copyOfRange(bytes, startIndex, bytes.length);
		return new String(sub, "GBK");
	}
	
	public static int compare(String s1, String s2) {
		if (StringHelper.isNullOrEmpty(s1)) {
			return StringHelper.isNullOrEmpty(s2) ? 0 : -1;
		}
		if (StringHelper.isNullOrEmpty(s2)) {
			return 1;
		}
		return s1.compareTo(s2);
	}
	
	/**
	 * 如果s为null，则取value的值
	 * @param s
	 * @param value
	 * @return
	 */
	public static String nullValue(String s, String value) {
		return s == null ? value : s;
	}
	
	/**
	 * 如果s为空，则取value的值
	 * @param s
	 * @param value
	 * @return
	 */
	public static String emptyValue(String s, String value) {
		return isNullOrEmpty(s) ? value : s;
	}
}
