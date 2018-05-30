package com.pufei.gxdt.module.sign.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wangwenzhang on 2017/8/11.
 */

public class GetScoreBean {

    /**
     * code : 0
     * msg : success
     * result : {"times":"10","data-str":["1502416361","1502416374","1502416383","1502416386","1502416389","1502416393","1502416397","1502416399","1502416402","1502416540"],"total_score":"49"}
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
         * times : 10
         * data-str : ["1502416361","1502416374","1502416383","1502416386","1502416389","1502416393","1502416397","1502416399","1502416402","1502416540"]
         * total_score : 49
         */

        private String times;
        private String total_score;
        @SerializedName("data-str")
        private List<String> datastr;

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public String getTotal_score() {
            return total_score;
        }

        public void setTotal_score(String total_score) {
            this.total_score = total_score;
        }

        public List<String> getDatastr() {
            return datastr;
        }

        public void setDatastr(List<String> datastr) {
            this.datastr = datastr;
        }
    }
}
