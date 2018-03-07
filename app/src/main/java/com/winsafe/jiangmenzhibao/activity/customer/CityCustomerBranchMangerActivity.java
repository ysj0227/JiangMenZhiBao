package com.winsafe.jiangmenzhibao.activity.customer;

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
import com.winsafe.jiangmenzhibao.adapter.CustomerAdpter;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.entity.CustomerBean;
import com.winsafe.jiangmenzhibao.utils.GlobalHelper;
import com.winsafe.jiangmenzhibao.view.AppBaseActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 县经销商管理
 * Created by shijie.yang on 2017/5/23.
 */

public class CityCustomerBranchMangerActivity extends AppBaseActivity {
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.lvListData)
    ListView lvListData;
    private List<CustomerBean.ReturnDataBean.CompanyinfoBean> listKey = null;
    private List<CustomerBean.ReturnDataBean.CompanyinfoBean> list;
    private String mEncode = "", companytype = "";
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.layout_customer_list);

    }

    @Override
    protected void initView() {
        setHeader("县经销商管理", true, true, 0, "新增", this);
        mBundle = getIntent().getExtras();
        mEncode = mBundle.getString("Encode");
        companytype = mBundle.getString(AppConfig.COMPANYTYPE);
        search();
        //显示输入法
        GlobalHelper.stateSoftWareShow(this, etSearch);
        OnItemClick();
    }

    private void OnItemClick() {
        lvListData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerBean.ReturnDataBean.CompanyinfoBean bean = listKey.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("CompantFullName", bean.getCompantFullName());
                bundle.putString("Telnum", bean.getTelnum());
                bundle.putString("Encode", bean.getEncode());
                bundle.putString("Province", bean.getProvince());
                bundle.putString("Provincename", bean.getProvincename());
                bundle.putString("City", bean.getCity());
                bundle.putString("Cityname", bean.getCityname());
                bundle.putString("County", bean.getCounty());
                bundle.putString("Countyname", bean.getCountyname());
                bundle.putString("Registedaddress", bean.getRegistedaddress());
                bundle.putString("companytype", bean.getCompanytype());
                openActivity(CityCustomerBranchMangerActivity.this, CityCustomerDetailsActivity.class, bundle, false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getList("");
    }

    //获取数据
    private void getList(final String key) {
        startDialogProgress("加载中");
        OkHttpUtils.post()
                .url(AppConfig.URL_APPLOADCOMPANY)
                .addParams("encode", mEncode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        stopDialogProgress();
                        Toast.makeText(CityCustomerBranchMangerActivity.this, getResources().getString(R.string.network_wifi_low), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, String s) {
                        stopDialogProgress();
                        CustomerBean bean = new Gson().fromJson(s, CustomerBean.class);

                        if ("0".equals(bean.getReturnCode())) {
                            list = bean.getReturnData().getCompanyinfo();
                            //信息筛选
                            CustomerBean.ReturnDataBean.CompanyinfoBean info;
                            listKey = new ArrayList<>();
                            listKey.clear();
                            for (int i = 0; i < list.size(); i++) {
                                info = new CustomerBean.ReturnDataBean.CompanyinfoBean();
                                if ("".equals(key) || list.get(i).getCompantFullName().contains(key)) {
                                    info.setCompantFullName(list.get(i).getCompantFullName());//客商名称
                                    info.setTelnum(list.get(i).getTelnum());//电话
                                    info.setEncode(list.get(i).getEncode());//外部编号
                                    info.setProvince(list.get(i).getProvince());//省
                                    info.setProvincename(list.get(i).getProvincename());
                                    info.setCity(list.get(i).getCity());//市
                                    info.setCityname(list.get(i).getCityname());
                                    info.setCounty(list.get(i).getCounty());//区
                                    info.setCountyname(list.get(i).getCountyname());
                                    info.setRegistedaddress(list.get(i).getRegistedaddress());//详细地址
                                    info.setCompanytype(list.get(i).getCompanytype());
                                    listKey.add(info);
                                }
                            }
                            lvListData.setAdapter(new CustomerAdpter(CityCustomerBranchMangerActivity.this, listKey));
                        } else if("-2".equals(bean.getReturnCode())){
                            Toast.makeText(CityCustomerBranchMangerActivity.this, bean.getReturnMsg(), Toast.LENGTH_SHORT).show();
                            openActivity(CityCustomerBranchMangerActivity.this, LoginActivity.class,true);
                        } else {
                            Toast.makeText(CityCustomerBranchMangerActivity.this, bean.getReturnMsg(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }

    //信息筛选
    private void searchListData(final String key) {
        if (list.size() > 0) {
            CustomerBean.ReturnDataBean.CompanyinfoBean info;
            listKey = new ArrayList<>();
            listKey.clear();
            for (int i = 0; i < list.size(); i++) {
                info = new CustomerBean.ReturnDataBean.CompanyinfoBean();
                if ("".equals(key) || list.get(i).getCompantFullName().contains(key)) {
                    info.setCompantFullName(list.get(i).getCompantFullName());//客商名称
                    info.setTelnum(list.get(i).getTelnum());//电话
                    info.setEncode(list.get(i).getEncode());//外部编号
                    info.setProvince(list.get(i).getProvince());//省
                    info.setProvincename(list.get(i).getProvincename());
                    info.setCity(list.get(i).getCity());//市
                    info.setCityname(list.get(i).getCityname());
                    info.setCounty(list.get(i).getCounty());//区
                    info.setCountyname(list.get(i).getCountyname());
                    info.setRegistedaddress(list.get(i).getRegistedaddress());//详细地址
                    info.setCompanytype(list.get(i).getCompanytype());
                    listKey.add(info);
                }
            }
            lvListData.setAdapter(new CustomerAdpter(CityCustomerBranchMangerActivity.this, listKey));
        }
    }

    //搜索
    private void search() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchListData(s.toString());
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
        switch (v.getId()) {
            case R.id.btnOther://县级新增客商
                Bundle bundle = new Bundle();
                bundle.putString(AppConfig.CUSTOMER_NAME, AppConfig.ADD);
                bundle.putString(AppConfig.ENCODE, mEncode);
                bundle.putString(AppConfig.COMPANYTYPE, companytype);
                openActivity(CityCustomerBranchMangerActivity.this, CityAddCustomerActivity.class, bundle, false);

                break;

            default:
                break;

        }
    }
}
