package com.winsafe.jiangmenzhibao.activity.billNot;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;
import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.activity.DownLoadActivity;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.entity.OrganDataBean;
import com.winsafe.jiangmenzhibao.entity.WarehouseDataBean;
import com.winsafe.jiangmenzhibao.utils.BillHelpter;
import com.winsafe.jiangmenzhibao.utils.StringHelper;
import com.winsafe.jiangmenzhibao.view.AppBaseActivity;
import com.winsafe.jiangmenzhibao.view.MyApp;

import net.tsz.afinal.FinalDb;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 无单出库
 * Created by shijie.yang on 2017/6/10.
 */

public class BillNotOutActivity extends AppBaseActivity {
    public static final int RETURN_DATA_SEND_ORGAN = 100;
    public static final int RETURN_DATA_SEND_STORE = 101;
    public static final int RETURN_DATA_RECEIVE_ORGAN = 102;
    public static final int RETURN_DATA_RECEIVE_STORE = 103;

    @BindView(R.id.tvSendOrgan)//发货机构
            TextView tvSendOrgan;
    @BindView(R.id.ivSendOrgan)
    ImageView ivSendOrgan;
    @BindView(R.id.tvSendStore)//发货仓库
            TextView tvSendStore;
    @BindView(R.id.ivSendStore)
    ImageView ivSendStore;
    @BindView(R.id.tvReceiveOrgan)//收货机构
            TextView tvReceiveOrgan;
    @BindView(R.id.ivReceiveOrgan)
    ImageView ivReceiveOrgan;
    @BindView(R.id.tvReceiveStore)//收货仓库
            TextView tvReceiveStore;
    @BindView(R.id.ivReceiveStore)
    ImageView ivReceiveStore;
    @BindView(R.id.btnReset)//重置
            Button btnReset;
    @BindView(R.id.btnScan)//扫描
            Button btnScan;
    @BindView(R.id.rl1)
    RelativeLayout rl1;
    @BindView(R.id.rl2)
    RelativeLayout rl2;
    @BindView(R.id.rl3)
    RelativeLayout rl3;
    @BindView(R.id.rl4)
    RelativeLayout rl4;

    private String FromOrganID = "";//发货机构id
    private String ToOrganID = "";//收货机构id
    private String FromHouseID = "";//发货仓库id
    private String ToHouseID = "";//收货仓库id

