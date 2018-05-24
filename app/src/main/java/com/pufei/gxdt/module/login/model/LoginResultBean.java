package com.pufei.gxdt.module.login.model;

public class LoginResultBean {

    /**
     * code : 0
     * msg : 登录成功！
     * userinfo : {"mobile":"17712906965","nickname":null,"avatar":null,"sex":"0","name":null,"number":null,"idtype":null,"province":null,"city":null,"brief":null,"auth":"79f6Q3xlwrOpHcPpza/sPYZ32SOP3WvJ+lxG11DZeVrfnb64pfreAGE3in0BtudC7CUsYmwA9YRrp4A1/ZyO50d50koMdAopLFIMKen+95po9RYynQ","pwd_set":false}
     */

    private int code;
    private String msg;
    private UserinfoBean userinfo;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UserinfoBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserinfoBean userinfo) {
        this.userinfo = userinfo;
    }

    public static class UserinfoBean {
        /**
         * mobile : 17712906965
         * nickname : null
         * avatar : null
         * sex : 0
         * name : null
         * number : null
         * idtype : null
         * province : null
         * city : null
         * brief : null
         * auth : 79f6Q3xlwrOpHcPpza/sPYZ32SOP3WvJ+lxG11DZeVrfnb64pfreAGE3in0BtudC7CUsYmwA9YRrp4A1/ZyO50d50koMdAopLFIMKen+95po9RYynQ
         * pwd_set : false
         */

        private String mobile;
        private String nickname;
        private String avatar;
        private int sex;
        private String name;
        private String number;
        private String idtype;
        private String province;
        private String city;
        private String brief;
        private String auth;
        private boolean pwd_set;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getIdtype() {
            return idtype;
        }

        public void setIdtype(String idtype) {
            this.idtype = idtype;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getBrief() {
            return brief;
        }

        public void setBrief(String brief) {
            this.brief = brief;
        }

        public String getAuth() {
            return auth;
        }

        public void setAuth(String auth) {
            this.auth = auth;
        }

        public boolean isPwd_set() {
            return pwd_set;
        }

        public void setPwd_set(boolean pwd_set) {
            this.pwd_set = pwd_set;
        }
    }
}
