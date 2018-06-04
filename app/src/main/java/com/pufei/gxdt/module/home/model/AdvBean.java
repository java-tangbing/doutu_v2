package com.pufei.gxdt.module.home.model;

/**
 * Created by tb on 2018/6/2.
 */

public class AdvBean {

    /**
     * code : 0
     * msg : success
     * result : {"data":{"id":"3","link":"www.sina.com","image":"http://dtds.oss-cn-hangzhou.aliyuncs.com/1527500888Ieahst.png","dateline":"1527502629","type":"4","position":"1","desc":"ddd","title":"haha"},"type":"4"}
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
         * data : {"id":"3","link":"www.sina.com","image":"http://dtds.oss-cn-hangzhou.aliyuncs.com/1527500888Ieahst.png","dateline":"1527502629","type":"4","position":"1","desc":"ddd","title":"haha"}
         * type : 4
         */

        private DataBean data;
        private String type;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public static class DataBean {
            /**
             * id : 3
             * link : www.sina.com
             * image : http://dtds.oss-cn-hangzhou.aliyuncs.com/1527500888Ieahst.png
             * dateline : 1527502629
             * type : 4
             * position : 1
             * desc : ddd
             * title : haha
             */

            private String id;
            private String link;
            private String image;
            private String dateline;
            private String type;
            private String position;
            private String desc;
            private String title;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getDateline() {
                return dateline;
            }

            public void setDateline(String dateline) {
                this.dateline = dateline;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
