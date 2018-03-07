package com.winsafe.jiangmenzhibao.application;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.utils.CloudHelper;
import com.winsafe.jiangmenzhibao.utils.CommonHelper;
import com.winsafe.jiangmenzhibao.utils.ConfigHelper;
import com.winsafe.jiangmenzhibao.utils.LogHelper;
import com.winsafe.jiangmenzhibao.utils.MyDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//import android.app.AlertDialog;

/**
 * Application update manager
 * 
 * @author Aaron.Zhao
 */
public class AppUpdate {
	private Context mContext;

	private String updateMsg = "";
	private String updateTitle = "";
	private String downloadTitle = "";

	private String mChkVerURL = "";
	private String mUpdateURL = "";
	private String mSavePath = "";
	private String mSaveFileName = "";

	private ProgressBar mProgressBar;
	private int progress;
	private Thread checkThread;
	private Thread downloadThread;
	private boolean interceptFlag = false;
	private boolean isNOUpdateDisplay = false;

	private static final int DOWNLOAD_UPDATE = 1;
	private static final int DOWNLOAD_OVER = 2;
	private static final int NEW_UPDATE = 3;
	private static final int NO_UPDATE = 4;

	public AppUpdate(Context context, String chkVerURL, String savePath, String saveFileName) {
		this.mContext = context;
		this.mChkVerURL = chkVerURL;
		this.mSavePath = savePath;
		this.mSaveFileName = saveFileName;

		updateTitle = mContext.getResources().getString(R.string.update_title);
		updateMsg = mContext.getResources().getString(R.string.update_msg);
		downloadTitle = mContext.getResources().getString(R.string.download_title);
	}

	private Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
				case DOWNLOAD_UPDATE:
					mProgressBar.setProgress(progress);
					break;
				case DOWNLOAD_OVER:
					installAPK();
					break;
				case NEW_UPDATE:
					mUpdateURL = (String) msg.obj;
					String strDownload = mContext.getResources().getString(R.string.button_download);
//					String strIgnore = mContext.getResources().getString(R.string.button_ignore);
//					MyDialog.showAlertDialog(mContext, updateTitle, updateMsg, strDownload, strIgnore, downloadListener, cancelUpdateListener);
					MyDialog.showAlertDialog(mContext, updateTitle, updateMsg, strDownload, "", downloadListener, null);
					break;
				case NO_UPDATE:
					if (isNOUpdateDisplay)
						MyDialog.showToast(mContext, mContext.getResources().getString(R.string.update_no));
					break;
			}
			return false;
		}
	});

	private OnClickListener downloadListener = new OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			// 显示下载框
			showDownloadDialog();
		}
	};

	/*private DialogInterface.OnClickListener cancelUpdateListener = new OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}
	};*/

	public void checkUpdateInfo(boolean isNOUpdateDisplay) {
		this.isNOUpdateDisplay = isNOUpdateDisplay;
		checkThread = new Thread(mCheckRunnable);
		checkThread.start();
	}

	/*private DialogInterface.OnClickListener cancelDownloadListener = new OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			interceptFlag = true;
		}
	};*/

	private void showDownloadDialog() {
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update_progress, null);
		mProgressBar = (ProgressBar) v.findViewById(R.id.progress);
		
//		String strCancel = mContext.getResources().getString(R.string.button_cancel);
//		MyDialog.showAlertDialog(mContext, downloadTitle, v, strCancel, cancelDownloadListener);
		
		/* 显示进度框的时候不要取消按钮 */
		Builder builder = new Builder(mContext);
		builder.setTitle(downloadTitle);
		builder.setView(v);
		builder.show();

		downloadAPK();
	}

	private Runnable mCheckRunnable = new Runnable() {
		public void run() {
			String result = CloudHelper.getStringDataOfCloudByPost(mChkVerURL, ConfigHelper.TextCode.UTF_8);
			if (result.contains("http")) {
				mHandler.sendMessage(Message.obtain(mHandler, NEW_UPDATE, result));
			}
			else {
				mHandler.sendMessage(Message.obtain(mHandler, NO_UPDATE, result));
			}
		}
	};

	private Runnable mDownloadRunnable = new Runnable() {
		public void run() {
			FileOutputStream fileOutputStream = null;
			InputStream inputStram = null;
			HttpURLConnection con = null;
			try {
				URL url = new URL(mUpdateURL);
				con = (HttpURLConnection) url.openConnection();
				con.connect();

				int length = con.getContentLength();
				inputStram = con.getInputStream();

				File newFilePath = new File(mSavePath);
				if (!newFilePath.exists()) {
					newFilePath.mkdirs();
				}

				File newFile = new File(mSavePath, mSaveFileName);
				if (!newFile.exists()) {
					newFile.createNewFile();
				}

				fileOutputStream = new FileOutputStream(newFile);
				int count = 0;

				byte[] buffer = new byte[1024];
				do {
					int numread = inputStram.read(buffer);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					mHandler.sendEmptyMessage(DOWNLOAD_UPDATE);
					if (numread <= 0) {
						mHandler.sendEmptyMessage(DOWNLOAD_OVER);
						break;
					}
					fileOutputStream.write(buffer, 0, numread);
				} while (!interceptFlag);
			}
			catch (MalformedURLException e) {
				LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()), ":MalformedURLException" + e.getMessage(),
						true);
			}
			catch (IOException e) {
				LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()), ":IOException" + e.getMessage(), true);
			}
			finally {
				try {
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					if (inputStram != null) {
						inputStram.close();
					}
					if (con != null) {
						con.disconnect();
					}
				}
				catch (IOException e) {
					LogHelper.exportLog(CommonHelper.getCName(new Exception()), CommonHelper.getMName(new Exception()), ":IOException" + e.getMessage(), true);
				}

			}
		}
	};

	private void downloadAPK() {
		downloadThread = new Thread(mDownloadRunnable);
		downloadThread.start();
	}

	private void installAPK() {
		File apkFile = new File(mSavePath, mSaveFileName);
		if (!apkFile.exists()) {
			return;
		}

		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			Uri data = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", apkFile);
			intent.setDataAndType(data, "application/vnd.android.package-archive");
		} else {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
		}
		mContext.startActivity(intent);
	}
}
