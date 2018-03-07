package com.winsafe.jiangmenzhibao.entity;

import java.util.List;

/**
 * Created by shijie.yang on 2017/6/6.
 */

public class ScanOutBean {

    /**
     * returnCode : 0
     * returnMsg : 正确
     * returnData : [{"customID":"1111","fromOrgID":"5516ec3d-803a-4cc6-9415-9d99cc26fd3b","fromWHName":"默认仓库","billSort":"1","fromWHID":"300423ff-6188-427f-a259-bb6135e8c861","toOrgID":"6ba2dd3a-6648-4f68-aedd-3559db7f753b","toWHName":"经销商Test默认仓库","toOrgName":"经销商Test","takeTicketDTL":[{"lCode":"002","batchNumber":"","ticketCount":"11.0","unitID":"4","productName":"产口名称2","productID":"f6a4f987-0044-44ed-93e8-8d1bf63ce85a"}],"totalCount":"11.0","billNo":"TT201705240001","billID":"1111","toWHID":"e7041b5e-5909-4b85-a7f6-41fc579c1cc2","fromOrgName":"公司名称"},{"customID":"003","fromOrgID":"5516ec3d-803a-4cc6-9415-9d99cc26fd3b","fromWHName":"默认仓库","billSort":"1","fromWHID":"300423ff-6188-427f-a259-bb6135e8c861","toOrgID":"6ba2dd3a-6648-4f68-aedd-3559db7f753b","toWHName":"经销商Test默认仓库","toOrgName":"经销商Test","takeTicketDTL":[{"lCode":"CODE0000003","batchNumber":"","ticketCount":"2.0","unitID":"11","productName":"产品003","productID":"4adf565d-066f-4330-8f74-ce3e2201d1a2"},{"lCode":"002","batchNumber":"","ticketCount":"2.0","unitID":"4","productName":"产口名称2","productID":"f6a4f987-0044-44ed-93e8-8d1bf63ce85a"}],"totalCount":"4.0","billNo":"TT201706050001","billID":"003","toWHID":"e7041b5e-5909-4b85-a7f6-41fc579c1cc2","fromOrgName":"公司名称"}]
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
         * customID : 1111
         * fromOrgID : 5516ec3d-803a-4cc6-9415-9d99cc26fd3b
         * fromWHName : 默认仓库
         * billSort : 1
         * fromWHID : 300423ff-6188-427f-a259-bb6135e8c861
         * toOrgID : 6ba2dd3a-6648-4f68-aedd-3559db7f753b
         * toWHName : 经销商Test默认仓库
         * toOrgName : 经销商Test
         * takeTicketDTL : [{"lCode":"002","batchNumber":"","ticketCount":"11.0","unitID":"4","productName":"产口名称2","productID":"f6a4f987-0044-44ed-93e8-8d1bf63ce85a"}]
         * totalCount : 11.0
         * billNo : TT201705240001
         * billID : 1111
         * toWHID : e7041b5e-5909-4b85-a7f6-41fc579c1cc2
         * fromOrgName : 公司名称
         */

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
        private List<TakeTicketDTLBean> takeTicketDTL;

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

        public List<TakeTicketDTLBean> getTakeTicketDTL() {
            return takeTicketDTL;
        }

        public void setTakeTicketDTL(List<TakeTicketDTLBean> takeTicketDTL) {
            this.takeTicketDTL = takeTicketDTL;
        }

        public static class TakeTicketDTLBean {
            /**
             * lCode : 002
             * batchNumber :
             * ticketCount : 11.0
             * unitID : 4
             * productName : 产口名称2
             * productID : f6a4f987-0044-44ed-93e8-8d1bf63ce85a
             */

            private String lCode;
            private String batchNumber;
            private String ticketCount;
            private String unitID;
            private String productName;
            private String productID;

            public String getLCode() {
                return lCode;
            }

            public void setLCode(String lCode) {
                this.lCode = lCode;
            }

            public String getBatchNumber() {
                return batchNumber;
            }

            public void setBatchNumber(String batchNumber) {
                this.batchNumber = batchNumber;
            }

            public String getTicketCount() {
                return ticketCount;
            }

            public void setTicketCount(String ticketCount) {
                this.ticketCount = ticketCount;
            }

            public String getUnitID() {
                return unitID;
            }

            public void setUnitID(String unitID) {
                this.unitID = unitID;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public String getProductID() {
                return productID;
            }

            public void setProductID(String productID) {
                this.productID = productID;
            }
        }
    }
}
