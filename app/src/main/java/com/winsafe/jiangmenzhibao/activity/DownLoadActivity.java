package com.winsafe.jiangmenzhibao.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.entity.OrganDataBean;
import com.winsafe.jiangmenzhibao.entity.ProductNoBean;
import com.winsafe.jiangmenzhibao.entity.WarehouseDataBean;
import com.winsafe.jiangmenzhibao.utils.GlobalHelper;
import com.winsafe.jiangmenzhibao.view.AppBaseActivity;
import com.winsafe.jiangmenzhibao.view.MyApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 数据下载
 * Created by shijie.yang 2017/6/10.
 */

public class DownLoadActivity extends AppBaseActivity {
    @BindView(R.id.btnDownload)
    Button btnDownload;
    @BindView(R.id.btnQuery)
    Button btnQuery;
    public int Flag = 0;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    break;
                case 5:
                    stopDialogProgress();
                    Flag = 0;
                    Toast.makeText(DownLoadActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                    DownLoadActivity.this.finish();
                    break;

            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.layout_download);

    }

    @Override
    protected void initView() {
        setHeader("数据更新", true, false, 0, "", null);
    }


    @Override
    protected void setListener() {

    }

    @OnClick({R.id.btnDownload, R.id.btnQuery})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDownload:
                startDialogProgress("下载中···");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //机构下载
                        downLoadOrganData(AppConfig.URL_APPGETCOMPANYBYGUANXIALIST, AppConfig.AUTHORITY_SEND); //发货
                        downLoadOrganData(AppConfig.URL_APPGETCOMPANYBYGUANXIALIST, AppConfig.AUTHORITY_RECEIVE); //收货
                        //仓库下载
                        downLoadWarehouseData(AppConfig.URL_APPGETWAREHOUSEBYAUTHORITYLIST, AppConfig.AUTHORITY_SEND);
                        downLoadWarehouseData(AppConfig.URL_APPGETWAREHOUSEBYAUTHORITYLIST, AppConfig.AUTHORITY_RECEIVE);
                        // 产品下载
                        downLoadProductData(AppConfig.URL_APPGETPRODUCT);
                    }
                },1000);

                break;

            default:
                break;

        }

    }

    //机构下载
    private void downLoadOrganData(String url, final String type) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add(AppConfig.USERNAME, GlobalHelper.getUserName())
                .add(AppConfig.PASSWORD, GlobalHelper.getPassword())
                .add("Authority", type)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        //下载前先清除数据
        MyApp.organDB.deleteByWhere(OrganDataBean.class, " type=\"" + type + "\"");

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopDialogProgress();
                        Toast.makeText(DownLoadActivity.this, getResources().getString(R.string.network_wifi_low), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject ob = new JSONObject(str);
                            String code = ob.getString("returnCode");
                            if ("0".equals(code)) {
                                JSONArray array = ob.getJSONArray("returnData");
                                OrganDataBean bean = null;
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject obs = (JSONObject) array.opt(i);
                                    bean = new OrganDataBean();
//                                  "compantFullName": "公司名称",
//                                   "encode": "20160927",
//                                   "code": "5516ec3d-803a-4cc6-9415-9d99cc26fd3b
                                    bean.setCustomOrgID(obs.getString("encode"));
                                    bean.setOrganID(obs.getString("code"));
                                    bean.setOrganName(obs.getString("compantFullName"));
                                    bean.setType(type);
                                    MyApp.organDB.save(bean);
                                }
                                Flag++;

                                Message message = new Message();
                                message.what = Flag;
                                handler.sendMessage(message);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });


            }
        });
    }

    //仓库下载
    private void downLoadWarehouseData(String url, final String type) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add(AppConfig.USERNAME, GlobalHelper.getUserName())
                .add(AppConfig.PASSWORD, GlobalHelper.getPassword())
                .add("Authority", type)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        //下载前先清除仓库数据
        MyApp.warehouseDB.deleteByWhere(WarehouseDataBean.class, " type=\"" + type + "\"");
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopDialogProgress();
                        Toast.makeText(DownLoadActivity.this, getResources().getString(R.string.network_wifi_low), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject ob = new JSONObject(str);
                            String code = ob.getString("returnCode");
                            if ("0".equals(code)) {
                                JSONArray array = ob.getJSONArray("returnData");
                                WarehouseDataBean bean = null;
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject obs = (JSONObject) array.opt(i);
                                    bean = new WarehouseDataBean();
//                                    "enCode": "123456",
//                                            "name": "aaaa默认仓库",
//                                            "code": "31a1cc84-62b2-4103-87f3-04e2cb2b2237",
//                                            "companyCode": "bedf20c3-fbfe-4f52-95de-2b080caa0bc2"
                                    bean.setWarehouseName(obs.getString("name"));
                                    bean.setOrganID(obs.getString("companyCode"));
                                    bean.setWarehouseID(obs.getString("code"));
                                    bean.setCustomWHID(obs.getString("enCode"));
                                    bean.setType(type);
                                    MyApp.warehouseDB.save(bean);
                                }
                                Flag++;

                                Message message = new Message();
                                message.what = Flag;
                                handler.sendMessage(message);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });


            }
        });
    }

    //产品下载
    private void downLoadProductData(String url) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add(AppConfig.USERNAME, GlobalHelper.getUserName())
                .add(AppConfig.PASSWORD, GlobalHelper.getPassword())
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        //下载前先清除产品数据
        MyApp.productNoDB.deleteAll(ProductNoBean.class);
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopDialogProgress();
                        Toast.makeText(DownLoadActivity.this, getResources().getString(R.string.network_wifi_low), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Log.i("TAG", str);
                        try {
                            JSONObject ob = new JSONObject(str);
                            String code = ob.getString("returnCode");
                            if ("0".equals(code)) {
                                JSONObject obs = ob.getJSONObject("returnData");
                                JSONArray array=obs.getJSONArray("productInfo");
                                ProductNoBean bean;
                                for (int i = 0; i <array.length() ; i++) {
                                    bean=new ProductNoBean();
                                    JSONObject object = (JSONObject) array.opt(i);
//                                              "ICode": "040",
//                                            "enCode": "CODE0000004",
//                                            "productId": "428645b2-a145-49ba-8a7f-cac3e7bc20b6",
//                                            "unitid": "25",
//                                            "unitname": "支",
//                                            "unitquantities": "2",
//                                            "wuliuPrefix": "040"
                                    bean.setICode(object.getString("ICode"));
                                    bean.setEnCode(object.getString("enCode"));
                                    bean.setProductId(object.getString("productId"));
                                    bean.setUnitid(object.getString("unitid"));
                                    bean.setUnitname(object.getString("unitname"));
                                    bean.setUnitquantities(object.getString("unitquantities"));
                                    bean.setWuliuPrefix(object.getString("wuliuPrefix"));
                                    MyApp.productNoDB.save(bean);
                                }

                                Flag++;

                                Message message = new Message();
                                message.what = Flag;
                                handler.sendMessage(message);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }
}
