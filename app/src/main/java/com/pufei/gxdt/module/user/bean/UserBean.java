package com.pufei.gxdt.module.user.bean;

/**
 * 创建者： wangwenzhang 时间： 2018/3/14.
 */

public class UserBean {
    private String name;
    private String alipay;
    private String realname;
    private String head;
    private String gender;
    private String address;
    private String auth;
    private String phone;
    private boolean pwd;

    public UserBean(String name,String alipay,String realname, String head, String gender, String address, String auth, String phone, boolean pwd) {
        this.name = name;
        this.alipay = alipay;
        this.realname = realname;
        this.head = head;
        this.gender = gender;
        this.address = address;
        this.auth = auth;
        this.phone = phone;
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "name='" + name + '\'' +
                ", head='" + head + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", auth='" + auth + '\'' +
                ", phone='" + phone + '\'' +
                ", pwd=" + pwd +
                '}';
    }

    public boolean isPwd() {
        return pwd;
    }

    public void setPwd(boolean pwd) {
        this.pwd = pwd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}

