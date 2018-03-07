package com.winsafe.jiangmenzhibao.entity;

import java.io.Serializable;

/**
 * Created by shijie.yang on 2017/6/6.
 */

public class SaveDBBean implements Serializable {

    //上传的文件的参数
//             "scannerType":"1",
//             "userID":"admin",
//             "lCode":"030",
//             "scannerDate":"20170601055030",
//             "fromWHID":"300423ff-6188-427f-a259-bb6135e8c861",
//             "billNo":"TT201705240001",
//             "scannerFlag":"B",
//             "barCode":"U03000000010011705242017052203",
//             "scannerNo":"a1000012636399",
//             "toWHID":"e7041b5e-5909-4b85-a7f6-41fc579c1cc2"

    private String id;
    private String customID;
    private String fromOrgID;
    private String fromWHName;
    private String billSort;
    private String fromWHID;
    private String toOrgID;
    private String toWHName;
    private String toOrgName;
    private String totalCount;
    private String billNo;
    private String billID;
    private String toWHID;
    private String fromOrgName;

    private String scannerType;
    private String userID;
    private String lCode;
    private String scannerDate;
    private String scannerFlag;
    private String barCode;
    private String scannerNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomID() {
        return customID;
    }

    public void setCustomID(String customID) {
        this.customID = customID;
    }

    public String getFromOrgID() {
        return fromOrgID;
    }

    public void setFromOrgID(String fromOrgID) {
        this.fromOrgID = fromOrgID;
    }

    public String getFromWHName() {
        return fromWHName;
    }

    public void setFromWHName(String fromWHName) {
        this.fromWHName = fromWHName;
    }

    public String getBillSort() {
        return billSort;
    }

    public void setBillSort(String billSort) {
        this.billSort = billSort;
    }

    public String getFromWHID() {
        return fromWHID;
    }

    public void setFromWHID(String fromWHID) {
        this.fromWHID = fromWHID;
    }

    public String getToOrgID() {
        return toOrgID;
    }

    public void setToOrgID(String toOrgID) {
        this.toOrgID = toOrgID;
    }

    public String getToWHName() {
        return toWHName;
    }

    public void setToWHName(String toWHName) {
        this.toWHName = toWHName;
    }

    public String getToOrgName() {
        return toOrgName;
    }

    public void setToOrgName(String toOrgName) {
        this.toOrgName = toOrgName;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillID() {
        return billID;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public String getToWHID() {
        return toWHID;
    }

    public void setToWHID(String toWHID) {
        this.toWHID = toWHID;
    }

    public String getFromOrgName() {
        return fromOrgName;
    }

    public void setFromOrgName(String fromOrgName) {
        this.fromOrgName = fromOrgName;
    }

    public String getScannerType() {
        return scannerType;
    }

    public void setScannerType(String scannerType) {
        this.scannerType = scannerType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getlCode() {
        return lCode;
    }

    public void setlCode(String lCode) {
        this.lCode = lCode;
    }

    public String getScannerDate() {
        return scannerDate;
    }

    public void setScannerDate(String scannerDate) {
        this.scannerDate = scannerDate;
    }

    public String getScannerFlag() {
        return scannerFlag;
    }

    public void setScannerFlag(String scannerFlag) {
        this.scannerFlag = scannerFlag;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getScannerNo() {
        return scannerNo;
    }

    public void setScannerNo(String scannerNo) {
        this.scannerNo = scannerNo;
    }
}
