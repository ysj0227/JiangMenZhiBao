package com.winsafe.jiangmenzhibao.view;

import android.app.Dialog;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.application.AppMgr;
import com.winsafe.jiangmenzhibao.base.BaseActivity;
import com.winsafe.jiangmenzhibao.city.model.CityModel;
import com.winsafe.jiangmenzhibao.city.model.DistrictModel;
import com.winsafe.jiangmenzhibao.city.model.ProvinceModel;
import com.winsafe.jiangmenzhibao.city.service.XmlParserHandler;
import com.winsafe.jiangmenzhibao.utils.CommonHelper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import butterknife.ButterKnife;


public abstract class AppBaseActivity extends BaseActivity implements OnClickListener {
	protected void setHeader(String title, boolean isBackVisiable, boolean isOtherVisiable, int bgId,
                             String OtherStr, OnClickListener clickListenr) {
		RelativeLayout rlTop = relativeLayoutInit(R.id.rlTop);
		rlTop.setBackgroundColor(getResources().getColor(R.color.header_title));

		View view = this.getLayoutInflater().inflate(R.layout.activity_common_header, null);

		RelativeLayout.LayoutParams viewLP = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				CommonHelper.dp2px(this, 60));
		viewLP.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

		RelativeLayout.LayoutParams btnBackLP = new RelativeLayout.LayoutParams(CommonHelper.dp2px(this, 30),
				CommonHelper.dp2px(this, 30));
		btnBackLP.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		btnBackLP.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		btnBackLP.leftMargin = CommonHelper.dp2px(this, 5);

		RelativeLayout.LayoutParams tvLP = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		tvLP.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

		RelativeLayout.LayoutParams btnOtherLP = new RelativeLayout.LayoutParams(CommonHelper.dp2px(this, 70),
				CommonHelper.dp2px(this, 40));
		btnOtherLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		btnOtherLP.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		btnOtherLP.rightMargin = CommonHelper.dp2px(this, 5);

		rlTop.addView(view, viewLP);

		TextView tvTitle = textViewInit(R.id.tvTitle);
		Button btnBack = buttonInit(R.id.btnBack);
		Button btnOther = buttonInit(R.id.btnOther);
		btnOther.setText(OtherStr);
		tvTitle.setLayoutParams(tvLP);
		btnBack.setLayoutParams(btnBackLP);
		btnOther.setLayoutParams(btnOtherLP);

		if (isBackVisiable) {
			btnBack.setVisibility(View.VISIBLE);
			btnBack.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					AppBaseActivity.this.finish();
				}
			});
		}
		if (isOtherVisiable) {
			btnOther.setVisibility(View.VISIBLE);
			btnOther.setBackgroundResource(bgId);
			if (clickListenr != null)
				btnOther.setOnClickListener(clickListenr);
		}

		setBoldTextForTextView(tvTitle, title);
	}

	protected void setLayout(int layoutId) {
		setContentView(layoutId);
		ButterKnife.bind(this);
		AppMgr.getInstance().addActivity(this);
		initView();
		setListener();
	}

	protected abstract void initView();

	protected abstract void setListener();

	protected Dialog dialog;
	protected void startDialogProgress(String tip) {

		if (dialog == null) {
			dialog = new Dialog(this, R.style.Son_dialog);
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.dialog_loading, null);
			TextView tvTip = (TextView) view.findViewById(R.id.tvTip);
			tvTip.setText(tip);
			dialog.setContentView(view);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

	}
	protected void stopDialogProgress() {
		if (dialog != null) {
			dialog.dismiss();
			dialog.cancel();
			dialog = null;
		}
	}
	/**
	 * 所有省 市 区的名称
	 */
	protected String[] mProvinceDatas;
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	//保存当前 省 市 区的zipCode
	protected String[]  mProvinceDatas_zip;
	protected Map<String, String[]> mCitisDatasMap_zip = new HashMap<String, String[]>();
	protected Map<String, String[]> mDistrictDatasMap_zip = new HashMap<String, String[]>();

	/**
	 * key - 区 values -通过 邮编  获取位置
	 */
	protected Map<String, String> mZipcodeDatasMap_Province = new HashMap<String, String>();
	protected Map<String, String> mZipcodeDatasMap_Citis = new HashMap<String, String>();
	protected Map<String, String> mZipcodeDatasMap_District = new HashMap<String, String>();

	/**
	 * 当前省 市 区的名称
	 */
	protected String mCurrentProviceName;
	protected String mCurrentCityName;
	protected String mCurrentDistrictName ="";

	/**
	 * 当前区的邮政编码
	 */
	protected String mCurrentZipCode_province ="";
	protected String mCurrentZipCode_citis ="";
	protected String mCurrentZipCode_district ="";

	/**
	 * 解析省市区的XML数据
	 */

	protected void initProvinceDatas()
	{
		List<ProvinceModel> provinceList = null;
		AssetManager asset = getAssets();
		try {
			InputStream input = asset.open("province_new_datas.xml");
			// 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			provinceList = handler.getDataList();
			//*/ 初始化默认选中的省、市、区
			if (provinceList!= null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				mCurrentZipCode_province=provinceList.get(0).getZipcode();

				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList!= null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					mCurrentZipCode_citis=cityList.get(0).getZipcode();

					List<DistrictModel> districtList = cityList.get(0).getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentZipCode_district = districtList.get(0).getZipcode();
				}
			}
			//*/
			mProvinceDatas = new String[provinceList.size()];
			mProvinceDatas_zip = new String[provinceList.size()];
			for (int i=0; i< provinceList.size(); i++) {
				// 遍历所有省的数据
				mProvinceDatas[i] = provinceList.get(i).getName();
				mProvinceDatas_zip[i]= provinceList.get(i).getZipcode();//保存编码

				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				String[] cityNames_zip = new String[cityList.size()];

				mZipcodeDatasMap_Province.put(provinceList.get(i).getZipcode(), i+"");//获取位置
				for (int j=0; j< cityList.size(); j++) {
					// 遍历省下面的所有市的数据
					cityNames[j] = cityList.get(j).getName();
					cityNames_zip[j] = cityList.get(j).getZipcode();

					List<DistrictModel> districtList = cityList.get(j).getDistrictList();
					String[] distrinctNameArray = new String[districtList.size()];
					String[] distrinctNameArray_zip = new String[districtList.size()];

					DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
					DistrictModel[] distrinctArray_zip = new DistrictModel[districtList.size()];
					for (int k=0; k<districtList.size(); k++) {
						// 遍历市下面所有区/县的数据
						DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
						DistrictModel districtModel_zip = new DistrictModel(districtList.get(k).getZipcode(), districtList.get(k).getZipcode());
						// 区/县对于的邮编，保存到mZipcodeDatasMap
						mZipcodeDatasMap_District.put(districtList.get(k).getZipcode(), k+""); //获取位置
						distrinctArray[k] = districtModel;//名称 区
						distrinctNameArray[k] = districtModel.getName();

						distrinctArray_zip[k] = districtModel_zip;//编码区
						distrinctNameArray_zip[k] = districtModel_zip.getZipcode();
					}
					// 市-区/县的数据，保存到mDistrictDatasMap
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
					mDistrictDatasMap_zip.put(cityNames_zip[j], distrinctNameArray_zip);

					mZipcodeDatasMap_Citis.put(cityList.get(j).getZipcode(), j+"");//获取位置
				}
				// 省-市的数据，保存到mCitisDatasMap
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);//保存名称
				mCitisDatasMap_zip.put(provinceList.get(i).getZipcode(), cityNames_zip);//保存编码
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
	}

}
