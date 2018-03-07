package com.winsafe.jiangmenzhibao.view;

import android.view.View.OnClickListener;

import com.winsafe.jiangmenzhibao.application.AppMgr;
import com.winsafe.jiangmenzhibao.base.BaseFragmentActivity;

import butterknife.ButterKnife;


public abstract class AppBaseFragmentActivity extends BaseFragmentActivity implements OnClickListener {
	protected void setLayout(int layoutId) {
		setContentView(layoutId);
		ButterKnife.bind(this);
		AppMgr.getInstance().addActivity(this);
		initView();
		setListener();
	}

	protected abstract void initView();

	protected abstract void setListener();
}
