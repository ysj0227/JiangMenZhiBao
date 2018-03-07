package com.winsafe.jiangmenzhibao.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.winsafe.jiangmenzhibao.R;

import java.util.Calendar;
import java.util.Date;


/**
 * 系统提示框：包括显示toast、progressdialog和alertdialog等方法。
 * 
 * @author Aaron.Zhao
 */
public class MyDialog {
	private static Toast mToast;
	private static ProgressDialog mProgressDialog;

	private MyDialog() {
	}

	/**
	 * 显示Toast信息
	 * 
	 * @param context 上下文
	 * @param msg 要显示的信息
	 */
	public static void showToast(Context context, String msg) {
		if (mToast == null) {
			if (msg == null)
				msg = "";

			if (msg.length() < 8)
				mToast = Toast.makeText(context, "default prompt message", Toast.LENGTH_SHORT);
			else
				mToast = Toast.makeText(context, "default prompt message", Toast.LENGTH_LONG);
		}

		mToast.setText(msg+"");
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.show();
	}

	/**
	 * 显示Toast信息,自定义显示样式
	 * 
	 * @param context 上下文
	 * @param msg 要显示的信息
	 */
	public static void showToastForCustom(Context context, String msg) {
		if (mToast == null) {
			if (msg == null)
				msg = "";

			if (msg.length() < 8)
				mToast = Toast.makeText(context, "default prompt message", Toast.LENGTH_SHORT);
			else
				mToast = Toast.makeText(context, "default prompt message", Toast.LENGTH_LONG);
		}

		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.activity_mydialog_toast_item, null);
		TextView tvMsg = (TextView) view.findViewById(R.id.tv0);
		tvMsg.setText(msg);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.setView(view);
		mToast.show();
		mToast = null;// 用完自定义Toast，就销毁
	}

	/**
	 * 显示弹出框
	 * 
	 * @param context 上下文
	 * @param titleResId 弹出框标题资源ID
	 * @param msgId 弹出框信息资源ID
	 * @param yesText 弹出框确认按钮文本
	 * @param noText 弹出框取消按钮文本
	 * @param positiveClickListener 弹出框确认按钮事件监听
	 * @param negtiveClickListener 弹出框取消按钮事件监听
	 * @return 返回弹出框
	 */
	public static AlertDialog showAlertDialog(Context context, int titleResId, int msgId, String yesText, String noText,
                                              DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnClickListener negtiveClickListener) {
		String strTitle = context.getResources().getString(titleResId);
		String strMessage = context.getResources().getString(msgId);
		return showAlertDialog(context, strTitle, strMessage, yesText, noText, positiveClickListener, negtiveClickListener);
	}

	/**
	 * 显示弹出框
	 * 
	 * @param context 上下文
	 * @param title 弹出框标题
	 * @param msg 弹出框信息
	 * @param yesText 弹出框确认按钮文本
	 * @param noText 弹出框取消按钮文本
	 * @param positiveClickListener 弹出框确认按钮事件监听
	 * @param negativeClickListener 弹出框取消按钮事件监听
	 * @return 返回弹出框
	 */
	public static AlertDialog showAlertDialog(Context context, String title, String msg, String yesText, String noText,
                                              DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnClickListener negativeClickListener) {
		Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton(yesText, positiveClickListener);
		if (negativeClickListener != null)
			builder.setNegativeButton(noText, negativeClickListener);
		builder.setCancelable(false);

		AlertDialog alertDialog = builder.show();
		return alertDialog;
	}

	/**
	 * 显示弹出框
	 * 
	 * @param context 上下文
	 * @param title 弹出框标题
	 * @param msg 弹出框信息
	 * @param yesText 弹出框确认按钮文本资源ID
	 * @param positiveClickListener 弹出框确认按钮事件监听
	 * @param cancelClickListener 基于手机的取消事件监听
	 * @return 返回弹出框
	 */
	public static AlertDialog showAlertDialog(Context context, String title, String msg, int yesText,
                                              DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnCancelListener cancelClickListener) {
		String strButtonText = context.getResources().getString(yesText);
		return showAlertDialog(context, title, msg, strButtonText, positiveClickListener, cancelClickListener);
	}

	/**
	 * 显示弹出框
	 * 
	 * @param context 上下文
	 * @param title 弹出框标题
	 * @param msg 弹出框信息
	 * @param yesText 弹出框确认按钮文本资源
	 * @param positiveClickListener 弹出框确认按钮事件监听
	 * @param cancelClickListener 基于手机的取消事件监听
	 * @return 返回弹出框
	 */
	public static AlertDialog showAlertDialog(Context context, String title, String msg, String yesText,
                                              DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnCancelListener cancelClickListener) {
		Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton(yesText, positiveClickListener);
		builder.setOnCancelListener(cancelClickListener);

		AlertDialog alertDialog = builder.show();
		return alertDialog;
	}

	/**
	 * 显示弹出框
	 * 
	 * @param context 上下文
	 * @param title 弹出框标题
	 * @param msg 弹出框信息
	 * @param yesText 弹出框确认按钮文本资源
	 * @param noText 弹出框取消按钮文本资源
	 * @param positiveClickListener 弹出框确认按钮事件监听
	 * @param neutralClickListener 基于普通按钮的监听事件
	 * @param negativeClickListener 基于手机的取消事件监听
	 * @return 返回弹出框
	 */
	public static AlertDialog showAlertDialog(Context context, int title, int msg, int yesText, int neutralText, int noText,
                                              DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnClickListener neutralClickListener,
                                              DialogInterface.OnClickListener negativeClickListener) {
		String strTitle = context.getResources().getString(title);
		String strMsg = context.getResources().getString(msg);
		String strYesButtonText = context.getResources().getString(yesText);
		String strNeutralText = context.getResources().getString(neutralText);
		String strNoText = context.getResources().getString(noText);

		Builder builder = new Builder(context);
		builder.setTitle(strTitle);
		builder.setIcon(R.drawable.ic_warn);
		builder.setMessage(strMsg);
		builder.setPositiveButton(strYesButtonText, positiveClickListener);
		builder.setNeutralButton(strNeutralText, neutralClickListener);
		builder.setNegativeButton(strNoText, negativeClickListener);

		AlertDialog alertDialog = builder.show();
		return alertDialog;
	}

	/**
	 * 显示弹出框
	 * 
	 * @param context 上下文
	 * @param title 弹出框标题
	 * @param msg 弹出框信息
	 * @param yesText 弹出框确认按钮文本资源
	 * @param neutralText 弹出框普通按钮文本资源
	 * @param noText 弹出框取消按钮文本资源
	 * @param positiveClickListener 弹出框确认按钮事件监听
	 * @param neutralClickListener 基于普通按钮的监听事件
	 * @param negativeClickListener 基于手机的取消事件监听
	 * @return 返回弹出框
	 */
	public static AlertDialog showAlertDialog(Context context, String title, String msg, String yesText, String neutralText, String noText,
                                              DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnClickListener neutralClickListener,
                                              DialogInterface.OnClickListener negativeClickListener) {
		Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setIcon(R.drawable.ic_warn);
		builder.setMessage(msg);
		builder.setPositiveButton(yesText, positiveClickListener);
		builder.setNeutralButton(neutralText, neutralClickListener);
		builder.setNegativeButton(noText, negativeClickListener);

		AlertDialog alertDialog = builder.show();
		return alertDialog;
	}

	/**
	 * 显示弹出框
	 * 
	 * @param context 上下文
	 * @param title 弹出框标题
	 * @param v 弹出框中所带的View
	 * @param negativeText 弹出框取消按钮文本资源
	 * @param negativeClickListener 基于手机的取消事件监听
	 * @return 返回弹出框
	 */
	public static AlertDialog showAlertDialog(Context context, String title, View v, String negativeText,
                                              DialogInterface.OnClickListener negativeClickListener) {
		Builder builder = new Builder(context);
		builder.setTitle(title);
		// builder.setIcon(R.drawable.ic_warn);
		builder.setView(v);
		builder.setNegativeButton(negativeText, negativeClickListener);

		AlertDialog alertDialog = builder.show();
		return alertDialog;
	}

	/**
	 * 显示弹出框
	 * 
	 * @param context 上下文
	 * @param title 弹出框标题
	 * @param v 弹出框中所带的View
	 * @param pPositiveText 弹出框指定功能按钮文本资源
	 * @param positiveClickListener 基于弹出框中指定按钮的事件监听
	 * @param pNegativeText 弹出框取消按钮文本资源
	 * @param negativeClickListener 基于手机的取消事件监听
	 * @return 返回弹出框
	 */
	public static AlertDialog showAlertDialog(Context context, String title, View v, String pPositiveText, String pNegativeText,
                                              DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnClickListener negativeClickListener) {
		Builder builder = new Builder(context);
		builder.setTitle(title);
		// builder.setIcon(R.drawable.ic_warn);
		builder.setView(v);
		builder.setPositiveButton(pPositiveText, positiveClickListener);
		builder.setNegativeButton(pNegativeText, negativeClickListener);

		AlertDialog alertDialog = builder.show();
		return alertDialog;
	}

	/**
	 * 不依附与Activity的弹出框
	 * 
	 * @param context 上下文
	 * @param title 弹出框标题
	 * @param msg 弹出框信息
	 * @param yesText 弹出框确认按钮文本
	 * @param noText 弹出框取消按钮文本
	 * @param positiveClickListener 弹出框确认按钮事件监听
	 * @param negativeClickListener 弹出框取消按钮事件监听
	 * @return 返回弹出框
	 */
	public static AlertDialog showSystemAlertDialog(Context context, String title, String msg, String yesText, String noText,
                                                    DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnClickListener negativeClickListener) {
		Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton(yesText, positiveClickListener);
		if (negativeClickListener != null)
			builder.setNegativeButton(noText, negativeClickListener);
		builder.setCancelable(false);

		AlertDialog alertDialog = builder.create();
		alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		alertDialog.show();
		return alertDialog;
	}

	/**
	 * 显示选择日期对话框
	 * 
	 * @param context 上下文
	 * @param callBack 回调事件
	 * @return 返回显示日期对话框对象
	 */
	public static DatePickerDialog showDatePickerDialog(Context context, OnDateSetListener callBack) {
		Calendar mCalendar = Calendar.getInstance();
		int year = mCalendar.get(Calendar.YEAR);
		int monthOfYear = mCalendar.get(Calendar.MONTH);
		int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog mDatePickerDialog = new DatePickerDialog(context, callBack, year, monthOfYear, dayOfMonth);
		mDatePickerDialog.show();
		return mDatePickerDialog;
	}

	/**
	 * 显示选择日期对话框
	 * 
	 * @param context 上下文
	 * @param callBack 回调事件
	 * @return 返回显示日期对话框对象
	 */
	public static DatePickerDialog showDatePickerDialog(Context context, OnDateSetListener callBack, Date date) {
		Calendar mCalendar = Calendar.getInstance();
		if (date != null)
			mCalendar.setTime(date);
		int year = mCalendar.get(Calendar.YEAR);
		int monthOfYear = mCalendar.get(Calendar.MONTH);
		int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog mDatePickerDialog = new DatePickerDialog(context, callBack, year, monthOfYear, dayOfMonth);
		mDatePickerDialog.show();
		return mDatePickerDialog;
	}

	/**
	 * 显示选择日期对话框
	 * 
	 * @param context 上下文
	 * @param callBack 回调事件
	 * @param isShowDay 是否显示日
	 * @return 返回显示日期对话框对象
	 */
	public static DatePickerDialog showDatePickerDialog(Context context, OnDateSetListener callBack, boolean isShowDay) {
		Calendar mCalendar = Calendar.getInstance();
		int year = mCalendar.get(Calendar.YEAR);
		int monthOfYear = mCalendar.get(Calendar.MONTH);
		int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog mDatePickerDialog = new DatePickerDialog(context, callBack, year, monthOfYear, dayOfMonth);
		mDatePickerDialog.show();

		if (!isShowDay) {
			int SDKVersion = CommonHelper.getSDKVersion();// 获取系统版本
			DatePicker datePicker = findDatePicker((ViewGroup) mDatePickerDialog.getWindow().getDecorView());// 设置弹出年月日
			if (datePicker != null) {
				if (SDKVersion < 11) {
					((ViewGroup) datePicker.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
				}
				else if (SDKVersion > 14) {
					((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
				}
			}
		}

		return mDatePickerDialog;
	}

	/**
	 * 显示选择年对话框
	 * 
	 * @param context 上下文
	 * @param callBack 回调事件
	 * @return 返回显示日期对话框对象
	 */
	public static DatePickerDialog showYearDatePickerDialog(Context context, OnDateSetListener callBack) {
		Calendar mCalendar = Calendar.getInstance();
		int year = mCalendar.get(Calendar.YEAR);
		int monthOfYear = mCalendar.get(Calendar.MONTH);
		int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog mDatePickerDialog = new DatePickerDialog(context, callBack, year, monthOfYear, dayOfMonth);
		mDatePickerDialog.show();

		int SDKVersion = CommonHelper.getSDKVersion();// 获取系统版本
		DatePicker datePicker = findDatePicker((ViewGroup) mDatePickerDialog.getWindow().getDecorView());// 设置弹出年月日
		if (datePicker != null) {
			if (SDKVersion < 11) {
				((ViewGroup) datePicker.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
				((ViewGroup) datePicker.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
			}
			else if (SDKVersion > 14) {
				((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
				((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
			}
		}

		return mDatePickerDialog;
	}

	/**
	 * 显示进度条
	 * 
	 * @param context 上下文
	 * @param titleResId 进度提示标题资源ID
	 * @param msgResId 进度提示内容资源ID
	 */
	public static void showProgressDialog(Context context, int titleResId, int msgResId) {
		String strTitle = context.getString(titleResId);
		String strMessage = context.getString(msgResId);

		showProgressDialog(context, strTitle, strMessage);
	}

	/**
	 * 显示进度条
	 * 
	 * @param context 上下文
	 * @param title 进度提示标题
	 * @param msg 进度提示内容
	 */
	public static void showProgressDialog(Context context, String title, String msg) {
		dismissProgressDialog();
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setTitle(title);
		mProgressDialog.setMessage(msg);
		mProgressDialog.setIcon(R.drawable.ic_warn);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setCancelable(true);
		mProgressDialog.show();
	}

	/**
	 * 取消进度条
	 */
	public static void dismissProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

	// ------------------------------
	// --------- 私有方法 -----------
	// ------------------------------
	/**
	 * 从当前Dialog中查找DatePicker子控件
	 * 
	 * @param group 控件组
	 * @return 返回DatePicker
	 */
	private static DatePicker findDatePicker(ViewGroup group) {
		if (group != null) {
			for (int i = 0, j = group.getChildCount(); i < j; i++) {
				View child = group.getChildAt(i);
				if (child instanceof DatePicker) {
					return (DatePicker) child;
				}
				else if (child instanceof ViewGroup) {
					DatePicker result = findDatePicker((ViewGroup) child);
					if (result != null)
						return result;
				}
			}
		}
		return null;
	}

}
