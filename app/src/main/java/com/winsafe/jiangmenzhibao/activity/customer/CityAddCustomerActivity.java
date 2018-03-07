package com.winsafe.jiangmenzhibao.activity.customer;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.activity.LoginActivity;
import com.winsafe.jiangmenzhibao.city.CurrentItemName;
import com.winsafe.jiangmenzhibao.city.OnWheelChangedListener;
import com.winsafe.jiangmenzhibao.city.WheelView;
import com.winsafe.jiangmenzhibao.city.adapters.ArrayWheelAdapter;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.utils.GlobalHelper;
import com.winsafe.jiangmenzhibao.utils.StringHelper;
import com.winsafe.jiangmenzhibao.view.AppBaseActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 县级新增客商
 * Created by shijie.yang on 2017/5/23.
 */

public class CityAddCustomerActivity extends AppBaseActivity implements OnWheelChangedListener {
    @BindView(R.id.tvArea)
    TextView tvArea;
    @BindView(R.id.ivArea)
    ImageView ivArea;
    @BindView(R.id.tvDetailsAdds)
    EditText tvDetailsAdds;
    @BindView(R.id.tvName)
    EditText tvName;
    @BindView(R.id.tvNumber)
    EditText tvNumber;
    @BindView(R.id.tvPhone)
    EditText tvPhone;
    @BindView(R.id.btnReset)//重置
            Button btnReset;
    @BindView(R.id.btnSure)//增加
            Button btnSure;

    // 区域选择
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private TextView mBtnCancel, mBtnConfirm;
    private PopupWindow popupwindow;
    private int state;
    //省市区编码
    private String provinceCode = null, cityCode = null, countryCode = null;
    private Bundle mBundle=null;
    private String mType="";

    //名称，电话，编码，省市区code，及名称，详细地址
    private String CompantFullName="",Telnum="",Encode="",Provincename="",
            Cityname="",Countyname="",Registedaddress="",companytype="";

