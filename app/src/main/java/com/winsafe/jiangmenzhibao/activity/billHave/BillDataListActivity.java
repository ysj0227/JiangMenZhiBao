package com.winsafe.jiangmenzhibao.activity.billHave;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.activity.LoginActivity;
import com.winsafe.jiangmenzhibao.adapter.BillAdpter;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.entity.OrganBean;
import com.winsafe.jiangmenzhibao.entity.ScanOutBean;
import com.winsafe.jiangmenzhibao.utils.GlobalHelper;
import com.winsafe.jiangmenzhibao.view.AppBaseActivity;
import com.winsafe.jiangmenzhibao.view.MyApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 单据列表
 * Created by shijie.yang on 2017/5/21.
 */

public class BillDataListActivity extends AppBaseActivity {
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.lvListData)
    ListView lvListData;
    private Bundle mBundle=null;
    private String billsortType="";//单据类型

    private List<OrganBean> newList=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.layout_out_list);

    }

    @Override
    protected void initView() {
        setHeader("选择机构", true, false, 0, "", null);
        mBundle=getIntent().getExtras();
        billsortType=mBundle.getString("BillSort");

        getList("");
        search();

        lvListData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrganBean bean=newList.get(position);
                Intent it=getIntent();

                it.putExtra("customID",bean.getCustomID());
                it.putExtra("fromOrgID",bean.getFromOrgID());
                it.putExtra("fromWHName",bean.getFromWHName());
                it.putExtra("billSort",bean.getBillSort());
                it.putExtra("fromWHID",bean.getFromWHID());
                it.putExtra("toOrgID",bean.getToOrgID());
                it.putExtra("toWHName",bean.getToWHName());
                it.putExtra("toOrgName",bean.getToOrgName());
                it.putExtra("totalCount",bean.getTotalCount());
                it.putExtra("billNo",bean.getBillNo());
                it.putExtra("billID",bean.getBillID());
                it.putExtra("toWHID",bean.getToWHID());
                it.putExtra("fromOrgName",bean.getFromOrgName());
                setResult(RESULT_OK, it);
                finish();
            }
        });

        //显示输入法
        GlobalHelper.stateSoftWareShow(this,etSearch);
    }
    //获取单据数据
    private void getList(final String key){
        OkHttpClient httpClient=new OkHttpClient();
        RequestBody body=new FormBody.Builder()
                .add(AppConfig.USERNAME, MyApp.shared.getValueByKey(AppConfig.USERNAME))
                .add(AppConfig.PASSWORD, MyApp.shared.getValueByKey(AppConfig.PASSWORD))
                .add("Billsort",billsortType)
                .build();
        Request request=new Request.Builder()
                .url(AppConfig.URL_APPQUERYTAKETICKET)
                .post(body)
                .build();
        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                stopDialogProgress();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BillDataListActivity.this, getResources().getString(R.string.network_wifi_low), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                stopDialogProgress();
                final String str = response.body().string();
                final ScanOutBean bean = new Gson().fromJson(str, ScanOutBean.class);
                final List<ScanOutBean.ReturnDataBean> list=bean.getReturnData();
                //筛选
                OrganBean bs=null;
                newList=new ArrayList<>();
                newList.clear();
                for (int i = 0; i < list.size(); i++) {
                    bs=new OrganBean();
                    if ("".equals(key) || list.get(i).getBillNo().contains(key)||list.get(i).getBillID().contains(key)) {
                        bs.setCustomID(list.get(i).getCustomID());
                        bs.setFromOrgID(list.get(i).getFromOrgID());
                        bs.setFromWHName(list.get(i).getFromWHName());
                        bs.setBillSort(list.get(i).getBillSort());
                        bs.setFromWHID(list.get(i).getFromWHID());
                        bs.setToOrgID(list.get(i).getToOrgID());
                        bs.setToWHName(list.get(i).getToWHName());
                        bs.setToOrgName(list.get(i).getToOrgName());
                        bs.setTotalCount(list.get(i).getTotalCount());
                        bs.setBillNo(list.get(i).getBillNo());
                        bs.setBillID(list.get(i).getBillID());
                        bs.setToWHID(list.get(i).getToWHID());
                        bs.setFromOrgName(list.get(i).getFromOrgName());
                        newList.add(bs);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Log.i("TAG", str);
                        if ("0".equals(bean.getReturnCode())){
                            lvListData.setAdapter(new BillAdpter(BillDataListActivity.this,newList));
                        }else if("-2".equals(bean.getReturnCode())){
                            Toast.makeText(BillDataListActivity.this, bean.getReturnMsg(), Toast.LENGTH_SHORT).show();
                            openActivity(BillDataListActivity.this, LoginActivity.class,true);
                        } else{
                            Toast.makeText(BillDataListActivity.this, bean.getReturnMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    //搜索
    private void search(){
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getList(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @Override
    protected void setListener() {
    }
    @Override
    public void onClick(View v) {

    }
}
