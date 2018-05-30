package com.pufei.gxdt.module.sign.model;

import java.util.List;

/**
 * Created by wangwenzhang on 2017/8/16.
 */

public class CrunchiesBean {

    /**
     * code : 0
     * msg : success
     * result : [{"uid":"100001","times":"432","total_score":"3997","dateline":"1502759584","header":"http://erp.xianwan.com/others/default.jpg","username":"好名字都让猪起了"},{"uid":"100000","times":"201","total_score":"2001","dateline":"1502848536","header":"http://q.qlogo.cn/qqapp/1105886594/5F61FE51E22E84987FEC7A2887D7C2D7/100","username":"不知今夕是何年"},{"uid":"100005","times":"187","total_score":"1974","dateline":"1502763253","header":"http://erp.xianwan.com/others/default.jpg","username":"未知用户"},{"uid":"100026","times":"136","total_score":"1951","dateline":"1502763285","header":"http://erp.xianwan.com/others/default.jpg","username":"未知用户"},{"uid":"100051","times":"295","total_score":"1951","dateline":"1502761381","header":"http://erp.xianwan.com/others/default.jpg","username":"未知用户"},{"uid":"100047","times":"778","total_score":"1912","dateline":"1502763114","header":"http://erp.xianwan.com/others/default.jpg","username":"未知用户"},{"uid":"100027","times":"432","total_score":"1870","dateline":"1502762740","header":"http://erp.xianwan.com/others/default.jpg","username":"未知用户"},{"uid":"100052","times":"985","total_score":"1775","dateline":"1502762882","header":"http://erp.xianwan.com/others/default.jpg","username":"未知用户"},{"uid":"100039","times":"473","total_score":"1730","dateline":"1502764140","header":"http://erp.xianwan.com/others/default.jpg","username":"未知用户"},{"uid":"100045","times":"114","total_score":"1622","dateline":"1502762100","header":"http://erp.xianwan.com/others/default.jpg","username":"未知用户"},{"uid":"100018","times":"345","total_score":"1619","dateline":"1502763835","header":"http://erp.xianwan.com/others/default.jpg","username":"未知用户"},{"uid":"100022","times":"324","total_score":"1599","dateline":"1502760895","header":"http://erp.xianwan.com/others/default.jpg","username":"未知用户"},{"uid":"100004","times":"472","total_score":"1571","dateline":"1502763275","header":"http://erp.xianwan.com/others/default.jpg","username":"未知用户"},{"uid":"100050","times":"611","total_score":"1529","dateline":"1502761653","header":"http://erp.xianwan.com/others/default.jpg","username":"未知用户"},{"uid":"100009","times":"780","total_score":"1514","dateline":"1502761991","header":"http://erp.xianwan.com/others/default.jpg","username":"未知用户"},{"uid":"100024","times":"250","total_score":"1484","dateline":"1502762137","header":"http://erp.xianwan.com/others/default.jpg","username":"未知用户"},{"uid":"100025","times":"200","total_score":"1445","dateline":"1502763445","header":"http://erp.xianwan.com/others/default.jpg","username":"未知用户"},{"uid":"100010","times":"743","total_score":"1423","dateline":"1502760866","header":"http://erp.xianwan.com/others/default.jpg","username":"未知用户"},{"uid":"100036","times":"490","total_score":"1407","dateline":"1502761321","header":"http://erp.xianwan.com/others/default.jpg","username":"未知用户"},{"uid":"100007","times":"199","total_score":"1349","dateline":"1502763203","header":"http://erp.xianwan.com/others/default.jpg","username":"未知用户"},{"uid":"100035","times":"266","total_score":"1335","dateline":"1502761225","header":"http://erp.xianwan.com/others/default.jpg","username":"未知用户"},{"uid":"100021","times":"565","total_score":"1334","dateline":"1502762191","header":"http://erp.xianwan.com/others/default.jpg","username":"未知用户"},{"uid":"100015","times":"981","total_score":"1328","dateline":"1502761062","header":"http://erp.xianwan.com/others/default.jpg","username":"未知用户"}]
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
         * uid : 100001
         * times : 432
         * total_score : 3997
         * dateline : 1502759584
         * header : http://erp.xianwan.com/others/default.jpg
         * username : 好名字都让猪起了
         */

        private String uid;
        private String times;
        private String total_score;
        private String dateline;
        private String header;
        private String username;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

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

        public String getDateline() {
            return dateline;
        }

        public void setDateline(String dateline) {
            this.dateline = dateline;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
