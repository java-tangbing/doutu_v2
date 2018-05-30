package com.pufei.gxdt.module.home.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tb on 2018/5/29.
 */

public class HomeTypeBean {

    /**
     * code : 0
     * msg : success
     * result : [{"id":"715","category_name":"污妹子","dateline":"1527478219","images":""},{"id":"718","category_name":"老司机开车了，滴","dateline":"1527478213","images":""},{"id":"752","category_name":"周星驰","dateline":"1527478191","images":""},{"id":"10702","category_name":"exo","dateline":"1527478186","images":""},{"id":"8044","category_name":"阿鲁","dateline":"1527478173","images":""},{"id":"7106","category_name":"時崎狂三","dateline":"1527478169","images":""},{"id":"1713","category_name":"可爱猫","dateline":"1527478165","images":""}]
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

    public static class ResultBean implements Serializable{
        /**
         * id : 715
         * category_name : 污妹子
         * dateline : 1527478219
         * images :
         */

        private String id;
        private String category_name;
        private String dateline;
        private String images;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public String getDateline() {
            return dateline;
        }

        public void setDateline(String dateline) {
            this.dateline = dateline;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }
    }
}
