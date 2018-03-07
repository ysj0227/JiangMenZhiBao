package com.winsafe.jiangmenzhibao.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.utils.OkHttpHelper;
import com.winsafe.jiangmenzhibao.view.AppBaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by shijie.yang on 2017/7/27.
 */

public class AroundCodeActivity extends AppBaseActivity {
    private final int RETURN_HANDLER = 100;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    private Bundle bundle;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case RETURN_HANDLER:
                    try {
                        JSONObject JSON=new JSONObject(AppConfig.strJSONObject.toString());
                        String data=JSON.getString("returnData");
                        tvMessage.setText("查询结果： \n\n"+data.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.layout_around);
    }

    @Override
    protected void initView() {
        setHeader("内外码查询", true, false, 0, "", null);
        bundle = getIntent().getExtras();
        String code = bundle.getString("code");
        //查询内外码关系信息
        Map<String, String> map = new HashMap<>();
        map.put("QrCode", code);
        OkHttpHelper.object(AroundCodeActivity.this, AppConfig.URL_APPINANDOUTCODEQUERY, map, handler, RETURN_HANDLER);
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View v) {
    }


}
