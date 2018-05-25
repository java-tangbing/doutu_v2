package com.pufei.gxdt.module.home.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tb on 2018/5/24.
 */

public class PictureResultBean {
    /**
     * code : 0
     * msg : success
     * result : [{"id":"150251","url":"http://ojco34ciy.bkt.clouddn.com/e342d52af779fefe1da8df9c30b25e1d.gif","category_id":"25","dateline":"1490597521"},{"id":"150250","url":"http://ojco34ciy.bkt.clouddn.com/4728622dbb451759270ab87a9b279a48.gif","category_id":"25","dateline":"1490597521"},{"id":"150224","url":"http://ojco34ciy.bkt.clouddn.com/bca99695dff1863f4418a0e4cbf2a27d.gif","category_id":"25","dateline":"1490597521"}]
     */

    private String code;
    private String msg;
    /**
     * id : 150251
     * url : http://ojco34ciy.bkt.clouddn.com/e342d52af779fefe1da8df9c30b25e1d.gif
     * category_id : 25
     * dateline : 1490597521
     */

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

    public static class ResultBean implements Serializable {
        private String id;
        private String url;
        private String category_id;
        private String dateline;
        private int  type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getDateline() {
            return dateline;
        }

        public void setDateline(String dateline) {
            this.dateline = dateline;
        }
    }
}
