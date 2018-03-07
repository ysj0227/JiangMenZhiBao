package com.winsafe.jiangmenzhibao.entity;

import java.util.List;

/**
 * Created by shijie.yang on 2017/5/23.
 */

public class CustomerBean {


    /**
     * returnCode : 0
     * returnMsg : 正确
     * returnData : {"companyinfo":[{"city":"110100","cityname":"","code":"34315a3d-d68c-43b2-8eb8-4c261a06be76","codes":[],"compantFullName":"x123456","companyNameEn":"","companytype":"3","companytypeName":"","contactperson":"","county":"110107","countyname":"","creationTime":{"date":5,"day":1,"hours":9,"minutes":50,"month":5,"seconds":46,"time":1496627446000,"timezoneOffset":-480,"year":117},"creator":null,"dataStatus":"1","emailaddress":"","encode":"201706042","faxnumber":"","isconfirmed":"","keyword":"","mobilenum":"","modificationTime":{"date":5,"day":1,"hours":10,"minutes":3,"month":5,"seconds":1,"time":1496628181000,"timezoneOffset":-480,"year":117},"modifier":null,"order":null,"ordering":false,"pager":null,"paging":false,"parentCompanyCode":"367ab7bf-ccba-4db2-bb8a-73d688eceb23","parentCompanyName":"","postalcode":"","printingPlantNum":"","province":"110000","provincename":"","registedaddress":"5454654","remark":"","ruleOrVisitUserCode":"","ruleUserCode":"","status":"1","telnum":"","visitUserCode":""},{"city":"120100","cityname":"","code":"6ce6577a-23e9-459f-b80c-588696bbe904","codes":[],"compantFullName":"课上","companyNameEn":"","companytype":"3","companytypeName":"","contactperson":"","county":"120101","countyname":"","creationTime":{"date":5,"day":1,"hours":9,"minutes":55,"month":5,"seconds":11,"time":1496627711000,"timezoneOffset":-480,"year":117},"creator":null,"dataStatus":"1","emailaddress":"","encode":"10002","faxnumber":"","isconfirmed":"","keyword":"","mobilenum":"","modificationTime":null,"modifier":null,"order":null,"ordering":false,"pager":null,"paging":false,"parentCompanyCode":"367ab7bf-ccba-4db2-bb8a-73d688eceb23","parentCompanyName":"","postalcode":"","printingPlantNum":"","province":"120000","provincename":"","registedaddress":"破婆婆","remark":"","ruleOrVisitUserCode":"","ruleUserCode":"","status":"","telnum":"18333333333","visitUserCode":""}]}
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
        private List<CompanyinfoBean> companyinfo;

        public List<CompanyinfoBean> getCompanyinfo() {
            return companyinfo;
        }

        public void setCompanyinfo(List<CompanyinfoBean> companyinfo) {
            this.companyinfo = companyinfo;
        }

        public static class CompanyinfoBean {
            /**
             * city : 110100
             * cityname :
             * code : 34315a3d-d68c-43b2-8eb8-4c261a06be76
             * codes : []
             * compantFullName : x123456
             * companyNameEn :
             * companytype : 3
             * companytypeName :
             * contactperson :
             * county : 110107
             * countyname :
             * creationTime : {"date":5,"day":1,"hours":9,"minutes":50,"month":5,"seconds":46,"time":1496627446000,"timezoneOffset":-480,"year":117}
             * creator : null
             * dataStatus : 1
             * emailaddress :
             * encode : 201706042
             * faxnumber :
             * isconfirmed :
             * keyword :
             * mobilenum :
             * modificationTime : {"date":5,"day":1,"hours":10,"minutes":3,"month":5,"seconds":1,"time":1496628181000,"timezoneOffset":-480,"year":117}
             * modifier : null
             * order : null
             * ordering : false
             * pager : null
             * paging : false
             * parentCompanyCode : 367ab7bf-ccba-4db2-bb8a-73d688eceb23
             * parentCompanyName :
             * postalcode :
             * printingPlantNum :
             * province : 110000
             * provincename :s
             * registedaddress : 5454654
             * remark :
             * ruleOrVisitUserCode :
             * ruleUserCode :
             * status : 1
             * telnum :
             * visitUserCode :
             */

