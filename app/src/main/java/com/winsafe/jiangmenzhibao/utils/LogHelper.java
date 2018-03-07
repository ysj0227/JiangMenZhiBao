package com.winsafe.jiangmenzhibao.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class LogHelper {
	public Context context;
	private LogHelper(Context context) {
		this.context=context;
	}

	/**
	 * 导出日志
	 * @param className 类名称
	 * @param methodName 方法名称
	 * @param message 错误消息
	 * @param isSaveLog 是否保存到文件
	 */
	public static void exportLog(String className, String methodName, String message, boolean isSaveLog) {
		if (isSaveLog)
			writeLog(className, methodName, message);
		else
			printLog(className, methodName, message);
	}

	/**
	 * 输出正常日志
	 * @param className 类名称
	 * @param methodName 方法名称
	 * @param message 要输出的信息
	 */
	public static void outLog(String className, String methodName, String message) {
		try {
			String strTag = className + "-->" + methodName + ":" + message;
			System.out.println(strTag);
		}
		catch (Exception e) {
			Log.e("LogHelper-->printLog-->", "Exception:" + e.getMessage());
		}
	}

	// ==============================================
	// ============ Private method ================
	// ==============================================
	private static void printLog(String className, String methodName, String message) {
		try {
			String strTag = className + "-->" + methodName;
			Log.e(strTag, message);
		}
		catch (Exception e) {
			Log.e("LogHelper-->printLog-->", "Exception:" + e.getMessage());
		}
	}

	private static void writeLog(String className, String methodName, String message) {
		try {
			File filePath = null;
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				filePath = new File(Environment.getExternalStorageDirectory().getCanonicalPath() + "/winsafe/");
				writeFile(filePath, className, methodName, message);
			}
		}
		catch (IOException e) {
			Log.e("LogHelper-->writeLog-->", "IOException:" + e.getMessage());
		}
		catch (Exception e) {
			Log.e("LogHelper-->writeLog-->", "Exception:" + e.getMessage());
		}
	}

	@SuppressLint("LongLogTag")
	private static void writeFile(File filePath, String className, String methodName, String message) {
		try {
			if (!filePath.exists()) {
				filePath.mkdirs();
			}

			String strFileName = DateTimeHelper.getStringDateTimeByStringPattern(DateTimeHelper.DateTimePattern.SHORT_DATETIME_1) + ".txt";
			File file = new File(filePath, strFileName);
			FileWriter fileWriter = new FileWriter(file.getAbsolutePath(), file.exists());

			String strMsg = getMessage(className, methodName, message);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(strMsg);
			bufferedWriter.close();
		}
		catch (IOException e) {
			Log.e("LogHelper-->writeFile-->", "IOException:" + e.getMessage());
		}
		catch (Exception e) {
			Log.e("LogHelper-->writeFile-->", "Exception:" + e.getMessage());
		}
	}

	private static String getMessage(String className, String methodName, String message) {
		String strMessage = "";

		String strTime = DateTimeHelper.getStringDateTimeByStringPattern(DateTimeHelper.DateTimePattern.LONG_DATETIME_1);
		StringBuffer builder = new StringBuffer();
		builder.append("TRACE__TIME:" + strTime + "\n");
		builder.append("CLASS__NAME:" + className + "\n");
		builder.append("METHOD_NAME:" + methodName + "\n");
		builder.append("TRACE__INFO:" + message + "\n\r");
		strMessage = builder.toString();

		return strMessage;
	}

}
