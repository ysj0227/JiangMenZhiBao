package com.winsafe.jiangmenzhibao.entity;

import java.util.List;

/**
 * Created by shijie.yang on 2017/5/31.
 */

public class LoginBean {


    /**
     * returnCode : 0
     * returnMsg : 正确
     * returnData : {"encode":"20000465","companytype":"2","employeeID":"","dealerTypeArea":{"COMPANYTYPE":[{"value":"省级经销商","key":"2"},{"value":"仓库","key":"1"},{"value":"销售公司","key":"7"},{"value":"","key":"6"},{"value":"零售商","key":"5"},{"value":"县经销商","key":"4"},{"value":"市经销商","key":"3"}],"dealerTypeArea":{"memo":"经销商的【机构类型】的区间2、3、4，分别为省、市、县。区间最大值5为零售商","value":"2,3,4,5"}},"menuList":[],"realName":"省级app账号","appIsHaveBILL":{"value":"0","key":"appIsHaveBILL"}}
     */

    private String returnCode;
    private String returnMsg;
    private ReturnDataBean returnData;

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

    public ReturnDataBean getReturnData() {
        return returnData;
    }

    public void setReturnData(ReturnDataBean returnData) {
        this.returnData = returnData;
    }

    public static class ReturnDataBean {
        /**
         * encode : 20000465
         * companytype : 2
         * employeeID :
         * dealerTypeArea : {"COMPANYTYPE":[{"value":"省级经销商","key":"2"},{"value":"仓库","key":"1"},{"value":"销售公司","key":"7"},{"value":"","key":"6"},{"value":"零售商","key":"5"},{"value":"县经销商","key":"4"},{"value":"市经销商","key":"3"}],"dealerTypeArea":{"memo":"经销商的【机构类型】的区间2、3、4，分别为省、市、县。区间最大值5为零售商","value":"2,3,4,5"}}
         * menuList : []
         * realName : 省级app账号
         * appIsHaveBILL : {"value":"0","key":"appIsHaveBILL"}
         */

        private String encode;
        private String companytype;
        private String employeeID;
        private DealerTypeAreaBeanX dealerTypeArea;
        private String realName;
        private AppIsHaveBILLBean appIsHaveBILL;
        private List<?> menuList;

        public String getEncode() {
            return encode;
        }

        public void setEncode(String encode) {
            this.encode = encode;
        }

        public String getCompanytype() {
            return companytype;
        }

        public void setCompanytype(String companytype) {
            this.companytype = companytype;
        }

        public String getEmployeeID() {
            return employeeID;
        }

        public void setEmployeeID(String employeeID) {
            this.employeeID = employeeID;
        }

        public DealerTypeAreaBeanX getDealerTypeArea() {
            return dealerTypeArea;
        }

        public void setDealerTypeArea(DealerTypeAreaBeanX dealerTypeArea) {
            this.dealerTypeArea = dealerTypeArea;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public AppIsHaveBILLBean getAppIsHaveBILL() {
            return appIsHaveBILL;
        }

        public void setAppIsHaveBILL(AppIsHaveBILLBean appIsHaveBILL) {
            this.appIsHaveBILL = appIsHaveBILL;
        }

        public List<?> getMenuList() {
            return menuList;
        }

        public void setMenuList(List<?> menuList) {
            this.menuList = menuList;
        }

        public static class DealerTypeAreaBeanX {
            /**
             * COMPANYTYPE : [{"value":"省级经销商","key":"2"},{"value":"仓库","key":"1"},{"value":"销售公司","key":"7"},{"value":"","key":"6"},{"value":"零售商","key":"5"},{"value":"县经销商","key":"4"},{"value":"市经销商","key":"3"}]
             * dealerTypeArea : {"memo":"经销商的【机构类型】的区间2、3、4，分别为省、市、县。区间最大值5为零售商","value":"2,3,4,5"}
             */

            private DealerTypeAreaBean dealerTypeArea;
            private List<COMPANYTYPEBean> COMPANYTYPE;

            public DealerTypeAreaBean getDealerTypeArea() {
                return dealerTypeArea;
            }

            public void setDealerTypeArea(DealerTypeAreaBean dealerTypeArea) {
                this.dealerTypeArea = dealerTypeArea;
            }

            public List<COMPANYTYPEBean> getCOMPANYTYPE() {
                return COMPANYTYPE;
            }

            public void setCOMPANYTYPE(List<COMPANYTYPEBean> COMPANYTYPE) {
                this.COMPANYTYPE = COMPANYTYPE;
            }

            public static class DealerTypeAreaBean {
                /**
                 * memo : 经销商的【机构类型】的区间2、3、4，分别为省、市、县。区间最大值5为零售商
                 * value : 2,3,4,5
                 */

                private String memo;
                private String value;

                public String getMemo() {
                    return memo;
                }

                public void setMemo(String memo) {
                    this.memo = memo;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
            }

            public static class COMPANYTYPEBean {
                /**
                 * value : 省级经销商
                 * key : 2
                 */

                private String value;
                private String key;

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }
            }
        }

        public static class AppIsHaveBILLBean {
            /**
             * value : 0
             * key : appIsHaveBILL
             */

            private String value;
            private String key;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }
        }
    }
}
