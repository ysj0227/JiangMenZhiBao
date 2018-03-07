package com.winsafe.jiangmenzhibao.confing;

public class AppConfig {
    //保存的文件名称和路径
    public final static String ROOTFOLDER = "winsafe_jiangmenzhibao";
    public final static String APK_NAME = "JiangMenZhiBao";
    public static final String BACKUPFOLDER = "Backup";
    public static final String UPLOADBACKUPFOLDER = "UploadBackup";
    // 错误日志是否保存
    public final static boolean isLogSave = false;
    // SharedManager info
    public final static String SHARED_NAME = "cache_jmzb";
    //FinalDb数据库
    public final static String DB_OUT = "db_out_jmzb.db";//出库
    public final static String DB_RETURN = "db_return_jmzb.db";//退货
    public final static String DB_OUT_NOT = "db_outNot_jmzb.db";//无单出库
    public final static String DB_RETURN_NOT = "db_returnNot_jmzb.db";//无单退货
    public final static String DB_HISTORY = "db_history_jmzb.db";//历史
    //下载机构，仓库保存数据库
    public final static String DB_ORGAN = "db_organ_jmzb.db";//机构
    public final static String DB_WAREHOUSE = "db_warehouse_jmzb.db";//仓库
    public final static String DB_PRODUCT = "db_product_jmzb.db";//产品

    //URL
//    public static String URL_BASE = "http://syjn.winsafe.cn/appController/";// 正式地址
//    public static final String URL_BASE = "http://192.168.20.40:8857/appController/"; // test
    public static final String URL_BASE = "http://192.168.8.144:8088/appController/"; // ke

    //登录
    public static final String URL_LOGIN = URL_BASE + "appLogin.do";
    //修改密码
    public static final String URL_CHANGE_PWD = URL_BASE + "appChangePwd.do";
    //版本更新
    public static final String CHECK_VERSION_URL = "http://d.winsafe.cn/apk/UpdateService.ashx?action={0}&appName={1}&appVer={2}&appType=android";
    //客商列表
    public static final String URL_APPLOADCOMPANY = URL_BASE + "appLoadCompany.do";
    //新增客商
    public static final String URL_APPADDCOMPANY = URL_BASE + "appAddCompany.do";
    //删除机构
    public static final String URL_APPDELETECOMPANY = URL_BASE + "appdeleteCompany.do";
    //修改机构
    public static final String URL_APPUPDCOMPANY = URL_BASE + "appupdCompany.do";
    //单据下载Billsort=1(出库)，Billsort=3(退货)
    public static final String URL_APPQUERYTAKETICKET = URL_BASE + "appQueryTakeTicket.do";
    //文件上传
    public static final String URL_APPUPLOADIDCODEFILE = URL_BASE + "appUploadIdcodeFile.do";
    //物流追溯
    public static final String URL_APPGETPRODUCTFLOW = URL_BASE + "appGetProductFlow.do";
    //日志
    public static final String URL_APPGETUPLOADIDCODELOGLIST = URL_BASE + "appGetUploadIdcodeLogList.do";
    //机构
    public static final String URL_APPGETCOMPANYBYGUANXIALIST = URL_BASE + "appGetCompanyByGuanxiaList.do";
    //仓库
    public static final String URL_APPGETWAREHOUSEBYAUTHORITYLIST = URL_BASE + "appGetWarehouseByAuthorityList.do";
    //产品下载
    public static final String URL_APPGETPRODUCT = URL_BASE + "appGetProduct.do";
    //日志详情
    public static final String URL_APPGETUPLOADIDCODELOGDETAIL = URL_BASE + "appGetUploadIdcodeLogDetail.do";
    //内外码查询
    public static final String URL_APPINANDOUTCODEQUERY = URL_BASE + "appQueryPrimaryCodeByQrCode.do";

