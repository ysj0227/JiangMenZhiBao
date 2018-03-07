package com.winsafe.jiangmenzhibao.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class BaseFragment extends Fragment {
	// ==============================================
	// ============ Widget initial ================
	// ==============================================
	protected View viewInit(View view, int resId) {
		return (View) view.findViewById(resId);
	}

	protected Button buttonInit(View view, int resId) {
		return (Button) view.findViewById(resId);
	}

	protected ImageButton imageButtonInit(View view, int resId) {
		return (ImageButton) view.findViewById(resId);
	}

	protected RadioButton radioButtonInit(View view, int resId) {
		return (RadioButton) view.findViewById(resId);
	}

	protected CompoundButton compoundButtonInit(View view, int resId) {
		return (CompoundButton) view.findViewById(resId);
	}

	protected ToggleButton toggleButtonInit(View view, int resId) {
		return (ToggleButton) view.findViewById(resId);
	}

	protected ZoomButton zoomButtonInit(View view, int resId) {
		return (ZoomButton) view.findViewById(resId);
	}

	protected TextView textViewInit(View view, int resId) {
		return (TextView) view.findViewById(resId);
	}

	protected CheckedTextView checkedTextViewInit(View view, int resId) {
		return (CheckedTextView) view.findViewById(resId);
	}

	protected AutoCompleteTextView autoCompleteTextViewInit(View view, int resId) {
		return (AutoCompleteTextView) view.findViewById(resId);
	}

	protected EditText editTextInit(View view, int resId) {
		return (EditText) view.findViewById(resId);
	}

	protected ListView listViewInit(View view, int resId) {
		return (ListView) view.findViewById(resId);
	}

	protected ImageView imageViewInit(View view, int resId) {
		return (ImageView) view.findViewById(resId);
	}

	protected GridView gridViewInit(View view, int resId) {
		return (GridView) view.findViewById(resId);
	}

	protected ScrollView scrollViewInit(View view, int resId) {
		return (ScrollView) view.findViewById(resId);
	}

	protected VideoView videoViewInit(View view, int resId) {
		return (VideoView) view.findViewById(resId);
	}

	protected RadioGroup radioGroupInit(View view, int resId) {
		return (RadioGroup) view.findViewById(resId);
	}

	protected DatePicker datePickerInit(View view, int resId) {
		return (DatePicker) view.findViewById(resId);
	}

	protected TimePicker timerPickerInit(View view, int resId) {
		return (TimePicker) view.findViewById(resId);
	}

	protected NumberPicker numberPickerInit(View view, int resId) {
		return (NumberPicker) view.findViewById(resId);
	}

	protected CheckBox checkBoxInit(View view, int resId) {
		return (CheckBox) view.findViewById(resId);
	}

	protected ProgressBar progressBarInit(View view, int resId) {
		return (ProgressBar) view.findViewById(resId);
	}

	protected Spinner spinnerInit(View view, int resId) {
		return (Spinner) view.findViewById(resId);
	}

	protected RatingBar ratingBarInit(View view, int resId) {
		return (RatingBar) view.findViewById(resId);
	}

	protected ImageSwitcher imageSwitcherInit(View view, int resId) {
		return (ImageSwitcher) view.findViewById(resId);
	}

	protected ViewSwitcher viewSwitcherInit(View view, int resId) {
		return (ViewSwitcher) view.findViewById(resId);
	}

	protected SeekBar seekBarInit(View view, int resId) {
		return (SeekBar) view.findViewById(resId);
	}

	protected TextSwitcher textSwitcherInit(View view, int resId) {
		return (TextSwitcher) view.findViewById(resId);
	}

	protected TabWidget tabWidgetInit(View view, int resId) {
		return (TabWidget) view.findViewById(resId);
	}

	protected TabHost tabHostInit(View view, int resId) {
		return (TabHost) view.findViewById(resId);
	}

	protected HorizontalScrollView horizontalScrollViewInit(View view, int resId) {
		return (HorizontalScrollView) view.findViewById(resId);
	}

	protected TableRow tableRowInit(View view, int resId) {
		return (TableRow) view.findViewById(resId);
	}

	protected TableLayout tableLayoutInit(View view, int resId) {
		return (TableLayout) view.findViewById(resId);
	}

	protected LinearLayout linearLayoutInit(View view, int resId) {
		return (LinearLayout) view.findViewById(resId);
	}

	protected FrameLayout frameLayoutInit(View view, int resId) {
		return (FrameLayout) view.findViewById(resId);
	}

	protected RelativeLayout relativeLayoutInit(View view, int resId) {
		return (RelativeLayout) view.findViewById(resId);
	}

	protected WebView webViewInit(View view, int resId) {
		return (WebView) view.findViewById(resId);
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
	 */
	protected void openActivity(Context context, Class<?> cls) {
		Intent intent = new Intent(context, cls);
		startActivity(intent);
	}

	/**
	 * Activity之间的跳转
	 * @param context 当前上下文
	 * @param cls 要跳转到的Activity类
	 * @param bundle 跳转时传递的参数
	 */
	protected void openActivity(Context context, Class<?> cls, Bundle bundle) {
		Intent intent = new Intent(context, cls);
		intent.putExtras(bundle);
		startActivity(intent);
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
		intent.putExtras(bundle);
		startActivityForResult(intent, requestCode);
	}
}
