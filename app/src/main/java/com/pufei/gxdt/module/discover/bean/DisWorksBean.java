package com.pufei.gxdt.module.discover.bean;

import java.util.List;

public class DisWorksBean {
    /**
     * code : 0
     * msg : success
     * result : {"user":{"username":"我在这，你在哪？","header":"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83erll5lJF53eNkG4jlgSzViaBYDB1FFobZCQUh4MJxDUPcmCLokTYPCaVic0aZHWbtFlugNFXOy235hw/132","gender":"男","mind":"","total_score":"5"},"product":[{"id":"1","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/3ed39014.jpg","orginid":"1","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"2","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/afabc637.jpg","orginid":"2","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"3","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/a849df17.jpg","orginid":"3","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"4","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/dfcd5abb.jpg","orginid":"4","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"5","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/c85bb1c4.jpg","orginid":"5","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"6","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/78d1f350.jpg","orginid":"6","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"7","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/cd5243bb.jpg","orginid":"7","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"8","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/66913efd.jpg","orginid":"8","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"9","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/004eb195.jpg","orginid":"9","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"10","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/f7404359.jpg","orginid":"10","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"11","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/79c29372.jpg","orginid":"11","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"14","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/86c35685.jpg","orginid":"14","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"20","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/1706bb89.jpg","orginid":"20","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"21","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/1a2e8f82.jpg","orginid":"21","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"22","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/ba65e9e7.jpg","orginid":"22","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"23","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/2bd9ba4f.jpg","orginid":"23","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"24","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/9356409b.jpg","orginid":"24","orgintable":"design_images","uid":"346979","isSaveImg":"0"}]}
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
         * user : {"username":"我在这，你在哪？","header":"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83erll5lJF53eNkG4jlgSzViaBYDB1FFobZCQUh4MJxDUPcmCLokTYPCaVic0aZHWbtFlugNFXOy235hw/132","gender":"男","mind":"","total_score":"5"}
         * product : [{"id":"1","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/3ed39014.jpg","orginid":"1","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"2","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/afabc637.jpg","orginid":"2","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"3","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/a849df17.jpg","orginid":"3","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"4","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/dfcd5abb.jpg","orginid":"4","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"5","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/c85bb1c4.jpg","orginid":"5","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"6","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/78d1f350.jpg","orginid":"6","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"7","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/cd5243bb.jpg","orginid":"7","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"8","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/66913efd.jpg","orginid":"8","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"9","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/004eb195.jpg","orginid":"9","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"10","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/f7404359.jpg","orginid":"10","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"11","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/79c29372.jpg","orginid":"11","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"14","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/86c35685.jpg","orginid":"14","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"20","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/1706bb89.jpg","orginid":"20","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"21","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/1a2e8f82.jpg","orginid":"21","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"22","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/ba65e9e7.jpg","orginid":"22","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"23","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/2bd9ba4f.jpg","orginid":"23","orgintable":"design_images","uid":"346979","isSaveImg":"0"},{"id":"24","url":"http://dtds.oss-cn-hangzhou.aliyuncs.com/9356409b.jpg","orginid":"24","orgintable":"design_images","uid":"346979","isSaveImg":"0"}]
         */

        private UserBean user;
        private List<ProductBean> product;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public List<ProductBean> getProduct() {
            return product;
        }

        public void setProduct(List<ProductBean> product) {
            this.product = product;
        }

        public static class UserBean {
            /**
             * username : 我在这，你在哪？
             * header : http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83erll5lJF53eNkG4jlgSzViaBYDB1FFobZCQUh4MJxDUPcmCLokTYPCaVic0aZHWbtFlugNFXOy235hw/132
             * gender : 男
             * mind :
             * total_score : 5
             */

            private String username;
            private String header;
            private String gender;
            private String mind;
            private String total_score;

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getHeader() {
                return header;
            }

            public void setHeader(String header) {
                this.header = header;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getMind() {
                return mind;
            }

            public void setMind(String mind) {
                this.mind = mind;
            }

            public String getTotal_score() {
                return total_score;
            }

            public void setTotal_score(String total_score) {
                this.total_score = total_score;
            }
        }

        public static class ProductBean {
            /**
             * id : 1
             * url : http://dtds.oss-cn-hangzhou.aliyuncs.com/3ed39014.jpg
             * orginid : 1
             * orgintable : design_images
             * uid : 346979
             * isSaveImg : 0
             */

            private String id;
            private String url;
            private String orginid;
            private String orgintable;
            private String uid;
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

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getIsSaveImg() {
                return isSaveImg;
            }

            public void setIsSaveImg(String isSaveImg) {
                this.isSaveImg = isSaveImg;
            }
        }
    }
}
