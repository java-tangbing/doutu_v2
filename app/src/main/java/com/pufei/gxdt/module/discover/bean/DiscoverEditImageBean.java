package com.pufei.gxdt.module.discover.bean;

import java.util.List;

public class DiscoverEditImageBean {


    /**
     * code : 0
     * msg : success
     * result : {"orgin_url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/2cf01891759faaaa0dfc19846bdce8a4.jpeg","data":[{"id":"4","uid":"339839","hot":"","orginid":"30","orgintable":"amuse_images1","height":"200","width":"300","title":"标题","dateline":"1527215999","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/2cf01891759faaaa0dfc19846bdce8a4.jpeg","view":"","user":{"header":"http://dtds.oss-cn-hangzhou.aliyuncs.com/f8e8334a.jpg","username":"ab33333","uid":"339839"}}],"count":"1","orginid":"30","orgintable":"amuse_images1"}
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
         * orgin_url : http://dtds.oss-cn-hangzhou.aliyuncs.com/2cf01891759faaaa0dfc19846bdce8a4.jpeg
         * data : [{"id":"4","uid":"339839","hot":"","orginid":"30","orgintable":"amuse_images1","height":"200","width":"300","title":"标题","dateline":"1527215999","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/2cf01891759faaaa0dfc19846bdce8a4.jpeg","view":"","user":{"header":"http://dtds.oss-cn-hangzhou.aliyuncs.com/f8e8334a.jpg","username":"ab33333","uid":"339839"}}]
         * count : 1
         * orginid : 30
         * orgintable : amuse_images1
         */

        private String orgin_url;
        private String count;
        private String orginid;
        private String orgintable;
        private List<DataBean> data;

        public String getOrgin_url() {
            return orgin_url;
        }

        public void setOrgin_url(String orgin_url) {
            this.orgin_url = orgin_url;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
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

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 4
             * uid : 339839
             * hot :
             * orginid : 30
             * orgintable : amuse_images1
             * height : 200
             * width : 300
             * title : 标题
             * dateline : 1527215999
             * url : http://dtds.oss-cn-hangzhou.aliyuncs.com/2cf01891759faaaa0dfc19846bdce8a4.jpeg
             * view :
             * user : {"header":"http://dtds.oss-cn-hangzhou.aliyuncs.com/f8e8334a.jpg","username":"ab33333","uid":"339839"}
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
            private String url;
            private String view;
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

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getView() {
                return view;
            }

            public void setView(String view) {
                this.view = view;
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
                 * username : ab33333
                 * uid : 339839
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
}
