package com.pufei.gxdt.module.login.model;


public class SendCodeBean {

    /**
     * status : 0
     * msg : 验证码发送成功
     * code : 0
     */

    private int status;
    private String msg;
    private int code;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
