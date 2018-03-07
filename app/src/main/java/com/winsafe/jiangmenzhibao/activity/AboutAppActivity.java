package com.winsafe.jiangmenzhibao.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.utils.CommonHelper;
import com.winsafe.jiangmenzhibao.view.AppBaseActivity;

import butterknife.BindView;

/**
 * Created by shijie.yang on 2017/5/22.
 */

public class AboutAppActivity extends AppBaseActivity {
    @BindView(R.id.tvAppName)
    TextView tvAppName;
    @BindView(R.id.tvVersion)
    TextView tvVersions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.layout_about_app);

    }

    @Override
    protected void initView() {
        setHeader("关于软件", true,false, 0, " ", null);
        tvAppName.setText("软件名称："+getResources().getString(R.string.app_name));
        tvVersions.setText("当前版本："+ CommonHelper.getAppVersionName(this));
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