    //定义回调的单据参数
    private String customID = "", fromOrgID = "", fromWHName = "", billSort = "",
            fromWHID = "", toOrgID = "", toWHName = "", toOrgName = "", totalCount = "",
            billNo = "", billID = "", toWHID = "", fromOrgName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.layout_out_not_store);

    }

    @Override
    protected void initView() {
        setHeader("无单出库", true, false, 0, "", null);
        isDownData();

    }

    private void isDownData() {
        List<OrganDataBean> list = MyApp.organDB.findAll(OrganDataBean.class);
        if (list == null || list.size() == 0) {
//            Toast.makeText(this, "请到 [ 数据更新 ] 下载数据", Toast.LENGTH_SHORT).show();
            goDialogUpdate();
            return;
        }
        showDefaultData();
    }
    //跳转数据更新
    private void goDialogUpdate(){
        Dialog dialog= new AlertDialog.Builder(this)
                .setTitle("数据更新")
                .setIcon(getResources().getDrawable(R.mipmap.ic_app_update))
                .setMessage("请到数据更新下载数据")
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openActivity(BillNotOutActivity.this, DownLoadActivity.class,true);
                        dialog.dismiss();
                        return;
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).create();
        dialog.setCancelable(false);//返回键
        dialog.show();
    }

    //初始化显示机构仓库
    private void showDefaultData() {
        //发
        List<OrganDataBean> list = MyApp.organDB.findAllByWhere(OrganDataBean.class, " type=\"" + AppConfig.AUTHORITY_SEND + "\"");
        if (list.size()==0||list==null){
            //return ;
        }else if (list.size()==1){
            OrganSendDefault();
            HouseSendDefault();
        }
        //收
        List<OrganDataBean> mlist = MyApp.organDB.findAllByWhere(OrganDataBean.class, " type=\"" + AppConfig.AUTHORITY_RECEIVE + "\"");
        if (mlist.size()==0||mlist==null){
           // return;
        }else if (mlist.size()==1){
            OrganReceiveDefault();
            HouseReceiveDefault();
        }

    }


    //初始默认发货机构
    private void OrganSendDefault() {
        List<OrganDataBean> list = MyApp.organDB.findAllByWhere(OrganDataBean.class, " type=\"" + AppConfig.AUTHORITY_SEND + "\"");
        String name = list.get(0).getOrganName();
        String id = list.get(0).getOrganID();
        tvSendOrgan.setText(name);
        fromOrgName = name;//名称
        fromOrgID = id;//id
        FromOrganID = id;
    }

    //初始默认发货仓库
    private void HouseSendDefault() {
        List<WarehouseDataBean> lists = MyApp.warehouseDB.findAllByWhere(WarehouseDataBean.class,
                " type=\"" + AppConfig.AUTHORITY_SEND + "\"" + " and " + " organID=\"" + FromOrganID + "\"");
        String names = lists.get(0).getWarehouseName();
        String ids = lists.get(0).getWarehouseID();
        tvSendStore.setText(names);
        fromWHName = names;
        fromWHID = ids;
        FromHouseID = ids;
    }

    //初始默认收货机构
    private void OrganReceiveDefault() {
        List<OrganDataBean> mlist = MyApp.organDB.findAllByWhere(OrganDataBean.class, " type=\"" + AppConfig.AUTHORITY_RECEIVE + "\"");
        String name_r = mlist.get(0).getOrganName();
        String ids_r = mlist.get(0).getOrganID();
        tvReceiveOrgan.setText(name_r);
        toOrgName = name_r;//名称
        toOrgID = ids_r;//id
        ToOrganID = ids_r;
    }

    //初始默认收货仓库
    private void HouseReceiveDefault() {
        List<WarehouseDataBean> mlists = MyApp.warehouseDB.findAllByWhere(WarehouseDataBean.class,
                " type=\"" + AppConfig.AUTHORITY_RECEIVE + "\"" + " and " + " organID=\"" + toOrgID + "\"");
        String names_w = mlists.get(0).getWarehouseName();
        String ids_w = mlists.get(0).getWarehouseID();
        tvReceiveStore.setText(names_w);
        toWHName = names_w;
        toWHID = ids_w;
        ToHouseID = ids_w;
    }

    @Override
    protected void setListener() {

    }
    //获取机构仓库列表数量
    private int getListSize(FinalDb db, String type){
        List<OrganDataBean> list = db.findAllByWhere(OrganDataBean.class, " type=\"" + type + "\"");
        return list.size();
    }
    @OnClick({R.id.ivSendOrgan, R.id.ivSendStore, R.id.ivReceiveOrgan, R.id.ivReceiveStore,
            R.id.rl1, R.id.rl2, R.id.rl3, R.id.rl4,R.id.btnReset, R.id.btnScan})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl1:
            case R.id.ivSendOrgan://发货机构

                Bundle bundle1 = new Bundle();
                openActivityForResult(BillNotOutActivity.this, SendOrganActivity.class, RETURN_DATA_SEND_ORGAN);
                break;
            case R.id.rl2:
            case R.id.ivSendStore://发货仓库
                if (StringHelper.isNullOrEmpty(tvSendOrgan.getText().toString())) {
                    Toast.makeText(this, "请选择发货机构", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bundle bundle2 = new Bundle();
                bundle2.putString("FromOrganID", FromOrganID);
                openActivityForResult(BillNotOutActivity.this, SendHouseActivity.class, RETURN_DATA_SEND_STORE, bundle2);
                break;
            case R.id.rl3:
            case R.id.ivReceiveOrgan://收货机构
                openActivityForResult(BillNotOutActivity.this, ReceiveOrganActivity.class, RETURN_DATA_RECEIVE_ORGAN);
                break;
            case R.id.rl4:
            case R.id.ivReceiveStore://收货仓库
                if (StringHelper.isNullOrEmpty(tvReceiveOrgan.getText().toString())) {
                    Toast.makeText(this, "请选择收货机构", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bundle bundle4 = new Bundle();
                bundle4.putString("ToOrganID", ToOrganID);
                openActivityForResult(BillNotOutActivity.this, ReceiveHouseActivity.class, RETURN_DATA_RECEIVE_STORE, bundle4);

                break;

            case R.id.btnReset://重置
                if (getListSize(MyApp.organDB, AppConfig.AUTHORITY_SEND)>1){  //发
                    tvSendOrgan.setText(null);
                    tvSendStore.setText(null);
                }
                if (getListSize(MyApp.organDB, AppConfig.AUTHORITY_RECEIVE)>1){ //收
                    tvReceiveOrgan.setText(null);
                    tvReceiveStore.setText(null);
                }
                break;
            case R.id.btnScan://扫描
                if (!returnNullToast()) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(AppConfig.OPERATE_TYPE, AppConfig.OPERATE_TYPE_NOT_CK);
                bundle.putString(CaptureActivity.SCAN_MODE, CaptureActivity.SCAN_REPEAT);
                //传递单据的参数
                BillHelpter.setBillBundle(bundle, customID, fromOrgName, fromOrgID,
                        fromWHName, fromWHID, billSort, toOrgName, toOrgID, toWHName,
                        toWHID, billNo, billID, totalCount);

                openActivity(BillNotOutActivity.this, CaptureActivity.class, bundle, false);
                break;

            default:
                break;

        }

    }

    private boolean returnNullToast() {
        if (StringHelper.isNullOrEmpty(tvSendOrgan.getText().toString())) {
            Toast.makeText(this, "请选择发货机构", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringHelper.isNullOrEmpty(tvSendStore.getText().toString())) {
            Toast.makeText(this, "请选择发货仓库", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringHelper.isNullOrEmpty(tvReceiveOrgan.getText().toString())) {
            Toast.makeText(this, "请选择收货机构", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringHelper.isNullOrEmpty(tvReceiveStore.getText().toString())) {
            Toast.makeText(this, "请选择收货仓库", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String name = data.getExtras().getString(AppConfig.DATA_NAME);
            String id = data.getExtras().getString(AppConfig.DATA_ID);
            switch (requestCode) {
                case RETURN_DATA_SEND_ORGAN://发货机构
                    tvSendOrgan.setText(name);
                    fromOrgName = name;//名称
                    fromOrgID = id;//id

                    FromOrganID = id;
                    //带出发货仓库
                    HouseSendDefault();

                    break;
                case RETURN_DATA_SEND_STORE://发货仓库
                    tvSendStore.setText(name);
                    fromWHName = name;
                    fromWHID = id;

                    FromHouseID = id;
                    break;
                case RETURN_DATA_RECEIVE_ORGAN://收货机构
                    tvReceiveOrgan.setText(name);
                    toOrgName = name;//名称
                    toOrgID = id;//id

                    ToOrganID = id;
                    //带出收货仓库
                    HouseReceiveDefault();
                    break;
                case RETURN_DATA_RECEIVE_STORE://收货仓库
                    tvReceiveStore.setText(name);
                    toWHName = name;//名称
                    toWHID = id;//id

                    ToHouseID = id;
                    break;

                default:
                    break;

            }
        }
    }
}
