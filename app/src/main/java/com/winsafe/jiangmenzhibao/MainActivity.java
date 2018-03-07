package com.winsafe.jiangmenzhibao;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.winsafe.jiangmenzhibao.activity.LoginActivity;
import com.winsafe.jiangmenzhibao.application.AppMgr;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.fragment.HomeFragment;
import com.winsafe.jiangmenzhibao.fragment.LogFragment;
import com.winsafe.jiangmenzhibao.fragment.MeFragment;
import com.winsafe.jiangmenzhibao.utils.GlobalHelper;
import com.winsafe.jiangmenzhibao.view.AppBaseFragmentActivity;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 首页
 * Created by shijie.yang on 2017/5/19.
 */
public class MainActivity extends AppBaseFragmentActivity {
    @BindView(R.id.tvHome)
    TextView tvHome;
    @BindView(R.id.llHome)
    LinearLayout llHome;
    @BindView(R.id.tvLog)
    TextView tvLog;
    @BindView(R.id.llLog)
    LinearLayout llLog;
    @BindView(R.id.tvMe)
    TextView tvMe;
    @BindView(R.id.llMe)
    LinearLayout llMe;
    @BindView(R.id.ivHome)
    ImageView ivHome;
    @BindView(R.id.ivLog)
    ImageView ivLog;
    @BindView(R.id.ivMe)
    ImageView ivMe;

    private HomeFragment homeFragment;
    private LogFragment logFragment;
    private MeFragment meFragment;

    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_main);
    }

    /**
     * 系统返回退出
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, GlobalHelper.showString(this, R.string.toast_press_again_quit), Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                AppMgr.getInstance().quit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void initView() {
        //销售--物流追溯
        if (LoginActivity.companyName.contains(AppConfig.COMPANYNAME_SALL) ||
                GlobalHelper.getUserName().equals(AppConfig.USER_ADMIN)  ) {
            //LoginActivity.companyName.contains(AppConfig.COMPANYNAME_HOUSE)
            llLog.setVisibility(View.INVISIBLE);
        } else {
            llLog.setVisibility(View.VISIBLE);
        }
        setTabSelection(0);
        ivHome.setBackgroundResource(R.mipmap.bar_b_1);
        ivLog.setBackgroundResource(R.mipmap.bar_a_2);
        ivMe.setBackgroundResource(R.mipmap.bar_a_3);
    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.llHome, R.id.llLog, R.id.llMe})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llHome:
                setTabSelection(0);
                ivHome.setBackgroundResource(R.mipmap.bar_b_1);
                ivLog.setBackgroundResource(R.mipmap.bar_a_2);
                ivMe.setBackgroundResource(R.mipmap.bar_a_3);
                break;
            case R.id.llLog:
                setTabSelection(1);
                ivHome.setBackgroundResource(R.mipmap.bar_a_1);
                ivLog.setBackgroundResource(R.mipmap.bar_b_2);
                ivMe.setBackgroundResource(R.mipmap.bar_a_3);
                break;
            case R.id.llMe:
                setTabSelection(2);
                ivHome.setBackgroundResource(R.mipmap.bar_a_1);
                ivLog.setBackgroundResource(R.mipmap.bar_a_2);
                ivMe.setBackgroundResource(R.mipmap.bar_b_3);
                break;

            default:
                break;
        }
    }

    private void setTabSelection(int index) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                homeFragment = new HomeFragment();
                transaction.add(R.id.content, homeFragment);

                break;
            case 1:
                logFragment = new LogFragment();
                transaction.add(R.id.content, logFragment);
//			if (findFragment == null) {
//				findFragment = new AuditFragment(AppConfig.ORDER_AUDIT);
//				transaction.add(R.id.content, findFragment);
//			} else {
//				transaction.show(findFragment);
//			}
                break;

            case 2:
                meFragment = new MeFragment();
                transaction.add(R.id.content, meFragment);
                break;

        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (logFragment != null) {
            transaction.hide(logFragment);

        }
        if (meFragment != null) {
            transaction.hide(meFragment);

        }
    }
}
