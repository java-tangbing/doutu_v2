package com.pufei.gxdt.module.news.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class NewsBean {
    /**
     * code : 0
     * msg : success
     * result : [{"id":"7","uid":"339839","type":"2","dateline":"2018-05-29 10:59","os":"android","content":"ceshi454","url":"","orgin":"0"},{"id":"2","uid":"339839","type":"2","dateline":"2018-05-29 10:59","os":"ios","content":"cehsi1","url":"","orgin":"0"},{"id":"5","uid":"339839","type":"2","dateline":"2018-05-29 10:59","os":"ios","content":"ceshi45","url":"","orgin":"0"}]
     */

    private String code;
    private String msg;
    private List<ResultBean> result;
//    private int itemType;//1101 or 1102

//    @Override
//    public int getItemType() {
//        return itemType;
//    }
//
//    public void setItemType(int itemType) {
//        this.itemType = itemType;
//    }

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
         * id : 7
         * uid : 339839
         * type : 2
         * dateline : 2018-05-29 10:59
         * os : android
         * content : ceshi454
         * url :
         * orgin : 0
         */

        private String id;
        private String uid;
        private String type;
        private String dateline;
        private String os;
        private String content;
        private String url;
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
