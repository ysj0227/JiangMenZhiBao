package com.winsafe.jiangmenzhibao.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.entity.SaveDBBean;
import com.winsafe.jiangmenzhibao.utils.DateYearMonthSelect;
import com.winsafe.jiangmenzhibao.utils.GlobalHelper;
import com.winsafe.jiangmenzhibao.view.AppBaseActivity;
import com.winsafe.jiangmenzhibao.view.MyApp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 扫描历史
 * Created by shijie.yang on 2017/6/8.
 */

public class HistoryCodeActivity extends AppBaseActivity {
    @BindView(R.id.tvStartDate)
    TextView tvStartDate;
    @BindView(R.id.ivStartDate)
    ImageView ivStartDate;
    @BindView(R.id.lvHistory)
    ListView lvHistory;
    @BindView(R.id.tvTip)
    TextView tvTip;

    private HistoryAdapter adapter;
    private List<SaveDBBean> list = null;
    private List<SaveDBBean> mList = new ArrayList<SaveDBBean>();
    private String dateSelect = "";

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String startDate = String.format("%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
            dateSelect = String.format("%d%02d%02d", year, monthOfYear + 1, dayOfMonth);
            tvStartDate.setText(startDate);


            getCodeList(dateSelect);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_history);

    }

    @Override
    protected void initView() {
        setHeader("扫描历史", true, false, 0, "", null);
        defaultDate();
        getCodeList(dateSelect);
    }

    private void getCodeList(String key) {
        list = MyApp.historyDB.findAllByWhere(SaveDBBean.class, " userID=\"" + GlobalHelper.getUserName() + "\"");
        SaveDBBean bean = null;
        mList.clear();
        for (int i = 0; i < list.size(); i++) {
            bean = new SaveDBBean();
            bean.setScannerDate(list.get(i).getScannerDate());
            bean.setBarCode(list.get(i).getBarCode());
            bean.setScannerType(list.get(i).getScannerType());
            if ("".equals(key) || list.get(i).getScannerDate().contains(key)) {
                mList.add(bean);
            }

        }
        //排序
        Collections.sort(mList, new Comparator<SaveDBBean>() {
            @Override
            public int compare(SaveDBBean o1, SaveDBBean o2) {
                int a = Integer.valueOf(o1.getScannerDate().substring(8, o1.getScannerDate().length()));
                int b = Integer.valueOf(o2.getScannerDate().substring(8, o2.getScannerDate().length()));
                return b - a;
            }
        });

        if (mList == null || mList.size() == 0) {
            mList.clear();
            lvHistory.invalidateViews();
            GlobalHelper.setTipVisible(tvTip,lvHistory);
//            Toast.makeText(this, "没有扫描历史", Toast.LENGTH_SHORT).show();
        } else {
            GlobalHelper.setTipGone(tvTip,lvHistory);
            adapter = new HistoryAdapter(HistoryCodeActivity.this, mList);
            lvHistory.setAdapter(adapter);
        }
    }

    /*
         * 初始化默认日期
         */
    private void defaultDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatterOther = new SimpleDateFormat("yyyyMMdd");
        String todayTime = formatter.format(c.getTime());
        dateSelect = formatterOther.format(c.getTime());

        tvStartDate.setText(todayTime);
    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.ivStartDate})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivStartDate:
                DateYearMonthSelect.showYearMonthDatePickerDialog(this, mDateSetListener).show();

                break;

            default:
                break;

        }

    }

    class HistoryAdapter extends BaseAdapter {
        private Context context;
        private List<SaveDBBean> list;
        private LayoutInflater inflater = null;

        public HistoryAdapter(Context context, List<SaveDBBean> list) {
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
            SaveDBBean bean = list.get(position);

            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.activity_history_item, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (AppConfig.SAVE_CODE_CK.equals(bean.getScannerType()) ||
                    AppConfig.SAVE_CODE_NOT_CK.equals(bean.getScannerType())) {
                holder.tvCodeType.setText("出库");
            } else if (AppConfig.SAVE_CODE_TH.equals(bean.getScannerType()) ||
                    AppConfig.SAVE_CODE_NOT_TH.equals(bean.getScannerType())) {
                holder.tvCodeType.setText("退货");
            }


            holder.tvTime.setText(bean.getScannerDate());
            holder.tvCode.setText(bean.getBarCode());

            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.tvCodeType)
            TextView tvCodeType;
            @BindView(R.id.tvTime)
            TextView tvTime;
            @BindView(R.id.tvCode)
            TextView tvCode;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
