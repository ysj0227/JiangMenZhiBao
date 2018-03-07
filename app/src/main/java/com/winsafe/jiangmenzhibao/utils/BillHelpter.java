package com.winsafe.jiangmenzhibao.utils;

import android.os.Bundle;

import com.winsafe.jiangmenzhibao.confing.AppConfig;

/**
 * Created by shijie.yang on 2017/6/6.
 */

public class BillHelpter {
    /**
     * customID : 1111
     * fromOrgID : 5516ec3d-803a-4cc6-9415-9d99cc26fd3b
     * fromWHName : 默认仓库
     * billSort : 1
     * fromWHID : 300423ff-6188-427f-a259-bb6135e8c861
     * toOrgID : 6ba2dd3a-6648-4f68-aedd-3559db7f753b
     * toWHName : 经销商Test默认仓库
     * toOrgName : 经销商Test
     * totalCount : 11.0
     * billNo : TT201705240001
     * billID : 1111
     * toWHID : e7041b5e-5909-4b85-a7f6-41fc579c1cc2
     * fromOrgName : 公司名称
     */
    //设置传递的单据参数
    public static void setBill(String customID, String fromOrgName, String fromOrgID,
                               String fromWHName, String fromWHID, String billSort,
                               String toOrgName, String toOrgID, String toWHName,
                               String toWHID, String billNo, String billID, String totalCount
    ) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConfig.CUSTOMID, customID);
        bundle.putString(AppConfig.FROMORGNAME, fromOrgName);
        bundle.putString(AppConfig.FROMORGID, fromOrgID);
        bundle.putString(AppConfig.FROMWHNAME, fromWHName);
        bundle.putString(AppConfig.FROMWHID, fromWHID);

        bundle.putString(AppConfig.BILLSORT, billSort);

        bundle.putString(AppConfig.TOORGNAME, toOrgName);
        bundle.putString(AppConfig.TOORGID, toOrgID);
        bundle.putString(AppConfig.TOWHNAME, toWHName);
        bundle.putString(AppConfig.TOWHID, toWHID);

        bundle.putString(AppConfig.BILLNO, billNo);
        bundle.putString(AppConfig.BILLID, billID);
        bundle.putString(AppConfig.TOTALCOUNT, totalCount);
    }

    //设置传递的单据参数
    public static void setBillBundle(Bundle bundle, String customID, String fromOrgName, String fromOrgID,
                                     String fromWHName, String fromWHID, String billSort,
                                     String toOrgName, String toOrgID, String toWHName,
                                     String toWHID, String billNo, String billID, String totalCount
    ) {
        bundle.putString(AppConfig.CUSTOMID, customID);
        bundle.putString(AppConfig.FROMORGNAME, fromOrgName);
        bundle.putString(AppConfig.FROMORGID, fromOrgID);
        bundle.putString(AppConfig.FROMWHNAME, fromWHName);
        bundle.putString(AppConfig.FROMWHID, fromWHID);

        bundle.putString(AppConfig.BILLSORT, billSort);

        bundle.putString(AppConfig.TOORGNAME, toOrgName);
        bundle.putString(AppConfig.TOORGID, toOrgID);
        bundle.putString(AppConfig.TOWHNAME, toWHName);
        bundle.putString(AppConfig.TOWHID, toWHID);

        bundle.putString(AppConfig.BILLNO, billNo);
        bundle.putString(AppConfig.BILLID, billID);
        bundle.putString(AppConfig.TOTALCOUNT, totalCount);
    }


}
