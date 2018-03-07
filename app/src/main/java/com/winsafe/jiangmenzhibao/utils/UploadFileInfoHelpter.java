package com.winsafe.jiangmenzhibao.utils;

import android.content.Context;

import com.winsafe.jiangmenzhibao.confing.AppConfig;
import com.winsafe.jiangmenzhibao.entity.SaveDBBean;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by shijie.yang on 2017/6/7.
 */

public class UploadFileInfoHelpter {
    /**
     * 根据用户账号和操作类型获取文件名字
     *
     * @param UserName     用户账号
     * @param pOperateType 操作类型
     * @return 返回文件名字
     */
    public static String getFileName(String UserName, String pOperateType) {
        String result = "";

        String strDateTime = DateTimeHelper.formatDate("yyyyMMddHHmmss", new Date());
        if (pOperateType.equalsIgnoreCase(AppConfig.SAVE_CODE_CK))
            result = String.format("%s_%s_%s.txt", UserName, strDateTime, "Out");
        if (pOperateType.equalsIgnoreCase(AppConfig.SAVE_CODE_TH))
            result = String.format("%s_%s_%s.txt", UserName, strDateTime, "Return");
        if (pOperateType.equalsIgnoreCase(AppConfig.SAVE_CODE_NOT_CK))
            result = String.format("%s_%s_%s.txt", UserName, strDateTime, "OutNot");
        if (pOperateType.equalsIgnoreCase(AppConfig.SAVE_CODE_NOT_TH))
            result = String.format("%s_%s_%s.txt", UserName, strDateTime, "ReturnNot");

        return result;
    }

    /**
     * 产生上传内容MAP
     *
     * @param mapList        已选择的条码列表
     * @param serverFileType 扫描类型
     * @return 返回产生的内容
     */
    public static String getUploadMapContent(Context context, List<Map<String, Object>> mapList, String serverFileType) {
        StringBuffer buffer = new StringBuffer();

        for (Map<String, Object> map : mapList) {
            JSONObject json = JSONHelper.getJSONObjectInstance();

            JSONHelper.put(json, "billNo", map.get("billNo"));
            JSONHelper.put(json, "barCode", map.get("idcode"));
            JSONHelper.put(json, "scannerType", serverFileType);
            JSONHelper.put(json, "scannerDate", DateTimeHelper.formatDate("yyyyMMddhhmmss", new Date()));
            JSONHelper.put(json, "scannerNo", ""); //CommonHelper.getIMEI(context)
            JSONHelper.put(json, "lCode", map.get("productNo")); //物流码三位
            JSONHelper.put(json, "fromWHID", map.get("fromWHID"));
            JSONHelper.put(json, "toWHID", map.get("toWHID"));
            JSONHelper.put(json, "scannerFlag", ""); //扫描类型标识（B,T）
            JSONHelper.put(json, "userID", GlobalHelper.getUserName());
            buffer.append(json.toString() + "\r\n");
        }


        return buffer.toString();
    }

    /**
     * 产生上传内容 BEAN
     *
     * @param beanList       已选择的条码列表
     * @param serverFileType 扫描类型
     * @return 返回产生的内容
     */
    public static String getUploadBeanContent(Context context, List<SaveDBBean> beanList, String serverFileType) {
        StringBuffer buffer = new StringBuffer();
        for (SaveDBBean bean : beanList) {
            JSONObject json = JSONHelper.getJSONObjectInstance();
            JSONHelper.put(json, "billNo", bean.getBillNo());
            JSONHelper.put(json, "barCode", bean.getBarCode());
            JSONHelper.put(json, "scannerType", serverFileType);
            JSONHelper.put(json, "scannerDate", bean.getScannerDate());
            JSONHelper.put(json, "scannerNo", ""); //CommonHelper.getIMEI(context)
            JSONHelper.put(json, "lCode", bean.getScannerNo()); //物流码三位
            JSONHelper.put(json, "fromWHID", bean.getFromWHID());
            JSONHelper.put(json, "toWHID", bean.getToWHID());
            JSONHelper.put(json, "scannerFlag", ""); //扫描类型标识（B,T）
            JSONHelper.put(json, "userID", GlobalHelper.getUserName());
            buffer.append(json.toString() + "\r\n");
        }

        return buffer.toString();
    }

}
