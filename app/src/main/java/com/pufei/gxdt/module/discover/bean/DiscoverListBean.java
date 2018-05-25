package com.pufei.gxdt.module.discover.bean;

import java.util.List;

public class DiscoverListBean {

    /**
     * code : 0
     * msg : success
     * result : [{"id":"4","uid":"339839","hot":"","orginid":"111","orgintable":"amuse_images","height":"200","width":"300","title":"标题","dateline":"1527215999","background":"","user":{"header":"http://dtds.oss-cn-hangzhou.aliyuncs.com/f8e8334a.jpg","username":"abdcddd","uid":"2323"}}]
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
         * hot :
         * orginid : 111
         * orgintable : amuse_images
         * height : 200
         * width : 300
         * title : 标题
         * dateline : 1527215999
         * background :
         * user : {"header":"http://dtds.oss-cn-hangzhou.aliyuncs.com/f8e8334a.jpg","username":"abdcddd","uid":"2323"}
         */

        private String id;
        private String uid;
        private String hot;
        private String orginid;
        private String orgintable;
        private String height;
        private String width;
        private String title;
        private String dateline;
        private String background;
        private UserBean user;

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

        public String getHot() {
            return hot;
        }

        public void setHot(String hot) {
            this.hot = hot;
        }

        public String getOrginid() {
            return orginid;
        }

        public void setOrginid(String orginid) {
            this.orginid = orginid;
        }

        public String getOrgintable() {
            return orgintable;
        }

        public void setOrgintable(String orgintable) {
            this.orgintable = orgintable;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDateline() {
            return dateline;
        }

        public void setDateline(String dateline) {
            this.dateline = dateline;
        }

        public String getBackground() {
            return background;
        }

        public void setBackground(String background) {
            this.background = background;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * header : http://dtds.oss-cn-hangzhou.aliyuncs.com/f8e8334a.jpg
             * username : abdcddd
             * uid : 2323
             */

            private String header;
            private String username;
            private String uid;

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

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }
        }
    }
}
