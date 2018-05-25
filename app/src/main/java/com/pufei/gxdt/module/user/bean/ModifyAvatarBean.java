package com.pufei.gxdt.module.user.bean;

public class ModifyAvatarBean {

    /**
     * code : 0
     * msg :
     * data : {"avatar":"http://cnaso.oss-cn-hangzhou.aliyuncs.com/img/static/app/avatar/1523587667439.jpg"}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * avatar : http://cnaso.oss-cn-hangzhou.aliyuncs.com/img/static/app/avatar/1523587667439.jpg
         */

        private String avatar;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
