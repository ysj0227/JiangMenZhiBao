package com.winsafe.jiangmenzhibao.view;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.base.BaseFragment;
import com.winsafe.jiangmenzhibao.utils.CommonHelper;

import butterknife.ButterKnife;


public abstract class AppBaseFragment extends BaseFragment implements OnClickListener {
	protected void setHeader(LayoutInflater inflater, View v, String title, boolean isOtherVisiable, int bgId, String strOther, OnClickListener clickListenr) {
		RelativeLayout rlTop = relativeLayoutInit(v, R.id.rlTop);
		rlTop.setBackgroundColor(getResources().getColor(R.color.header_title));

		View view = inflater.inflate(R.layout.activity_common_header, null);

		RelativeLayout.LayoutParams viewLP = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, CommonHelper.dp2px(getActivity(), 60));
		viewLP.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

		RelativeLayout.LayoutParams btnLP = new RelativeLayout.LayoutParams(CommonHelper.dp2px(getActivity(), 40), CommonHelper.dp2px(getActivity(), 40));
		btnLP.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		btnLP.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		btnLP.leftMargin = CommonHelper.dp2px(getActivity(), 5);

		RelativeLayout.LayoutParams tvLP = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		tvLP.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

		RelativeLayout.LayoutParams btnOtherLP = new RelativeLayout.LayoutParams(CommonHelper.dp2px(getActivity(), 60), CommonHelper.dp2px(getActivity(), 40));
		btnOtherLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		btnOtherLP.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		btnOtherLP.rightMargin = CommonHelper.dp2px(getActivity(), 5);

		rlTop.addView(view, viewLP);

		TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
		tvTitle.setLayoutParams(tvLP);
		Button btnBack = (Button) v.findViewById(R.id.btnBack);
		Button btnOther = (Button) v.findViewById(R.id.btnOther);
		btnOther.setText(strOther);
		btnBack.setLayoutParams(btnLP);
		btnOther.setLayoutParams(btnOtherLP);

		setBoldTextForTextView(tvTitle, title);
		btnBack.setVisibility(View.GONE);
		
		if (isOtherVisiable) {
			btnOther.setVisibility(View.VISIBLE);
			btnOther.setBackgroundResource(bgId);
			if (clickListenr != null)
				btnOther.setOnClickListener(clickListenr);
		}
	}

	protected View setLayout(LayoutInflater inflater, ViewGroup container, int layoutId) {
		View v = inflater.inflate(layoutId, container, false);
		ButterKnife.bind(this, v);
		initView(inflater, v);
		setListener();

		return v;
	}

	protected abstract void initView(LayoutInflater inflater, View v);

	protected abstract void setListener();
	protected Dialog dialog;
	protected void startDialogProgress(String tip) {

		if (dialog == null) {
			dialog = new Dialog(getActivity(), R.style.Son_dialog);
			LayoutInflater inflater = getActivity().getLayoutInflater();
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
}
