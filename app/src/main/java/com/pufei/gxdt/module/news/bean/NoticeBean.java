package com.pufei.gxdt.module.news.bean;

import java.util.List;

public class NoticeBean {

    /**
     * code : 0
     * msg : success
     * result : [{"id":"4","uid":"339839","type":"1","dateline":"2018-05-29 10:59","os":"ios","content":"ceshi4","url":"","orgin":"0","title":"系统消息"},{"id":"7","uid":"339839","type":"2","dateline":"2018-05-29 10:59","os":"android","content":"ceshi454","url":"","orgin":"0","title":"斗图小助手"},{"id":"8","uid":"339839","type":"3","dateline":"2018-05-29 17:35","os":"","content":"技术测试意见","url":"","orgin":"0","title":"意见反馈"}]
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

    public static class ResultBean {
        /**
         * id : 4
         * uid : 339839
         * type : 1
         * dateline : 2018-05-29 10:59
         * os : ios
         * content : ceshi4
         * url :
         * orgin : 0
         * title : 系统消息
         */

        private String id;
        private String uid;
        private String type;
        private String dateline;
        private String os;
        private String content;
        private String url;
        private String orgin;
        private String title;

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
