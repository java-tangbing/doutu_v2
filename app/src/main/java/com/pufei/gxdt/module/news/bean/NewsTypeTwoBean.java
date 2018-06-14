package com.pufei.gxdt.module.news.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class NewsTypeTwoBean {


    /**
     * code : 0
     * msg : success
     * result : [{"id":"92","uid":"350073","type":"3","dateline":"2018-06-13 15:52","os":"","content":"急急急","url":"","images":"","orgin":"1"},{"id":"151","uid":"350073","type":"3","dateline":"2018-06-14 14:53","os":"","content":"一定有人","url":"","images":"","orgin":"1"},{"id":"159","uid":"350073","type":"3","dateline":"2018-06-14 16:23","os":"","content":"哈哈哈","url":"","images":"","orgin":"1"},{"id":"164","uid":"350073","type":"3","dateline":"2018-06-14 18:01","os":"","content":"姐姐那就","url":"","images":"","orgin":"1"}]
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
         * id : 92
         * uid : 350073
         * type : 3
         * dateline : 2018-06-13 15:52
         * os :
         * content : 急急急
         * url :
         * images :
         * orgin : 1
         */

        private String id;
        private String uid;
        private String type;
        private String dateline;
        private String os;
        private String content;
        private String url;
        private String images;
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

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
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
    }
}
