package com.pufei.gxdt.module.update.model;

/**
 *
 */
public class UpdateBean {

    /**
     * code : 0
     * msg : success
     * result : {"version":"3.5.0","link":"http://dtds.oss-cn-hangzhou.aliyuncs.com/apk/app-gengxin-release_342_jiagu_sign.apk","des":"新增\r\n·改图功能，\r\n·制图增加图片、文字、和画笔的功能\r\n·发现表情，用户制作的表情可发布在发现中。\r\n·系统消息\r\n·助手消息\r\n·意见反馈\r\n优化\r\n·更加细致的表情分类\r\n·UI界面的优化\r\n·搞笑斗图大师会持续更新应用，每次更新都包含性能和稳定性的提升，让应用变得更完美","version_code":"20","updateOpen":"1","isIgnore":"0","isForce":"0"}
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
         * version : 3.5.0
         * link : http://dtds.oss-cn-hangzhou.aliyuncs.com/apk/app-gengxin-release_342_jiagu_sign.apk
         * des : 新增
         ·改图功能，
         ·制图增加图片、文字、和画笔的功能
         ·发现表情，用户制作的表情可发布在发现中。
         ·系统消息
         ·助手消息
         ·意见反馈
         优化
         ·更加细致的表情分类
         ·UI界面的优化
         ·搞笑斗图大师会持续更新应用，每次更新都包含性能和稳定性的提升，让应用变得更完美
         * version_code : 20
         * updateOpen : 1
         * isIgnore : 0
         * isForce : 0
         */

        private String version;
        private String link;
        private String des;
        private String version_code;
        private String updateOpen;
        private String isIgnore;
        private String isForce;
        private String channel;

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

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

        public String getUpdateOpen() {
            return updateOpen;
        }

        public void setUpdateOpen(String updateOpen) {
            this.updateOpen = updateOpen;
        }

        public String getIsIgnore() {
            return isIgnore;
        }

        public void setIsIgnore(String isIgnore) {
            this.isIgnore = isIgnore;
        }

        public String getIsForce() {
            return isForce;
        }

        public void setIsForce(String isForce) {
            this.isForce = isForce;
        }
    }
}
