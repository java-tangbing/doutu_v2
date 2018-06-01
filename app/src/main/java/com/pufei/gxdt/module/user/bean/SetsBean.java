package com.pufei.gxdt.module.user.bean;

public class SetsBean {


    /**
     * code : 0
     * msg : success
     * result : {"mobile":"","wechat":"","qq":"5F61FE51E22E84987FEC7A288d7C2D7"}
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
         * mobile :
         * wechat :
         * qq : 5F61FE51E22E84987FEC7A288d7C2D7
         */

        private String mobile;
        private String wechat;
        private String qq;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
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
    }
}
