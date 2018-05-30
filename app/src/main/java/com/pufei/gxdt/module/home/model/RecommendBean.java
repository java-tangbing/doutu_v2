package com.pufei.gxdt.module.home.model;

import java.util.List;

/**
 * Created by tb on 2018/5/30.
 */

public class RecommendBean {
    /**
     * code : 0
     * msg : success
     * result : [{"id":"93","category_name":"斗图大会堂","imgs":[{"id":"131827","url":"http://ojco34ciy.bkt.clouddn.com/f494069d13b27810e420b48bcd5161b1.jpeg","category_id":"93","dateline":"1490029733"},{"id":"131826","url":"http://ojco34ciy.bkt.clouddn.com/dbaae0acc7842691369229fd95d31a93.png","category_id":"93","dateline":"1490029733"},{"id":"131825","url":"http://ojco34ciy.bkt.clouddn.com/45ab26602bd4492e0b1717359113dfef.png","category_id":"93","dateline":"1490029733"}]},{"id":"110","category_name":"组图","imgs":[{"id":"14040","url":"http://ojco34ciy.bkt.clouddn.com/1692a08b9e23a4ec0367b82ea9f663ac.jpeg","category_id":"110","dateline":"1486519905"},{"id":"14039","url":"http://ojco34ciy.bkt.clouddn.com/fbc9269b120f5d5cce0d2d7251870943.jpeg","category_id":"110","dateline":"1486519905"},{"id":"14038","url":"http://ojco34ciy.bkt.clouddn.com/6b9a2f08f70efebf26f51e0b3f7cb924.jpeg","category_id":"110","dateline":"1486519905"}]},{"id":"114","category_name":"一波万能表情包","imgs":[{"id":"141359","url":"http://ojco34ciy.bkt.clouddn.com/ce6dd6866c1a2530f009106c536e07e7.png","category_id":"114","dateline":"1490579319"},{"id":"141358","url":"http://ojco34ciy.bkt.clouddn.com/9d9c37949b3441657942b9d6206e265a.jpeg","category_id":"114","dateline":"1490579319"},{"id":"141357","url":"http://ojco34ciy.bkt.clouddn.com/2f23639833ba7b4fed05c4e2fd8d04c7.jpeg","category_id":"114","dateline":"1490579319"}]},{"id":"162","category_name":"好图","imgs":[{"id":"54540","url":"http://ojco34ciy.bkt.clouddn.com/6d5df6566201a96ff63401bb7c0e0fd3.png","category_id":"162","dateline":"1486964170"},{"id":"54539","url":"http://ojco34ciy.bkt.clouddn.com/5f5a3574b6859513695c15a656a03ea9.png","category_id":"162","dateline":"1486964170"},{"id":"54538","url":"http://ojco34ciy.bkt.clouddn.com/64cbc133425fcbf8ca81c77ac954de21.png","category_id":"162","dateline":"1486964170"}]}]
     */

    private String code;
    private String msg;
    /**
     * id : 93
     * category_name : 斗图大会堂
     * imgs : [{"id":"131827","url":"http://ojco34ciy.bkt.clouddn.com/f494069d13b27810e420b48bcd5161b1.jpeg","category_id":"93","dateline":"1490029733"},{"id":"131826","url":"http://ojco34ciy.bkt.clouddn.com/dbaae0acc7842691369229fd95d31a93.png","category_id":"93","dateline":"1490029733"},{"id":"131825","url":"http://ojco34ciy.bkt.clouddn.com/45ab26602bd4492e0b1717359113dfef.png","category_id":"93","dateline":"1490029733"}]
     */

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
        private String source;
        private int type;
        private String id;
        private String category_name;
        private String dateline;
        private String advert_url;
        private String advert_image_url;
        private String down_url;


        /**

         * id : 131827
         * url : http://ojco34ciy.bkt.clouddn.com/f494069d13b27810e420b48bcd5161b1.jpeg
         * category_id : 93
         * dateline : 1490029733
         */

        private List<ImgsBean> imgs;

        public String getAdvert_url() {
            return advert_url;
        }

        public void setAdvert_url(String advert_url) {
            this.advert_url = advert_url;
        }

        public String getAdvert_image_url() {
            return advert_image_url;
        }

        public void setAdvert_image_url(String advert_image_url) {
            this.advert_image_url = advert_image_url;
        }

        public String getDown_url() {
            return down_url;
        }

        public void setDown_url(String down_url) {
            this.down_url = down_url;
        }
        public String getDateline() {
            return dateline;
        }

        public void setDateline(String dateline) {
            this.dateline = dateline;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

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

        public List<ImgsBean> getImgs() {
            return imgs;
        }

        public void setImgs(List<ImgsBean> imgs) {
            this.imgs = imgs;
        }

        public static class ImgsBean {
            private String id;
            private String url;
            private String category_id;
            private String dateline;

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
        }
    }
}
