package com.winsafe.jiangmenzhibao.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.view.MyApp;

/**
 * Created by shijie.yang on 2017/6/7.
 */

public class GlobalHelper {
    private Context context;
    GlobalHelper(Context context){
        this.context=context;
    }

    public static String getUserName() {
        return MyApp.shared.getValueByKey(AppConfig.USERNAME);
    }

    public static String getPassword() {
        return MyApp.shared.getValueByKey(AppConfig.PASSWORD);
    }

    public static String showString(Context context, int str) {
        return context.getResources().getString(str);
    }
    //显示输入法
    public static void stateSoftWareShow(final Context context, EditText etSearch){
        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
                }
            }
        });
    }

    //提示显示
    public static void setTipVisible(TextView tvTip, ListView lv){
        tvTip.setVisibility(View.VISIBLE);
        lv.setVisibility(View.GONE);
    }
    //提示隐藏
    public static void setTipGone(TextView tvTip, ListView lv){
        tvTip.setVisibility(View.GONE);
        lv.setVisibility(View.VISIBLE);
    }
    public static boolean isMobileNO(String mobiles) {
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][34578]\\d{9}" ;
        if (TextUtils.isEmpty(mobiles)) return false ;
        else return mobiles.matches( telRegex ) ;
    }


}
