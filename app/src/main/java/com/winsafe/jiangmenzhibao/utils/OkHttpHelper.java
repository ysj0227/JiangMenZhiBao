package com.winsafe.jiangmenzhibao.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.winsafe.jiangmenzhibao.MainActivity;
import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;

/**
 * 网络请求
 * Created by shijie.yang on 2017/7/18.
 */

public class OkHttpHelper {
    public Context context;

    public OkHttpHelper(Context context) {
        this.context = context;
    }
    //网络请求封装
    public static void object(final Context context, String url, Map<String, String> map,
                              final Handler handler, final int flag) {
        map.put(AppConfig.USERNAME, GlobalHelper.getUserName());
        map.put(AppConfig.PASSWORD, GlobalHelper.getPassword());

        OkHttpUtils.post().url(url).params(map).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(context, context.getResources().getString(R.string.network_wifi_low), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, String s) {
                        try {
                            JSONObject JSONObject = new JSONObject(s);
                            String code = JSONObject.getString("returnCode");
                            String msg = JSONObject.getString("returnMsg");
                            if ("0".equals(code)) {
                                AppConfig.strJSONObject = s;

                                Message message = new Message();
                                message.what = flag;
                                handler.sendMessage(message);
                            }
                            else if("-2".equals(code)){
                                Intent it=new Intent(context, MainActivity.class) ;
                                context.startActivity(it);
                            }
                            else {
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

}
