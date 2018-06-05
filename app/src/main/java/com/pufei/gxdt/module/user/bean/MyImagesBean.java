package com.pufei.gxdt.module.user.bean;

import java.util.List;

public class MyImagesBean {

    /**
     * code : 0
     * msg : success
     * result : [{"id":"664646","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/093021000217985.png"},{"id":"4","category_name":"宠物","view":"","hot":"","imgs":[{"id":"4970","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/134743000227990.gif","category_id":"4","dateline":"1527674422","view":"","hot":""},{"id":"4748","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/005818000259621.png","category_id":"4","dateline":"1527652822","view":"","hot":""},{"id":"4743","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/105922000172436.png","category_id":"4","dateline":"1527652820","view":"","hot":""}]},{"id":"3","category_name":"萌宝","view":"","hot":"","imgs":[{"id":"5432","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/052231000259621.gif","category_id":"3","dateline":"1527717612","view":"","hot":""},{"id":"5431","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/052453000259621.gif","category_id":"3","dateline":"1527717612","view":"","hot":""},{"id":"5430","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/052707000259621.gif","category_id":"3","dateline":"1527717612","view":"","hot":""}]},{"id":"2","category_name":"搞笑","view":"","hot":"","imgs":[{"id":"5412","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/052231000259621.gif","category_id":"2","dateline":"1527717610","view":"","hot":""},{"id":"5411","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/052453000259621.gif","category_id":"2","dateline":"1527717610","view":"","hot":""},{"id":"5410","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/052707000259621.gif","category_id":"2","dateline":"1527717610","view":"","hot":""}]}]
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
         * id : 664646
         * url : http://dtds.oss-cn-hangzhou.aliyuncs.com/093021000217985.png
         * category_name : 宠物
         * view :
         * hot :
         * imgs : [{"id":"4970","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/134743000227990.gif","category_id":"4","dateline":"1527674422","view":"","hot":""},{"id":"4748","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/005818000259621.png","category_id":"4","dateline":"1527652822","view":"","hot":""},{"id":"4743","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/105922000172436.png","category_id":"4","dateline":"1527652820","view":"","hot":""}]
         */

        private String id;
        private String url;
        private String category_name;
        private String view;
        private String hot;
        private List<ImgsBean> imgs;

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

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
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

        public List<ImgsBean> getImgs() {
            return imgs;
        }

        public void setImgs(List<ImgsBean> imgs) {
            this.imgs = imgs;
        }

        public static class ImgsBean {
            /**
             * id : 4970
             * url : http://dtds.oss-cn-hangzhou.aliyuncs.com/134743000227990.gif
             * category_id : 4
             * dateline : 1527674422
             * view :
             * hot :
             */

            private String id;
            private String url;
            private String category_id;
            private String dateline;
            private String view;
            private String hot;

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
        }
    }
}
