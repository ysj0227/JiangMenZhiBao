package com.winsafe.jiangmenzhibao.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.entity.AlterPsdBean;
import com.winsafe.jiangmenzhibao.utils.EmojiEditText;
import com.winsafe.jiangmenzhibao.utils.GlobalHelper;
import com.winsafe.jiangmenzhibao.utils.StringHelper;
import com.winsafe.jiangmenzhibao.view.AppBaseActivity;
import com.winsafe.jiangmenzhibao.view.MyApp;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 修改密码
 */
public class ForgetPasswordActivity extends AppBaseActivity {
    @BindView(R.id.etOldPSD)
    EmojiEditText etOldPSD;
    @BindView(R.id.etNewPSD)
    EmojiEditText etNewPSD;
    @BindView(R.id.etNewPSD_Again)
    EmojiEditText etNewPSDAgain;
    @BindView(R.id.btnReset)
    Button btnReset;
    @BindView(R.id.btnGetPsd)
    Button btnGetPsd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_password);

    }

    @OnClick({R.id.btnGetPsd, R.id.btnReset})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGetPsd://确定
                alterPsd();
                break;

            case R.id.btnReset://重置
                etOldPSD.setText(null);
                etNewPSD.setText(null);
                etNewPSDAgain.setText(null);
                break;

            default:
                break;
        }
    }

    @Override
    protected void initView() {
        setHeader("修改密码", true, false, 0, "", null);
    }

    @Override
    protected void setListener() {

    }

    private boolean checkIsNull() {
        String oldPsd = etOldPSD.getText().toString().trim();
        String password = etNewPSD.getText().toString().trim();
        String passwordSure = etNewPSDAgain.getText().toString().trim();

        if (StringHelper.isNullOrEmpty(oldPsd)) {
            Toast.makeText(ForgetPasswordActivity.this, GlobalHelper.showString(this, R.string.toast_old_password), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringHelper.isNullOrEmpty(password)) {
            Toast.makeText(ForgetPasswordActivity.this, GlobalHelper.showString(this, R.string.toast_new_password), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length()<6||password.length()>25) {
            Toast.makeText(ForgetPasswordActivity.this, GlobalHelper.showString(this, R.string.toast_new_password_length), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.contains(" ")) {
            Toast.makeText(ForgetPasswordActivity.this, GlobalHelper.showString(this, R.string.toast_password_black), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (StringHelper.isNullOrEmpty(passwordSure)) {
            Toast.makeText(ForgetPasswordActivity.this, GlobalHelper.showString(this, R.string.toast_sure_password), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (passwordSure.contains(" ")) {
            Toast.makeText(ForgetPasswordActivity.this, GlobalHelper.showString(this, R.string.toast_password_black), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(passwordSure)) {
            Toast.makeText(ForgetPasswordActivity.this, GlobalHelper.showString(this, R.string.toast_notsame_password), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //修改密码
    private void alterPsd() {
        if (!checkIsNull()) {
            return;
        }
        OkHttpUtils.post()
                .url(AppConfig.URL_CHANGE_PWD)
                .addParams(AppConfig.USERNAME, GlobalHelper.getUserName())
                .addParams(AppConfig.PASSWORD, GlobalHelper.getPassword())
                .addParams("NewPassWord", etNewPSDAgain.getText().toString().trim())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(ForgetPasswordActivity.this, getResources().getString(R.string.network_wifi_low), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, String s) {
                        AlterPsdBean bean = new Gson().fromJson(s, AlterPsdBean.class);
                        if ("0".equals(bean.getReturnCode())) {
                            MyApp.shared.saveValueByKey(AppConfig.PASSWORD, etNewPSDAgain.getText().toString().trim());
                            Toast.makeText(ForgetPasswordActivity.this, bean.getReturnMsg(), Toast.LENGTH_SHORT).show();
                            ForgetPasswordActivity.this.finish();
                        }else if("-2".equals(bean.getReturnCode())){
                            Toast.makeText(ForgetPasswordActivity.this, bean.getReturnMsg(), Toast.LENGTH_SHORT).show();
                            openActivity(ForgetPasswordActivity.this, LoginActivity.class,true);
                        }  else {
                            Toast.makeText(ForgetPasswordActivity.this, bean.getReturnMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
