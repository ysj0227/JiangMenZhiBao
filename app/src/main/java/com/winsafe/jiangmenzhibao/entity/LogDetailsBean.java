package com.winsafe.jiangmenzhibao.entity;

import java.util.List;

/**
 * Created by shijie.yang on 2017/7/12.
 */

public class LogDetailsBean {

    /**
     * returnCode : 0
     * returnMsg : 数据查询成功
     * logDetailList : ["第3行 处理错误：箱码[U03000000000000000001000000003]扫箱出托，托码下面的箱码，在本单据中已经先扫描了。"]
     */

    private String returnCode;
    private String returnMsg;
    private List<String> logDetailList;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public List<String> getLogDetailList() {
        return logDetailList;
    }

    public void setLogDetailList(List<String> logDetailList) {
        this.logDetailList = logDetailList;
    }
}
