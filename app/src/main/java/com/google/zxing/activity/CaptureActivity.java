package com.google.zxing.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.camera.BeepManager;
import com.google.zxing.camera.CameraManager;
import com.google.zxing.decode.CaptureActivityHandler;
import com.google.zxing.decode.DecodeThread;
import com.google.zxing.decode.FinishListener;
import com.google.zxing.decode.InactivityTimer;
import com.google.zxing.help.DrawHelper;
import com.google.zxing.view.ViewfinderView;
import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.activity.AroundCodeActivity;
import com.winsafe.jiangmenzhibao.activity.RetrospectActivity;
import com.winsafe.jiangmenzhibao.activity.UploadCodeFilesActivity;
import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.entity.SaveDBBean;
import com.winsafe.jiangmenzhibao.utils.CodeRuleHelper;
import com.winsafe.jiangmenzhibao.utils.DateTimeHelper;
import com.winsafe.jiangmenzhibao.utils.EmojiEditText;
import com.winsafe.jiangmenzhibao.utils.GlobalHelper;
import com.winsafe.jiangmenzhibao.utils.MyDialog;
import com.winsafe.jiangmenzhibao.utils.StringHelper;
import com.winsafe.jiangmenzhibao.view.AppBaseActivity;
import com.winsafe.jiangmenzhibao.view.MyApp;

import net.tsz.afinal.FinalDb;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.OnClick;

public class CaptureActivity extends AppBaseActivity implements SurfaceHolder.Callback {
    private static final String DIALOG_TITLE = "友情提示";
    private static final String DIALOG_MSG = "打开摄像头失败，请开启摄像头权限";
    private static final String BUTTON_CONFIRM = "确定";
    private static final String BUTTON_CANCEL = "取消";
    private static final String TOAST_SET_PARAMS = "请设置参数";
    private static final String TOAST_SET_SCAN_MODE = "请设置扫描模式";
    private static final String TOAST_SET_SCAN_TYPE = "请设置扫描类型";

    public static final String SCAN_TYPE = "scan_type";
    public static final String SCAN_MODE = "scan_mode";
    public static final String SCAN_NOREPEAT = "scan_no_repeat";
    public static final String SCAN_REPEAT = "scan_repeat";

    public static final String SCAN_BARCODE = "scan_barcode";

    public static String mCurrentState; // 条形码二维码选择状态
    public SharedPreferences mSharedPreferences;// 存储二维码条形码选择的状态

    private boolean hasSurface;
    private BeepManager mBeepManager;// 声音震动管理器。如果扫描成功后可以播放一段音频，也可以震动提醒，可以通过配置来决定扫描成功后的行为。
    private String mCharacterSet;

    // 活动监控器，用于省电，如果手机没有连接电源线，那么当相机开启后如果一直处于不被使用状态则该服务会将当前activity关闭。
    // 活动监控器全程监控扫描活跃状态，与CaptureActivity生命周期相同.每一次扫描过后都会重置该监控，即重新倒计时。
    private InactivityTimer mInactivityTimer;
    private CameraManager mCameraManager;
    private Vector<BarcodeFormat> mDecodeFormats;// 编码格式
    private CaptureActivityHandler mHandler;// 解码线程

    private ViewfinderView cvScanView;
    private SurfaceView svPreview;
    private SurfaceHolder mSurfaceHolder;

    private Bundle mBundle = null;
    private String mScanMode = "", mScanType = "";

    @BindView(R.id.btnEnter)
    Button btnEnter;
    @BindView(R.id.btnLook)
    Button btnLook;
    @BindView(R.id.btnLight)
    Button btnLight;
    //是否开关灯
    private boolean isOpenLight;
    //定义接收传递的参数
    private String customID = "", fromOrgID = "", fromWHName = "", billSort = "",
            fromWHID = "", toOrgID = "", toWHName = "", toOrgName = "", totalCount = "",
            billNo = "", billID = "", toWHID = "", fromOrgName = "";

