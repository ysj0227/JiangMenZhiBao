package com.winsafe.jiangmenzhibao.entity;

import java.io.Serializable;

/**
 * 机构数据
 * Created by shijie.yang on 2017/6/10.
 */

public class OrganDataBean implements Serializable {

//            "customOrgID":null,
//            "organID":"10536",""
//            "organName":"住商农资"
    public String id;
    public String customOrgID;
    public String organID;
    public String organName;
    public String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomOrgID() {
        return customOrgID;
    }

    public void setCustomOrgID(String customOrgID) {
        this.customOrgID = customOrgID;
    }

    public String getOrganID() {
        return organID;
    }

    public void setOrganID(String organID) {
        this.organID = organID;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }


}
