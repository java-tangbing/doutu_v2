package com.pufei.gxdt.module.home.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tb on 2018/5/24.
 */

public class PictureResultBean{

    /**
     * code : 0
     * msg : success
     * result : [{"id":"5392","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/052231000259621.gif","category_id":"1","dateline":"1527717608","view":"","hot":"","orgintable":"amuse_images3","orginid":"","isSaveImg":"0"},{"id":"4675","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/092533000217985.png","category_id":"1","dateline":"1527652808","view":"","hot":"","orgintable":"amuse_images3","orginid":"","isSaveImg":"0"},{"id":"4674","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/181002000259621.gif","category_id":"1","dateline":"1527652808","view":"","hot":"","orgintable":"amuse_images3","orginid":"","isSaveImg":"0"},{"id":"4673","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/181706000262057.gif","category_id":"1","dateline":"1527652807","view":"","hot":"","orgintable":"amuse_images3","orginid":"","isSaveImg":"0"},{"id":"4671","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/180753000259621.gif","category_id":"1","dateline":"1527652807","view":"","hot":"","orgintable":"amuse_images3","orginid":"","isSaveImg":"0"},{"id":"4670","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/000747000262057.gif","category_id":"1","dateline":"1527652806","view":"","hot":"","orgintable":"amuse_images3","orginid":"","isSaveImg":"0"},{"id":"4669","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/093021000217985.png","category_id":"1","dateline":"1527652806","view":"","hot":"","orgintable":"amuse_images3","orginid":"","isSaveImg":"0"}]
     * isSave : 0
     */

    private String code;
    private String msg;
    private String isSave;
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

    public String getIsSave() {
        return isSave;
    }

    public void setIsSave(String isSave) {
        this.isSave = isSave;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        /**
         * id : 5392
         * url : http://dtds.oss-cn-hangzhou.aliyuncs.com/052231000259621.gif
         * category_id : 1
         * dateline : 1527717608
         * view :
         * hot :
         * orgintable : amuse_images3
         * orginid :
         * isSaveImg : 0
         */

        private String id;
        private String url;
        private String category_id;
        private String dateline;
        private String view;
        private String hot;
        private String orgintable;
        private String orginid;
        private String isSaveImg;

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

        public String getOrginid() {
            return orginid;
        }

        public void setOrginid(String orginid) {
            this.orginid = orginid;
        }

        public String getIsSaveImg() {
            return isSaveImg;
        }

        public void setIsSaveImg(String isSaveImg) {
            this.isSaveImg = isSaveImg;
        }
    }
}
