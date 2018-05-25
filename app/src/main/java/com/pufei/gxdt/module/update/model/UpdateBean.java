package com.pufei.gxdt.module.update.model;

/**
 *
 */
public class UpdateBean {

    /**
     * code : 0
     * msg : success
     * result : {"version":"3.2.1","link":"http://erp.xianwan.com/app-gengxin-release_321_jiagu_sign.apk","des":"修复BUG，优化运行","version_code":"7"}
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
         * version : 3.2.1
         * link : http://erp.xianwan.com/app-gengxin-release_321_jiagu_sign.apk
         * des : 修复BUG，优化运行
         * version_code : 7
         */

        private String version;
        private String link;
        private String des;
        private String version_code;
        private boolean update;
        private boolean force;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public String getVersion_code() {
            return version_code;
        }

        public void setVersion_code(String version_code) {
            this.version_code = version_code;
        }

        public boolean isUpdate() {
            return update;
        }

        public void setUpdate(boolean update) {
            this.update = update;
        }

        public boolean isForce() {
            return force;
        }

        public void setForce(boolean force) {
            this.force = force;
        }
    }
}
