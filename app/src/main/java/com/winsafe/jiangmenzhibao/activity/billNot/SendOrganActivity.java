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
import com.winsafe.jiangmenzhibao.adapter.OrganAdpter;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.entity.OrganDataBean;
import com.winsafe.jiangmenzhibao.utils.GlobalHelper;
import com.winsafe.jiangmenzhibao.view.AppBaseActivity;
import com.winsafe.jiangmenzhibao.view.MyApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 发货机构
 * Created by shijie.yang on 2017/6/11.
 */

public class SendOrganActivity extends AppBaseActivity {
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.lvListData)
    ListView lvListData;
    private List<OrganDataBean> list = null;
    //筛选列表
    private List<OrganDataBean> mList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.layout_out_list);
    }

    @Override
    protected void initView() {
        setHeader("选择发货机构", true, false, 0, "", null);
        getList();
        lvListData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrganDataBean bean = mList.get(position);
                Intent it = getIntent();

                it.putExtra(AppConfig.DATA_NAME, bean.getOrganName());
                it.putExtra(AppConfig.DATA_ID, bean.getOrganID());
                setResult(RESULT_OK, it);
                finish();
            }
        });
        search();
        //显示输入法
        GlobalHelper.stateSoftWareShow(this, etSearch);
    }

    private void search() {

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

    //初始化列表
    private void getList() {
        list = MyApp.organDB.findAllByWhere(OrganDataBean.class, " type=\"" + AppConfig.AUTHORITY_SEND + "\"");
        mList = list;
        lvListData.setAdapter(new OrganAdpter(SendOrganActivity.this, mList));


    }

    //筛选
    private void getSearchList(final String key) {
        OrganDataBean bean;
        mList = new ArrayList<OrganDataBean>();//必须此处定义
        mList.clear();
        for (int i = 0; i < list.size(); i++) {
            bean = new OrganDataBean();
            bean.setCustomOrgID(list.get(i).getCustomOrgID());
            bean.setOrganName(list.get(i).getOrganName());
            bean.setOrganID(list.get(i).getOrganID());
            if ("".equals(key) || bean.getOrganName().contains(key)) {
                mList.add(bean);
            }
        }
        lvListData.setAdapter(new OrganAdpter(SendOrganActivity.this, mList));

    }

}
