package com.winsafe.jiangmenzhibao.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.activity.AboutAppActivity;
import com.winsafe.jiangmenzhibao.activity.ForgetPasswordActivity;
import com.winsafe.jiangmenzhibao.activity.LoginActivity;
import com.winsafe.jiangmenzhibao.application.APKUpdate;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.utils.CommonHelper;
import com.winsafe.jiangmenzhibao.utils.GlobalHelper;
import com.winsafe.jiangmenzhibao.utils.NetworkHelper;
import com.winsafe.jiangmenzhibao.view.AppBaseFragment;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人中心
 * Created by shijie.yang on 2017/5/19.
 */

public class MeFragment extends AppBaseFragment {
    @BindView(R.id.llPsd)
    LinearLayout llPsd;
    @BindView(R.id.llUpdate)
    LinearLayout llUpdate;
    @BindView(R.id.llAbout)
    LinearLayout llAbout;
    @BindView(R.id.llClearCache)
    LinearLayout llClearCache;
    @BindView(R.id.btnOut)
    Button btnOut;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = setLayout(inflater, container, R.layout.fragment_me);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    protected void initView(LayoutInflater inflater, View v) {
        setHeader(inflater, v, "个人中心", false, 0, "", null);

        if (LoginActivity.companyName.contains(AppConfig.COMPANYNAME_SALL)||
                GlobalHelper.getUserName().equals(AppConfig.USER_ADMIN)) {
            //LoginActivity.companyName.contains(AppConfig.COMPANYNAME_HOUSE)
            //销售和仓库账号 --隐藏
            llClearCache.setVisibility(View.GONE);
        }else {
            llClearCache.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void setListener() {

    }
    //清理备份文件
    private void clearCache(File mFile){
        File[] files = mFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                File clearFile = new File(files[i].getPath());
                clearFile.delete();
            }
        }
    }

    @OnClick({R.id.llPsd, R.id.llUpdate, R.id.llAbout, R.id.btnOut, R.id.llClearCache})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llPsd://修改密码
                openActivity(getActivity(), ForgetPasswordActivity.class);
                break;
            case R.id.llAbout://关于软件
                openActivity(getActivity(), AboutAppActivity.class);
                break;
            case R.id.btnOut://退出
                openActivity(getActivity(),LoginActivity.class);
                getActivity().finish();
                break;
            case R.id.llClearCache://清理缓存文件
                String PathAfter = CommonHelper.getExternalStoragePath()
                        + String.format("/%s/%s", AppConfig.ROOTFOLDER, AppConfig.BACKUPFOLDER);
                String PathBefore = CommonHelper.getExternalStoragePath()
                        + String.format("/%s/%s/", AppConfig.ROOTFOLDER, AppConfig.UPLOADBACKUPFOLDER);

                File mFile = new File(PathAfter);
                File mFile1 = new File(PathBefore);
                if (mFile.exists()||mFile1.exists()) {
                    //遍历删除文件内容
                    clearCache(mFile);
                    clearCache(mFile1);
                }
                Toast.makeText(getActivity(), getResources().getString(R.string.toast_clear_success), Toast.LENGTH_SHORT).show();

                break;
            case R.id.llUpdate: //版本检测
                if (NetworkHelper.isNetworkAvailable(getActivity()) == true) {
                    //第二的参数：否提示有没有版本更新的toast
                    APKUpdate.checkAPKVersion(getActivity(), true);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.network_wifi_low), Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;

        }
    }

    //=====版本检测========
//    private void updateVersion() {
//        startDialogProgress("版本检测中");
//        String chkVerURL = "https://rtci-qa.bayer.cn/RTCI/phone/checkVersionAction.do";
//        String saveFileName = "RI4BKDBKR";
//
//        OkHttpClient mOkHttpClient = new OkHttpClient();
//        RequestBody formBody = new FormBody.Builder()
//                .add("IMEI_number", CommonHelper.getIMEI(getActivity()))
//                .add("appName", saveFileName)
//                .add("appVersion", CommonHelper.getAppVersionName(getActivity()))
//                .add("appType", "android")
//
//                .build();
//        Request request = new Request.Builder()
//                .url(chkVerURL)
//                .post(formBody)
//                .build();
//        Call call = mOkHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, final IOException e) {
//                stopDialogProgress();
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getActivity(), getResources().getString(R.string.network_wifi_low), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                stopDialogProgress();
//                final String str = response.body().string();
//                getActivity().runOnUiThread(new Runnable() { //这是Activity的方法，会在主线程执行任务,不然会报错
//                    @Override
//                    public void run() {
////                        Log.i("TAG", str);
//                        try {
//                            JSONObject object = new JSONObject(str);
//                            String code = object.getString("returnCode");
//                            String msg = object.getString("returnMsg");
//                            String apkUrl = object.getString("downloadUrl");
//                            if ("0".equals(code)) {//有新版本
//                                updateApk(apkUrl);
//                            } else {//没有新版本
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//
//
//        });
//    }
//
//    // 更新
//    private void updateApk(final String url) {
//        final Dialog dialog;
//        dialog = new Dialog(getActivity(), R.style.Son_dialog);
//        LayoutInflater inflater = ((Activity) getActivity()).getLayoutInflater();
//        View view = inflater.inflate(R.layout.dialog_update_apk, null);
//        final TextView tvTip = (TextView) view.findViewById(R.id.tvTip);
//        TextView tv_sure = (TextView) view.findViewById(R.id.tv_sure);
//        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
//        final ProgressBar pd = (ProgressBar) view.findViewById(R.id.ProgressBar);
//        final LinearLayout llButton = (LinearLayout) view.findViewById(R.id.llButton);
//        pd.setVisibility(View.INVISIBLE);
//
//        tv_sure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tvTip.setText("正在更新···");
//                pd.setVisibility(View.VISIBLE);
//                llButton.setVisibility(View.GONE);
//                new Thread() {
//                    @Override
//                    public void run() {
//                        try {
//                            File file = Update.getFileFromServerBar(url, pd);
//                            sleep(3000);
//                            installApk(file);
//                            dialog.dismiss();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }.start();
//            }
//        });
//        tv_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.setContentView(view);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//        dialog.show();
//    }
//
//    // 安装apk
//    protected void installApk(File file) {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addCategory("android.intent.category.DEFAULT");
//        // 执行的数据类型
//        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        startActivity(intent);
//    }
}
