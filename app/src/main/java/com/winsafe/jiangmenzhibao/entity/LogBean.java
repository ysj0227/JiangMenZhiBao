package com.winsafe.jiangmenzhibao.entity;

import java.util.List;

/**
 * Created by shijie.yang on 2017/5/22.
 */

public class LogBean {

    /**
     * returnCode : 0
     * returnMsg : 正确
     * returnData : [{"executedLineNo":0,"billSort":4,"status":"0","orderCode":1496975382000,"warningCount":0,"errorCount":0,"code":"f244d922-2717-4797-aa38-8446a8bf312c","companyName":null,"billSortName":null,"correctCount":0,"companyCode":"367ab7bf-ccba-4db2-bb8a-73d688eceb23","creator":"e3e78123-d2dd-4984-8bd5-c4bc87cfc46b","dataStatus":"1","creationTime":"20170609102943"}]
     */

    private String returnCode;
    private String returnMsg;
    private List<ReturnDataBean> returnData;

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

    public List<ReturnDataBean> getReturnData() {
        return returnData;
    }

    public void setReturnData(List<ReturnDataBean> returnData) {
        this.returnData = returnData;
    }

    public static class ReturnDataBean {
        /**
         * executedLineNo : 0
         * billSort : 4
         * status : 0
         * orderCode : 1496975382000
         * warningCount : 0
         * errorCount : 0
         * code : f244d922-2717-4797-aa38-8446a8bf312c
         * companyName : null
         * billSortName : null
         * correctCount : 0
         * companyCode : 367ab7bf-ccba-4db2-bb8a-73d688eceb23
         * creator : e3e78123-d2dd-4984-8bd5-c4bc87cfc46b
         * dataStatus : 1
         * creationTime : 20170609102943
         */

        private int executedLineNo;
        private int billSort;
        private String status;
        private long orderCode;
        private int warningCount;
        private int errorCount;
        private String code;
        private Object companyName;
        private Object billSortName;
        private int correctCount;
        private String companyCode;
        private String creator;
        private String dataStatus;
        private String creationTime;

        public int getExecutedLineNo() {
            return executedLineNo;
        }

        public void setExecutedLineNo(int executedLineNo) {
            this.executedLineNo = executedLineNo;
        }

        public int getBillSort() {
            return billSort;
        }

        public void setBillSort(int billSort) {
            this.billSort = billSort;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public long getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(long orderCode) {
            this.orderCode = orderCode;
        }

        public int getWarningCount() {
            return warningCount;
        }

        public void setWarningCount(int warningCount) {
            this.warningCount = warningCount;
        }

        public int getErrorCount() {
            return errorCount;
        }

        public void setErrorCount(int errorCount) {
            this.errorCount = errorCount;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Object getCompanyName() {
            return companyName;
        }

        public void setCompanyName(Object companyName) {
            this.companyName = companyName;
        }

        public Object getBillSortName() {
            return billSortName;
        }

        public void setBillSortName(Object billSortName) {
            this.billSortName = billSortName;
        }

        public int getCorrectCount() {
            return correctCount;
        }

        public void setCorrectCount(int correctCount) {
            this.correctCount = correctCount;
        }

        public String getCompanyCode() {
            return companyCode;
        }

        public void setCompanyCode(String companyCode) {
            this.companyCode = companyCode;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getDataStatus() {
            return dataStatus;
        }

        public void setDataStatus(String dataStatus) {
            this.dataStatus = dataStatus;
        }

        public String getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(String creationTime) {
            this.creationTime = creationTime;
        }
    }
}