    // ******************* Activity 流程 *******************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSetting();
        setLayout(R.layout.activity_capture);
    }

    @Override
    protected void initView() {
        mBundle = getIntent().getExtras();
        mScanType = mBundle.getString(AppConfig.OPERATE_TYPE);
        mScanMode = mBundle.getString(SCAN_MODE);

        customID = mBundle.getString(AppConfig.CUSTOMID);
        fromOrgName = mBundle.getString(AppConfig.FROMORGNAME);
        fromOrgID = mBundle.getString(AppConfig.FROMORGID);
        fromWHName = mBundle.getString(AppConfig.FROMWHNAME);
        fromWHID = mBundle.getString(AppConfig.FROMWHID);

        toOrgName = mBundle.getString(AppConfig.TOORGNAME);
        toOrgID = mBundle.getString(AppConfig.TOORGID);
        toWHName = mBundle.getString(AppConfig.TOWHNAME);
        toWHID = mBundle.getString(AppConfig.TOWHID);
        totalCount = mBundle.getString(AppConfig.TOTALCOUNT);

        billNo = mBundle.getString(AppConfig.BILLNO);
        billID = mBundle.getString(AppConfig.BILLID);
        billSort = mBundle.getString(AppConfig.BILLSORT);

        String title = "";
        if (AppConfig.OPERATE_TYPE_CK.equals(mScanType)) {
            title = getResources().getString(R.string.title_scanned_out);
        } else if (AppConfig.OPERATE_TYPE_TH.equals(mScanType)) {
            title = getResources().getString(R.string.title_scanned_return);
        } else if (AppConfig.OPERATE_TYPE_NOT_CK.equals(mScanType)) {
            title = getResources().getString(R.string.title_scanned_not_out);
        } else if (AppConfig.OPERATE_TYPE_NOT_TH.equals(mScanType)) {
            title = getResources().getString(R.string.title_scanned_not_return);
        } else if (AppConfig.OPERATE_TYPE_ZS.equals(mScanType)) {
            title = getResources().getString(R.string.title_scanned_zs);
            btnLook.setVisibility(View.GONE);
        }else if (AppConfig.OPERATE_TYPE_CODE_QUERY.equals(mScanType)) {
            title = getResources().getString(R.string.button_around_look);
            btnLook.setVisibility(View.GONE);
        }
        setHeader(title, true, true, 0, "", this);
        init();

    }

    @Override
    protected void setListener() {
    }

    /**
     * 主要对相机进行初始化工作
     */
    @Override
    protected void onResume() {
        super.onResume();

        mInactivityTimer.onActivity();

        cvScanView = (ViewfinderView) findViewById(R.id.cvScanView);
        cvScanView.setCameraManager(mCameraManager);
        // cameraManager.setManualFramingRect(360, 300);
        cvScanView.setVisibility(View.VISIBLE);
        cvScanView.refreshDrawableState();

        mDecodeFormats = new Vector<BarcodeFormat>(7);
        mDecodeFormats.clear();
        mDecodeFormats.addAll(DecodeThread.ONE_D_FORMATS); // 支持条码扫描
        mDecodeFormats.add(BarcodeFormat.QR_CODE); // 支持二维码扫描（QR码）
        mDecodeFormats.add(BarcodeFormat.DATA_MATRIX); // 支持二维码扫描（DATA_MARTIX码）

        if (null != mHandler) {
            mHandler.setDecodeFormats(mDecodeFormats);
        }

        resetStatusView();

        mSurfaceHolder = svPreview.getHolder();
        if (hasSurface)
            initCamera(mSurfaceHolder);
        else
            mSurfaceHolder.addCallback(this); // 如果SurfaceView已经渲染完毕，会回调surfaceCreated，在surfaceCreated中调用initCamera()

        mBeepManager.updatePrefs(); // 加载声音配置，其实在BeemManager的构造器中也会调用该方法，即在onCreate的时候会调用一次
        mInactivityTimer.onResume(); // 恢复活动监控器

        // 获取传递数据以判断是否要加载该页面
//		mBundle = getIntent().getExtras();
//		if (mBundle != null) {
//			mScanMode = mBundle.getString(SCAN_MODE);
//			if (StringHelper.isNullOrEmpty(mScanMode)) {
//				MyDialog.showToast(this, TOAST_SET_SCAN_MODE);
//				this.finish();
//			}
//		}

    }

    /**
     * 暂停活动监控器,关闭摄像头
     */
    @Override
    protected void onPause() {
        if (mHandler != null) {
            mHandler.quitSynchronously();
            mHandler = null;
        }

        mInactivityTimer.onPause();
        mCameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.svPreview);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    /**
     * 停止活动监控器,保存最后选中的扫描类型
     */
    @Override
    protected void onDestroy() {
        mInactivityTimer.shutdown();
        super.onDestroy();
    }

    // *******************************************************************************************************
    // * 扫描结果处理
    // *******************************************************************************************************
    public void handleDecode(Result result, Bitmap barcode, float scaleFactor) {
        mInactivityTimer.onActivity();

        if (barcode != null) {
            mBeepManager.playBeepSoundAndVibrate();
            DrawHelper.drawResultPoints(barcode, scaleFactor, result);
        }
        if (mScanMode.equals(SCAN_NOREPEAT))
            handleNoRepeatDecode(result, barcode);
        else if (mScanMode.equals(SCAN_REPEAT))
            handleRepeatDecode(result, barcode);
    }

    //不能多次扫描
    private void handleNoRepeatDecode(Result result, Bitmap barcode) {
//        String code = result.getText().trim();
    }

    //多次扫描
    protected void handleRepeatDecode(Result result, Bitmap barcode) {
        rescan();
        String code = result.getText().trim();

        if (AppConfig.OPERATE_TYPE_CK.equals(mScanType)) {
            //有单出库
            savaScanedCode(code, MyApp.outDB, AppConfig.OPERATE_TYPE_CK,AppConfig.TYPE_SCAN);

        } else if (AppConfig.OPERATE_TYPE_TH.equals(mScanType)) {
            //有单退货
            savaScanedCode(code, MyApp.returnDB, AppConfig.OPERATE_TYPE_TH,AppConfig.TYPE_SCAN);
        } else if (AppConfig.OPERATE_TYPE_NOT_CK.equals(mScanType)) {
            //无单出库
            savaScanedCode(code, MyApp.outNotDB, AppConfig.OPERATE_TYPE_NOT_CK,AppConfig.TYPE_SCAN);
        } else if (AppConfig.OPERATE_TYPE_NOT_TH.equals(mScanType)) {
            //无单出库
            savaScanedCode(code, MyApp.returnNotDB, AppConfig.OPERATE_TYPE_NOT_TH,AppConfig.TYPE_SCAN);
        } else if (AppConfig.OPERATE_TYPE_ZS.equals(mScanType)) {
            //物流追溯
            if (!CodeRuleHelper.logisticalRules(CaptureActivity.this, code)) {
                return;
            }
            String mCode = code.contains("=") ? code.split("=")[1] : code;
            Bundle bundle = new Bundle();
            bundle.putString("code", mCode);
            openActivity(CaptureActivity.this, RetrospectActivity.class,bundle, false);
        }else if (AppConfig.OPERATE_TYPE_CODE_QUERY.equals(mScanType)) {
            //内外码关系查询
            if (!CodeRuleHelper.inAndOutCodeRules(CaptureActivity.this, code)) {
                return;
            }
            String mCode = code.contains("=") ? code.split("=")[1] : code;
            Bundle bundle = new Bundle();
            bundle.putString("code", mCode);
            openActivity(CaptureActivity.this, AroundCodeActivity.class,bundle, false);
        }
    }

    //重复扫描的延时
    protected void rescan() {
        mHandler.sendEmptyMessageDelayed(R.id.restart_preview, 500);
    }

    /*
      @code: 扫描的码
      @db：保存的数据库
     */
    private void savaScanedCode(String code, FinalDb db, String scannerType, String isScanOrInput) {
        //判断码规则
        if (!CodeRuleHelper.codeRules(CaptureActivity.this, code)) {
            return;
        }
        String mCode = code.contains("=") ? code.split("=")[1] : code;

        //当输入条码的时候关闭dialog
        if (AppConfig.TYPE_INPUT.equals(isScanOrInput)){
            if (dialog!=null){
                dialog.dismiss();
                dialog.cancel();
                dialog = null;
            }
        }
        List<SaveDBBean> list = db.findAllByWhere(SaveDBBean.class, " barCode=\"" + mCode + "\"" + " and " + " userID=\"" + GlobalHelper.getUserName() + "\"");
        if (list == null || list.size() == 0) {
            String time = DateTimeHelper.getCurrentDateTimeString();
            String lCode = mCode.substring(1, 4).trim();
            SaveDBBean bean = new SaveDBBean();
            bean.setScannerType(scannerType);//扫描类型
            bean.setUserID(MyApp.shared.getValueByKey(AppConfig.USERNAME));
            bean.setScannerDate(time);
            bean.setBillNo(billNo);
            bean.setScannerFlag("");
            bean.setBarCode(mCode);
            bean.setScannerNo(lCode);
            bean.setlCode(lCode);
            bean.setId(time.substring(5, time.length()));//设置码的id
            bean.setFromWHID(fromWHID);//发货仓库id
            bean.setToWHID(toWHID);//收货仓库id
            bean.setFromWHName(fromWHName);//发货仓库名称
            bean.setToWHName(toWHName);//收货仓库名称
            db.save(bean);
            //保存扫码历史
            MyApp.historyDB.save(bean);
            showToast(getResources().getString(R.string.code_save_success));
        } else {
            showToast(getResources().getString(R.string.code_scanned));
//            Toast.makeText(this, getResources().getString(R.string.code_scanned), Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick({R.id.btnEnter, R.id.btnLook, R.id.btnLight})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEnter://输入条码
                inputCode();
                break;
            case R.id.btnLook://查看条码

                mBundle.putString(AppConfig.SKIP_ACTIVITY, AppConfig.SKIP_CAPTUREACTIVITY);//扫描页面跳转
                if (AppConfig.OPERATE_TYPE_CK.equals(mScanType)) {//出库
                    mBundle.putString(AppConfig.OPERATE_TYPE, AppConfig.OPERATE_TYPE_CK);

                } else if (AppConfig.OPERATE_TYPE_TH.equals(mScanType)) { //退货
                    mBundle.putString(AppConfig.OPERATE_TYPE, AppConfig.OPERATE_TYPE_TH);

                } else if (AppConfig.OPERATE_TYPE_NOT_CK.equals(mScanType)) { //无单出库
                    mBundle.putString(AppConfig.OPERATE_TYPE, AppConfig.OPERATE_TYPE_NOT_CK);

                } else if (AppConfig.OPERATE_TYPE_NOT_TH.equals(mScanType)) { //无单退货
                    mBundle.putString(AppConfig.OPERATE_TYPE, AppConfig.OPERATE_TYPE_NOT_TH);
                }
                openActivity(CaptureActivity.this, UploadCodeFilesActivity.class, mBundle, false);

                break;
            case R.id.btnLight://开关灯
                if (isOpenLight) {
                    mCameraManager.offLight();
                    btnLight.setBackgroundResource(R.mipmap.ic_light_off);
                    isOpenLight = false;
                } else {
                    mCameraManager.onLight();
                    btnLight.setBackgroundResource(R.mipmap.ic_light_on);
                    isOpenLight = true;
                }
                break;

            default:
                break;

        }
    }

    //输入条码
    private Dialog dialog;
    private void inputCode() {
        dialog = new Dialog(CaptureActivity.this, R.style.Son_dialog);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_code, null);

        final EmojiEditText etCode = (EmojiEditText) view.findViewById(R.id.etCode);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tv_sure = (TextView) view.findViewById(R.id.tv_sure);
        String titleMsg = "";
        String hintMsg = "";
        if (AppConfig.OPERATE_TYPE_CK.equals(mScanType) || AppConfig.OPERATE_TYPE_NOT_CK.equals(mScanType)) {//出库
            titleMsg=GlobalHelper.showString(CaptureActivity.this, R.string.dialog_title);
            hintMsg = GlobalHelper.showString(CaptureActivity.this, R.string.dialog_code_out);

        } else if (AppConfig.OPERATE_TYPE_TH.equals(mScanType) || AppConfig.OPERATE_TYPE_NOT_TH.equals(mScanType)) {//退货
            titleMsg=GlobalHelper.showString(CaptureActivity.this, R.string.dialog_title);
            hintMsg = GlobalHelper.showString(CaptureActivity.this, R.string.dialog_code_return);
        } else if (AppConfig.OPERATE_TYPE_ZS.equals(mScanType)) {//追溯
            titleMsg=GlobalHelper.showString(CaptureActivity.this, R.string.dialog_title_wl);
            hintMsg = GlobalHelper.showString(CaptureActivity.this, R.string.dialog_code_zs);
        }else if (AppConfig.OPERATE_TYPE_CODE_QUERY.equals(mScanType)) {//追溯
            titleMsg=GlobalHelper.showString(CaptureActivity.this, R.string.dialog_title_inout);
            hintMsg = GlobalHelper.showString(CaptureActivity.this, R.string.dialog_code_query);
        }
        tvTitle.setText(titleMsg);
        etCode.setHint(hintMsg);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_sure://确定
                        String code = etCode.getText().toString().trim();
                        if (StringHelper.isNullOrEmpty(code)) {
                            Toast.makeText(CaptureActivity.this, GlobalHelper.showString(CaptureActivity.this, R.string.code_input), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (AppConfig.OPERATE_TYPE_CK.equals(mScanType)) {
                            //出库
                            savaScanedCode(code, MyApp.outDB, AppConfig.OPERATE_TYPE_CK,AppConfig.TYPE_INPUT);
                        } else if (AppConfig.OPERATE_TYPE_TH.equals(mScanType)) {
                            //退货
                            savaScanedCode(code, MyApp.returnDB, AppConfig.OPERATE_TYPE_TH,AppConfig.TYPE_INPUT);
                        } else if (AppConfig.OPERATE_TYPE_NOT_CK.equals(mScanType)) {
                            //无单出库
                            savaScanedCode(code, MyApp.outNotDB, AppConfig.OPERATE_TYPE_NOT_CK,AppConfig.TYPE_INPUT);
                        } else if (AppConfig.OPERATE_TYPE_NOT_TH.equals(mScanType)) {
                            //无单退货
                            savaScanedCode(code, MyApp.returnNotDB, AppConfig.OPERATE_TYPE_NOT_TH,AppConfig.TYPE_INPUT);
                        } else if (AppConfig.OPERATE_TYPE_ZS.equals(mScanType)) {
                            // /追溯
                            if (!CodeRuleHelper.logisticalRules(CaptureActivity.this, code)) {
                                return;
                            }
                            String mCode = code.contains("=") ? code.split("=")[1] : code;
                            dialog.dismiss();
                            Bundle bundle = new Bundle();
                            bundle.putString("code", mCode);
                            openActivity(CaptureActivity.this, RetrospectActivity.class, bundle, false);
                        }else if (AppConfig.OPERATE_TYPE_CODE_QUERY.equals(mScanType)) {
                            //内外码关系查询
                            if (!CodeRuleHelper.inAndOutCodeRules(CaptureActivity.this, code)) {
                                return;
                            }
                            String mCode = code.contains("=") ? code.split("=")[1] : code;
                            Bundle bundle = new Bundle();
                            bundle.putString("code", mCode);
                            openActivity(CaptureActivity.this, AroundCodeActivity.class,bundle, false);
                        }
                        break;
                    case R.id.tv_cancel://取消
                        dialog.dismiss();
                        break;

                    default:
                        break;

                }
            }
        };
        tv_sure.setOnClickListener(listener);
        tv_cancel.setOnClickListener(listener);

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
    }

    // *******************************************************************************************************
    // * 摄像头初始化工作
    // *******************************************************************************************************
    private void initSetting() {
        // 初始化窗口设置
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 保持屏幕处于点亮状态
//        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 竖屏
    }

    private void init() {
        // 初始化功能组件
        hasSurface = false;
        mInactivityTimer = new InactivityTimer(this);
        mBeepManager = new BeepManager(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mCurrentState = this.mSharedPreferences.getString("currentState", "qrcode");
        mCameraManager = new CameraManager(getApplication());

        // 初始化视图
        svPreview = (SurfaceView) findViewById(R.id.svPreview);
    }

    /**
     * 初始化摄像头。 打开摄像头，检查摄像头是否被开启及是否被占用
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null)
            return;
        if (mCameraManager.isOpen())
            return;

        try {
            mCameraManager.openDriver(surfaceHolder);

            if (mHandler == null)
                mHandler = new CaptureActivityHandler(this, mDecodeFormats, mCharacterSet, mCameraManager);
        } catch (IOException ioe) {
            MyDialog.showAlertDialog(this, DIALOG_TITLE, DIALOG_MSG, BUTTON_CONFIRM, BUTTON_CANCEL,
                    new FinishListener(this), new FinishListener(this));
        } catch (RuntimeException e) {
            MyDialog.showAlertDialog(this, DIALOG_TITLE, DIALOG_MSG, BUTTON_CONFIRM, BUTTON_CANCEL,
                    new FinishListener(this), new FinishListener(this));
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null)
            return;

        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    /**
     * 展示状态视图和扫描窗口，隐藏结果视图
     */
    private void resetStatusView() {
        cvScanView.setVisibility(View.VISIBLE);
    }

    public void drawViewfinder() {
        cvScanView.drawViewfinder();
    }

    /**
     * 闪光灯调节器。自动检测环境光线强弱并决定是否开启闪光灯
     */
    public ViewfinderView getViewfinderView() {
        return cvScanView;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public CameraManager getCameraManager() {
        return mCameraManager;
    }

    /**
     * 在经过一段延迟后重置相机以进行下一次扫描。 成功扫描过后可调用此方法立刻准备进行下次扫描
     *  delayMS
     */
    public void restartPreviewAfterDelay(long delayMS) {
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        resetStatusView();
    }

    /**
     * 无延时继续扫描
     */
    public void continuePreviewScan() {
        if (mHandler != null) {
            mHandler.restartPreviewAndDecode();
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    //解决Toast重复显示
    private Toast mToast;
    public void showToast(String text) {
        if(mToast == null) {
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
}