    private String Url="",oldEncode="";
    private String progressStr="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.layout_add_customer);

    }

    @Override
    protected void initView() {
        mBundle=getIntent().getExtras();
        mType=mBundle.getString(AppConfig.CUSTOMER_NAME);
        companytype=mBundle.getString(AppConfig.COMPANYTYPE);

        String title="";
        if (AppConfig.ADD.equals(mType)){
            title=GlobalHelper.showString(this, R.string.title_addcity_customer);
            progressStr=GlobalHelper.showString(this, R.string.title_progress_add);
            Url=AppConfig.URL_APPADDCOMPANY;
            Encode=mBundle.getString(AppConfig.ENCODE);
            oldEncode="";
        }else if(AppConfig.EDIT.equals(mType)){
            title=GlobalHelper.showString(this, R.string.title_editcity_customer_msg);
            progressStr=GlobalHelper.showString(this, R.string.title_progress_edit);
            CompantFullName=mBundle.getString("CompantFullName");
            Telnum=mBundle.getString("Telnum");
            Encode=mBundle.getString("Encode");
            provinceCode=mBundle.getString("Province");
            Provincename=mBundle.getString("Provincename");
            cityCode=mBundle.getString("City");
            Cityname=mBundle.getString("Cityname");
            countryCode=mBundle.getString("County");
            Countyname=mBundle.getString("Countyname");
            Registedaddress=mBundle.getString("Registedaddress");

            Url=AppConfig.URL_APPUPDCOMPANY;
            oldEncode=Encode;

            tvArea.setText(Provincename+"-"+Cityname+"-"+Countyname);
            tvDetailsAdds.setText(Registedaddress);
            tvName.setText(CompantFullName);
            tvNumber.setText(Encode);
            tvPhone.setText(Telnum);
        }
        setHeader(title, true, false, 0, "", null);
    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.ivArea, R.id.tvArea, R.id.btnReset, R.id.btnSure})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivArea://区域选择
            case R.id.tvArea://区域选择
                citySelected();
                break;
            case R.id.btnReset://重置
                tvArea.setText(null);
                tvDetailsAdds.setText(null);
                tvNumber.setText(null);
                tvName.setText(null);
                tvPhone.setText(null);
                break;
            case R.id.btnSure://确定
                addCustomers();
                break;

            default:
                break;

        }
    }
    //新增客户
    private void addCustomers(){
        String area = tvArea.getText().toString().trim();
        String detailsAdds = tvDetailsAdds.getText().toString().trim();
        String compantFullName = tvName.getText().toString().trim();
        String parentEncode= tvNumber.getText().toString().trim();
        String phone = tvPhone.getText().toString().trim();
        if (StringHelper.isNullOrEmpty(area)) {
            Toast.makeText(this, GlobalHelper.showString(this,R.string.toast_select_area), Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringHelper.isNullOrEmpty(detailsAdds)) {
            Toast.makeText(this, GlobalHelper.showString(this,R.string.toast_input_details_adds), Toast.LENGTH_SHORT).show();
            return;
        }
        if (detailsAdds.length()<3||detailsAdds.length()>128){
            Toast.makeText(this, GlobalHelper.showString(this, R.string.toast_input_details_adds_length), Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringHelper.isNullOrEmpty(compantFullName)) {
            Toast.makeText(this, GlobalHelper.showString(this,R.string.toast_input_organ_name), Toast.LENGTH_SHORT).show();
            return;
        }
        if (compantFullName.length()>25){
            Toast.makeText(this, GlobalHelper.showString(this, R.string.toast_input_organ_name_length), Toast.LENGTH_SHORT).show();
            return;
        }
        //判断是否有空格
        if (compantFullName.contains(" ")) {
            Toast.makeText(this, GlobalHelper.showString(this, R.string.toast_input_organ_name_black), Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringHelper.isNullOrEmpty(parentEncode)) {
            Toast.makeText(this, GlobalHelper.showString(this,R.string.toast_input_out_number), Toast.LENGTH_SHORT).show();
            return;
        }
        if (parentEncode.length()<3||parentEncode.length()>20) {
            Toast.makeText(this, GlobalHelper.showString(this, R.string.toast_input_out_number_length), Toast.LENGTH_SHORT).show();
            return;
        }
        //判断是否有空格
        if (parentEncode.contains(" ")) {
            Toast.makeText(this, GlobalHelper.showString(this, R.string.toast_input_out_number_black), Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringHelper.isNullOrEmpty(phone)) {
            Toast.makeText(this, GlobalHelper.showString(this,R.string.toast_input_phone), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!phone.substring(0,1).equals("0") && !phone.substring(0,1).equals("1")){
            Toast.makeText(this, GlobalHelper.showString(this,R.string.toast_input_mobile_success), Toast.LENGTH_SHORT).show();
            return;
        }else {
            //判断座机
            if (phone.substring(0,1).equals("0")&& !phone.contains("-")){
                Toast.makeText(this, GlobalHelper.showString(this,R.string.toast_input_mobile_success), Toast.LENGTH_SHORT).show();
                return;
            }
            //判断手机号码
            if (phone.substring(0,1).equals("1")){
                if (!GlobalHelper.isMobileNO(phone)){
                    Toast.makeText(this, GlobalHelper.showString(this, R.string.toast_input_phone_success), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        startDialogProgress(progressStr);
        int comtype= Integer.valueOf(companytype)+1;
        Map<String, String> map = new HashMap<>();
        map.put("companytype", String.valueOf(comtype));//账号类型
        map.put("parentEncode", Encode);//登录返回的上级机构
        map.put("province", provinceCode);//省市区code
        map.put("city", cityCode);
        map.put("county", countryCode);
        map.put("registedaddress", detailsAdds);//详细地址
        map.put("compantFullName", compantFullName);//机构名称
        map.put("encode", parentEncode); //外部编码
        map.put("telnum", phone);//手机号
        map.put("oldencode", oldEncode);
        map.put("UserName", GlobalHelper.getUserName() );
        //Log.i("TAG", companytype+"===市");
        OkHttpUtils.post().url(Url).params(map).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        stopDialogProgress();
                        Toast.makeText(CityAddCustomerActivity.this, getResources().getString(R.string.network_wifi_low), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(Call call, String s) {
                        stopDialogProgress();
                        try {
                            JSONObject object = new JSONObject(s);
                            String code = object.getString("returnCode");
                            String msg = object.getString("returnMsg");
                            if ("0".equals(code)) {
                                Toast.makeText(CityAddCustomerActivity.this, msg, Toast.LENGTH_SHORT).show();
                                CityAddCustomerActivity.this.finish();
                            }else if("-2".equals(code)){
                                Toast.makeText(CityAddCustomerActivity.this, msg, Toast.LENGTH_SHORT).show();
                                openActivity(CityAddCustomerActivity.this, LoginActivity.class,true);
                            } else {
                                Toast.makeText(CityAddCustomerActivity.this, msg, Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    // 区域选择=======

    private void setUpListener() {
        //添加change事件
        mViewProvince.addChangingListener(this);
        mViewCity.addChangingListener(this);
        mViewDistrict.addChangingListener(this);

    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(CityAddCustomerActivity.this, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        mViewProvince.setCurrentItem(CurrentItemName.currentItemPosition(tvArea.getText().toString()));//设置当前显示的第几条数据

        updateCities();
        updateAreas();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
            mCurrentZipCode_district = mDistrictDatasMap_zip.get(mCurrentZipCode_citis)[0];
        } else if (wheel == mViewCity) {
            updateAreas();
            mCurrentZipCode_district = mDistrictDatasMap_zip.get(mCurrentZipCode_citis)[0];
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode_district = mDistrictDatasMap_zip.get(mCurrentZipCode_citis)[newValue];
        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];//获取市的名称
        mCurrentZipCode_citis = mCitisDatasMap_zip.get(mCurrentZipCode_province)[pCurrent];

        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mViewDistrict.setCurrentItem(0);
        // 新加更新（区的信息）
        mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];//获取省的名称
        mCurrentZipCode_province = mProvinceDatas_zip[pCurrent];//获取省的zip

        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    private void showSelectedResult() {
        //省市区编码
        provinceCode = mCurrentZipCode_province;
        cityCode = mCurrentZipCode_citis;
        countryCode = mCurrentZipCode_district;
        tvArea.setText(mCurrentProviceName + "-" + mCurrentCityName + "-" + mCurrentDistrictName);
       // Log.i("TAG", "省： " + mCurrentZipCode_province + " 市： " + mCurrentZipCode_citis + " 区：" + mCurrentZipCode_district);
    }

    private void citySelected() {
        if (state == 1 && popupwindow.isShowing()) {
            state = 0;
            popupwindow.dismiss();
        } else {
            LayoutInflater inflater = LayoutInflater.from(this);
            View viewLayout = inflater.inflate(R.layout.pop_city, null);
            popupwindow = new PopupWindow(viewLayout, ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT);
            // popupwindow.setAnimationStyle(R.style.AnimationFade);
            popupwindow.showAtLocation(viewLayout, Gravity.BOTTOM, 0, 0);

            ColorDrawable cd = new ColorDrawable(0x00000000);
            popupwindow.setBackgroundDrawable(cd);
            popupwindow.update();
            popupwindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            popupwindow.setTouchable(true); // 设置popupwindow可点击
            popupwindow.setOutsideTouchable(true); // 设置popupwindow外部可点击
            popupwindow.setFocusable(false); // 获取焦点
            state = 1;

            mViewProvince = (WheelView) viewLayout.findViewById(R.id.id_province);
            mViewCity = (WheelView) viewLayout.findViewById(R.id.id_city);
            mViewDistrict = (WheelView) viewLayout.findViewById(R.id.id_district);
            mBtnConfirm = (TextView) viewLayout.findViewById(R.id.btn_confirm);
            mBtnCancel = (TextView) viewLayout.findViewById(R.id.btn_cancel);
            setUpListener();
            setUpData();
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btn_confirm:
                            showSelectedResult();
                            popupwindow.dismiss();
                            break;
                        case R.id.btn_cancel:
                            popupwindow.dismiss();
                            break;

                        default:
                            break;

                    }
                }
            };

            mBtnConfirm.setOnClickListener(listener);
            mBtnCancel.setOnClickListener(listener);


        }
    }

}