    //用户名密码参数
    public static final String USERNAME = "UserName";
    public static final String PASSWORD = "PassWord";
    public static final String USER_ADMIN = "admin";
    //用户类型
    public static final String COMPANYTYPE1 = "1"; //仓库
    public static final String COMPANYTYPE2 = "2"; //省级经销商
    public static final String COMPANYTYPE3 = "3"; //市经销商
    public static final String COMPANYTYPE4 = "4"; //县级经销商
    public static final String COMPANYTYPE5 = "5"; //零售商
    public static final String COMPANYTYPE6 = "6"; //其他
    public static final String COMPANYTYPE7 = "7"; //销售公司
    //用户名称
    public static final String COMPANYNAME_HOUSE = "仓库";
    public static final String COMPANYNAME_PROVINCE = "省";
    public static final String COMPANYNAME_CITY = "市";
    public static final String COMPANYNAME_COUNTRY = "县";
    public static final String COMPANYNAME_LINGSHOU = "零售";
    public static final String COMPANYNAME_SALL = "销售";

    // 类型
    public static final String OPERATE_TYPE = "operateType";
    public static final String OPERATE_TYPE_CK = "4";//出库
    public static final String OPERATE_TYPE_TH = "8";//退货
    public static final String OPERATE_TYPE_NOT_CK = "17";//无单出库
    public static final String OPERATE_TYPE_NOT_TH = "19";//无单退货
    public static final String OPERATE_TYPE_ZS = "OPERATE_TYPE_ZS";//追溯
    public static final String OPERATE_TYPE_CODE_QUERY = "OPERATE_TYPE_CODEQ";//追溯
    //从不同页面跳转的方式参数
    public static final String SKIP_ACTIVITY = "SKIP_NEXT_ACTIVITY";
    public static final String SKIP_CAPTUREACTIVITY = "CAPTUREACTIVITY";//扫描页面
    public static final String SKIP_UPLOADFILESACTIVITY = "UPLOADFILESACTIVITY";//文件上传页面

    //新增和编辑客商的type
    public static final String CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String ENCODE = "Encode"; //参数
    public static final String COMPANYTYPE = "companytype"; //参数
    public static final String ADD = "3"; //新增
    public static final String EDIT = "4"; //编辑
    //扫描，输入的type
    public static final String TYPE_SCAN = "SCAN"; //扫描
    public static final String TYPE_INPUT = "INPUT"; //输入
    //机构，仓库id，name
    public static final String DATA_NAME = "DATA_NAME";
    public static final String DATA_ID = "DATA_ID";
    //机构，仓库 type（Authority）
    public static final String AUTHORITY_SEND = "0";//发货
    public static final String AUTHORITY_RECEIVE = "1";//收货

    //保存码的类型
    public static final String SAVE_CODE_CK = "4";
    public static final String SAVE_CODE_TH = "8";
    public static final String SAVE_CODE_NOT_CK = "17";
    public static final String SAVE_CODE_NOT_TH = "19";
    //是否有单参数
    public static final String NOT_BILL = "0";//0无单 1有单

    //获取日志的类型
    public static final String GET_LOG_CK = "4"; //有单出库
    public static final String GET_LOG_TH = "8"; //有单退货
    public static final String GET_LOG_NOT_CK = "17"; //无单出库
    public static final String GET_LOG_NOT_TH = "19"; //无单退货

    //单据参数
    public static final String CUSTOMID = "customID";
    public static final String FROMORGNAME = "fromOrgName";
    public static final String FROMORGID = "fromOrgID";
    public static final String FROMWHNAME = "fromWHName";
    public static final String FROMWHID = "fromWHID";
    public static final String BILLSORT = "billSort";
    public static final String TOORGNAME = "toOrgName";
    public static final String TOORGID = "toOrgID";
    public static final String TOWHNAME = "toWHName";
    public static final String TOWHID = "toWHID";
    public static final String BILLNO = "billNo";
    public static final String BILLID = "billID";
    public static final String TOTALCOUNT = "totalCount";
    //okhttp 返回数据
    public static String strJSONObject = "";

}
