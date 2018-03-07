package com.winsafe.jiangmenzhibao.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.webkit.WebView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import android.widget.VideoView;
import android.widget.ViewSwitcher;
import android.widget.ZoomButton;

/**
 * 所有Activity的基类，其中包含组件初始化、activity之间跳转以及读写文件等方法。
 * @author Aaron.Zhao
 */
public abstract class BaseActivity extends Activity {
	// ==============================================
	// ============ Widget initial ================
	// ==============================================
	protected View viewInit(int resId) {
		return (View) findViewById(resId);
	}

	protected Button buttonInit(int resId) {
		return (Button) findViewById(resId);
	}

	protected ImageButton imageButtonInit(int resId) {
		return (ImageButton) findViewById(resId);
	}

	protected RadioButton radioButtonInit(int resId) {
		return (RadioButton) findViewById(resId);
	}

	protected CompoundButton compoundButtonInit(int resId) {
		return (CompoundButton) findViewById(resId);
	}

	protected ToggleButton toggleButtonInit(int resId) {
		return (ToggleButton) findViewById(resId);
	}

	protected ZoomButton zoomButtonInit(int resId) {
		return (ZoomButton) findViewById(resId);
	}

	protected TextView textViewInit(int resId) {
		return (TextView) findViewById(resId);
	}

	protected CheckedTextView checkedTextViewInit(int resId) {
		return (CheckedTextView) findViewById(resId);
	}

	protected AutoCompleteTextView autoCompleteTextViewInit(int resId) {
		return (AutoCompleteTextView) findViewById(resId);
	}

	protected EditText editTextInit(int resId) {
		return (EditText) findViewById(resId);
	}

	protected ListView listViewInit(int resId) {
		return (ListView) findViewById(resId);
	}

	protected ImageView imageViewInit(int resId) {
		return (ImageView) findViewById(resId);
	}

	protected GridView gridViewInit(int resId) {
		return (GridView) findViewById(resId);
	}

	protected ScrollView scrollViewInit(int resId) {
		return (ScrollView) findViewById(resId);
	}

	protected VideoView videoViewInit(int resId) {
		return (VideoView) findViewById(resId);
	}

	protected RadioGroup radioGroupInit(int resId) {
		return (RadioGroup) findViewById(resId);
	}

	protected DatePicker datePickerInit(int resId) {
		return (DatePicker) findViewById(resId);
	}

	protected TimePicker timerPickerInit(int resId) {
		return (TimePicker) findViewById(resId);
	}

	protected NumberPicker numberPickerInit(int resId) {
		return (NumberPicker) findViewById(resId);
	}

	protected CheckBox checkBoxInit(int resId) {
		return (CheckBox) findViewById(resId);
	}

	protected ProgressBar progressBarInit(int resId) {
		return (ProgressBar) findViewById(resId);
	}

	protected Spinner spinnerInit(int resId) {
		return (Spinner) findViewById(resId);
	}

	protected RatingBar ratingBarInit(int resId) {
		return (RatingBar) findViewById(resId);
	}

	protected ImageSwitcher imageSwitcherInit(int resId) {
		return (ImageSwitcher) findViewById(resId);
	}

	protected ViewSwitcher viewSwitcherInit(int resId) {
		return (ViewSwitcher) findViewById(resId);
	}

	protected SeekBar seekBarInit(int resId) {
		return (SeekBar) findViewById(resId);
	}

	protected TextSwitcher textSwitcherInit(int resId) {
		return (TextSwitcher) findViewById(resId);
	}

	protected TabWidget tabWidgetInit(int resId) {
		return (TabWidget) findViewById(resId);
	}

	protected TabHost tabHostInit(int resId) {
		return (TabHost) findViewById(resId);
	}

	protected HorizontalScrollView horizontalScrollViewInit(int resId) {
		return (HorizontalScrollView) findViewById(resId);
	}

	protected TableRow tableRowInit(int resId) {
		return (TableRow) findViewById(resId);
	}

