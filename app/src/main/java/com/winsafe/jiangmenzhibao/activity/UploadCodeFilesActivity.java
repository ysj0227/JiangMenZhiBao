package com.winsafe.jiangmenzhibao.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.entity.SaveDBBean;
import com.winsafe.jiangmenzhibao.utils.CommonHelper;
import com.winsafe.jiangmenzhibao.utils.FileHelper;
import com.winsafe.jiangmenzhibao.utils.GlobalHelper;
import com.winsafe.jiangmenzhibao.utils.NetworkHelper;
import com.winsafe.jiangmenzhibao.utils.UploadFileInfoHelpter;
import com.winsafe.jiangmenzhibao.view.AppBaseActivity;
import com.winsafe.jiangmenzhibao.view.MyApp;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import net.tsz.afinal.FinalDb;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 查看上传条码
 * Created by shijie.yang on 2017/5/21.
 */

public class UploadCodeFilesActivity extends AppBaseActivity {


    @BindView(R.id.btnDelete)
    Button btnDelete;
    @BindView(R.id.btnUpload)
    Button btnUpload;
    @BindView(R.id.lvListData)
    ListView lvListData;
    @BindView(R.id.tvTip)
    TextView tvTip;

    private CodeAdapters adapter;

    private List<SaveDBBean> list = null;//初始化列表
    private List<SaveDBBean> listChecked = null;//选中列表
    public Map<Integer, Boolean> checkedCartMap = new HashMap<Integer, Boolean>();
    private boolean mCheckedAll = false;
    private Bundle mBundle = null;
    private String operateType = "", skipActivity = "";
    private String nowFromWHID = "", nowToWHID = "";//当前扫描页面传递的收发货仓库id
    private FinalDb db;//数据库

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.layout_outstore_code);
    }

    @Override
    protected void initView() {
        operateType();
        // 初始化显示数据列表
        querylistData();
        if (list == null || list.size() == 0) {
            GlobalHelper.setTipVisible(tvTip,lvListData);
           // Toast.makeText(this, getResources().getString(R.string.code_not_scanned), Toast.LENGTH_SHORT).show();
            return;
        } else {
            GlobalHelper.setTipGone(tvTip,lvListData);
            adapter = new CodeAdapters(UploadCodeFilesActivity.this, list);
            lvListData.setAdapter(adapter);
        }
        //初始全选
        onChecked();
    }

    //类型
    private void operateType() {
        mBundle = getIntent().getExtras();
        operateType = mBundle.getString(AppConfig.OPERATE_TYPE);
        skipActivity = mBundle.getString(AppConfig.SKIP_ACTIVITY);
        nowFromWHID = mBundle.getString(AppConfig.FROMWHID);
        nowToWHID = mBundle.getString(AppConfig.TOWHID);

        String title = "";
        if (AppConfig.OPERATE_TYPE_CK.equals(operateType)) {//出库
            db = MyApp.outDB;
            title = getResources().getString(R.string.title_out);
        } else if (AppConfig.OPERATE_TYPE_TH.equals(operateType)) { //退货
            db = MyApp.returnDB;
            title = getResources().getString(R.string.title_return);
        } else if (AppConfig.OPERATE_TYPE_NOT_CK.equals(operateType)) { //无单出库
            db = MyApp.outNotDB;
            title = getResources().getString(R.string.title_not_out);
        } else if (AppConfig.OPERATE_TYPE_NOT_TH.equals(operateType)) { //无单退货
            db = MyApp.returnNotDB;
            title = getResources().getString(R.string.title_not_return);
        }
        setHeader(title, true, true, 0, "全选", this);
    }

    //刷新数据
    private void reQueryCodeList() {
        querylistData();
        //设置显示的提示
        if (list == null || list.size() == 0) {
            GlobalHelper.setTipVisible(tvTip,lvListData);
        } else {
            GlobalHelper.setTipGone(tvTip,lvListData);
        }
        adapter = new CodeAdapters(UploadCodeFilesActivity.this, list);
        lvListData.setAdapter(adapter);
    }

    //初始化查询数据列表
    private void querylistData() {
        if (AppConfig.SKIP_CAPTUREACTIVITY.equals(skipActivity)) {
            //扫描跳转  筛选当前用户选择的 发货仓库和收货仓库
            list = db.findAllByWhere(SaveDBBean.class, " fromWHID=\"" + nowFromWHID + "\"" + " and " + " toWHID=\"" + nowToWHID + "\"" + " and " + " userID=\"" + GlobalHelper.getUserName() + "\"");
        } else {
            //从文件上传跳转
            list = db.findAllByWhere(SaveDBBean.class, " userID=\"" + GlobalHelper.getUserName() + "\"");
        }
    }

    @Override
    protected void setListener() {

    }

    //当前的码的list是0时
    private boolean isListNull() {
        if (list.size() == 0) {
            showToast(getResources().getString(R.string.code_list));
            //Toast.makeText(this, getResources().getString(R.string.code_list), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @OnClick({R.id.btnDelete, R.id.btnUpload})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOther://全选或取消
                if (!isListNull()) {
                    return;
                }
                onChecked();
                break;
            case R.id.btnDelete://删除
                /* 判断是否有需要上传的条码 */
                if (!isListNull()) {
                    return;
                }

                if (listChecked.size() == 0) {
                    Toast.makeText(this, getResources().getString(R.string.code_checked_list), Toast.LENGTH_SHORT).show();
                    return;
                }
                codeDialog("1", "确定要删除选中的条码吗");
                break;
            case R.id.btnUpload://上传
                /* 判断是否有需要上传的条码 */
                if (!isListNull()) {
                    return;
                }
                if (listChecked.size() == 0) {
                    Toast.makeText(this, getResources().getString(R.string.code_checked_list), Toast.LENGTH_SHORT).show();
                    return;
                }
                codeDialog("2", "确定要上传选中的条码吗");
                break;

            default:
                break;

        }
    }

    //1删除选中的码，2上传成功后删除
    private void deleteCheckCode() {
        if (list.size() == 1) {//当列表只有一个时，强制刷新
            list.remove(0);
            lvListData.invalidateViews();
        }
        for (SaveDBBean bean : listChecked) {
            db.deleteByWhere(SaveDBBean.class, " barCode=\"" + bean.getBarCode() + "\"" + " and " + " userID=\"" + GlobalHelper.getUserName() + "\"");
        }
        reQueryCodeList();
    }

    private void codeDialog(final String type, String msg) {
        Dialog dialog = new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ("1".equals(type)) {//删除
                            deleteCheckCode();
                            Toast.makeText(UploadCodeFilesActivity.this, getResources().getString(R.string.delete_add_success), Toast.LENGTH_SHORT).show();
                        } else { //上传文件
                            uploadFiles();
                        }
                        return;
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

    // 全选或取消
    private void onChecked() {
        if (mCheckedAll) {
            checkedCartMap.clear();
            mCheckedAll = false;
        } else {
            checkAll();
            mCheckedAll = true;
        }
        listCheckedNotifyDataSetChanged();
        adapter.notifyDataSetChanged();// 刷新数据

    }

    // 全选，将数据加入checkedCartMap
    public void checkAll() {
        for (int i = 0; i < list.size(); i++) {
            checkedCartMap.put(Integer.valueOf(list.get(i).getId()), true);
        }
    }

    // 保存选中的数据
    public void listCheckedNotifyDataSetChanged() {
        listChecked = new ArrayList<SaveDBBean>();
        SaveDBBean bs = null;
        for (int i = 0; i < list.size(); i++) {
            Boolean isChecked = checkedCartMap.get(Integer.valueOf(list.get(i).getId()));
            //获取的id（list.get(i).getId()）位数不能大于9位，否则报错
            bs = new SaveDBBean();
            if (isChecked != null && isChecked) {
                bs.setId(list.get(i).getId());
                bs.setlCode(list.get(i).getlCode());
                bs.setBarCode(list.get(i).getBarCode());
                bs.setScannerDate(list.get(i).getScannerDate());
                bs.setUserID(list.get(i).getUserID());
                bs.setScannerType(list.get(i).getScannerType());
                bs.setFromWHID(list.get(i).getFromWHID());
                bs.setBillNo(list.get(i).getBillNo());
                bs.setScannerFlag(list.get(i).getScannerFlag());
                bs.setToWHID(list.get(i).getToWHID());
                bs.setScannerNo(list.get(i).getScannerNo());
                listChecked.add(bs);
            }
        }
    }

    //上传文件
    private void uploadFiles() {
        /* 判断当前网络是否可用 */
        if (!NetworkHelper.isNetworkAvailable(this)) {
            Toast.makeText(this, getResources().getString(R.string.toast_networkIsExceptional), Toast.LENGTH_SHORT).show();
            return;
        }
        //文件名称
        final String mFileName = UploadFileInfoHelpter.getFileName(GlobalHelper.getUserName(), operateType);
        //文件内容
        String fileContent = UploadFileInfoHelpter.getUploadBeanContent(UploadCodeFilesActivity.this, listChecked, operateType);
        //写入文件
        FileHelper.writeFile(UploadCodeFilesActivity.this, mFileName, fileContent);
        //写入文件路径
        String filePath = String.format("%s/%s", getFilesDir(), mFileName);
        final File file = new File(filePath);
        //备份文件
        if (!CommonHelper.getExternalStoragePath().equalsIgnoreCase("")) {
            String newFilePath = String.format("%s/%s/%s/%s", CommonHelper.getExternalStoragePath(),
                    AppConfig.ROOTFOLDER, AppConfig.UPLOADBACKUPFOLDER, mFileName);
            FileHelper.copyFile(filePath, newFilePath);
        }
        startDialogProgress("上传中");
        //OkHttpUtils 方法提交参数
        Map<String, String> map = new HashMap<>();
        map.put(AppConfig.USERNAME, GlobalHelper.getUserName());
        map.put(AppConfig.PASSWORD, GlobalHelper.getPassword());
        map.put("Billsort", operateType);
        map.put("Billno", "");
        OkHttpUtils.post()
                .url(AppConfig.URL_APPUPLOADIDCODEFILE)
                .params(map)
                .addFile("idcodeFile", mFileName, file)//文件
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        stopDialogProgress();
                        Toast.makeText(UploadCodeFilesActivity.this, getResources().getString(R.string.network_wifi_low), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, String s) {
                        try {
                            stopDialogProgress();
                            JSONObject ob = new JSONObject(s);
                            String code = ob.getString("returnCode");
                            String msg = ob.getString("returnMsg");
                            if ("0".equals(code)) {
                                if (!CommonHelper.getExternalStoragePath().equalsIgnoreCase("")) {
                                    String oldPath = getFilesDir() + "/" + mFileName;
                                    String newPath = CommonHelper.getExternalStoragePath()
                                            + String.format("/%s/%s/", AppConfig.ROOTFOLDER, AppConfig.BACKUPFOLDER) + mFileName;
                                    //保存备份文件
                                    FileHelper.copyFile(oldPath, newPath);
                                }
                                //删除源文件
                                FileHelper.delFile(UploadCodeFilesActivity.this, mFileName);
                                //删除本地条码
                                Toast.makeText(UploadCodeFilesActivity.this, msg, Toast.LENGTH_SHORT).show();
                                deleteCheckCode();
                            } else if("-2".equals(code)){
                                Toast.makeText(UploadCodeFilesActivity.this, msg, Toast.LENGTH_SHORT).show();
                                openActivity(UploadCodeFilesActivity.this, LoginActivity.class,true);
                            } else {
                                Toast.makeText(UploadCodeFilesActivity.this, msg, Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }

    //解决Toast重复显示
    private Toast mToast;

    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    //返回消失
    public void onBackPressed() {
        cancelToast();
        super.onBackPressed();
    }

    private class CodeAdapters extends BaseAdapter {
        private Context context;
        private List<SaveDBBean> list;
        private LayoutInflater inflater = null;

        public CodeAdapters(Context context, List<SaveDBBean> list) {
            this.context = context;
            this.list = list;
            inflater = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final SaveDBBean bean = list.get(position);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_look_code_item, null);
                holder = new ViewHolder();
                holder.CBox = (CheckBox) convertView.findViewById(R.id.CBox);
                holder.tvHouseName = (TextView) convertView.findViewById(R.id.tvHouseName);
                holder.tvHouseRsName = (TextView) convertView.findViewById(R.id.tvHouseRsName);
                holder.tvCodeName = (TextView) convertView.findViewById(R.id.tvCodeName);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvHouseName.setText("发货仓库：" + bean.getFromWHName());
            holder.tvHouseRsName.setText("收货仓库：" + bean.getToWHName());
            holder.tvCodeName.setText(bean.getBarCode());

            listCheckedNotifyDataSetChanged();
            // 避免由于复用触发点击事件
            holder.CBox.setOnCheckedChangeListener(null);
            Boolean isChecked = checkedCartMap.get(Integer.valueOf(bean.getId()));
            if (isChecked != null && isChecked) {
                holder.CBox.setChecked(true);
            } else {
                holder.CBox.setChecked(false);

            }
            // 选择按钮
            holder.CBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkedCartMap.put(Integer.valueOf(bean.getId()), isChecked);
                    if (isChecked) {

                        // 如果所有项都被选中，则点亮全选按钮
                        if (checkedCartMap.size() == list.size()) {
                            mCheckedAll = true;
                        }
                    } else {
                        // 如果之前是全选状态，则取消全选状态
                        if (checkedCartMap.size() == list.size()) {
                            mCheckedAll = false;
                            onChecked();
                        }
                        checkedCartMap.remove(Integer.valueOf(bean.getId()));
                    }
                    listCheckedNotifyDataSetChanged();
                }
            });
            return convertView;
        }

        public class ViewHolder {
            private CheckBox CBox;
            private TextView tvHouseName, tvHouseRsName;
            private TextView tvCodeName;

        }
    }

}
