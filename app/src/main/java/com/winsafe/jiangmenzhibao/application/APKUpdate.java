package com.winsafe.jiangmenzhibao.application;

import android.content.Context;

import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.utils.CommonHelper;

public class APKUpdate {
	/**
	 * 检查APK更新
	 * 
	 * @param context
	 *            上下文
	 * @param isNOUpdateDisplay
	 *            是否显示没有更新时的信息
	 */
	public static void checkAPKVersion(Context context, boolean isNOUpdateDisplay) {
		String savePath = CommonHelper.getExternalStoragePath() + "/" + CommonHelper.getAppPackageName(context);
		String chkVerURL = AppConfig.CHECK_VERSION_URL;
		String saveFileName = AppConfig.APK_NAME;

		chkVerURL = chkVerURL.replace("{0}", "chkver").replace("{1}", saveFileName).replace("{2}", CommonHelper.getAppVersionName(context));
		AppUpdate appUpdate = new AppUpdate(context, chkVerURL, savePath, saveFileName + ".apk");
		appUpdate.checkUpdateInfo(isNOUpdateDisplay);
	}
}
