package com.pufei.gxdt.module.maker.bean;

import java.util.List;

public class MaterialBean {

    /**
     * code : 0
     * msg : success
     * result : [{"id":"1715","dateline":"1527890403","title":"","image":"http://dtds.oss-cn-hangzhou.aliyuncs.com/476572cda7a6098ffae9b62320f213b6.gif","type":"1"},{"id":"1713","dateline":"1527890402","title":"","image":"http://dtds.oss-cn-hangzhou.aliyuncs.com/c6aad9d03269424477c7407548bc3acd.gif","type":"1"},{"id":"1714","dateline":"1527890402","title":"","image":"http://dtds.oss-cn-hangzhou.aliyuncs.com/6c630ce91bcf98c1a75ee2b26cec3908.gif","type":"1"},{"id":"1128","dateline":"1527782408","title":"","image":"http://dtds.oss-cn-hangzhou.aliyuncs.com/0f3ea9a2f3916214127b7ccb0045ff75.gif","type":"1"},{"id":"1090","dateline":"1527782407","title":"","image":"http://dtds.oss-cn-hangzhou.aliyuncs.com/0dcbb1df35902043d85791a5874fefed.gif","type":"1"},{"id":"1127","dateline":"1527782407","title":"","image":"http://dtds.oss-cn-hangzhou.aliyuncs.com/3787cff13fd638d3c69016bc94de24f7.gif","type":"1"},{"id":"1085","dateline":"1527782406","title":"","image":"http://dtds.oss-cn-hangzhou.aliyuncs.com/0f8503035db174c88d11aa3f25fd5a54.gif","type":"1"},{"id":"1086","dateline":"1527782406","title":"","image":"http://dtds.oss-cn-hangzhou.aliyuncs.com/9ccd98fb12400a91b365008de614d9ec.gif","type":"1"},{"id":"1087","dateline":"1527782406","title":"","image":"http://dtds.oss-cn-hangzhou.aliyuncs.com/10d05408121bbaffa8be196bfe1f4ca9.gif","type":"1"},{"id":"1088","dateline":"1527782406","title":"","image":"http://dtds.oss-cn-hangzhou.aliyuncs.com/3532b7752d2f3365be247e42998f58b6.gif","type":"1"},{"id":"1089","dateline":"1527782406","title":"","image":"http://dtds.oss-cn-hangzhou.aliyuncs.com/6cec9dc2929c9ffea099bd6e9ae96bc8.gif","type":"1"},{"id":"1048","dateline":"1527782405","title":"","image":"http://dtds.oss-cn-hangzhou.aliyuncs.com/abb034af0ebb44a85b598736b1d4ff6b.gif","type":"1"},{"id":"1049","dateline":"1527782405","title":"","image":"http://dtds.oss-cn-hangzhou.aliyuncs.com/779f8f4d5877ae5eccb10ee3c83a5b1f.gif","type":"1"},{"id":"1050","dateline":"1527782405","title":"","image":"http://dtds.oss-cn-hangzhou.aliyuncs.com/020b77b50627866938dbcf794e594567.gif","type":"1"},{"id":"1051","dateline":"1527782405","title":"","image":"http://dtds.oss-cn-hangzhou.aliyuncs.com/d0176bedd3c79728fa44aa71b4b9fdfe.gif","type":"1"},{"id":"1044","dateline":"1527782404","title":"","image":"http://dtds.oss-cn-hangzhou.aliyuncs.com/2bac8c71f1f4caf8e47fffd5e219d187.gif","type":"1"},{"id":"1045","dateline":"1527782404","title":"","image":"http://dtds.oss-cn-hangzhou.aliyuncs.com/6c771190492c367e0f0b10c41c4086b4.gif","type":"1"},{"id":"1046","dateline":"1527782404","title":"","image":"http://dtds.oss-cn-hangzhou.aliyuncs.com/4d1373b1e0d70f171090002b441b536f.gif","type":"1"},{"id":"1047","dateline":"1527782404","title":"","image":"http://dtds.oss-cn-hangzhou.aliyuncs.com/5f2ad2cd2112a4615b6531ffe5a53e42.gif","type":"1"},{"id":"1043","dateline":"1527782403","title":"","image":"http://dtds.oss-cn-hangzhou.aliyuncs.com/03dbae0b61d791c7df7b2bb4e79f565c.gif","type":"1"}]
     */

    private int code;
    private String msg;
    private List<ResultBean> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
         * id : 1715
         * dateline : 1527890403
         * title :
         * image : http://dtds.oss-cn-hangzhou.aliyuncs.com/476572cda7a6098ffae9b62320f213b6.gif
         * type : 1
         */

        private String id;
        private String dateline;
        private String title;
        private String image;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDateline() {
            return dateline;
        }

        public void setDateline(String dateline) {
            this.dateline = dateline;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
