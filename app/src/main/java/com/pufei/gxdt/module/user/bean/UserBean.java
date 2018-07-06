package com.pufei.gxdt.module.user.bean;

/**
 * 创建者： wangwenzhang 时间： 2018/3/14.
 */

public class UserBean {
    private String name;
    private String head;
    private String gender;
    private String address;
    private String auth = "";
    private String phone;
    private String mind;
    private String uid;
    private String wechat;
    private String qq;
    private String state = "";

    public UserBean(String name, String head, String gender, String address, String auth, String phone,String uid) {
        this.name = name;
        this.head = head;
        this.gender = gender;
        this.address = address;
        this.auth = auth;
        this.phone = phone;
        this.uid = uid;
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
                ", mind='" + mind + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getMind() {
            return mind;
        }

    public void setMind(String mind) {
            this.mind = mind;
        }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}