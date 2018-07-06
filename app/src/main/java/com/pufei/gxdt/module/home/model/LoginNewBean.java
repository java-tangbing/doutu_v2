package com.pufei.gxdt.module.home.model;

/**
 * Created by tb on 2018/6/1.
 */

public class LoginNewBean {


    /**
     * code : 0
     * msg : success
     * result : {"auth":"42dbEUNyvhMRB0Xo35O0TWegzToOGg2vd9sI30xw","uid":"339839","mobile":"","username":"aaaa","province":"江苏","city":"南京","gender":"男","header":"www.baidu.com","orgin":"qq","mind":"个性签名"}
     */

    private String code;
    private String msg;
    private ResultBean result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * auth : 42dbEUNyvhMRB0Xo35O0TWegzToOGg2vd9sI30xw
         * uid : 339839
         * mobile :
         * username : aaaa
         * province : 江苏
         * city : 南京
         * gender : 男
         * header : www.baidu.com
         * orgin : qq
         * mind : 个性签名
         */

        private String auth;
        private String uid;
        private String mobile;
        private String username;
        private String province;
        private String city;
        private String gender;
        private String header;
        private String orgin;
        private String mind;

        public String getAuth() {
            return auth;
        }

        public void setAuth(String auth) {
            this.auth = auth;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
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

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getOrgin() {
            return orgin;
        }

        public void setOrgin(String orgin) {
            this.orgin = orgin;
        }

        public String getMind() {
            return mind;
        }

        public void setMind(String mind) {
            this.mind = mind;
        }
    }
}
