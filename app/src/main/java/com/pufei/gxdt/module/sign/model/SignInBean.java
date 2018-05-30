package com.pufei.gxdt.module.sign.model;

/**
 * Created by wangwenzhang on 2017/8/11.
 */

public class SignInBean {

    /**
     * code : 0
     * msg : success
     * result : {"times":"2","total_score":"2","score":"1"}
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
         * times : 2
         * total_score : 2
         * score : 1
         */

        private String times;
        private String total_score;
        private String score;

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

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }
    }
}
