package com.winsafe.jiangmenzhibao.entity;

import java.io.Serializable;

/**
 * 仓库
 * Created by shijie.yang on 2017/6/10.
 */

public class WarehouseDataBean implements Serializable {
//            "warehouseName":"默认仓库",
//            "warehouseID":"10000",
//            "customWHID":null,
//            "organID":"10536

    public String id;
    public String warehouseName;
    public String warehouseID;
    public String customWHID;
    public String organID;
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

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(String warehouseID) {
        this.warehouseID = warehouseID;
    }

    public String getCustomWHID() {
        return customWHID;
    }

    public void setCustomWHID(String customWHID) {
        this.customWHID = customWHID;
    }

    public String getOrganID() {
        return organID;
    }

    public void setOrganID(String organID) {
        this.organID = organID;
    }
}
