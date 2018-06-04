package com.pufei.gxdt.module.news.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class NewsBean {

    /**
     * code : 0
     * msg : success
     * result : [{"id":"1","uid":"339839","type":"2","dateline":"2018-06-04 16:17","os":"ios","content":"【17712906965】引用了您的表情","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/3b9e1dc7.gif","images":{"orgintable":"amuse_images1","orginid":"28","id":"28","uid":"346979"},"orgin":"0"}]
     */

    private String code;
    private String msg;
    private List<ResultBean> result;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements MultiItemEntity {
        /**
         * id : 1
         * uid : 339839
         * type : 2
         * dateline : 2018-06-04 16:17
         * os : ios
         * content : 【17712906965】引用了您的表情
         * url : http://dtds.oss-cn-hangzhou.aliyuncs.com/3b9e1dc7.gif
         * images : {"orgintable":"amuse_images1","orginid":"28","id":"28","uid":"346979"}
         * orgin : 0
         */

        private String id;
        private String uid;
        private String type;
        private String dateline;
        private String os;
        private String content;
        private String url;
        private ImagesBean images;
        private String orgin;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDateline() {
            return dateline;
        }

        public void setDateline(String dateline) {
            this.dateline = dateline;
        }

        public String getOs() {
            return os;
        }

        public void setOs(String os) {
            this.os = os;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public ImagesBean getImages() {
            return images;
        }

        public void setImages(ImagesBean images) {
            this.images = images;
        }

        public String getOrgin() {
            return orgin;
        }

        public void setOrgin(String orgin) {
            this.orgin = orgin;
        }

        @Override
        public int getItemType() {
        int itemtype = -1;
        switch (getOrgin()) {
            case "0":
                itemtype = 0;
                break;
            case "1":
                itemtype = 1;
                break;
        }
        return itemtype;
        }

        public static class ImagesBean {
            /**
             * orgintable : amuse_images1
             * orginid : 28
             * id : 28
             * uid : 346979
             */

            private String orgintable;
            private String orginid;
            private String id;
            private String uid;

            public String getOrgintable() {
                return orgintable;
            }

            public void setOrgintable(String orgintable) {
                this.orgintable = orgintable;
            }

            public String getOrginid() {
                return orginid;
            }

            public void setOrginid(String orginid) {
                this.orginid = orginid;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }
        }
    }
}
//    @Override
//    public int getItemType() {
//        int itemtype = -1;
//        switch (getOrgin()) {
//            case "0":
//                itemtype = 0;
//                break;
//            case "1":
//                itemtype = 1;
//                break;
//        }
//        return itemtype;
//
//    }