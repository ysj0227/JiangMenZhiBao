package com.winsafe.jiangmenzhibao.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.entity.LogDetailsBean;
import com.winsafe.jiangmenzhibao.utils.GlobalHelper;
import com.winsafe.jiangmenzhibao.view.AppBaseActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 日志详情
 * Created by shijie.yang on 2017/5/23.
 */

public class LogDetailsActivity extends AppBaseActivity {

    @BindView(R.id.tvCodeName)
    TextView tvCodeName;
    @BindView(R.id.tvCons)
    TextView tvCons;
    private Bundle bundle;
    String logCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.layout_log_des);
    }


    private void getData(){
        startDialogProgress("查询中···");
        OkHttpUtils.post()
                .url(AppConfig.URL_APPGETUPLOADIDCODELOGDETAIL)
                .addParams(AppConfig.USERNAME, GlobalHelper.getUserName())
                .addParams(AppConfig.PASSWORD, GlobalHelper.getPassword())
                .addParams("logCode", logCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        stopDialogProgress();
                        Toast.makeText(LogDetailsActivity.this, getResources().getString(R.string.network_wifi_low), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, String s) {
                        stopDialogProgress();
                        LogDetailsBean bean = new Gson().fromJson(s, LogDetailsBean.class);

                        if ("0".equals(bean.getReturnCode())) {
                            String data=bean.getLogDetailList().toString();
                            String nn="";
                            String mStr="";
                            if (data.contains(",")){
                                nn=data.replaceAll(",","");
                            }else {
                                nn=data;
                            }

                            if (nn.substring(0,1).contains("[")){
                                mStr=nn.substring(1,nn.length()-1);
                            }else {
                                mStr=nn;
                            }
                            tvCons.setText(mStr);

                        }else if("-2".equals(bean.getReturnCode())){
                            Toast.makeText(LogDetailsActivity.this, bean.getReturnMsg(), Toast.LENGTH_SHORT).show();
                            openActivity(LogDetailsActivity.this, LoginActivity.class,true);
                        }  else {
                            Toast.makeText(LogDetailsActivity.this, bean.getReturnMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void initView() {
        setHeader("日志详情", true, false, 0, "", null);
        bundle=getIntent().getExtras();
        logCode=bundle.getString("logCode");
        getData();

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
