package com.winsafe.jiangmenzhibao.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.utils.GlobalHelper;
import com.winsafe.jiangmenzhibao.view.AppBaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by shijie.yang on 2017/6/14.
 */

public class UploadFilesActivity extends AppBaseActivity {
    @BindView(R.id.btnUploadCk)
    Button btnUploadCk;
    @BindView(R.id.btnUploadTh)
    Button btnUploadTh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.layout_upload_files);

    }

    @Override
    protected void initView() {

        setHeader("上传文件", true, false, 0, "", null);
        if (AppConfig.NOT_BILL.equals(LoginActivity.isHaveBill)) {
            //无单
            btnUploadCk.setText(GlobalHelper.showString(this,R.string.button_upload_ck_not));
            btnUploadTh.setText(GlobalHelper.showString(this,R.string.button_upload_th_not));
        } else {
            //有单
            btnUploadCk.setText(GlobalHelper.showString(this,R.string.button_upload_ck));
            btnUploadTh.setText(GlobalHelper.showString(this,R.string.button_upload_th));
        }
    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.btnUploadCk, R.id.btnUploadTh})

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUploadCk://出库
                Bundle bundle = new Bundle();
                bundle.putString(AppConfig.SKIP_ACTIVITY, AppConfig.SKIP_UPLOADFILESACTIVITY);//文件上传页面跳转
                if (AppConfig.NOT_BILL.equals(LoginActivity.isHaveBill)) {
                    //无单
                    bundle.putString(AppConfig.OPERATE_TYPE, AppConfig.OPERATE_TYPE_NOT_CK);
                } else {
                    //有单
                    bundle.putString(AppConfig.OPERATE_TYPE, AppConfig.OPERATE_TYPE_CK);
                }
                openActivity(UploadFilesActivity.this, UploadCodeFilesActivity.class, bundle, false);

                break;
            case R.id.btnUploadTh://退货
                Bundle bundles = new Bundle();
                bundles.putString(AppConfig.SKIP_ACTIVITY, AppConfig.SKIP_UPLOADFILESACTIVITY);//文件上传页面跳转
                if (AppConfig.NOT_BILL.equals(LoginActivity.isHaveBill)) {
                    //无单
                    bundles.putString(AppConfig.OPERATE_TYPE, AppConfig.OPERATE_TYPE_NOT_TH);
                } else {//有单
                    bundles.putString(AppConfig.OPERATE_TYPE, AppConfig.OPERATE_TYPE_TH);
                }
                openActivity(UploadFilesActivity.this, UploadCodeFilesActivity.class, bundles, false);
                break;

            default:
                break;

        }

    }
}
