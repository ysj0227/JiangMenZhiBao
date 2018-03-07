package com.winsafe.jiangmenzhibao.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.activity.LoginActivity;
import com.winsafe.jiangmenzhibao.activity.customer.CityCustomerBranchMangerActivity;
import com.winsafe.jiangmenzhibao.activity.customer.CountyCustomerMangerActivity;
import com.winsafe.jiangmenzhibao.activity.customer.ProvinceCustomerMangerActivity;
import com.winsafe.jiangmenzhibao.confing.AppConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author shijie.yang
 */
public class HomeAdapter extends SimpleAdapter {
    public static final String FIELD_IMAGE_ID = "imageId";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_TIPS_COUNT = "tipsCount";
    public static final String FIELD_ACTIVITY_CLASS = "activityClass";
    public static final String FIELD_ACTIVITY_BUNDLE = "activityBundle";
    public static final String FIELD_ENABLED = "enabled";

    private boolean titleVisible = true;
    private boolean imageVisible = true;

    private Activity activity;

    public HomeAdapter(Activity activity, List<? extends Map<String, ?>> data, int resource) {
        super(activity, data, resource, new String[]{}, new int[]{});
        this.activity = activity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final View view = super.getView(position, convertView, parent);
        ImageView ivImage = (ImageView) view.findViewById(R.id.ivImage);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvCount = (TextView) view.findViewById(R.id.tvCount);
        View lmain = view.findViewById(R.id.lmain);

        Map<String, Object> item = (Map<String, Object>) getItem(position);
        int imageId = (Integer) item.get(FIELD_IMAGE_ID);
        final String title = (String) item.get(FIELD_TITLE);
        int tipsCount = (Integer) item.get(FIELD_TIPS_COUNT);

        ivImage.setImageResource(imageId);
        tvTitle.setText(title);
        tvCount.setText(String.valueOf(tipsCount));

        ivImage.setVisibility(imageVisible ? View.VISIBLE : View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvCount.setVisibility(tipsCount > 0 ? View.VISIBLE : View.GONE);
        if (lmain == null)
            lmain = view;
        lmain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (title.contains("追踪")) {
                    Intent it = new Intent(activity, CaptureActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(AppConfig.OPERATE_TYPE, AppConfig.OPERATE_TYPE_ZS);
                    bundle.putString(CaptureActivity.SCAN_MODE, CaptureActivity.SCAN_REPEAT);
                    it.putExtras(bundle);
                    activity.startActivity(it);
                }
                if (title.contains("内外码")) {
                    Intent it = new Intent(activity, CaptureActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(AppConfig.OPERATE_TYPE, AppConfig.OPERATE_TYPE_CODE_QUERY);
                    bundle.putString(CaptureActivity.SCAN_MODE, CaptureActivity.SCAN_REPEAT);
                    it.putExtras(bundle);
                    activity.startActivity(it);
                }
                if (title.contains("客商")) {
                    Intent it = null;
                    if (LoginActivity.companyName.contains(AppConfig.COMPANYNAME_PROVINCE)){
                        //省账号
                        it = new Intent(activity, ProvinceCustomerMangerActivity.class);
                    }else if(LoginActivity.companyName.contains(AppConfig.COMPANYNAME_CITY)){
                        //市账号
                        it = new Intent(activity, CityCustomerBranchMangerActivity.class);
                    }else if(LoginActivity.companyName.contains(AppConfig.COMPANYNAME_COUNTRY)){
                        //县账号
                        it = new Intent(activity, CountyCustomerMangerActivity.class);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(AppConfig.ENCODE, LoginActivity.encode);
                    bundle.putString(AppConfig.COMPANYTYPE, LoginActivity.companyType);
                    it.putExtras(bundle);
                    activity.startActivity(it);
                }

                onItemClick(position, view, parent);

            }
        });

        return view;
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("unchecked")
    public void onItemClick(int position, View view, ViewGroup parent) {
        Map<String, Object> item = (Map<String, Object>) getItem(position);
        boolean enabled = (Boolean) item.get(FIELD_ENABLED);
        if (!enabled)
            return;
        //
        Class<?> activityClass = (Class<?>) item.get(FIELD_ACTIVITY_CLASS);
        if (activityClass == null)
            return;
        //
        Bundle bundle = (Bundle) item.get(FIELD_ACTIVITY_BUNDLE);
        if (bundle == null) {
            activity.startActivity(new Intent(activity, activityClass));
        } else {
            activity.startActivity(new Intent(activity, activityClass), bundle);
        }
    }

    public static Map<String, Object> createMap(int imageId, String title) {
        return createMap(imageId, title, 0, null, null);
    }

    public static Map<String, Object> createMap(int imageId, String title, int tipsCount, Class<?> activityClass,
                                                Bundle bundle) {
        return createMap(imageId, title, tipsCount, activityClass, bundle, true);
    }

    public static Map<String, Object> createMap(int imageId, String title, Class<?> activityClass) {
        return createMap(imageId, title, activityClass, null);
    }

    public static Map<String, Object> createMap(int imageId, String title, Class<?> activityClass, Bundle bundle) {
        return createMap(imageId, title, 0, activityClass, bundle);
    }

    public static Map<String, Object> createMap(int imageId, String title, int tipsCount, Class<?> activityClass,
                                                Bundle bundle, boolean enabled) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(FIELD_ACTIVITY_BUNDLE, bundle);
        map.put(FIELD_ACTIVITY_CLASS, activityClass);
        map.put(FIELD_TIPS_COUNT, tipsCount);
        map.put(FIELD_IMAGE_ID, imageId);
        map.put(FIELD_TITLE, title);
        map.put(FIELD_ENABLED, enabled);
        return map;
    }

    public boolean isTitleVisible() {
        return titleVisible;
    }

    public void setTitleVisible(boolean titleVisible) {
        this.titleVisible = titleVisible;
    }

    public boolean isImageVisible() {
        return imageVisible;
    }

    public void setImageVisible(boolean imageVisible) {
        this.imageVisible = imageVisible;
    }

    public Activity getActivity() {
        return activity;
    }
}
