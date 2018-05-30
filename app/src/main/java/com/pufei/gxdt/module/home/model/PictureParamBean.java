package com.pufei.gxdt.module.home.model;

import java.io.Serializable;

/**
 * Created by tb on 2018/5/29.
 */

public class PictureParamBean implements Serializable{
    private String id;
    private String orginid;
    private String orgintable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrginid() {
        return orginid;
    }

    public void setOrginid(String orginid) {
        this.orginid = orginid;
    }

    public String getOrgintable() {
        return orgintable;
    }

    public void setOrgintable(String orgintable) {
        this.orgintable = orgintable;
    }
}
