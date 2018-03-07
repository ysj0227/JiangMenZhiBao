package com.winsafe.jiangmenzhibao.activity;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.winsafe.jiangmenzhibao.MainActivity;
import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.application.APKUpdate;
import com.winsafe.jiangmenzhibao.application.AppMgr;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.entity.LoginBean;
import com.winsafe.jiangmenzhibao.entity.OrganDataBean;
import com.winsafe.jiangmenzhibao.entity.WarehouseDataBean;
import com.winsafe.jiangmenzhibao.utils.CommonHelper;
import com.winsafe.jiangmenzhibao.utils.GlobalHelper;
import com.winsafe.jiangmenzhibao.utils.NetworkHelper;
import com.winsafe.jiangmenzhibao.utils.StringHelper;
import com.winsafe.jiangmenzhibao.view.AppBaseActivity;
import com.winsafe.jiangmenzhibao.view.MyApp;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author shijie.yang 登录
 */
@SuppressLint("NewApi")
public class LoginActivity extends AppBaseActivity {
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.llForgetPsd)
    LinearLayout llForgetPsd;
    @BindView(R.id.tvGetPsd)
    TextView tvGetPsd;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    private boolean mFlag;
    //客户账户类型
    public static List<LoginBean.ReturnDataBean.DealerTypeAreaBeanX.COMPANYTYPEBean> valueList;
    public static String companyType = "";//登录账号类型
    public static String companyName = "";//登录账号名称
    public static String encode = "";
    public static String isHaveBill = ""; //0无单，1有单
    public static String dealerTypeArea = ""; //经销商区域

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_login);
    }

    /**
     * 禁用返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            AppMgr.getInstance().quit();
//            return true;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        etUserName.setText(MyApp.shared.getValueByKey(AppConfig.USERNAME));
        etPassword.setText(MyApp.shared.getValueByKey(AppConfig.PASSWORD));
    }

    @Override
    protected void initView() {
        /* 如果SDK版本大于等于23、或者Android系统版本是6.0以上，那么需要动态请求创建文件的权限 */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        //自动检测更新
        if (NetworkHelper.isNetworkAvailable(this) == true) {
            APKUpdate.checkAPKVersion(this, false);
        }
    }

    @Override
    protected void setListener() {
    }

    /* 根据获取的授权创建下载文件夹 */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //创建APP更新时需要的文件夹
                    String sdRoot = CommonHelper.getExternalStoragePath();
                    if (!sdRoot.equals("")) {
                        String downloadApkPath = sdRoot + "/" + CommonHelper.getAppPackageName(this);
                        File newFilePath = new File(downloadApkPath);
                        if (!newFilePath.exists()) {
                            newFilePath.mkdirs();
                        }
                    }
                }
                break;
        }
    }

    @OnClick({R.id.btnLogin, R.id.tvGetPsd})
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        Drawable checkBoxOn = getResources().getDrawable(R.drawable.check_on);
        checkBoxOn.setBounds(0, 0, checkBoxOn.getMinimumWidth(), checkBoxOn.getMinimumHeight());

        Drawable checkBoxOff = getResources().getDrawable(R.drawable.check_off);
        checkBoxOff.setBounds(0, 0, checkBoxOff.getMinimumWidth(), checkBoxOff.getMinimumHeight());

        switch (v.getId()) {
            case R.id.btnLogin://登录
                login();
                break;
            case R.id.tvGetPsd://记住密码
                if (mFlag) {
                    tvGetPsd.setCompoundDrawables(checkBoxOn, null, null, null);
                    mFlag = false;
                } else {
                    tvGetPsd.setCompoundDrawables(checkBoxOff, null, null, null);
                    mFlag = true;
                }
                break;

            default:
                break;
        }

    }

    private void login() {
        final String userName = etUserName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (StringHelper.isNullOrEmpty(userName)) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringHelper.isNullOrEmpty(password)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        startDialogProgress("登录中");
        Map<String, String> map = new HashMap<>();
        map.put(AppConfig.USERNAME, userName);
        map.put(AppConfig.PASSWORD, password);
        OkHttpUtils.post()
                .url(AppConfig.URL_LOGIN)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        stopDialogProgress();
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.network_wifi_low), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, String s) {
//                        Log.i("TAG", s);
                        stopDialogProgress();
                        LoginBean bean = new Gson().fromJson(s, LoginBean.class);
                        if ("0".equals(bean.getReturnCode())) {
                            LoginBean.ReturnDataBean bs = bean.getReturnData();
                            //显示的所有用户
                            valueList = bs.getDealerTypeArea().getCOMPANYTYPE();
                            for (LoginBean.ReturnDataBean.DealerTypeAreaBeanX.COMPANYTYPEBean mm : valueList) {
                                if (bs.getCompanytype().equals(mm.getKey())) {
                                    companyName = mm.getValue();
                                    break;
                                }
                            }
                            //仓库账号
                            if (companyName.contains(AppConfig.COMPANYNAME_HOUSE)&&!AppConfig.USER_ADMIN.equals(userName)) {
                                Toast.makeText(LoginActivity.this, "该用户没有功能权限", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            //是否有单
                            LoginBean.ReturnDataBean.AppIsHaveBILLBean lho = bs.getAppIsHaveBILL();
                            //经销商的【机构类型】的区间2、3、4，分别为省、市、县。区间最大值5为零售商
                            LoginBean.ReturnDataBean.DealerTypeAreaBeanX.DealerTypeAreaBean organType = bs.getDealerTypeArea().getDealerTypeArea();

                            companyType = bs.getCompanytype();//账号类型
                            encode = bs.getEncode(); //新增客商encode
                            isHaveBill = lho.getValue();//是否有单 (0无单，1有单)
                            dealerTypeArea = organType.getValue(); //经销商区域

                            //如果上次登陆的用户名和本次登陆不一致删除下载的机构，仓库数据
                            if (!GlobalHelper.getUserName().equals(userName)) {
                                MyApp.organDB.deleteAll(OrganDataBean.class);
                                MyApp.warehouseDB.deleteAll(WarehouseDataBean.class);
                            }
                            //保存用户名
                            MyApp.shared.saveValueByKey(AppConfig.USERNAME, etUserName.getText().toString().trim());

                            if (!mFlag) {
                                MyApp.shared.saveValueByKey(AppConfig.PASSWORD, etPassword.getText().toString().trim());
                            } else {
                                MyApp.shared.remove(AppConfig.PASSWORD);
                            }
                            openActivity(LoginActivity.this, MainActivity.class, false);
                        } else {
                            Toast.makeText(LoginActivity.this, bean.getReturnMsg(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    //经销商区域名称
    public static String dealerAreaName() {
        String area[] = LoginActivity.dealerTypeArea.split(",");
        String nn = "";
        for (int i = 0; i < area.length; i++) {
            String str = area[i];
            for (LoginBean.ReturnDataBean.DealerTypeAreaBeanX.COMPANYTYPEBean mm : LoginActivity.valueList) {
                if (str.equals(mm.getKey())) {
                    nn += mm.getValue() + "/";
                }
            }
        }
        return nn;
    }

}