	protected TableLayout tableLayoutInit(int resId) {
		return (TableLayout) findViewById(resId);
	}

	protected LinearLayout linearLayoutInit(int resId) {
		return (LinearLayout) findViewById(resId);
	}

	protected FrameLayout frameLayoutInit(int resId) {
		return (FrameLayout) findViewById(resId);
	}

	protected RelativeLayout relativeLayoutInit(int resId) {
		return (RelativeLayout) findViewById(resId);
	}

	protected WebView webViewInit(int resId) {
		return (WebView) findViewById(resId);
	}

	protected ExpandableListView expandableListViewInit(int resId) {
		return (ExpandableListView) findViewById(resId);
	}

	// ==============================================
	// ======== Get message of string.xml =========
	// ==============================================
	/**
	 * 根据资源ID获取资源的字符串值
	 * @param resId 资源ID
	 * @return 返回获取到的值
	 */
	protected String getStringById(int resId) {
		return this.getResources().getString(resId);
	}

	/**
	 * 设置TextView的字体加粗
	 * @param textView TextView组件
	 * @param text 要显示的文字
	 */
	protected void setBoldTextForTextView(TextView textView, String text) {
		TextPaint textPaint = textView.getPaint();
		textPaint.setFakeBoldText(true);
		textView.setText(text);
	}

	// ==============================================
	// ========== Activity redirect ===============
	// ==============================================
	/**
	 * Activity之间的跳转
	 * @param context 当前上下文
	 * @param cls 要跳转到的Activity类
	 * @param isActivityFinish 是否销毁当前的Activity
	 */
	protected void openActivity(Context context, Class<?> cls, boolean isActivityFinish) {
		Intent intent = new Intent(context, cls);
		startActivity(intent);
		if (isActivityFinish)
			this.finish();
	}

	/**
	 * Activity之间的跳转
	 * @param context 当前上下文
	 * @param cls 要跳转到的Activity类
	 * @param bundle 跳转时传递的参数
	 * @param isActivityFinish 是否销毁当前的Activity
	 */
	protected void openActivity(Context context, Class<?> cls, Bundle bundle, boolean isActivityFinish) {
		Intent intent = new Intent(context, cls);
		intent.putExtras(bundle);
		startActivity(intent);
		if (isActivityFinish)
			this.finish();
	}

	/**
	 * Activity之间获取结果的跳转
	 * @param context 当前上下文
	 * @param cls 要跳转到的Activity类
	 * @param requestCode 获取结果时的请求码
	 */
	protected void openActivityForResult(Context context, Class<?> cls, int requestCode) {
		openActivityForResult(context, cls, requestCode, null);
	}

	/**
	 * Activity之间获取结果的跳转（需要API16版本以上才可以使用）
	 * @param context 当前上下文
	 * @param cls 要跳转到的Activity类
	 * @param requestCode 获取结果时的请求码
	 * @param bundle 跳转时传递的参数
	 */
	protected void openActivityForResult(Context context, Class<?> cls, int requestCode, Bundle bundle) {
		Intent intent = new Intent(context, cls);
		if (bundle != null)
			intent.putExtras(bundle);
		startActivityForResult(intent, requestCode);
	}

	// ==============================================
	// ============== Service operate ===============
	// ==============================================
	/**
	 * 开启服务
	 * @param context 当前上下文
	 * @param cls 要跳转到的Service类
	 * @param
	 * @param isActivityFinish 是否销毁当前的Activity
	 */
	protected void openService(Context context, Class<?> cls, Bundle bundle, boolean isActivityFinish) {
		Intent intent = new Intent(context, cls);
		intent.putExtras(bundle);
		startService(intent);
		if (isActivityFinish)
			this.finish();
	}

	/**
	 * 停止服务
	 * @param context 当前上下文
	 * @param cls 要停止的Service类
	 */
	protected void stopService(Context context, Class<?> cls) {
		Intent intent = new Intent(context, cls);
		stopService(intent);
	}
}
