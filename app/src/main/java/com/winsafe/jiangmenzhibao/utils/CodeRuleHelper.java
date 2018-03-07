package com.winsafe.jiangmenzhibao.utils;

import android.content.Context;
import android.widget.Toast;

import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.entity.ProductNoBean;
import com.winsafe.jiangmenzhibao.view.MyApp;

import java.util.List;

/**
 * 码规则的判断
 * Created by shijie.yang on 2017/6/3.
 */

public class CodeRuleHelper {
    public Context context;

    CodeRuleHelper(Context context) {
        this.context = context;
    }

    //物流码规则
    public static boolean logisticalRules(Context context, String code) {
        String mCode = code.contains("=") ? code.split("=")[1] : code;
        String mCodeFirst = mCode.substring(0, 1);
        if (mCode.length() == 16) {
            if ("8".equals(mCodeFirst) || "9".equals(mCodeFirst)) {
                return true;
            } else {
                Toast.makeText(context, GlobalHelper.showString(context, R.string.toast_code_wl_error), Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (mCode.length() == 30) {
            if ("J".equalsIgnoreCase(mCodeFirst)) {
                return true;
            } else {
                Toast.makeText(context, GlobalHelper.showString(context, R.string.toast_code_wl_error), Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (mCode.length() == 32) {
            return true;
        } else {
            Toast.makeText(context, GlobalHelper.showString(context, R.string.toast_code_wl_error), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //内外码码规则
    public static boolean inAndOutCodeRules(Context context, String code) {
        String mCode = code.contains("=") ? code.split("=")[1] : code;
        String mCodeFirst = mCode.substring(0, 1);
        if (mCode.length() == 16) {
            if ("8".equals(mCodeFirst) || "9".equals(mCodeFirst)) {
                return true;
            } else {
                Toast.makeText(context, GlobalHelper.showString(context, R.string.toast_code_bm_error), Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (mCode.length() == 30) {
            if ("J".equalsIgnoreCase(mCodeFirst)) {
                return true;
            } else {
                Toast.makeText(context, GlobalHelper.showString(context, R.string.toast_code_bm_error), Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (mCode.length() == 32) {
            return true;
        } else {
            Toast.makeText(context, GlobalHelper.showString(context, R.string.toast_code_bm_error), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //出库退货规则
    public static boolean codeRules(Context context, String code) {
        String mCode = code.contains("=") ? code.split("=")[1] : code;
        String mCodeFirst = mCode.substring(0, 1);

        if (!"J".equalsIgnoreCase(mCodeFirst)) {
            Toast.makeText(context, GlobalHelper.showString(context, R.string.toast_code_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mCode.length() != 30) {
            Toast.makeText(context, GlobalHelper.showString(context, R.string.toast_code_30_length), Toast.LENGTH_SHORT).show();
            return false;
        }
        String mICode = mCode.substring(1, 4);
        List<ProductNoBean> las = MyApp.productNoDB.findAllByWhere(ProductNoBean.class, " ICode=\"" + mICode + "\"");
        if (las == null || las.size() == 0) {
            Toast.makeText(context, GlobalHelper.showString(context, R.string.toast_code_no_product), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
