package com.winsafe.jiangmenzhibao.activity.billHave;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;
import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.utils.BillHelpter;
import com.winsafe.jiangmenzhibao.utils.StringHelper;
import com.winsafe.jiangmenzhibao.view.AppBaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 退货管理
 * Created by shijie.yang on 2017/5/23.
 */

public class BillReturnActivity extends AppBaseActivity {


    public static final int RETURN_DATA_SEND_ORGAN = 100;
    public static final int RETURN_DATA_SEND_STORE = 101;
    public static final int RETURN_DATA_RECEIVE_ORGAN = 102;
    public static final int RETURN_DATA_RECEIVE_STORE = 103;

    @BindView(R.id.tvbill)
    TextView tvbill;
    @BindView(R.id.tvSelectBill)
    TextView tvSelectBill;
    @BindView(R.id.ivBill)
    ImageView ivBill;
    @BindView(R.id.tvSendOrgan)
    TextView tvSendOrgan;
    @BindView(R.id.ivSendOrgan)
    ImageView ivSendOrgan;
    @BindView(R.id.rl1)
    RelativeLayout rl1;
    @BindView(R.id.tvSendStore)
    TextView tvSendStore;
    @BindView(R.id.ivSendStore)
    ImageView ivSendStore;
    @BindView(R.id.rl2)
    RelativeLayout rl2;
    @BindView(R.id.tvReceiveOrgan)
    TextView tvReceiveOrgan;
    @BindView(R.id.ivReceiveOrgan)
    ImageView ivReceiveOrgan;
    @BindView(R.id.rl3)
    RelativeLayout rl3;
    @BindView(R.id.tvReceiveStore)
    TextView tvReceiveStore;
    @BindView(R.id.ivReceiveStore)
    ImageView ivReceiveStore;
    @BindView(R.id.rl4)
    RelativeLayout rl4;
    @BindView(R.id.btnReset)
    Button btnReset;
    @BindView(R.id.btnScan)
    Button btnScan;
    //定义回调的单据参数
    private String customID="",fromOrgID="",fromWHName="",billSort="",
            fromWHID="",toOrgID="",toWHName="",toOrgName="",totalCount="",
            billNo="",billID="",toWHID="",fromOrgName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.layout_return_store);

    }

    @Override
    protected void initView() {
        setHeader("退货管理", true, false, 0, "", null);

    }

    @Override
    protected void setListener() {

    }


    @OnClick({R.id.ivBill,R.id.ivSendOrgan, R.id.ivSendStore, R.id.ivReceiveOrgan, R.id.ivReceiveStore,
            R.id.btnReset, R.id.btnScan})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBill://选择退货单据
                Bundle bundles=new Bundle();
                bundles.putString("BillSort","3");
                openActivityForResult(BillReturnActivity.this, BillDataListActivity.class, RETURN_DATA_SEND_ORGAN,bundles);

                break;
            case R.id.btnReset://重置
                tvSelectBill.setText(null);
                rl1.setVisibility(View.INVISIBLE);
                rl2.setVisibility(View.INVISIBLE);
                rl3.setVisibility(View.INVISIBLE);
                rl4.setVisibility(View.INVISIBLE);
                break;

            case R.id.btnScan://退货扫描
                if (StringHelper.isNullOrEmpty(tvSelectBill.getText().toString())){
                    Toast.makeText(this, "请选择单据", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(AppConfig.OPERATE_TYPE, AppConfig.OPERATE_TYPE_TH);
                bundle.putString(CaptureActivity.SCAN_MODE, CaptureActivity.SCAN_REPEAT);
                //传递单据的参数
                BillHelpter.setBillBundle(bundle,customID, fromOrgName,fromOrgID,
                        fromWHName,fromWHID, billSort,toOrgName, toOrgID,toWHName,
                        toWHID, billNo, billID, totalCount);

                openActivity(BillReturnActivity.this, CaptureActivity.class, bundle, false);
                break;

            default:
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RETURN_DATA_SEND_ORGAN) {
            switch (resultCode) {
                case RESULT_OK:
                    rl1.setVisibility(View.VISIBLE);
                    rl2.setVisibility(View.VISIBLE);
                    rl3.setVisibility(View.VISIBLE);
                    rl4.setVisibility(View.VISIBLE);

                    //得到新Activity关闭后返回的数据
                    customID = data.getExtras().getString("customID");
                    fromOrgName = data.getExtras().getString("fromOrgName");
                    fromOrgID = data.getExtras().getString("fromOrgID");
                    fromWHName = data.getExtras().getString("fromWHName");
                    fromWHID = data.getExtras().getString("fromWHID");

                    toOrgName = data.getExtras().getString("toOrgName");
                    toOrgID = data.getExtras().getString("toOrgID");
                    toWHName = data.getExtras().getString("toWHName");
                    toWHID = data.getExtras().getString("toWHID");

                    billNo = data.getExtras().getString("billNo");
                    billID = data.getExtras().getString("billID");
                    billSort = data.getExtras().getString("billSort");
                    totalCount = data.getExtras().getString("totalCount");

                    tvSelectBill.setText(billNo);
                    tvSendOrgan.setText(fromOrgName);
                    tvSendStore.setText(fromWHName);
                    tvReceiveOrgan.setText(toOrgName);
                    tvReceiveStore.setText(toWHName);
                    break;

                default:
                    break;

            }
        }
    }
}
