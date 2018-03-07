package com.winsafe.jiangmenzhibao.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.activity.LogDetailsActivity;
import com.winsafe.jiangmenzhibao.activity.LoginActivity;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.entity.LogBean;
import com.winsafe.jiangmenzhibao.utils.DateYearMonthSelect;
import com.winsafe.jiangmenzhibao.utils.GlobalHelper;
import com.winsafe.jiangmenzhibao.view.AppBaseFragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 日志
 * Created by shijie.yang on 2017/5/19.
 */

public class LogFragment extends AppBaseFragment {
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.ivType)
    ImageView ivType;
    @BindView(R.id.tvStartDate)
    TextView tvStartDate;
    @BindView(R.id.ivStartDate)
    ImageView ivStartDate;
    @BindView(R.id.lvLogs)
    ListView lvLogs;
    @BindView(R.id.SwipeRefreshLog)
    SwipeRefreshLayout SwipeRefreshLog;
    @BindView(R.id.tvTip)
    TextView tvTip;

//    private List<LogBean> list = new ArrayList<>();
    private LogAdapter adpter;
    private String mTime = "", billSort = "";
    private List<LogBean.ReturnDataBean> list;

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String startDate = String.format("%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
            mTime = startDate;
            tvStartDate.setText(startDate);
            getLogDataList(billSort);

        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = setLayout(inflater, container, R.layout.fragment_log);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    protected void initView(LayoutInflater inflater, View v) {
        setHeader(inflater, v, "上传日志", false, 0, "", null);
        //初始化默认出库
        if (AppConfig.NOT_BILL.equals(LoginActivity.isHaveBill)) {
            billSort = AppConfig.GET_LOG_NOT_CK;//无单出库
        } else {
            billSort = AppConfig.GET_LOG_CK;//有单出库
        }
        defaultDate();
        getLogDataList(billSort);
        otherSetting();
    }

    /*
     * 初始化默认日期
    */
    private void defaultDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String todayTime = formatter.format(c.getTime());
        mTime = todayTime;
        tvStartDate.setText(todayTime);
    }

    private void otherSetting() {
        //item详情
        lvLogs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogBean.ReturnDataBean bean=list.get(position);
                if (bean.getErrorCount()==0){
                 return;
                }

                Bundle bundle=new Bundle();
                bundle.putString("logCode",bean.getCode());
                openActivity(getActivity(), LogDetailsActivity.class,bundle);
            }
        });
        // 设置下拉刷新时的颜色值,颜色值需要定义在xml中
        SwipeRefreshLog.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);

        //自带刷新列表
        SwipeRefreshLog.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLogDataList(billSort);
                SwipeRefreshLog.setRefreshing(false);
//                SwipeRefreshLog.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        getLogDataList(billSort);
//                        SwipeRefreshLog.setRefreshing(false);
//                    }
//                },800);
            }
        });
    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.ivType, R.id.ivStartDate})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOther://查询
                getLogDataList(billSort);
                break;
            case R.id.ivType:
                selectType("日志类型");
                break;
            case R.id.ivStartDate://日期选择
                DateYearMonthSelect.showYearMonthDatePickerDialog(getActivity(), mDateSetListener).show();
                break;

            default:
                break;

        }
    }

    //获取日志列表
    private void getLogDataList(String billsort) {
        startDialogProgress("查询中···");
        Map<String, String> map = new HashMap<>();
        map.put(AppConfig.USERNAME, GlobalHelper.getUserName());
        map.put(AppConfig.PASSWORD, GlobalHelper.getPassword());
        map.put("fromdate", mTime);
        map.put("billsort", billsort);
        OkHttpUtils.post()
                .url(AppConfig.URL_APPGETUPLOADIDCODELOGLIST)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        stopDialogProgress();
                        Toast.makeText(getActivity(), getResources().getString(R.string.network_wifi_low), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(Call call, String s) {
                        stopDialogProgress();
                        final LogBean bean = new Gson().fromJson(s, LogBean.class);
                        list = bean.getReturnData();
                        if ("0".equals(bean.getReturnCode())) {
                            if (list.size()==0){
                                GlobalHelper.setTipVisible(tvTip,lvLogs);
                            }else {
                                adpter = new LogAdapter(getActivity(), list);
                                lvLogs.setAdapter(adpter);
                                GlobalHelper.setTipGone(tvTip,lvLogs);
                            }

                        }else if("-2".equals(bean.getReturnCode())){
                            Toast.makeText(getActivity(), bean.getReturnMsg(), Toast.LENGTH_SHORT).show();
                            openActivity(getActivity(), LoginActivity.class);
                            getActivity().finish();
                        }  else {
                            Toast.makeText(getActivity(), bean.getReturnMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    //选择日志类型
    private void selectType(String msg) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_log_dialog, null);

        final Dialog dialog = new AlertDialog.Builder(getActivity())
                .setMessage(msg)
                .setView(view)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        return;
                    }
                }).create();
        final TextView tvOut = (TextView) view.findViewById(R.id.tvOut);
        final TextView tvReturn = (TextView) view.findViewById(R.id.tvReturn);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tvOut://出库
                        if (AppConfig.NOT_BILL.equals(LoginActivity.isHaveBill)) {
                            billSort = AppConfig.GET_LOG_NOT_CK;//无单出库
                        } else {
                            billSort = AppConfig.GET_LOG_CK;//有单出库
                        }
                        tvType.setText(tvOut.getText().toString());
                        dialog.dismiss();
                        getLogDataList(billSort);
                        break;
                    case R.id.tvReturn://退货
                        if (AppConfig.NOT_BILL.equals(LoginActivity.isHaveBill)) {
                            billSort = AppConfig.GET_LOG_NOT_TH;//无单退货
                        } else {
                            billSort = AppConfig.GET_LOG_TH;//有单退货
                        }
                        tvType.setText(tvReturn.getText().toString());
                        dialog.dismiss();
                        getLogDataList(billSort);
                        break;

                    default:
                        break;

                }
            }
        };
        tvOut.setOnClickListener(listener);
        tvReturn.setOnClickListener(listener);
        //部分android手机加入以下注释的两行会报错
//        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT); //TYPE_SYSTEM_ALERT
//        dialog.setCanceledOnTouchOutside(true);//禁止点击屏幕消失
        dialog.setCancelable(true);//返回键
        dialog.show();

    }

    class LogAdapter extends BaseAdapter {
        private Context context;
        private List<LogBean.ReturnDataBean> list;
        private LayoutInflater inflater = null;

        public LogAdapter(Context context, List<LogBean.ReturnDataBean> list) {
            this.context = context;
            this.list = list;
            inflater = LayoutInflater.from(context);
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
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LogBean.ReturnDataBean bean = list.get(position);

            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_log, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvCodeName.setText(bean.getCreationTime());

            holder.tvSuccess.setText(Html.fromHtml("成功：  " + "<font color=#60aa39>" + bean.getCorrectCount() + "</font>"));
            holder.tvFailed.setText(Html.fromHtml("失败：  " + "<font color=#fa965a>" + bean.getErrorCount() + "</font>"));

            return convertView;
        }

        public class ViewHolder {
            @BindView(R.id.tvCodeName)
            TextView tvCodeName;
            @BindView(R.id.tvSuccess)
            TextView tvSuccess;
            @BindView(R.id.tvFailed)
            TextView tvFailed;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
