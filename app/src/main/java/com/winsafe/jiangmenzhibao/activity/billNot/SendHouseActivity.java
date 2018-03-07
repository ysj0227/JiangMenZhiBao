package com.winsafe.jiangmenzhibao.activity.billNot;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.adapter.HouseAdpter;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.entity.WarehouseDataBean;
import com.winsafe.jiangmenzhibao.utils.GlobalHelper;
import com.winsafe.jiangmenzhibao.view.AppBaseActivity;
import com.winsafe.jiangmenzhibao.view.MyApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 发货仓库
 * Created by shijie.yang on 2017/6/11.
 */

public class SendHouseActivity extends AppBaseActivity {
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.lvListData)
    ListView lvListData;
    private List<WarehouseDataBean> list=null;
    //筛选列表
    private List<WarehouseDataBean> mList=null;
    private Bundle mBundle=null;
    private String FromOrganID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.layout_out_list);


    }

    @Override
    protected void initView() {
        setHeader("选择发货仓库", true, false, 0, "", null);
        mBundle=getIntent().getExtras();
        FromOrganID=mBundle.getString("FromOrganID");
        getList();
        lvListData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WarehouseDataBean bean=mList.get(position);
                Intent it=getIntent();

                it.putExtra(AppConfig.DATA_NAME,bean.getWarehouseName());
                it.putExtra(AppConfig.DATA_ID,bean.getWarehouseID());
                setResult(RESULT_OK, it);
                finish();
            }
        });
        search();
        //显示输入法
        GlobalHelper.stateSoftWareShow(this,etSearch);
    }
    private void search(){
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getSearchList(s.toString());
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
    private void getList() {
        list= MyApp.warehouseDB.findAllByWhere(WarehouseDataBean.class,
                " type=\"" +AppConfig.AUTHORITY_SEND+ "\""+" and "+" organID=\"" + FromOrganID+ "\"");
        mList=list;
        lvListData.setAdapter(new HouseAdpter(SendHouseActivity.this,mList));

    }
    private void getSearchList(final String key) {
        WarehouseDataBean bean=null;
        mList=new ArrayList<WarehouseDataBean>();//必须此处定义
        mList.clear();
        for (int i = 0; i <list.size() ; i++) {
            bean=new WarehouseDataBean();
            bean.setCustomWHID(list.get(i).getCustomWHID());
            bean.setWarehouseName(list.get(i).getWarehouseName());
            bean.setWarehouseID(list.get(i).getWarehouseID());
            bean.setOrganID(list.get(i).getOrganID());
            if ("".equals(key)||bean.getWarehouseName().contains(key)){
                mList.add(bean);
            }
        }

        lvListData.setAdapter(new HouseAdpter(SendHouseActivity.this,mList));

    }
}