            private String city;
            private String cityname;
            private String code;
            private String compantFullName;
            private String companyNameEn;
            private String companytype;
            private String companytypeName;
            private String contactperson;
            private String county;
            private String countyname;
            private CreationTimeBean creationTime;
            private Object creator;
            private String dataStatus;
            private String emailaddress;
            private String encode;
            private String faxnumber;
            private String isconfirmed;
            private String keyword;
            private String mobilenum;
            private ModificationTimeBean modificationTime;
            private Object modifier;
            private Object order;
            private boolean ordering;
            private Object pager;
            private boolean paging;
            private String parentCompanyCode;
            private String parentCompanyName;
            private String postalcode;
            private String printingPlantNum;
            private String province;
            private String provincename;
            private String registedaddress;
            private String remark;
            private String ruleOrVisitUserCode;
            private String ruleUserCode;
            private String status;
            private String telnum;
            private String visitUserCode;
            private List<?> codes;

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getCityname() {
                return cityname;
            }

            public void setCityname(String cityname) {
                this.cityname = cityname;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getCompantFullName() {
                return compantFullName;
            }

            public void setCompantFullName(String compantFullName) {
                this.compantFullName = compantFullName;
            }

            public String getCompanyNameEn() {
                return companyNameEn;
            }

            public void setCompanyNameEn(String companyNameEn) {
                this.companyNameEn = companyNameEn;
            }

            public String getCompanytype() {
                return companytype;
            }

            public void setCompanytype(String companytype) {
                this.companytype = companytype;
            }

            public String getCompanytypeName() {
                return companytypeName;
            }

            public void setCompanytypeName(String companytypeName) {
                this.companytypeName = companytypeName;
            }

            public String getContactperson() {
                return contactperson;
            }

            public void setContactperson(String contactperson) {
                this.contactperson = contactperson;
            }

            public String getCounty() {
                return county;
            }

            public void setCounty(String county) {
                this.county = county;
            }

            public String getCountyname() {
                return countyname;
            }

            public void setCountyname(String countyname) {
                this.countyname = countyname;
            }

            public CreationTimeBean getCreationTime() {
                return creationTime;
            }

            public void setCreationTime(CreationTimeBean creationTime) {
                this.creationTime = creationTime;
            }

            public Object getCreator() {
                return creator;
            }

            public void setCreator(Object creator) {
                this.creator = creator;
            }

            public String getDataStatus() {
                return dataStatus;
            }

            public void setDataStatus(String dataStatus) {
                this.dataStatus = dataStatus;
            }

            public String getEmailaddress() {
                return emailaddress;
            }

            public void setEmailaddress(String emailaddress) {
                this.emailaddress = emailaddress;
            }

            public String getEncode() {
                return encode;
            }

            public void setEncode(String encode) {
                this.encode = encode;
            }

            public String getFaxnumber() {
                return faxnumber;
            }

            public void setFaxnumber(String faxnumber) {
                this.faxnumber = faxnumber;
            }

            public String getIsconfirmed() {
                return isconfirmed;
            }

            public void setIsconfirmed(String isconfirmed) {
                this.isconfirmed = isconfirmed;
            }

            public String getKeyword() {
                return keyword;
            }

            public void setKeyword(String keyword) {
                this.keyword = keyword;
            }

            public String getMobilenum() {
                return mobilenum;
            }

            public void setMobilenum(String mobilenum) {
                this.mobilenum = mobilenum;
            }

            public ModificationTimeBean getModificationTime() {
                return modificationTime;
            }

            public void setModificationTime(ModificationTimeBean modificationTime) {
                this.modificationTime = modificationTime;
            }

            public Object getModifier() {
                return modifier;
            }

            public void setModifier(Object modifier) {
                this.modifier = modifier;
            }

            public Object getOrder() {
                return order;
            }

            public void setOrder(Object order) {
                this.order = order;
            }

            public boolean isOrdering() {
                return ordering;
            }

            public void setOrdering(boolean ordering) {
                this.ordering = ordering;
            }

            public Object getPager() {
                return pager;
            }

            public void setPager(Object pager) {
                this.pager = pager;
            }

            public boolean isPaging() {
                return paging;
            }

            public void setPaging(boolean paging) {
                this.paging = paging;
            }

            public String getParentCompanyCode() {
                return parentCompanyCode;
            }

            public void setParentCompanyCode(String parentCompanyCode) {
                this.parentCompanyCode = parentCompanyCode;
            }

            public String getParentCompanyName() {
                return parentCompanyName;
            }

            public void setParentCompanyName(String parentCompanyName) {
                this.parentCompanyName = parentCompanyName;
            }

            public String getPostalcode() {
                return postalcode;
            }

            public void setPostalcode(String postalcode) {
                this.postalcode = postalcode;
            }

            public String getPrintingPlantNum() {
                return printingPlantNum;
            }

            public void setPrintingPlantNum(String printingPlantNum) {
                this.printingPlantNum = printingPlantNum;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getProvincename() {
                return provincename;
            }

            public void setProvincename(String provincename) {
                this.provincename = provincename;
            }

            public String getRegistedaddress() {
                return registedaddress;
            }

            public void setRegistedaddress(String registedaddress) {
                this.registedaddress = registedaddress;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getRuleOrVisitUserCode() {
                return ruleOrVisitUserCode;
            }

            public void setRuleOrVisitUserCode(String ruleOrVisitUserCode) {
                this.ruleOrVisitUserCode = ruleOrVisitUserCode;
            }

            public String getRuleUserCode() {
                return ruleUserCode;
            }

            public void setRuleUserCode(String ruleUserCode) {
                this.ruleUserCode = ruleUserCode;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getTelnum() {
                return telnum;
            }

            public void setTelnum(String telnum) {
                this.telnum = telnum;
            }

            public String getVisitUserCode() {
                return visitUserCode;
            }

            public void setVisitUserCode(String visitUserCode) {
                this.visitUserCode = visitUserCode;
            }

            public List<?> getCodes() {
                return codes;
            }

            public void setCodes(List<?> codes) {
                this.codes = codes;
            }

            public static class CreationTimeBean {
                /**
                 * date : 5
                 * day : 1
                 * hours : 9
                 * minutes : 50
                 * month : 5
                 * seconds : 46
                 * time : 1496627446000
                 * timezoneOffset : -480
                 * year : 117
                 */

                private int date;
                private int day;
                private int hours;
                private int minutes;
                private int month;
                private int seconds;
                private long time;
                private int timezoneOffset;
                private int year;

                public int getDate() {
                    return date;
                }

                public void setDate(int date) {
                    this.date = date;
                }

                public int getDay() {
                    return day;
                }

                public void setDay(int day) {
                    this.day = day;
                }

                public int getHours() {
                    return hours;
                }

                public void setHours(int hours) {
                    this.hours = hours;
                }

                public int getMinutes() {
                    return minutes;
                }

                public void setMinutes(int minutes) {
                    this.minutes = minutes;
                }

                public int getMonth() {
                    return month;
                }

                public void setMonth(int month) {
                    this.month = month;
                }

                public int getSeconds() {
                    return seconds;
                }

                public void setSeconds(int seconds) {
                    this.seconds = seconds;
                }

                public long getTime() {
                    return time;
                }

                public void setTime(long time) {
                    this.time = time;
                }

                public int getTimezoneOffset() {
                    return timezoneOffset;
                }

                public void setTimezoneOffset(int timezoneOffset) {
                    this.timezoneOffset = timezoneOffset;
                }

                public int getYear() {
                    return year;
                }

                public void setYear(int year) {
                    this.year = year;
                }
            }

            public static class ModificationTimeBean {
                /**
                 * date : 5
                 * day : 1
                 * hours : 10
                 * minutes : 3
                 * month : 5
                 * seconds : 1
                 * time : 1496628181000
                 * timezoneOffset : -480
                 * year : 117
                 */

                private int date;
                private int day;
                private int hours;
                private int minutes;
                private int month;
                private int seconds;
                private long time;
                private int timezoneOffset;
                private int year;

                public int getDate() {
                    return date;
                }

                public void setDate(int date) {
                    this.date = date;
                }

                public int getDay() {
                    return day;
                }

                public void setDay(int day) {
                    this.day = day;
                }

                public int getHours() {
                    return hours;
                }

                public void setHours(int hours) {
                    this.hours = hours;
                }

                public int getMinutes() {
                    return minutes;
                }

                public void setMinutes(int minutes) {
                    this.minutes = minutes;
                }

                public int getMonth() {
                    return month;
                }

                public void setMonth(int month) {
                    this.month = month;
                }

                public int getSeconds() {
                    return seconds;
                }

                public void setSeconds(int seconds) {
                    this.seconds = seconds;
                }

                public long getTime() {
                    return time;
                }

                public void setTime(long time) {
                    this.time = time;
                }

                public int getTimezoneOffset() {
                    return timezoneOffset;
                }

                public void setTimezoneOffset(int timezoneOffset) {
                    this.timezoneOffset = timezoneOffset;
                }

                public int getYear() {
                    return year;
                }

                public void setYear(int year) {
                    this.year = year;
                }
            }
        }
    }
}
