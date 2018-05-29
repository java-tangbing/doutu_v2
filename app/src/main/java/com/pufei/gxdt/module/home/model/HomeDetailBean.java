package com.pufei.gxdt.module.home.model;

import java.util.List;

/**
 * Created by tb on 2018/5/28.
 */

public class HomeDetailBean {

    /**
     * code : 0
     * msg : success
     * result : [{"id":"187","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/1f61d786c5368b40e84020a450abd91c.gif","category_id":"21","dateline":"1497582436","view":"","hot":"","orgintable":"amuse_images1"},{"id":"186","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/ea78690de2de8dbd8e552c4094f21a41.gif","category_id":"21","dateline":"1497582436","view":"","hot":"","orgintable":"amuse_images1"},{"id":"185","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/7910249e3c76a57aa823d747be275308.gif","category_id":"21","dateline":"1497582436","view":"","hot":"","orgintable":"amuse_images1"},{"id":"184","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/554029a59ab84acbe9c5d43fa20d73af.gif","category_id":"21","dateline":"1497582436","view":"","hot":"","orgintable":"amuse_images1"},{"id":"183","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/1d59952ed154c49fd2e5e1a3a807b0b7.gif","category_id":"21","dateline":"1497582436","view":"","hot":"","orgintable":"amuse_images1"},{"id":"182","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/9c89be40e7d96476372cb532ddaf9e1e.gif","category_id":"21","dateline":"1497582436","view":"","hot":"","orgintable":"amuse_images1"},{"id":"181","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/a89e755cfd6339adf7c405b17ae59d14.gif","category_id":"21","dateline":"1497582436","view":"","hot":"","orgintable":"amuse_images1"},{"id":"180","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/09ae2e72728f2f75b0fe064ea48e4c7d.gif","category_id":"21","dateline":"1497582436","view":"","hot":"","orgintable":"amuse_images1"}]
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
         * id : 187
         * url : http://dtds.oss-cn-hangzhou.aliyuncs.com/1f61d786c5368b40e84020a450abd91c.gif
         * category_id : 21
         * dateline : 1497582436
         * view :
         * hot :
         * orgintable : amuse_images1
         */

        private String id;
        private String url;
        private String category_id;
        private String dateline;
        private String view;
        private String hot;
        private String orgintable;

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

        public String getView() {
            return view;
        }

        public void setView(String view) {
            this.view = view;
        }

        public String getHot() {
            return hot;
        }

        public void setHot(String hot) {
            this.hot = hot;
        }

        public String getOrgintable() {
            return orgintable;
        }

        public void setOrgintable(String orgintable) {
            this.orgintable = orgintable;
        }
    }
}
