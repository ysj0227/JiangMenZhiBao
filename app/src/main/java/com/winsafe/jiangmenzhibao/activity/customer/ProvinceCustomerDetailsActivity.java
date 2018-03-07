package com.winsafe.jiangmenzhibao.activity.customer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.activity.LoginActivity;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.view.AppBaseActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 省级客商信息
 * Created by shijie.yang on 2017/5/23.
 */

public class ProvinceCustomerDetailsActivity extends AppBaseActivity {


    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvArea)
    TextView tvArea;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.btnFix)
    Button btnFix;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    @BindView(R.id.btnSetOrgan)
    Button btnSetOrgan;
    private Bundle mBundle = null;
    //名称，电话，编码，省市区code，及名称，详细地址
    private String CompantFullName = "", Telnum = "", Encode = "", Province = "", Provincename = "",
            City = "", Cityname = "", County = "", Countyname = "", Registedaddress = "",companytype="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.layout_customer_details);

    }

    @Override
    protected void initView() {
        setHeader("市经销商信息", true, false, 0, "", null);
        mBundle = getIntent().getExtras();
        CompantFullName = mBundle.getString("CompantFullName");
        Telnum = mBundle.getString("Telnum");
        Encode = mBundle.getString("Encode");
        Province = mBundle.getString("Province");
        Provincename = mBundle.getString("Provincename");
        City = mBundle.getString("City");
        Cityname = mBundle.getString("Cityname");
        County = mBundle.getString("County");
        Countyname = mBundle.getString("Countyname");
        Registedaddress = mBundle.getString("Registedaddress");
        companytype = mBundle.getString("companytype");

        tvName.setText(CompantFullName);
        tvArea.setText(Provincename + " " + Cityname + " " + Countyname + " " + Registedaddress);
        tvNumber.setText(Encode);
        tvPhone.setText(Telnum);

    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.btnFix, R.id.btnDelete, R.id.btnSetOrgan})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFix://编辑
                mBundle.putString(AppConfig.CUSTOMER_NAME, AppConfig.EDIT);
                openActivity(ProvinceCustomerDetailsActivity.this, ProvinceAddCustomerActivity.class, mBundle, true);
                break;
            case R.id.btnDelete://删除
                deleteDialog();
                break;
            case R.id.btnSetOrgan://设置下属机构
//                Log.i("TAG", "Encode:"+Encode);
                Bundle bundle = new Bundle();
                bundle.putString("Encode", Encode);
                bundle.putString(AppConfig.COMPANYTYPE, companytype);

                if (LoginActivity.dealerAreaName().contains(AppConfig.COMPANYNAME_CITY)){
                    //包含市级经销商
                    openActivity(ProvinceCustomerDetailsActivity.this, CityCustomerBranchMangerActivity.class, bundle, false);
                }else {//不包含市级经销商
                    openActivity(ProvinceCustomerDetailsActivity.this, CountyCustomerMangerActivity.class, bundle, false);
                }
                break;

            default:
                break;

        }

    }

    private void deleteDialog() {
        Dialog dialog = new AlertDialog.Builder(this)
                .setMessage("确定要删除这个客商吗")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCustomer();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).create();
        dialog.setCancelable(true);//返回键
        dialog.show();
    }

    private void deleteCustomer() {
        startDialogProgress("删除中");
        OkHttpUtils.post().url(AppConfig.URL_APPDELETECOMPANY).addParams("encode", Encode).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        stopDialogProgress();
                        Toast.makeText(ProvinceCustomerDetailsActivity.this, getResources().getString(R.string.network_wifi_low), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(Call call, String s) {
                        stopDialogProgress();
                        try {
                            JSONObject object = new JSONObject(s);
                            String code = object.getString("returnCode");
                            String msg = object.getString("returnMsg");
                            if ("0".equals(code)) {
                                Toast.makeText(ProvinceCustomerDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                                ProvinceCustomerDetailsActivity.this.finish();
                            } else if("-2".equals(code)){
                                Toast.makeText(ProvinceCustomerDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                                openActivity(ProvinceCustomerDetailsActivity.this, LoginActivity.class,true);
                            }else {
                                Toast.makeText(ProvinceCustomerDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
