package com.pufei.gxdt.module.discover.bean;

import java.io.Serializable;

public class IsSaveImgBean implements Serializable {
    //    private String beanType;//DiscoverListBean
    private int index;
    private String isSaveImg;

//    public String getBeanType() {
//        return beanType;
//    }
//
//    public void setBeanType(String beanType) {
//        this.beanType = beanType;
//    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getIsSaveImg() {
        return isSaveImg;
    }

    public void setIsSaveImg(String isSaveImg) {
        this.isSaveImg = isSaveImg;
    }
}
