package com.winsafe.jiangmenzhibao.view;

import android.app.Application;

import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.entity.OrganDataBean;
import com.winsafe.jiangmenzhibao.entity.ProductNoBean;
import com.winsafe.jiangmenzhibao.entity.SaveDBBean;
import com.winsafe.jiangmenzhibao.entity.WarehouseDataBean;
import com.winsafe.jiangmenzhibao.utils.SharedManager;
import com.winsafe.jiangmenzhibao.utils.UnknownException;

import net.tsz.afinal.FinalDb;

public class MyApp extends Application {

	public static MyApp myApp;
	public static SharedManager shared;
	public static FinalDb outDB,returnDB,historyDB,outNotDB,returnNotDB,
			organDB, warehouseDB,productNoDB;//出库，退货,历史,有单出库，有单退货，机构，仓库,产品数据库

	@Override
	public void onCreate() {
		super.onCreate();
		createDB();
		init();
	}

	private void init() {
		myApp = this;
		if (shared == null) {
			shared = new SharedManager(this, AppConfig.SHARED_NAME);
		}
		crashUnknownException();

	}
	//初始化创建数据库
	private void createDB(){
		//初始实体 扫描保存数据
		outDB= FinalDb.create(this,AppConfig.DB_OUT);
		returnDB= FinalDb.create(this,AppConfig.DB_RETURN);
		historyDB= FinalDb.create(this,AppConfig.DB_HISTORY);
		outNotDB= FinalDb.create(this,AppConfig.DB_OUT_NOT);
		returnNotDB= FinalDb.create(this,AppConfig.DB_RETURN_NOT);
		//下载机构，仓库数据保存本地
		organDB = FinalDb.create(this, AppConfig.DB_ORGAN);
		warehouseDB = FinalDb.create(this, AppConfig.DB_WAREHOUSE);
		productNoDB = FinalDb.create(this, AppConfig.DB_PRODUCT);

		outDB.save(new SaveDBBean());
		returnDB.save(new SaveDBBean());
		historyDB.save(new SaveDBBean());
		outNotDB.save(new SaveDBBean());
		returnNotDB.save(new SaveDBBean());

		organDB.save(new OrganDataBean());
		warehouseDB.save(new WarehouseDataBean());
		productNoDB.save(new ProductNoBean());
	}

	//获取异常日志
	private void crashUnknownException() {
		UnknownException mUnknownException = UnknownException.getInstance();
		mUnknownException.init(getApplicationContext(), AppConfig.isLogSave);
	}


}
