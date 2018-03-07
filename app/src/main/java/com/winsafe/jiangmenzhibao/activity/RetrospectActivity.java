package com.winsafe.jiangmenzhibao.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.entity.logisticsBean;
import com.winsafe.jiangmenzhibao.utils.GlobalHelper;
import com.winsafe.jiangmenzhibao.view.AppBaseActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by shijie.yang on 2017/3/30.
 * 追溯详情
 */

public class RetrospectActivity extends AppBaseActivity {
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.lv_logistics)
    ListView lvLogistics;
    private List<logisticsBean> list = new ArrayList<logisticsBean>();
    private ReservationDetailsAdapter adapter;
    private Bundle bundle;
    private String code = "", ip = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_ret);

    }

    @Override
    protected void initView() {
        setHeader("追踪结果", true, false, 0, "", null);
        bundle = getIntent().getExtras();
        code = bundle.getString("code");
        getIp();
        getQueryResult();
    }

    @Override
    protected void setListener() {
    }

    @Override
    public void onClick(View v) {
    }

    //获取ip地址
    private void getIp() {
        //公网地址
        OkHttpUtils.post().url("http://pv.sohu.com/cityjson?ie=utf-8")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(Call call, String s) {
                        String str = s.split("=")[1];
                        try {
                            JSONObject object = new JSONObject(str);
                            ip = object.getString("cip");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //获取物流查询结果
    private void getQueryResult() {
        startDialogProgress("查询中···");
        OkHttpUtils.post()
                .url(AppConfig.URL_APPGETPRODUCTFLOW)
                .addParams(AppConfig.USERNAME, GlobalHelper.getUserName())
                .addParams("idcode", code)
                .addParams("ip", ip)
                .build()
                .connTimeOut(1000*40)
                .readTimeOut(1000*40)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        stopDialogProgress();
                        Toast.makeText(RetrospectActivity.this, getResources().getString(R.string.network_wifi_low), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, String s) {
                        stopDialogProgress();
                        if (s.contains("<html>")) {
                            Toast.makeText(RetrospectActivity.this,
                                    GlobalHelper.showString(RetrospectActivity.this, R.string.toast_not_message), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        logisticsBean bean = new Gson().fromJson(s, logisticsBean.class);
                        if ("0".equals(bean.getReturnCode())) {
                            List<logisticsBean.FlowListBean> list = bean.getFlowList();
                            adapter = new ReservationDetailsAdapter(RetrospectActivity.this, list);
                            lvLogistics.setAdapter(adapter);
                        } else if ("-2".equals(bean.getReturnCode())) {
                            Toast.makeText(RetrospectActivity.this, bean.getReturnMsg(), Toast.LENGTH_SHORT).show();
                            openActivity(RetrospectActivity.this, LoginActivity.class, true);
                        } else {
                            tvMessage.setVisibility(View.VISIBLE);
                            lvLogistics.setVisibility(View.GONE);
                            Toast.makeText(RetrospectActivity.this, bean.getReturnMsg(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    class ReservationDetailsAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<logisticsBean.FlowListBean> list;

        public ReservationDetailsAdapter(Context context, List<logisticsBean.FlowListBean> list) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final logisticsBean.FlowListBean coup = list.get(position);
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.activity_logistics_list, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                holder.vLineCircle = (View) convertView.findViewById(R.id.vLineCircle);
                holder.vLineTop = (View) convertView.findViewById(R.id.vLineTop);
                holder.vLineBottom = (View) convertView.findViewById(R.id.vLineBottom);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_time.setText("商品从 " + coup.getOutWarehouse() + " 发货，到 " + coup.getInWarehouse() + " 收货");
            holder.tv_name.setText(coup.getAuditTime());

            if (list.size() == 1) {//列表只有一个
                holder.vLineBottom.setVisibility(View.INVISIBLE);
                holder.vLineTop.setVisibility(View.INVISIBLE);
                holder.vLineCircle.setBackgroundResource(R.drawable.circle_green_wl); //shap_circle_green
            } else if (list.size() > 1) {//列表数据大于1
                if (position == (list.size() - 1)) {
                    holder.vLineBottom.setVisibility(View.INVISIBLE);
                    holder.vLineTop.setVisibility(View.VISIBLE);
                    holder.vLineCircle.setBackgroundResource(R.drawable.circle_grey_wl);
                } else if (position == 0) {
                    holder.vLineBottom.setVisibility(View.VISIBLE);
                    holder.vLineTop.setVisibility(View.INVISIBLE);
                    holder.vLineCircle.setBackgroundResource(R.drawable.circle_green_wl); //shap_circle_green
                } else {
                    holder.vLineCircle.setBackgroundResource(R.drawable.circle_grey_wl);
                    holder.vLineTop.setVisibility(View.VISIBLE);
                    holder.vLineBottom.setVisibility(View.VISIBLE);
                }
            }

            return convertView;
        }

        public class ViewHolder {
            public TextView tv_name;
            public TextView tv_time;
            public View vLineCircle;
            public View vLineTop;
            public View vLineBottom;

        }
    }
}
