package com.winsafe.jiangmenzhibao.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.activity.DownLoadActivity;
import com.winsafe.jiangmenzhibao.activity.HistoryCodeActivity;
import com.winsafe.jiangmenzhibao.activity.LoginActivity;
import com.winsafe.jiangmenzhibao.activity.UploadFilesActivity;
import com.winsafe.jiangmenzhibao.activity.billHave.BillOutActivity;
import com.winsafe.jiangmenzhibao.activity.billHave.BillReturnActivity;
import com.winsafe.jiangmenzhibao.activity.billNot.BillNotOutActivity;
import com.winsafe.jiangmenzhibao.activity.billNot.BillNotReturnActivity;
import com.winsafe.jiangmenzhibao.adapter.DefaultPagerAdapter;
import com.winsafe.jiangmenzhibao.adapter.HomeAdapter;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.utils.CollectionHelper;
import com.winsafe.jiangmenzhibao.utils.MyDialog;
import com.winsafe.jiangmenzhibao.view.AppBaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页
 * Created by shijie.yang on 2017/5/19.
 */

public class HomeFragment extends AppBaseFragment {

    @BindView(R.id.vp0)
    ViewPager vpMenu;
    @BindView(R.id.llPointGroup)
    ViewGroup llMenuPointGroup;

    private int vpMenuHeight;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = setLayout(inflater, container, R.layout.fragment_home);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    protected void initView(LayoutInflater inflater, View v) {
        setHeader(inflater, v, "首页", false, 0, "", null);
        // 获得全局viewTree的观察者，并添加 全局的layout监听

        ((View) vpMenu.getParent()).getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        // 由于此方法会执行多次，而我们只需要执行一次就可以了，
                        // 所以，在执行一次的时候，将全局的layout监听取消，此处this指的是，内部的匿名对象
                        // ((View) vpMenu.getParent())
                        // .getViewTreeObserver()
                        // .removeGlobalOnLayoutListener(this);
                        vpMenuHeight = ((View) vpMenu.getParent()).getHeight();
                    }
                });

        initMenu();


    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View v) {

    }

    @SuppressWarnings("deprecation")
    private void initMenu() {
        final List<Map<String, Object>> menuMapList = new ArrayList<Map<String, Object>>();
        if (LoginActivity.companyName.contains(AppConfig.COMPANYNAME_SALL)) {  //销售
            menuMapList.add(HomeAdapter.createMap(R.mipmap.home_item_fw_normal, "物流追溯", 0, null, null));
        }else if (LoginActivity.companyName.contains(AppConfig.COMPANYNAME_HOUSE)) {//仓库
            menuMapList.add(HomeAdapter.createMap(R.mipmap.home_item_fw_normal, "内外码查询", 0, null, null));
        }else {
            if (AppConfig.NOT_BILL.equals(LoginActivity.isHaveBill)) {
                menuMapList.add(HomeAdapter.createMap(R.mipmap.home_item_out_normal, "无单出库", 0, BillNotOutActivity.class, null));
                menuMapList.add(HomeAdapter.createMap(R.mipmap.home_item_th_normal, "无单退货", 0, BillNotReturnActivity.class, null));
            } else {
                menuMapList.add(HomeAdapter.createMap(R.mipmap.home_item_out_normal, "有单出库", 0, BillOutActivity.class, null));
                menuMapList.add(HomeAdapter.createMap(R.mipmap.home_item_th_normal, "有单退货", 0, BillReturnActivity.class, null));
            }
            menuMapList.add(HomeAdapter.createMap(R.mipmap.home_item_upload_files, "上传文件", 0, UploadFilesActivity.class, null));

            if (!LoginActivity.companyName.contains(AppConfig.COMPANYNAME_LINGSHOU)) { //零售
                menuMapList.add(HomeAdapter.createMap(R.mipmap.home_customer_normal, "客商管理", 0, null, null));
            }
            menuMapList.add(HomeAdapter.createMap(R.mipmap.home_item_wl_normal, "物流追踪", 0, null, null));
            menuMapList.add(HomeAdapter.createMap(R.mipmap.home_item_history_normal, "扫描历史", 0, HistoryCodeActivity.class, null));
            menuMapList.add(HomeAdapter.createMap(R.mipmap.home_item_update_normal, "数据更新", 0, DownLoadActivity.class, null));
        }

        int totalCount = menuMapList.size();
        int pageSize = 9;
        int pageCount = totalCount / 9;
        if (pageCount == 0)
            pageCount = 1;
        else if (totalCount % 9 > 0) {
            pageCount += 1;
        }
        if (totalCount == 0) {
            MyDialog.showToast(getActivity(), "该用户没有功能权限");
            return;
        }

        List<View> menuViewList = new ArrayList<View>(pageCount);
        final List<View> navViewList = new ArrayList<View>();
        for (int i = 0; i < pageCount; i++) {
            View menuView = getActivity().getLayoutInflater().inflate(R.layout.fragment_home_gridview, null);
            final GridView gv = (GridView) menuView.findViewById(R.id.gvMenu);
            //
            List<Map<String, Object>> lst = CollectionHelper.take(menuMapList, i * pageSize, pageSize);
            BaseAdapter adapter = new HomeAdapter(getActivity(), lst, R.layout.activity_home_item) {
                @SuppressLint("NewApi")
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    return view;
                }

                @SuppressWarnings("unchecked")
                @Override
                public void onItemClick(int position, View view, ViewGroup parent) {
                    Map<String, Object> item = (Map<String, Object>) getItem(position);
                    Class<?> c = (Class<?>) item.get(FIELD_ACTIVITY_CLASS);
//					if (c == null) {
//						MyDialog.showToast(getActivity(), "该用户暂无权限");
//						return;
//					}
                    super.onItemClick(position, view, parent);
                }
            };
            gv.setAdapter(adapter);
            //
            menuViewList.add(menuView);
            View v = new View(getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(5, 5);
            if (i > 0) {
                layoutParams.leftMargin = 5;
                v.setEnabled(false);
            }
            v.setLayoutParams(layoutParams);
            v.setBackgroundResource(R.drawable.point_background);
            navViewList.add(v);
            //
            llMenuPointGroup.addView(v);
        }

        vpMenu.setAdapter(new DefaultPagerAdapter(menuViewList));
        vpMenu.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < navViewList.size(); i++) {
                    navViewList.get(i).setEnabled(i == position);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }
}
